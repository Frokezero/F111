package com.example.communitystock.ui.screen

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
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

    ProductStockContent(
        state = uiState,
        onNameChange = viewModel::onNameChange,
        onStockChange = viewModel::onStockChange,
        onPriceChange = viewModel::onPriceChange,
        onAddProduct = viewModel::addProduct,
        onAdjustStock = viewModel::adjustStock,
        onDeleteProduct = viewModel::deleteProduct,
        onRefresh = viewModel::loadProducts,
        onDismissMessage = viewModel::dismissMessage,
        modifier = modifier
    )
}
