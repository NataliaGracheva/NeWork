package com.example.nework.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.nework.entity.UserPostEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface UserWallDao {
    @Query("SELECT * FROM UserPostEntity ORDER BY id DESC")
    fun getAll(): Flow<List<UserPostEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(post: UserPostEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(post: List<UserPostEntity>)

    @Query("DELETE FROM UserPostEntity WHERE id = :id")
    suspend fun removeById(id: Long)

    @Query("DELETE FROM UserPostEntity")
    suspend fun deleteAll()
}