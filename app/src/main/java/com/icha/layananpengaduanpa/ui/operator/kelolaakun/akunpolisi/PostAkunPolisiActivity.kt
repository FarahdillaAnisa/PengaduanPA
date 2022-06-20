package com.icha.layananpengaduanpa.ui.operator.kelolaakun.akunpolisi

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.icha.layananpengaduanpa.databinding.ActivityPostAkunPolisiBinding
import com.icha.layananpengaduanpa.model.ApiConfig
import com.icha.layananpengaduanpa.model.PolisiModel
import com.icha.layananpengaduanpa.ui.login.LoginActivity
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class PostAkunPolisiActivity : AppCompatActivity() {
    private lateinit var binding : ActivityPostAkunPolisiBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPostAkunPolisiBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        binding.btnPostpolisi.setOnClickListener {
            tambahAkunPolisi()
        }
    }

    private fun tambahAkunPolisi() {
        ApiConfig.instance.tambahAkunPolisi(
            binding.edtIdpolisi.text.toString(),
            binding.edtNama.text.toString(),
            binding.edtSatuanwilayah.text.toString(),
            binding.edtNotelp.text.toString(),
            binding.edtPassword.text.toString()
        ).enqueue(object :Callback<PolisiModel> {
            override fun onResponse(call: Call<PolisiModel>, response: Response<PolisiModel>) {
                Toast.makeText(this@PostAkunPolisiActivity, "Polisi ${binding.edtNama.text} berhasil didaftarkan", Toast.LENGTH_SHORT).show()
//                val intent = Intent(this@PostAkunPolisiActivity, LoginActivity::class.java)
//                startActivity(intent)
            }

            override fun onFailure(call: Call<PolisiModel>, t: Throwable) {
                Toast.makeText(this@PostAkunPolisiActivity, "Data Polisi gagal disimpan!", Toast.LENGTH_SHORT).show()
                Toast.makeText(this@PostAkunPolisiActivity, "Message : ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return super.onSupportNavigateUp()
    }
}