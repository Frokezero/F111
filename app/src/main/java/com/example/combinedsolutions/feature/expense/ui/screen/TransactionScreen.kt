package com.example.combinedsolutions.feature.expense.ui.screen

import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.combinedsolutions.feature.expense.ui.viewmodel.TransactionViewModel

@Composable
fun TransactionScreen(
    viewModel: TransactionViewModel
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(uiState.userMessage) {
        uiState.userMessage?.let { message ->
            snackbarHostState.showSnackbar(message)
            viewModel.userMessageShown()
        }
    }

    TransactionContent(
        uiState = uiState,
        snackbarHostState = snackbarHostState,
        onTitleChange = viewModel::onTitleChange,
        onAmountChange = viewModel::onAmountChange,
        onTypeChange = viewModel::onTypeChange,
        onSaveClick = viewModel::saveTransaction,
        onClearClick = viewModel::clearAllData
    )
}
