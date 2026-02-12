package com.min.ex_room_db_mvvm_flow.db

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * DB 테이블을 정의하는 클래스
 */
@Entity
data class UserEntity(
    @PrimaryKey(autoGenerate = true) val uid: Long = 0, // auto increment를 위해 초기값 0 세팅
    @ColumnInfo(name = "first_name") val firstName: String?,
    @ColumnInfo(name = "last_name") val lastName: String?
)