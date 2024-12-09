package com.exal.grocerease.model.network.response

import com.google.gson.annotations.SerializedName

data class GetListResponse(

	@field:SerializedName("code")
	val code: Int? = null,

	@field:SerializedName("pagination")
	val pagination: Pagination? = null,

	@field:SerializedName("data")
	val data: Data? = null,

	@field:SerializedName("message")
	val message: String? = null,

	@field:SerializedName("status")
	val status: Boolean? = null
)

data class ListsItem(

	@field:SerializedName("createdAt")
	val createdAt: String? = null,

	@field:SerializedName("image")
	val image: String? = null,

	@field:SerializedName("id")
	val id: Int? = null,

	@field:SerializedName("title")
	val title: String? = null,

	@field:SerializedName("type")
	val type: String? = null,

	@field:SerializedName("total_expenses")
	val totalExpenses: String? = null,

	@field:SerializedName("total_products")
	val totalProducts: Int? = null,

	@field:SerializedName("boughtAt")
	val boughtAt: String? = null,

	@field:SerializedName("total_items")
	val totalItems: Int? = null
)

data class Pagination(

	@field:SerializedName("total")
	val total: Int? = null,

	@field:SerializedName("limit")
	val limit: Int? = null,

	@field:SerializedName("totalPages")
	val totalPages: Int? = null,

	@field:SerializedName("page")
	val page: Int? = null
)

data class Data(

	@field:SerializedName("lists")
	val lists: List<ListsItem?>? = null
)

