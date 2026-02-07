package com.min.ex_room_db_mvvm_flow

import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import com.min.ex_room_db_mvvm_flow.db.AppDatabase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MainActivity : AppCompatActivity() {
    private lateinit var db: AppDatabase
    private var users: List<UserEntity> = listOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // DB 초기화
        db = (application as App).database

        findViewById<Button>(R.id.insert_btn).setOnClickListener {
            insertData()
        }
        findViewById<Button>(R.id.delete_btn).setOnClickListener {
            deleteData()
        }

        // 화면 초기화
        setDataArea()
    }

    private fun setDataArea() {
        // --- 데이터베이스 작업은 코루틴 스코프 내에서 수행 ---
        lifecycleScope.launch {
            // 1. users 변수 초기화가 끝날때까지 suspend 함수를 사용해 코루틴 블록내 작업 일시 정지
            users = selectAllData()

            // 2. 데이터 조회가 완료된 후, 그 결과로 UI를 업데이트합니다.
            findViewById<TextView>(R.id.data_area_tv).text = users.toString()
        }
    }

    /**
     * select 예시
     */
    private suspend fun selectAllData(): List<UserEntity> {
        // withContext는 마지막 줄의 결과를 반환하므로, 바로 return 할 수 있습니다.
        return withContext(Dispatchers.IO) {
            db.userDao().getAll() // withContext()는 suspend 함수이므로 작업이 끝날 때까지 함수가 '일시 중단'됩니다.
        }
    }

    /**
     * delete 예시
     */
    private fun deleteData() {
        // --- 데이터베이스 작업은 코루틴 스코프 내에서 수행 ---
        lifecycleScope.launch {
            // IO 스레드에서 데이터베이스 작업 실행
            withContext(Dispatchers.IO) {
                val userDao = db.userDao()

                // 데이터가 없을때 delete를 실행하면 오류 발생함, empty 체크 필수
                if(users.isNotEmpty()) {
                    // 예시 : Delete
                    userDao.delete(users.get(0))
                }
            }
            // Delete 작업이 모두 끝난 후, UI를 갱신합니다.
            // withContext가 suspend 함수이므로 이 코드는 withContext가 끝난 후에 실행되는 것이 보장
            setDataArea()
        }
    }

    /**
     * insert 예시
     */
    private fun insertData() {
        // --- 데이터베이스 작업은 코루틴 스코프 내에서 수행 ---
        lifecycleScope.launch {
            // IO 스레드에서 데이터베이스 작업 실행
            withContext(Dispatchers.IO) {
                val userDao = db.userDao()

                // 예시 : Insert
                userDao.insertAll(
                    UserEntity(firstName = "길동", lastName = "홍"),
                    UserEntity(firstName = "철수", lastName = "김")
                )
            }
            // Insert 작업이 모두 끝난 후, UI를 갱신합니다.
            // withContext가 suspend 함수이므로 이 코드는 withContext가 끝난 후에 실행되는 것이 보장
            setDataArea()
        }

    }
}