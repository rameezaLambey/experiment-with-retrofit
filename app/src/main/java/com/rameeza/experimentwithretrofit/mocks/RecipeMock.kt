package com.rameeza.experimentwithretrofit.mocks

import com.rameeza.experimentwithretrofit.data.model.Recipe
import com.rameeza.experimentwithretrofit.data.model.RecipeResponse

object RecipeMock {
    fun createRecipe(
        id: String = "1",
        name: String = "Chicken Curry",
        category: String = "Chicken",
        instructions: String = "Cook it with spices.",
        imageUrl: String = "https://www.themealdb.com/images/media/meals/wvpsw11487340621.jpg",
        youtubeUrl: String = "https://www.youtube.com/watch?v=dQw4w9WgXcQ"
    ): Recipe {
        return Recipe(
            id = id,
            name = name,
            category = category,
            instructions = instructions,
            imageUrl = imageUrl,
            youtubeUrl = youtubeUrl
        )
    }

    fun createRecipeList(count: Int = 1): List<Recipe> {
        return List(count) { i ->
            createRecipe(id = (i + 1).toString(), name = "Recipe ${i + 1}")
        }
    }

    fun createRecipeResponse(recipes: List<Recipe>? = listOf(createRecipe())): RecipeResponse {
        return RecipeResponse(meals = recipes)
    }

    fun createRecipeJsonResponse(count: Int = 1): String {
        val mealsJson = createRecipeList(count).joinToString(",") { recipe ->
            """
            {
                "idMeal": "${recipe.id}",
                "strMeal": "${recipe.name}",
                "strCategory": "${recipe.category}",
                "strInstructions": "${recipe.instructions}",
                "strMealThumb": "${recipe.imageUrl}",
                "strYoutube": "${recipe.youtubeUrl}"
            }
            """.trimIndent()
        }
        return """{"meals": [$mealsJson]}"""
    }
}
