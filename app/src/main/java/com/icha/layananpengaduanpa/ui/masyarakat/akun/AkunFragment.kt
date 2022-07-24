package com.icha.layananpengaduanpa.ui.masyarakat.akun

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.icha.layananpengaduanpa.databinding.FragmentAkunBinding
import com.icha.layananpengaduanpa.model.ApiConfig
import com.icha.layananpengaduanpa.model.MasyarakatModel
import com.icha.layananpengaduanpa.session.SessionManager
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class AkunFragment : Fragment() {

    lateinit var session: SessionManager
    private lateinit var binding : FragmentAkunBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentAkunBinding.inflate(inflater, container, false)
        val view = binding.root
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        session = SessionManager(view.context)

        val user: HashMap<String, String> = session.getUserDetails()
        val id: String = user.get(SessionManager.KEY_ID)!!
        binding.edtId.setText(id)
        detailAkun(id)

        binding.btnUpdate.setOnClickListener {
            updateAkun(id)
        }
        binding.btnUpdatePass.setOnClickListener {
            if (validasiPassword() == true) {
                updatePassAkun(id)
            }

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

    private fun updatePassAkun(id: String) {
        ApiConfig.instance.updatePassMsy(
                id,
                binding.edtPassword.text.toString()
        ).enqueue(object: Callback<MasyarakatModel> {
            override fun onResponse(call: Call<MasyarakatModel>, response: Response<MasyarakatModel>) {
                if (response.isSuccessful) {
                    Toast.makeText(context, "Password Akun $id berhasil diperbaharui", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<MasyarakatModel>, t: Throwable) {
                Toast.makeText(context, "Password Akun $id gagal diperbaharui", Toast.LENGTH_SHORT).show()
            }

        })
    }

    private fun updateAkun(id_msy: String) {
        ApiConfig.instance.updateAkunMsy(
                id_msy,
                binding.edtNama.text.toString(),
                binding.edtNotelp.text.toString()
        ).enqueue(object : Callback<MasyarakatModel> {
            override fun onResponse(call: Call<MasyarakatModel>, response: Response<MasyarakatModel>) {
                if (response.isSuccessful) {
                    Toast.makeText(context, "Akun $id_msy berhasil diperbaharui", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<MasyarakatModel>, t: Throwable) {
                Toast.makeText(context, "Akun $id_msy gagal diperbaharui", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun detailAkun(id_msy: String) {
        ApiConfig.instance.getAkunMsy(id_msy)
            .enqueue(object: Callback<MasyarakatModel> {
                override fun onResponse(
                    call: Call<MasyarakatModel>,
                    response: Response<MasyarakatModel>
                ) {
                    if (response.isSuccessful) {
                        val dataAkun = response.body()
                        dataAkun?.let { data->
                            binding.edtId.setText(data.idMsy)
                            binding.edtNama.setText(data.namaMsy)
                            binding.edtNotelp.setText(data.notelpMsy)
                        }
                    } else {
                        Toast.makeText(context, "Gagal Menampilkan Data", Toast.LENGTH_SHORT).show()
                    }
                }

                override fun onFailure(call: Call<MasyarakatModel>, t: Throwable) {
                    val responseCode = t.message
                    Toast.makeText(context, responseCode, Toast.LENGTH_SHORT).show()
                }
            })
    }
}