package com.icha.layananpengaduanpa.ui.polisi.pelaporan.postlaporan

import android.content.DialogInterface
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.AdapterView
import android.widget.Toast
import com.google.android.material.dialog.MaterialAlertDialogBuilder
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
        supportActionBar?.setDisplayHomeAsUpEnabled(false)
        supportActionBar?.setHomeButtonEnabled(false)

        spinnerOpsiKecamatan()
        session = SessionManager(applicationContext)
        val polisi: HashMap<String, String> = session.getUserDetails()
        val satwil: String = polisi.get(SessionManager.KEY_SATWIL)!!

        binding.txtKodeAduan.addTextChangedListener(checkNull)
        binding.btnCariAduan.setOnClickListener {
            binding.progressBar.visibility = View.VISIBLE
            cariAduan()
        }

        binding.btnTambahLaporan.setOnClickListener {
            tambahLaporan(kode_aduan, satwil)
        }
    }

    private val checkNull = object: TextWatcher {
        override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
        }

        override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            val kodeAduan: String = binding.txtKodeAduan.text.toString().trim()
            binding.btnCariAduan.isEnabled = !kodeAduan.isEmpty()
        }

        override fun afterTextChanged(p0: Editable?) {
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
            }
        }
    }

    private fun tambahLaporan(kodeAduan: String, satwil: String) {
        if (status == true){
            if (satwil == opsiKecamatan) {
                val intent = Intent(this, PostPelaporanActivity::class.java)
                intent.putExtra(PostPelaporanActivity.EXTRA_KODE_ADUAN, kodeAduan)
                startActivity(intent)
            } else {
                dialogBox("Perhatian!", "Satuan Wilayah anda berbeda dengan Kecamatan Pengaduan")
            }
        } else {
            dialogBox("Perhatian!", "Silahkan Cari Aduan Terlebih Dahulu")
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
                    if (dataAduan?.statusAduan == "proses" || dataAduan?.statusAduan == "diproses") {
                        dataAduan.let {
                            getDataMsy(dataAduan.idMsyFk)
                            binding.tvKetAduan.setText(dataAduan.isiAduan)
                            latitude = dataAduan.latLokasi
                            longitude = dataAduan.longLokasi
                            binding.btnTambahLaporan.isEnabled = true
                            status = true
                        }
                    } else {
                        dialogBox("Perhatian!", "Data Aduan : ${kode_aduan} sudah ditindaklanjuti")
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
                    Toast.makeText(this@CariPengaduanActivity, "Data Pengaduan Tidak Ditemukan", Toast.LENGTH_SHORT).show()
                    binding.btnTambahLaporan.isEnabled = false
                }
            }

            override fun onFailure(call: Call<ResponsePengaduan>, t: Throwable) {
                Toast.makeText(this@CariPengaduanActivity, "Gagal Mengambil Data dengan Kode : $kode_aduan", Toast.LENGTH_SHORT).show()
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
                                binding.btnKontakPelapor.isEnabled = true
                                binding.btnLokasiAduan.isEnabled = true
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
                dialogBox("Perhatian!", "Lihat Data Tersimpan di Beranda")
            }
        }
        return true
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
}