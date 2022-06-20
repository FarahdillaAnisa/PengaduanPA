package com.icha.layananpengaduanpa.ui.operator.kelolaakun.akunspkt

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.icha.layananpengaduanpa.databinding.ActivityPostSpktPolsekBinding
import com.icha.layananpengaduanpa.model.ApiConfig
import com.icha.layananpengaduanpa.model.SpktModel
import com.icha.layananpengaduanpa.ui.login.LoginActivity
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class PostSpktPolsekActivity : AppCompatActivity() {
    private lateinit var binding : ActivityPostSpktPolsekBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPostSpktPolsekBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        binding.btnPostspkt.setOnClickListener {
            tambahAkunSpkt()
        }
    }

    private fun tambahAkunSpkt() {
        ApiConfig.instance.tambahAkunSpkt(
                binding.edtNama.text.toString(),
                binding.edtSatuanwilayah.text.toString(),
                binding.edtPassword.text.toString(),
                binding.edtNotelp.text.toString()
        ).enqueue(object : Callback<SpktModel> {
            override fun onResponse(call: Call<SpktModel>, response: Response<SpktModel>) {
                Toast.makeText(this@PostSpktPolsekActivity, "SPKT ${binding.edtNama.text} berhasil didaftarkan", Toast.LENGTH_SHORT).show()
//                val intent = Intent(this@PostAkunPolisiActivity, LoginActivity::class.java)
//                startActivity(intent)
            }

            override fun onFailure(call: Call<SpktModel>, t: Throwable) {
                Toast.makeText(this@PostSpktPolsekActivity, "Data SPKT gagal disimpan!", Toast.LENGTH_SHORT).show()
                Toast.makeText(this@PostSpktPolsekActivity, "Message : ${t.message}", Toast.LENGTH_SHORT).show()
            }

        })
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return super.onSupportNavigateUp()
    }
}