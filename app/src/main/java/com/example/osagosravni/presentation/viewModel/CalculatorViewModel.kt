package com.example.osagosravni.presentation.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.osagosravni.data.entities.RationDetailsData
import com.example.osagosravni.data.repository.MainRepository
import com.example.osagosravni.utils.Constants
import com.example.osagosravni.utils.NetworkState
import java.io.IOException


class CalculatorViewModel(
    private val mainRepository: MainRepository,
): ViewModel() {

    private val _editTextList: MutableLiveData<ArrayList<String>> = MutableLiveData()
    val editTextList : LiveData<ArrayList<String>>
        get() = _editTextList

    private val _oldEditTextList: MutableLiveData<ArrayList<String>> = MutableLiveData()
    val oldEditTextList : LiveData<ArrayList<String>>
        get() = _oldEditTextList

    private val _coefficientData = MutableLiveData<RationDetailsData>()
    val coefficientData: LiveData<RationDetailsData>
        get() = _coefficientData

    init {
        _oldEditTextList.value = Constants.textList
        _editTextList.value = Constants.textList
    }

    suspend fun getCoefficientData(): NetworkState<RationDetailsData> {
        return try {
            mainRepository.getCoefficient().let {
                if (it.isSuccessful) {
                    if (it.body() != null){
                        _coefficientData.postValue(it.body())
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

    fun updateEditTextList(newList: ArrayList<String>) {
        val data = editTextList.value
        _oldEditTextList.value = data!!
        _editTextList.value = newList
    }

}