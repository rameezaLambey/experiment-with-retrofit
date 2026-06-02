package com.rameeza.experimentwithretrofit

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.rounded.ErrorOutline
import androidx.compose.material.icons.rounded.Restaurant
import androidx.compose.material.icons.rounded.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.rameeza.experimentwithretrofit.ui.components.RecipeList
import com.rameeza.experimentwithretrofit.ui.theme.ExperimentWithRetrofitTheme
import com.rameeza.experimentwithretrofit.ui.viewmodel.RecipeViewModel
import com.rameeza.experimentwithretrofit.ui.viewmodel.UiState

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            ExperimentWithRetrofitTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    RecipeScreen()
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RecipeScreen(viewModel: RecipeViewModel = viewModel()) {
    var searchQuery by remember { mutableStateOf("") }
    val uiState by viewModel.uiState.collectAsState()
    val focusManager = LocalFocusManager.current

    val isIdle = uiState is UiState.Idle
    val searchBarPadding by animateDpAsState(
        targetValue = if (isIdle) 200.dp else 0.dp,
        animationSpec = tween(durationMillis = 600),
        label = "searchBarPadding"
    )

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        "Recipe Finder",
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 1.2.sp
                    )
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surface
                )
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            Spacer(modifier = Modifier.height(searchBarPadding))

            SearchHeader(
                query = searchQuery,
                onQueryChange = { searchQuery = it },
                onSearch = { 
                    viewModel.searchRecipes(searchQuery)
                    focusManager.clearFocus()
                },
                onRandomClick = { 
                    viewModel.fetchRandomRecipe()
                    focusManager.clearFocus()
                },
                showWelcome = isIdle
            )

            Box(modifier = Modifier.fillMaxSize()) {
                when (val state = uiState) {
                    is UiState.Idle -> {
                        // Handled by showWelcome in SearchHeader
                    }
                    is UiState.Loading -> {
                        CircularProgressIndicator(
                            modifier = Modifier.align(Alignment.Center),
                            strokeWidth = 3.dp,
                            color = MaterialTheme.colorScheme.primary
                        )
                    }
                    is UiState.Success -> {
                        RecipeList(recipes = state.recipes)
                    }
                    is UiState.Error -> {
                        InfoState(
                            icon = Icons.Rounded.ErrorOutline,
                            message = state.message,
                            color = MaterialTheme.colorScheme.error
                        )
                    }
                    is UiState.Empty -> {
                        InfoState(
                            icon = Icons.Rounded.Search,
                            message = "No recipes found. Try searching for another ingredient!"
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun SearchHeader(
    query: String,
    onQueryChange: (String) -> Unit,
    onSearch: () -> Unit,
    onRandomClick: () -> Unit,
    showWelcome: Boolean
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        if (showWelcome) {
            Icon(
                imageVector = Icons.Rounded.Restaurant,
                contentDescription = null,
                modifier = Modifier.size(80.dp),
                tint = MaterialTheme.colorScheme.primary
            )
            Text(
                text = "What's in your fridge?",
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onBackground
            )
            Text(
                text = "Search for recipes by ingredient",
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Spacer(modifier = Modifier.height(8.dp))
        }

        OutlinedTextField(
            value = query,
            onValueChange = onQueryChange,
            placeholder = { Text("Search ingredient (e.g., chicken)") },
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(16.dp),
            leadingIcon = { Icon(Icons.Default.Search, contentDescription = null) },
            singleLine = true,
            colors = TextFieldDefaults.colors(
                focusedContainerColor = MaterialTheme.colorScheme.surface,
                unfocusedContainerColor = MaterialTheme.colorScheme.surface,
                focusedIndicatorColor = MaterialTheme.colorScheme.primary,
                unfocusedIndicatorColor = MaterialTheme.colorScheme.outline
            ),
            trailingIcon = {
                if (query.isNotEmpty()) {
                    IconButton(onClick = onSearch) {
                        Icon(Icons.Default.Search, contentDescription = "Search", tint = MaterialTheme.colorScheme.primary)
                    }
                }
            }
        )

        Button(
            onClick = onRandomClick,
            modifier = Modifier.fillMaxWidth().height(56.dp),
            shape = RoundedCornerShape(16.dp),
            elevation = ButtonDefaults.buttonElevation(defaultElevation = 4.dp)
        ) {
            Icon(Icons.Rounded.Restaurant, contentDescription = null)
            Spacer(modifier = Modifier.width(8.dp))
            Text("I'm Feeling Lucky", fontWeight = FontWeight.SemiBold)
        }
    }
}

@Composable
fun BoxScope.InfoState(
    icon: ImageVector,
    message: String,
    color: Color = MaterialTheme.colorScheme.onSurfaceVariant
) {
    Column(
        modifier = Modifier
            .align(Alignment.Center)
            .padding(32.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            modifier = Modifier.size(64.dp),
            tint = color.copy(alpha = 0.6f)
        )
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = message,
            style = MaterialTheme.typography.bodyLarge,
            color = color,
            textAlign = TextAlign.Center
        )
    }
}
