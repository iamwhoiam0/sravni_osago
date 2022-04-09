package com.example.osagosravni.presentation.di.module

import com.example.osagosravni.data.api.ApiHelper
import com.example.osagosravni.data.api.ApiHelperImpl
import com.example.osagosravni.data.api.ApiService
import com.example.osagosravni.data.repository.MainRepository
import com.example.osagosravni.presentation.viewModel.CalculatorViewModel
import com.example.osagosravni.presentation.viewModel.PolicePriceViewModel
import com.example.osagosravni.utils.Constants
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

val mainModule = module{

    single {
        provideRetrofit(Constants.BASE_URL)
    }

    factory {
        provideApiService(get())
    }

    factory<ApiHelper> {
        return@factory ApiHelperImpl(get())
    }

    single {
        MainRepository(get())
    }

    single {
        CalculatorViewModel(get())
    }

    single {
        PolicePriceViewModel(get())
    }
}

private fun provideRetrofit(BASE_URL: String): Retrofit {
    return Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .build()
}

private fun provideApiService(retrofit: Retrofit): ApiService = retrofit.create(ApiService::class.java)