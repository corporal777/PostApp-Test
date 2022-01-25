package org.testapp.postapp

import android.app.Application
import android.content.Context
import androidx.appcompat.app.AppCompatDelegate
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import org.koin.core.logger.Level
import org.testapp.postapp.di.networkModule
import org.testapp.postapp.di.repositoryModule
import org.testapp.postapp.di.viewModelModule

class App : Application() {

    companion object {
        lateinit var instance: App
            private set

        lateinit var context: Context
    }

    override fun onCreate() {
        super.onCreate()
        instance = this
        context = this
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
        startKoin {
            androidLogger(Level.NONE)
            androidContext(this@App)
            modules(
                listOf(
                    repositoryModule,
                    networkModule,
                    viewModelModule
                )
            )
        }
    }

}