package com.icha.layananpengaduanpa.ui.polisi.pelaporan.postlaporan

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.AdapterView
import android.widget.Toast
import com.icha.layananpengaduanpa.R
import com.icha.layananpengaduanpa.databinding.ActivityCariPengaduanBinding
import com.icha.layananpengaduanpa.model.ApiConfig
import com.icha.layananpengaduanpa.model.MasyarakatModel
import com.icha.layananpengaduanpa.model.ResponsePengaduan
import com.icha.layananpengaduanpa.session.SessionManager
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
    private var opsiKecamatan: String = ""
    lateinit var session: SessionManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCariPengaduanBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        spinnerOpsiKecamatan()
        session = SessionManager(applicationContext)

        binding.btnCariAduan.setOnClickListener {
            binding.progressBar.visibility = View.VISIBLE
            cariAduan()
        }

        binding.btnTambahLaporan.setOnClickListener {
            tambahLaporan(kode_aduan)
        }
    }

    private fun spinnerOpsiKecamatan() {
        val kecamatan = resources.getStringArray(R.array.subdistrict_array)
        binding.opsiKecamatan.onItemSelectedListener = object :
                AdapterView.OnItemSelectedListener {
            override fun onItemSelected(p0: AdapterView<*>?, view: View?, position: Int, id: Long) {
                opsiKecamatan = kecamatan[position].toString()
            }

            override fun onNothingSelected(p0: AdapterView<*>?) {
                TODO("Not yet implemented")
            }
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
        kode_aduan = "ADUAN-${binding.txtKodeAduan.text.toString()}"
        ApiConfig.instance.cariAduan(kode_aduan, opsiKecamatan).enqueue(object : Callback<ResponsePengaduan> {
            override fun onResponse(
                call: Call<ResponsePengaduan>,
                response: Response<ResponsePengaduan>
            ) {
                binding.progressBar.visibility = View.GONE
                if (response.isSuccessful) {
                    val dataAduan = response.body()
                    if (dataAduan?.statusAduan == "proses") {
                        dataAduan.let {
                            getDataMsy(dataAduan.idMsyFk)
                            binding.tvKetAduan.setText(dataAduan.isiAduan)
                            latitude = dataAduan.latLokasi
                            longitude = dataAduan.longLokasi
                            status = true
                        }
                    } else {
                        Toast.makeText(this@CariPengaduanActivity, "Data Aduan : ${kode_aduan} sudah ditindaklanjuti", Toast.LENGTH_SHORT).show()
                        binding.btnTambahLaporan.isEnabled = false
                        status = false
                        dataAduan.let {
                            getDataMsy(dataAduan!!.idMsyFk)
                            binding.tvKetAduan.setText(dataAduan.isiAduan)
                            latitude = dataAduan.latLokasi
                            longitude = dataAduan.longLokasi
                        }

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

    private fun getDataMsy(idMsyFk: String) {
        ApiConfig.instance.getAkunMsy(idMsyFk)
                .enqueue(object : Callback<MasyarakatModel> {
                    override fun onResponse(call: Call<MasyarakatModel>, response: Response<MasyarakatModel>) {
                        if (response.isSuccessful) {
                            val dataAkun = response.body()
                            dataAkun?.let { data ->
                                binding.tvDataNamaPelapor.setText("$idMsyFk - ${data.namaMsy}" )
                                binding.tvNotelpPelapor.setText(data.notelpMsy)
                                binding.btnKontakPelapor.setOnClickListener {
                                    val intent = Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + Uri.encode(data.notelpMsy)))
                                    startActivity(intent)
                                }
                            }
                        }
                    }

                    override fun onFailure(call: Call<MasyarakatModel>, t: Throwable) {
                    }
                })
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_simpanaduan, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.action_simpan -> {
                val kode_aduan = binding.txtKodeAduan.text.toString()
                session.simpanDataAduan(kode_aduan, opsiKecamatan)
                Toast.makeText(this, "Lihat Data Tersimpan di Beranda", Toast.LENGTH_SHORT).show()
            }
        }
        return true
    }

//    override fun onSupportNavigateUp(): Boolean {
//        onBackPressed()
//        return super.onSupportNavigateUp()
//    }
}