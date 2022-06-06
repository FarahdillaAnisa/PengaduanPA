package com.icha.layananpengaduanpa.model

import com.google.gson.annotations.SerializedName

data class OperatorModel(

	@field:SerializedName("uname_operator")
	val unameOperator: String? = null,

	@field:SerializedName("id_operator")
	val idOperator: String? = null,

	@field:SerializedName("pass_operator")
	val passOperator: String? = null
)
