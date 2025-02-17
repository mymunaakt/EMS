import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.btracsolutions.ems.Model.Notification
import com.btracsolutions.flowwithapi.Api.ApiService

import retrofit2.HttpException
import java.io.IOException

class NotificationPagingSource(
    private val service: ApiService,
    val token: String
) : PagingSource<Int, Notification>() {

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Notification> {
        val position = params.key ?: 0
        return try {
            val response = service.getNotifications(skip = position, token = token)

            System.out.println("dsfdff "+response.results)
            LoadResult.Page(
                data = response.results,
                prevKey = if (position == 0) null else position - 10,
                nextKey = if (response.results.isEmpty()) null else position + 10
            )
        } catch (exception: IOException) {
            System.out.println("dsfdff io"+exception)

            LoadResult.Error(exception)
        } catch (exception: HttpException) {
            System.out.println("dsfdff http"+exception)

            LoadResult.Error(exception)
        }
    }

    override fun getRefreshKey(state: PagingState<Int, Notification>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            val anchorPage = state.closestPageToPosition(anchorPosition)
            anchorPage?.prevKey?.plus(10) ?: anchorPage?.nextKey?.minus(10)
        }
    }
}
