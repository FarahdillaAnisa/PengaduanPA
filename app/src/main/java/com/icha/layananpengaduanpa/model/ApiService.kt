 package com.icha.layananpengaduanpa.model

import retrofit2.Call
import retrofit2.http.*

 interface ApiService {
    //get aduan by id
    @GET("api/pengaduan/getaduanbyid/{kode_aduan}")
    fun getAduan(@Query("kode_aduan") kode_aduan : String): Call<ResponsePengaduan>

    //get aduan by status
    @GET("api/pengaduan/getaduanbystatus/{status_aduan}")
    fun getAduanStatus(
            @Query("status_aduan") status_aduan:String,
            @Query("id_msy_fk") id_msy_fk:Int
    ): Call<ArrayList<ResponsePengaduan>>

    //post new aduan using field x-www-form-urlencoded
    @FormUrlEncoded
    @POST("api/pengaduan")
    fun createNewAduan(
         @Field("kode_aduan") kode_aduan: String,
         @Field("lat_lokasi") lat_lokasi: Double,
         @Field("long_lokasi") long_lokasi: Double,
         @Field("kec_lokasi") kec_lokasi: String,
         @Field("isi_aduan") isi_aduan: String,
         @Field("tgl_aduan") tgl_aduan: String,
         @Field("id_msy_fk") id_msy_fk: Int
    ): Call<ResponsePengaduan>

    //Signup-User
    @FormUrlEncoded
    @POST("auth/registration")
    fun signupUser(
         @Field("nama_msy") nama_msy: String,
         @Field("notelp_msy") notelp_msy: String,
         @Field("uname_msy") uname_msy: String,
         @Field("pass_msy") pass_msy: String
    ): Call<MasyarakatModel>

    //login akun masyarakat
    @GET("auth/{uname_user}")
    fun loginMasyarakat(
            @Query("uname_user") uname_msy: String,
            @Query("role_user") role_user: String,
            @Query("pass_user") pass_msy: String
    ): Call<MasyarakatModel>

     //Signup-User
     @FormUrlEncoded
     @POST("api/polisi")
     fun tambahAkunPolisi(
         @Field("id_polisi") id_polisi: String,
         @Field("nama_polisi") nama_polisi: String,
         @Field("satuan_wilayah") satuan_wilayah: String,
         @Field("notelp_polisi") notelp_polisi: String,
         @Field("pass_polisi") pass_polisi: String
     ): Call<PolisiModel>

     //tambah akun spkt
     @FormUrlEncoded
     @POST("api/spktpolsek")
     fun tambahAkunSpkt(
//             @Field("id_spkt") id_spkt: String,
             @Field("uname_spkt") uname_spkt: String,
             @Field("satuan_wilayah") satuan_wilayah: String,
             @Field("pass_spkt") pass_spkt: String,
             @Field("notelp_spkt") notelp_spkt: String
     ): Call<SpktModel>

    //Login Akun Polisi
    @GET("auth/{id_polisi}")
    fun loginPolisi(
            @Query("id_polisi") id_polisi: String,
            @Query("role_user") role_user: String,
            @Query("pass_polisi") pass_polisi: String
    ): Call<PolisiModel>

    //cari aduan - polisi
    @GET("api/pelaporan/cariaduan/{kode_aduan}")
    fun cariAduan(
        @Query("kode_aduan") kode_aduan : String
    ): Call<ResponsePengaduan>

    //post laporan
    @PUT("api/pelaporan/{kode_aduan}")
    fun tambahLaporan(
            @Query("kode_aduan") kode_aduan : String,
            @Query("id_polisi") id_polisi : String,
            @Query("isi_laporanpolisi") isi_laporanpolisi : String,
            @Query("tgl_laporan") tgl_laporan : String
    ) : Call<ResponsePengaduan>

    //get aduan oleh polisi
    @GET("api/pengaduan")
    fun getLaporan(@Query("id_polisi") id_polisi : String): Call<ArrayList<ResponsePengaduan>>


 }