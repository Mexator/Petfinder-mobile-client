package com.example.rxhomework.data.pojo

data class TokenResponse(
    val token_type: String,
    val expires_in: Int,
    val access_token: String
)