package com.icha.layananpengaduanpa.ui.polisi.pelaporan.postlaporan

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.core.app.ActivityCompat
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.tasks.Task
import com.icha.layananpengaduanpa.databinding.ActivityPostPelaporanBinding
import com.icha.layananpengaduanpa.helper.Helper
import com.icha.layananpengaduanpa.model.ApiConfig
import com.icha.layananpengaduanpa.model.ResponsePengaduan
import com.icha.layananpengaduanpa.session.SessionManager
import com.icha.layananpengaduanpa.ui.masyarakat.pengaduan.postaduan.DetailAduanActivity
import com.icha.layananpengaduanpa.ui.polisi.pelaporan.PelaporanFragment
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.time.LocalDate

class PostPelaporanActivity : AppCompatActivity() {
    private lateinit var binding : ActivityPostPelaporanBinding
    lateinit var session: SessionManager
    private var latitude : Double = 0.0
    private var longitude : Double = 0.0
    private var id_polisi: String = ""
    //location-fusedprovider
    lateinit var fusedLocationProviderClient: FusedLocationProviderClient

    companion object {
        const val EXTRA_KODE_ADUAN = "kode_aduan"
    }
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPostPelaporanBinding.inflate(layoutInflater)
        setContentView(binding.root)

        cekLoginId()
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this)
//        getLocations()
        getLokasi()

        binding.btnAddLaporan.setOnClickListener {
            val bundle : Bundle? = intent.extras
            if (bundle?.containsKey(EXTRA_KODE_ADUAN)!!) {
                val kodeAduan = intent.getStringExtra(EXTRA_KODE_ADUAN)
                if (kodeAduan != null) {
                    Log.d("kode_aduan : ", "${kodeAduan}")
                    tambahLaporan(kodeAduan)
                }
            }
        }

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun tambahLaporan(kodeAduan: String) {
        val helper = Helper()
//        val tanggalLaporan = LocalDate.now()
        ApiConfig.instance.tambahLaporan(
                kodeAduan,
                id_polisi,
                binding.txtIsiLaporan.text.toString(),
                helper.saveCurrentDate()
        ).enqueue(object : Callback<ResponsePengaduan> {
            override fun onResponse(call: Call<ResponsePengaduan>, response: Response<ResponsePengaduan>) {
                Toast.makeText(applicationContext, "Laporan ${kodeAduan} telah berhasil disimpan!", Toast.LENGTH_SHORT).show()
//                val intent  = Intent(this@PostPelaporanActivity, PelaporanFragment::class.java)
//                startActivity(intent)
            }

            override fun onFailure(call: Call<ResponsePengaduan>, t: Throwable) {
                Toast.makeText(applicationContext, "Laporan ${kodeAduan} gagal disimpan!", Toast.LENGTH_SHORT).show()
                Toast.makeText(applicationContext, "Message : ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun getLokasi() {
        val task : Task<Location> = fusedLocationProviderClient.lastLocation
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), 101)
            return
        }

        task.addOnSuccessListener {
            if (it != null) {
                latitude = it.latitude
                longitude = it.longitude
                binding.koordinatTxt.text = "Latitude : $latitude , Longitude : $longitude"
            }
        }
    }

    private fun cekLoginId() {
        session = SessionManager(applicationContext)
        session.checkLogin()
        val polisi: HashMap<String, String> = session.getPolisiDetails()
        val id: String = polisi.get(SessionManager.KEY_ID)!!
        val nama: String = polisi.get(SessionManager.KEY_NAMA)!!
        val notelp: String = polisi.get(SessionManager.KEY_NOTELP)!!
        val satwil: String = polisi.get(SessionManager.KEY_SATWIL)!!

        binding.txtNama.setText(nama)
        binding.notelpTxt.setText(notelp)
        binding.satwilTxt.setText(satwil)
        id_polisi = id
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return super.onSupportNavigateUp()
    }
}