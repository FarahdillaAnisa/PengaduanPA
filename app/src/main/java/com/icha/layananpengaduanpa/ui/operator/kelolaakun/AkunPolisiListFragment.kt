package com.icha.layananpengaduanpa.ui.operator.kelolaakun

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.icha.layananpengaduanpa.R
import com.icha.layananpengaduanpa.databinding.FragmentAkunPolisiListBinding
import com.icha.layananpengaduanpa.model.ApiConfig
import com.icha.layananpengaduanpa.model.PolisiModel
import com.icha.layananpengaduanpa.model.ResponsePengaduan
import com.icha.layananpengaduanpa.ui.operator.kelolaakun.akunpolisi.PostAkunPolisiActivity
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class AkunPolisiListFragment : Fragment() {
    private lateinit var binding: FragmentAkunPolisiListBinding
    private lateinit var rvAkunPolisi: RecyclerView
    private val listAkun = ArrayList<PolisiModel>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentAkunPolisiListBinding.inflate(inflater, container, false)
        val view = binding.root
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        rvAkunPolisi = view.findViewById(R.id.rv_polisi)

        getListAkunPolisi()
    }

    private fun getListAkunPolisi() {
        ApiConfig.instance.getAkunPolisi()
                .enqueue(object: Callback<ArrayList<PolisiModel>> {
                    override fun onResponse(call: Call<ArrayList<PolisiModel>>, response: Response<ArrayList<PolisiModel>>) {
                        response.body()?.let { listAkun.addAll(it) }
                        showRecyclerListAkun()
                    }

                    override fun onFailure(call: Call<ArrayList<PolisiModel>>, t: Throwable) {
                        val responseCode = t.message
                        Toast.makeText(context, responseCode, Toast.LENGTH_SHORT).show()
                    }
                })
    }

    private fun showRecyclerListAkun() {
        rvAkunPolisi.layoutManager = LinearLayoutManager(requireContext())
        val akunAdapter = KelolaAkunAdapter(listAkun)
        rvAkunPolisi.adapter = akunAdapter

        akunAdapter.setOnItemClickCallback(object : KelolaAkunAdapter.OnItemClickCallback {
            override fun onItemClicked(data: PolisiModel) {
                showSelectedAkun(data)
            }

        })
    }

    private fun showSelectedAkun(data: PolisiModel) {
        val intent = Intent(context, PostAkunPolisiActivity::class.java)
//        intent.putExtra()
    }


}
