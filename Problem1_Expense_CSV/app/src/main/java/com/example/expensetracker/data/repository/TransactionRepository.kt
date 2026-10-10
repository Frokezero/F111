package com.example.expensetracker.data.repository

import com.example.expensetracker.data.model.Transaction

/**
 * Interface Repository ตามหลัก Repository Pattern
 * ตัดขาดการจัดการไฟล์ I/O ออกจาก UI และ ViewModel
 */
interface TransactionRepository {
    suspend fun saveTransaction(transaction: Transaction): Result<Unit>
    suspend fun getAllTransactions(): Result<List<Transaction>>
    suspend fun clearAllTransactions(): Result<Boolean>
}
