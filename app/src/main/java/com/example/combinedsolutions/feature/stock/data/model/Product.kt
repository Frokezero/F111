package com.example.combinedsolutions.feature.stock.data.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Data Class สำหรับเก็บข้อมูลสินค้า
 * ตรงกับตาราง products ใน Supabase Database
 */
@Serializable
data class Product(
    val id: Long? = null,

    @SerialName("product_name")
    val productName: String,

    @SerialName("stock_qty")
    val stockQty: Int,

    val price: Double
)
