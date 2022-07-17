package com.icha.layananpengaduanpa.ui.login

import android.content.DialogInterface
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
import android.view.View
import androidx.appcompat.app.AlertDialog
import com.google.android.material.dialog.MaterialAlertDialogBuilder

class SignupActivity : AppCompatActivity() {
    private lateinit var binding : ActivitySignupBinding
    val helper = Helper()

    @Override
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignupBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        MaterialAlertDialogBuilder(this)
                .setTitle("Perhatian!")
                .setMessage("Halaman Signup ini hanya diperuntukkan" +
                        "bagi masyarakat untuk mendaftarkan" +
                        "akun baru")
                .setNeutralButton("Lanjutkan", object : DialogInterface.OnClickListener{
                    override fun onClick(p0: DialogInterface?, p1: Int) {
                    }
                })
                .show()

        val id_msy = helper.getRandomId(3, "masyarakat")
        binding.edtId.setText(id_msy)
        binding.btnSignup.setOnClickListener {
            binding.progressBar.visibility = View.VISIBLE
            if (validasiPassword() == true){
                userSignup(id_msy)
            }
        }
    }

    private fun validasiPassword() : Boolean {
        var check: Boolean
        val passwordInput = binding.edtPassword.text.toString().trim()
        val passwordConfirm = binding.edtPasswordConfirm.text.toString().trim()
        if (passwordConfirm.equals(passwordInput)) {
            binding.checkPass.text = "Konfirmasi Password Cocok"
            check = true
        } else {
            binding.checkPass.text = "Harap sesuaikan Konfirmasi Password dengan isi password"
            check = false
        }
        return check
    }


    private fun userSignup(id_msy: String) {
        ApiConfig.instance.signupUser(
                id_msy,
                binding.edtNama.text.toString(),
                binding.edtNotelp.text.toString(),
//                    binding.edtUname.text.toString(),
                binding.edtPassword.text.toString()
        ).enqueue(object : Callback<MasyarakatModel> {
            override fun onResponse(call: Call<MasyarakatModel>, response: Response<MasyarakatModel>) {
                binding.progressBar.visibility = View.GONE
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

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return super.onSupportNavigateUp()
    }


}