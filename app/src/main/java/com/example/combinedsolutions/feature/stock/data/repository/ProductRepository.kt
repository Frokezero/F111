package com.example.combinedsolutions.feature.stock.data.repository

import com.example.combinedsolutions.feature.stock.data.model.Product

interface ProductRepository {
    suspend fun addProduct(product: Product): Result<Unit>
    suspend fun getProducts(): Result<List<Product>>
    suspend fun updateStock(id: Long, newStock: Int): Result<Unit>
    suspend fun deleteProduct(id: Long): Result<Unit>
}
