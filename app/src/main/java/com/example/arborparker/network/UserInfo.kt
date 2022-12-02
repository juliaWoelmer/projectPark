package com.example.arborparker.network

data class UserInfo(
    var id: Int,
    var username: String,
    var password: String,
    var firstName: String?,
    var lastName: String?,
    var email: String?,
    var colorTheme: String
)
