package com.min.ex_room_db_mvvm_flow.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.min.ex_room_db_mvvm_flow.db.AppDatabase
import com.min.ex_room_db_mvvm_flow.ui.main.MainViewModel
import kotlin.jvm.java

/**
 * ViewModel을 객체화하는 클래스
 * */
class BaseViewModelFactory(private val db: AppDatabase) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        // 1. 요청된 클래스가 MainViewModel 클래스라면?
        if (modelClass.isAssignableFrom(MainViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return MainViewModel(db) as T
        }
        // 2. 만약 다른 ViewModel(예: DetailViewModel)이 추가된다면?
        // else if(modelClass.isAssignableFrom(DetailViewModel::class.java)) {
        //      return DetailViewModel(db.userDao()) as T
        // }
        else {
            throw IllegalArgumentException("Unknown ViewModel class")
        }

    }
}