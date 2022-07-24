package com.icha.layananpengaduanpa.ui.spktpolsek.akun

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.icha.layananpengaduanpa.databinding.FragmentAkunSpktBinding
import com.icha.layananpengaduanpa.model.ApiConfig
import com.icha.layananpengaduanpa.model.SpktModel
import com.icha.layananpengaduanpa.session.SessionManager
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class AkunFragment : Fragment() {

  lateinit var session: SessionManager
  private lateinit var binding : FragmentAkunSpktBinding

  override fun onCreateView(
    inflater: LayoutInflater,
    container: ViewGroup?,
    savedInstanceState: Bundle?
  ): View? {
    binding = FragmentAkunSpktBinding.inflate(inflater, container, false)
    val view = binding.root
    return view
  }

  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)
    session = SessionManager(view.context)
    val user: HashMap<String, String> = session.getUserDetails()
    val id: String = user.get(SessionManager.KEY_ID)!!
    val nama: String = user.get(SessionManager.KEY_NAMA)!!
    binding.edtNama.setText(nama)
    detailAkun(id)

    binding.btnUpdate.setOnClickListener { 
      updateSpkt(id)
    }

    binding.btnUpdatePass.setOnClickListener {
      if (validasiPassword() == true) {
        updatePassSpkt(id)
      }
    }
  }

  private fun updatePassSpkt(id: String) {
    ApiConfig.instance.editPassSpkt(
            id,
            binding.edtPassword.text.toString()
    ).enqueue(object : Callback<SpktModel> {
      override fun onResponse(call: Call<SpktModel>, response: Response<SpktModel>) {
        if (response.isSuccessful) {
          Toast.makeText(context, "Password Akun $id berhasil diperbaharui", Toast.LENGTH_SHORT).show()
        }
      }

      override fun onFailure(call: Call<SpktModel>, t: Throwable) {
        Toast.makeText(context, "Password Akun $id gagal diperbaharui", Toast.LENGTH_SHORT).show()
      }

    })
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

  private fun updateSpkt(id: String) {
    ApiConfig.instance.editAkunSpkt(
            id,
            binding.edtNama.text.toString(),
            binding.edtSatwil.text.toString(),
            binding.edtNotelp.text.toString()
    ).enqueue(object: Callback<SpktModel> {
      override fun onResponse(call: Call<SpktModel>, response: Response<SpktModel>) {
        if (response.isSuccessful) {
          Toast.makeText(context, "Akun $id berhasil diperbaharui", Toast.LENGTH_SHORT).show()
        }
      }

      override fun onFailure(call: Call<SpktModel>, t: Throwable) {
        Toast.makeText(context, "Akun $id gagal diperbaharui", Toast.LENGTH_SHORT).show()
      }
    })
  }

  private fun detailAkun(id: String) {
    ApiConfig.instance.getAkunSpktById(id)
            .enqueue(object: Callback<SpktModel> {
              override fun onResponse(call: Call<SpktModel>, response: Response<SpktModel>) {
                if (response.isSuccessful) {
                  val dataSpkt = response.body()
                  dataSpkt?.let { spkt ->
                    binding.edtId.setText(spkt.idSpkt)
                    binding.edtNama.setText(spkt.unameSpkt)
                    binding.edtSatwil.setText(spkt.satuanWilayah)
                    binding.edtNotelp.setText(spkt.notelpSpkt)
                  }
                } else {
                  Toast.makeText(context, "Gagal Menampilkan Data", Toast.LENGTH_SHORT).show()
                }
              }

              override fun onFailure(call: Call<SpktModel>, t: Throwable) {
                Toast.makeText(context, "Gagal Menampilkan Data", Toast.LENGTH_SHORT).show()
              }
            })

  }
}