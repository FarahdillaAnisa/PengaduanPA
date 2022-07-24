package com.icha.layananpengaduanpa.ui.polisi.akun

import android.annotation.SuppressLint
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.icha.layananpengaduanpa.databinding.FragmentAkunPolisiBinding
import com.icha.layananpengaduanpa.model.ApiConfig
import com.icha.layananpengaduanpa.model.PolisiModel
import com.icha.layananpengaduanpa.model.SpktModel
import com.icha.layananpengaduanpa.session.SessionManager
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class AkunPolisiFragment : Fragment() {

    lateinit var session: SessionManager
    private lateinit var binding: FragmentAkunPolisiBinding

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        binding = FragmentAkunPolisiBinding.inflate(inflater, container, false)
        val view = binding.root
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        session = SessionManager(view.context)

        val polisi: HashMap<String, String> = session.getUserDetails()
        val idPolisi: String = polisi.get(SessionManager.KEY_ID)!!
        detailAkun(idPolisi)

        binding.btnUpdate.setOnClickListener {
            updateSpkt(idPolisi)
        }
    }

    private fun updateSpkt(id: String) {
        ApiConfig.instance.editAkunPolisi(
                id,
                binding.edtNama.text.toString(),
                binding.edtSatwil.text.toString(),
                binding.edtNotelp.text.toString()
        ).enqueue(object: Callback<PolisiModel> {
            override fun onResponse(call: Call<PolisiModel>, response: Response<PolisiModel>) {
                if (response.isSuccessful) {
                    Toast.makeText(context, "Akun $id berhasil diperbaharui", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<PolisiModel>, t: Throwable) {
                Toast.makeText(context, "Akun $id gagal diperbaharui", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun detailAkun(id: String) {
        ApiConfig.instance.getAkunPolisiById(id)
                .enqueue(object: Callback<PolisiModel> {
                    override fun onResponse(call: Call<PolisiModel>, response: Response<PolisiModel>) {
                        if (response.isSuccessful) {
                            val dataPolisi = response.body()
                            dataPolisi?.let { polisi ->
                                binding.edtId.setText(polisi.idPolisi)
                                binding.edtNama.setText(polisi.namaPolisi)
                                binding.edtSatwil.setText(polisi.satuanWilayah)
                                binding.edtNotelp.setText(polisi.notelpPolisi)
                            }
                        } else {
                            Toast.makeText(context, "Gagal Menampilkan Data", Toast.LENGTH_SHORT).show()
                        }
                    }

                    override fun onFailure(call: Call<PolisiModel>, t: Throwable) {
                        Toast.makeText(context, "Gagal Menampilkan Data", Toast.LENGTH_SHORT).show()
                    }
                })
    }

    @SuppressLint("SetTextI18n")
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
}