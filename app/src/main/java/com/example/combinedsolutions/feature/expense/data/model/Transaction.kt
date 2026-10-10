package com.example.combinedsolutions.feature.expense.data.model

import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

enum class TransactionType {
    INCOME,   // รายรับ
    EXPENSE;  // รายจ่าย

    companion object {
        fun fromString(value: String): TransactionType {
            return if (value.equals("INCOME", ignoreCase = true) || value == "รายรับ") {
                INCOME
            } else {
                EXPENSE
            }
        }
    }
}

/**
 * Data Class สำหรับเก็บโครงสร้างข้อมูลแต่ละแถว
 * ได้แก่ วันที่, เวลา, ชื่อรายการ, จำนวนเงิน และประเภทรายการ (รายรับ/รายจ่าย)
 */
data class Transaction(
    val date: String,
    val time: String,
    val title: String,
    val amount: Double,
    val type: TransactionType
) {
    /**
     * แปลง Object เป็นข้อความบรรทัด CSV
     * รูปแบบ: date,time,title,amount,type
     */
    fun toCsvRow(): String {
        val escapedTitle = title.replace(",", " ")
        return "$date,$time,$escapedTitle,$amount,${type.name}\n"
    }

    companion object {
        /**
         * แปลงข้อความบรรทัด CSV กลับเป็น Object
         */
        fun fromCsvRow(csvRow: String): Transaction? {
            val parts = csvRow.trim().split(",")
            if (parts.size < 5) return null

            return try {
                val date = parts[0].trim()
                val time = parts[1].trim()
                val title = parts[2].trim()
                val amount = parts[3].trim().toDoubleOrNull() ?: 0.0
                val type = TransactionType.fromString(parts[4].trim())
                Transaction(date, time, title, amount, type)
            } catch (e: Exception) {
                null
            }
        }

        /**
         * Utility สำหรับสร้างวันและเวลาปัจจุบัน
         */
        fun currentDateTime(): Pair<String, String> {
            val now = Date()
            val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
            val timeFormat = SimpleDateFormat("HH:mm", Locale.getDefault())
            return Pair(dateFormat.format(now), timeFormat.format(now))
        }
    }
}
