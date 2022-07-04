package com.icha.layananpengaduanpa.ui.operator.kelolaakun.akunpolisi

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.icha.layananpengaduanpa.databinding.ActivityPostAkunPolisiBinding
import com.icha.layananpengaduanpa.model.ApiConfig
import com.icha.layananpengaduanpa.model.PolisiModel
import com.icha.layananpengaduanpa.ui.masyarakat.pengaduan.postaduan.DetailAduanActivity
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class PostAkunPolisiActivity : AppCompatActivity() {
    companion object {
//        const val EXTRA_POLISI = "extra_polisi"
        const val EXTRA_ID = "extra_id"
    }

    private lateinit var binding : ActivityPostAkunPolisiBinding
    private var isEdit = false
//    private var aPolisi: PolisiModel? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPostAkunPolisiBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
//        aPolisi = intent.getParcelableExtra(EXTRA_POLISI)
        val bundle : Bundle? = intent.extras
        if (bundle?.containsKey(EXTRA_ID)!!) {
            val aPolisi = intent.getStringExtra(EXTRA_ID)
            if (aPolisi != null) {
                isEdit = true
                binding.btnDeletepolisi.visibility = View.VISIBLE
                binding.btnPostpolisi.setText("Perbaharui Akun")
                supportActionBar?.title = "Edit Data Akun Polisi"
                getAkunPolisi(aPolisi)
            }
        }

        binding.btnDeletepolisi.setOnClickListener {
            deleteAkunPolisi()
        }

        binding.btnPostpolisi.setOnClickListener {
            if (isEdit) {
                editAkunPolisi()
            } else {
                tambahAkunPolisi()
            }

        }
    }

    private fun getAkunPolisi(id: String) {
        ApiConfig.instance.getAkunPolisiById(id)
                .enqueue(object: Callback<PolisiModel> {
                    override fun onResponse(call: Call<PolisiModel>, response: Response<PolisiModel>) {
                        if (response.isSuccessful) {
                            val dataAkun = response.body()
                            dataAkun?.let {data->
                                binding.edtIdpolisi.setText(data.idPolisi)
                                binding.edtNama.setText(data.namaPolisi)
                                binding.edtNotelp.setText(data.notelpPolisi)
                                binding.edtSatuanwilayah.setText(data.satuanWilayah)
                                //PASSWORD
                            }
                        }
                        else{
                            Toast.makeText(this@PostAkunPolisiActivity, "Gagal Mengambil Data", Toast.LENGTH_SHORT).show()
                        }
                    }

                    override fun onFailure(call: Call<PolisiModel>, t: Throwable) {
                        Toast.makeText(this@PostAkunPolisiActivity, "Kode : $id", Toast.LENGTH_SHORT).show()
                        val responseCode = t.message
                        Toast.makeText(this@PostAkunPolisiActivity, responseCode, Toast.LENGTH_SHORT).show()
                    }

                })
    }

    private fun deleteAkunPolisi() {
        ApiConfig.instance.deleteAkunPolisi(
            binding.edtIdpolisi.text.toString()
        ).enqueue(object: Callback<PolisiModel> {
            override fun onResponse(call: Call<PolisiModel>, response: Response<PolisiModel>) {
                Toast.makeText(this@PostAkunPolisiActivity, "Akun Polisi ${binding.edtIdpolisi.text} berhasil dihapus", Toast.LENGTH_SHORT).show()
            }

            override fun onFailure(call: Call<PolisiModel>, t: Throwable) {
                Toast.makeText(this@PostAkunPolisiActivity, "Data Polisi gagal diperbaharui!", Toast.LENGTH_SHORT).show()
                Toast.makeText(this@PostAkunPolisiActivity, "Message : ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun editAkunPolisi() {
        ApiConfig.instance.editAkunPolisi(
            binding.edtIdpolisi.text.toString(),
            binding.edtNama.text.toString(),
            binding.edtSatuanwilayah.text.toString(),
            binding.edtNotelp.text.toString()
//            binding.edtPassword.text.toString()
        ).enqueue(object : Callback<PolisiModel> {
            override fun onResponse(call: Call<PolisiModel>, response: Response<PolisiModel>) {
                Toast.makeText(this@PostAkunPolisiActivity, "Polisi ${binding.edtNama.text} berhasil diperbaharui", Toast.LENGTH_SHORT).show()
            }

            override fun onFailure(call: Call<PolisiModel>, t: Throwable) {
                Toast.makeText(this@PostAkunPolisiActivity, "Data Polisi gagal diperbaharui!", Toast.LENGTH_SHORT).show()
                Toast.makeText(this@PostAkunPolisiActivity, "Message : ${t.message}", Toast.LENGTH_SHORT).show()
            }

        })
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