package com.exal.grocerease.model.network.request


data class ProductItemPost(
    val name: String?,
    val amount: Int?,
    val price: Int?,
    val category: String?,
    val total_price: Int?
)