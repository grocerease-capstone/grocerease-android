package com.exal.grocerease.model.network.retrofit

import com.exal.testapp.data.network.response.ExpenseListResponseItem
import com.exal.testapp.data.network.response.GetListResponse
import com.exal.testapp.data.network.response.LoginResponse
import com.exal.testapp.data.network.response.LogoutResponse
import com.exal.testapp.data.network.response.PostListResponse
import com.exal.testapp.data.network.response.RegisterResponse
import com.exal.testapp.data.network.response.ResultListResponseItem
import com.exal.testapp.data.network.response.ScanImageResponse
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.Query

interface ApiServices {
    @GET("/expense-lists")
    suspend fun getExpensesList(@Query("id_user") id: String): List<ExpenseListResponseItem>

    @GET("/expense-detail")
    suspend fun getResultList(@Query("list_id") id: String): List<ResultListResponseItem>

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

    @Multipart
    @POST("/v1/receipt")
    suspend fun scanImage(
        @Part file: MultipartBody.Part
    ): ScanImageResponse

    @Multipart
    @POST("/list")
    suspend fun postData(
        @Header("Authorization") token: String,
        @Part("title") title: RequestBody,
        @Part receiptImage: MultipartBody.Part?,
        @Part thumbnailImage: MultipartBody.Part?,
        @Part("product_items") productItems: RequestBody,
        @Part("type") type: RequestBody,
        @Part("total_expenses") totalExpenses: RequestBody,
        @Part("total_items") totalItems: RequestBody
    ): PostListResponse

    @GET("/list")
    suspend fun getExpenseList(
        @Header("Authorization") token: String,
        @Query("type") type: String = "Track"
    ): GetListResponse
}