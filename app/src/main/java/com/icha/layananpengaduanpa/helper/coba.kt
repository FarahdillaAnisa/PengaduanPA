package com.icha.layananpengaduanpa.helper

import com.icha.layananpengaduanpa.model.AduanCount
import com.icha.layananpengaduanpa.model.ApiConfig
import com.icha.layananpengaduanpa.model.ResponsePengaduan
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

fun main() {
    println(getCurrentDate())
    println(displayDate("2022-10-7"))

    ApiConfig.instance.countAllAduan("proses")
            .enqueue(object : Callback<AduanCount> {
                override fun onResponse(call: Call<AduanCount>, response: Response<AduanCount>) {
                    if (response.isSuccessful) {
                        val data = response.body()?.data
                        println(data)

                        val jumlah = response.body()?.jumlah
                        println(jumlah)
                    }
                }

                override fun onFailure(call: Call<AduanCount>, t: Throwable) {
                    println(t.message)
                }

            })

    ApiConfig.instance.getAllAduan("proses")
            .enqueue(object : Callback<ArrayList<ResponsePengaduan>> {
                override fun onResponse(call: Call<ArrayList<ResponsePengaduan>>, response: Response<ArrayList<ResponsePengaduan>>) {
                    if (response.isSuccessful) {
                        val data = response.body()
                        println(data)
                    }
                }

                override fun onFailure(call: Call<ArrayList<ResponsePengaduan>>, t: Throwable) {
                    println(t.message)
                }

            })
}

fun getCurrentDate(): String {
    val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
    val date = Date()
    return dateFormat.format(date)
}

fun displayDate(date: String): String {
    val inputFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
    val dateFormat = SimpleDateFormat("dd-MMMM-yyyy", Locale.getDefault())
    val dateInput = inputFormat.parse(date)
    return dateFormat.format(dateInput)
}