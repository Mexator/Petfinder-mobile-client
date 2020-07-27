package com.mexator.petfinder_client.data.model

abstract class PetModel {
    abstract val source: StorageLocation
    enum class StorageLocation { LOCAL, REMOTE }
}