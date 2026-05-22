package com.rameeza.experimentwithretrofit.data.remote

import com.rameeza.experimentwithretrofit.data.model.RecipeResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiService {

    @GET("search.php")
    suspend fun searchRecipes(
        @Query("s") query: String
    ): Response<RecipeResponse>

    @GET("random.php")
    suspend fun getRandomRecipe(): Response<RecipeResponse>

    @GET("filter.php")
    suspend fun filterByCategory(
        @Query("c") category: String
    ): Response<RecipeResponse>

    @GET("lookup.php")
    suspend fun getRecipeById(
        @Query("i") id: String
    ): Response<RecipeResponse>
}
