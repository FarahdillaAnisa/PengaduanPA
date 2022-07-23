package com.icha.layananpengaduanpa.ui.masyarakat.pengaduan

import android.app.Application
import android.app.SearchManager
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.AdapterView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.icha.layananpengaduanpa.R
import com.icha.layananpengaduanpa.databinding.FragmentAduanProsesBinding
import com.icha.layananpengaduanpa.model.AduanCount
import com.icha.layananpengaduanpa.model.ApiConfig
import com.icha.layananpengaduanpa.model.ResponsePengaduan
import com.icha.layananpengaduanpa.session.SessionManager
import com.icha.layananpengaduanpa.ui.login.SignupActivity
import com.icha.layananpengaduanpa.ui.masyarakat.pengaduan.postaduan.DetailAduanActivity
import com.icha.layananpengaduanpa.ui.spktpolsek.pengaduan.DetailAduanSpktActivity
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*
import kotlin.collections.ArrayList
import kotlin.collections.HashMap

//@Suppress("DEPRECATION")
class AduanProsesFragment() : Fragment() {
    private lateinit var binding: FragmentAduanProsesBinding
    private lateinit var rvAduan: RecyclerView
    private val listAduan = ArrayList<ResponsePengaduan>()
    lateinit var session: SessionManager
    private var role_user: String = ""
    private var opsiKecamatan: String = ""
    private var filterKecamatan: String = ""

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

        val user: HashMap<String, String> = session.getUserDetails()
        val id_user: String = user.get(SessionManager.KEY_ID)!!
        //add-cek role user
        role_user = user.get(SessionManager.ROLE_USER)!!
        val satwil: String = user.get(SessionManager.KEY_SATWIL)!!
        binding.progressBar.visibility = View.VISIBLE

        if (role_user == "masyarakat") {
            binding.searchfilter.visibility = View.GONE
            setHasOptionsMenu(false)
            getAduanMsy(id_user)
        } else if (role_user == "spkt") {
            binding.searchfilter.visibility = View.VISIBLE
            setHasOptionsMenu(false)
            getAduanSpkt(satwil)
        } else if (role_user == "operator") {
            binding.searchfilter.visibility = View.VISIBLE
            setHasOptionsMenu(true)
            getAduanOperator()
        }

        //SEARCH VIEW
        val searchView: SearchView = binding.actionSearch
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                if(newText!!.isNotEmpty()) {
                    listAduan.clear()
                    val namaKeyword = newText.toLowerCase(Locale.getDefault())
                    if (role_user == "operator") {
                        cariAduan(namaKeyword, null)
                    } else if (role_user == "spkt") {
                        cariAduan(namaKeyword, satwil)
                    }
                    rvAduan.adapter!!.notifyDataSetChanged()
                } else {
                    listAduan.clear()
                    if (role_user == "operator") {
                        getAduanOperator()
                    } else if (role_user == "spkt") {
                        getAduanSpkt(satwil)
                    }
                    rvAduan.adapter!!.notifyDataSetChanged()
                }
                return true
            }
        })

        //filter by kecamatan - operator ONLY
        binding.actionFilter.setOnClickListener {
            val kecamatan = arrayOf("Bukit Raya", "Pelabuhan", "Lima Puluh", "Payung Sekaki", "Pekanbaru Kota", "Rumbai", "Rumbai Pesisir", "Senapelan", "Sukajadi", "Tampan", "Tenayan Raya")
            val mBuilder = AlertDialog.Builder(requireContext())
            mBuilder.setTitle("Sortir Aduan Berdasarkan Kecamatan")
            mBuilder.setSingleChoiceItems(kecamatan, -1) { dialogInterface, i ->
                filterKecamatan = kecamatan[i]
                listAduan.clear()
                getAduanSpkt(filterKecamatan)
                rvAduan.adapter!!.notifyDataSetChanged()
                dialogInterface.dismiss()
            }
            mBuilder.setNeutralButton("TAMPILKAN SEMUA DATA", object : DialogInterface.OnClickListener {
                override fun onClick(p0: DialogInterface?, p1: Int) {
                    getAduanOperator()
                }
            })
            val mDialog = mBuilder.create()
            mDialog.show()
        }

//            MaterialAlertDialogBuilder(requireContext())
//                    .setTitle("Sortir Aduan Berdasarkan Kecamatan")
//                    .setMessage("Silahkan Pilih Salah Satu Kecamatan")
//                    .setSingleChoiceItems(kecamatan, -1, DialogInterface.OnClickListener(){
//
//                    }
//                        filterKecamatan = kecamatan[i]
//                        dialogInterface.dismiss()
//                    }
//                    .setPositiveButton("OK", object : DialogInterface.OnClickListener{
//                        override fun onClick(p0: DialogInterface?, p1: Int) {
//                            listAduan.clear()
//                            getAduanSpkt(filterKecamatan)
//                            rvAduan.adapter!!.notifyDataSetChanged()
//                        }
//                    })
//                    .setNeutralButton("BATAL", object : DialogInterface.OnClickListener {
//                        override fun onClick(p0: DialogInterface?, p1: Int) {
//                        }
//                    })
//                    .create()
//                    .show()

    }

    private fun getAduanOperator() {
        ApiConfig.instance.getAllAduan("proses")
                .enqueue(object : Callback<ArrayList<ResponsePengaduan>> {
                    override fun onResponse(call: Call<ArrayList<ResponsePengaduan>>, response: Response<ArrayList<ResponsePengaduan>>) {
                        binding.progressBar.visibility = View.GONE
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
                        binding.progressBar.visibility = View.GONE
//                        val data = response.body().data
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
        ApiConfig.instance.getAduanStatus("proses", idUser).enqueue(object : Callback<ArrayList<ResponsePengaduan>> {
            override fun onResponse(call: Call<ArrayList<ResponsePengaduan>>, response: Response<ArrayList<ResponsePengaduan>>) {
                binding.progressBar.visibility = View.GONE
                response.body()?.let { listAduan.addAll(it) }
                showRecyclerListAduan()
            }

            override fun onFailure(call: Call<ArrayList<ResponsePengaduan>>, t: Throwable) {
                val responseCode = t.message
                Toast.makeText(getContext(), responseCode, Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun cariAduan(keyword_nama_msy: String, kecamatan: String?){
        ApiConfig.instance.searchAduanByNama(nama_msy = keyword_nama_msy, kec_lokasi = kecamatan)
                .enqueue(object : Callback<ArrayList<ResponsePengaduan>> {
                    override fun onResponse(call: Call<ArrayList<ResponsePengaduan>>, response: Response<ArrayList<ResponsePengaduan>>) {
                        if (response.isSuccessful) {
                            response.body()?.let { listAduan.addAll(it) }
                            showRecyclerListAduan()
                        }
                    }

                    override fun onFailure(call: Call<ArrayList<ResponsePengaduan>>, t: Throwable) {
                        val responseCode = t.message
                        Toast.makeText(context, responseCode, Toast.LENGTH_SHORT).show()
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
}

