package com.icha.layananpengaduanpa.ui.polisi.pelaporan

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.icha.layananpengaduanpa.R
import com.icha.layananpengaduanpa.databinding.FragmentAkunPolisiBinding
import com.icha.layananpengaduanpa.databinding.FragmentPelaporanBinding
import com.icha.layananpengaduanpa.model.ApiConfig
import com.icha.layananpengaduanpa.model.ResponsePengaduan
import com.icha.layananpengaduanpa.session.SessionManager
import com.icha.layananpengaduanpa.ui.polisi.pelaporan.postlaporan.CariPengaduanActivity
import com.icha.layananpengaduanpa.ui.polisi.pelaporan.postlaporan.DetailLaporanActivity
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class PelaporanFragment : Fragment(), View.OnClickListener {

    private lateinit var binding: FragmentPelaporanBinding
    lateinit var session: SessionManager
    private val listLaporan = ArrayList<ResponsePengaduan>()

    override fun onCreateView(
      inflater: LayoutInflater,
      container: ViewGroup?,
      savedInstanceState: Bundle?
    ): View? {
        binding = FragmentPelaporanBinding.inflate(inflater, container, false)
        val view = binding.root

        binding.fabLaporan.setOnClickListener(this)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        //session
        session = SessionManager(view.context)
        val polisi: HashMap<String, String> = session.getUserDetails()
        val id_polisi: String = polisi.get(SessionManager.KEY_ID)!!

        binding.rvLaporan.setHasFixedSize(true)

        getAduanByKode(id_polisi)
    }

    private fun getAduanByKode(idPolisi: String) {
        ApiConfig.instance.getLaporan(idPolisi)
                .enqueue(object : Callback<ArrayList<ResponsePengaduan>> {
                    override fun onResponse(call: Call<ArrayList<ResponsePengaduan>>, response: Response<ArrayList<ResponsePengaduan>>) {
                        response.body()?.let { laporan ->
                            listLaporan.addAll(laporan)
                        }
                        showRecyclerListLaporan()
                    }

                    override fun onFailure(call: Call<ArrayList<ResponsePengaduan>>, t: Throwable) {
                        Toast.makeText(context, "Data gagal ditampilkan!", Toast.LENGTH_SHORT).show()
                    }
                })
    }

    private fun showRecyclerListLaporan() {
        binding.rvLaporan.layoutManager = LinearLayoutManager(requireContext())
        val pelaporanAdapter = PelaporanAdapter(listLaporan)
        binding.rvLaporan.adapter = pelaporanAdapter

        pelaporanAdapter.setOnItemClickCallback(object : PelaporanAdapter.OnItemClickCallback {
            override fun onItemClicked(laporan: ResponsePengaduan) {
                showSelectedLaporan(laporan)
            }
        })
    }

    private fun showSelectedLaporan(laporan: ResponsePengaduan) {
        Toast.makeText(context, "Data Laporan : ${laporan.kodeAduan}", Toast.LENGTH_SHORT).show()
//        val intent = Intent(context, DetailLaporanActivity::class.java)
//        intent.putExtra(DetailLaporanActivity.EXTRA_KODE_ADUAN, laporan.kodeAduan)
//        startActivity(intent)
    }

    override fun onClick(p0: View) {
        if (p0.id == R.id.fab_laporan) {
            val moveIntent = Intent(activity, CariPengaduanActivity::class.java)
            activity?.startActivity(moveIntent)
        }
    }
}