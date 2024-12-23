package com.practicum.playlistmaker

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface ITunesAPI {
    @GET("/search?entity=song")
    fun search(@Query("term", encoded = false) text: String): Call<ITunesResponse>
}

class ITunesResponse(
    val resultCount: Int,
    val results: List<Track>,
)

