package com.icha.layananpengaduanpa.model

import com.google.gson.annotations.SerializedName

data class MasyarakatModel(
	@field:SerializedName("id_msy")
	val idMsy: String,

	@field:SerializedName("nama_msy")
	val namaMsy: String,

	@field:SerializedName("notelp_msy")
	val notelpMsy: String,

	@field:SerializedName("pass_msy")
	val passMsy: String
)