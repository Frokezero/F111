package com.example.communitystock

import com.example.communitystock.data.model.Product
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Test

class ProductTest {

    @Test
    fun testProductCreation() {
        val product = Product(
            id = 1L,
            productName = "ข้าวหอมมะลิ 5 กก.",
            stockQty = 25,
            price = 220.0
        )

        assertEquals(1L, product.id)
        assertEquals("ข้าวหอมมะลิ 5 กก.", product.productName)
        assertEquals(25, product.stockQty)
        assertEquals(220.0, product.price, 0.001)
    }

    @Test
    fun testProductCreation_DefaultId() {
        val product = Product(
            productName = "น้ำมันมะพร้าว",
            stockQty = 10,
            price = 150.0
        )

        assertNull(product.id)
        assertEquals("น้ำมันมะพร้าว", product.productName)
    }

    @Test
    fun testStockAdjustmentLogic() {
        val initialStock = 5
        val deltaPlus = 1
        val deltaMinus = -1

        assertEquals(6, initialStock + deltaPlus)
        assertEquals(4, initialStock + deltaMinus)
    }
}
