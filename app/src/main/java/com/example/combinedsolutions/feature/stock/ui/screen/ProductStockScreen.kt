package com.example.combinedsolutions.feature.stock.ui.screen

import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.combinedsolutions.feature.stock.ui.viewmodel.ProductViewModel

@Composable
fun ProductStockScreen(
    viewModel: ProductViewModel
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(uiState.userMessage) {
        uiState.userMessage?.let { message ->
            snackbarHostState.showSnackbar(message)
            viewModel.userMessageShown()
        }
    }

    ProductStockContent(
        uiState = uiState,
        snackbarHostState = snackbarHostState,
        onNameChange = viewModel::onNameChange,
        onStockChange = viewModel::onStockChange,
        onPriceChange = viewModel::onPriceChange,
        onAddClick = viewModel::addProduct,
        onStockIncrement = { product -> viewModel.updateStock(product, 1) },
        onStockDecrement = { product -> viewModel.updateStock(product, -1) },
        onDeleteClick = viewModel::deleteProduct,
        onRefreshClick = viewModel::loadProducts
    )
}
