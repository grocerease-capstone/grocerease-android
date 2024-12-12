package com.exal.grocerease.model.network.response

import com.google.gson.annotations.SerializedName

data class DetailListResponse(

    @field:SerializedName("code")
    val code: Int? = null,

    @field:SerializedName("data")
    val data: Data2? = null,

    @field:SerializedName("message")
    val message: String? = null,

    @field:SerializedName("status")
    val status: Boolean? = null
)

data class ProductItemsItem(

    @field:SerializedName("amount")
    val amount: Int? = null,

    @field:SerializedName("totalPrice")
    val totalPrice: String? = null,

    @field:SerializedName("price")
    val price: String? = null,

    @field:SerializedName("name")
    val name: String? = null,

    @field:SerializedName("id")
    val id: Int? = null,

    @field:SerializedName("category")
    val category: String? = null
)

data class DetailList(

    @field:SerializedName("boughtAt")
    val boughtAt: String? = null,

    @field:SerializedName("receiptImage")
    val receiptImage: String? = null,

    @field:SerializedName("title")
    val title: String? = null,

    @field:SerializedName("thumbnailImage")
    val thumbnailImage: String? = null,

    @field:SerializedName("Product_Items")
    val productItems: List<ProductItemsItem?>? = null
)

data class Data2(

    @field:SerializedName("detailList")
    val detailList: DetailList? = null
)

