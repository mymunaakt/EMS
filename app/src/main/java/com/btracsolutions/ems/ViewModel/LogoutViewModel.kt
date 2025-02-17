package com.btracsolutions.ems.ViewModel

import android.app.Application
import android.media.tv.TvContract.Channels.Logo
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.btracsolutions.ems.Model.Equipments
import com.btracsolutions.ems.Model.Logout
import com.btracsolutions.ems.NetworkRepositories.BaseResponse
import com.btracsolutions.ems.NetworkRepositories.EquipRepository
import com.btracsolutions.ems.NetworkRepositories.UserRepository
import kotlinx.coroutines.launch

class LogoutViewModel(application: Application) : AndroidViewModel(application)  {

    val userRepo = UserRepository()
    val logoutResult: MutableLiveData<BaseResponse<Logout>> = MutableLiveData()

    fun userLogout(token: String) {

        logoutResult.value = BaseResponse.Loading()
        viewModelScope.launch {
            try {


                val response = userRepo.logoutUser(token = token)
                logoutResult.value = BaseResponse.Success(response?.body())


                Log.d("MAinActivity", "EXcess: "+response?.body())


            } catch (ex: Exception) {
                Log.d("MAinActivity", "EXcess Error: "+ex.message)

                logoutResult.value = BaseResponse.Error(ex.message)
            }
        }
    }
}