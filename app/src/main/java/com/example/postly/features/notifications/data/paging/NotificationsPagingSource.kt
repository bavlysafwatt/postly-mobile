package com.example.postly.features.notifications.data.paging

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.example.postly.core.common.Constants
import com.example.postly.core.domain.Result
import com.example.postly.core.network.PagingAppException
import com.example.postly.core.network.safeApiCall
import com.example.postly.features.notifications.data.dto.toDomain
import com.example.postly.features.notifications.data.remote.NotificationApi
import com.example.postly.features.notifications.domain.model.Notification

class NotificationsPagingSource(
    private val api: NotificationApi
) : PagingSource<Int, Notification>() {

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Notification> {
        val page = params.key ?: 1
        return when (val result = safeApiCall {
            api.getNotifications(
                page = page,
                limit = Constants.DEFAULT_PAGE_SIZE
            )
        }) {
            is Result.Success -> LoadResult.Page(
                data = result.data.notifications.map { it.toDomain() },
                prevKey = if (page == 1) null else page - 1,
                nextKey = if (result.data.hasNextPage) page + 1 else null
            )

            is Result.Failure -> LoadResult.Error(PagingAppException(result.error))
        }
    }

    override fun getRefreshKey(state: PagingState<Int, Notification>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            val anchorPage = state.closestPageToPosition(anchorPosition)
            anchorPage?.prevKey?.plus(1) ?: anchorPage?.nextKey?.minus(1)
        }
    }
}