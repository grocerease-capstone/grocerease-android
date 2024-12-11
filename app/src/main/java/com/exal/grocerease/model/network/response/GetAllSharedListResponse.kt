package com.exal.grocerease.model.network.response

import com.google.gson.annotations.SerializedName

data class GetAllSharedListResponse(

	@field:SerializedName("code")
	val code: Int? = null,

	@field:SerializedName("data")
	val data: Data5? = null,

	@field:SerializedName("message")
	val message: String? = null,

	@field:SerializedName("status")
	val status: Boolean? = null
)

data class Data5(

	@field:SerializedName("allDetailList")
	val allDetailList: List<AllDetailListItem?>? = null
)

data class AllDetailListItem(

	@field:SerializedName("image")
	val image: String? = null,

	@field:SerializedName("boughtAt")
	val boughtAt: String? = null,

	@field:SerializedName("id")
	val id: Int? = null,

	@field:SerializedName("title")
	val title: String? = null,

	@field:SerializedName("type")
	val type: String? = null,

	@field:SerializedName("total_expenses")
	val totalExpenses: String? = null,

	@field:SerializedName("sender")
	val sender: String? = null,

	@field:SerializedName("total_products")
	val totalProducts: Int? = null,

	@field:SerializedName("total_items")
	val totalItems: Int? = null
)
