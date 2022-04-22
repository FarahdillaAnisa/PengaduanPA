package com.icha.layananpengaduanpa.ui.masyarakat.akun

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.icha.layananpengaduanpa.R

class AkunFragment : Fragment() {

    private lateinit var akunViewModel: AkunViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        akunViewModel =
            ViewModelProvider(this).get(AkunViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_akun, container, false)
        val textView: TextView = root.findViewById(R.id.text_notifications)
        akunViewModel.text.observe(viewLifecycleOwner, Observer {
            textView.text = it
        })
        return root
    }
}