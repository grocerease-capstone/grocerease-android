package com.exal.grocerease.model.network.response

data class ProductItem(
    val name: String?,
    val amount: Int?,
    val price: Int?,
    val category: String?,
    val total_price: Int?
)