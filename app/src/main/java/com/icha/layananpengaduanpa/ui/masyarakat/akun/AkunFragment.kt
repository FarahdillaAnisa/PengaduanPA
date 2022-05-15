package com.icha.layananpengaduanpa.ui.masyarakat.akun

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.icha.layananpengaduanpa.R
import com.icha.layananpengaduanpa.databinding.ActivityMainBinding
import com.icha.layananpengaduanpa.databinding.FragmentAkunBinding
import com.icha.layananpengaduanpa.session.SessionManager

class AkunFragment : Fragment() {

    private lateinit var akunViewModel: AkunViewModel
    lateinit var session: SessionManager
    private lateinit var binding : FragmentAkunBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        akunViewModel = ViewModelProvider(this).get(AkunViewModel::class.java)
        binding = FragmentAkunBinding.inflate(inflater, container, false)
        val view = binding.root
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        session = SessionManager(view.context)
        session.checkLogin()

        val user: HashMap<String, String> = session.getUserDetails()
        val username: String = user.get(SessionManager.KEY_USERNAME)!!
        binding.unameTxt.setText(username)

        binding.logoutBtn.setOnClickListener {
            session.logoutUser()
            Toast.makeText(view.context, "Berhasil Logout", Toast.LENGTH_SHORT).show()
        }
    }
}