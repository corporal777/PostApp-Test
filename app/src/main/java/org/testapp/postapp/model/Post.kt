package org.testapp.postapp.model

import com.google.gson.annotations.SerializedName

data class Post(
    @SerializedName("type")
    val type: String,
    @SerializedName("payload")
    val payload: Payload

)

data class Payload(
    @SerializedName("url")
    val url: String,
    @SerializedName("text")
    val text: String
)