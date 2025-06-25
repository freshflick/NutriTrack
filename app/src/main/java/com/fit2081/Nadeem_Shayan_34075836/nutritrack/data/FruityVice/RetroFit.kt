package com.fit2081.Nadeem_Shayan_34075836.nutritrack.data.FruityVice

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitClient{
    val api: FruityViceApi by lazy{
        Retrofit.Builder()
            .baseUrl("https://www.fruityvice.com/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(FruityViceApi::class.java)
    }
}