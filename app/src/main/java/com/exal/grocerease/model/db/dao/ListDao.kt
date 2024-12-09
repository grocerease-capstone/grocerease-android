package com.exal.grocerease.model.db.dao

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.exal.grocerease.model.db.entities.ListEntity

@Dao
interface ListDao {
    @Query("SELECT * FROM list_table WHERE type = :type ORDER BY createdAt DESC")
    fun getListsByType(type: String): PagingSource<Int, ListEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(lists: List<ListEntity>)

    @Query("DELETE FROM list_table WHERE type = :type")
    suspend fun clearByType(type: String)

    @Query("SELECT SUM(CAST(totalExpenses AS INT)) FROM list_table WHERE type = :type")
    suspend fun getTotalExpensesByType(type: String): Int?

    @Query("SELECT SUM(totalItems) FROM list_table WHERE type = :type")
    suspend fun getTotalItemsByType(type: String): Int?

    @Query("SELECT * FROM list_table WHERE type = :type ORDER BY createdAt DESC LIMIT 5")
    fun getFiveLatestData(type: String): PagingSource<Int, ListEntity>

    @Query("DELETE FROM list_table")
    suspend fun clearAll()

    @Query("SELECT COUNT(*) FROM list_table WHERE type = :type")
    suspend fun getCountByType(type: String): Int
}