package com.example.combinedsolutions.feature.stock.ui.screen

import android.content.res.Configuration
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.combinedsolutions.feature.stock.data.model.Product
import com.example.combinedsolutions.feature.stock.ui.viewmodel.ProductStockUiState
import java.text.DecimalFormat

@Composable
fun ProductStockContent(
    uiState: ProductStockUiState,
    snackbarHostState: SnackbarHostState,
    onNameChange: (String) -> Unit,
    onStockChange: (String) -> Unit,
    onPriceChange: (String) -> Unit,
    onAddClick: () -> Unit,
    onStockIncrement: (Product) -> Unit,
    onStockDecrement: (Product) -> Unit,
    onDeleteClick: (Product) -> Unit,
    onRefreshClick: () -> Unit
) {
    val decimalFormat = DecimalFormat("#,##0.00")
    val configuration = LocalConfiguration.current
    val isLandscape = configuration.orientation == Configuration.ORIENTATION_LANDSCAPE

    Scaffold(
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "ระบบจัดการสต็อกสินค้าชุมชน",
                    style = MaterialTheme.typography.titleLarge.copy(
                        fontWeight = FontWeight.Bold,
                        fontSize = 20.sp
                    ),
                    color = MaterialTheme.colorScheme.primary
                )

                IconButton(onClick = onRefreshClick, enabled = !uiState.isLoading) {
                    Icon(
                        imageVector = Icons.Default.Refresh,
                        contentDescription = "รีเฟรช",
                        tint = MaterialTheme.colorScheme.primary
                    )
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            if (isLandscape) {
                // Landscape Mode: Layout แบ่งเป็น 2 คอลัมน์ (ซ้าย: ฟอร์มเพิ่มสินค้า, ขวา: รายการสต็อก)
                Row(
                    modifier = Modifier.fillMaxSize(),
                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    Column(
                        modifier = Modifier
                            .weight(1f)
                            .verticalScroll(rememberScrollState())
                    ) {
                        ProductInputCard(
                            uiState = uiState,
                            onNameChange = onNameChange,
                            onStockChange = onStockChange,
                            onPriceChange = onPriceChange,
                            onAddClick = onAddClick
                        )
                    }

                    Column(
                        modifier = Modifier.weight(1f)
                    ) {
                        Text(
                            text = "รายการสินค้าในสต็อก (${uiState.products.size} รายการ):",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.padding(bottom = 8.dp)
                        )
                        ProductStockList(
                            uiState = uiState,
                            decimalFormat = decimalFormat,
                            onStockIncrement = onStockIncrement,
                            onStockDecrement = onStockDecrement,
                            onDeleteClick = onDeleteClick,
                            modifier = Modifier.weight(1f)
                        )
                    }
                }
            } else {
                // Portrait Mode: Layout แนวตั้ง
                ProductInputCard(
                    uiState = uiState,
                    onNameChange = onNameChange,
                    onStockChange = onStockChange,
                    onPriceChange = onPriceChange,
                    onAddClick = onAddClick
                )

                Spacer(modifier = Modifier.height(12.dp))

                Text(
                    text = "รายการสินค้าในสต็อก (${uiState.products.size} รายการ):",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(bottom = 8.dp)
                )

                ProductStockList(
                    uiState = uiState,
                    decimalFormat = decimalFormat,
                    onStockIncrement = onStockIncrement,
                    onStockDecrement = onStockDecrement,
                    onDeleteClick = onDeleteClick,
                    modifier = Modifier.weight(1f)
                )
            }
        }
    }
}

