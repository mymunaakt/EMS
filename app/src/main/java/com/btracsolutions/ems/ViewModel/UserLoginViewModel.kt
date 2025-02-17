package com.btracsolutions.ems.ViewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.btracsolutions.ems.Model.EquipmentsNew
import com.btracsolutions.ems.Model.LoginRequest
import com.btracsolutions.ems.Model.LoginResponse
import com.btracsolutions.ems.Utils.DispatcherProvider
import com.btracsolutions.flowwithapi.Api.ApiHelper
import com.btracsolutions.flowwithapi.Api.UiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.launch

class UserLoginViewModel(
    private val apiHelper: ApiHelper,
    // private val dbHelper: DatabaseHelper,
    private val dispatcherProvider: DispatcherProvider
) : ViewModel() {

    //  private val _uiState = MutableStateFlow<UiState<List<Equipments>>>(UiState.Loading)

    //val uiState: StateFlow<UiState<List<Equipments>>> = _uiState

    private val _uiState = MutableStateFlow<UiState<LoginResponse>>(UiState.Loading)

    val uiState: StateFlow<UiState<LoginResponse>> = _uiState
    /*
        init {
            fetchUsers()
        }*/
    public fun fetchLoginInfo(email: String, pwd: String) {
        viewModelScope.launch(dispatcherProvider.main) {

            val loginRequest = LoginRequest(
                password = pwd,
                email = email
            )

            _uiState.value = UiState.Loading

            apiHelper.login(loginRequest)
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