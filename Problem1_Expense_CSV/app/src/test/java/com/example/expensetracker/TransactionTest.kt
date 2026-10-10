package com.example.expensetracker

import com.example.expensetracker.data.model.Transaction
import com.example.expensetracker.data.model.TransactionType
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull
import org.junit.Test

class TransactionTest {

    @Test
    fun testToCsvRow_Income() {
        val transaction = Transaction(
            date = "2023-10-10",
            time = "10:00:00",
            title = "Part-time job",
            amount = 1000.0,
            type = TransactionType.INCOME
        )
        val expected = "2023-10-10,10:00:00,Part-time job,1000.0,INCOME\n"
        assertEquals(expected, transaction.toCsvRow())
    }

    @Test
    fun testToCsvRow_Expense() {
        val transaction = Transaction(
            date = "2023-10-10",
            time = "12:30:00",
            title = "Food",
            amount = 150.5,
            type = TransactionType.EXPENSE
        )
        val expected = "2023-10-10,12:30:00,Food,150.5,EXPENSE\n"
        assertEquals(expected, transaction.toCsvRow())
    }

    @Test
    fun testFromCsvRow_Valid() {
        val csvLine = "2023-10-10,14:00:00,Coffee,80.0,EXPENSE"
        val transaction = Transaction.fromCsvRow(csvLine)

        assertNotNull(transaction)
        assertEquals("2023-10-10", transaction?.date)
        assertEquals("14:00:00", transaction?.time)
        assertEquals("Coffee", transaction?.title)
        assertEquals(80.0, transaction?.amount ?: 0.0, 0.001)
        assertEquals(TransactionType.EXPENSE, transaction?.type)
    }

    @Test
    fun testFromCsvRow_InvalidFormat() {
        val invalidCsv = "invalid,data,format"
        val transaction = Transaction.fromCsvRow(invalidCsv)
        assertNull(transaction)
    }

    @Test
    fun testFromCsvRow_BlankLine() {
        val transaction = Transaction.fromCsvRow("   ")
        assertNull(transaction)
    }
}
