package com.btracsolutions.ems.ViewModel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.btracsolutions.ems.Model.DeviceCount
import com.btracsolutions.ems.Model.EquipmentDetailsData
import com.btracsolutions.ems.Model.EquipmentsData
import com.btracsolutions.ems.Model.EquipmentsNew
import com.btracsolutions.ems.Utils.DispatcherProvider
import com.btracsolutions.flowwithapi.Api.ApiHelper
import com.btracsolutions.flowwithapi.Api.UiState
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class EquipmentAllDataViewModel  (
    private val apiHelper: ApiHelper,

    private val dispatcherProvider: DispatcherProvider
) : ViewModel() {


    // private val _uiState = MutableStateFlow<UiState<EquipmentDetailsData>?>(null)
    // val uiState: MutableStateFlow<UiState<EquipmentDetailsData>?> = _uiState

    private val _uiState = MutableStateFlow<UiState<EquipmentsData>>(UiState.Loading)

    val uiState: StateFlow<UiState<EquipmentsData>> = _uiState

    public fun fetchallEquipmentsData(id: Int, token: String, request_time:String) {
        viewModelScope.launch(dispatcherProvider.io) {

            System.out.println("checing viewpoint: $request_time")

            _uiState.value = UiState.Loading


            apiHelper.getallEquipmentData(id,token,request_time,"created_at","ASC")
                .flowOn(dispatcherProvider.io)
                .catch { e ->
                    _uiState.value = UiState.Error(e.toString())
                }
                .collectLatest {

                    Log.e("fetchallEquipmentsData", "Calledd")
                    _uiState.value = UiState.Success(it)
                }
        }
    }
}