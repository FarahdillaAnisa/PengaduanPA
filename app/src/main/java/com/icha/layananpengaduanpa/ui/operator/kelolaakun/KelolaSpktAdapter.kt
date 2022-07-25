package com.icha.layananpengaduanpa.ui.operator.kelolaakun

import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.button.MaterialButton
import com.icha.layananpengaduanpa.R
import com.icha.layananpengaduanpa.model.SpktModel
import com.icha.layananpengaduanpa.ui.operator.kelolaakun.akunpolisi.PostAkunPolisiActivity

class KelolaSpktAdapter(private val listSpkt : ArrayList<SpktModel>): RecyclerView.Adapter<KelolaSpktAdapter.AkunViewHolder>()  {
    private lateinit var onItemClickCallback: OnItemClickCallback

    fun setOnItemClickCallback(onItemClickCallback: OnItemClickCallback) {
        this.onItemClickCallback = onItemClickCallback
    }

    inner class AkunViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        var idnamatxt: TextView = itemView.findViewById(R.id.tv_nama_petugas)
        var satwiltxt: TextView = itemView.findViewById(R.id.tv_satwil)
        var dialBtn: ImageButton = itemView.findViewById(R.id.dialBtn)
        var shareBtn: MaterialButton = itemView.findViewById(R.id.shareBtn)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AkunViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_row_spkt_polisi, parent,false)
        return AkunViewHolder(view)
    }

    override fun onBindViewHolder(holder: AkunViewHolder, position: Int) {
        val dataSpkt = listSpkt[position]
        holder.idnamatxt.text = dataSpkt.unameSpkt
        holder.satwiltxt.text = dataSpkt.satuanWilayah
        holder.dialBtn.setOnClickListener {
            val intent = Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + Uri.encode(dataSpkt.notelpSpkt)))
            holder.itemView.context.startActivity(intent)
        }
        holder.shareBtn.setOnClickListener {
            val intent = Intent(Intent.ACTION_SEND)
            intent.putExtra("Share", "ID Akun : ${dataSpkt.idSpkt}, Nama : ${dataSpkt.unameSpkt}, Satuan Wilayah : ${dataSpkt.satuanWilayah}, No. Telp : ${dataSpkt.notelpSpkt}, Password Awal : ${dataSpkt.passSpkt} }")
            intent.setType("text/plain")
            holder.itemView.context.startActivity(Intent.createChooser(intent, "Bagikan Melalui : "))
        }
        holder.itemView.setOnClickListener {
            onItemClickCallback.onItemClicked(listSpkt[holder.adapterPosition])
//            val intent = Intent(holder.itemView.context, PostAkunPolisiActivity::class.java)
//            intent.putExtra(PostAkunPolisiActivity.EXTRA_ID, dataSpkt.idSpkt)
//            holder.itemView.context.startActivity(intent)
        }
    }

    override fun getItemCount(): Int {
        return listSpkt.size
    }

    interface OnItemClickCallback {
        fun onItemClicked(data: SpktModel)
    }

}