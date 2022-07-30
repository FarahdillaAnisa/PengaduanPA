package com.icha.layananpengaduanpa.ui.login

import android.content.ClipData
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.Toast
import com.icha.layananpengaduanpa.databinding.ActivitySignupBinding
import com.icha.layananpengaduanpa.helper.Helper
import com.icha.layananpengaduanpa.model.ApiConfig
import com.icha.layananpengaduanpa.model.MasyarakatModel
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import android.view.View
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

        dialogBox("Perhatian!", "Halaman Signup ini hanya diperuntukkan" +
                "bagi masyarakat untuk mendaftarkan" +
                "akun baru")

        val id_msy = helper.getRandomId(3, "masyarakat")
        binding.edtId.setText(id_msy)

        binding.edtNama.addTextChangedListener(checkNull)
        binding.edtNotelp.addTextChangedListener(checkNull)
        binding.edtPassword.addTextChangedListener(checkNull)

        binding.btnSignup.setOnClickListener {
            binding.progressBar.visibility = View.VISIBLE
            if (validasiPassword() == true){
                userSignup(id_msy)
                val clipboardManager = getSystemService(Context.CLIPBOARD_SERVICE) as android.content.ClipboardManager
                val clipData = ClipData.newPlainText("text", id_msy)
                clipboardManager.setPrimaryClip(clipData)

                dialogBox("Perhatian!", "ID Pengguna : $id_msy, Mohon diingat id pengguna beserta password untuk login, id pengguna telah disalin untuk login pertama kali")
            }
        }
    }

    private val checkNull = object: TextWatcher {
        override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
        }

        override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            val nama: String = binding.edtNama.text.toString().trim()
            val notelp: String = binding.edtNotelp.text.toString().trim()
            val password : String = binding.edtPassword.text.toString().trim()
            binding.btnSignup.isEnabled = !nama.isEmpty() && !password.isEmpty() && !notelp.isEmpty()
        }

        override fun afterTextChanged(p0: Editable?) {
        }
    }

    private fun validasiPassword() : Boolean {
        var check: Boolean
        val passwordInput = binding.edtPassword.text.toString().trim()
        val passwordConfirm = binding.edtPasswordConfirm.text.toString().trim()
        if (passwordConfirm.equals(passwordInput)) {
            binding.checkPass.text = "Konfirmasi Password Cocok"
            binding.checkPass.setTextColor(Color.parseColor("#72a50b"))
            check = true
        } else {
            binding.checkPass.text = "Harap sesuaikan Konfirmasi Password dengan isi password"
            binding.checkPass.setTextColor(Color.parseColor("#B72227"))
            check = false
        }
        return check
    }


    private fun userSignup(id_msy: String) {
        ApiConfig.instance.signupUser(
                id_msy,
                binding.edtNama.text.toString(),
                binding.edtNotelp.text.toString(),
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
            }
        })
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return super.onSupportNavigateUp()
    }

    private fun dialogBox(title: String, message: String) {
        MaterialAlertDialogBuilder(this)
                .setTitle(title)
                .setMessage(message)
                .setNeutralButton("Lanjutkan", object : DialogInterface.OnClickListener{
                    override fun onClick(p0: DialogInterface?, p1: Int) {
                    }
                })
                .show()
    }

}