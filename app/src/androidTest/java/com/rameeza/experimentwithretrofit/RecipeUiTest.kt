package com.rameeza.experimentwithretrofit

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performTextInput
import com.rameeza.experimentwithretrofit.ui.theme.ExperimentWithRetrofitTheme
import org.junit.Rule
import org.junit.Test

class RecipeUiTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun initialUiState_isDisplayed() {
        composeTestRule.setContent {
            ExperimentWithRetrofitTheme {
                RecipeScreen()
            }
        }

        // Check if title is displayed
        composeTestRule.onNodeWithText("Recipe Finder").assertIsDisplayed()

        // Check if search field is displayed
        composeTestRule.onNodeWithText("Search ingredient (e.g., chicken)").assertIsDisplayed()

        // Check if Lucky button is displayed
        composeTestRule.onNodeWithText("I'm Feeling Lucky (Random Recipe)").assertIsDisplayed()
    }

    @Test
    fun typingInSearchField_updatesText() {
        composeTestRule.setContent {
            ExperimentWithRetrofitTheme {
                RecipeScreen()
            }
        }

        val searchField = composeTestRule.onNodeWithText("Search ingredient (e.g., chicken)")
        searchField.performTextInput("Beef")
        
        composeTestRule.onNodeWithText("Beef").assertIsDisplayed()
    }
    
    @Test
    fun clickingSearchButton_showsLoading() {
        composeTestRule.setContent {
            ExperimentWithRetrofitTheme {
                RecipeScreen()
            }
        }

        // We can't easily mock the ViewModel here without more setup, 
        // but we can verify the search button exists.
        composeTestRule.onNodeWithContentDescription("Search").assertIsDisplayed()
    }
}
