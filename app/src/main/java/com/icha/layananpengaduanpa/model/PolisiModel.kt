package com.icha.layananpengaduanpa.model

import com.google.gson.annotations.SerializedName

data class PolisiModel(
		@field:SerializedName("id_polisi")
		val idPolisi: String,

		@field:SerializedName("nama_polisi")
		val namaPolisi: String,

		@field:SerializedName("satuan_wilayah")
		val satuanWilayah: String,

		@field:SerializedName("notelp_polisi")
		val notelpPolisi: String,

		@field:SerializedName("pass_polisi")
		val passPolisi: String
)






