package com.icha.layananpengaduanpa.ui.login

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.icha.layananpengaduanpa.MainActivity
import com.icha.layananpengaduanpa.databinding.ActivityLoginBinding
import com.icha.layananpengaduanpa.model.ApiConfig
import com.icha.layananpengaduanpa.model.MasyarakatModel
import com.icha.layananpengaduanpa.ui.masyarakat.pengaduan.postaduan.DetailAduanActivity
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LoginActivity : AppCompatActivity() {
    private lateinit var binding : ActivityLoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        binding.loginbtn.setOnClickListener {
            userLogin()
        }
    }

    private fun userLogin() {
        ApiConfig.instance.loginUser(
                binding.edtUname.text.toString().trim(),
                "Masyarakat",
                binding.edtPass.text.toString().trim()
        ).enqueue(object : Callback<MasyarakatModel> {
            override fun onResponse(call: Call<MasyarakatModel>, response: Response<MasyarakatModel>) {
                Toast.makeText(this@LoginActivity, "User berhasil login!", Toast.LENGTH_SHORT).show()
                val user = response.body()
                val intent = Intent(this@LoginActivity, MainActivity::class.java)
                intent.putExtra(MainActivity.EXTRA_USERNAME, user?.unameMsy)
                startActivity(intent)
            }

            override fun onFailure(call: Call<MasyarakatModel>, t: Throwable) {
                Toast.makeText(this@LoginActivity, "User gagal login!", Toast.LENGTH_SHORT).show()
                Toast.makeText(this@LoginActivity, "Message : ${t.message}", Toast.LENGTH_SHORT).show()
            }

        })
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return super.onSupportNavigateUp()
    }
}