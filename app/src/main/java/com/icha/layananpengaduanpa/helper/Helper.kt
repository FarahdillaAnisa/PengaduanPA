package com.icha.layananpengaduanpa.helper

import java.text.SimpleDateFormat
import java.util.*

class Helper {
    fun saveCurrentDate(): String {
        val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        val date = Date()
        return dateFormat.format(date)
    }

    fun displayDate(date: String): String {
        val inputFormat = SimpleDateFormat("EE MMM dd HH:mm:ss 'GMT'Z yyyy", Locale.ENGLISH)
        val dateFormat = SimpleDateFormat("dd-MMMM-yyyy", Locale.ENGLISH)
        val dateInput = inputFormat.parse(date)
        return dateFormat.format(dateInput)
    }

    fun displayDateBeranda(date: String): String {
        val inputFormat = SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH)
        val dateFormat = SimpleDateFormat("dd-MMMM-yyyy", Locale.ENGLISH)
        val dateInput = inputFormat.parse(date)
        return dateFormat.format(dateInput)
    }

    fun getRandomId(length: Int, entity: String) : String {
        val charset = "ABCDEFGHIJKLMNOPQRSTUVWXTZ0123456789"
        val output =  (1..length)
                .map { charset.random() }
                .joinToString("")
        var id = ""

        if (entity == "pengaduan") {
            id = "ADUAN-$output"
        }
        else if (entity == "masyarakat"){
            id = "MSY-$output"
        }
        else if (entity == "polisi"){
            id = "POL-$output"
        }
        else if (entity == "spkt"){
            id = "SPKT-$output"
        }
        else if (entity == "operator"){
            id = "OP-$output"
        }
        return id
    }

    fun getRandomPassword() : String {
        val charset = "abcdefghiklmnopqrstuvwxyz0123456789"
        return (1..5)
                .map { charset.random() }
                .joinToString("")
    }

//    fun checkUniqueIdUsername(username : String): Boolean {
//        var status = true
//        ApiConfig.instance.getAkunMsy(username)
//                .enqueue(object: Callback<MasyarakatUnameCheck> {
//                    override fun onResponse(call: Call<MasyarakatUnameCheck>, response: Response<MasyarakatUnameCheck>) {
//                        if (response.isSuccessful) {
//                            val data = response.body()
//                            data?.let { data ->
//                                if(data.unameMsyJumlah!! > 1) {
//                                    status = false
//                                    binding.statusUname.setText("Username $username sudah digunakan")
//                                } else {
//                                    status = true
//                                    binding.statusUname.visibility = View.GONE
//                                }
//                            }
//                        }
//                    }
//
//                    override fun onFailure(call: Call<MasyarakatUnameCheck>, t: Throwable) {
//                        Log.d("Message", "Tidak Berhasil Mendapatkan Data Jumlah")
//                    }
//                })
//
//        return status
//    }
}