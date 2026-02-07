package com.min.ex_room_db_mvvm_flow.ui.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.min.ex_room_db_mvvm_flow.UserEntity
import com.min.ex_room_db_mvvm_flow.db.AppDatabase
import kotlinx.coroutines.launch

class MainViewModel(private val db: AppDatabase) : ViewModel() {

    // 1. Dao의 Flow를 LiveData로 변환하여 UI가 관찰할 수 있도록 합니다.
    //    이 users 데이터는 DB가 변경되면 자동으로 업데이트됩니다.
    val users: LiveData<List<UserEntity>> = db.userDao().getAll().asLiveData()
    // 1. Dao의 Flow를 StateFlow로 변환합니다.
//    val users: StateFlow<List<UserEntity>> = db.userDao().getAll()
//        .stateIn(
//            scope = viewModelScope, // ViewModel의 생명주기와 함께 관리
//            started = SharingStarted.WhileSubscribed(5000L), // 구독자가 있을 때만 Flow를 활성화 (5초의 유예 시간)
//            initialValue = emptyList() // 초기값으로 빈 리스트를 제공
//        )

    // 2. Insert 로직: 이제 Activity가 아닌 ViewModel이 DB에 접근합니다.
    fun insertUser(firstName: String, lastName: String) {
        // 코루틴 시작
        viewModelScope.launch {
            db.userDao().insertAll(UserEntity(firstName = firstName, lastName = lastName))
        }
    }

    // 3. Delete 로직
    fun deleteUser(user: UserEntity) {
        // 코루틴 시작
        viewModelScope.launch {
            db.userDao().delete(user)
        }
    }
}
