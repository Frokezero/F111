package com.example.communitystock.data.repository

import com.example.communitystock.data.model.Product

/**
 * Repository Interface ตามข้อกำหนดสถาปัตยกรรม MVVM
 * ตัดขาดคำสั่ง Supabase SDK ออกจากหน้าจอ UI
 */
interface ProductRepository {
    suspend fun getProducts(): Result<List<Product>>
    suspend fun addProduct(product: Product): Result<Unit>
    suspend fun updateStock(id: Long, newStock: Int): Result<Unit>
    suspend fun deleteProduct(id: Long): Result<Unit>
}
