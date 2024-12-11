package com.exal.grocerease.model.network.response

import com.google.gson.annotations.SerializedName

data class GetSharedListResponse(

	@field:SerializedName("code")
	val code: Int? = null,

	@field:SerializedName("data")
	val data: Data4? = null,

	@field:SerializedName("message")
	val message: String? = null,

	@field:SerializedName("status")
	val status: Boolean? = null
)

data class RequestDetailItem(

	@field:SerializedName("id")
	val id: Int? = null,

	@field:SerializedName("title")
	val title: String? = null,

	@field:SerializedName("username")
	val username: String? = null
)

data class Data4(

	@field:SerializedName("requestDetail")
	val requestDetail: List<RequestDetailItem?>? = null
)
