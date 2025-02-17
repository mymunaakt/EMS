package com.btracsolutions.ems.ViewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.btracsolutions.ems.NetworkRepositories.EquipmentRepository
import com.btracsolutions.ems.NetworkRepositories.UserRepository

class MyViewModelFactory constructor(private val repository: EquipmentRepository): ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return if (modelClass.isAssignableFrom(EquipmentViewModel::class.java)) {
            EquipmentViewModel(this.repository) as T
        } else {
            throw IllegalArgumentException("ViewModel Not Found")
        }
    }
}