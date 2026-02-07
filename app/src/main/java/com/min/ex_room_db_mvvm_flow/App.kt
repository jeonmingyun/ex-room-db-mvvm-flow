package com.min.ex_room_db_mvvm_flow

import android.app.Application
import com.min.ex_room_db_mvvm_flow.db.AppDatabase

class App : Application() {
    // AppDatabase 인스턴스를 저장할 변수
    // lazy를 사용해 실제로 접근할 때 단 한 번만 초기화
    val database by lazy { AppDatabase.getInstance(this) }

}