package com.icha.layananpengaduanpa.ui.spktpolsek.beranda

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.icha.layananpengaduanpa.R
import com.icha.layananpengaduanpa.databinding.FragmentBerandaBinding
import com.icha.layananpengaduanpa.databinding.FragmentBerandaOperatorBinding
import com.icha.layananpengaduanpa.databinding.FragmentBerandaSpktBinding
import com.icha.layananpengaduanpa.helper.Helper
import com.icha.layananpengaduanpa.helper.getCurrentDate
import com.icha.layananpengaduanpa.model.AduanCount
import com.icha.layananpengaduanpa.model.ApiConfig
import com.icha.layananpengaduanpa.model.ResponsePengaduan
import com.icha.layananpengaduanpa.model.SpktModel
import com.icha.layananpengaduanpa.session.SessionManager
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class BerandaFragment : Fragment() {

  private lateinit var binding: FragmentBerandaSpktBinding
  lateinit var session: SessionManager

  override fun onCreateView(
    inflater: LayoutInflater,
    container: ViewGroup?,
    savedInstanceState: Bundle?
  ): View? {
    binding = FragmentBerandaSpktBinding.inflate(inflater, container, false)
    val view = binding.root
    return view
  }

  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)
    session = SessionManager(view.context)
    val spkt: HashMap<String, String> = session.getUserDetails()
    val satwil: String = spkt.get(SessionManager.KEY_SATWIL)!!
    val id: String = spkt.get(SessionManager.KEY_ID)!!
    getSpktDetail(id)
    getCountAduanKec(satwil)

    binding.btnLogout.setOnClickListener {
      session.logoutUser()
      Toast.makeText(view.context, "Berhasil Logout", Toast.LENGTH_SHORT).show()
    }
  }

  private fun getSpktDetail(id: String) {
    val helper = Helper()
    val currentDate = helper.displayDateBeranda(getCurrentDate())
    ApiConfig.instance.getAkunSpktById(id)
            .enqueue(object : Callback<SpktModel> {
              @SuppressLint("SetTextI18n")
              override fun onResponse(call: Call<SpktModel>, response: Response<SpktModel>) {
                val dataSpkt = response.body()
                dataSpkt?.let { spkt ->
                  val nama = spkt.unameSpkt
                  binding.idTxt.setText("Id Pengguna : $id")
                  binding.namaTxt.setText("Halo, $nama")
                  binding.tgltxt.setText(currentDate)
                }
              }

              override fun onFailure(call: Call<SpktModel>, t: Throwable) {
                Toast.makeText(context, "Tidak berhasil menampilkan data", Toast.LENGTH_SHORT).show()
              }
            })
  }

  private fun getCountAduanKec(kec_lokasi : String) {
    ApiConfig.instance.getAduanKec("proses", kec_lokasi)
            .enqueue(object: Callback<ArrayList<ResponsePengaduan>> {
              override fun onResponse(call: Call<ArrayList<ResponsePengaduan>>, response: Response<ArrayList<ResponsePengaduan>>) {
                if (response.isSuccessful) {
                  val data = response.body()
                  data?.let { jumlah ->
                    binding.jumlahProses.setText(jumlah.count().toString())
                  }
                }
              }

              override fun onFailure(call: Call<ArrayList<ResponsePengaduan>>, t: Throwable) {
                println(t.message)
              }
            })

    ApiConfig.instance.getAduanKec("selesai", kec_lokasi)
            .enqueue(object: Callback<ArrayList<ResponsePengaduan>> {
              override fun onResponse(call: Call<ArrayList<ResponsePengaduan>>, response: Response<ArrayList<ResponsePengaduan>>) {
                if (response.isSuccessful) {
                  val data = response.body()
                  data?.let { jumlah ->
                    binding.jumlahSelesai.setText(jumlah.count().toString())
                  }
                }
              }

              override fun onFailure(call: Call<ArrayList<ResponsePengaduan>>, t: Throwable) {
                println(t.message)
              }
            })
  }
}