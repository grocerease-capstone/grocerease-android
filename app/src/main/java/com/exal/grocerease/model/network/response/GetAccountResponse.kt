package com.exal.grocerease.model.network.response

import com.google.gson.annotations.SerializedName

data class GetAccountResponse(

	@field:SerializedName("code")
	val code: Int,

	@field:SerializedName("data")
	val data: Data3,

	@field:SerializedName("message")
	val message: String,

	@field:SerializedName("status")
	val status: Boolean
)

data class UserProfile(

	@field:SerializedName("image")
	val image: String,

	@field:SerializedName("email")
	val email: String,

	@field:SerializedName("username")
	val username: String
)

data class Data3(

	@field:SerializedName("userProfile")
	val userProfile: UserProfile
)
