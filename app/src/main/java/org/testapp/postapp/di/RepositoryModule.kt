package org.testapp.postapp.di

import org.koin.dsl.module
import org.testapp.postapp.data.MainRepository

val repositoryModule = module {
    single { MainRepository(get()) }
}