 package com.icha.layananpengaduanpa.model

import retrofit2.Call
import retrofit2.http.*

 interface ApiService {
    //getAllAduan
     @GET("api/pengaduan/getAduan/{status_aduan}")
     fun getAllAduan(@Query("status_aduan") status_aduan: String): Call<ArrayList<ResponsePengaduan>>

    //get aduan by id
    @GET("api/pengaduan/getaduanbyid/{kode_aduan}")
    fun getAduan(@Query("kode_aduan") kode_aduan : String): Call<ResponsePengaduan>

    //get aduan by status
    @GET("api/pengaduan/getaduanbystatus/{status_aduan}")
    fun getAduanStatus(
            @Query("status_aduan") status_aduan:String,
            @Query("id_msy_fk") id_msy_fk:Int
    ): Call<ArrayList<ResponsePengaduan>>

     //get aduan by status & kec
     @GET("api/pengaduan/getaduanbykecamatan/")
     fun getAduanKec(
             @Query("status_aduan") status_aduan:String,
             @Query("kec_lokasi") kec_lokasi: String
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

    //get Akun Polisi
    @GET("api/polisi")
    fun getAkunPolisi(
            @Query("id_polisi") id_polisi: String? = null
    ): Call<ArrayList<PolisiModel>>

     //Tambah Akun Polisi
     @FormUrlEncoded
     @POST("api/polisi")
     fun tambahAkunPolisi(
         @Field("id_polisi") id_polisi: String,
         @Field("nama_polisi") nama_polisi: String,
         @Field("satuan_wilayah") satuan_wilayah: String,
         @Field("notelp_polisi") notelp_polisi: String,
         @Field("pass_polisi") pass_polisi: String
     ): Call<PolisiModel>

     @DELETE("api/polisi/")
     fun deleteAkunPolisi(@Query("id_polisi") id_polisi: String) : Call<PolisiModel>

     //Edit Akun Polisi
     @FormUrlEncoded
     @PUT("api/polisi")
     fun editAkunPolisi(
             @Field("id_polisi") id_polisi: String,
             @Field("nama_polisi") nama_polisi: String,
             @Field("satuan_wilayah") satuan_wilayah: String,
             @Field("notelp_polisi") notelp_polisi: String,
             @Field("pass_polisi") pass_polisi: String
     ): Call<PolisiModel>

     //get Akun Polisi
     @GET("api/spktpolsek")
     fun getAkunSpkt(@Query("id_spkt") id_spkt: String? = null): Call<SpktModel>

     //tambah akun spkt
     @FormUrlEncoded
     @POST("api/spktpolsek/tambahspkt")
     fun tambahAkunSpkt(
//             @Field("id_spkt") id_spkt: String,
             @Field("uname_spkt") uname_spkt: String,
             @Field("satuan_wilayah") satuan_wilayah: String,
             @Field("pass_spkt") pass_spkt: String,
             @Field("notelp_spkt") notelp_spkt: String
     ): Call<SpktModel>

     @DELETE("api/spktpolsek/")
     fun deleteAkunSpkt(@Query("id_spkt") id_spkt: String) : Call<SpktModel>

     //edit akun Spkt
     @FormUrlEncoded
     @PUT("api/spktpolsek")
     fun editAkunSpkt(
             @Field("id_spkt") id_spkt: String,
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

     //Login Akun SPKT
     @GET("auth/{id_spkt}")
     fun loginSpkt(
             @Query("id_spkt") id_spkt: String,
             @Query("role_user") role_user: String,
             @Query("pass_spkt") pass_spkt: String
     ): Call<SpktModel>

     //Login Akun Operator
     @GET("auth/{id_operator}")
     fun loginOperator(
             @Query("id_operator") id_operator: String,
             @Query("role_user") role_user: String,
             @Query("pass_operator") pass_operator: String
     ): Call<OperatorModel>

    //cari aduan - polisi
    @GET("api/pelaporan/cariaduan/{kode_aduan}")
    fun cariAduan(
        @Query("kode_aduan") kode_aduan : String
    ): Call<ResponsePengaduan>

    //post laporan
    @FormUrlEncoded
    @PUT("api/pelaporan/{kode_aduan}")
    fun tambahLaporan(
            @Path("kode_aduan") kode_aduan : String,
            @Field("id_polisi") id_polisi : String,
            @Field("isi_laporanpolisi") isi_laporanpolisi : String,
            @Field("tgl_laporan") tgl_laporan : String
    ) : Call<ResponsePengaduan>

    //get aduan oleh polisi
    @GET("api/pengaduan")
    fun getLaporan(@Query("id_polisi") id_polisi : String): Call<ArrayList<ResponsePengaduan>>
 }