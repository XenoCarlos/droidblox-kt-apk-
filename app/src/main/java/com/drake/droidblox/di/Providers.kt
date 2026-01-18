package com.drake.droidblox.di

import android.app.Application
import android.content.Context
import com.drake.droidblox.DBApplication
import com.drake.droidblox.apiservice.DiscordApi
import com.drake.droidblox.apiservice.IpLocationApi
import com.drake.droidblox.apiservice.RobloxApi
import com.drake.droidblox.apiservice.httpclient.customHttpClient
import com.drake.droidblox.logger.AndroidLogger
import com.drake.droidblox.logger.Logger
import com.drake.droidblox.sharedprefs.SettingsManager
import com.drake.droidblox.sharedprefs.FastFlagsManager
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import io.ktor.client.HttpClient
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object Providers {
    @Provides
    @Singleton
    fun provideCustomHttpClient(): HttpClient {
        return customHttpClient(AndroidLogger, "v1.0.0")
    } // TODO

    @Provides
    @Singleton
    fun provideLogger(): Logger {
        return AndroidLogger
    }

    @Provides
    @Singleton
    fun provideDiscordApi(logger: Logger, httpClient: HttpClient): DiscordApi {
        return DiscordApi(logger, httpClient)
    }

    @Provides
    @Singleton
    fun providesRobloxApi(logger: Logger, httpClient: HttpClient): RobloxApi {
        return RobloxApi(logger, httpClient)
    }

    @Provides
    @Singleton
    fun provideIpLocationApi(logger: Logger, httpClient: HttpClient): IpLocationApi {
        return IpLocationApi(logger, httpClient)
    }

    @Provides
    @Singleton
    fun provideSettingsManager(logger: Logger, @ApplicationContext context: Context): SettingsManager {
        return SettingsManager(logger, context)
    }

    @Provides
    @Singleton
    fun provideFFlagsManager(logger: Logger, @ApplicationContext context: Context): FastFlagsManager {
        return FastFlagsManager(logger, context)
    }
}
