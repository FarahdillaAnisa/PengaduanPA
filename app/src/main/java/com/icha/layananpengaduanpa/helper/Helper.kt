package com.icha.layananpengaduanpa.helper

import android.util.Log
import android.widget.Toast
import com.icha.layananpengaduanpa.model.ApiConfig
import com.icha.layananpengaduanpa.model.MasyarakatModel
import com.icha.layananpengaduanpa.model.MasyarakatUnameCheck
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.SimpleDateFormat
import java.util.*

class Helper {
    fun saveCurrentDate(): String {
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

    fun getRandomId(length: Int, entity: String) : String {
        val charset = "ABCDEFGHIJKLMNOPQRSTUVWXTZabcdefghiklmnopqrstuvwxyz0123456789"
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
        val charset = "ABCDEFGHIJKLMNOPQRSTUVWXTZabcdefghiklmnopqrstuvwxyz0123456789*$#+-"
        return (1..10)
                .map { charset.random() }
                .joinToString("")
    }

    fun checkUniqueIdUsername(username : String): Boolean {
        var status = false
        ApiConfig.instance.getAkunMsy(username)
                .enqueue(object: Callback<MasyarakatUnameCheck> {
                    override fun onResponse(call: Call<MasyarakatUnameCheck>, response: Response<MasyarakatUnameCheck>) {
                        if (response.isSuccessful) {
                            val data = response.body()
                            data?.let { data ->
                                if(data.unameMsyJumlah!! > 1) {
                                    status = false
                                } else {
                                    status = true
                                }
                            }
                        }
                    }

                    override fun onFailure(call: Call<MasyarakatUnameCheck>, t: Throwable) {
                        Log.d("Message", "Tidak Berhasil Mendapatkan Data Jumlah")
                    }
                })

        return status
    }
}