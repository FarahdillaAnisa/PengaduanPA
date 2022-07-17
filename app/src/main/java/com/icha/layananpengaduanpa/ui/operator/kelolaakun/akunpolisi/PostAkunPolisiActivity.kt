package com.icha.layananpengaduanpa.ui.operator.kelolaakun.akunpolisi

import android.content.DialogInterface
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.icha.layananpengaduanpa.databinding.ActivityPostAkunPolisiBinding
import com.icha.layananpengaduanpa.helper.Helper
import com.icha.layananpengaduanpa.model.ApiConfig
import com.icha.layananpengaduanpa.model.PolisiModel
import com.icha.layananpengaduanpa.model.SpktModel
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class PostAkunPolisiActivity : AppCompatActivity() {
    companion object {
        //        const val EXTRA_POLISI = "extra_polisi"
        const val EXTRA_ID = "extra_id"
        const val EXTRA_ROLE = "role_user"
    }

    private lateinit var binding: ActivityPostAkunPolisiBinding
    private var isEdit = false
    private var role : String? = ""
    private var id_akun: String? = ""
    //    private var aPolisi: PolisiModel? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPostAkunPolisiBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
//        aPolisi = intent.getParcelableExtra(EXTRA_POLISI)

        MaterialAlertDialogBuilder(this)
            .setTitle("Perhatian!")
            .setMessage("Pilih salah satu diantara opsi akun Polisi" +
                    "atau SPKT di kolom “Jenis Akun (Polisi / SPKT)“ " +
                    "terlebih dahulu")
            .setNeutralButton("Lanjutkan", object : DialogInterface.OnClickListener{
                override fun onClick(p0: DialogInterface?, p1: Int) {
                }
            })
            .show()

        val bundle: Bundle? = intent.extras
        if (bundle?.containsKey(EXTRA_ID)!! && bundle.containsKey(EXTRA_ROLE)) {
            role = intent.getStringExtra(EXTRA_ROLE)
            id_akun = intent.getStringExtra(EXTRA_ID)
            if (role == "polisi") {
                if (id_akun != null) {
                    isEdit = true
//                    binding.btnDeletepolisi.visibility = View.VISIBLE
                    binding.btnPostpolisi.setText("Perbaharui Akun")
                    supportActionBar?.title = "Edit Data Akun Polisi"
                    getAkunPolisi(id_akun!!)
                }
            } else if (role == "spkt") {
                if (id_akun != null) {
                    isEdit = true
//                    binding.btnDeletepolisi.visibility = View.VISIBLE
                    binding.btnPostpolisi.setText("Perbaharui Akun")
                    supportActionBar?.title = "Edit Data Akun Spkt"
                    getAkunSpkt(id_akun!!)
                }
            }
        }

//        binding.btnDeletepolisi.setOnClickListener {
//            if (role == "polisi" && isEdit) {
//                deleteAkunPolisi(id_akun!!)
//            } else if (role == "spkt" && isEdit){
//                deleteAkunSpkt(id_akun!!)
//            }
//        }

        binding.btnPostpolisi.setOnClickListener {
            binding.progressBar.visibility = View.VISIBLE
            if (role == "polisi") {
                if (isEdit) {
                    editAkunPolisi()
                } else {
                    tambahAkunPolisi()
                }
            } else if (role == "spkt") {
                if (isEdit) {
                    editAkunSpkt()
                } else {
                    tambahAkunSpkt()
                }
            }
        }
    }

    private fun tambahAkunSpkt() {
        val getIdRandom = Helper()
        val id_spkt = getIdRandom.getRandomId(3, "spkt")
        binding.edtIdpolisi.setText(id_spkt)
        ApiConfig.instance.tambahAkunSpkt(
                id_spkt,
                binding.edtNama.text.toString(),
                binding.edtSatuanwilayah.text.toString(),
                binding.edtPassword.text.toString(),
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

    private fun editAkunSpkt() {
        ApiConfig.instance.editAkunSpkt(
                binding.edtIdpolisi.text.toString(),
                binding.edtNama.text.toString(),
                binding.edtSatuanwilayah.text.toString(),
//                binding.edtPassword.text.toString(),
                binding.edtNotelp.text.toString()
        ).enqueue(object : Callback<SpktModel> {
            override fun onResponse(call: Call<SpktModel>, response: Response<SpktModel>) {
                binding.progressBar.visibility = View.GONE
                Toast.makeText(this@PostAkunPolisiActivity, "Spkt ${binding.edtNama.text} berhasil diperbaharui", Toast.LENGTH_SHORT).show()
//                val intent = Intent(this@PostAkunPolisiActivity, KelolaAkunFragment::class.java)
//                startActivity(intent)
            }

            override fun onFailure(call: Call<SpktModel>, t: Throwable) {
                Toast.makeText(this@PostAkunPolisiActivity, "Data Spkt gagal diperbaharui!", Toast.LENGTH_SHORT).show()
                Toast.makeText(this@PostAkunPolisiActivity, "Message : ${t.message}", Toast.LENGTH_SHORT).show()
            }

        })
    }

    private fun deleteAkunSpkt(idAkun: String) {
        ApiConfig.instance.deleteAkunSpkt(
                idAkun
        ).enqueue(object: Callback<SpktModel> {
            override fun onResponse(call: Call<SpktModel>, response: Response<SpktModel>) {
                binding.progressBar.visibility = View.GONE
                Toast.makeText(this@PostAkunPolisiActivity, "Akun Spkt ${binding.edtNama.text} berhasil dihapus", Toast.LENGTH_SHORT).show()
//                val intent = Intent(this@PostAkunPolisiActivity, KelolaAkunFragment::class.java)
//                startActivity(intent)
            }

            override fun onFailure(call: Call<SpktModel>, t: Throwable) {
                Toast.makeText(this@PostAkunPolisiActivity, "Data Spkt gagal diperbaharui!", Toast.LENGTH_SHORT).show()
                Toast.makeText(this@PostAkunPolisiActivity, "Message : ${t.message}", Toast.LENGTH_SHORT).show()
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
                                binding.edtSatuanwilayah.setText(data.satuanWilayah)
                                //PASSWORD
                            }
                        } else {
                            Toast.makeText(this@PostAkunPolisiActivity, "Gagal Mengambil Data", Toast.LENGTH_SHORT).show()
                        }
                    }

                    override fun onFailure(call: Call<SpktModel>, t: Throwable) {
                        Toast.makeText(this@PostAkunPolisiActivity, "Kode : $idAkun", Toast.LENGTH_SHORT).show()
                        val responseCode = t.message
                        Toast.makeText(this@PostAkunPolisiActivity, responseCode, Toast.LENGTH_SHORT).show()
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
                                binding.edtSatuanwilayah.setText(data.satuanWilayah)
                                //PASSWORD
                            }
                        } else {
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

    private fun deleteAkunPolisi(idAkun: String) {
        ApiConfig.instance.deleteAkunPolisi(idAkun)
                .enqueue(object : Callback<PolisiModel> {
            override fun onResponse(call: Call<PolisiModel>, response: Response<PolisiModel>) {
                binding.progressBar.visibility = View.GONE
                Toast.makeText(this@PostAkunPolisiActivity, "Akun Polisi ${binding.edtIdpolisi.text} berhasil dihapus", Toast.LENGTH_SHORT).show()
//                val intent = Intent(this@PostAkunPolisiActivity, KelolaAkunFragment::class.java)
//                startActivity(intent)
            }

            override fun onFailure(call: Call<PolisiModel>, t: Throwable) {
                Toast.makeText(this@PostAkunPolisiActivity, "Data Polisi gagal dihapus!", Toast.LENGTH_SHORT).show()
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
                binding.progressBar.visibility = View.GONE
                Toast.makeText(this@PostAkunPolisiActivity, "Polisi ${binding.edtNama.text} berhasil diperbaharui", Toast.LENGTH_SHORT).show()
//                val intent = Intent(this@PostAkunPolisiActivity, KelolaAkunFragment::class.java)
//                startActivity(intent)
            }

            override fun onFailure(call: Call<PolisiModel>, t: Throwable) {
                Toast.makeText(this@PostAkunPolisiActivity, "Data Polisi gagal diperbaharui!", Toast.LENGTH_SHORT).show()
                Toast.makeText(this@PostAkunPolisiActivity, "Message : ${t.message}", Toast.LENGTH_SHORT).show()
            }

        })
    }

    private fun tambahAkunPolisi() {
        val getIdRandom = Helper()
        val id_polisi = getIdRandom.getRandomId(3, "polisi", )
        binding.edtIdpolisi.setText(id_polisi)

        //yes/no dialog box

        ApiConfig.instance.tambahAkunPolisi(
                id_polisi,
                binding.edtNama.text.toString(),
                binding.edtSatuanwilayah.text.toString(),
                binding.edtNotelp.text.toString(),
                binding.edtPassword.text.toString()
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


