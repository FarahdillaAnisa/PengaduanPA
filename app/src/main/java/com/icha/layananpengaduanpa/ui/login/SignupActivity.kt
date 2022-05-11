package com.icha.layananpengaduanpa.ui.login

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.icha.layananpengaduanpa.R
import com.icha.layananpengaduanpa.databinding.ActivityLoginBinding
import com.icha.layananpengaduanpa.databinding.ActivitySignupBinding
import com.icha.layananpengaduanpa.model.ApiConfig

class SignupActivity : AppCompatActivity() {
    private lateinit var binding : ActivitySignupBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignupBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        binding.btnSignup.setOnClickListener {
            userSignup()
        }
    }

    private fun userSignup() {
//        ApiConfig.instance.
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return super.onSupportNavigateUp()
    }
}