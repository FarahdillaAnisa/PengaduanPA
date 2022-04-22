package com.icha.layananpengaduanpa.model
import com.google.gson.annotations.SerializedName
import java.util.*


data class ResponsePengaduan(
        @field:SerializedName("kode_aduan")
        val kodeAduan: String,

        @field:SerializedName("lat_lokasi")
        val latLokasi: String? = null,

        @field:SerializedName("long_lokasi")
        val longLokasi: String? = null,

        @field:SerializedName("kec_lokasi")
        val kecLokasi: String? = null,

        @field:SerializedName("isi_aduan")
        val isiAduan: String? = null,

        @field:SerializedName("tgl_aduan")
        val tglAduan: Date? = null,

        @field:SerializedName("id_polisi")
        val idPolisi: String? = null,

        @field:SerializedName("isi_laporanpolisi")
        val isiLaporanpolisi: String? = null,

        @field:SerializedName("tgl_laporan")
        val tglLaporan: Date? = null,

        @field:SerializedName("status_aduan")
        val statusAduan: String? = null
)