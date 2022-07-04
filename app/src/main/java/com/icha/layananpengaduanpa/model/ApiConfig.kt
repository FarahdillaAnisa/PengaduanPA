package com.icha.layananpengaduanpa.model

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object ApiConfig {

//        private const val BASE_URL = "http://localhost/projectpa/projectpengaduan/"
        private const val BASE_URL = "http://192.168.42.131/projectpa/projectpengaduan/"
//        private const val BASE_URL = "http://10.0.2.2/projectpa/projectpengaduan/"
        val instance: ApiService by lazy {
            val retrofit = Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
//                    .client(client)
                    .build()

            retrofit.create(ApiService::class.java)
        }
//            val loggingInterceptor =
//                    HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY)
//            val client = OkHttpClient.Builder()
//                    .addInterceptor(loggingInterceptor)
//                    .build()
        }
