package com.mexator.petfinder_client.extensions

fun Any.getTag(): String {
    return this.javaClass.simpleName
}