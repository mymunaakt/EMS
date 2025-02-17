package com.btracsolutions.ems.ViewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.btracsolutions.ems.Model.Equip
import com.btracsolutions.ems.Model.Equipments
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

class EquipmentViewModelWithFlow (
    private val apiHelper: ApiHelper,
    // private val dbHelper: DatabaseHelper,
    private val dispatcherProvider: DispatcherProvider
) : ViewModel() {

  //  private val _uiState = MutableStateFlow<UiState<List<Equipments>>>(UiState.Loading)

    //val uiState: StateFlow<UiState<List<Equipments>>> = _uiState

    private val _uiState = MutableStateFlow<UiState<Equip>>(UiState.Loading)

    val uiState: StateFlow<UiState<Equip>> = _uiState
/*
    init {
        fetchUsers()
    }*/
    public fun fetchUsers(token:String) {
        viewModelScope.launch(dispatcherProvider.main) {

          /*  _uiState.value = UiState.Loading

            apiHelper.getAllEquipments(token)
                .flowOn(dispatcherProvider.io)
                .catch { e ->
                    _uiState.value = UiState.Error(e.toString())
                }
                .collect {
                    _uiState.value = UiState.Success(it)
                }*/
// when api is refreshed with every 5 secs

            while (true){

                _uiState.value = UiState.Loading

                apiHelper.getAllEquipments(token)
                    .flowOn(dispatcherProvider.io)
                    .catch { e ->
                        _uiState.value = UiState.Error(e.toString())
                    }
                    .collect {
                        _uiState.value = UiState.Success(it)
                    }


                delay(5000)
            }
        }
    }
}