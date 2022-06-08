package com.icha.layananpengaduanpa.ui.masyarakat.pengaduan

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter

class SectionsPagerAdapter(fm: FragmentManager) : FragmentPagerAdapter(fm) {

    override fun getCount(): Int {
        return 2
    }

    override fun getItem(position: Int): Fragment {
        return when (position) {
            0 -> {
                AduanProsesFragment()
            }
            else -> {
                AduanSelesaiFragment()
            }
        }
    }

    override fun getPageTitle(position: Int): CharSequence? {
        return when (position) {
            0 -> "Belum Diproses"
            else -> {
                return "Selesai"
            }
        }
    }
}