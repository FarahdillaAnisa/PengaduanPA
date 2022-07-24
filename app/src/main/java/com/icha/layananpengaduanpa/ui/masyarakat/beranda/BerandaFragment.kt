package com.icha.layananpengaduanpa.ui.masyarakat.beranda

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.text.style.ReplacementSpan
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.icha.layananpengaduanpa.R
import com.icha.layananpengaduanpa.databinding.FragmentAkunBinding
import com.icha.layananpengaduanpa.databinding.FragmentBerandaBinding
import com.icha.layananpengaduanpa.helper.Helper
import com.icha.layananpengaduanpa.helper.getCurrentDate
import com.icha.layananpengaduanpa.model.ApiConfig
import com.icha.layananpengaduanpa.model.MasyarakatModel
import com.icha.layananpengaduanpa.model.ResponsePengaduan
import com.icha.layananpengaduanpa.session.SessionManager
import com.icha.layananpengaduanpa.ui.masyarakat.pengaduan.postaduan.PostAduanActivity
import com.icha.layananpengaduanpa.ui.spktpolsek.beranda.BerandaViewModel
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*
import kotlin.collections.ArrayList
import kotlin.collections.HashMap

class BerandaFragment : Fragment() {
    lateinit var session: SessionManager
    private lateinit var binding: FragmentBerandaBinding
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentBerandaBinding.inflate(inflater, container, false)
        val view = binding.root
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        session = SessionManager(view.context)

        val user: HashMap<String, String> = session.getUserDetails()
        val id: String = user.get(SessionManager.KEY_ID)!!
        binding.idMsytxt.setText(id)
        getAkunDetail(id)
        getAduanCount(id)

        binding.btnLogout.setOnClickListener {
            session.logoutUser()
            Toast.makeText(view.context, "Berhasil Logout", Toast.LENGTH_SHORT).show()
        }

        binding.btnTambahaduan.setOnClickListener {
            val moveIntent = Intent(activity, PostAduanActivity::class.java)
            activity?.startActivity(moveIntent)
        }
    }

    private fun getAduanCount(id_msy: String) {
        ApiConfig.instance.getAduanStatus("proses", id_msy)
            .enqueue(object: Callback<ArrayList<ResponsePengaduan>> {
                override fun onResponse(
                    call: Call<ArrayList<ResponsePengaduan>>,
                    response: Response<ArrayList<ResponsePengaduan>>
                ) {
                    if (response.isSuccessful) {
                        val data = response.body()
                        data?.let { jumlah->
                                binding.jumlahProses.setText(jumlah.count().toString())
                        }
                    }
                }

                override fun onFailure(call: Call<ArrayList<ResponsePengaduan>>, t: Throwable) {
                    println(t.message)
                }
            })

        ApiConfig.instance.getAduanStatus("selesai", id_msy)
                .enqueue(object: Callback<ArrayList<ResponsePengaduan>> {
                    override fun onResponse(
                            call: Call<ArrayList<ResponsePengaduan>>,
                            response: Response<ArrayList<ResponsePengaduan>>
                    ) {
                        if (response.isSuccessful) {
                            val data = response.body()
                            data?.let { jumlah->
//                                Toast.makeText(context, jumlah.count().toString(), Toast.LENGTH_SHORT).show()
                                if (jumlah.count() < 1) {
                                    binding.jumlahSelesai.setText("0")
                                } else {
                                    binding.jumlahSelesai.setText(jumlah.count().toString())
                                }
                            }
                        }
                    }

                    override fun onFailure(call: Call<ArrayList<ResponsePengaduan>>, t: Throwable) {
                        println(t.message)
                    }
                })
    }

    private fun getAkunDetail(id_msy: String) {
//        var namamsy: String
        val helper = Helper()
        val currentDate = helper.displayDateBeranda(getCurrentDate())
        ApiConfig.instance.getAkunMsy(id_msy)
            .enqueue(object: Callback<MasyarakatModel> {
                @SuppressLint("SetTextI18n")
                override fun onResponse(
                    call: Call<MasyarakatModel>,
                    response: Response<MasyarakatModel>
                ) {
                    if (response.isSuccessful) {
                        val dataAkun = response.body()
                        dataAkun?.let { data ->
                            val nama = data.namaMsy
                            binding.idMsytxt.setText(id_msy)
                            binding.namaTxt.setText("Halo, $nama")
                            binding.tgltxt.setText(currentDate)
                        }
                    }
                }

                override fun onFailure(call: Call<MasyarakatModel>, t: Throwable) {
                    val responseCode = t.message
                    Toast.makeText(context, responseCode, Toast.LENGTH_SHORT).show()
                }
            })
    }
}