package com.example.combinedsolutions.feature.expense.data.repository

import com.example.combinedsolutions.feature.expense.data.model.Transaction

interface TransactionRepository {
    suspend fun saveTransaction(transaction: Transaction): Result<Unit>
    suspend fun getAllTransactions(): Result<List<Transaction>>
    suspend fun clearAllTransactions(): Result<Unit>
    suspend fun exportCsvToDownloads(): Result<String>
}
