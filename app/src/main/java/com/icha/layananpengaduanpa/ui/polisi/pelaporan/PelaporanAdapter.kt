package com.icha.layananpengaduanpa.ui.polisi.pelaporan

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.icha.layananpengaduanpa.R
import com.icha.layananpengaduanpa.helper.Helper
import com.icha.layananpengaduanpa.model.ResponsePengaduan
import com.icha.layananpengaduanpa.ui.masyarakat.pengaduan.PengaduanAdapter
import com.icha.layananpengaduanpa.ui.polisi.pelaporan.postlaporan.DetailLaporanActivity

class PelaporanAdapter(private val listLaporan: ArrayList<ResponsePengaduan>) :
    RecyclerView.Adapter<PelaporanAdapter.LaporanViewHolder>() {
    private lateinit var onItemClickCallback: PengaduanAdapter.OnItemClickCallback

    fun setOnItemClickCallback(onItemClickCallback: OnItemClickCallback) {
        this.onItemClickCallback = onItemClickCallback
    }

    inner class LaporanViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var kodetxt: TextView = itemView.findViewById(R.id.tv_kode_aduan)
        var tgladuantxt: TextView = itemView.findViewById(R.id.tv_tgl_aduan)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LaporanViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.item_row_aduan, parent, false)
        return LaporanViewHolder(view)
    }

    override fun onBindViewHolder(holder: LaporanViewHolder, position: Int) {
        val dataLaporan = listLaporan[position]
        val helper = Helper()
        holder.kodetxt.text = dataLaporan.kodeAduan
        holder.tgladuantxt.text = helper.displayDate(dataLaporan.tglAduan.toString())
        holder.itemView.setOnClickListener {
            onItemClickCallback.onItemClicked(listLaporan[holder.adapterPosition])
            val intent = Intent(holder.itemView.context, DetailLaporanActivity::class.java)
            intent.putExtra("kode_aduan", listLaporan[position].kodeAduan)
            holder.itemView.context.startActivity(intent)
        }
    }

    override fun getItemCount(): Int {
        return listLaporan.size
    }

    interface OnItemClickCallback : PengaduanAdapter.OnItemClickCallback {
        override fun onItemClicked(data: ResponsePengaduan)
    }
}