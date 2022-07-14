package com.icha.layananpengaduanpa.ui.operator.kelolaakun

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter

class AkunPagerAdapter(fm: FragmentManager): FragmentPagerAdapter(fm) {
    override fun getCount(): Int {
        return 2
    }

    override fun getItem(position: Int): Fragment {
        return when(position) {
            0 -> {
                AkunPolisiListFragment("polisi")
            }
            else -> {
                AkunPolisiListFragment("spkt")
            }
        }
    }

    override fun getPageTitle(position: Int): CharSequence? {
        return when (position) {
            0 -> {
                "Polisi"
            }
            else -> {
                return "SPKT"
            }
        }
    }
}