package com.icha.layananpengaduanpa.ui.masyarakat.pengaduan

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.icha.layananpengaduanpa.R
import com.icha.layananpengaduanpa.model.ApiConfig
import com.icha.layananpengaduanpa.model.ResponsePengaduan
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class AduanSelesaiFragment : Fragment() {
    private lateinit var pengaduanAdapter: PengaduanAdapter
    private lateinit var rvAduanSelesai: RecyclerView
    private val listAduanSelesai = ArrayList<ResponsePengaduan>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_aduan_selesai, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        rvAduanSelesai = view.findViewById(R.id.rv_aduan_selesai)
        rvAduanSelesai.setHasFixedSize(true)
        rvAduanSelesai.layoutManager = LinearLayoutManager(requireContext())

        ApiConfig.instance.getAduanStatus("selesai").enqueue(object : Callback<ArrayList<ResponsePengaduan>> {
            override fun onResponse(call: Call<ArrayList<ResponsePengaduan>>, response: Response<ArrayList<ResponsePengaduan>>) {

                response.body()?.let { listAduanSelesai.addAll(it) }
                pengaduanAdapter = PengaduanAdapter(listAduanSelesai)
                rvAduanSelesai.adapter = pengaduanAdapter
            }

            override fun onFailure(call: Call<ArrayList<ResponsePengaduan>>, t: Throwable) {
                val responseCode = t.message.toString()
//                val applicationContext = activity?.applicationContext
                Toast.makeText(getContext(), responseCode, Toast.LENGTH_SHORT).show()
            }
        })
    }
}