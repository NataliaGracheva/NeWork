package com.example.nework.repository

import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.map
import com.example.nework.api.PostService
import com.example.nework.dao.PostDao
import com.example.nework.dao.PostRemoteKeyDao
import com.example.nework.db.AppDb
import com.example.nework.dto.Post
import com.example.nework.entity.PostEntity
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map


import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class PostRepositoryImpl @Inject constructor(
    private val postService: PostService,
    db: AppDb,
    private val postDao: PostDao,
    postRemoteKeyDao: PostRemoteKeyDao,
) :
    PostRepository {
    @OptIn(ExperimentalPagingApi::class)
    override val data: Flow<PagingData<Post>> = Pager(
        config = PagingConfig(pageSize = 5),
        remoteMediator = PostRemoteMediator(postService, db, postDao, postRemoteKeyDao),
        pagingSourceFactory = postDao::pagingSource,
    ).flow.map { pagingData ->
        pagingData.map(PostEntity::toDto) }
}