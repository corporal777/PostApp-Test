package org.testapp.postapp.data

import android.util.Log
import org.testapp.postapp.api.ApiService
import org.testapp.postapp.model.Post
import retrofit2.Response

class MainRepository(private val api: ApiService) {

    suspend fun getAllPosts(): Response<List<Int>>? {
        return try {
            api.loadAllPosts()
        } catch (e: Exception) {
            null
        }
    }

    suspend fun getChosenPost(id : Int): Response<Post>? {
        return try {
            api.loadPost(id)
        }catch (e : Exception){
            null
        }
    }
}