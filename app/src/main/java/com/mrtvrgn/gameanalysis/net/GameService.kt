package com.mrtvrgn.gameanalysis.net

import com.mrtvrgn.gameanalysis.model.GameRecord
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.GET

interface GameService {

    @GET("shots")
    suspend fun getGameRecord(): GameRecord

    object Creator {
        @JvmStatic
        fun create(): GameService {

            val moshi = MoshiConverterFactory.create(
                Moshi.Builder().add(KotlinJsonAdapterFactory()).build()
            )
            val retrofit = Retrofit.Builder()
                .client(OkHttpClient().newBuilder().build())
                .baseUrl("http://ec2-18-188-69-79.us-east-2.compute.amazonaws.com:3000/")
                .addConverterFactory(moshi)
                .build()

            return retrofit.create(GameService::class.java)
        }
    }
}