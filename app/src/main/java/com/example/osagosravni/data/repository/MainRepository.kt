package com.example.osagosravni.data.repository

import com.example.osagosravni.data.api.ApiHelper

class MainRepository (private val apiHelper: ApiHelper){

    suspend fun getCoefficient() = apiHelper.getCoefficient()

    suspend fun getInsuranceCompany() = apiHelper.getInsuranceCompany()
}