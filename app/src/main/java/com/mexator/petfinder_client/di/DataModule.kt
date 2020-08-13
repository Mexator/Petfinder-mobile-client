package com.mexator.petfinder_client.di

import com.mexator.petfinder_client.data.remote.api_interaction.CookieHolder
import org.koin.dsl.module

val dataModule = module {
    single { CookieHolder }
}