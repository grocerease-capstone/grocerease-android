package com.exal.grocerease.model.network.retrofit

import com.exal.grocerease.model.network.response.DetailListResponse
import com.exal.grocerease.model.network.response.ExpenseListResponseItem
import com.exal.grocerease.model.network.response.GetAllSharedListResponse
import com.exal.grocerease.model.network.response.GetListResponse
import com.exal.grocerease.model.network.response.GetProfileResponse
import com.exal.grocerease.model.network.response.GetSharedListResponse
import com.exal.grocerease.model.network.response.LoginResponse
import com.exal.grocerease.model.network.response.LogoutResponse
import com.exal.grocerease.model.network.response.PostListResponse
import com.exal.grocerease.model.network.response.RegisterResponse
import com.exal.grocerease.model.network.response.ResultListResponseItem
import com.exal.grocerease.model.network.response.ScanImageResponse
import com.exal.grocerease.model.network.response.UpdateListResponse
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.http.DELETE
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Part
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiServices {
    @FormUrlEncoded
    @POST("/auth/login")
    suspend fun login(
        @Field("email") email: String,
        @Field("password") password: String
    ): LoginResponse

    @Multipart
    @POST("/auth/register")
    suspend fun register(
        @Part("username") name: RequestBody,
        @Part("email") email: RequestBody,
        @Part("password") password: RequestBody,
        @Part("password_repeat") passwordRepeat: RequestBody,
        @Part profileImage: MultipartBody.Part
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
        @Part("total_items") totalItems: RequestBody,
        @Part("boughtAt") boughtAt: RequestBody
    ): PostListResponse

    @GET("/list")
    suspend fun getExpenseList(
        @Header("Authorization") token: String,
        @Query("type") type: String,
        @Query("page") page: Int = 1,
        @Query("limit") limit: Int = 10
    ): GetListResponse

    @GET("/list/{id}")
    suspend fun getExpensesDetail(
        @Header("Authorization") token: String,
        @Path("id") id: Int
    ): DetailListResponse

    @GET("/list/filter")
    suspend fun getExpensesMonth(
        @Header("Authorization") token: String,
        @Query("type") type: String = "Track",
        @Query("month") month: Int,
        @Query("year") year: Int,
        @Query("page") page: Int = 1,
        @Query("limit") limit: Int = 10
    ): GetListResponse

    @Multipart
    @PUT("/list/{id}")
    suspend fun updateList(
        @Header("Authorization") token: String,
        @Path("id") id: Int,
        @Part("title") title: RequestBody,
        @Part("product_items") productItems: RequestBody,
        @Part("type") type: RequestBody,
        @Part("total_expenses") totalExpenses: RequestBody,
        @Part("total_items") totalItems: RequestBody
    ): UpdateListResponse

    @DELETE("/list/{id}")
    suspend fun deleteExpense(
        @Header("Authorization") token: String,
        @Path("id") id: Int
    ): PostListResponse

    @GET("/user")
    suspend fun getAccount(
        @Header("Authorization") token: String
    ): GetProfileResponse

    @Multipart
    @PUT("/user")
    suspend fun updateAccount(
        @Header("Authorization") token: String,
        @Part profileImage: MultipartBody.Part?,
        @Part("username") username: RequestBody?,
        @Part("password") password: RequestBody?,
        @Part("new_password") passwordRepeat: RequestBody?
    ): UpdateListResponse

    @FormUrlEncoded
    @POST("/share-request/{id}")
    suspend fun shareList(
        @Header("Authorization") token: String,
        @Path("id") id: Int,
        @Field("email") email: String
    ): PostListResponse

    @GET("/share-request")
    suspend fun getSharedList(
        @Header("Authorization") token: String
    ): GetSharedListResponse

    @GET("/share-request/{id}")
    suspend fun acceptNotification(
        @Header("Authorization") token: String,
        @Path("id") id: Int
    ): UpdateListResponse

    @DELETE("/share-request/{id}")
    suspend fun declineNotification(
        @Header("Authorization") token: String,
        @Path("id") id: Int
    ): UpdateListResponse

    @GET("/list/shared")
    suspend fun getAllSharedList(
        @Header("Authorization") token: String
    ): GetAllSharedListResponse
}
