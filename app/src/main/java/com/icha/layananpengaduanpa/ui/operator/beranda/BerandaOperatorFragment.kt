package com.icha.layananpengaduanpa.ui.operator.beranda

import android.widget.AdapterView
import com.icha.layananpengaduanpa.R
import android.annotation.SuppressLint
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.icha.layananpengaduanpa.databinding.FragmentBerandaOperatorBinding
import com.icha.layananpengaduanpa.helper.Helper
import com.icha.layananpengaduanpa.helper.getCurrentDate
import com.icha.layananpengaduanpa.model.ApiConfig
import com.icha.layananpengaduanpa.model.OperatorModel
import com.icha.layananpengaduanpa.model.ResponsePengaduan
import com.icha.layananpengaduanpa.session.SessionManager
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class BerandaOperatorFragment : Fragment() {
    private lateinit var binding: FragmentBerandaOperatorBinding
    lateinit var session: SessionManager
    private var kecamatan : String = ""

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
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
        val idOperator: String = operator.get(SessionManager.KEY_ID)!!
        getOperatorDetail(idOperator)

        opxiKecamatanAkun()

        //getCountData
        binding.kecBtn.setOnClickListener {
            if (kecamatan != "Data Semua Aduan") {
                getCountAduanKec(kecamatan)
            } else {
                getCountAduan()
            }
        }

        binding.logoutBtn.setOnClickListener {
            session.logoutUser()
            Toast.makeText(view.context, "Berhasil Logout", Toast.LENGTH_SHORT).show()
        }
    }

    private fun getOperatorDetail(idOperator: String) {
        val helper = Helper()
        val currentDate = helper.displayDateBeranda(getCurrentDate())
        ApiConfig.instance.getOperatorById(idOperator)
                .enqueue(object: Callback<OperatorModel> {
                    @SuppressLint("SetTextI18n")
                    override fun onResponse(call: Call<OperatorModel>, response: Response<OperatorModel>) {
                        val dataOperator = response.body()
                        dataOperator?.let { operator ->
                            val uname = operator.unameOperator
                            binding.idTxt.setText("Id Operator : $idOperator")
                            binding.namaTxt.setText("Halo, $uname")
                            binding.tgltxt.setText(currentDate)

                        }
                    }

                    override fun onFailure(call: Call<OperatorModel>, t: Throwable) {
                        Toast.makeText(context, "Tidak berhasil menampilkan data", Toast.LENGTH_SHORT).show()
                    }

                })
    }

    private fun opxiKecamatanAkun() {
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

    private fun getCountAduan() {
        binding.jumlahProses.setText("0")
        binding.jumlahSelesai.setText("0")
        ApiConfig.instance.getAllAduan("proses")
                .enqueue(object : Callback<ArrayList<ResponsePengaduan>> {
                    override fun onResponse(call: Call<ArrayList<ResponsePengaduan>>, response: Response<ArrayList<ResponsePengaduan>>) {
                        if (response.isSuccessful) {
                            val data = response.body()
                            data?.let { jumlah ->
                                if (jumlah.count() > 1) {
                                    binding.jumlahProses.setText(jumlah.count().toString())
                                } else {
                                    binding.jumlahProses.setText("0")
                                }
                            }
                        }
                    }

                    override fun onFailure(call: Call<ArrayList<ResponsePengaduan>>, t: Throwable) {
                        println(t.message)
                    }
                })

        ApiConfig.instance.getAllAduan("selesai")
                .enqueue(object : Callback<ArrayList<ResponsePengaduan>> {
                    override fun onResponse(call: Call<ArrayList<ResponsePengaduan>>, response: Response<ArrayList<ResponsePengaduan>>) {
                        if (response.isSuccessful) {
                            val data = response.body()
                            data?.let { jumlah ->
                                if (jumlah.count() > 1) {
                                    binding.jumlahSelesai.setText(jumlah.count().toString())
                                } else {
                                    binding.jumlahSelesai.setText("0")
                                }
                            }
                        }
                    }

                    override fun onFailure(call: Call<ArrayList<ResponsePengaduan>>, t: Throwable) {
                        println(t.message)
                    }
                })
    }

    private fun getCountAduanKec(kec_lokasi : String) {
        binding.jumlahProses.setText("0")
        binding.jumlahSelesai.setText("0")
        ApiConfig.instance.getAduanKec("proses", kec_lokasi)
                .enqueue(object: Callback<ArrayList<ResponsePengaduan>> {
                    override fun onResponse(call: Call<ArrayList<ResponsePengaduan>>, response: Response<ArrayList<ResponsePengaduan>>) {
                        if (response.isSuccessful) {
                            val data = response.body()
                            data?.let { jumlah ->
                                if (jumlah.count() > 1) {
                                    binding.jumlahProses.setText(jumlah.count().toString())

                                } else {
                                    binding.jumlahProses.setText("0")
                                }
                            }
                        }
                    }

                    override fun onFailure(call: Call<ArrayList<ResponsePengaduan>>, t: Throwable) {
                        println(t.message)
                    }
                })

        ApiConfig.instance.getAduanKec("selesai", kec_lokasi)
                .enqueue(object: Callback<ArrayList<ResponsePengaduan>> {
                    override fun onResponse(call: Call<ArrayList<ResponsePengaduan>>, response: Response<ArrayList<ResponsePengaduan>>) {
                        if (response.isSuccessful) {
                            val data = response.body()
                            data?.let { jumlah ->
                                if (jumlah.count() > 1) {
                                    binding.jumlahSelesai.setText(jumlah.count().toString())
                                } else {
                                    binding.jumlahSelesai.setText("0")
                                }
                            }
                        }
                    }

                    override fun onFailure(call: Call<ArrayList<ResponsePengaduan>>, t: Throwable) {
                        println(t.message)
                    }
                })
    }
}