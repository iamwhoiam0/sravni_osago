package com.example.osagosravni.presentation.viewModel

import androidx.lifecycle.ViewModel
import com.example.osagosravni.data.entities.OffersData
import com.example.osagosravni.data.repository.MainRepository
import com.example.osagosravni.utils.NetworkState
import java.io.IOException

class PolicePriceViewModel(
    private val mainRepository: MainRepository,
): ViewModel() {

    suspend fun getInsuranceCompanyData(): NetworkState<OffersData> {
        return try {
            mainRepository.getInsuranceCompany().let {
                if (it.isSuccessful) {
                    if (it.body() != null){
                        NetworkState.Success(it.body()!!)
                    }else{
                        NetworkState.InvalidData
                    }
                }else {
                    when(it.code()) {
                        403 -> NetworkState.HttpErrors.ResourceForbidden(it.message())
                        404 -> NetworkState.HttpErrors.ResourceNotFound(it.message())
                        500 -> NetworkState.HttpErrors.InternalServerError(it.message())
                        502 -> NetworkState.HttpErrors.BadGateWay(it.message())
                        301 -> NetworkState.HttpErrors.ResourceRemoved(it.message())
                        302 -> NetworkState.HttpErrors.RemovedResourceFound(it.message())
                        else -> NetworkState.Error(it.message())
                    }
                }
            }

        } catch (error : IOException) {
            NetworkState.NetworkException(error.message)
        }
    }
}