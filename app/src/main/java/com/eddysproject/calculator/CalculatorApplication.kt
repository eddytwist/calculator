package com.eddysproject.calculator

import android.app.Application
import com.eddysproject.calculator.di.databaseModules
import com.eddysproject.calculator.di.repositoryModule
import com.eddysproject.calculator.di.viewModelModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin

class CalculatorApplication : Application() {
    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidLogger()
            androidContext(this@CalculatorApplication)
            modules(
                listOf(databaseModules, repositoryModule, viewModelModule)
            )
        }
    }
}