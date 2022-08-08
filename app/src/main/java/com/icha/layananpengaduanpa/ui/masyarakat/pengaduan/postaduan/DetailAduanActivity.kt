package com.icha.layananpengaduanpa.ui.masyarakat.pengaduan.postaduan

import android.annotation.SuppressLint
import android.content.DialogInterface
import android.content.Intent
import android.graphics.Color
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.icha.layananpengaduanpa.databinding.ActivityDetailAduanBinding
import com.icha.layananpengaduanpa.helper.Helper
import com.icha.layananpengaduanpa.model.ApiConfig
import com.icha.layananpengaduanpa.model.PolisiModel
import com.icha.layananpengaduanpa.model.ResponsePengaduan
import com.icha.layananpengaduanpa.session.SessionManager
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DetailAduanActivity : AppCompatActivity() {
    private lateinit var binding : ActivityDetailAduanBinding
    lateinit var session: SessionManager
    var latitude : Double = 0.0
    var longitude : Double = 0.0
    var kodeAduan: String = ""
    var roleUser : String = ""

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

        session = SessionManager(applicationContext)
        val user: HashMap<String, String> = session.getUserDetails()
        roleUser = user.get(SessionManager.ROLE_USER).toString()

        val bundle : Bundle? = intent.extras
        if (bundle?.containsKey(EXTRA_KODE_ADUAN)!!) {
            kodeAduan = intent.getStringExtra(EXTRA_KODE_ADUAN).toString()
            getDetailAduan()
        }

        binding.lokasiBtn.setOnClickListener {
            val intent = Intent(this, MapsActivity::class.java)
            intent.putExtra(EXTRA_LOCATION_LATITUDE, latitude)
            intent.putExtra(EXTRA_LOCATION_LONGITUDE, longitude)
            startActivity(intent)
        }

        binding.btnSdgDiproses.setOnClickListener {
            MaterialAlertDialogBuilder(this)
                    .setTitle("Perhatian!")
                    .setMessage("Apakah anda yakin ingin mengatur status pengaduan menjadi sedang diproses?")
                    .setPositiveButton("Ya", object : DialogInterface.OnClickListener{
                        override fun onClick(p0: DialogInterface?, p1: Int) {
                            setSedangDiproses(kodeAduan)
                        }
                    })
                    .setNegativeButton("Tidak", object : DialogInterface.OnClickListener {
                        override fun onClick(p0: DialogInterface?, p1: Int) {
                        }
                    })
                    .show()
        }
    }

    private fun setSedangDiproses(kodeAduan: String) {
        ApiConfig.instance.setSedangDiproses(kodeAduan)
                .enqueue(object: Callback<ResponsePengaduan> {
                    override fun onResponse(call: Call<ResponsePengaduan>, response: Response<ResponsePengaduan>) {
                        if (response.isSuccessful) {
                            binding.btnSdgDiproses.visibility = View.GONE
                            dialogBox("Atur Status : Sedang Diproses", "Status Pengaduan $kodeAduan telah berhasil diperbaharui!")
                            getDetailAduan()
                        }
                    }

                    override fun onFailure(call: Call<ResponsePengaduan>, t: Throwable) {
                    }
                })
    }

    private fun getDetailAduan() {
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
                            binding.aduancv3.visibility = View.INVISIBLE
                            binding.btnKontakPetugas.visibility = View.INVISIBLE
                            if (roleUser == "operator") {
                                binding.btnSdgDiproses.visibility = View.VISIBLE
                            } else if (roleUser == "masyarakat") {
                                binding.btnSdgDiproses.visibility = View.GONE
                            }
                        } else if (statusAduan == "selesai") {
                            binding.statusAduan.text = "Selesai"
                            binding.statusAduan.setTextColor(Color.parseColor("#72a50b"))
                            getDataPolisi(dataAduan.idPolisi)
                            binding.tvKetAduan.setText(dataAduan.isiLaporanpolisi)
                            binding.btnSdgDiproses.visibility = View.GONE
                        } else {
                            binding.statusAduan.text = "Sedang Diproses"
                            binding.statusAduan.setTextColor(Color.parseColor("#E3B505"))
                            binding.aduancv3.visibility = View.GONE
                            binding.btnKontakPetugas.visibility = View.GONE
                            binding.btnSdgDiproses.visibility = View.GONE
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
                Toast.makeText(this@DetailAduanActivity, "Gagal Mengambil Data", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun getDataPolisi(idPolisi: String?) {
        ApiConfig.instance.getAkunPolisiById(idPolisi)
                .enqueue(object : Callback<PolisiModel> {
                    @SuppressLint("SetTextI18n")
                    override fun onResponse(call: Call<PolisiModel>, response: Response<PolisiModel>) {
                        if (response.isSuccessful) {
                            val dataAkun = response.body()
                            dataAkun?.let { data ->
                                binding.tvDataNamaPetugas.setText("$idPolisi - ${data.namaPolisi}" )
                                binding.tvSatwil.setText(data.satuanWilayah)
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

    private fun dialogBox(title: String, message: String) {
        MaterialAlertDialogBuilder(this)
                .setTitle(title)
                .setMessage(message)
                .setNeutralButton("Lanjutkan", object : DialogInterface.OnClickListener{
                    override fun onClick(p0: DialogInterface?, p1: Int) {
                    }
                })
                .show()
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return super.onSupportNavigateUp()
    }
}