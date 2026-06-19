package com.niyuva.app.di

import com.niyuva.app.data.remote.api.GeminiApiService
import com.niyuva.app.data.remote.api.GroqApiService
import com.niyuva.app.data.remote.api.OpenRouterApiService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Named
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    @Provides
    @Singleton
    fun provideLoggingInterceptor(): HttpLoggingInterceptor {
        return HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }
    }

    @Provides
    @Singleton
    fun provideOkHttpClient(loggingInterceptor: HttpLoggingInterceptor): OkHttpClient {
        return OkHttpClient.Builder()
            .addInterceptor(loggingInterceptor)
            .build()
    }

    @Provides
    @Singleton
    @Named("gemini")
    fun provideGeminiRetrofit(okHttpClient: OkHttpClient): Retrofit {
        return Retrofit.Builder()
            .baseUrl("https://generativelanguage.googleapis.com/")
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    @Provides
    @Singleton
    @Named("groq")
    fun provideGroqRetrofit(okHttpClient: OkHttpClient): Retrofit {
        return Retrofit.Builder()
            .baseUrl("https://api.groq.com/")
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    @Provides
    @Singleton
    @Named("openrouter")
    fun provideOpenRouterRetrofit(okHttpClient: OkHttpClient): Retrofit {
        return Retrofit.Builder()
            .baseUrl("https://openrouter.ai/")
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    @Provides
    @Singleton
    fun provideGeminiApiService(@Named("gemini") retrofit: Retrofit): GeminiApiService {
        return retrofit.create(GeminiApiService::class.java)
    }

    @Provides
    @Singleton
    fun provideGroqApiService(@Named("groq") retrofit: Retrofit): GroqApiService {
        return retrofit.create(GroqApiService::class.java)
    }

    @Provides
    @Singleton
    fun provideOpenRouterApiService(@Named("openrouter") retrofit: Retrofit): OpenRouterApiService {
        return retrofit.create(OpenRouterApiService::class.java)
    }
}
