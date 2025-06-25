package com.fit2081.Nadeem_Shayan_34075836.nutritrack.data.FruityVice

data class Fruit(
    val name: String,
    val family: String,
    val nutritions: Nutrition
)

//as nutrition info is in another category so separate
data class Nutrition(
    val calories: Float,
    val fat: Float,
    val sugar: Float,
    val carbohydrates: Float,
    val protein: Float
)