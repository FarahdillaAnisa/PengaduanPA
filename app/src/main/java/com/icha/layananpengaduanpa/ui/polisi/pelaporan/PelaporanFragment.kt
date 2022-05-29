package com.icha.layananpengaduanpa.ui.polisi.pelaporan

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.icha.layananpengaduanpa.R
import com.icha.layananpengaduanpa.databinding.FragmentAkunPolisiBinding
import com.icha.layananpengaduanpa.databinding.FragmentPelaporanBinding
import com.icha.layananpengaduanpa.ui.polisi.pelaporan.postlaporan.CariPengaduanActivity

class PelaporanFragment : Fragment(), View.OnClickListener {

    private lateinit var pelaporanViewModel: PelaporanViewModel
    private lateinit var binding: FragmentPelaporanBinding

    override fun onCreateView(
      inflater: LayoutInflater,
      container: ViewGroup?,
      savedInstanceState: Bundle?
    ): View? {
        pelaporanViewModel = ViewModelProvider(this).get(PelaporanViewModel::class.java)
        binding = FragmentPelaporanBinding.inflate(inflater, container, false)
        val view = binding.root

        binding.fabLaporan.setOnClickListener(this)
        return view
    }

    override fun onClick(p0: View) {
        if (p0.id == R.id.fab_laporan) {
            val moveIntent = Intent(activity, CariPengaduanActivity::class.java)
            activity?.startActivity(moveIntent)
        }
    }
}