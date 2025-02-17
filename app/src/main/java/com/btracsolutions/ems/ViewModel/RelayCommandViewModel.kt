package com.btracsolutions.ems.ViewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.btracsolutions.ems.Model.EquipmentsNew
import com.btracsolutions.ems.Model.RelayCommand
import com.btracsolutions.ems.Utils.DispatcherProvider
import com.btracsolutions.flowwithapi.Api.ApiHelper
import com.btracsolutions.flowwithapi.Api.UiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.launch

class RelayCommandViewModel (
    private val apiHelper: ApiHelper,
    // private val dbHelper: DatabaseHelper,
    private val dispatcherProvider: DispatcherProvider
) : ViewModel() {

    private val _uiState = MutableStateFlow<UiState<RelayCommand>>(UiState.Loading)

    val uiState: StateFlow<UiState<RelayCommand>> = _uiState

    public fun sendCommand(token:String, command: String, relay: String, hardware_id: String) {
        viewModelScope.launch(dispatcherProvider.main) {

            _uiState.value = UiState.Loading

            apiHelper.transferRelayCommand(token,command,relay,hardware_id)
                .flowOn(dispatcherProvider.io)
                .catch { e ->
                    _uiState.value = UiState.Error(e.toString())
                }
                .collect {
                    _uiState.value = UiState.Success(it)
                }
        }
    }
}