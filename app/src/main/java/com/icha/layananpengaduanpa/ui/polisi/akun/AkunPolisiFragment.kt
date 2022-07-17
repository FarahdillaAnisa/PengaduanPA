package com.icha.layananpengaduanpa.ui.polisi.akun

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.icha.layananpengaduanpa.databinding.FragmentAkunPolisiBinding
import com.icha.layananpengaduanpa.session.SessionManager

class AkunPolisiFragment : Fragment() {

    private lateinit var akunPolisiViewModel: AkunPolisiViewModel
    lateinit var session: SessionManager
    private lateinit var binding: FragmentAkunPolisiBinding

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        akunPolisiViewModel = ViewModelProvider(this).get(AkunPolisiViewModel::class.java)
        binding = FragmentAkunPolisiBinding.inflate(inflater, container, false)
        val view = binding.root
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        session = SessionManager(view.context)

        val polisi: HashMap<String, String> = session.getUserDetails()
        val idPolisi: String = polisi.get(SessionManager.KEY_ID)!!
        binding.idTxt.setText(idPolisi)

        binding.logoutBtn.setOnClickListener {
            session.logoutUser()
            Toast.makeText(view.context, "Berhasil Logout", Toast.LENGTH_SHORT).show()
        }
    }
}