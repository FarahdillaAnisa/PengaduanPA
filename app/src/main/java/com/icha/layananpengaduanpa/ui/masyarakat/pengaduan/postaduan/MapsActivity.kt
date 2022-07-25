package com.icha.layananpengaduanpa.ui.masyarakat.pengaduan.postaduan

import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.webkit.WebView
import android.webkit.WebViewClient
import com.icha.layananpengaduanpa.R

class MapsActivity : AppCompatActivity() {

    companion object {
        const val EXTRA_LOCATION_LATITUDE = "lat_loc"
        const val EXTRA_LOCATION_LONGITUDE = "long_loc"
    }

    private var latitude: Double = 0.0
    private var longitude: Double = 0.0

    @SuppressLint("SetJavaScriptEnabled")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_maps)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        val bundle : Bundle? = intent.extras
        if (bundle?.containsKey(EXTRA_LOCATION_LATITUDE)!! && bundle.containsKey(EXTRA_LOCATION_LONGITUDE)) {
            latitude = intent.getDoubleExtra(EXTRA_LOCATION_LATITUDE, 0.0)
            longitude = intent.getDoubleExtra(EXTRA_LOCATION_LONGITUDE, 0.0)

            val webViewMap : WebView = findViewById(R.id.webViewMaps)
            webViewMap.webViewClient = WebViewClient()
            webViewMap.settings.javaScriptEnabled = true
            webViewMap.loadUrl("http://maps.google.com/maps?q=${latitude},${longitude}&z=17")
            webViewMap.settings.setSupportZoom(true)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.appbar_menu_aduan, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.action_gmaps -> {
                val gmmIntentUri = Uri.parse("geo:$latitude,$longitude")
                val mapIntent = Intent(Intent.ACTION_VIEW, gmmIntentUri)
                mapIntent.setPackage("com.google.android.apps.maps")
                mapIntent.resolveActivity(packageManager)?.let {
                    startActivity(mapIntent)
                }
            }
        }
        return true
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return super.onSupportNavigateUp()
    }
}