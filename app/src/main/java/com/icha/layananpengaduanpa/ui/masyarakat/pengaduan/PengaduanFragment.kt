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
import com.icha.layananpengaduanpa.ui.masyarakat.pengaduan.postaduan.PostAduanActivity

class PengaduanFragment : Fragment(), View.OnClickListener {

    private lateinit var viewPager: ViewPager
    private lateinit var tabs: TabLayout

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
        val view: View = inflater.inflate(R.layout.fragment_pengaduan, container, false)
        val btn_post_aduan: Button = view.findViewById(R.id.fab_aduan)
        viewPager = view.findViewById(R.id.view_pager)
        tabs = view.findViewById(R.id.tabs)

        val fragmentAdapter = SectionsPagerAdapter(childFragmentManager)
        viewPager.adapter = fragmentAdapter
        tabs.setupWithViewPager(viewPager)

        btn_post_aduan.setOnClickListener(this)
        return view
    }

    override fun onClick(p0: View) {
        if(p0.id == R.id.fab_aduan) {
            val moveIntent = Intent(activity, PostAduanActivity::class.java)
            activity?.startActivity(moveIntent)
        }
    }
}