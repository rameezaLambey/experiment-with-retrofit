package com.rameeza.experimentwithretrofit.ui.viewmodel

import com.rameeza.experimentwithretrofit.data.remote.ApiService
import com.rameeza.experimentwithretrofit.mocks.RecipeMock
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.MockitoAnnotations
import retrofit2.Response

@ExperimentalCoroutinesApi
class RecipeViewModelTest {

    private val testDispatcher = StandardTestDispatcher()

    @Mock
    private lateinit var apiService: ApiService

    private lateinit var viewModel: RecipeViewModel

    @Before
    fun setup() {
        MockitoAnnotations.openMocks(this)
        Dispatchers.setMain(testDispatcher)
        viewModel = RecipeViewModel(apiService)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun successOnRecipesFound() = runTest {
        val recipes = RecipeMock.createRecipeList()
        val response = RecipeMock.createRecipeResponse(recipes)
        `when`(apiService.searchRecipes("chicken")).thenReturn(Response.success(response))

        val states = mutableListOf<UiState>()
        val collectJob = launch(UnconfinedTestDispatcher(testScheduler)) {
            viewModel.uiState.collect { states.add(it) }
        }

        viewModel.searchRecipes("chicken")
        advanceUntilIdle()

        assertTrue("Loading state was not emitted", states.contains(UiState.Loading))
        assertTrue("Success state was not emitted", states.any { it is UiState.Success })
        assertEquals(recipes, (states.find { it is UiState.Success } as UiState.Success).recipes)

        collectJob.cancel()
    }

    @Test
    fun emptyOnNoRecipesFound() = runTest {
        val response = RecipeMock.createRecipeResponse(null)
        `when`(apiService.searchRecipes("nonexistent")).thenReturn(Response.success(response))

        val states = mutableListOf<UiState>()
        val collectJob = launch(UnconfinedTestDispatcher(testScheduler)) {
            viewModel.uiState.collect { states.add(it) }
        }

        viewModel.searchRecipes("nonexistent")
        advanceUntilIdle()

        assertTrue("Loading state was not emitted", states.contains(UiState.Loading))
        assertTrue("Empty state was not emitted", states.contains(UiState.Empty))

        collectJob.cancel()
    }

    @Test
    fun errorOnApiFailure() = runTest {
        `when`(apiService.searchRecipes("error")).thenThrow(RuntimeException("Network Error"))

        val states = mutableListOf<UiState>()
        val collectJob = launch(UnconfinedTestDispatcher(testScheduler)) {
            viewModel.uiState.collect { states.add(it) }
        }

        viewModel.searchRecipes("error")
        advanceUntilIdle()

        assertTrue("Loading state was not emitted", states.contains(UiState.Loading))
        assertTrue("Error state was not emitted", states.any { it is UiState.Error })
        assertEquals("Exception: Network Error", (states.find { it is UiState.Error } as UiState.Error).message)

        collectJob.cancel()
    }
}
