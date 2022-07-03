package com.icha.layananpengaduanpa.ui.polisi.beranda

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.icha.layananpengaduanpa.R
import com.icha.layananpengaduanpa.databinding.FragmentBerandaPolisiBinding

class BerandaPolisiFragment : Fragment() {

  private lateinit var berandaPolisiViewModel: BerandaPolisiViewModel
  private lateinit var binding : FragmentBerandaPolisiBinding

  override fun onCreateView(
    inflater: LayoutInflater,
    container: ViewGroup?,
    savedInstanceState: Bundle?
  ): View? {
    berandaPolisiViewModel =
            ViewModelProvider(this).get(BerandaPolisiViewModel::class.java)
    binding  = FragmentBerandaPolisiBinding.inflate(inflater, container, false)
    val view = binding.root
    return view
  }
}