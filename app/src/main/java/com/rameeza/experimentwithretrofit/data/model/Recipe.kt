package com.rameeza.experimentwithretrofit.data.model

import com.google.gson.annotations.SerializedName

data class RecipeResponse(
    @SerializedName("meals")
    val meals: List<Recipe>?
)

data class Recipe(
    @SerializedName("idMeal")
    val id: String,
    @SerializedName("strMeal")
    val name: String,
    @SerializedName("strCategory")
    val category: String,
    @SerializedName("strInstructions")
    val instructions: String,
    @SerializedName("strMealThumb")
    val imageUrl: String,
    @SerializedName("strYoutube")
    val youtubeUrl: String?
)
