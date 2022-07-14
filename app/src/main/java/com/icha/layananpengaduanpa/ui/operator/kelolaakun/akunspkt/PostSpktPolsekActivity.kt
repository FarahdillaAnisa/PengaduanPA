package com.icha.layananpengaduanpa.ui.operator.kelolaakun.akunspkt

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.icha.layananpengaduanpa.databinding.ActivityPostSpktPolsekBinding
import com.icha.layananpengaduanpa.model.ApiConfig
import com.icha.layananpengaduanpa.model.PolisiModel
import com.icha.layananpengaduanpa.model.SpktModel
import com.icha.layananpengaduanpa.ui.operator.kelolaakun.akunpolisi.PostAkunPolisiActivity
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class PostSpktPolsekActivity : AppCompatActivity() {
    companion object {
        const val EXTRA_SPKT = "extra_spkt"
    }
    private lateinit var binding : ActivityPostSpktPolsekBinding
    private var isEdit = false
    private var aSpkt: SpktModel? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPostSpktPolsekBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        aSpkt = intent.getParcelableExtra(EXTRA_SPKT)
        if (aSpkt != null) {
            isEdit = true
            binding.btnDeletespkt.visibility = View.VISIBLE
            binding.btnPostspkt.setText("Perbaharui Akun")
            supportActionBar?.title = "Edit Data Akun SPKT"
            if (aSpkt != null) {
                aSpkt?.let { dataSpkt ->
                    binding.edtIdspkt.setText(dataSpkt.idSpkt)
                    binding.edtNama.setText(dataSpkt.unameSpkt)
                    binding.edtNotelp.setText(dataSpkt.notelpSpkt)
                    binding.edtSatuanwilayah.setText(dataSpkt.satuanWilayah)
                }
            }
        }

        binding.btnDeletespkt.setOnClickListener {
            deleteAkunSpkt()
        }
        binding.btnPostspkt.setOnClickListener {
            if (isEdit) {
                editAkunSpkt()
            } else {
                tambahAkunSpkt()
            }
        }
    }

    private fun editAkunSpkt() {
        ApiConfig.instance.editAkunSpkt(
                binding.edtIdspkt.text.toString(),
                binding.edtNama.text.toString(),
                binding.edtSatuanwilayah.text.toString(),
                binding.edtNotelp.text.toString()
//                binding.edtPassword.text.toString()
        ).enqueue(object : Callback<SpktModel> {
            override fun onResponse(call: Call<SpktModel>, response: Response<SpktModel>) {
                Toast.makeText(this@PostSpktPolsekActivity, "Spkt ${binding.edtNama.text} berhasil diperbaharui", Toast.LENGTH_SHORT).show()
            }

            override fun onFailure(call: Call<SpktModel>, t: Throwable) {
                Toast.makeText(this@PostSpktPolsekActivity, "Data Spkt gagal diperbaharui!", Toast.LENGTH_SHORT).show()
                Toast.makeText(this@PostSpktPolsekActivity, "Message : ${t.message}", Toast.LENGTH_SHORT).show()
            }

        })
    }

    private fun deleteAkunSpkt() {
        ApiConfig.instance.deleteAkunSpkt(
                binding.edtIdspkt.text.toString()
        ).enqueue(object: Callback<SpktModel> {
            override fun onResponse(call: Call<SpktModel>, response: Response<SpktModel>) {
                Toast.makeText(this@PostSpktPolsekActivity, "Akun Spkt ${binding.edtNama.text} berhasil dihapus", Toast.LENGTH_SHORT).show()
            }

            override fun onFailure(call: Call<SpktModel>, t: Throwable) {
                Toast.makeText(this@PostSpktPolsekActivity, "Data Spkt gagal diperbaharui!", Toast.LENGTH_SHORT).show()
                Toast.makeText(this@PostSpktPolsekActivity, "Message : ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun tambahAkunSpkt() {
        ApiConfig.instance.tambahAkunSpkt(
                "id_spkt",
                binding.edtNama.text.toString(),
                binding.edtSatuanwilayah.text.toString(),
                binding.edtPassword.text.toString(),
                binding.edtNotelp.text.toString()
        ).enqueue(object : Callback<SpktModel> {
            override fun onResponse(call: Call<SpktModel>, response: Response<SpktModel>) {
                Toast.makeText(this@PostSpktPolsekActivity, "SPKT ${binding.edtNama.text} berhasil didaftarkan", Toast.LENGTH_SHORT).show()
//                val intent = Intent(this@PostAkunPolisiActivity, LoginActivity::class.java)
//                startActivity(intent)
            }

            override fun onFailure(call: Call<SpktModel>, t: Throwable) {
                Toast.makeText(this@PostSpktPolsekActivity, "Data SPKT gagal disimpan!", Toast.LENGTH_SHORT).show()
                Toast.makeText(this@PostSpktPolsekActivity, "Message : ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return super.onSupportNavigateUp()
    }
}