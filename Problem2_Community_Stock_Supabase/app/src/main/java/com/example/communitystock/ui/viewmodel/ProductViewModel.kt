package com.example.communitystock.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.communitystock.data.model.Product
import com.example.communitystock.data.repository.ProductRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

/**
 * UI State สำหรับระบบจัดการสต็อกสินค้า
 */
data class ProductUiState(
    val products: List<Product> = emptyList(),
    val nameInput: String = "",
    val stockInput: String = "",
    val priceInput: String = "",
    val isLoading: Boolean = false,
    val message: String? = null
)

class ProductViewModel(
    private val repository: ProductRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(ProductUiState())
    val uiState: StateFlow<ProductUiState> = _uiState.asStateFlow()

    init {
        loadProducts()
    }

    fun onNameChange(name: String) {
        _uiState.update { it.copy(nameInput = name, message = null) }
    }

    fun onStockChange(stock: String) {
        if (stock.isEmpty() || stock.matches(Regex("^\\d+$"))) {
            _uiState.update { it.copy(stockInput = stock, message = null) }
        }
    }

    fun onPriceChange(price: String) {
        if (price.isEmpty() || price.matches(Regex("^\\d*\\.?\\d*$"))) {
            _uiState.update { it.copy(priceInput = price, message = null) }
        }
    }

    /**
     * R - Read: ดึงข้อมูลสินค้าทั้งหมด
     */
    fun loadProducts() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            val result = repository.getProducts()
            result.onSuccess { list ->
                _uiState.update {
                    it.copy(
                        products = list,
                        isLoading = false
                    )
                }
            }.onFailure { error ->
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        message = "โหลดข้อมูลล้มเหลว: ${error.localizedMessage}"
                    )
                }
            }
        }
    }

    /**
     * C - Create: เพิ่มสินค้าใหม่ลง Supabase
     */
    fun addProduct() {
        val state = _uiState.value
        val name = state.nameInput.trim()
        val stock = state.stockInput.toIntOrNull()
        val price = state.priceInput.toDoubleOrNull()

        if (name.isBlank()) {
            _uiState.update { it.copy(message = "กรุณากรอกชื่อสินค้า") }
            return
        }
        if (stock == null || stock < 0) {
            _uiState.update { it.copy(message = "กรุณาระบุจำนวนสินค้าให้ถูกต้อง") }
            return
        }
        if (price == null || price < 0.0) {
            _uiState.update { it.copy(message = "กรุณาระบุราคาให้ถูกต้อง") }
            return
        }

        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            val newProduct = Product(
                productName = name,
                stockQty = stock,
                price = price
            )
            val result = repository.addProduct(newProduct)
            result.onSuccess {
                _uiState.update {
                    it.copy(
                        nameInput = "",
                        stockInput = "",
                        priceInput = "",
                        message = "เพิ่มสินค้า '$name' สำเร็จ"
                    )
                }
                loadProducts()
            }.onFailure { error ->
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        message = "เพิ่มสินค้าล้มเหลว: ${error.localizedMessage}"
                    )
                }
            }
        }
    }

    /**
     * U - Update: ปรับสต็อก (+1 หรือ -1) ทันที
     */
    fun adjustStock(product: Product, delta: Int) {
        val productId = product.id ?: return
        val currentStock = product.stockQty
        val updatedStock = currentStock + delta

        if (updatedStock < 0) {
            _uiState.update { it.copy(message = "จำนวนสินค้าไม่สามารถน้อยกว่า 0 ได้") }
            return
        }

        // อัปเดต UI ทันทีแบบ Optimistic เพื่อความลื่นไหล
        _uiState.update { currentState ->
            val updatedList = currentState.products.map {
                if (it.id == productId) it.copy(stockQty = updatedStock) else it
            }
            currentState.copy(products = updatedList)
        }

        // ยิงคำสั่ง Update ไปยัง Supabase
        viewModelScope.launch {
            val result = repository.updateStock(productId, updatedStock)
            result.onFailure { error ->
                _uiState.update {
                    it.copy(message = "ปรับปรุงสต็อกล้มเหลว: ${error.localizedMessage}")
                }
                // ย้อนค่ากลับโดยโหลดจาก server
                loadProducts()
            }
        }
    }

    /**
     * D - Delete: ลบสินค้าตาม ID
     */
    fun deleteProduct(product: Product) {
        val productId = product.id ?: return

        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            val result = repository.deleteProduct(productId)
            result.onSuccess {
                _uiState.update {
                    it.copy(
                        message = "ลบสินค้า '${product.productName}' เรียบร้อยแล้ว"
                    )
                }
                loadProducts()
            }.onFailure { error ->
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        message = "ลบสินค้าล้มเหลว: ${error.localizedMessage}"
                    )
                }
            }
        }
    }

    fun dismissMessage() {
        _uiState.update { it.copy(message = null) }
    }

    class Factory(private val repository: ProductRepository) : ViewModelProvider.Factory {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(ProductViewModel::class.java)) {
                return ProductViewModel(repository) as T
            }
            throw IllegalArgumentException("Unknown ViewModel class")
        }
    }
}
