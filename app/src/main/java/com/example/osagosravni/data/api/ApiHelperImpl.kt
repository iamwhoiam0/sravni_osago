package com.example.osagosravni.data.api

import com.example.osagosravni.data.entities.OffersData
import com.example.osagosravni.data.entities.RationDetailsData
import retrofit2.Response

class ApiHelperImpl (private val apiService: ApiService) : ApiHelper {

    override suspend fun getCoefficient():
            Response<RationDetailsData> = apiService.getCoefficient()

    override suspend fun getInsuranceCompany():
            Response<OffersData> = apiService.getInsuranceCompany()

}