package com.icha.layananpengaduanpa.ui.masyarakat.pengaduan

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.icha.layananpengaduanpa.R
import com.icha.layananpengaduanpa.model.ApiConfig
import com.icha.layananpengaduanpa.model.ResponsePengaduan
import com.icha.layananpengaduanpa.ui.masyarakat.pengaduan.postaduan.DetailAduanActivity
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class AduanProsesFragment : Fragment() {
    private lateinit var rvAduan: RecyclerView
    private val listAduan = ArrayList<ResponsePengaduan>()

    companion object {
        private const val EXTRA_KODE_ADUAN = "kode_aduan"

        fun newInstance(kodeaduan: String?) =
                AduanProsesFragment().apply {
                    arguments = Bundle().apply {
                        putString(EXTRA_KODE_ADUAN, kodeaduan)
                    }
                }
    }

    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_aduan_proses, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        rvAduan = view.findViewById(R.id.rv_aduan_proses)
        rvAduan.setHasFixedSize(true)

        ApiConfig.instance.getAduanStatus("proses").enqueue(object : Callback<ArrayList<ResponsePengaduan>> {
            override fun onResponse(call: Call<ArrayList<ResponsePengaduan>>, response: Response<ArrayList<ResponsePengaduan>>) {

                response.body()?.let { listAduan.addAll(it) }
                showRecyclerListAduan()
            }

            override fun onFailure(call: Call<ArrayList<ResponsePengaduan>>, t: Throwable) {
                val responseCode = t.message
                Toast.makeText(getContext(), responseCode, Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun showRecyclerListAduan() {
        rvAduan.layoutManager = LinearLayoutManager(requireContext())
        val pengaduanAdapter = PengaduanAdapter(listAduan)
        rvAduan.adapter = pengaduanAdapter

        pengaduanAdapter.setOnItemClickCallback(object: PengaduanAdapter.OnItemClickCallback {
            override fun onItemClicked(data: ResponsePengaduan) {
                showSelectedAduan(data)
            }
        } )
    }

    private fun showSelectedAduan(aduan : ResponsePengaduan) {
        Toast.makeText(context, "Data Aduan : ${aduan.kodeAduan}", Toast.LENGTH_SHORT).show()
        val intent = Intent(context, DetailAduanActivity::class.java)
        intent.putExtra(DetailAduanActivity.EXTRA_KODE_ADUAN, aduan.kodeAduan)
        startActivity(intent)
    }
}