package com.example.nework.repository


import com.example.nework.api.WallService
import com.example.nework.dao.UserWallDao
import com.example.nework.dto.Post
import com.example.nework.entity.toDto
import com.example.nework.entity.toUserPostEntity
import com.example.nework.error.ApiError
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class WallRepositoryImpl @Inject constructor(
    private val dao: UserWallDao,
    private val service: WallService,
) :
    WallRepository {
    override val data: Flow<List<Post>> = dao.getAll()
        .map { it.toDto() }
        .flowOn(Dispatchers.Default)

    override suspend fun getUserWall(userId: Long) {
        dao.deleteAll()
        val response = service.getUserWall(userId)
        if (!response.isSuccessful) {
            throw ApiError(response.code(), response.message())
        }
        val body = response.body() ?: throw ApiError(response.code(), response.message())
        dao.insert(body.toUserPostEntity())
    }
}