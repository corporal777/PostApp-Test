package org.testapp.postapp.api

import org.testapp.postapp.model.Post
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path

interface ApiService {
    @GET("api/v1/hot")
    suspend fun loadAllPosts(): Response<List<Int>>

    @GET("api/v1/post/{id}")
    suspend fun loadPost(@Path("id") id: Int): Response<Post>
}