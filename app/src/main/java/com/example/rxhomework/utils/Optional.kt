package com.example.rxhomework.utils

class Optional<T>(private val actualValue: T?) {
    fun isEmpty() = actualValue == null
    fun get(): T? = actualValue
}