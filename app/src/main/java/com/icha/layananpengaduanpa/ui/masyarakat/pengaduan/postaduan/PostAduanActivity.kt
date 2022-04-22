package com.icha.layananpengaduanpa.ui.masyarakat.pengaduan.postaduan

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.LocationManager
import android.location.Location
import android.location.LocationListener
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.tasks.Task
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import com.google.android.material.textview.MaterialTextView
import com.icha.layananpengaduanpa.R
import com.icha.layananpengaduanpa.model.ApiConfig
import com.icha.layananpengaduanpa.model.ResponsePengaduan
import com.icha.layananpengaduanpa.ui.masyarakat.pengaduan.AduanProsesFragment
import com.icha.layananpengaduanpa.ui.masyarakat.pengaduan.PengaduanFragment
import org.w3c.dom.Text
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class PostAduanActivity : AppCompatActivity(), LocationListener {
    //get location
    private lateinit var locationManager: LocationManager
    private val locationPermissionCode = 2

    private var latitude : Double = 0.0
    private var longitude : Double = 0.0

    //location-fusedprovider
    lateinit var fusedLocationProviderClient: FusedLocationProviderClient

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_post_aduan)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this)
//        getLocations()
        getLokasi()
        val btn_add_aduan : Button = findViewById(R.id.btn_add_aduan)

        btn_add_aduan.setOnClickListener{
            createNewAduan()
//            val intent = Intent(, AduanProsesFragment::class)
//            startActivity(intent)
        }
    }

    private fun getLokasi() {
        val koordinat_msy : MaterialTextView = findViewById(R.id.koordinat_txt)
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
                koordinat_msy.text = "$latitude , $longitude"
            }
        }
    }

    private fun getLocations() {
        locationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager
        if ((ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED)){
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), locationPermissionCode)
        }
//        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1000, 0, this)
    }


    override fun onLocationChanged(location: Location) {
        latitude = location.latitude
        longitude = location.longitude

        val koordinat_msy : MaterialTextView = findViewById(R.id.koordinat_txt)
        koordinat_msy.text = "$latitude , $longitude"

    }

//    @SuppressLint("MissingSuperCall")
//    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
//        if (requestCode == locationPermissionCode) {
//            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
//                Toast.makeText(this, "Permission Granted", Toast.LENGTH_SHORT).show()
//            }
//            else {
//                Toast.makeText(this, "Permission Denied", Toast.LENGTH_SHORT).show()
//            }
//        }
//    }

    private fun createNewAduan() {
        val nama_msy : TextInputLayout = findViewById(R.id.txt_nama)
        val notelp_msy : TextInputLayout = findViewById(R.id.txt_notelp)
        val koordinat_msy : MaterialTextView = findViewById(R.id.koordinat_txt)
        val isiaduan_msy : TextInputEditText = findViewById(R.id.txt_isi_aduan)

        ApiConfig.instance.createNewAduan(
                "ADUANDUMMY2",
                latitude,
                longitude,
                "sukajadi",
                isiaduan_msy.text.toString(),
                "2022-04-05"
        ).enqueue(object : Callback<ResponsePengaduan>{
            override fun onResponse(call: Call<ResponsePengaduan>, response: Response<ResponsePengaduan>) {
                Toast.makeText(applicationContext, "Data telah berhasil disimpan!", Toast.LENGTH_SHORT).show()
            }

            override fun onFailure(call: Call<ResponsePengaduan>, t: Throwable) {
                Toast.makeText(applicationContext, "Data gagal disimpan!", Toast.LENGTH_SHORT).show()
                Toast.makeText(applicationContext, "Message : ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return super.onSupportNavigateUp()
    }
}

