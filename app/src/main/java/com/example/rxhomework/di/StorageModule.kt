package com.example.rxhomework.di

import com.example.rxhomework.storage.StorageManager
import org.koin.dsl.module

val storageModule = module {
    single { StorageManager }
}