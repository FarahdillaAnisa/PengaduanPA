package com.icha.layananpengaduanpa.ui.polisi.pelaporan.postlaporan

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.icha.layananpengaduanpa.R
import com.icha.layananpengaduanpa.databinding.ActivityDetailLaporanBinding
import com.icha.layananpengaduanpa.helper.Helper
import com.icha.layananpengaduanpa.model.ApiConfig
import com.icha.layananpengaduanpa.model.ResponsePengaduan
import com.icha.layananpengaduanpa.ui.masyarakat.pengaduan.postaduan.DetailAduanActivity
import com.icha.layananpengaduanpa.ui.masyarakat.pengaduan.postaduan.MapsActivity
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DetailLaporanActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDetailLaporanBinding
    var latitude : Double = 0.0
    var longitude : Double = 0.0

    companion object {
        const val EXTRA_KODE_ADUAN = "kode_aduan"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailLaporanBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        getLaporanDetail()

        binding.lokasiBtn.setOnClickListener {
            val intent = Intent(this, MapsActivity::class.java)
            intent.putExtra(MapsActivity.EXTRA_LOCATION_LATITUDE, latitude)
            intent.putExtra(MapsActivity.EXTRA_LOCATION_LONGITUDE, longitude)
            startActivity(intent)
        }
    }

    private fun getLaporanDetail() {
        val bundle: Bundle? = intent.extras
        if (bundle?.containsKey(EXTRA_KODE_ADUAN)!!) {
            val kodeAduan = intent.getStringExtra(EXTRA_KODE_ADUAN)
            if (kodeAduan != null) {
                ApiConfig.instance.getAduan(kodeAduan).enqueue(object : Callback<ResponsePengaduan> {
                    override fun onResponse(call: Call<ResponsePengaduan>, response: Response<ResponsePengaduan>) {
                        if (response.isSuccessful) {
                            Log.d("Data Aduan : " , response.body().toString())
                            val helper = Helper()
                            val dataAduan = response.body()
                            dataAduan?.let {
                                binding.kodeAduanTxt.setText(kodeAduan)
                                binding.kecTxt.setText(dataAduan.kecLokasi)
                                binding.tgladuanTxt.setText(helper.displayDate(dataAduan.tglAduan.toString()))
                                binding.tvIsiAduan.setText(dataAduan.isiAduan)
                                binding.tvKetLaporan.setText(dataAduan.isiLaporanpolisi)
                                binding.statusAduan.setText(dataAduan.statusAduan)
                                latitude = dataAduan.latLokasi
                                longitude = dataAduan.longLokasi
                            }
                        }
                        else {
                            Toast.makeText(this@DetailLaporanActivity, "Gagal Mengambil Data", Toast.LENGTH_SHORT).show()
                        }
                    }

                    override fun onFailure(call: Call<ResponsePengaduan>, t: Throwable) {
                        Toast.makeText(this@DetailLaporanActivity, "Gagal Mengambil Data dengan Kode : $kodeAduan", Toast.LENGTH_SHORT).show()
                    }
                })
            }
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return super.onSupportNavigateUp()
    }
}