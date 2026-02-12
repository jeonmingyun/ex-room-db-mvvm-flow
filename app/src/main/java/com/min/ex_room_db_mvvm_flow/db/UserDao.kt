package com.min.ex_room_db_mvvm_flow

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

/**
 * DB 쿼리를 정의하는 인터페이스
 */
@Dao
interface UserDao {
    // DB 업데이트시 실시간 반영이 필요한 쿼리에 Flow 사용
    @Query("SELECT * FROM UserEntity")
    fun getAll(): Flow<List<UserEntity>>

    @Query("SELECT * FROM UserEntity WHERE uid IN (:userIds)")
    suspend fun loadAllByIds(userIds: IntArray): List<UserEntity>

    @Query("SELECT * FROM UserEntity WHERE first_name LIKE :first AND " +
            "last_name LIKE :last LIMIT 1")
    suspend fun findByName(first: String, last: String): UserEntity

    @Insert(onConflict = OnConflictStrategy.REPLACE) // REPLACE = 동일한 데이터가 있으면 덮어쓰기
    suspend fun insertAll(vararg users: UserEntity)

    @Delete
    suspend fun delete(user: UserEntity)
}