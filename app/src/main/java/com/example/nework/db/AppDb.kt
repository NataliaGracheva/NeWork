package com.example.nework.db

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.nework.dao.PostDao
import com.example.nework.dao.PostRemoteKeyDao
import com.example.nework.dao.UserDao
import com.example.nework.entity.PostEntity
import com.example.nework.entity.PostRemoteKeyEntity
import com.example.nework.entity.UserEntity
import com.example.nework.utils.Converters


@Database(
    entities = [PostEntity::class, PostRemoteKeyEntity::class, UserEntity::class],
    version = 1,
    exportSchema = false
)
@TypeConverters(Converters::class)
abstract class AppDb : RoomDatabase() {
    abstract fun postDao(): PostDao
    abstract fun postRemoteKeyDao(): PostRemoteKeyDao
    abstract fun userDao(): UserDao
}