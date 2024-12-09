package com.exal.grocerease.model.network.request

data class ProductItem(
    val id: Int?,
    val name: String?,
    val amount: Int?,
    val price: Int?,
    val category: String?,
    val total_price: Int?
)
