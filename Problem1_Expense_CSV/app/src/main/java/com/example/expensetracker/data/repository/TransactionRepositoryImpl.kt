package com.example.expensetracker.data.repository

import android.content.Context
import com.example.expensetracker.data.model.Transaction
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File
import java.io.FileOutputStream

/**
 * Implementation ของ TransactionRepository
 * จัดการไฟล์ CSV ในพื้นที่จัดเก็บภายในเครื่อง (Internal Storage: context.filesDir)
 * ทำงานบน Dispatchers.IO เพื่อความปลอดภัยของ Thread และไม่ทำให้ UI ค้าง
 */
class TransactionRepositoryImpl(
    private val context: Context
) : TransactionRepository {

    private val fileName = "transactions.csv"

    private fun getCsvFile(): File {
        return File(context.filesDir, fileName)
    }

    /**
     * การบันทึก (Append): ต่อท้ายลงในไฟล์ .csv
     */
    override suspend fun saveTransaction(transaction: Transaction): Result<Unit> =
        withContext(Dispatchers.IO) {
            try {
                val file = getCsvFile()
                // เปิด FileOutputStream แบบ append = true
                FileOutputStream(file, true).bufferedWriter().use { writer ->
                    writer.write(transaction.toCsvRow())
                }
                Result.success(Unit)
            } catch (e: Exception) {
                Result.failure(e)
            }
        }

    /**
     * การอ่าน (Read & Parse): อ่านไฟล์ .csv บรรทัดต่อบรรทัด แปลงเป็น List<Transaction>
     */
    override suspend fun getAllTransactions(): Result<List<Transaction>> =
        withContext(Dispatchers.IO) {
            try {
                val file = getCsvFile()
                if (!file.exists()) {
                    return@withContext Result.success(emptyList())
                }

                val list = mutableListOf<Transaction>()
                file.bufferedReader().useLines { lines ->
                    lines.forEach { line ->
                        if (line.isNotBlank()) {
                            Transaction.fromCsvRow(line)?.let { transaction ->
                                list.add(transaction)
                            }
                        }
                    }
                }
                // เรียงลำดับจากรายการล่าสุดไว้ด้านบนเพื่อให้ดูง่าย
                Result.success(list.reversed())
            } catch (e: Exception) {
                Result.failure(e)
            }
        }

    /**
     * การลบ (Delete): ลบไฟล์ .csv ออกจากระบบ
     */
    override suspend fun clearAllTransactions(): Result<Boolean> =
        withContext(Dispatchers.IO) {
            try {
                val file = getCsvFile()
                val deleted = if (file.exists()) file.delete() else true
                Result.success(deleted)
            } catch (e: Exception) {
                Result.failure(e)
            }
        }
}
