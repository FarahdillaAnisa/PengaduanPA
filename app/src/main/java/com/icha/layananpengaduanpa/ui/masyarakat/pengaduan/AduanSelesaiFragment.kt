package com.icha.layananpengaduanpa.ui.masyarakat.pengaduan

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
import com.icha.layananpengaduanpa.databinding.FragmentAduanProsesBinding
import com.icha.layananpengaduanpa.databinding.FragmentAduanSelesaiBinding
import com.icha.layananpengaduanpa.model.ApiConfig
import com.icha.layananpengaduanpa.model.ResponsePengaduan
import com.icha.layananpengaduanpa.session.SessionManager
import com.icha.layananpengaduanpa.ui.masyarakat.pengaduan.postaduan.DetailAduanActivity
import com.icha.layananpengaduanpa.ui.spktpolsek.pengaduan.DetailAduanSpktActivity
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class AduanSelesaiFragment() : Fragment() {
    private lateinit var rvAduanSelesai: RecyclerView
    private lateinit var binding: FragmentAduanSelesaiBinding
    private val listAduanSelesai = ArrayList<ResponsePengaduan>()
    lateinit var session: SessionManager
    private var role_user: String = ""

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentAduanSelesaiBinding.inflate(inflater, container, false)
        val view = binding.root
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        rvAduanSelesai = view.findViewById(R.id.rv_aduan_selesai)
        rvAduanSelesai.setHasFixedSize(true)

        //session
        session = SessionManager(view.context)
        session.checkLogin()
        val user: HashMap<String, String> = session.getUserDetails()
        val id_user: String = user.get(SessionManager.KEY_ID)!!

        //add-cek role user
        role_user= user.get(SessionManager.ROLE_USER)!!
        val satwil: String = user.get(SessionManager.KEY_SATWIL)!!

        if (role_user == "masyarakat") {
            setHasOptionsMenu(false)
            getAduanMsy(id_user)
        } else if (role_user == "spkt") {
            setHasOptionsMenu(false)
            getAduanSpkt(satwil)
        } else if (role_user == "operator") {
            setHasOptionsMenu(true)
            getAduanOperator()
        }
    }

    private fun getAduanOperator() {
        ApiConfig.instance.getAllAduan("selesai")
                .enqueue(object : Callback<ArrayList<ResponsePengaduan>> {
                    override fun onResponse(call: Call<ArrayList<ResponsePengaduan>>, response: Response<ArrayList<ResponsePengaduan>>) {
                        response.body()?.let { listAduanSelesai.addAll(it) }
                        showRecyclerListAduan()
                    }

                    override fun onFailure(call: Call<ArrayList<ResponsePengaduan>>, t: Throwable) {
                        val responseCode = t.message
                        Toast.makeText(getContext(), responseCode, Toast.LENGTH_SHORT).show()
                    }
                })
    }

    private fun getAduanSpkt(satwil: String) {
        ApiConfig.instance.getAduanKec("selesai", satwil)
                .enqueue(object : Callback<ArrayList<ResponsePengaduan>> {
                    override fun onResponse(call: Call<ArrayList<ResponsePengaduan>>, response: Response<ArrayList<ResponsePengaduan>>) {
                        response.body()?.let { listAduanSelesai.addAll(it) }
                        showRecyclerListAduan()
                    }

                    override fun onFailure(call: Call<ArrayList<ResponsePengaduan>>, t: Throwable) {
                        val responseCode = t.message
                        Toast.makeText(getContext(), responseCode, Toast.LENGTH_SHORT).show()
                    }
                })
    }

    private fun getAduanMsy(idUser: String) {
        ApiConfig.instance.getAduanStatus("selesai", idUser.toInt()).enqueue(object : Callback<ArrayList<ResponsePengaduan>> {
            override fun onResponse(call: Call<ArrayList<ResponsePengaduan>>, response: Response<ArrayList<ResponsePengaduan>>) {

                response.body()?.let { listAduanSelesai.addAll(it) }
                showRecyclerListAduan()
            }

            override fun onFailure(call: Call<ArrayList<ResponsePengaduan>>, t: Throwable) {
                val responseCode = t.message.toString()
                Toast.makeText(getContext(), responseCode, Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun showRecyclerListAduan() {
        rvAduanSelesai.layoutManager = LinearLayoutManager(requireContext())
        val pengaduanAdapter = PengaduanAdapter(listAduanSelesai)
        rvAduanSelesai.adapter = pengaduanAdapter

        pengaduanAdapter.setOnItemClickCallback(object: PengaduanAdapter.OnItemClickCallback {
            override fun onItemClicked(data: ResponsePengaduan) {
                showSelectedAduan(data)
            }
        } )
    }

    private fun showSelectedAduan(aduan : ResponsePengaduan) {
        Toast.makeText(context, "Data Aduan : ${aduan.kodeAduan}", Toast.LENGTH_SHORT).show()
        if (role_user == "masyarakat") {
            val intent = Intent(context, DetailAduanActivity::class.java)
            intent.putExtra(DetailAduanActivity.EXTRA_KODE_ADUAN, aduan.kodeAduan)
            startActivity(intent)
        }else if (role_user == "spkt") {
            val intent = Intent(context, DetailAduanSpktActivity::class.java)
            intent.putExtra(DetailAduanSpktActivity.EXTRA_KODE_ADUAN, aduan.kodeAduan)
            startActivity(intent)
        }
    }
}