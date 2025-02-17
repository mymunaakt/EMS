package com.btracsolutions.ems.ViewModel

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.btracsolutions.ems.Model.Equipments
import com.btracsolutions.ems.Model.LoginRequest
import com.btracsolutions.ems.Model.LoginResponse
import com.btracsolutions.ems.NetworkRepositories.*
import kotlinx.coroutines.launch

class EquipmentViewModelT(application: Application) : AndroidViewModel(application) {

    val userRepo = EquipRepository()
    val equipmentResult: MutableLiveData<BaseResponse<Equipments>> = MutableLiveData()

    fun equipmentList(token: String) {

        equipmentResult.value = BaseResponse.Loading()
        viewModelScope.launch {
            try {


                val response = userRepo.equipmentList(token = token)
                equipmentResult.value = BaseResponse.Success(response?.body())


                Log.d("MAinActivity", "EXcess: "+response?.body())


            } catch (ex: Exception) {
                Log.d("MAinActivity", "EXcess Error: "+ex.message)

                equipmentResult.value = BaseResponse.Error(ex.message)
            }
        }
    }
}