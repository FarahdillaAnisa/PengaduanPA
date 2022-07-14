package com.icha.layananpengaduanpa.ui.operator.kelolaakun

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.icha.layananpengaduanpa.databinding.FragmentKelolaAkunBinding
import com.icha.layananpengaduanpa.ui.operator.kelolaakun.akunpolisi.PostAkunPolisiActivity

class KelolaAkunFragment : Fragment() {
    private lateinit var binding: FragmentKelolaAkunBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        binding = FragmentKelolaAkunBinding.inflate(inflater, container, false)
        val view = binding.root

        val fragmentAdapter = AkunPagerAdapter(childFragmentManager)
        binding.viewPager.adapter = fragmentAdapter
        binding.tabs.setupWithViewPager(binding.viewPager)
        binding.fabAkun.setOnClickListener {
            val intent = Intent(activity, PostAkunPolisiActivity::class.java)
            startActivity(intent)
        }
        return view
    }
}