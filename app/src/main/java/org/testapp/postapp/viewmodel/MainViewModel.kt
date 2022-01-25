package org.testapp.postapp.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.async
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.testapp.postapp.data.MainRepository
import org.testapp.postapp.data.State
import org.testapp.postapp.model.Post

class MainViewModel(private val repo: MainRepository) : ViewModel() {

    private val _id: MutableLiveData<State<List<Int>>> = MutableLiveData()
    val id: LiveData<State<List<Int>>> = _id

    private val _post: MutableLiveData<State<Post>> = MutableLiveData()
    val post: LiveData<State<Post>> = _post

    fun getAllPosts() {
        viewModelScope.launch {
            val result = async {
                repo.getAllPosts()
            }
            val response = result.await()
            if (response?.body() != null && response.code() == 200) {
                _id.postValue(State.Success(response.body()))
            } else {
                _id.postValue(State.Error())
            }
        }
    }

    fun getChosenPost(id: Int) {
        _post.postValue(State.Loading())
        viewModelScope.launch {
            delay(1000)
            val result = async {
                repo.getChosenPost(id)
            }
            val response = result.await()
            if (response?.body() != null && response.code() == 200) {
                _post.postValue(State.Success(response.body()))
            } else {
                _post.postValue(State.Error())
            }
        }
    }

}