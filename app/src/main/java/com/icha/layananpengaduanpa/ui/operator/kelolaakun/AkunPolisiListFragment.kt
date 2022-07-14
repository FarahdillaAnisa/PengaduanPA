package com.icha.layananpengaduanpa.ui.operator.kelolaakun

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.icha.layananpengaduanpa.databinding.FragmentAkunPolisiListBinding
import com.icha.layananpengaduanpa.model.ApiConfig
import com.icha.layananpengaduanpa.model.PolisiModel
import com.icha.layananpengaduanpa.model.SpktModel
import com.icha.layananpengaduanpa.ui.operator.kelolaakun.akunpolisi.PostAkunPolisiActivity
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class AkunPolisiListFragment(val role_user : String) : Fragment() {
    private lateinit var binding: FragmentAkunPolisiListBinding
    private val listPolisi = ArrayList<PolisiModel>()
    private val listSpkt = ArrayList<SpktModel>()

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
        binding.rvPolisi.setHasFixedSize(true)

        if (role_user == "polisi") {
            getListAkunPolisi()
        } else if (role_user == "spkt") {
            getListAkunSpkt()
        }
    }

    private fun getListAkunSpkt() {
        ApiConfig.instance.getAkunSpkt()
                .enqueue(object : Callback<ArrayList<SpktModel>> {
                    override fun onResponse(call: Call<ArrayList<SpktModel>>, response: Response<ArrayList<SpktModel>>) {
                        response.body()?.let {
                            listSpkt.addAll(it)
                            Log.d("SPKT", listSpkt.toString())
                        }
//                        showRecyclerListAkun()
                        binding.rvPolisi.layoutManager = LinearLayoutManager(requireContext())
                        binding.rvPolisi.setHasFixedSize(true)
                        val akunAdapter = KelolaSpktAdapter(listSpkt)
                        binding.rvPolisi.adapter = akunAdapter
                        akunAdapter.setOnItemClickCallback(object : KelolaSpktAdapter.OnItemClickCallback {
                            override fun onItemClicked(data: SpktModel) {showSelectedAkunSpkt(data)}
                        })

                        Log.d("RV", "sudah di rlistAkun")
                    }

                    override fun onFailure(call: Call<ArrayList<SpktModel>>, t: Throwable) {
                        val responseCode = t.message
                        Toast.makeText(context, responseCode, Toast.LENGTH_SHORT).show()
                    }

                })
    }

    private fun getListAkunPolisi() {
        ApiConfig.instance.getAkunPolisi()
                .enqueue(object: Callback<ArrayList<PolisiModel>> {
                    override fun onResponse(call: Call<ArrayList<PolisiModel>>, response: Response<ArrayList<PolisiModel>>) {

                        response.body()?.let {
                            listPolisi.addAll(it)
                            Log.d("POLISI", listPolisi.toString())
                        }
//                        showRecyclerListAkun()
                        binding.rvPolisi.layoutManager = LinearLayoutManager(requireContext())
                        binding.rvPolisi.setHasFixedSize(true)
                        val akunAdapter = KelolaAkunAdapter(listPolisi)
                        binding.rvPolisi.adapter = akunAdapter
                        akunAdapter.setOnItemClickCallback(object : KelolaAkunAdapter.OnItemClickCallback {
                            override fun onItemClicked(data: PolisiModel) {
                                showSelectedAkun(data)
                            }
                        })

                        Log.d("RV", "sudah di rlistAkun")
                    }

                    override fun onFailure(call: Call<ArrayList<PolisiModel>>, t: Throwable) {
                        val responseCode = t.message
                        Toast.makeText(context, responseCode, Toast.LENGTH_SHORT).show()
                    }
                })
    }

    private fun showSelectedAkun(data: PolisiModel) {
        val intent = Intent(context, PostAkunPolisiActivity::class.java)
        intent.putExtra(PostAkunPolisiActivity.EXTRA_ID, data.idPolisi)
        intent.putExtra(PostAkunPolisiActivity.EXTRA_ROLE, "polisi")
        startActivity(intent)
    }

    private fun showSelectedAkunSpkt(data: SpktModel) {
        val intent = Intent(context, PostAkunPolisiActivity::class.java)
        intent.putExtra(PostAkunPolisiActivity.EXTRA_ID, data.idSpkt)
        intent.putExtra(PostAkunPolisiActivity.EXTRA_ROLE, "spkt")
        startActivity(intent)
    }
}
