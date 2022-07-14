package com.icha.layananpengaduanpa.ui.login

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.icha.layananpengaduanpa.databinding.ActivitySignupBinding
import com.icha.layananpengaduanpa.helper.Helper
import com.icha.layananpengaduanpa.model.ApiConfig
import com.icha.layananpengaduanpa.model.MasyarakatModel
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SignupActivity : AppCompatActivity() {
    private lateinit var binding : ActivitySignupBinding
    val helper = Helper()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignupBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        val statusUname = helper.checkUniqueIdUsername(binding.edtUname.text.toString())
        if (statusUname == false) {
            binding.statusUname.setText("Username sudah digunakan")
        }
        binding.btnSignup.setOnClickListener {
            userSignup(statusUname)
        }
    }

    private fun userSignup(statusUname: Boolean) {
        val id_msy = helper.getRandomId(3, "masyarakat")
        if (statusUname ==  true) {
            ApiConfig.instance.signupUser(
                    id_msy,
                    binding.edtNama.text.toString(),
                    binding.edtNotelp.text.toString(),
                    binding.edtUname.text.toString(),
                    binding.edtPassword.text.toString()
            ).enqueue(object : Callback<MasyarakatModel> {
                override fun onResponse(call: Call<MasyarakatModel>, response: Response<MasyarakatModel>) {
                    Toast.makeText(this@SignupActivity, "User ${binding.edtNama.text} berhasil didaftarkan", Toast.LENGTH_SHORT).show()
                    val intent = Intent(this@SignupActivity, LoginActivity::class.java)
                    startActivity(intent)
                }

                override fun onFailure(call: Call<MasyarakatModel>, t: Throwable) {
                    Toast.makeText(this@SignupActivity, "Data user gagal disimpan!", Toast.LENGTH_SHORT).show()
                    Toast.makeText(this@SignupActivity, "Message : ${t.message}", Toast.LENGTH_SHORT).show()
                }
            })
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return super.onSupportNavigateUp()
    }
}