@Composable
fun ProductInputCard(
    uiState: ProductStockUiState,
    onNameChange: (String) -> Unit,
    onStockChange: (String) -> Unit,
    onPriceChange: (String) -> Unit,
    onAddClick: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(modifier = Modifier.padding(12.dp)) {
            Text(
                text = "เพิ่มสินค้าใหม่ลง Cloud",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(6.dp))

            OutlinedTextField(
                value = uiState.nameInput,
                onValueChange = onNameChange,
                label = { Text("ชื่อสินค้า") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )

            Spacer(modifier = Modifier.height(6.dp))

            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                OutlinedTextField(
                    value = uiState.stockInput,
                    onValueChange = onStockChange,
                    label = { Text("จำนวนคงเหลือ (ชิ้น)") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    modifier = Modifier.weight(1f),
                    singleLine = true
                )

                OutlinedTextField(
                    value = uiState.priceInput,
                    onValueChange = onPriceChange,
                    label = { Text("ราคา (บาท)") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                    modifier = Modifier.weight(1f),
                    singleLine = true
                )
            }

            Spacer(modifier = Modifier.height(10.dp))

            Button(
                onClick = onAddClick,
                modifier = Modifier.fillMaxWidth(),
                enabled = !uiState.isLoading
            ) {
                Icon(imageVector = Icons.Default.Add, contentDescription = null)
                Spacer(modifier = Modifier.width(4.dp))
                Text("บันทึกขึ้น Supabase")
            }
        }
    }
}

@Composable
fun ProductStockList(
    uiState: ProductStockUiState,
    decimalFormat: DecimalFormat,
    onStockIncrement: (Product) -> Unit,
    onStockDecrement: (Product) -> Unit,
    onDeleteClick: (Product) -> Unit,
    modifier: Modifier = Modifier
) {
    if (uiState.isLoading && uiState.products.isEmpty()) {
        Box(
            modifier = modifier.fillMaxWidth(),
            contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator()
        }
    } else if (uiState.products.isEmpty()) {
        Box(
            modifier = modifier.fillMaxWidth(),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "ไม่มีรายการสินค้าในตาราง",
                color = Color.Gray,
                style = MaterialTheme.typography.bodyLarge
            )
        }
    } else {
        LazyColumn(
            modifier = modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            items(uiState.products, key = { it.id ?: 0L }) { product ->
                ProductStockCard(
                    product = product,
                    decimalFormat = decimalFormat,
                    onIncrement = { onStockIncrement(product) },
                    onDecrement = { onStockDecrement(product) },
                    onDelete = { onDeleteClick(product) },
                    enabled = !uiState.isLoading
                )
            }
        }
    }
}

@Composable
fun ProductStockCard(
    product: Product,
    decimalFormat: DecimalFormat,
    onIncrement: () -> Unit,
    onDecrement: () -> Unit,
    onDelete: () -> Unit,
    enabled: Boolean
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(8.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(10.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = product.productName,
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "ราคา: ฿ ${decimalFormat.format(product.price)}",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.primary
                )
                Text(
                    text = "คงเหลือ: ${product.stockQty} ชิ้น",
                    style = MaterialTheme.typography.bodySmall,
                    color = if (product.stockQty > 0) Color(0xFF2E7D32) else Color(0xFFC62828)
                )
            }

            Row(verticalAlignment = Alignment.CenterVertically) {
                OutlinedButton(
                    onClick = onDecrement,
                    enabled = enabled && product.stockQty > 0,
                    shape = RoundedCornerShape(8.dp),
                    contentPadding = ButtonDefaults.ContentPadding,
                    modifier = Modifier.size(36.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Remove,
                        contentDescription = "ลดสต็อก",
                        modifier = Modifier.size(16.dp)
                    )
                }

                Spacer(modifier = Modifier.width(4.dp))

                OutlinedButton(
                    onClick = onIncrement,
                    enabled = enabled,
                    shape = RoundedCornerShape(8.dp),
                    contentPadding = ButtonDefaults.ContentPadding,
                    modifier = Modifier.size(36.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Add,
                        contentDescription = "เพิ่มสต็อก",
                        modifier = Modifier.size(16.dp)
                    )
                }

                Spacer(modifier = Modifier.width(4.dp))

                IconButton(
                    onClick = onDelete,
                    enabled = enabled,
                    modifier = Modifier.size(36.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Delete,
                        contentDescription = "ลบสินค้า",
                        tint = MaterialTheme.colorScheme.error,
                        modifier = Modifier.size(20.dp)
                    )
                }
            }
        }
    }
}
