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

class BerandaPolisiFragment : Fragment() {

  private lateinit var berandaPolisiViewModel: BerandaPolisiViewModel

  override fun onCreateView(
    inflater: LayoutInflater,
    container: ViewGroup?,
    savedInstanceState: Bundle?
  ): View? {
    berandaPolisiViewModel =
            ViewModelProvider(this).get(BerandaPolisiViewModel::class.java)
    val root = inflater.inflate(R.layout.fragment_beranda_polisi, container, false)
    val textView: TextView = root.findViewById(R.id.text_home)
    berandaPolisiViewModel.text.observe(viewLifecycleOwner, Observer {
      textView.text = it
    })
    return root
  }
}