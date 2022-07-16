package com.icha.layananpengaduanpa.model
import com.google.gson.annotations.SerializedName
import java.util.*

data class ResponsePengaduan(
        @field:SerializedName("kode_aduan")
        val kodeAduan: String,

        @field:SerializedName("lat_lokasi")
        val latLokasi: Double,

        @field:SerializedName("long_lokasi")
        val longLokasi: Double,

        @field:SerializedName("kec_lokasi")
        val kecLokasi: String,

        @field:SerializedName("isi_aduan")
        val isiAduan: String,

        @field:SerializedName("tgl_aduan")
        val tglAduan: Date,

        @field:SerializedName("id_polisi")
        val idPolisi: String? = null,

        @field:SerializedName("isi_laporanpolisi")
        val isiLaporanpolisi: String? = null,

        @field:SerializedName("tgl_laporan")
        val tglLaporan: Date? = null,

        @field:SerializedName("status_aduan")
        val statusAduan: String,

        @field:SerializedName("id_msy_fk")
        val idMsyFk: String,

        @field:SerializedName("lat_laporan")
        val latLaporan: Double,

        @field:SerializedName("long_laporan")
        val longLaporan: Double
)