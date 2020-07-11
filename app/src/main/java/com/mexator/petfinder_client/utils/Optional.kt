package com.mexator.petfinder_client.utils

/**
 * This generic class is used to workaround inability of RxJava to work with nulls.
 * Java's default Optional class is available only since API 29.
 */
class Optional<T>(private val actualValue: T?) {
    fun isEmpty() = actualValue == null
    fun get(): T? = actualValue
}