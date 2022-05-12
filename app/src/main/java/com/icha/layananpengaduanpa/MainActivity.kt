package com.icha.layananpengaduanpa

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.icha.layananpengaduanpa.databinding.ActivityDetailAduanBinding
import com.icha.layananpengaduanpa.databinding.ActivityMainBinding
import com.icha.layananpengaduanpa.ui.masyarakat.pengaduan.postaduan.DetailAduanActivity

class MainActivity : AppCompatActivity() {
    private lateinit var binding : ActivityMainBinding
    companion object {
        const val EXTRA_USERNAME = "uname_msy"
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val username = intent.getStringExtra(EXTRA_USERNAME)
        binding.unameTxt.setText(username)
    }
}