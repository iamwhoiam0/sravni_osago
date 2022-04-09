package com.example.osagosravni.data.api

import com.example.osagosravni.data.entities.OffersData
import com.example.osagosravni.data.entities.RationDetailsData
import com.example.osagosravni.utils.Constants
import retrofit2.Response
import retrofit2.http.POST

interface ApiService {


    @POST(Constants.API_COEFFICIENT)
    suspend fun getCoefficient(): Response<RationDetailsData>

    @POST(Constants.API_INSURANCE_COMPANY)
    suspend fun getInsuranceCompany(): Response<OffersData>
}