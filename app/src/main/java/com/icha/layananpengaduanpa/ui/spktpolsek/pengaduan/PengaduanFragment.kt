package com.icha.layananpengaduanpa.ui.spktpolsek.pengaduan

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.icha.layananpengaduanpa.databinding.FragmentPengaduanSpktBinding
import com.icha.layananpengaduanpa.session.SessionManager
import com.icha.layananpengaduanpa.ui.masyarakat.pengaduan.SectionsPagerAdapter

class PengaduanFragment : Fragment() {

    private lateinit var pengaduanViewModel: PengaduanViewModel
    private lateinit var binding : FragmentPengaduanSpktBinding
    lateinit var session: SessionManager

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
      ): View? {
        pengaduanViewModel = ViewModelProvider(this).get(PengaduanViewModel::class.java)
        binding = FragmentPengaduanSpktBinding.inflate(inflater, container, false)
        val view = binding.root

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val fragmentAdapter = SectionsPagerAdapter(childFragmentManager)
        binding.viewPager.adapter = fragmentAdapter
        binding.tabs.setupWithViewPager(binding.viewPager)
    }
}