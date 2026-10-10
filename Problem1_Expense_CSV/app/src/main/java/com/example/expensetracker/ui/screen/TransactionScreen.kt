package com.example.expensetracker.ui.screen

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import com.example.expensetracker.ui.viewmodel.TransactionViewModel

/**
 * Stateful Composable (Screen):
 * เชื่อมโยงกับ ViewModel และดึง StateFlow ออกมาส่งต่อให้ Stateless Composable (TransactionContent)
 * ตามหลัก State Hoisting ของ Jetpack Compose
 */
@Composable
fun TransactionScreen(
    viewModel: TransactionViewModel,
    modifier: Modifier = Modifier
) {
    val uiState by viewModel.uiState.collectAsState()

    TransactionContent(
        state = uiState,
        onTitleChange = viewModel::onTitleChange,
        onAmountChange = viewModel::onAmountChange,
        onTypeChange = viewModel::onTypeChange,
        onSaveClick = viewModel::saveTransaction,
        onClearClick = viewModel::clearData,
        modifier = modifier
    )
}
