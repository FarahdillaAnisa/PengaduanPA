package com.icha.layananpengaduanpa.ui.operator.beranda

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.icha.layananpengaduanpa.databinding.FragmentBerandaOperatorBinding
import com.icha.layananpengaduanpa.session.SessionManager

class BerandaOperatorFragment : Fragment() {
    private lateinit var binding: FragmentBerandaOperatorBinding
    lateinit var session: SessionManager

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentBerandaOperatorBinding.inflate(inflater, container, false)
        val view = binding.root
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        session = SessionManager(view.context)
//        session.checkLogin()

        val operator: HashMap<String, String> = session.getOperatorDetails()
        val unameOperator: String = operator.get(SessionManager.KEY_USERNAME)!!
        val idOperator: String = operator.get(SessionManager.KEY_ID)!!
        Log.d("USERNAME" , "$unameOperator dan id : $idOperator ")
        binding.idTxt.setText(unameOperator)

        binding.logoutBtn.setOnClickListener {
            session.logoutUser()
            Toast.makeText(view.context, "Berhasil Logout", Toast.LENGTH_SHORT).show()
        }
    }
}