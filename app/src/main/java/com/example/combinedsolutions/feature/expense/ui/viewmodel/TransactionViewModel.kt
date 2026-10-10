package com.example.combinedsolutions.feature.expense.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.combinedsolutions.feature.expense.data.model.Transaction
import com.example.combinedsolutions.feature.expense.data.model.TransactionType
import com.example.combinedsolutions.feature.expense.data.repository.TransactionRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class TransactionUiState(
    val titleInput: String = "",
    val amountInput: String = "",
    val selectedType: TransactionType = TransactionType.EXPENSE,
    val transactions: List<Transaction> = emptyList(),
    val netBalance: Double = 0.0,
    val isLoading: Boolean = false,
    val userMessage: String? = null
)

class TransactionViewModel(
    private val repository: TransactionRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(TransactionUiState())
    val uiState: StateFlow<TransactionUiState> = _uiState.asStateFlow()

    init {
        loadTransactions()
    }

    fun onTitleChange(newTitle: String) {
        _uiState.update { it.copy(titleInput = newTitle) }
    }

    fun onAmountChange(newAmount: String) {
        if (newAmount.isEmpty() || newAmount.toDoubleOrNull() != null) {
            _uiState.update { it.copy(amountInput = newAmount) }
        }
    }

    fun onTypeChange(newType: TransactionType) {
        _uiState.update { it.copy(selectedType = newType) }
    }

    fun saveTransaction() {
        val current = _uiState.value
        val title = current.titleInput.trim()
        val amount = current.amountInput.toDoubleOrNull()

        if (title.isBlank()) {
            _uiState.update { it.copy(userMessage = "กรุณากรอกชื่อรายการ") }
            return
        }
        if (amount == null || amount <= 0) {
            _uiState.update { it.copy(userMessage = "กรุณากรอกจำนวนเงินให้ถูกต้อง (> 0)") }
            return
        }

        val (dateStr, timeStr) = Transaction.currentDateTime()
        val transaction = Transaction(
            date = dateStr,
            time = timeStr,
            title = title,
            amount = amount,
            type = current.selectedType
        )

        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            repository.saveTransaction(transaction)
                .onSuccess {
                    _uiState.update {
                        it.copy(
                            titleInput = "",
                            amountInput = "",
                            userMessage = "บันทึกข้อมูลเรียบร้อยแล้ว"
                        )
                    }
                    loadTransactionsInternal()
                }
                .onFailure { error ->
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            userMessage = "บันทึกข้อมูลล้มเหลว: ${error.localizedMessage}"
                        )
                    }
                }
        }
    }

    fun clearAllData() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            repository.clearAllTransactions()
                .onSuccess {
                    _uiState.update {
                        it.copy(
                            titleInput = "",
                            amountInput = "",
                            transactions = emptyList(),
                            netBalance = 0.0,
                            isLoading = false,
                            userMessage = "ล้างข้อมูลทั้งหมดแล้ว"
                        )
                    }
                }
                .onFailure { error ->
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            userMessage = "ล้างข้อมูลล้มเหลว: ${error.localizedMessage}"
                        )
                    }
                }
        }
    }

    fun loadTransactions() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            loadTransactionsInternal()
        }
    }

    private suspend fun loadTransactionsInternal() {
        repository.getAllTransactions()
            .onSuccess { list ->
                val income = list.filter { it.type == TransactionType.INCOME }.sumOf { it.amount }
                val expense = list.filter { it.type == TransactionType.EXPENSE }.sumOf { it.amount }
                val net = income - expense

                _uiState.update {
                    it.copy(
                        transactions = list,
                        netBalance = net,
                        isLoading = false
                    )
                }
            }
            .onFailure { error ->
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        userMessage = "โหลดข้อมูลล้มเหลว: ${error.localizedMessage}"
                    )
                }
            }
    }

    fun userMessageShown() {
        _uiState.update { it.copy(userMessage = null) }
    }

    class Factory(private val repository: TransactionRepository) : ViewModelProvider.Factory {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return TransactionViewModel(repository) as T
        }
    }
}
