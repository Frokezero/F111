package com.example.expensetracker

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import com.example.expensetracker.data.repository.TransactionRepositoryImpl
import com.example.expensetracker.ui.screen.TransactionScreen
import com.example.expensetracker.ui.viewmodel.TransactionViewModel

class MainActivity : ComponentActivity() {

    // ViewModel Lifecycle Retained เมื่อหมุนจอ (Configuration Change)
    private val viewModel: TransactionViewModel by viewModels {
        val repository = TransactionRepositoryImpl(applicationContext)
        TransactionViewModel.Factory(repository)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MaterialTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    TransactionScreen(viewModel = viewModel)
                }
            }
        }
    }
}
