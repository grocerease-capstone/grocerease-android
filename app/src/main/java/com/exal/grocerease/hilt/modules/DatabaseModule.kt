package com.exal.grocerease.hilt.modules

import android.content.Context
import com.exal.grocerease.model.db.AppDatabase
import com.exal.grocerease.model.db.dao.ListDao
import com.exal.grocerease.model.db.dao.RemoteKeysDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context): AppDatabase {
        return AppDatabase.getDatabase(context)
    }

    @Provides
    fun provideListDao(database: AppDatabase): ListDao {
        return database.listDao()
    }

    @Provides
    fun provideRemoteKeysDao(database: AppDatabase): RemoteKeysDao {
        return database.remoteKeysDao()
    }
}