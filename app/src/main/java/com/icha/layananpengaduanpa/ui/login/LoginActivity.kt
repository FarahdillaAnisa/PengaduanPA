package com.icha.layananpengaduanpa.ui.login

import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.dialog.MaterialAlertDialogBuilder
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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        session = SessionManager(applicationContext)
        val user: HashMap<String, String> = session.getUserDetails()
        val role_user: String = user.get(SessionManager.ROLE_USER).toString()
        checkLogin(role_user)

        MaterialAlertDialogBuilder(this)
            .setTitle("Perhatian!")
            .setMessage("Apakah anda sudah memiliki akun?")
            .setNeutralButton("Sudah", object : DialogInterface.OnClickListener{
                override fun onClick(p0: DialogInterface?, p1: Int) {
                }
            })
            .setNegativeButton("Belum", object : DialogInterface.OnClickListener {
                override fun onClick(p0: DialogInterface?, p1: Int) {
                    val intent = Intent(this@LoginActivity, SignupActivity::class.java)
                    startActivity(intent)
                }
            })
            .show()

        spinnerOpsiLogin()
        binding.edtId.addTextChangedListener(checkNull)
        binding.edtPass.addTextChangedListener(checkNull)
        binding.loginbtn.setOnClickListener {
            binding.progressBar.visibility = View.VISIBLE
            userLogin(opsiLogin)
        }
    }

    private val checkNull = object: TextWatcher {
        override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
        }

        override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            val id: String = binding.edtId.text.toString().trim()
            val password : String = binding.edtPass.text.toString().trim()
            binding.loginbtn.isEnabled = !id.isEmpty() && !password.isEmpty()
        }

        override fun afterTextChanged(p0: Editable?) {
        }
    }

    private fun checkLogin(role_user : String) {
        if (session.isLoggedIn()) {
            if (role_user == "masyarakat") {
                val intent = Intent(this, MenuMasyarakat::class.java)
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
                startActivity(intent)
            } else if (role_user == "polisi") {
                val intent = Intent(this, MenuPolisi::class.java)
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
                startActivity(intent)
            } else if (role_user == "spkt") {
                val intent = Intent(this, MenuSpkt::class.java)
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
                startActivity(intent)
            } else if (role_user == "operator") {
                val intent = Intent(this, MenuOperator::class.java)
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
                startActivity(intent)
            }
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
                binding.edtId.text.toString().trim(),
                opsi,
                binding.edtPass.text.toString().trim()
        ).enqueue(object : Callback<OperatorModel> {
            override fun onResponse(call: Call<OperatorModel>, response: Response<OperatorModel>) {
                binding.progressBar.visibility = View.GONE
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
                Toast.makeText(this@LoginActivity, "User gagal login! Pastikan Id dan Password Sudah Sesuai", Toast.LENGTH_SHORT).show()
                binding.progressBar.visibility = View.GONE
                Log.d("Gagal : ", t.message.toString())
            }
        })

    }

    private fun loginSpkt(opsi: String) {
        ApiConfig.instance.loginSpkt(
                binding.edtId.text.toString().trim(),
                opsi,
                binding.edtPass.text.toString().trim()
        ).enqueue(object : Callback<SpktModel> {
            override fun onResponse(call: Call<SpktModel>, response: Response<SpktModel>) {
                binding.progressBar.visibility = View.GONE
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
                Toast.makeText(this@LoginActivity, "User gagal login! Pastikan Id dan Password Sudah Sesuai", Toast.LENGTH_SHORT).show()
                binding.progressBar.visibility = View.GONE
                Log.d("Gagal : ", t.message.toString())
            }
        })
    }

    private fun loginPolisi(opsi: String) {
        ApiConfig.instance.loginPolisi(
                binding.edtId.text.toString().trim(),
                opsi,
                binding.edtPass.text.toString().trim()
        ).enqueue(object : Callback<PolisiModel> {
            override fun onResponse(call: Call<PolisiModel>, response: Response<PolisiModel>) {
                binding.progressBar.visibility = View.GONE
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
            }

            override fun onFailure(call: Call<PolisiModel>, t: Throwable) {
                Toast.makeText(this@LoginActivity, "User gagal login! Pastikan Id dan Password Sudah Sesuai", Toast.LENGTH_SHORT).show()
                binding.progressBar.visibility = View.GONE
                Log.d("Gagal : ", t.message.toString())
            }
        })
    }

    private fun loginMasyarakat(opsi: String) {
        ApiConfig.instance.loginMasyarakat(
                binding.edtId.text.toString().trim(),
                opsi,
                binding.edtPass.text.toString().trim()
        ).enqueue(object : Callback<MasyarakatModel> {
            override fun onResponse(call: Call<MasyarakatModel>, response: Response<MasyarakatModel>) {
                binding.progressBar.visibility = View.GONE
                Toast.makeText(this@LoginActivity, "User berhasil login!", Toast.LENGTH_SHORT).show()
                val user = response.body()
                if (user != null) {
                    session.createLoginSession(user.passMsy, user.idMsy, user.namaMsy, user.notelpMsy)
                    val intent = Intent(this@LoginActivity, MenuMasyarakat::class.java)
                    startActivity(intent)
                    finish()
                } else {
                    Toast.makeText(applicationContext, "Login gagal", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<MasyarakatModel>, t: Throwable) {
                Toast.makeText(this@LoginActivity, "User gagal login! Pastikan Id dan Password Sudah Sesuai", Toast.LENGTH_SHORT).show()
                binding.progressBar.visibility = View.GONE
                Log.d("Gagal : ", t.message.toString())
            }
        })
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return super.onSupportNavigateUp()
    }
}