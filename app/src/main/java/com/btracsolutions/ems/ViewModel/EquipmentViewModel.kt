package com.btracsolutions.ems.ViewModel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.btracsolutions.ems.Model.Equipments
import com.btracsolutions.ems.NetworkRepositories.EquipmentRepository
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class EquipmentViewModel constructor(private val repository: EquipmentRepository)  : ViewModel()  {

   /* val equipmentList = MutableLiveData<Equipments>()
    val errorMessage = MutableLiveData<String>()

    fun getAllEquipments(token: String) {
        val response = repository.getAllEquipments(token)
        response?.enqueue(object : Callback<Equipments> {
            override fun onResponse(call: Call<Equipments>, response: Response<Equipments>) {
                Log.d("MAIN RESPONSE", "onCreate RSPONSE:"+response.body())

                equipmentList.postValue(response.body())
            }
            override fun onFailure(call: Call<Equipments>, t: Throwable) {
                Log.d("MAIN RESPONSE", "onCreate Error 1:"+t.message)

                errorMessage.postValue(t.message)
            }
        })
    }*/
}