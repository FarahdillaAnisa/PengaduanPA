package com.icha.layananpengaduanpa.ui.operator.kelolaakun

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.icha.layananpengaduanpa.R
import com.icha.layananpengaduanpa.databinding.FragmentAkunPolisiListBinding
import com.icha.layananpengaduanpa.model.PolisiModel

class AkunSpktListFragment : Fragment() {
    private lateinit var binding: FragmentAkunPolisiListBinding
    private val listAkun = ArrayList<PolisiModel>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_akun_spkt_list, container, false)
    }
}