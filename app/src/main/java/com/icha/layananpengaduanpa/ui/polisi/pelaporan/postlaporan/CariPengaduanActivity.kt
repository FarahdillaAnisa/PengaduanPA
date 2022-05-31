package com.icha.layananpengaduanpa.ui.polisi.pelaporan.postlaporan

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.icha.layananpengaduanpa.R
import com.icha.layananpengaduanpa.databinding.ActivityCariPengaduanBinding
import com.icha.layananpengaduanpa.model.ApiConfig
import com.icha.layananpengaduanpa.model.ResponsePengaduan
import com.icha.layananpengaduanpa.ui.masyarakat.pengaduan.postaduan.DetailAduanActivity
import com.icha.layananpengaduanpa.ui.masyarakat.pengaduan.postaduan.MapsActivity
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class CariPengaduanActivity : AppCompatActivity() {
    private lateinit var binding : ActivityCariPengaduanBinding
    var latitude : Double = 0.0
    var longitude : Double = 0.0
    var status: Boolean = false
    var kode_aduan : String = ""


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCariPengaduanBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        binding.btnCariAduan.setOnClickListener {
            cariAduan()
        }

        binding.btnTambahLaporan.setOnClickListener {
            tambahLaporan(kode_aduan)
        }
    }

    private fun tambahLaporan(kodeAduan : String) {
        if (status == true){
            val intent = Intent(this, PostPelaporanActivity::class.java)
            intent.putExtra(PostPelaporanActivity.EXTRA_KODE_ADUAN, kodeAduan)
            startActivity(intent)
        } else {
            Toast.makeText(this@CariPengaduanActivity, "Tidak ada data aduan", Toast.LENGTH_SHORT).show()
        }
    }

    private fun cariAduan() {
        kode_aduan = binding.txtKodeAduan.text.toString()
        ApiConfig.instance.cariAduan(kode_aduan).enqueue(object : Callback<ResponsePengaduan> {
            override fun onResponse(
                call: Call<ResponsePengaduan>,
                response: Response<ResponsePengaduan>
            ) {
                if (response.isSuccessful) {
                    val dataAduan = response.body()
                    if (dataAduan?.statusAduan == "proses") {
                        dataAduan.let {
                            binding.tvDataNamaPelapor.setText(dataAduan.idMsyFk.toString())
                            binding.tvKetAduan.setText(dataAduan.isiAduan)

                            latitude = dataAduan.latLokasi
                            longitude = dataAduan.longLokasi
                            status = true
                        }
                    } else {
                        Toast.makeText(this@CariPengaduanActivity, "Data Aduan : ${kode_aduan} sudah ditindaklanjuti", Toast.LENGTH_SHORT).show()
                    }
                }
                else {

                    Toast.makeText(this@CariPengaduanActivity, "Gagal Mengambil Data", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<ResponsePengaduan>, t: Throwable) {
                Toast.makeText(this@CariPengaduanActivity, "Kode : $kode_aduan", Toast.LENGTH_SHORT).show()
                val responseCode = t.message
                Toast.makeText(this@CariPengaduanActivity, responseCode, Toast.LENGTH_SHORT).show()
            }
        })

        binding.btnLokasiAduan.setOnClickListener {
            val intent = Intent(this, MapsActivity::class.java)
            intent.putExtra(DetailAduanActivity.EXTRA_LOCATION_LATITUDE, latitude)
            intent.putExtra(DetailAduanActivity.EXTRA_LOCATION_LONGITUDE, longitude)
            startActivity(intent)
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return super.onSupportNavigateUp()
    }
}