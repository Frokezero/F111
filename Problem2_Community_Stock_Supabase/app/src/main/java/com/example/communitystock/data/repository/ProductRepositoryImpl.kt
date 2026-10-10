package com.example.communitystock.data.repository

import com.example.communitystock.data.model.Product
import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.postgrest.from
import io.github.jan.supabase.postgrest.query.Order
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

/**
 * Implementation ของ ProductRepository
 * ยิงคำสั่ง Network ไปยัง Supabase Postgrest บน Dispatchers.IO ทั้งหมด
 */
class ProductRepositoryImpl(
    private val supabaseClient: SupabaseClient
) : ProductRepository {

    private val tableName = "products"

    /**
     * R - Read: ดึงข้อมูลสินค้าทั้งหมดจากตาราง products เรียงลำดับจากรายการล่าสุด (id DESC)
     */
    override suspend fun getProducts(): Result<List<Product>> =
        withContext(Dispatchers.IO) {
            try {
                val products = supabaseClient.from(tableName)
                    .select {
                        order("id", order = Order.DESCENDING)
                    }
                    .decodeList<Product>()
                Result.success(products)
            } catch (e: Exception) {
                Result.failure(e)
            }
        }

    /**
     * C - Create: ยิงคำสั่ง insert เพื่อบันทึกแถวใหม่ขึ้นฐานข้อมูลบน Supabase
     */
    override suspend fun addProduct(product: Product): Result<Unit> =
        withContext(Dispatchers.IO) {
            try {
                supabaseClient.from(tableName).insert(product)
                Result.success(Unit)
            } catch (e: Exception) {
                Result.failure(e)
            }
        }

    /**
     * U - Update: ส่งคำสั่ง update ไปแก้ไขค่า stock_qty ในฐานข้อมูลตาม ID ทันที
     */
    override suspend fun updateStock(id: Long, newStock: Int): Result<Unit> =
        withContext(Dispatchers.IO) {
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

    /**
     * D - Delete: ส่งคำสั่ง delete ลบแถวข้อมูลนั้นออกจาก Supabase ตาม ID ที่ระบุ
     */
    override suspend fun deleteProduct(id: Long): Result<Unit> =
        withContext(Dispatchers.IO) {
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
