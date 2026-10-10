package com.example.expensetracker.ui.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.expensetracker.data.model.Transaction
import com.example.expensetracker.data.model.TransactionType
import com.example.expensetracker.ui.viewmodel.TransactionUiState
import java.text.NumberFormat
import java.util.Locale

/**
 * Stateless Composable: รับ State และ Event Callbacks ทั้งหมดตามหลัก State Hoisting
 * ไม่ขึ้นกับ ViewModel โดยตรง ทำให้ทดสอบและ Preview ได้ง่าย
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TransactionContent(
    state: TransactionUiState,
    onTitleChange: (String) -> Unit,
    onAmountChange: (String) -> Unit,
    onTypeChange: (TransactionType) -> Unit,
    onSaveClick: () -> Unit,
    onClearClick: () -> Unit,
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
                        text = "ระบบบันทึกรายรับ-รายจ่าย (.CSV)",
                        fontWeight = FontWeight.Bold,
                        fontSize = 19.sp
                    )
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surface
                )
            )
        },
        modifier = modifier
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(horizontal = 16.dp)
        ) {
            // 1. การ์ดยอดคงเหลือสุทธิ
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp),
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(
                    containerColor = Color(0xFFE8F5E9) // สีเขียวอ่อนสบายตา
                )
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                ) {
                    Text(
                        text = "ยอดคงเหลือสุทธิ:",
                        fontSize = 14.sp,
                        color = Color(0xFF2E7D32)
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    val balancePrefix = if (state.netBalance >= 0) "฿ " else "-฿ "
                    val absBalance = kotlin.math.abs(state.netBalance)
                    Text(
                        text = "$balancePrefix${currencyFormat.format(absBalance)}",
                        fontSize = 26.sp,
                        fontWeight = FontWeight.Bold,
                        color = if (state.netBalance >= 0) Color(0xFF2E7D32) else Color(0xFFC62828)
                    )
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            // 2. ช่องกรอกชื่อรายการ
            OutlinedTextField(
                value = state.titleInput,
                onValueChange = onTitleChange,
                label = { Text("ชื่อรายการ (เช่น ค่าอาหาร, เงินเดือน)") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                shape = RoundedCornerShape(8.dp)
            )

            Spacer(modifier = Modifier.height(8.dp))

            // 3. ช่องกรอกจำนวนเงิน
            OutlinedTextField(
                value = state.amountInput,
                onValueChange = onAmountChange,
                label = { Text("จำนวนเงิน (บาท)") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                shape = RoundedCornerShape(8.dp)
            )

            Spacer(modifier = Modifier.height(8.dp))

            // 4. RadioButton เลือกประเภท (รายจ่าย / รายรับ)
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // RadioButton รายจ่าย
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .selectable(
                            selected = (state.selectedType == TransactionType.EXPENSE),
                            onClick = { onTypeChange(TransactionType.EXPENSE) },
                            role = Role.RadioButton
                        )
                        .padding(horizontal = 12.dp, vertical = 4.dp)
                ) {
                    RadioButton(
                        selected = (state.selectedType == TransactionType.EXPENSE),
                        onClick = null
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(text = "รายจ่าย", fontSize = 15.sp)
                }

                Spacer(modifier = Modifier.width(20.dp))

                // RadioButton รายรับ
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .selectable(
                            selected = (state.selectedType == TransactionType.INCOME),
                            onClick = { onTypeChange(TransactionType.INCOME) },
                            role = Role.RadioButton
                        )
                        .padding(horizontal = 12.dp, vertical = 4.dp)
                ) {
                    RadioButton(
                        selected = (state.selectedType == TransactionType.INCOME),
                        onClick = null
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(text = "รายรับ", fontSize = 15.sp)
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            // 5. ปุ่มบันทึกข้อมูล และ ปุ่มล้างข้อมูล
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Button(
                    onClick = onSaveClick,
                    modifier = Modifier.weight(1f),
                    shape = RoundedCornerShape(8.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF3F51B5))
                ) {
                    Text("บันทึกข้อมูล", color = Color.White)
                }

                OutlinedButton(
                    onClick = onClearClick,
                    modifier = Modifier.weight(1f),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Text("ล้างข้อมูล")
                }
            }

            // แสดงข้อความสถานะ (ถ้ามี)
            state.statusMessage?.let { msg ->
                Spacer(modifier = Modifier.height(6.dp))
                Text(
                    text = msg,
                    color = if (msg.contains("สำเร็จ") || msg.contains("เรียบร้อย")) Color(0xFF2E7D32) else Color(0xFFC62828),
                    fontSize = 13.sp,
                    modifier = Modifier.align(Alignment.CenterHorizontally)
                )
            }

            Spacer(modifier = Modifier.height(14.dp))

            // 6. ส่วนหัวข้อรายการ
            Text(
                text = "รายการ (จากไฟล์ CSV):",
                fontWeight = FontWeight.Bold,
                fontSize = 15.sp,
                color = MaterialTheme.colorScheme.onSurface
            )

            Spacer(modifier = Modifier.height(6.dp))

            // 7. ประวัติย้อนหลังผ่าน LazyColumn
            if (state.transactions.isEmpty()) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "ยังไม่มีข้อมูลรายการ",
                        color = Color.Gray,
                        fontSize = 14.sp
                    )
                }
            } else {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(state.transactions) { transaction ->
                        TransactionRowItem(transaction = transaction, currencyFormat = currencyFormat)
                    }
                }
            }
        }
    }
}

/**
 * แถวแสดงรายการแต่ละไอเท็มใน LazyColumn
 */
@Composable
fun TransactionRowItem(
    transaction: Transaction,
    currencyFormat: NumberFormat,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(8.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color(0xFFF9F9F9)
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 14.dp, vertical = 10.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = transaction.title,
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp,
                    color = Color(0xFF212121)
                )
                Spacer(modifier = Modifier.height(2.dp))
                Text(
                    text = "${transaction.date} ${transaction.time}",
                    fontSize = 12.sp,
                    color = Color.Gray
                )
            }

            val isIncome = transaction.type == TransactionType.INCOME
            val sign = if (isIncome) "+ ฿ " else "- ฿ "
            val amountColor = if (isIncome) Color(0xFF2E7D32) else Color(0xFFC62828)

            Text(
                text = "$sign${currencyFormat.format(transaction.amount)}",
                fontWeight = FontWeight.Bold,
                fontSize = 16.sp,
                color = amountColor
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun TransactionContentPreview() {
    MaterialTheme {
        TransactionContent(
            state = TransactionUiState(
                titleInput = "เงินเดือน",
                amountInput = "25000",
                selectedType = TransactionType.INCOME,
                netBalance = 20500.0,
                transactions = listOf(
                    Transaction("2023-10-10", "09:00:00", "เงินเดือน", 25000.0, TransactionType.INCOME),
                    Transaction("2023-10-10", "12:30:00", "ค่าอาหารกลางวัน", 150.0, TransactionType.EXPENSE),
                    Transaction("2023-10-10", "18:00:00", "ค่าเดินทาง", 4350.0, TransactionType.EXPENSE)
                )
            ),
            onTitleChange = {},
            onAmountChange = {},
            onTypeChange = {},
            onSaveClick = {},
            onClearClick = {}
        )
    }
}

