package com.icha.layananpengaduanpa.ui.spktpolsek.beranda

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.icha.layananpengaduanpa.R
import com.icha.layananpengaduanpa.databinding.FragmentBerandaBinding
import com.icha.layananpengaduanpa.databinding.FragmentBerandaOperatorBinding
import com.icha.layananpengaduanpa.databinding.FragmentBerandaSpktBinding
import com.icha.layananpengaduanpa.model.AduanCount
import com.icha.layananpengaduanpa.model.ApiConfig
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

    //getCountData
    val dataProses = getCountAduanKec("proses", satwil)
    val dataSelesai = getCountAduanKec("selesai", satwil)
    binding.dataTxt.setText("Kecamatan : $satwil, Aduan Diproses : $dataProses, Aduan Selesai :  $dataSelesai")
  }

  private fun getCountAduanKec(status_aduan: String, kec_lokasi : String): Int {
    var jumlah = 0
    ApiConfig.instance.countAduanKec(status_aduan, kec_lokasi)
            .enqueue(object: Callback<AduanCount> {
              override fun onResponse(call: Call<AduanCount>, response: Response<AduanCount>) {
                if (response.isSuccessful) {
                  jumlah = response.body()?.jumlah!!
                }
              }

              override fun onFailure(call: Call<AduanCount>, t: Throwable) {
                println(t.message)
              }
            })
    return jumlah
  }
}