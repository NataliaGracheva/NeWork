package com.example.nework.repository

import com.example.nework.dto.Job
import kotlinx.coroutines.flow.Flow

interface JobRepository {
    val data: Flow<List<Job>>

    suspend fun getUserJobs(id: Long)
}