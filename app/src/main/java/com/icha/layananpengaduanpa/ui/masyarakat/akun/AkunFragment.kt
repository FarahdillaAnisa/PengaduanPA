package com.icha.layananpengaduanpa.ui.masyarakat.akun

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.icha.layananpengaduanpa.databinding.FragmentAkunBinding
import com.icha.layananpengaduanpa.model.ApiConfig
import com.icha.layananpengaduanpa.model.MasyarakatModel
import com.icha.layananpengaduanpa.session.SessionManager
import com.icha.layananpengaduanpa.ui.spktpolsek.akun.AkunViewModel
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class AkunFragment : Fragment() {

    lateinit var session: SessionManager
    private lateinit var binding : FragmentAkunBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentAkunBinding.inflate(inflater, container, false)
        val view = binding.root
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        session = SessionManager(view.context)

        val user: HashMap<String, String> = session.getUserDetails()
        val id: String = user.get(SessionManager.KEY_ID)!!
        binding.idMsytxt.setText(id)

        detailAkun(id)

        binding.logoutBtn.setOnClickListener {
            session.logoutUser()
            Toast.makeText(view.context, "Berhasil Logout", Toast.LENGTH_SHORT).show()
        }
    }

    private fun detailAkun(id_msy: String) {
        ApiConfig.instance.getAkunMsy(id_msy)
            .enqueue(object: Callback<MasyarakatModel> {
                override fun onResponse(
                    call: Call<MasyarakatModel>,
                    response: Response<MasyarakatModel>
                ) {
                    if (response.isSuccessful) {
                        val dataAkun = response.body()
                        if (dataAkun != null) {
                            binding.idMsytxt.setText(dataAkun.idMsy)
                            binding.namaTxt.setText(dataAkun.namaMsy)
                            binding.edtNotelp.setText(dataAkun.notelpMsy)
                        }
                    }
                }

                override fun onFailure(call: Call<MasyarakatModel>, t: Throwable) {
                    val responseCode = t.message
                    Toast.makeText(context, responseCode, Toast.LENGTH_SHORT).show()
                }
            })
    }
}