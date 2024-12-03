package com.exal.grocerease.hilt.modules

import com.exal.grocerease.hilt.helper.MlApiService
import com.exal.grocerease.hilt.helper.RegularApiService
import com.exal.grocerease.model.network.retrofit.ApiConfig
import com.exal.grocerease.model.network.retrofit.ApiServices
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
object ApiModule {
    @Provides
    fun provideApiConfig(): ApiConfig {
        return ApiConfig()
    }

    @Provides
    @RegularApiService
    fun provideApiServices(apiConfig: ApiConfig): ApiServices {
        return apiConfig.getApiService()
    }

    @Provides
    @MlApiService
    fun provideApiServicesML(apiConfig: ApiConfig): ApiServices {
        return apiConfig.getMLApiService()
    }
}