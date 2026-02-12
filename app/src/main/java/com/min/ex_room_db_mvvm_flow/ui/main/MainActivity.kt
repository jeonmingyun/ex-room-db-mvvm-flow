package com.min.ex_room_db_mvvm_flow.ui.main

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.min.ex_room_db_mvvm_flow.App
import com.min.ex_room_db_mvvm_flow.R
import com.min.ex_room_db_mvvm_flow.ui.BaseViewModelFactory

class MainActivity : AppCompatActivity(), View.OnClickListener {
    private lateinit var dataTextView: TextView
    private val viewModel: MainViewModel by viewModels {
        BaseViewModelFactory((application as App).database)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // set view
        dataTextView = findViewById<TextView>(R.id.data_area_tv)

        // set click listener
        findViewById<Button>(R.id.insert_btn).setOnClickListener(this)
        findViewById<Button>(R.id.delete_btn).setOnClickListener(this)

        // set observer
        setViewObservers()

    }

    override fun onClick(view: View?) {
        when (view?.id) {
            R.id.insert_btn -> {
                // 버튼 클릭 시 ViewModel에 데이터 변경을 요청
                viewModel.insertUser("새로운", "사용자")
            }

            R.id.delete_btn -> {
                // 첫 번째 사용자 삭제 요청
                viewModel.users.value?.firstOrNull()?.let { userToDelete ->
                    viewModel.deleteUser(userToDelete)
                }
            }
        }
    }

    // ViewModel LiveData 관찰자(observer) 모음
    private fun setViewObservers() {
        //    데이터가 변경될 때마다 이 블록이 자동으로 호출됩니다.
        viewModel.users.observe(this) { it ->
            // UI 업데이트는 여기서만 처리합니다.
            dataTextView.text = it.joinToString("\n")
        }

    }
}