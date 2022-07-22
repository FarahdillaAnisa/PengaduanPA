package com.icha.layananpengaduanpa.ui.masyarakat.pengaduan.postaduan

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.DialogInterface
import android.content.pm.PackageManager
import android.location.LocationManager
import android.location.Location
import android.location.LocationListener
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.tasks.Task
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.icha.layananpengaduanpa.R
import com.icha.layananpengaduanpa.databinding.ActivityPostAduanBinding
import com.icha.layananpengaduanpa.helper.Helper
import com.icha.layananpengaduanpa.model.ApiConfig
import com.icha.layananpengaduanpa.model.ResponsePengaduan
import com.icha.layananpengaduanpa.session.SessionManager
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*
import kotlin.collections.HashMap

class PostAduanActivity : AppCompatActivity(), LocationListener {
    //get location
    private lateinit var locationManager: LocationManager
    private val locationPermissionCode = 2
    private var subdistrict : String = ""
    private var latitude : Double = 0.0
    private var longitude : Double = 0.0

    //location-fusedprovider
    lateinit var fusedLocationProviderClient: FusedLocationProviderClient

    //session
    lateinit var session: SessionManager

    //binding
    private lateinit var binding : ActivityPostAduanBinding

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPostAduanBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this)
        getLokasi()
//        getLocations()
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

        ArrayAdapter.createFromResource(
                this,
                R.array.subdistrict_array,
                android.R.layout.simple_spinner_item
        ).also { adapter ->
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            binding.subdistrictSpinner.adapter = adapter
        }

        // --------------------
        val subdistrictArray = resources.getStringArray(R.array.subdistrict_array)
        binding.subdistrictSpinner.onItemSelectedListener = object :
            AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View, position: Int, id: Long) {
                Toast.makeText(this@PostAduanActivity, subdistrictArray[position], Toast.LENGTH_SHORT).show()
                subdistrict = subdistrictArray[position].toString()
                Log.d("kecamatan" , subdistrict)
            }

            override fun onNothingSelected(parent: AdapterView<*>) {
                // write code to perform some action
            }
        }

        //get id_msy
        session = SessionManager(applicationContext)
        val user: HashMap<String, String> = session.getUserDetails()
        val id: String = user.get(SessionManager.KEY_ID)!!
        val nama: String = user.get(SessionManager.KEY_NAMA)!!
        val notelp: String = user.get(SessionManager.KEY_NOTELP)!!

        binding.txtNama.setText(nama)
        binding.notelpTxt.setText(notelp)

        binding.btnAddAduan.setOnClickListener{
            binding.progressBar.visibility = View.VISIBLE
            createNewAduan(id, nama)
        }
    }

    private fun refreshPage() {
        binding.swipeToRefresh.setOnRefreshListener {
            getLokasi()
            binding.swipeToRefresh.isRefreshing = false
        }
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

    private fun getLocations() {
        locationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager
        if ((ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED)){
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), locationPermissionCode)
        }
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1000, 5f, this)
//        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1000, 0, this)
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

    override fun onProviderEnabled(provider: String) {
    }

    override fun onProviderDisabled(provider: String) {
    }

    override fun onStatusChanged(provider: String?, status: Int, extras: Bundle?) {
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun createNewAduan(id_msy : String, nama_msy: String) {
        val helper = Helper()
        val currentDate = helper.saveCurrentDate()
        val id_aduan = helper.getRandomId(5, "pengaduan")

        ApiConfig.instance.createNewAduan(
                id_aduan,
                latitude,
                longitude,
                subdistrict,
                binding.txtIsiAduan.text.toString(),
                currentDate,
                id_msy,
                nama_msy
        ).enqueue(object : Callback<ResponsePengaduan>{
            override fun onResponse(call: Call<ResponsePengaduan>, response: Response<ResponsePengaduan>) {
                binding.progressBar.visibility = View.GONE
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

