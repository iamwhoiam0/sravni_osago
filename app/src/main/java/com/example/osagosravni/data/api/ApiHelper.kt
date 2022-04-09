package com.example.osagosravni.data.api

import com.example.osagosravni.data.entities.OffersData
import com.example.osagosravni.data.entities.RationDetailsData
import retrofit2.Response

interface ApiHelper {

    suspend fun getCoefficient(): Response<RationDetailsData>

    suspend fun getInsuranceCompany(): Response<OffersData>

}