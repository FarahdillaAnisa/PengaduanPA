package com.icha.layananpengaduanpa.model

import com.google.gson.annotations.SerializedName

data class SpktModel(
	@field:SerializedName("id_spkt")
	val idSpkt: String? = null,

	@field:SerializedName("pass_spkt")
	val passSpkt: String? = null,

	@field:SerializedName("pass_awal")
	val pass_awal: String,

	@field:SerializedName("notelp_spkt")
	val notelpSpkt: String? = null,

	@field:SerializedName("uname_spkt")
	val unameSpkt: String? = null,

	@field:SerializedName("satuan_wilayah")
	val satuanWilayah: String? = null
)
