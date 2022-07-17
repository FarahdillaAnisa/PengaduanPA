package com.icha.layananpengaduanpa.ui.polisi.pelaporan.postlaporan

import android.Manifest
import android.annotation.SuppressLint
import android.content.DialogInterface
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.core.app.ActivityCompat
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.tasks.Task
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.icha.layananpengaduanpa.databinding.ActivityPostPelaporanBinding
import com.icha.layananpengaduanpa.helper.Helper
import com.icha.layananpengaduanpa.model.ApiConfig
import com.icha.layananpengaduanpa.model.ResponsePengaduan
import com.icha.layananpengaduanpa.session.SessionManager
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class PostPelaporanActivity : AppCompatActivity(), LocationListener {
    private lateinit var binding : ActivityPostPelaporanBinding
    lateinit var session: SessionManager
    private var latitude : Double = 0.0
    private var longitude : Double = 0.0
    private var id_polisi: String = ""
    //location-fusedprovider
    lateinit var fusedLocationProviderClient: FusedLocationProviderClient
    private val locationPermissionCode = 2

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

        MaterialAlertDialogBuilder(this)
            .setTitle("Perhatian!")
            .setMessage("Jika koordinat tidak muncul" +
                    " silahkan swipe kebawah untuk refresh halaman")
            .setNeutralButton("Lanjutkan", object : DialogInterface.OnClickListener{
                override fun onClick(p0: DialogInterface?, p1: Int) {
                }
            })
            .show()
        refreshPage()

        binding.btnAddLaporan.setOnClickListener {
            binding.progressBar.visibility = View.VISIBLE
            val bundle : Bundle? = intent.extras
            if (bundle?.containsKey(EXTRA_KODE_ADUAN)!!) {
                val kodeAduan = intent.getStringExtra(EXTRA_KODE_ADUAN)
                if (kodeAduan != null) {
                    Log.d("kode_aduan : ", "${kodeAduan}")
                    tambahLaporan(kodeAduan, id_polisi)
                }
            }
        }

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    private fun refreshPage() {
        binding.swipeToRefresh.setOnRefreshListener {
            getLokasi()
            binding.swipeToRefresh.isRefreshing = false
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun tambahLaporan(kodeAduan: String, idPolisi: String) {
        val helper = Helper()
//        val tanggalLaporan = LocalDate.now()
        ApiConfig.instance.tambahLaporan(
                kodeAduan,
                idPolisi,
                binding.txtIsiLaporan.text.toString(),
                latitude,
                longitude,
                helper.saveCurrentDate()
        ).enqueue(object : Callback<ResponsePengaduan> {
            override fun onResponse(call: Call<ResponsePengaduan>, response: Response<ResponsePengaduan>) {
                binding.progressBar.visibility = View.GONE
                Toast.makeText(applicationContext, "Laporan $kodeAduan : $idPolisi - ${binding.txtIsiLaporan.text.toString()}" +
                        "${helper.saveCurrentDate()} telah berhasil disimpan!", Toast.LENGTH_SHORT).show()
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
                binding.koordinatTxt.text = "Lintang : $latitude , Bujur : $longitude"
            }
        }
    }

    override fun onLocationChanged(location: Location) {
        latitude = location.latitude
        longitude = location.longitude

        binding.koordinatTxt.text = "Lintang : $latitude , Bujur : $longitude"
    }

    @SuppressLint("MissingSuperCall")
    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        if (requestCode == locationPermissionCode) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "Permission Granted", Toast.LENGTH_SHORT).show()
            }
            else {
                Toast.makeText(this, "Permission Denied", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun cekLoginId() {
        session = SessionManager(applicationContext)
        val polisi: HashMap<String, String> = session.getUserDetails()
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