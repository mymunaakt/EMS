package com.btracsolutions.ems.ViewModel

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.btracsolutions.ems.Model.EquipmentDetailsData
import com.btracsolutions.ems.Model.Equipments
import com.btracsolutions.ems.NetworkRepositories.BaseResponse
import com.btracsolutions.ems.NetworkRepositories.EquipRepository
import com.btracsolutions.ems.NetworkRepositories.EquipmentDetailsDataRepository
import kotlinx.coroutines.launch

class EquipmentDetailsViewModel(application: Application) : AndroidViewModel(application) {
    val userRepo = EquipmentDetailsDataRepository()
    val equipmentDetailsResult: MutableLiveData<BaseResponse<EquipmentDetailsData>> = MutableLiveData()

    fun equipmentDetailsData(id:Int,token: String,request_time: String) {

        equipmentDetailsResult.value = BaseResponse.Loading()
        viewModelScope.launch {
            try {


                val response = userRepo.equipmentData(id=id,token = token, request_time = request_time)
                equipmentDetailsResult.value = BaseResponse.Success(response?.body())


                Log.d("MAinActivity", "EXcess: "+response?.body())


            } catch (ex: Exception) {
                Log.d("MAinActivity", "EXcess Error: "+ex.message)

                equipmentDetailsResult.value = BaseResponse.Error(ex.message)
            }
        }
    }
}