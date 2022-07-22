package com.icha.layananpengaduanpa.ui.operator.beranda

import android.widget.AdapterView
import com.icha.layananpengaduanpa.R
import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.widget.SearchView
import androidx.recyclerview.widget.LinearLayoutManager
import com.icha.layananpengaduanpa.databinding.FragmentBerandaOperatorBinding
import com.icha.layananpengaduanpa.model.ApiConfig
import com.icha.layananpengaduanpa.model.ResponsePengaduan
import com.icha.layananpengaduanpa.session.SessionManager
import com.icha.layananpengaduanpa.ui.masyarakat.pengaduan.PengaduanAdapter
import com.icha.layananpengaduanpa.ui.spktpolsek.pengaduan.DetailAduanSpktActivity
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class BerandaOperatorFragment : Fragment() {
    private lateinit var binding: FragmentBerandaOperatorBinding
    lateinit var session: SessionManager
    private val listAduan = ArrayList<ResponsePengaduan>()
    private var kecamatan : String = ""
    private var jumlahAduanProses: Int = 0
    private var jumlahAduanSelesai: Int = 0

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentBerandaOperatorBinding.inflate(inflater, container, false)
        val view = binding.root
        return view
    }

    //Tampilan
    @SuppressLint("SetTextI18n")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        session = SessionManager(view.context)

        val operator: HashMap<String, String> = session.getUserDetails()
        val unameOperator: String = operator.get(SessionManager.KEY_USERNAME)!!
        val idOperator: String = operator.get(SessionManager.KEY_ID)!!
        Log.d("USERNAME" , "$unameOperator dan id : $idOperator ")
        binding.idTxt.setText(unameOperator)

        spinnerKecamatan()

        //getCountData
        binding.kecBtn.setOnClickListener {
            if (kecamatan != "Data Semua Aduan") {
                getCountAduanKec("proses", kecamatan)
                getCountAduanKec("selesai", kecamatan)
                binding.dataTxt.setText("Kecamatan : $kecamatan, Aduan Diproses : $jumlahAduanProses, Aduan Selesai :  $jumlahAduanSelesai")
            } else {
                getCountAduan("proses")
                getCountAduan("selesai")
                binding.dataTxt.setText("Semua Aduan di Kota Pekanbaru, Aduan Diproses : $jumlahAduanProses, Aduan Selesai :  $jumlahAduanSelesai")
            }
        }

        //search data aduan
        binding.search.queryHint = "Pencarian Aduan Berdasarkan Nama Pelapor"
        binding.search.setOnQueryTextListener(object: SearchView.OnQueryTextListener{
            override fun onQueryTextSubmit(query: String): Boolean {
                cariAduan(query, null)
                return true
            }

            override fun onQueryTextChange(newText: String): Boolean {
                if (newText.isNotEmpty()) {
                    cariAduan(newText, null)
                }
                return true
            }
        })


        binding.logoutBtn.setOnClickListener {
            session.logoutUser()
            Toast.makeText(view.context, "Berhasil Logout", Toast.LENGTH_SHORT).show()
        }
    }

    private fun spinnerKecamatan() {
        val kecamatanOpsi = resources.getStringArray(R.array.kecamatan)
        binding.kecamatan.onItemSelectedListener = object :
                AdapterView.OnItemSelectedListener {
            override fun onItemSelected(p0: AdapterView<*>?, view: View?, position: Int, id: Long) {
                kecamatan = kecamatanOpsi[position].toString()
            }

            override fun onNothingSelected(p0: AdapterView<*>?) {
                TODO("Not yet implemented")
            }
        }
    }

    private fun cariAduan(keyword_nama_msy: String, kecamatan: String?){
        ApiConfig.instance.searchAduanByNama(keyword_nama_msy, kecamatan)
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

    private fun getCountAduan(status_aduan : String) {
        var jumlah : Int = 0
        ApiConfig.instance.getAllAduan(status_aduan)
                .enqueue(object : Callback<ArrayList<ResponsePengaduan>> {
                    override fun onResponse(call: Call<ArrayList<ResponsePengaduan>>, response: Response<ArrayList<ResponsePengaduan>>) {
                        if (response.isSuccessful) {
                            if (status_aduan == "proses" ){
                                jumlahAduanProses = response.body()!!.count()
                                Log.d("aduan proses : ", jumlahAduanProses.toString())
                            } else if (status_aduan == "selesai") {
                                jumlahAduanSelesai = response.body()!!.count()
                                Log.d("aduan selesai : ", jumlahAduanSelesai.toString())
                            }
                            println(response.body()!!.count())
                        }
                    }

                    override fun onFailure(call: Call<ArrayList<ResponsePengaduan>>, t: Throwable) {
                        println(t.message)
                    }
                })
    }

    private fun getCountAduanKec(status_aduan: String, kec_lokasi : String) {
        ApiConfig.instance.getAduanKec(status_aduan, kec_lokasi)
                .enqueue(object: Callback<ArrayList<ResponsePengaduan>> {
                    override fun onResponse(call: Call<ArrayList<ResponsePengaduan>>, response: Response<ArrayList<ResponsePengaduan>>) {
                        if (response.isSuccessful) {
                            if (status_aduan == "proses") {
                                jumlahAduanProses = response.body()!!.count()
                                Log.d("aduan proses : ", jumlahAduanProses.toString())
                            } else if (status_aduan == "selesai"){
                                jumlahAduanSelesai = response.body()!!.count()
                                Log.d("aduan selesai : ", jumlahAduanSelesai.toString())
                            }
                        }
                    }

                    override fun onFailure(call: Call<ArrayList<ResponsePengaduan>>, t: Throwable) {
                        println(t.message)
                    }
                })
    }

    private fun showRecyclerListAduan() {
        binding.rvAduan.layoutManager = LinearLayoutManager(requireContext())
        val pengaduanAdapter = PengaduanAdapter(listAduan)
        binding.rvAduan.adapter = pengaduanAdapter

        pengaduanAdapter.setOnItemClickCallback(object: PengaduanAdapter.OnItemClickCallback {
            override fun onItemClicked(data: ResponsePengaduan) {
                showSelectedAduan(data)
            }
        })
    }

    private fun showSelectedAduan(aduan : ResponsePengaduan) {
        Toast.makeText(context, "Data Aduan : ${aduan.kodeAduan}", Toast.LENGTH_SHORT).show()
        val intent = Intent(context, DetailAduanSpktActivity::class.java)
        intent.putExtra(DetailAduanSpktActivity.EXTRA_KODE_ADUAN, aduan.kodeAduan)
        startActivity(intent)
    }
}