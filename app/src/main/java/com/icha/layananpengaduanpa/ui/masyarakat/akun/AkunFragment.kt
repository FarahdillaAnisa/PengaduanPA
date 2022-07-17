package com.icha.layananpengaduanpa.ui.masyarakat.akun

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
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

        val user: HashMap<String, String> = session.getUserDetails()
        val id: String = user.get(SessionManager.KEY_ID)!!
        binding.unameTxt.setText(id)

        binding.logoutBtn.setOnClickListener {
            session.logoutUser()
            Toast.makeText(view.context, "Berhasil Logout", Toast.LENGTH_SHORT).show()
        }
    }
}