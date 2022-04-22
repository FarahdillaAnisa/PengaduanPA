package com.icha.layananpengaduanpa.ui.masyarakat.pengaduan.postaduan

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.icha.layananpengaduanpa.R
import com.icha.layananpengaduanpa.databinding.ActivityDetailAduanBinding
import com.icha.layananpengaduanpa.model.ApiConfig
import com.icha.layananpengaduanpa.model.ResponsePengaduan
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class DetailAduanActivity : AppCompatActivity() {
    private lateinit var aduan: ResponsePengaduan
    private lateinit var binding : ActivityDetailAduanBinding
    private var idAduan: String = ""

    companion object {
        const val EXTRA_KODE_ADUAN = "kode_aduan"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail_aduan)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        val kodeAduan = intent?.extras?.getString(EXTRA_KODE_ADUAN).toString()
        ApiConfig.instance.getAduan(kodeAduan).enqueue(object : Callback<ResponsePengaduan> {
            override fun onResponse(call: Call<ResponsePengaduan>, response: Response<ResponsePengaduan>) {
                idAduan = kodeAduan
                binding.kodeAduanTxt.text = idAduan
                binding.kecTxt.text = response.body()?.kecLokasi
                binding.tgladuanTxt.text = response.body()?.tglAduan.toString()
            }

            override fun onFailure(call: Call<ResponsePengaduan>, t: Throwable) {
                val responseCode = t.message
                Toast.makeText(this@DetailAduanActivity, responseCode, Toast.LENGTH_SHORT).show()
            }

        })
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return super.onSupportNavigateUp()
    }
}