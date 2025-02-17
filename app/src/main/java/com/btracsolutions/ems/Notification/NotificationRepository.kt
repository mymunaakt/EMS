import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.btracsolutions.ems.Model.Notification
import com.btracsolutions.flowwithapi.Api.ApiService

import kotlinx.coroutines.flow.Flow

class NotificationRepository(private val service: ApiService) {
    fun getNotifications(token: String): Flow<PagingData<Notification>> {
        return Pager(
            config = PagingConfig(pageSize = 10, enablePlaceholders = false),
            pagingSourceFactory = { NotificationPagingSource(service,token) }
        ).flow
    }
}
