package com.exal.grocerease.model.remotemediator

import android.util.Log
import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import androidx.room.withTransaction
import com.exal.grocerease.model.db.AppDatabase
import com.exal.grocerease.model.db.entities.ListEntity
import com.exal.grocerease.model.db.entities.RemoteKeys
import com.exal.grocerease.model.network.retrofit.ApiServices
import kotlinx.coroutines.flow.MutableSharedFlow

@OptIn(ExperimentalPagingApi::class)
class FilterRemoteMediator(
    private val type: String,
    private val token: String,
    private val month: Int,
    private val year: Int,
    private val apiService: ApiServices,
    private val database: AppDatabase,
    private val toastEvent: MutableSharedFlow<String>
) : RemoteMediator<Int, ListEntity>() {

    override suspend fun initialize(): InitializeAction {
        return InitializeAction.LAUNCH_INITIAL_REFRESH
    }

    private companion object {
        const val INITIAL_PAGE_INDEX = 1
    }

    override suspend fun load(
        loadType: LoadType,
        state: PagingState<Int, ListEntity>
    ): MediatorResult {
        val page = when (loadType) {
            LoadType.REFRESH -> {
                val remoteKeys = getRemoteKeyClosestToCurrentPosition(state)
                remoteKeys?.prevKey?.plus(1) ?: INITIAL_PAGE_INDEX
            }

            LoadType.PREPEND -> {
                val remoteKeys = getRemoteKeyForFirstItem(state)
                val prevKey = remoteKeys?.prevKey
                    ?: return MediatorResult.Success(endOfPaginationReached = remoteKeys != null)
                prevKey
            }

            LoadType.APPEND -> {
                val remoteKeys = getRemoteKeyForLastItem(state)
                val nextKey = remoteKeys?.nextKey
                    ?: return MediatorResult.Success(endOfPaginationReached = remoteKeys != null)
                nextKey
            }
        }

        try {
            val responseData = apiService.getExpensesMonth("Bearer: $token", type, month, year, page, state.config.pageSize)

            if (responseData.message == "No lists found for this user") {
                toastEvent.emit("No lists found for this Month/Year")
                return MediatorResult.Success(endOfPaginationReached = true)
            }

            val endOfPaginationReached = page >= responseData.pagination?.totalPages!!
            Log.d("End of pagination", "End of pagination reached: $endOfPaginationReached")

            val lists = responseData.data?.lists?.mapNotNull { list ->
                Log.d("RemoteMediator", "Processing item: $list")
                list?.let {
                    ListEntity(
                        id = it.id?.toString()
                            ?: throw IllegalArgumentException("ID is required"),
                        title = it.title,
                        type = type,
                        totalExpenses = it.totalExpenses,
                        totalProducts = it.totalProducts,
                        totalItems = it.totalItems,
                        createdAt = it.createdAt,
                        boughtAt = it.boughtAt,
                        image = it.image
                    )
                }
            }

            database.withTransaction {
                if (loadType == LoadType.REFRESH) {
                    database.remoteKeysDao().clearRemoteKeys()
                    database.listDao().clearByType(type)
                }

                val prevKey = if (page == INITIAL_PAGE_INDEX) null else page - 1
                val nextKey = if (endOfPaginationReached) null else page + 1

                val keys = responseData.data?.lists?.map {
                    RemoteKeys(
                        id = it?.id.toString(),
                        prevKey = prevKey,
                        nextKey = nextKey
                    )
                }
                keys?.let { database.remoteKeysDao().insertAll(it) }
                Log.d("List Size", lists?.size.toString())
                lists?.let { database.listDao().insertAll(it) }
            }

            return MediatorResult.Success(endOfPaginationReached = endOfPaginationReached)
        } catch (e: Exception) {
            return MediatorResult.Error(e)
        }
    }


    private suspend fun getRemoteKeyForLastItem(state: PagingState<Int, ListEntity>): RemoteKeys? {
        return state.pages.lastOrNull()  { it.data.isNotEmpty() }?.data?.lastOrNull()?.let { data ->
            Log.d("statePages Last", state.pages.toString())
            Log.d("data Last", data.toString())
            database.remoteKeysDao().getRemoteKeysById(data.id)
        }
    }

    private suspend fun getRemoteKeyForFirstItem(state: PagingState<Int, ListEntity>): RemoteKeys? {
        return state.pages.firstOrNull() { it.data.isNotEmpty() }?.data?.firstOrNull()?.let { data ->
            Log.d("statePages First", state.pages.toString())
            Log.d("data First", data.toString())
            database.remoteKeysDao().getRemoteKeysById(data.id)
        }
    }

    private suspend fun getRemoteKeyClosestToCurrentPosition(state: PagingState<Int, ListEntity>): RemoteKeys? {
        return state.anchorPosition?.let { anchorPosition ->
            state.closestItemToPosition(anchorPosition)?.id?.let { itemId ->
                database.remoteKeysDao().getRemoteKeysById(itemId)
            }
        }
    }
}