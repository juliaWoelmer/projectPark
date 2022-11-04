package com.example.arborparker.network

import retrofit2.Call
import retrofit2.http.*

interface NetworkApi {
    @GET("/spots")
    suspend fun getSpots(): List<Spot>

    // Get user info by id
    @GET("/user/{id}")
    fun getUserInfoById(@Path("id") id: Int): Call<List<UserInfo>>

    // returns user information by the username
    @GET("/user/by-username/{username}")
    fun getUserInfoByUsername(@Path("username") username: String): Call<List<UserInfo>>

    // adds user to database
    @POST("/user/add-new-user")
    fun addUser(@Body userdata: User): Call<UserId>

    @PUT("/user/edit-user-profile/{id}")
    fun editUserProfile(@Path("id") id: Int, @Body userProfileInfo: UserProfileInfo): Call<RowsAffected>


}