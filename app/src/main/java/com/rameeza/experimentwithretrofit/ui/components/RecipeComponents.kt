package com.rameeza.experimentwithretrofit.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import androidx.compose.ui.tooling.preview.Preview
import com.rameeza.experimentwithretrofit.data.model.Recipe
import com.rameeza.experimentwithretrofit.ui.theme.ExperimentWithRetrofitTheme

@Composable
fun RecipeItem(recipe: Recipe) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column {
            AsyncImage(
                model = recipe.imageUrl,
                contentDescription = recipe.name,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp),
                contentScale = ContentScale.Crop
            )
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = recipe.name,
                    style = MaterialTheme.typography.headlineSmall
                )
                Text(
                    text = recipe.category,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.secondary
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = recipe.instructions,
                    style = MaterialTheme.typography.bodySmall,
                    maxLines = 3
                )
            }
        }
    }
}

@Composable
fun RecipeList(recipes: List<Recipe>) {
    LazyColumn {
        items(recipes) { recipe ->
            RecipeItem(recipe = recipe)
        }
    }
}

@Preview(showBackground = true)
@Composable
fun RecipeItemPreview() {
    ExperimentWithRetrofitTheme {
        RecipeItem(
            recipe = Recipe(
                id = "1",
                name = "Chocolate Cake",
                category = "Dessert",
                instructions = "Mix ingredients and bake...",
                imageUrl = "https://www.themealdb.com/images/media/meals/wvpsw11487340456.jpg",
                youtubeUrl = null
            )
        )
    }
}
