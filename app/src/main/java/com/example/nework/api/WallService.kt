package com.example.nework.api

import com.example.nework.dto.Post
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface WallService {
    @GET("{authorId}/wall/{id}/before")
    suspend fun getBefore(
        @Path("authorId") authorId: Long,
        @Path("id") id: Long,
        @Query("count") count: Int,
    ): Response<List<Post>>

    @GET("{authorId}/wall/{id}/after")
    suspend fun getAfter(
        @Path("authorId") authorId: Long,
        @Path("id") id: Long,
        @Query("count") count: Int,
    ): Response<List<Post>>

    @GET("{authorId}/wall/latest")
    suspend fun getLatest(
        @Path("authorId") authorId: Long,
        @Query("count") count: Int,
    ): Response<List<Post>>

    @GET("{authorId}/wall")
    suspend fun getUserWall(
        @Path("authorId") authorId: Long
    ): Response<List<Post>>

}