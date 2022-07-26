package com.icha.layananpengaduanpa.ui.polisi.beranda

import android.annotation.SuppressLint
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Context.CLIPBOARD_SERVICE
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat.getSystemService
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.icha.layananpengaduanpa.R
import com.icha.layananpengaduanpa.databinding.FragmentBerandaPolisiBinding
import com.icha.layananpengaduanpa.helper.Helper
import com.icha.layananpengaduanpa.helper.getCurrentDate
import com.icha.layananpengaduanpa.model.ApiConfig
import com.icha.layananpengaduanpa.model.PolisiModel
import com.icha.layananpengaduanpa.model.SpktModel
import com.icha.layananpengaduanpa.session.SessionManager
import com.icha.layananpengaduanpa.ui.masyarakat.pengaduan.postaduan.PostAduanActivity
import com.icha.layananpengaduanpa.ui.polisi.pelaporan.postlaporan.CariPengaduanActivity
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class BerandaPolisiFragment : Fragment() {

  private lateinit var binding : FragmentBerandaPolisiBinding
  lateinit var session: SessionManager

  override fun onCreateView(
    inflater: LayoutInflater,
    container: ViewGroup?,
    savedInstanceState: Bundle?
  ): View? {
    binding  = FragmentBerandaPolisiBinding.inflate(inflater, container, false)
    val view = binding.root
    return view
  }

  @SuppressLint("SetTextI18n")
  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)
    session = SessionManager(view.context)
    val polisi: HashMap<String, String> = session.getUserDetails()
    val id: String = polisi.get(SessionManager.KEY_ID)!!
    getPolisiDetail(id)

    binding.btnLogout.setOnClickListener {
      session.logoutUser()
      Toast.makeText(view.context, "Berhasil Logout", Toast.LENGTH_SHORT).show()
    }

     binding.btnLaporan.setOnClickListener {
       val moveIntent = Intent(activity, CariPengaduanActivity::class.java)
       activity?.startActivity(moveIntent)
     }

    val aduan: HashMap<String, String> = session.getSimpanAduan()
    val kode_aduan: String = aduan.get(SessionManager.KEY_ADUAN).toString()
    val satwil: String = aduan.get(SessionManager.KEY_SATWIL_ADUAN).toString()
    if (kode_aduan.isNotEmpty()) {
      binding.tvKodeAduan.setText("Kode Aduan: $kode_aduan")
      binding.tvSatwil.setText("Kecamatan : $satwil")
    } else {
      binding.tvKodeAduan.setText("Kode Aduan: Tidak ada")
      binding.tvSatwil.setText("Kecamatan : Tidak ada")
    }

    binding.salin.setOnClickListener {
      val clipboardManager : ClipboardManager = activity?.getSystemService(CLIPBOARD_SERVICE) as ClipboardManager
      val clipData = ClipData.newPlainText("text", "$kode_aduan, $satwil")
      clipboardManager.setPrimaryClip(clipData)
      Toast.makeText(context, "Data aduan sudah disalin, silahkan tempel pada cari pengaduan", Toast.LENGTH_SHORT).show()
    }
  }

  private fun getPolisiDetail(id: String) {
    val helper = Helper()
    val currentDate = helper.displayDateBeranda(getCurrentDate())
    ApiConfig.instance.getAkunPolisiById(id)
            .enqueue(object : Callback<PolisiModel> {
              @SuppressLint("SetTextI18n")
              override fun onResponse(call: Call<PolisiModel>, response: Response<PolisiModel>) {
                val dataPolisi = response.body()
                dataPolisi?.let { polisi ->
                  val nama = polisi.namaPolisi
                  binding.idTxt.setText("Id Pengguna : $id")
                  binding.namaTxt.setText("Halo, $nama")
                  binding.tgltxt.setText(currentDate)
                }
              }

              override fun onFailure(call: Call<PolisiModel>, t: Throwable) {
                Toast.makeText(context, "Tidak berhasil menampilkan data", Toast.LENGTH_SHORT).show()
              }
            })
  }
}