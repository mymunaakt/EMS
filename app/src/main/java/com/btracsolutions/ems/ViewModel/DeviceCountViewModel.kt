package com.btracsolutions.ems.ViewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.btracsolutions.ems.Model.DeviceCount
import com.btracsolutions.ems.Model.EquipmentsNew
import com.btracsolutions.ems.Utils.DispatcherProvider
import com.btracsolutions.flowwithapi.Api.ApiHelper
import com.btracsolutions.flowwithapi.Api.UiState
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.launch

class DeviceCountViewModel(
    private val apiHelper: ApiHelper,
    private val dispatcherProvider: DispatcherProvider
) : ViewModel() {



    private val _uiState = MutableStateFlow<UiState<DeviceCount>>(UiState.Loading)

    val uiState: StateFlow<UiState<DeviceCount>> = _uiState

    public fun fetchTotalDeviceCount(token:String) {
        viewModelScope.launch(dispatcherProvider.main) {




            while (true){

                _uiState.value = UiState.Loading

                apiHelper.totalDeviceCount(token)
                    .flowOn(dispatcherProvider.io)
                    .catch { e ->
                        _uiState.value = UiState.Error(e.toString())
                    }
                    .collect {
                        _uiState.value = UiState.Success(it)
                    }
                delay(10000)
            }
        }
    }
}