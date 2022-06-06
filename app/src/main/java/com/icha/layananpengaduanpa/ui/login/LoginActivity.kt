package com.icha.layananpengaduanpa.ui.login

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.icha.layananpengaduanpa.*
import com.icha.layananpengaduanpa.databinding.ActivityLoginBinding
import com.icha.layananpengaduanpa.model.*
import com.icha.layananpengaduanpa.session.SessionManager
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LoginActivity : AppCompatActivity() {
    private lateinit var binding : ActivityLoginBinding
    lateinit var session: SessionManager
    private var opsiLogin : String = ""
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
            val intent = Intent(applicationContext, MenuMasyarakat::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
            startActivity(intent)
            finish()
        }

        spinnerOpsiLogin()

        binding.loginbtn.setOnClickListener {
//            sharedPreferences = getSharedPreferences(LOGPREFERENCES, Context.MODE_PRIVATE)
            userLogin(opsiLogin)
        }
    }

    private fun spinnerOpsiLogin() {
        val opsiLoginArray = resources.getStringArray(R.array.opsi_login)
        binding.opsiLogin.onItemSelectedListener = object :
        AdapterView.OnItemSelectedListener {
            override fun onItemSelected(p0: AdapterView<*>?, view: View?, position: Int, id: Long) {
                opsiLogin = opsiLoginArray[position].toString()
            }

            override fun onNothingSelected(p0: AdapterView<*>?) {
                TODO("Not yet implemented")
            }
        }
    }

    private fun userLogin(opsi: String) {
        if (opsi.equals("Masyarakat")) {
            loginMasyarakat(opsi)
        } else if (opsi.equals("Polisi")) {
            loginPolisi(opsi)
        } else if (opsi.equals("SPKT Polsek")) {
            loginSpkt("Spkt")
        } else if (opsi.equals("Operator")) {
            loginOperator("Operator")
        }
    }

    private fun loginOperator(opsi : String) {
        ApiConfig.instance.loginOperator(
                binding.edtUname.text.toString().trim(),
                opsi,
                binding.edtPass.text.toString().trim()
        ).enqueue(object : Callback<OperatorModel> {
            override fun onResponse(call: Call<OperatorModel>, response: Response<OperatorModel>) {
                Toast.makeText(this@LoginActivity, "User ${response.body()?.unameOperator} berhasil login!", Toast.LENGTH_SHORT).show()
                val operator = response.body()
                if (operator != null) {
                    session.createLoginOperatorSession(operator.idOperator.toString(), operator.unameOperator.toString())
                    val intent = Intent(this@LoginActivity, MenuOperator::class.java)
                    startActivity(intent)
                    finish()
                } else {
                    Toast.makeText(applicationContext, "Login gagal", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<OperatorModel>, t: Throwable) {
                Toast.makeText(this@LoginActivity, "Operator gagal login!", Toast.LENGTH_SHORT).show()
                Toast.makeText(this@LoginActivity, "Message : ${t.message}", Toast.LENGTH_SHORT).show()
                Log.d("Gagal : ", t.message.toString())
            }
        })

    }

    private fun loginSpkt(opsi: String) {
        ApiConfig.instance.loginSpkt(
                binding.edtUname.text.toString().trim(),
                opsi,
                binding.edtPass.text.toString().trim()
        ).enqueue(object : Callback<SpktModel> {
            override fun onResponse(call: Call<SpktModel>, response: Response<SpktModel>) {
                Toast.makeText(this@LoginActivity, "User ${response.body()?.unameSpkt} berhasil login!", Toast.LENGTH_SHORT).show()
                val spkt = response.body()
                if (spkt != null) {
                    session.createLoginSpktSession(spkt.idSpkt.toString(), spkt.unameSpkt.toString(), spkt.satuanWilayah.toString(), spkt.notelpSpkt.toString())
                    val intent = Intent(this@LoginActivity, MenuSpkt::class.java)
                    startActivity(intent)
                    finish()
                } else {
                    Toast.makeText(applicationContext, "Login gagal", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<SpktModel>, t: Throwable) {
                Toast.makeText(this@LoginActivity, "Spkt gagal login!", Toast.LENGTH_SHORT).show()
                Toast.makeText(this@LoginActivity, "Message : ${t.message}", Toast.LENGTH_SHORT).show()
                Log.d("Gagal : ", t.message.toString())
            }
        })
    }

    private fun loginPolisi(opsi: String) {
        ApiConfig.instance.loginPolisi(
                binding.edtUname.text.toString().trim(),
                opsi,
                binding.edtPass.text.toString().trim()
        ).enqueue(object : Callback<PolisiModel> {
            override fun onResponse(call: Call<PolisiModel>, response: Response<PolisiModel>) {
                Toast.makeText(this@LoginActivity, "User ${response.body()?.namaPolisi} berhasil login!", Toast.LENGTH_SHORT).show()
                val polisi = response.body()
                if (polisi != null) {
                    session.createLoginPolisiSession(polisi.passPolisi, polisi.idPolisi, polisi.namaPolisi, polisi.notelpPolisi, polisi.satuanWilayah)
                    val intent = Intent(this@LoginActivity, MenuPolisi::class.java)
                    startActivity(intent)
                    finish()
                } else {
                    Toast.makeText(applicationContext, "Login gagal", Toast.LENGTH_SHORT).show()
                }

//                //Menyimpan data login di SharedPreferences
//                val editor: SharedPreferences.Editor = sharedPreferences.edit()
//                editor.putString(Username, user?.unameMsy)
//                editor.commit();
            }

            override fun onFailure(call: Call<PolisiModel>, t: Throwable) {
                Toast.makeText(this@LoginActivity, "Polisi gagal login!", Toast.LENGTH_SHORT).show()
                Toast.makeText(this@LoginActivity, "Message : ${t.message}", Toast.LENGTH_SHORT).show()
                Log.d("Gagal : ", t.message.toString())
            }
        })
    }

    private fun loginMasyarakat(opsi: String) {
        ApiConfig.instance.loginMasyarakat(
                binding.edtUname.text.toString().trim(),
                opsi,
                binding.edtPass.text.toString().trim()
        ).enqueue(object : Callback<MasyarakatModel> {
            override fun onResponse(call: Call<MasyarakatModel>, response: Response<MasyarakatModel>) {
                Toast.makeText(this@LoginActivity, "User berhasil login!", Toast.LENGTH_SHORT).show()
                val user = response.body()
                if (user != null) {
                    session.createLoginSession(user.unameMsy, user.passMsy, user.idMsy.toString(), user.namaMsy, user.notelpMsy)
                    val intent = Intent(this@LoginActivity, MenuMasyarakat::class.java)
//                    intent.putExtra(MainActivity.EXTRA_USERNAME, user.unameMsy)

                    startActivity(intent)
                    finish()
                } else {
                    Toast.makeText(applicationContext, "Login gagal", Toast.LENGTH_SHORT).show()
                }
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