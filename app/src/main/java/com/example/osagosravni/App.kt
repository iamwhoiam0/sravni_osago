package com.example.osagosravni

import android.app.Application
import com.example.osagosravni.presentation.di.module.mainModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class App: Application() {

    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidContext(this@App)
            modules(mainModule)
        }
    }
}