package com.example.combinedsolutions.feature.expense.ui.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.combinedsolutions.feature.expense.data.model.Transaction
import com.example.combinedsolutions.feature.expense.data.model.TransactionType
import com.example.combinedsolutions.feature.expense.ui.viewmodel.TransactionUiState
import java.text.DecimalFormat

@Composable
fun TransactionContent(
    uiState: TransactionUiState,
    snackbarHostState: SnackbarHostState,
    onTitleChange: (String) -> Unit,
    onAmountChange: (String) -> Unit,
    onTypeChange: (TransactionType) -> Unit,
    onSaveClick: () -> Unit,
    onClearClick: () -> Unit
) {
    val decimalFormat = DecimalFormat("#,##0.00")

    Scaffold(
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(16.dp)
        ) {
            Text(
                text = "ระบบบันทึกรายรับ-รายจ่าย (.CSV)",
                style = MaterialTheme.typography.titleLarge.copy(
                    fontWeight = FontWeight.Bold,
                    fontSize = 22.sp
                ),
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier.padding(bottom = 12.dp)
            )

            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(
                    containerColor = Color(0xFFE8F5E9)
                ),
                shape = RoundedCornerShape(12.dp),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
            ) {
                Column(
                    modifier = Modifier.padding(16.dp)
                ) {
                    Text(
                        text = "ยอดคงเหลือสุทธิ:",
                        style = MaterialTheme.typography.bodyMedium,
                        color = Color(0xFF2E7D32)
                    )
                    Text(
                        text = "฿ ${decimalFormat.format(uiState.netBalance)}",
                        style = MaterialTheme.typography.headlineMedium.copy(
                            fontWeight = FontWeight.Bold
                        ),
                        color = if (uiState.netBalance >= 0) Color(0xFF1B5E20) else Color(0xFFC62828)
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            OutlinedTextField(
                value = uiState.titleInput,
                onValueChange = onTitleChange,
                label = { Text("ชื่อรายการ (เช่น ค่าอาหาร, เงินเดือน)") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )

            Spacer(modifier = Modifier.height(8.dp))

            OutlinedTextField(
                value = uiState.amountInput,
                onValueChange = onAmountChange,
                label = { Text("จำนวนเงิน (บาท)") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )

            Spacer(modifier = Modifier.height(8.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .clickable { onTypeChange(TransactionType.EXPENSE) }
                        .padding(end = 16.dp)
                ) {
                    RadioButton(
                        selected = uiState.selectedType == TransactionType.EXPENSE,
                        onClick = { onTypeChange(TransactionType.EXPENSE) }
                    )
                    Text(text = "รายจ่าย", modifier = Modifier.padding(start = 4.dp))
                }

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.clickable { onTypeChange(TransactionType.INCOME) }
                ) {
                    RadioButton(
                        selected = uiState.selectedType == TransactionType.INCOME,
                        onClick = { onTypeChange(TransactionType.INCOME) }
                    )
                    Text(text = "รายรับ", modifier = Modifier.padding(start = 4.dp))
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Button(
                    onClick = onSaveClick,
                    modifier = Modifier.weight(1f),
                    enabled = !uiState.isLoading
                ) {
                    Text("บันทึกข้อมูล")
                }

                OutlinedButton(
                    onClick = onClearClick,
                    modifier = Modifier.weight(1f),
                    enabled = !uiState.isLoading,
                    colors = ButtonDefaults.outlinedButtonColors(contentColor = MaterialTheme.colorScheme.error)
                ) {
                    Text("ล้างข้อมูล")
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "รายการ (จากไฟล์ CSV):",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(bottom = 8.dp)
            )

            if (uiState.isLoading) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            } else if (uiState.transactions.isEmpty()) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "ยังไม่มีข้อมูลรายการ",
                        color = Color.Gray,
                        style = MaterialTheme.typography.bodyLarge
                    )
                }
            } else {
                LazyColumn(
                    modifier = Modifier.weight(1f),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(uiState.transactions) { item ->
                        TransactionItemCard(transaction = item, decimalFormat = decimalFormat)
                    }
                }
            }
        }
    }
}

@Composable
fun TransactionItemCard(
    transaction: Transaction,
    decimalFormat: DecimalFormat
) {
    val isIncome = transaction.type == TransactionType.INCOME
    val amountColor = if (isIncome) Color(0xFF2E7D32) else Color(0xFFC62828)
    val prefix = if (isIncome) "+ " else "- "

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(8.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = transaction.title,
                    style = MaterialTheme.typography.bodyLarge,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "${transaction.date} ${transaction.time}",
                    style = MaterialTheme.typography.bodySmall,
                    color = Color.Gray
                )
            }

            Text(
                text = "$prefix฿ ${decimalFormat.format(transaction.amount)}",
                style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Bold),
                color = amountColor
            )
        }
    }
}
