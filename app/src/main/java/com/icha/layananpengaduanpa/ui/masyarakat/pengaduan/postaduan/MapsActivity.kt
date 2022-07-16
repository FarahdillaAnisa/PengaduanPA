package com.icha.layananpengaduanpa.ui.masyarakat.pengaduan.postaduan

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.webkit.WebView
import android.webkit.WebViewClient
import com.icha.layananpengaduanpa.R

class MapsActivity : AppCompatActivity() {

    companion object {
        const val EXTRA_LOCATION_LATITUDE = "lat_loc"
        const val EXTRA_LOCATION_LONGITUDE = "long_loc"
    }

    @SuppressLint("SetJavaScriptEnabled")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_maps)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        val bundle : Bundle? = intent.extras
        if (bundle?.containsKey(EXTRA_LOCATION_LATITUDE)!! && bundle.containsKey(EXTRA_LOCATION_LONGITUDE)) {
            val latitude = intent.getDoubleExtra(EXTRA_LOCATION_LATITUDE, 0.0)
            val longitude = intent.getDoubleExtra(EXTRA_LOCATION_LONGITUDE, 0.0)

            val webViewMap : WebView = findViewById(R.id.webViewMaps)
            webViewMap.webViewClient = WebViewClient()
            webViewMap.loadUrl("http://maps.google.com/maps?q=${latitude},${longitude}&z=17")
            webViewMap.settings.javaScriptEnabled = true
            webViewMap.settings.setSupportZoom(true)
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return super.onSupportNavigateUp()
    }
}