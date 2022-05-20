package com.icha.layananpengaduanpa.ui.masyarakat.pengaduan.postaduan

import android.Manifest
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
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.tasks.Task
import com.icha.layananpengaduanpa.R
import com.icha.layananpengaduanpa.databinding.ActivityPostAduanBinding
import com.icha.layananpengaduanpa.model.ApiConfig
import com.icha.layananpengaduanpa.model.ResponsePengaduan
import com.icha.layananpengaduanpa.session.SessionManager
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.time.LocalDate
import java.util.*
import kotlin.collections.HashMap

class PostAduanActivity : AppCompatActivity(), LocationListener {
    //get location
    private lateinit var locationManager: LocationManager
    private val locationPermissionCode = 2
    private var subdistrict : String = ""
//    val subdistrictArray = resources.getStringArray(R.array.subdistrict_array)

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
//        getLocations()
        getLokasi()

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
        session.checkLogin()
        val user: HashMap<String, String> = session.getUserDetails()
        val id: String = user.get(SessionManager.KEY_ID)!!
        val nama: String = user.get(SessionManager.KEY_NAMA)!!
        val notelp: String = user.get(SessionManager.KEY_NOTELP)!!

        binding.txtNama.setText(nama)
        binding.notelpTxt.setText(notelp)

        binding.btnAddAduan.setOnClickListener{
            createNewAduan(id.toInt())
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
                binding.koordinatTxt.text = "Latitude : $latitude , Longitude : $longitude"
            }
        }
    }

//    private fun getLocations() {
//        locationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager
//        if ((ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED)){
//            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), locationPermissionCode)
//        }
////        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1000, 0, this)
//    }


    override fun onLocationChanged(location: Location) {
        latitude = location.latitude
        longitude = location.longitude

        binding.koordinatTxt.text = "$latitude , $longitude"

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

    @RequiresApi(Build.VERSION_CODES.O)
    private fun createNewAduan(id_msy : Int) {
//        val subdistrictArray = resources.getStringArray(R.array.subdistrict_array)
//        binding.subdistrictSpinner.onItemSelectedListener = object :
//            AdapterView.OnItemSelectedListener {
//            override fun onItemSelected(parent: AdapterView<*>, view: View, position: Int, id: Long) {
//                Toast.makeText(this@PostAduanActivity, subdistrictArray[position], Toast.LENGTH_SHORT).show()
//                subdistrict = subdistrictArray[position].toString()
//                Log.d("kecamatan" , subdistrict)
//            }
//
//            override fun onNothingSelected(parent: AdapterView<*>) {
//                // write code to perform some action
//            }
//        }

        val tanggalAduan = LocalDate.now()

        ApiConfig.instance.createNewAduan(
                "ADUAN_SKJD6",
                latitude,
                longitude,
                subdistrict,
                binding.txtIsiAduan.text.toString(),
                tanggalAduan.toString(),
                id_msy
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

