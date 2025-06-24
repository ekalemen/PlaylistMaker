package com.practicum.playlistmaker.search.data.network

import com.practicum.playlistmaker.search.data.NetworkClient
import com.practicum.playlistmaker.search.data.dto.Response
import com.practicum.playlistmaker.search.data.dto.TrackSearchRequest
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class RetrofitNetworkClient: NetworkClient {
    private val baseUrl = "https://itunes.apple.com"

    private val retrofit = Retrofit.Builder()
        .baseUrl(baseUrl)
        .addConverterFactory(GsonConverterFactory.create())
        .build()
    private val iTunesService = retrofit.create(ITunesAPIService::class.java)

    override fun doRequest(dto: Any): Response {
        if(dto is TrackSearchRequest) {
            val resp = iTunesService.search(dto.term).execute()
            val body = resp.body() ?: Response()

            return body.apply { resultCode = resp.code() }
        }  else {
            return Response().apply { resultCode = 400 }
        }
    }
}