package com.example.postly.features.feed.data.paging

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.example.postly.core.common.Constants
import com.example.postly.core.domain.Result
import com.example.postly.core.network.PagingAppException
import com.example.postly.core.network.safeApiCall
import com.example.postly.features.feed.data.dto.toDomain
import com.example.postly.features.feed.data.remote.FeedApi
import com.example.postly.features.feed.domain.model.Comment

class CommentsPagingSource(
    private val api: FeedApi,
    private val postId: String
) : PagingSource<Int, Comment>() {

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Comment> {
        val page = params.key ?: 1
        return when (val result =
            safeApiCall { api.getComments(postId, page, Constants.DEFAULT_PAGE_SIZE) }) {
            is Result.Success -> LoadResult.Page(
                data = result.data.comments.map { it.toDomain() },
                prevKey = if (page == 1) null else page - 1,
                nextKey = if (result.data.hasNextPage) page + 1 else null
            )

            is Result.Failure -> LoadResult.Error(PagingAppException(result.error))
        }
    }

    override fun getRefreshKey(state: PagingState<Int, Comment>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            val anchorPage = state.closestPageToPosition(anchorPosition)
            anchorPage?.prevKey?.plus(1) ?: anchorPage?.nextKey?.minus(1)
        }
    }
}