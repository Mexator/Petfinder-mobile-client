package com.example.rxhomework.pojo

data class TokenResponse(
    val token_type: String,
    val expires_in: Int,
    val access_token: String
)