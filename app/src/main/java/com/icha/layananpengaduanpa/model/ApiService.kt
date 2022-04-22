 package com.icha.layananpengaduanpa.model

import retrofit2.Call
import retrofit2.http.*

 interface ApiService {
    //get all aduan
    @GET("api/pengaduan")
    fun getListAduan(): Call<ArrayList<ResponsePengaduan>>

    //get aduan by id
    @GET("api/pengaduan/{kode_aduan}")
    fun getAduan(@Path("kode_aduan") kode_aduan:String): Call<ResponsePengaduan>

    //get aduan by status
    @GET("api/pengaduan/getaduanbystatus/{status_aduan}")
    fun getAduanStatus(@Query("status_aduan") status_aduan:String): Call<ArrayList<ResponsePengaduan>>

    //post new aduan using field x-www-form-urlencoded
    @FormUrlEncoded
    @POST("api/pengaduan")
    fun createNewAduan(
            @Field("kode_aduan") kode_aduan: String,
            @Field("lat_lokasi") lat_lokasi: Double,
            @Field("long_lokasi") long_lokasi: Double,
            @Field("kec_lokasi") kec_lokasi: String,
            @Field("isi_aduan") isi_aduan: String,
            @Field("tgl_aduan") tgl_aduan: String

    ): Call<ResponsePengaduan>
}