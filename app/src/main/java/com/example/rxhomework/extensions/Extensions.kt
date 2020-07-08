package com.example.rxhomework.extensions

fun Any.getTag(): String {
    return this.javaClass.simpleName
}