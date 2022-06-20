package com.icha.layananpengaduanpa.ui.operator.kelolaakun

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.icha.layananpengaduanpa.databinding.FragmentKelolaAkunBinding
import com.icha.layananpengaduanpa.ui.operator.kelolaakun.akunpolisi.PostAkunPolisiActivity
import com.icha.layananpengaduanpa.ui.operator.kelolaakun.akunspkt.PostSpktPolsekActivity

class KelolaAkunFragment : Fragment() {
    private lateinit var binding: FragmentKelolaAkunBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        binding = FragmentKelolaAkunBinding.inflate(inflater, container, false)
        val view = binding.root
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.kelolapolisiBtn.setOnClickListener {
            val intent = Intent(activity, PostAkunPolisiActivity::class.java)
            startActivity(intent)
        }

        binding.kelolaspktBtn.setOnClickListener {
            val intent = Intent(activity, PostSpktPolsekActivity::class.java)
            startActivity(intent)
        }
    }
}