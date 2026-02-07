package com.min.ex_room_db_mvvm_flow.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.min.ex_room_db_mvvm_flow.UserDao
import com.min.ex_room_db_mvvm_flow.UserEntity

/**
 * DB를 정의하는 클래스
 * abstract class이므로 외부에서 AppDatabase()로 생성 불가능! -> 싱글톤 구현
 */
@Database(entities = [UserEntity::class], version = 1)
abstract class AppDatabase : RoomDatabase() {

    abstract fun userDao(): UserDao

    companion object {
        /* [1] @Volatile : 이 어노테이션은 클래스의 어떤 프로퍼티(필드)가 Update됐을 때,
         * 그 갱신된 값을 다른 스레드에서 바로 읽게(= 캐시가 아닌 메인 메모리를 읽게) 한다.
         * 이 어노테이션이 없다면, 다른 스레드에서 갱신 이전의 값으로 잘못 읽을 수도 있다.
         * 캐시에는 시차가 존재하기 때문이다.*/
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getInstance(context: Context): AppDatabase {
            // [2] 1차 체크: 이미 인스턴스가 있다면 즉시 반환 (성능 최적화)
            // [2-2] 메인 메모리(Volatile 변수) 접근을 줄이기 위해 tempInstance로 반환
            val tempInstance = INSTANCE
            if (tempInstance != null) {
                return tempInstance
            }

            // [3] synchronized: 여러 스레드가 동시에 접근하지 못하도록 함
            synchronized(this) {
                // [4] 2차 체크: 다른 스레드가 먼저 들어와서 생성했을 수 있으므로 다시 NULL 확인 (무결성 보장)
                var instance = INSTANCE
                if (instance == null) {
                    // [5] 인스턴스 생성
                    instance = Room.databaseBuilder(
                        context.applicationContext,
                        AppDatabase::class.java,
                        "memo_database"
                    ).build()

                    // [6] 생성된 객체를 Volatile 변수에 할당
                    INSTANCE = instance
                }

                // [7] 최종적으로 생성된 인스턴스 반환
                return instance
            }
        }
    }
}