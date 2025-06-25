package com.fit2081.Nadeem_Shayan_34075836.nutritrack.data.FruityVice

import retrofit2.http.GET
import retrofit2.http.Path

interface FruityViceApi {
    @GET("api/fruit/{fruitName}")
    suspend fun getFruit(@Path("fruitName") fruitName: String): Fruit
}