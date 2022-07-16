package com.icha.layananpengaduanpa.ui.masyarakat.pengaduan.postaduan

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.icha.layananpengaduanpa.databinding.ActivityDetailAduanBinding
import com.icha.layananpengaduanpa.helper.Helper
import com.icha.layananpengaduanpa.model.ApiConfig
import com.icha.layananpengaduanpa.model.PolisiModel
import com.icha.layananpengaduanpa.model.ResponsePengaduan
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class DetailAduanActivity : AppCompatActivity() {
    private lateinit var aduan: ResponsePengaduan
    private lateinit var binding : ActivityDetailAduanBinding
    var latitude : Double = 0.0
    var longitude : Double = 0.0

    companion object {
        const val EXTRA_KODE_ADUAN = "kode_aduan"
        const val EXTRA_LOCATION_LATITUDE = "lat_loc"
        const val EXTRA_LOCATION_LONGITUDE = "long_loc"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailAduanBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        val bundle : Bundle? = intent.extras
        if (bundle?.containsKey(EXTRA_KODE_ADUAN)!!) {
            val kodeAduan = intent.getStringExtra(EXTRA_KODE_ADUAN)
            if (kodeAduan != null) {
                ApiConfig.instance.getAduan(kodeAduan).enqueue(object : Callback<ResponsePengaduan> {
                    override fun onResponse(call: Call<ResponsePengaduan>, response: Response<ResponsePengaduan>) {
                        if (response.isSuccessful) {
                            val helper = Helper()
                            Log.d("Data Aduan : " , response.body().toString())
                            val dataAduan = response.body()
                            dataAduan?.let {
                                binding.kodeAduanTxt.setText(kodeAduan)
                                binding.kecTxt.setText(dataAduan.kecLokasi)

                                //getCurrentDate
                                val tanggalAduan = helper.displayDate(dataAduan.tglAduan.toString())
                                binding.tgladuanTxt.setText(tanggalAduan)
                                binding.tvIsiAduan.setText(dataAduan.isiAduan)

                                val statusAduan = dataAduan.statusAduan
                                if (statusAduan == "proses") {
                                    binding.statusAduan.text = "Belum Diproses"
                                } else {
                                    binding.statusAduan.text = "Selesai"
                                    getDataPolisi(dataAduan.idPolisi!!)
                                    binding.tvKetAduan.setText(dataAduan.isiLaporanpolisi)
                                }

                                latitude = dataAduan.latLokasi
                                longitude = dataAduan.longLokasi
                            }
                        }
                        else {
                            Toast.makeText(this@DetailAduanActivity, "Gagal Mengambil Data", Toast.LENGTH_SHORT).show()
                        }
                    }

                    override fun onFailure(call: Call<ResponsePengaduan>, t: Throwable) {
                        Toast.makeText(this@DetailAduanActivity, "Kode : $kodeAduan", Toast.LENGTH_SHORT).show()
                        val responseCode = t.message
                        Toast.makeText(this@DetailAduanActivity, responseCode, Toast.LENGTH_SHORT).show()
                    }
                })
            }
        }

        binding.lokasiBtn.setOnClickListener {
//            val gmmIntentUri = Uri.parse("geo:0.522737, 101.431?z=21")
//            val mapIntent = Intent(Intent.ACTION_VIEW, gmmIntentUri)
//            mapIntent.setPackage("com.google.android.apps.maps")
//            mapIntent.resolveActivity(packageManager)?.let {
//                startActivity(mapIntent)
//            }

            val intent = Intent(this, MapsActivity::class.java)
            intent.putExtra(EXTRA_LOCATION_LATITUDE, latitude)
            intent.putExtra(EXTRA_LOCATION_LONGITUDE, longitude)
            startActivity(intent)
        }
    }

    private fun getDataPolisi(idPolisi: String) {
        ApiConfig.instance.getAkunPolisiById(idPolisi)
                .enqueue(object : Callback<PolisiModel> {
                    override fun onResponse(call: Call<PolisiModel>, response: Response<PolisiModel>) {
                        if (response.isSuccessful) {
                            val dataAkun = response.body()
                            dataAkun?.let { data ->
                                binding.tvDataNamaPetugas.setText("${data.idPolisi} - ${data.namaPolisi}" )
                                binding.tvSatwil?.setText(data.satuanWilayah)
                                binding.btnKontakPetugas.setOnClickListener {
                                    val intent = Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + Uri.encode(data.notelpPolisi)))
                                    startActivity(intent)
                                }
                            }
                        }
                    }

                    override fun onFailure(call: Call<PolisiModel>, t: Throwable) {
                    }

                })

    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return super.onSupportNavigateUp()
    }
}