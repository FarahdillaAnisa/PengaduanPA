package com.icha.layananpengaduanpa.ui.masyarakat.pengaduan

import android.app.SearchManager
import android.content.Context
import android.content.Context.SEARCH_SERVICE
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.Toast
import androidx.appcompat.widget.SearchView
import androidx.core.content.ContextCompat.getSystemService
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.icha.layananpengaduanpa.R
import com.icha.layananpengaduanpa.databinding.FragmentAduanProsesBinding
import com.icha.layananpengaduanpa.model.ApiConfig
import com.icha.layananpengaduanpa.model.ResponsePengaduan
import com.icha.layananpengaduanpa.session.SessionManager
import com.icha.layananpengaduanpa.ui.masyarakat.pengaduan.postaduan.DetailAduanActivity
import com.icha.layananpengaduanpa.ui.spktpolsek.pengaduan.DetailAduanSpktActivity
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

//@Suppress("DEPRECATION")
class AduanProsesFragment() : Fragment() {
    private lateinit var binding: FragmentAduanProsesBinding
    private lateinit var rvAduan: RecyclerView
    private val listAduan = ArrayList<ResponsePengaduan>()
    lateinit var session: SessionManager
    private var role_user: String = ""
    private lateinit var searchManager: SearchManager
    private lateinit var searchView: SearchView

    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        binding = FragmentAduanProsesBinding.inflate(inflater, container, false)
        val view = binding.root
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        rvAduan = view.findViewById(R.id.rv_aduan_proses)

        //session
        session = SessionManager(view.context)
        session.checkLogin()

        val user: HashMap<String, String> = session.getUserDetails()
        val id_user: String = user.get(SessionManager.KEY_ID)!!
        //add-cek role user
        role_user = user.get(SessionManager.ROLE_USER)!!
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
        ApiConfig.instance.getAllAduan("proses")
                .enqueue(object : Callback<ArrayList<ResponsePengaduan>> {
                    override fun onResponse(call: Call<ArrayList<ResponsePengaduan>>, response: Response<ArrayList<ResponsePengaduan>>) {
                        response.body()?.let { listAduan.addAll(it) }
                        showRecyclerListAduan()
                    }

                    override fun onFailure(call: Call<ArrayList<ResponsePengaduan>>, t: Throwable) {
                        val responseCode = t.message
                        Toast.makeText(context, responseCode, Toast.LENGTH_SHORT).show()
                    }
                })
    }

    private fun getAduanSpkt(satwil: String) {
        ApiConfig.instance.getAduanKec("proses", satwil)
                .enqueue(object : Callback<ArrayList<ResponsePengaduan>> {
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

    private fun getAduanMsy(idUser: String) {
        ApiConfig.instance.getAduanStatus("proses", idUser.toInt()).enqueue(object : Callback<ArrayList<ResponsePengaduan>> {
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
        })
    }

    private fun showSelectedAduan(aduan : ResponsePengaduan) {
        Toast.makeText(context, "Data Aduan : ${aduan.kodeAduan}", Toast.LENGTH_SHORT).show()
        Log.d("ROLE_USER : ", role_user)
        if (role_user == "masyarakat") {
            val intent = Intent(context, DetailAduanActivity::class.java)
            intent.putExtra(DetailAduanActivity.EXTRA_KODE_ADUAN, aduan.kodeAduan)
            startActivity(intent)
        } else if (role_user == "spkt" || role_user == "operator") {
            val intent = Intent(context, DetailAduanSpktActivity::class.java)
            intent.putExtra(DetailAduanSpktActivity.EXTRA_KODE_ADUAN, aduan.kodeAduan)
            startActivity(intent)
        }
    }

//    override fun onCreate(savedInstanceState: Bundle?) {
//        setHasOptionsMenu(true)
//        super.onCreate(savedInstanceState)
//    }
//
//    override fun onCreateOptionsMenu(menu: Menu, menuinflater: MenuInflater) {
//        menuinflater.inflate(R.menu.appbar_menu_aduan, menu)
//        menu.findItem(R.id.action_search)
//        searchManager = getSystemService(SEARCH_SERVICE) as SearchManager
//        searchView = menu.findItem(R.id.action_search).actionView as SearchView
//        return super.onCreateOptionsMenu(menu, menuinflater)
//    }
//
//    override fun onOptionsItemSelected(item: MenuItem): Boolean {
//        return when (item.itemId) {
//            R.id.action_search -> {
//                // navigate to settings screen
//                true
//            }
//            R.id.action_filter -> {
//                // save profile changes
//                true
//            }
//            else -> super.onOptionsItemSelected(item)
//        }
//    }
}

