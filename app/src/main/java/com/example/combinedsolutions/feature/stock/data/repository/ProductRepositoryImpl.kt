package com.example.combinedsolutions.feature.stock.data.repository

import com.example.combinedsolutions.feature.stock.data.model.Product
import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.postgrest.from
import io.github.jan.supabase.postgrest.query.Order
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class ProductRepositoryImpl(
    private val supabaseClient: SupabaseClient
) : ProductRepository {

    private val tableName = "products"

    // In-memory fallback สำหรับการสาธิตแบบ Demo กรณีไม่ได้ใส่ Supabase Key
    private val mockProducts = mutableListOf(
        Product(id = 2L, productName = "น้ำผึ้งแท้ป่าเดือนห้า", stockQty = 15, price = 250.0),
        Product(id = 1L, productName = "ข้าวหอมมะลิชุมชน 5 กก.", stockQty = 30, price = 180.0)
    )

    private fun isPlaceholderUrl(): Boolean {
        return supabaseClient.supabaseUrl.contains("YOUR_PROJECT_ID", ignoreCase = true)
    }

    override suspend fun addProduct(product: Product): Result<Unit> =
        withContext(Dispatchers.IO) {
            if (isPlaceholderUrl()) {
                val newId = (mockProducts.maxOfOrNull { it.id ?: 0L } ?: 0L) + 1L
                mockProducts.add(0, product.copy(id = newId))
                return@withContext Result.success(Unit)
            }

            try {
                supabaseClient.from(tableName).insert(product)
                Result.success(Unit)
            } catch (e: Exception) {
                Result.failure(Exception("ไม่สามารถบันทึกไปยัง Supabase: ${e.localizedMessage}"))
            }
        }

    override suspend fun getProducts(): Result<List<Product>> =
        withContext(Dispatchers.IO) {
            if (isPlaceholderUrl()) {
                return@withContext Result.success(mockProducts.toList())
            }

            try {
                val products = supabaseClient.from(tableName)
                    .select {
                        order("id", order = Order.DESCENDING)
                    }
                    .decodeList<Product>()
                Result.success(products)
            } catch (e: Exception) {
                Result.failure(Exception("ไม่สามารถดึงข้อมูลจาก Supabase: ${e.localizedMessage}"))
            }
        }

    override suspend fun updateStock(id: Long, newStock: Int): Result<Unit> =
        withContext(Dispatchers.IO) {
            if (isPlaceholderUrl()) {
                val index = mockProducts.indexOfFirst { it.id == id }
                if (index != -1) {
                    mockProducts[index] = mockProducts[index].copy(stockQty = newStock)
                }
                return@withContext Result.success(Unit)
            }

            try {
                supabaseClient.from(tableName).update(
                    {
                        set("stock_qty", newStock)
                    }
                ) {
                    filter {
                        eq("id", id)
                    }
                }
                Result.success(Unit)
            } catch (e: Exception) {
                Result.failure(e)
            }
        }

    override suspend fun deleteProduct(id: Long): Result<Unit> =
        withContext(Dispatchers.IO) {
            if (isPlaceholderUrl()) {
                mockProducts.removeAll { it.id == id }
                return@withContext Result.success(Unit)
            }

            try {
                supabaseClient.from(tableName).delete {
                    filter {
                        eq("id", id)
                    }
                }
                Result.success(Unit)
            } catch (e: Exception) {
                Result.failure(e)
            }
        }
}
