package com.icha.layananpengaduanpa.ui.polisi.pelaporan.postlaporan

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.icha.layananpengaduanpa.R

class PostPelaporanActivity : AppCompatActivity() {

    companion object {
        const val EXTRA_KODE_ADUAN = "kode_aduan"
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_post_pelaporan)
    }
}