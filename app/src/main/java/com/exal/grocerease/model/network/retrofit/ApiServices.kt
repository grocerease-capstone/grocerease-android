package com.exal.grocerease.model.network.retrofit

import com.exal.testapp.data.network.response.ExpenseListResponseItem
import com.exal.testapp.data.network.response.LoginResponse
import com.exal.testapp.data.network.response.LogoutResponse
import com.exal.testapp.data.network.response.RegisterResponse
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.Query

interface ApiServices {
    @GET("/expense-lists")
    suspend fun getExpensesList(@Query("id_user") id: String): List<ExpenseListResponseItem>

    @FormUrlEncoded
    @POST("/auth/login")
    suspend fun login(
        @Field("email") email: String,
        @Field("password") password: String
    ): LoginResponse

    @FormUrlEncoded
    @POST("/auth/register")
    suspend fun register(
        @Field("username") name: String,
        @Field("email") email: String,
        @Field("password") password: String,
        @Field("passwordRepeat") passwordRepeat: String
    ): RegisterResponse

    @POST("/auth/logout")
    suspend fun logout(
        @Header("Authorization") token: String
    ): LogoutResponse
}