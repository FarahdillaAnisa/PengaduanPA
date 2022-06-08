package com.icha.layananpengaduanpa.ui.masyarakat.pengaduan

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.annotation.StringRes
import androidx.fragment.app.Fragment
import androidx.viewpager.widget.ViewPager
import com.google.android.material.tabs.TabLayout
import com.icha.layananpengaduanpa.R
import com.icha.layananpengaduanpa.databinding.FragmentPelaporanBinding
import com.icha.layananpengaduanpa.databinding.FragmentPengaduanBinding
import com.icha.layananpengaduanpa.ui.masyarakat.pengaduan.postaduan.PostAduanActivity

class PengaduanFragment : Fragment(), View.OnClickListener {
    private lateinit var binding: FragmentPengaduanBinding
    companion object {
        @StringRes
        private val TAB_TITLES = intArrayOf(
            R.string.tab_text_proses,
            R.string.tab_text_selesai
        )
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentPengaduanBinding.inflate(inflater, container, false)
        val view = binding.root

//        btn_post_aduan.visibility = View.GONE
        val fragmentAdapter = SectionsPagerAdapter(childFragmentManager)
        binding.viewPager.adapter = fragmentAdapter
        binding.tabs.setupWithViewPager(binding.viewPager)

        binding.fabAduan.setOnClickListener(this)
        return view
    }

    override fun onClick(p0: View) {
        if(p0.id == R.id.fab_aduan) {
            val moveIntent = Intent(activity, PostAduanActivity::class.java)
            activity?.startActivity(moveIntent)
        }
    }
}