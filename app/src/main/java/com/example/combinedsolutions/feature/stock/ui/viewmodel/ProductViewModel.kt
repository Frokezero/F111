package com.example.combinedsolutions.feature.stock.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.combinedsolutions.feature.stock.data.model.Product
import com.example.combinedsolutions.feature.stock.data.repository.ProductRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class ProductStockUiState(
    val nameInput: String = "",
    val stockInput: String = "",
    val priceInput: String = "",
    val products: List<Product> = emptyList(),
    val isLoading: Boolean = false,
    val userMessage: String? = null
)

class ProductViewModel(
    private val repository: ProductRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(ProductStockUiState())
    val uiState: StateFlow<ProductStockUiState> = _uiState.asStateFlow()

    init {
        loadProducts()
    }

    fun onNameChange(name: String) {
        _uiState.update { it.copy(nameInput = name) }
    }

    fun onStockChange(stock: String) {
        if (stock.isEmpty() || stock.toIntOrNull() != null) {
            _uiState.update { it.copy(stockInput = stock) }
        }
    }

    fun onPriceChange(price: String) {
        if (price.isEmpty() || price.toDoubleOrNull() != null) {
            _uiState.update { it.copy(priceInput = price) }
        }
    }

    fun addProduct() {
        val current = _uiState.value
        val name = current.nameInput.trim()
        val stock = current.stockInput.toIntOrNull()
        val price = current.priceInput.toDoubleOrNull()

        if (name.isBlank()) {
            _uiState.update { it.copy(userMessage = "กรุณากรอกชื่อสินค้า") }
            return
        }
        if (stock == null || stock < 0) {
            _uiState.update { it.copy(userMessage = "กรุณากรอกจำนวนสินค้าให้ถูกต้อง (>= 0)") }
            return
        }
        if (price == null || price < 0) {
            _uiState.update { it.copy(userMessage = "กรุณากรอกราคาให้ถูกต้อง (>= 0)") }
            return
        }

        val product = Product(
            productName = name,
            stockQty = stock,
            price = price
        )

        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            repository.addProduct(product)
                .onSuccess {
                    _uiState.update {
                        it.copy(
                            nameInput = "",
                            stockInput = "",
                            priceInput = "",
                            userMessage = "เพิ่มสินค้าเรียบร้อยแล้ว"
                        )
                    }
                    loadProductsInternal()
                }
                .onFailure { error ->
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            userMessage = "เพิ่มสินค้าล้มเหลว: ${error.localizedMessage}"
                        )
                    }
                }
        }
    }

    fun updateStock(product: Product, delta: Int) {
        val newStock = product.stockQty + delta
        if (newStock < 0) {
            _uiState.update { it.copy(userMessage = "จำนวนสินค้าคงเหลือต้องไม่ติดลบ") }
            return
        }

        val id = product.id ?: return

        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            repository.updateStock(id, newStock)
                .onSuccess {
                    loadProductsInternal()
                }
                .onFailure { error ->
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            userMessage = "อัปเดตสต็อกล้มเหลว: ${error.localizedMessage}"
                        )
                    }
                }
        }
    }

    fun deleteProduct(product: Product) {
        val id = product.id ?: return

        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            repository.deleteProduct(id)
                .onSuccess {
                    _uiState.update { it.copy(userMessage = "ลบรายการสินค้าเรียบร้อยแล้ว") }
                    loadProductsInternal()
                }
                .onFailure { error ->
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            userMessage = "ลบรายการสินค้าล้มเหลว: ${error.localizedMessage}"
                        )
                    }
                }
        }
    }

    fun loadProducts() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            loadProductsInternal()
        }
    }

    private suspend fun loadProductsInternal() {
        repository.getProducts()
            .onSuccess { list ->
                _uiState.update {
                    it.copy(
                        products = list,
                        isLoading = false
                    )
                }
            }
            .onFailure { error ->
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        userMessage = "โหลดข้อมูลล้มเหลว: ${error.localizedMessage}"
                    )
                }
            }
    }

    fun userMessageShown() {
        _uiState.update { it.copy(userMessage = null) }
    }

    class Factory(private val repository: ProductRepository) : ViewModelProvider.Factory {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return ProductViewModel(repository) as T
        }
    }
}
