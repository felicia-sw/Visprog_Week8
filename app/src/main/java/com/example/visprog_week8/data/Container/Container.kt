package com.example.visprog_week8.data.Container

import com.example.newproject.data.service.ArtistService
import com.example.newproject.util.ArtistConstants
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

/**
 * Container: A singleton responsible for creating and managing the Retrofit client and
 * providing the functional ArtistService instance.
 */
object Container {
    private val retrofit: Retrofit = Retrofit.Builder()
        .baseUrl(ArtistConstants.BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    // Lazily initialized service instance
    val artistService: ArtistService by lazy {
        retrofit.create(ArtistService::class.java)
    }
}
