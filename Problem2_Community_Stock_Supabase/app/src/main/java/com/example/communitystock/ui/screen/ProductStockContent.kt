package com.example.communitystock.ui.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.communitystock.data.model.Product
import com.example.communitystock.ui.viewmodel.ProductUiState
import java.text.NumberFormat
import java.util.Locale

/**
 * Stateless Composable สำหรับหน้าจอจัดการสต็อกสินค้าชุมชน
 * แยกส่วนประกอบตามหลัก State Hoisting เพื่อความง่ายในการ Preview และทดสอบ
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProductStockContent(
    state: ProductUiState,
    onNameChange: (String) -> Unit,
    onStockChange: (String) -> Unit,
    onPriceChange: (String) -> Unit,
    onAddProduct: () -> Unit,
    onAdjustStock: (Product, Int) -> Unit,
    onDeleteProduct: (Product) -> Unit,
    onRefresh: () -> Unit,
    onDismissMessage: () -> Unit,
    modifier: Modifier = Modifier
) {
    val currencyFormat = NumberFormat.getNumberInstance(Locale.US).apply {
        minimumFractionDigits = 2
        maximumFractionDigits = 2
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "ระบบจัดการสต็อกสินค้าชุมชน",
                        fontWeight = FontWeight.Bold,
                        fontSize = 19.sp
                    )
                },
                actions = {
                    IconButton(onClick = onRefresh) {
                        Icon(imageVector = Icons.Default.Refresh, contentDescription = "รีเฟรช")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.onPrimaryContainer
                )
            )
        },
        snackbarHost = {
            state.message?.let { msg ->
                Snackbar(
                    modifier = Modifier.padding(16.dp),
                    action = {
                        TextButton(onClick = onDismissMessage) {
                            Text("ปิด", color = Color.White)
                        }
                    }
                ) {
                    Text(msg)
                }
            }
        },
        modifier = modifier
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(horizontal = 16.dp)
        ) {
            // ส่วนที่ 1: ฟอร์มเพิ่มสินค้าใหม่ (Create)
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 10.dp),
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surfaceVariant
                ),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(14.dp)
                ) {
                    Text(
                        text = "เพิ่มสินค้าใหม่ลง Cloud",
                        fontWeight = FontWeight.Bold,
                        fontSize = 16.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    OutlinedTextField(
                        value = state.nameInput,
                        onValueChange = onNameChange,
                        label = { Text("ชื่อสินค้า") },
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        OutlinedTextField(
                            value = state.stockInput,
                            onValueChange = onStockChange,
                            label = { Text("จำนวนคงเหลือ (ชิ้น)") },
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                            modifier = Modifier.weight(1f),
                            singleLine = true
                        )

                        OutlinedTextField(
                            value = state.priceInput,
                            onValueChange = onPriceChange,
                            label = { Text("ราคา (บาท)") },
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                            modifier = Modifier.weight(1f),
                            singleLine = true
                        )
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    Button(
                        onClick = onAddProduct,
                        modifier = Modifier.fillMaxWidth(),
                        enabled = !state.isLoading,
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Icon(imageVector = Icons.Default.Add, contentDescription = null)
                        Spacer(modifier = Modifier.width(6.dp))
                        Text("บันทึกขึ้น Supabase")
                    }
                }
            }

            // ส่วนแสดงสถานะโหลด
            if (state.isLoading) {
                LinearProgressIndicator(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp)
                )
            }

            // ส่วนที่ 2: รายการสินค้า (Read / Update / Delete)
            Text(
                text = "รายการสินค้าในสต็อก (${state.products.size} รายการ):",
                fontWeight = FontWeight.Bold,
                fontSize = 16.sp,
                modifier = Modifier.padding(vertical = 6.dp)
            )

            if (state.products.isEmpty() && !state.isLoading) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "ไม่มีรายการสินค้าในตาราง",
                        color = Color.Gray,
                        fontSize = 14.sp
                    )
                }
            } else {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f),
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                    contentPadding = PaddingValues(bottom = 16.dp)
                ) {
                    items(
                        items = state.products,
                        key = { it.id ?: it.hashCode() }
                    ) { product ->
                        ProductCardItem(
                            product = product,
                            currencyFormat = currencyFormat,
                            onAdjustStock = onAdjustStock,
                            onDeleteProduct = onDeleteProduct
                        )
                    }
                }
            }
        }
    }
}

/**
 * การ์ดแสดงผลข้อมูลสินค้าแต่ละชิ้น พร้อมปุ่มปรับสต็อก (+1 / -1) และปุ่มลบ
 */
