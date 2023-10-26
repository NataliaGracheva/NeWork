package com.example.nework.repository

import androidx.paging.PagingData
import com.example.nework.dto.Post
import kotlinx.coroutines.flow.Flow

interface PostRepository {
    val data: Flow<PagingData<Post>>
}