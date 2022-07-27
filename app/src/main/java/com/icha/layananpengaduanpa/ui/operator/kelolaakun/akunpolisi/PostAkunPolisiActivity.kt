package com.icha.layananpengaduanpa.ui.operator.kelolaakun.akunpolisi

import android.content.DialogInterface
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.Toast
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.icha.layananpengaduanpa.R
import com.icha.layananpengaduanpa.databinding.ActivityPostAkunPolisiBinding
import com.icha.layananpengaduanpa.helper.Helper
import com.icha.layananpengaduanpa.model.ApiConfig
import com.icha.layananpengaduanpa.model.PolisiModel
import com.icha.layananpengaduanpa.model.SpktModel
import com.icha.layananpengaduanpa.ui.masyarakat.pengaduan.postaduan.DetailAduanActivity
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class PostAkunPolisiActivity : AppCompatActivity() {
    private lateinit var binding: ActivityPostAkunPolisiBinding
    private var kecamatan: String? = ""
    private var opsiAkun: String = ""
    private var id_pengguna: String = ""
    private var passRandom: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPostAkunPolisiBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        MaterialAlertDialogBuilder(this)
                .setTitle("Perhatian!")
                .setMessage("Pilih salah satu diantara opsi akun yang ditambahkan" +
                        "di kolom “Jenis Akun (Polisi / SPKT / Operator)“ " +
                        "terlebih dahulu")
                .setNeutralButton("Lanjutkan", object : DialogInterface.OnClickListener{
                    override fun onClick(p0: DialogInterface?, p1: Int) {
                    }
                })
                .show()

        spinnerKecamatan()
        cekOpsiAkun()

        binding.btnPost.setOnClickListener {
            binding.progressBar.visibility = View.VISIBLE
            if (opsiAkun == "Polisi") {
                tambahAkunPolisi()
            } else if (opsiAkun == "SPKT Polsek") {
                tambahAkunSpkt()
            }
        }
    }

    private fun cekOpsiAkun() {


    }

    private fun spinnerKecamatan() {
        val helper = Helper()
        passRandom = helper.getRandomPassword()
        binding.edtPassword.setText(passRandom)
        val kecamatanOpsi = resources.getStringArray(R.array.kecamatan)
        val akunOpsi = resources.getStringArray(R.array.opsi_tambah_akun)
        binding.satuanwilayah.onItemSelectedListener = object :
                AdapterView.OnItemSelectedListener {
            override fun onItemSelected(p0: AdapterView<*>?, view: View?, position: Int, id: Long) {
                kecamatan = kecamatanOpsi[position].toString()
            }

            override fun onNothingSelected(p0: AdapterView<*>?) {
            }
        }

        binding.opsiAkun.onItemSelectedListener = object :
        AdapterView.OnItemSelectedListener {
            override fun onItemSelected(p0: AdapterView<*>?, view: View?, position: Int, id: Long) {
                opsiAkun = akunOpsi[position]
                Toast.makeText(this@PostAkunPolisiActivity, opsiAkun, Toast.LENGTH_SHORT).show()
                if (opsiAkun == "Polisi") {
                    id_pengguna = helper.getRandomId(3, "polisi" )
                    Toast.makeText(this@PostAkunPolisiActivity, id_pengguna, Toast.LENGTH_SHORT).show()
                    binding.edtId.setText(id_pengguna)
                } else if (opsiAkun == "SPKT Polsek") {
                    id_pengguna = helper.getRandomId(3, "spkt" )
                    binding.edtId.setText(id_pengguna)
                }


            }

            override fun onNothingSelected(p0: AdapterView<*>?) {
            }
        }
    }

    private fun tambahAkunSpkt() {
        ApiConfig.instance.tambahAkunSpkt(
                id_pengguna,
                binding.edtNama.text.toString(),
                kecamatan.toString(),
                passRandom,
                passRandom,
                binding.edtNotelp.text.toString()
        ).enqueue(object : Callback<SpktModel> {
            override fun onResponse(call: Call<SpktModel>, response: Response<SpktModel>) {
                binding.progressBar.visibility = View.GONE
                Toast.makeText(this@PostAkunPolisiActivity, "SPKT ${binding.edtNama.text} berhasil didaftarkan", Toast.LENGTH_SHORT).show()
//                val intent = Intent(this@PostAkunPolisiActivity, KelolaAkunFragment::class.java)
//                startActivity(intent)
            }

            override fun onFailure(call: Call<SpktModel>, t: Throwable) {
                Toast.makeText(this@PostAkunPolisiActivity, "Data SPKT gagal disimpan!", Toast.LENGTH_SHORT).show()
                Toast.makeText(this@PostAkunPolisiActivity, "Message : ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun tambahAkunPolisi() {
        //yes/no dialog box

        ApiConfig.instance.tambahAkunPolisi(
                id_pengguna,
                binding.edtNama.text.toString(),
                kecamatan.toString(),
                binding.edtNotelp.text.toString(),
                passRandom,
                passRandom
        ).enqueue(object : Callback<PolisiModel> {
            override fun onResponse(call: Call<PolisiModel>, response: Response<PolisiModel>) {
                binding.progressBar.visibility = View.GONE
                Toast.makeText(this@PostAkunPolisiActivity, "Polisi ${binding.edtNama.text} berhasil didaftarkan", Toast.LENGTH_SHORT).show()
//                val intent = Intent(this@PostAkunPolisiActivity, KelolaAkunFragment::class.java)
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


