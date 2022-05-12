package com.icha.layananpengaduanpa.ui.login

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.icha.layananpengaduanpa.MainActivity
import com.icha.layananpengaduanpa.databinding.ActivityLoginBinding
import com.icha.layananpengaduanpa.model.ApiConfig
import com.icha.layananpengaduanpa.model.MasyarakatModel
import com.icha.layananpengaduanpa.session.SessionManager
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LoginActivity : AppCompatActivity() {
    private lateinit var binding : ActivityLoginBinding
    lateinit var session: SessionManager
//    var LOGPREFERENCES : String = "LogPrefs"
//    private var Username : String = "UnameKey"
//    lateinit var sharedPreferences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        session = SessionManager(applicationContext)
        if (session.isLoggedIn()) {
            val intent = Intent(applicationContext, MainActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
            startActivity(intent)
            finish()
        }

        binding.loginbtn.setOnClickListener {
//            sharedPreferences = getSharedPreferences(LOGPREFERENCES, Context.MODE_PRIVATE)
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
                if (user != null) {
                    session.createLoginSession(user.unameMsy, user.passMsy)
                    val intent = Intent(this@LoginActivity, MainActivity::class.java)
                    intent.putExtra(MainActivity.EXTRA_USERNAME, user.unameMsy)
                    startActivity(intent)
                    finish()
                }
                else {
                    Toast.makeText(applicationContext, "Login gagal", Toast.LENGTH_SHORT).show()
                }

//                //Menyimpan data login di SharedPreferences
//                val editor: SharedPreferences.Editor = sharedPreferences.edit()
//                editor.putString(Username, user?.unameMsy)
//                editor.commit();
            }

            override fun onFailure(call: Call<MasyarakatModel>, t: Throwable) {
                Toast.makeText(this@LoginActivity, "User gagal login!", Toast.LENGTH_SHORT).show()
                Toast.makeText(this@LoginActivity, "Message : ${t.message}", Toast.LENGTH_SHORT).show()
                Log.d("Gagal : ", t.message.toString())
            }

        })
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return super.onSupportNavigateUp()
    }
}