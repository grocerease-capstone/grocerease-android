package com.exal.grocerease.model.network.response

import com.google.gson.annotations.SerializedName

data class GetProfileResponse(

	@field:SerializedName("code")
	val code: Int? = null,

	@field:SerializedName("data")
	val data: Data6? = null,

	@field:SerializedName("message")
	val message: String? = null,

	@field:SerializedName("status")
	val status: Boolean? = null
)

data class ListsItem2(

	@field:SerializedName("boughtAt")
	val boughtAt: String? = null,

	@field:SerializedName("total_expenses")
	val totalExpenses: String? = null,

	@field:SerializedName("total_products")
	val totalProducts: Int? = null,

	@field:SerializedName("total_items")
	val totalItems: Int? = null
)

data class UserProfile(

	@field:SerializedName("image")
	val image: String? = null,

	@field:SerializedName("email")
	val email: String? = null,

	@field:SerializedName("username")
	val username: String? = null
)

data class Data6(

	@field:SerializedName("lists")
	val lists: List<ListsItem2?>? = null,

	@field:SerializedName("userProfile")
	val userProfile: UserProfile? = null
)
