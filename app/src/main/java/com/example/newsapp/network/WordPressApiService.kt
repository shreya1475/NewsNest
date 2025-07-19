package com.example.newsapp.network

import com.example.newsapp.model.Author
import com.example.newsapp.model.FeaturedMedia
import com.example.newsapp.model.WordPressPost
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Path

interface WordPressApiService {

    @GET("wp-json/wp/v2/posts")
    suspend fun getPosts(): List<WordPressPost>

    @GET("wp-json/wp/v2/users/{id}")
    suspend fun getAuthor(@Path("id") id: Int): Author

    @GET("wp-json/wp/v2/media/{id}")
    suspend fun getMedia(@Path("id") id: Int): FeaturedMedia

    companion object {

        /**
         * Creates the WordPress API service.
         *
         * @param baseUrl Full URL of your WordPress site (e.g. https://myblog.com/)
         * @param token Optional Bearer token for authentication.
         */
        fun create(
            baseUrl: String,
            token: String? = null
        ): WordPressApiService {
            val clientBuilder = OkHttpClient.Builder()

            if (!token.isNullOrEmpty()) {
                clientBuilder.addInterceptor { chain ->
                    val request = chain.request().newBuilder()
                        .addHeader("Authorization", "Bearer $token")
                        .build()
                    chain.proceed(request)
                }
            }

            val retrofit = Retrofit.Builder()
                .baseUrl(baseUrl)
                .client(clientBuilder.build())
                .addConverterFactory(GsonConverterFactory.create())
                .build()

            return retrofit.create(WordPressApiService::class.java)
        }
    }
}
