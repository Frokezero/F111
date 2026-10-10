package com.example.communitystock.data.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Data Class สำหรับข้อมูลสินค้าชุมชน
 * มีแอนโนเทชัน @Serializable และ @SerialName สำหรับแปลง JSON อัตโนมัติร่วมกับ Supabase
 */
@Serializable
data class Product(
    @SerialName("id")
    val id: Long? = null,

    @SerialName("product_name")
    val productName: String,

    @SerialName("stock_qty")
    val stockQty: Int,

    @SerialName("price")
    val price: Double
)
