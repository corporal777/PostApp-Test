package org.testapp.postapp.di

import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import org.testapp.postapp.viewmodel.MainViewModel

val viewModelModule = module {
    viewModel { MainViewModel(get()) }
}