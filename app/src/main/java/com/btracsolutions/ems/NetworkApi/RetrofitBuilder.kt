package com.btracsolutions.flowwithapi.Api

import android.util.Log
import com.btracsolutions.ems.Utils.Constant.BASE_URL
import com.btracsolutions.flowwithapi.Api.ApiService
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.IOException
import java.net.SocketTimeoutException
import java.util.concurrent.TimeUnit

object RetrofitBuilder {
   // private const val BASE_URL = "https://5e510330f2c0d300147c034c.mockapi.io/"
   // private const val BASE_URL = "http://13.251.135.12:8081"



    val okHttpClient = OkHttpClient.Builder()
        .connectTimeout(60, TimeUnit.SECONDS)
        .writeTimeout(60, TimeUnit.SECONDS)
        .readTimeout(60, TimeUnit.SECONDS)
        .addInterceptor { chain ->
            val request = chain.request()
            val response = try {
                chain.proceed(request)
            } catch (e: SocketTimeoutException) {
                // Log the timeout exception
                Log.e("NetworkTimeout", "Request timed out: ${request.url}", e)
                throw e
            }
            response
        }
        .build()
/*
    val okHttpClient = OkHttpClient.Builder()
        .addInterceptor { chain ->
            val request = chain.request()
            val response = chain.proceed(request)

            if (response.code == 422) {
                // Handle the 422 error
                val errorBody = response.body?.string()
                Log.e("APIError", "422 Unprocessable Content: $errorBody")

                // Optionally, you might want to throw an exception or handle it accordingly
                throw IOException("Unprocessable Content: $errorBody")
            }

            response
        }
        .build()*/






    private fun getRetrofit(): Retrofit {
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
           .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }
    val apiService: ApiService = getRetrofit().create(ApiService::class.java)
}