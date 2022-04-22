package com.icha.layananpengaduanpa.ui.masyarakat.pengaduan

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.icha.layananpengaduanpa.R
import com.icha.layananpengaduanpa.model.ResponsePengaduan
import com.icha.layananpengaduanpa.ui.masyarakat.pengaduan.postaduan.DetailAduanActivity
import kotlin.collections.ArrayList

class PengaduanAdapter(private val listAduan: ArrayList<ResponsePengaduan>): RecyclerView.Adapter<PengaduanAdapter.AduanViewHolder>()  {
    private lateinit var onItemClickCallback: OnItemClickCallback

    fun setOnItemClickCallback(onItemClickCallback: OnItemClickCallback) {
        this.onItemClickCallback = onItemClickCallback
    }

    inner class AduanViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var kodetxt: TextView = itemView.findViewById(R.id.tv_kode_aduan)
        var tgladuantxt: TextView = itemView.findViewById(R.id.tv_tgl_aduan)

//        fun bind(aduan: ResponsePengaduan) {
//            kodetxt.text = aduan.kodeAduan
//            tgladuantxt.text = aduan.tglAduan.toString()
//            itemView.setOnClickListener{onItemClickCallback?.onItemClicked(aduan)}
//        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AduanViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_row_aduan, parent, false)
        return AduanViewHolder(view)
    }

    override fun onBindViewHolder(holder: AduanViewHolder, position: Int) {
////        val formatter= SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH)
////        val tanggal_aduan = formatter.parse(tglAduan.toString())
        val dataAduan = listAduan[position]
//        holder.bind(dataAduan)
        holder.kodetxt.text = dataAduan.kodeAduan
        holder.tgladuantxt.text = dataAduan.tglAduan.toString()
        holder.itemView.setOnClickListener{
            onItemClickCallback.onItemClicked(listAduan[holder.adapterPosition])
            val intent = Intent(holder.itemView.context, DetailAduanActivity::class.java)
            intent.putExtra("kode_aduan", listAduan[position].kodeAduan)
            holder.itemView.context.startActivity(intent)
        }
    }

    override fun getItemCount(): Int {
        return listAduan.size
    }

    interface OnItemClickCallback {
        fun onItemClicked(data: ResponsePengaduan)
    }
}