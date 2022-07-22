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

var jumlahAduan : Int = 0

fun main() {

    println(getCurrentDate())
    println(displayDate("2022-10-7"))

//    ApiConfig.instance.countAllAduan("proses")
//            .enqueue(object : Callback<AduanCount> {
//                override fun onResponse(call: Call<AduanCount>, response: Response<AduanCount>) {
//                    if (response.isSuccessful) {
//                        val data = response.body()?.data
//                        println(data)
//
//                        val jumlah = response.body()?.jumlah
//                        println(jumlah)
//                    }
//                }
//
//                override fun onFailure(call: Call<AduanCount>, t: Throwable) {
//                    println(t.message)
//                }
//            })
//
//    ApiConfig.instance.getAduanKec("selesai", "sukajadi")
//            .enqueue(object: Callback<ArrayList<ResponsePengaduan>> {
//                override fun onResponse(call: Call<ArrayList<ResponsePengaduan>>, response: Response<ArrayList<ResponsePengaduan>>) {
//                    if (response.isSuccessful) {
//                        val jumlah = response.body()!!.count()
//                        println("jumlah : $jumlah")
//                    }
//                }
//
//                override fun onFailure(call: Call<ArrayList<ResponsePengaduan>>, t: Throwable) {
//                    println(t.message)
//                }
//            })

    getCountAduan("proses")
    getCountAduan("selesai")

}

fun getCountAduan(status_aduan: String) {
    var jumlah: Int = 0
    ApiConfig.instance.getAllAduan(status_aduan)
            .enqueue(object : Callback<ArrayList<ResponsePengaduan>> {
                override fun onResponse(call: Call<ArrayList<ResponsePengaduan>>, response: Response<ArrayList<ResponsePengaduan>>) {
                    if (response.isSuccessful) {
                        jumlah = response.body()!!.count()
                        jumlahAduan = jumlah
                        println(jumlahAduan)

//                        println("data : $data")
//                        jumlahAduan = data
                    }
                }

                override fun onFailure(call: Call<ArrayList<ResponsePengaduan>>, t: Throwable) {
                    println(t.message)
                    jumlah = 0
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