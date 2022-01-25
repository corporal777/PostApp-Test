package org.testapp.postapp.data

sealed class State<T>(val data: T? = null, val message: String? = null) {
    class Success<T>(data: T?) : State<T>(data)
    class Error<T>(data: T? = null) : State<T>(data)
    class Loading<T>(data: T? = null) : State<T>(data)
    class NoConnection<T>(data: T? = null) : State<T>(data)
}