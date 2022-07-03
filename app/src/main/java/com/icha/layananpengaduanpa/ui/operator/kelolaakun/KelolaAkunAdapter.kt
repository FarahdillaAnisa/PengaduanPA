package com.icha.layananpengaduanpa.ui.operator.kelolaakun

import android.annotation.SuppressLint
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.icha.layananpengaduanpa.R
import com.icha.layananpengaduanpa.model.PolisiModel
import com.icha.layananpengaduanpa.ui.operator.kelolaakun.akunpolisi.PostAkunPolisiActivity

class KelolaAkunAdapter(private val listAkun: ArrayList<PolisiModel>): RecyclerView.Adapter<KelolaAkunAdapter.AkunViewHolder>() {
    private lateinit var onItemClickCallback: OnItemClickCallback

    fun setOnItemClickCallback(onItemClickCallback: OnItemClickCallback) {
        this.onItemClickCallback = onItemClickCallback
    }

    inner class AkunViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        var idnamatxt: TextView = itemView.findViewById(R.id.tv_nama_petugas)
        var satwiltxt: TextView = itemView.findViewById(R.id.tv_satwil)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): KelolaAkunAdapter.AkunViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_row_spkt_polisi, parent,false)
        return AkunViewHolder(view)
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: KelolaAkunAdapter.AkunViewHolder, position: Int) {
        val dataAkun = listAkun[position]
        holder.idnamatxt.setText("${dataAkun.namaPolisi} | ID : ${dataAkun.idPolisi}")
        holder.satwiltxt.text = dataAkun.satuanWilayah
        holder.itemView.setOnClickListener {
            onItemClickCallback.onItemClicked(listAkun[holder.adapterPosition])
            val intent = Intent(holder.itemView.context, PostAkunPolisiActivity::class.java)
            intent.putExtra("id_akun", listAkun[position].idPolisi)
            intent.putExtra(PostAkunPolisiActivity.EXTRA_POLISI, "extra_polisi")
            holder.itemView.context.startActivity(intent)
        }
    }

    override fun getItemCount(): Int {
        return listAkun.size
    }

    interface OnItemClickCallback {
        fun onItemClicked(data: PolisiModel)
    }
}