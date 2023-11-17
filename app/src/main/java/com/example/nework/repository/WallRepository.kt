package com.example.nework.repository

import com.example.nework.dto.Post
import kotlinx.coroutines.flow.Flow

interface WallRepository {
    val data: Flow<List<Post>>
    suspend fun getUserWall(userId: Long)
}