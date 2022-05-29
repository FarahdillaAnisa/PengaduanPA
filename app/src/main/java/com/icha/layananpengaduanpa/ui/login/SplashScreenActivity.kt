package com.icha.layananpengaduanpa.ui.login

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.icha.layananpengaduanpa.R
import com.icha.layananpengaduanpa.databinding.ActivityDetailAduanBinding
import com.icha.layananpengaduanpa.databinding.ActivitySplashScreenBinding
import com.icha.layananpengaduanpa.ui.operator.kelolaakun.akunpolisi.PostAkunPolisiActivity

class SplashScreenActivity : AppCompatActivity() {
    private lateinit var binding : ActivitySplashScreenBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySplashScreenBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.loginbtn.setOnClickListener {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        }

        //SEMENTARA
        binding.signupbtn.setOnClickListener {
            val intent = Intent(this, PostAkunPolisiActivity::class.java)
            startActivity(intent)
        }
    }
}