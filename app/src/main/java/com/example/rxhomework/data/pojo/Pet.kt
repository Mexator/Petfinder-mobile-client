package com.example.rxhomework.data.pojo

data class Pet(
    val id: Long,
    val age: String?,
    val name: String,
    val description: String?,
    val type: String,
    val breed: String?,
    val photos: List<PetPhoto>?
)