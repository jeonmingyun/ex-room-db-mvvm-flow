package com.min.ex_room_db_mvvm_flow

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

/**
 * DB 쿼리를 정의하는 인터페이스
 */
@Dao
interface UserDao {
    @Query("SELECT * FROM user")
    fun getAll(): List<UserEntity>

    @Query("SELECT * FROM user WHERE uid IN (:userIds)")
    fun loadAllByIds(userIds: IntArray): List<UserEntity>

    @Query("SELECT * FROM user WHERE first_name LIKE :first AND " +
            "last_name LIKE :last LIMIT 1")
    fun findByName(first: String, last: String): UserEntity

    @Insert(onConflict = OnConflictStrategy.REPLACE) // REPLACE = 동일한 데이터가 있으면 덮어쓰기
    fun insertAll(vararg users: UserEntity)

    @Delete
    fun delete(user: UserEntity)
}