package com.example.communitystock.ui.screen

import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import com.example.communitystock.ui.viewmodel.ProductViewModel

/**
 * Stateful Composable:
 * ผูก ViewModel เข้ากับ UI State และส่ง event ไปยัง Stateless Composable (ProductStockContent)
 */
@Composable
fun ProductStockScreen(
    viewModel: ProductViewModel,
    modifier: Modifier = Modifier
) {
    val uiState by viewModel.uiState.collectAsState()
    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(uiState.message) {
        uiState.message?.let { message ->
            snackbarHostState.showSnackbar(message)
            viewModel.dismissMessage()
        }
    }

    ProductStockContent(
        uiState = uiState,
        snackbarHostState = snackbarHostState,
        onNameChange = viewModel::onNameChange,
        onStockChange = viewModel::onStockChange,
        onPriceChange = viewModel::onPriceChange,
        onAddClick = viewModel::addProduct,
        onStockIncrement = { product -> viewModel.adjustStock(product, 1) },
        onStockDecrement = { product -> viewModel.adjustStock(product, -1) },
        onDeleteClick = viewModel::deleteProduct,
        onRefreshClick = viewModel::loadProducts,
        modifier = modifier
    )
}
