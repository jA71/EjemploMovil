package com.casella.turecuador.db

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class Backend {

    companion object {

        private const val URL = "http://172.16.76.250:8080/"
        private var retrofit: Retrofit? = null

        private fun retrofitClient(): Retrofit? {
            if (retrofit == null) {
                retrofit = Retrofit.Builder().baseUrl(URL)
                    .addConverterFactory(GsonConverterFactory.create()).build()
            }
            return retrofit
        }

        fun api(): ApiService? {
            return retrofitClient()?.create(ApiService::class.java)
        }
    }
}