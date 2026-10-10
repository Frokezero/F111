package com.example.expensetracker.data.repository

import android.content.Context
import android.os.Environment
import com.example.expensetracker.data.model.Transaction
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File
import java.io.FileOutputStream

class TransactionRepositoryImpl(
    private val context: Context,
    private val fileName: String = "transactions.csv"
) : TransactionRepository {

    private val csvFile: File
        get() = File(context.filesDir, fileName)

    override suspend fun saveTransaction(transaction: Transaction): Result<Unit> =
        withContext(Dispatchers.IO) {
            try {
                FileOutputStream(csvFile, true).use { outputStream ->
                    outputStream.write(transaction.toCsvRow().toByteArray(Charsets.UTF_8))
                }
                Result.success(Unit)
            } catch (e: Exception) {
                Result.failure(e)
            }
        }

    override suspend fun getAllTransactions(): Result<List<Transaction>> =
        withContext(Dispatchers.IO) {
            try {
                if (!csvFile.exists()) {
                    return@withContext Result.success(emptyList())
                }

                val transactions = mutableListOf<Transaction>()
                csvFile.bufferedReader(Charsets.UTF_8).useLines { lines ->
                    lines.forEach { line ->
                        if (line.isNotBlank()) {
                            Transaction.fromCsvRow(line)?.let { transaction ->
                                transactions.add(transaction)
                            }
                        }
                    }
                }
                Result.success(transactions.reversed())
            } catch (e: Exception) {
                Result.failure(e)
            }
        }

    override suspend fun clearAllTransactions(): Result<Unit> =
        withContext(Dispatchers.IO) {
            try {
                if (csvFile.exists()) {
                    csvFile.delete()
                }
                Result.success(Unit)
            } catch (e: Exception) {
                Result.failure(e)
            }
        }

    override suspend fun exportCsvToDownloads(): Result<String> =
        withContext(Dispatchers.IO) {
            try {
                if (!csvFile.exists() || csvFile.length() == 0L) {
                    return@withContext Result.failure(Exception("ยังไม่มีข้อมูลในไฟล์ CSV สำหรับส่งออก"))
                }

                val downloadDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
                if (!downloadDir.exists()) {
                    downloadDir.mkdirs()
                }

                val targetFile = File(downloadDir, "transactions_exported.csv")
                csvFile.copyTo(targetFile, overwrite = true)
                Result.success(targetFile.absolutePath)
            } catch (e: Exception) {
                Result.failure(Exception("ส่งออกไฟล์ไม่สำเร็จ: ${e.localizedMessage}"))
            }
        }
}
