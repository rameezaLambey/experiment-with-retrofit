package com.rameeza.experimentwithretrofit.data.remote

import kotlinx.coroutines.runBlocking
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Before
import org.junit.Test
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class ApiServiceTest {

    private lateinit var mockWebServer: MockWebServer
    private lateinit var apiService: ApiService

    @Before
    fun setup() {
        mockWebServer = MockWebServer()
        apiService = Retrofit.Builder()
            .baseUrl(mockWebServer.url("/"))
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ApiService::class.java)
    }

    @After
    fun tearDown() {
        mockWebServer.shutdown()
    }

    @Test
    fun `searchRecipes returns success and parses data correctly`() = runBlocking {
        val mockResponse = MockResponse()
            .setResponseCode(200)
            .setBody("""
                {
                    "meals": [
                        {
                            "idMeal": "52772",
                            "strMeal": "Teriyaki Chicken Casserole",
                            "strCategory": "Chicken",
                            "strInstructions": "Cook it",
                            "strMealThumb": "https://url.com/image.jpg",
                            "strYoutube": "https://youtube.com/v"
                        }
                    ]
                }
            """.trimIndent())
        mockWebServer.enqueue(mockResponse)

        val response = apiService.searchRecipes("chicken")
        
        assertNotNull(response.body())
        assertEquals(1, response.body()?.meals?.size)
        assertEquals("Teriyaki Chicken Casserole", response.body()?.meals?.get(0)?.name)
    }

    @Test
    fun `getRandomRecipe returns success`() = runBlocking {
        val mockResponse = MockResponse()
            .setResponseCode(200)
            .setBody("""{"meals": [{"idMeal": "1", "strMeal": "Test", "strCategory": "Cat", "strInstructions": "Inst", "strMealThumb": "thumb"}]}""")
        mockWebServer.enqueue(mockResponse)

        val response = apiService.getRandomRecipe()
        
        assertEquals(200, response.code())
        assertEquals("Test", response.body()?.meals?.get(0)?.name)
    }
}
