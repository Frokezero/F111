package com.example.combinedsolutions.feature.stock.data.repository

import com.example.combinedsolutions.feature.stock.data.model.Product
import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.postgrest.postgrest
import io.github.jan.supabase.postgrest.query.Order
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class ProductRepositoryImpl(
    private val supabaseClient: SupabaseClient
) : ProductRepository {

    override suspend fun addProduct(product: Product): Result<Unit> =
        withContext(Dispatchers.IO) {
            try {
                supabaseClient.postgrest["products"].insert(product)
                Result.success(Unit)
            } catch (e: Exception) {
                Result.failure(e)
            }
        }

    override suspend fun getProducts(): Result<List<Product>> =
        withContext(Dispatchers.IO) {
            try {
                val products = supabaseClient.postgrest["products"]
                    .select {
                        order("id", order = Order.DESCENDING)
                    }
                    .decodeList<Product>()
                Result.success(products)
            } catch (e: Exception) {
                Result.failure(e)
            }
        }

    override suspend fun updateStock(id: Long, newStock: Int): Result<Unit> =
        withContext(Dispatchers.IO) {
            try {
                supabaseClient.postgrest["products"]
                    .update({
                        set("stock_qty", newStock)
                    }) {
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
            try {
                supabaseClient.postgrest["products"]
                    .delete {
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
