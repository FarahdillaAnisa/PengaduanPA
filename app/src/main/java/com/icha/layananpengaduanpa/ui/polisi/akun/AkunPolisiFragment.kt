package com.icha.layananpengaduanpa.ui.polisi.akun

import android.annotation.SuppressLint
import android.graphics.Color
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
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

        binding.edtNama.addTextChangedListener(checkNull)
        binding.edtNotelp.addTextChangedListener(checkNull)
        binding.btnUpdate.setOnClickListener {
            updateSpkt(idPolisi)
        }

        binding.btnUpdatePass.setOnClickListener {
            if (validasiPassword() == true) {
                updatePassPolisi(idPolisi)
            }
        }
    }

    private fun updatePassPolisi(id: String) {
        ApiConfig.instance.editPassPolisi(
                id,
                binding.edtPassword.text.toString()
        ).enqueue(object : Callback<PolisiModel> {
            override fun onResponse(call: Call<PolisiModel>, response: Response<PolisiModel>) {
                if (response.isSuccessful) {
                    Toast.makeText(context, "Password Akun $id berhasil diperbaharui", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<PolisiModel>, t: Throwable) {
                Toast.makeText(context, "Password Akun $id gagal diperbaharui", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private val checkNull = object: TextWatcher {
        override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
        }

        override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            val nama: String = binding.edtNama.text.toString().trim()
            val notelp : String = binding.edtNotelp.text.toString().trim()
            binding.btnUpdate.isEnabled = !nama.isEmpty() && !notelp.isEmpty()
        }

        override fun afterTextChanged(p0: Editable?) {
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