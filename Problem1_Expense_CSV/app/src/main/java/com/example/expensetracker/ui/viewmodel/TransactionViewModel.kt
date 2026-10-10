package com.example.expensetracker.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.expensetracker.data.model.Transaction
import com.example.expensetracker.data.model.TransactionType
import com.example.expensetracker.data.repository.TransactionRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

/**
 * UI State สำหรับหน้าบันทึกรายรับ-รายจ่าย
 * ถือครองข้อมูลช่องกรอก, ประวัติรายการ, ยอดสุทธิ และสถานะแจ้งเตือน
 */
data class TransactionUiState(
    val titleInput: String = "",
    val amountInput: String = "",
    val selectedType: TransactionType = TransactionType.EXPENSE,
    val transactions: List<Transaction> = emptyList(),
    val netBalance: Double = 0.0,
    val statusMessage: String? = null,
    val isLoading: Boolean = false
)

class TransactionViewModel(
    private val repository: TransactionRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(TransactionUiState())
    val uiState: StateFlow<TransactionUiState> = _uiState.asStateFlow()

    init {
        loadTransactions()
    }

    /**
     * เปลี่ยนค่าในช่องชื่อรายการ
     */
    fun onTitleChange(newTitle: String) {
        _uiState.update { it.copy(titleInput = newTitle, statusMessage = null) }
    }

    /**
     * เปลี่ยนค่าในช่องจำนวนเงิน
     */
    fun onAmountChange(newAmount: String) {
        // กรองรับเฉพาะตัวเลขและจุดทศนิยม
        if (newAmount.isEmpty() || newAmount.matches(Regex("^\\d*\\.?\\d*$"))) {
            _uiState.update { it.copy(amountInput = newAmount, statusMessage = null) }
        }
    }

    /**
     * สลับประเภทรายการ (รายรับ / รายจ่าย)
     */
    fun onTypeChange(type: TransactionType) {
        _uiState.update { it.copy(selectedType = type) }
    }

    /**
     * อ่านข้อมูลไฟล์ CSV และคำนวณยอดเงินคงเหลือสุทธิ
     */
    fun loadTransactions() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            val result = repository.getAllTransactions()
            result.onSuccess { list ->
                val balance = calculateNetBalance(list)
                _uiState.update {
                    it.copy(
                        transactions = list,
                        netBalance = balance,
                        isLoading = false
                    )
                }
            }.onFailure { error ->
                _uiState.update {
                    it.copy(
                        statusMessage = "เกิดข้อผิดพลาดในการอ่านไฟล์: ${error.localizedMessage}",
                        isLoading = false
                    )
                }
            }
        }
    }

    /**
     * บันทึกรายการใหม่ (Append ลงในไฟล์ CSV)
     */
    fun saveTransaction() {
        val currentState = _uiState.value
        val title = currentState.titleInput.trim()
        val amount = currentState.amountInput.toDoubleOrNull()

        if (title.isBlank()) {
            _uiState.update { it.copy(statusMessage = "กรุณาระบุชื่อรายการ") }
            return
        }

        if (amount == null || amount <= 0.0) {
            _uiState.update { it.copy(statusMessage = "กรุณาระบุจำนวนเงินให้ถูกต้อง") }
            return
        }

        val (currentDate, currentTime) = Transaction.currentDateTime()
        val newTransaction = Transaction(
            date = currentDate,
            time = currentTime,
            title = title,
            amount = amount,
            type = currentState.selectedType
        )

        viewModelScope.launch {
            val result = repository.saveTransaction(newTransaction)
            result.onSuccess {
                // เคลียร์ฟอร์มกรอก และโหลดข้อมูลใหม่จากไฟล์ CSV
                _uiState.update {
                    it.copy(
                        titleInput = "",
                        amountInput = "",
                        statusMessage = "บันทึกข้อมูลเรียบร้อยแล้ว"
                    )
                }
                loadTransactions()
            }.onFailure { error ->
                _uiState.update {
                    it.copy(statusMessage = "บันทึกข้อมูลล้มเหลว: ${error.localizedMessage}")
                }
            }
        }
    }

    /**
     * ล้างข้อมูล (ลบไฟล์ CSV และเคลียร์หน้าจอ)
     */
    fun clearData() {
        viewModelScope.launch {
            val result = repository.clearAllTransactions()
            result.onSuccess {
                _uiState.update {
                    it.copy(
                        titleInput = "",
                        amountInput = "",
                        transactions = emptyList(),
                        netBalance = 0.0,
                        statusMessage = "ล้างข้อมูลทั้งหมดเรียบร้อยแล้ว"
                    )
                }
            }.onFailure { error ->
                _uiState.update {
                    it.copy(statusMessage = "ลบไฟล์ล้มเหลว: ${error.localizedMessage}")
                }
            }
        }
    }

    /**
     * คำนวณยอดเงินคงเหลือสุทธิ (รายรับทั้งหมด หักลบด้วย รายจ่ายทั้งหมด)
     */
    private fun calculateNetBalance(list: List<Transaction>): Double {
        var totalIncome = 0.0
        var totalExpense = 0.0
        for (item in list) {
            when (item.type) {
                TransactionType.INCOME -> totalIncome += item.amount
                TransactionType.EXPENSE -> totalExpense += item.amount
            }
        }
        return totalIncome - totalExpense
    }

    /**
     * Factory สำหรับสร้าง TransactionViewModel พร้อมส่ง Dependency (Repository)
     */
    class Factory(private val repository: TransactionRepository) : ViewModelProvider.Factory {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(TransactionViewModel::class.java)) {
                return TransactionViewModel(repository) as T
            }
            throw IllegalArgumentException("Unknown ViewModel class")
        }
    }
}