@Composable
fun ProductCardItem(
    product: Product,
    currencyFormat: NumberFormat,
    onAdjustStock: (Product, Int) -> Unit,
    onDeleteProduct: (Product) -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(10.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.White
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(14.dp)
        ) {
            // แถวหัวข้อ: ชื่อสินค้า และ ราคา
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = product.productName,
                    fontWeight = FontWeight.Bold,
                    fontSize = 17.sp,
                    color = Color(0xFF1E1E1E),
                    modifier = Modifier.weight(1f)
                )
                Text(
                    text = "฿${currencyFormat.format(product.price)}",
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp,
                    color = MaterialTheme.colorScheme.primary
                )
            }

            Spacer(modifier = Modifier.height(10.dp))

            // แถวจัดการสต็อก: แสดงจำนวนคงเหลือ และปุ่ม -1 / +1 / ปุ่มลบ
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                // ตัวเลขสต็อก
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = "สต็อกคงเหลือ: ",
                        fontSize = 14.sp,
                        color = Color.DarkGray
                    )
                    Text(
                        text = "${product.stockQty} ชิ้น",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.ExtraBold,
                        color = if (product.stockQty > 0) Color(0xFF2E7D32) else Color(0xFFC62828)
                    )
                }

                // กลุ่มปุ่ม Action: ปรับลด (-1), ปรับเพิ่ม (+1), ลบ (Delete)
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    // ปุ่มลดสต็อก (-1)
                    OutlinedButton(
                        onClick = { onAdjustStock(product, -1) },
                        contentPadding = PaddingValues(horizontal = 10.dp, vertical = 2.dp),
                        shape = RoundedCornerShape(6.dp),
                        enabled = product.stockQty > 0
                    ) {
                        Text("-1", fontWeight = FontWeight.Bold)
                    }

                    // ปุ่มเพิ่มสต็อก (+1)
                    OutlinedButton(
                        onClick = { onAdjustStock(product, 1) },
                        contentPadding = PaddingValues(horizontal = 10.dp, vertical = 2.dp),
                        shape = RoundedCornerShape(6.dp)
                    ) {
                        Text("+1", fontWeight = FontWeight.Bold)
                    }

                    // ปุ่มลบรายการ
                    IconButton(
                        onClick = { onDeleteProduct(product) }
                    ) {
                        Icon(
                            imageVector = Icons.Default.Delete,
                            contentDescription = "ลบสินค้า",
                            tint = Color(0xFFD32F2F)
                        )
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ProductStockContentPreview() {
    MaterialTheme {
        ProductStockContent(
            state = ProductUiState(
                nameInput = "ข้าวหอมมะลิ 5 กก.",
                stockInput = "20",
                priceInput = "220",
                products = listOf(
                    Product(id = 1, productName = "ข้าวหอมมะลิ 5 กก.", stockQty = 20, price = 220.0),
                    Product(id = 2, productName = "น้ำมันมะพร้าวสกัดเย็น", stockQty = 5, price = 150.0),
                    Product(id = 3, productName = "ชาสมุนไพรใบหม่อน", stockQty = 0, price = 85.0)
                )
            ),
            onNameChange = {},
            onStockChange = {},
            onPriceChange = {},
            onAddProduct = {},
            onAdjustStock = { _, _ -> },
            onDeleteProduct = {},
            onRefresh = {},
            onDismissMessage = {}
        )
    }
}

