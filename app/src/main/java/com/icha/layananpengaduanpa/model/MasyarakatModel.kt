package com.icha.layananpengaduanpa.model

import com.google.gson.annotations.SerializedName

data class MasyarakatModel(
	@field:SerializedName("id_msy")
	val idMsy: String? = null,

	@field:SerializedName("nama_msy")
	val namaMsy: String? = null,

	@field:SerializedName("notelp_msy")
	val notelpMsy: String? = null,

	@field:SerializedName("uname_msy")
	val unameMsy: String? = null,

	@field:SerializedName("pass_msy")
	val passMsy: String? = null,

	@field:SerializedName("is_active")
	val isActive: String? = null,

	@field:SerializedName("date_created")
	val dateCreated: String? = null
)
