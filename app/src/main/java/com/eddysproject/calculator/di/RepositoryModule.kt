package com.eddysproject.calculator.di

import com.eddysproject.calculator.ui.main.MainRepository
import org.koin.dsl.module

val repositoryModule = module {
    single { MainRepository(get()) }
}