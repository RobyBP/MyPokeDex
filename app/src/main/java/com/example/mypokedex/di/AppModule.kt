package com.example.mypokedex.di

import android.content.Context
import coil.ImageLoader
import com.example.mypokedex.data.remote.PokeApi
import com.example.mypokedex.database.PokemonRepository
import com.example.mypokedex.util.Constants.baseUrl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Singleton
    @Provides
    fun providePokemonRepository(
        api: PokeApi
    ) = PokemonRepository(api)

    @Singleton
    @Provides
    fun providePokeApi(): PokeApi {
        return Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create())
            .baseUrl(baseUrl)
            .build()
            .create(PokeApi::class.java)
    }

    @Singleton
    @Provides
    fun provideImageLoader(@ApplicationContext appContext: Context): ImageLoader {
        return ImageLoader.Builder(appContext)
            .availableMemoryPercentage(0.25)
            .crossfade(true)
            .build()
    }

}
