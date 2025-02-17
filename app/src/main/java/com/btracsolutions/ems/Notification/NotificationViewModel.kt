package com.btracsolutions.ems.Notification

import NotificationRepository
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.btracsolutions.ems.Model.Notification

import com.btracsolutions.flowwithapi.Api.ApiService
import kotlinx.coroutines.flow.Flow

class NotificationViewModel(private val repository: NotificationRepository) : ViewModel() {
    //val notifications: Flow<PagingData<Notification>> = repository.getNotifications().cachedIn(viewModelScope)
    fun notif(token:String):Flow<PagingData<Notification>> = repository.getNotifications(token = token).cachedIn(viewModelScope)
}