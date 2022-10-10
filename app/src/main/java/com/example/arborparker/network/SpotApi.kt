package com.example.arborparker.network

import retrofit2.http.GET

interface SpotApi {
    @GET("/spots")
    suspend fun getSpots(): List<Spot>
}