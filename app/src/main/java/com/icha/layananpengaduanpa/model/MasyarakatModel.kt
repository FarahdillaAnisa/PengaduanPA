package com.icha.layananpengaduanpa.model

import com.google.gson.annotations.SerializedName

data class MasyarakatModel(
	@field:SerializedName("id_msy")
	val idMsy: Int,

	@field:SerializedName("nama_msy")
	val namaMsy: String,

	@field:SerializedName("notelp_msy")
	val notelpMsy: String,

	@field:SerializedName("uname_msy")
	val unameMsy: String,

	@field:SerializedName("pass_msy")
	val passMsy: String,

	@field:SerializedName("is_active")
	val isActive: String? = null,

	@field:SerializedName("date_created")
	val dateCreated: String
)
