package com.icha.layananpengaduanpa.ui.operator.kelolaakun

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.icha.layananpengaduanpa.R
import com.icha.layananpengaduanpa.databinding.ActivityEditPolisiSpktBinding
import com.icha.layananpengaduanpa.model.ApiConfig
import com.icha.layananpengaduanpa.model.PolisiModel
import com.icha.layananpengaduanpa.model.SpktModel
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class EditPolisiSpktActivity : AppCompatActivity() {
    companion object {
        const val EXTRA_ID = "extra_id"
        const val EXTRA_ROLE = "role_user"
    }

    private lateinit var binding: ActivityEditPolisiSpktBinding
    private var isEdit = false
    private var role : String? = ""
    private var id_akun: String? = ""
    private var kecamatan: String? = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEditPolisiSpktBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        val bundle: Bundle? = intent.extras
        if (bundle!!.containsKey(EXTRA_ID)) {
            role = intent.getStringExtra(EXTRA_ROLE)
            id_akun = intent.getStringExtra(EXTRA_ID)
            if (role != null && id_akun != null) {
                if (role == "polisi" && id_akun != null) {
                    supportActionBar?.title = "Edit Data Akun Polisi"
                    getAkunPolisi(id_akun!!)

                } else if (role == "spkt" && id_akun != null) {
                    supportActionBar?.title = "Edit Data Akun Spkt"
                    getAkunSpkt(id_akun!!)
                }
            }
        }

        binding.btnPost.setOnClickListener {
            binding.progressBar.visibility = View.VISIBLE
            if (role == "polisi") {
                editAkunPolisi()
            } else if (role == "spkt") {
                editAkunSpkt()
            }
        }
    }

    private fun editAkunSpkt() {
        ApiConfig.instance.editAkunSpkt(
                binding.edtIdpolisi.text.toString(),
                binding.edtNama.text.toString(),
                kecamatan.toString(),
//                binding.edtPassword.text.toString(),
                binding.edtNotelp.text.toString()
        ).enqueue(object : Callback<SpktModel> {
            override fun onResponse(call: Call<SpktModel>, response: Response<SpktModel>) {
                binding.progressBar.visibility = View.GONE
                Toast.makeText(this@EditPolisiSpktActivity, "Spkt ${binding.edtNama.text} berhasil diperbaharui", Toast.LENGTH_SHORT).show()
//                val intent = Intent(this@PostAkunPolisiActivity, KelolaAkunFragment::class.java)
//                startActivity(intent)
            }

            override fun onFailure(call: Call<SpktModel>, t: Throwable) {
                Toast.makeText(this@EditPolisiSpktActivity, "Data Spkt gagal diperbaharui!", Toast.LENGTH_SHORT).show()
                Toast.makeText(this@EditPolisiSpktActivity, "Message : ${t.message}", Toast.LENGTH_SHORT).show()
            }

        })
    }

    private fun getAkunSpkt(idAkun: String) {
        ApiConfig.instance.getAkunSpktById(idAkun)
                .enqueue(object : Callback<SpktModel> {
                    override fun onResponse(call: Call<SpktModel>, response: Response<SpktModel>) {
                        binding.progressBar.visibility = View.GONE
                        if (response.isSuccessful) {
                            val dataAkun = response.body()
                            dataAkun?.let { data ->
                                binding.edtIdpolisi.setText(data.idSpkt)
                                binding.edtNama.setText(data.unameSpkt)
                                binding.edtNotelp.setText(data.notelpSpkt)
                                binding.edtSatwil.setText(data.satuanWilayah)
                                binding.edtPassword.setText(data.pass_awal)
                            }
                        } else {
                            Toast.makeText(this@EditPolisiSpktActivity, "Gagal Mengambil Data", Toast.LENGTH_SHORT).show()
                        }
                    }

                    override fun onFailure(call: Call<SpktModel>, t: Throwable) {
                        Toast.makeText(this@EditPolisiSpktActivity, "Kode : $idAkun", Toast.LENGTH_SHORT).show()
                        val responseCode = t.message
                        Toast.makeText(this@EditPolisiSpktActivity, responseCode, Toast.LENGTH_SHORT).show()
                    }
                })
    }

    private fun getAkunPolisi(id: String) {
        ApiConfig.instance.getAkunPolisiById(id)
                .enqueue(object : Callback<PolisiModel> {
                    override fun onResponse(call: Call<PolisiModel>, response: Response<PolisiModel>) {
                        binding.progressBar.visibility = View.GONE
                        if (response.isSuccessful) {
                            val dataAkun = response.body()
                            dataAkun?.let { data ->
                                binding.edtIdpolisi.setText(data.idPolisi)
                                binding.edtNama.setText(data.namaPolisi)
                                binding.edtNotelp.setText(data.notelpPolisi)
                                binding.edtSatwil.setText(data.satuanWilayah)
                                binding.edtPassword.setText(data.pass_awal)
                            }
                        } else {
                            Toast.makeText(this@EditPolisiSpktActivity, "Gagal Mengambil Data", Toast.LENGTH_SHORT).show()
                        }
                    }

                    override fun onFailure(call: Call<PolisiModel>, t: Throwable) {
                        Toast.makeText(this@EditPolisiSpktActivity, "Kode : $id", Toast.LENGTH_SHORT).show()
                        val responseCode = t.message
                        Toast.makeText(this@EditPolisiSpktActivity, responseCode, Toast.LENGTH_SHORT).show()
                    }

                })
    }

    private fun editAkunPolisi() {
        ApiConfig.instance.editAkunPolisi(
                binding.edtIdpolisi.text.toString(),
                binding.edtNama.text.toString(),
                kecamatan.toString(),
                binding.edtNotelp.text.toString()
//            binding.edtPassword.text.toString()
        ).enqueue(object : Callback<PolisiModel> {
            override fun onResponse(call: Call<PolisiModel>, response: Response<PolisiModel>) {
                binding.progressBar.visibility = View.GONE
                Toast.makeText(this@EditPolisiSpktActivity, "Polisi ${binding.edtNama.text} berhasil diperbaharui", Toast.LENGTH_SHORT).show()
//                val intent = Intent(this@PostAkunPolisiActivity, KelolaAkunFragment::class.java)
//                startActivity(intent)
            }

            override fun onFailure(call: Call<PolisiModel>, t: Throwable) {
                Toast.makeText(this@EditPolisiSpktActivity, "Data Polisi gagal diperbaharui!", Toast.LENGTH_SHORT).show()
                Toast.makeText(this@EditPolisiSpktActivity, "Message : ${t.message}", Toast.LENGTH_SHORT).show()
            }

        })
    }
}