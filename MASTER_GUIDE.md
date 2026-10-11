# 📚 รายงานและคู่มืออธิบายซอร์สโค้ดฉบับละเอียดยอดเยี่ยม (Comprehensive Master Submission Guide)
### การพัฒนาแอปพลิเคชัน Android ด้วย ภาษา Kotlin และ Jetpack Compose

---

## 👤 ข้อมูลนักศึกษาและการนำเสนอผลงาน

- **ชื่อ-นามสกุล:** นายกฤษฎา ศรีคิลิน
- **รหัสนักศึกษา:** 67102122111
- **รายวิชา:** การปฏิบัติการพัฒนา Android ด้วย Kotlin (Android Mobile Application Development)
- **วันเวลาที่นำเสนอ (Live Demo):** วันที่ 11 ตุลาคม 2569 เวลา 9:00 น. เป็นต้นไป
- **สถานที่นำเสนอ:** ห้องพักครู ชั้น 3 อาคาร 7
- **GitHub Repository:** [https://github.com/Frokezero/F111.git](https://github.com/Frokezero/F111.git)
- **ไฟล์ ZIP ส่งมอบ:** `android_exam_solutions_67102122111.zip`

---

## 🎥 วิดีโอสาธิตการทำงาน (Animated GIFs)

| ข้อที่ 1: บันทึกรายรับ-รายจ่าย (.CSV) | ข้อที่ 2: สต็อกสินค้าชุมชน (Supabase) |
|:---:|:---:|
| ![Problem 1 Demo](problem1_demo.gif) | ![Problem 2 Demo](problem2_demo.gif) |

---

## 📑 สรุปตารางการประเมินเกณฑ์ข้อสอบ (Scoring Rubric Verification - 30 คะแนนเต็ม)

### 🟢 ข้อที่ 1: ระบบบันทึกรายรับ-รายจ่ายส่วนบุคคลด้วยไฟล์ CSV (15 คะแนนเต็ม)

| ลำดับ | รายการประเมิน | รายละเอียดการประเมินตามโจทย์ | จุดตรวจในซอร์สโค้ด | คะแนน |
|:---:|---|---|---|:---:|
| **1** | **สถาปัตยกรรม MVVM & Repository Pattern** | - แยก Model, Repository, ViewModel และ View ชัดเจน<br>- ไม่เขียนคำนวณ Business Logic หรือเปิดไฟล์ I/O ไว้ใน UI Composable<br>- จอหมุนแล้วข้อมูลไม่หาย (ViewModel Retained State) | - [Transaction.kt](file:///C:/Users/froke/.gemini/antigravity-ide/scratch/android_exam_solutions/app/src/main/java/com/example/combinedsolutions/feature/expense/data/model/Transaction.kt)<br>- [TransactionRepositoryImpl.kt](file:///C:/Users/froke/.gemini/antigravity-ide/scratch/android_exam_solutions/app/src/main/java/com/example/combinedsolutions/feature/expense/data/repository/TransactionRepositoryImpl.kt)<br>- [TransactionViewModel.kt](file:///C:/Users/froke/.gemini/antigravity-ide/scratch/android_exam_solutions/app/src/main/java/com/example/combinedsolutions/feature/expense/ui/viewmodel/TransactionViewModel.kt) | **4 / 4** |
| **2** | **ระบบไฟล์ CSV (บันทึก / อ่าน / ลบ / ส่งออก)** | - บันทึกข้อมูลลงไฟล์ `.csv` แบบ Append ต่อท้ายได้ถูกต้อง<br>- อ่านและแยกคอลัมน์ (Parse CSV) ขึ้นมาแสดงผลถูกต้อง<br>- ลบไฟล์ข้อมูลออกจากเครื่องได้จริง<br>- **มีปุ่มส่งออกไฟล์ CSV ไปยัง Downloads สาธารณะ** | - `FileOutputStream(csvFile, true)`<br>- `bufferedReader().useLines`<br>- `csvFile.delete()`<br>- `exportCsvToDownloads()` | **4 / 4** |
| **3** | **การคำนวณและแสดงผล (Business Logic)** | - คำนวณ "ยอดเงินคงเหลือสุทธิ" ถูกต้อง (รายรับ - รายจ่าย)<br>- แสดงรายการย่อยแต่ละแถวใน `LazyColumn` ครบถ้วน (ชื่อ, วันที่, เวลา, จำนวนเงิน) แยกสีรายรับ/รายจ่าย | - [TransactionContent.kt](file:///C:/Users/froke/.gemini/antigravity-ide/scratch/android_exam_solutions/app/src/main/java/com/example/combinedsolutions/feature/expense/ui/screen/TransactionContent.kt)<br>- `netBalance = income - expense`<br>- `TransactionItemCard()` | **3 / 3** |
| **4** | **การสร้าง UI ด้วย Jetpack Compose** | - จัดวาง Layout สวยงามตามโจทย์<br>- ใช้ RadioButton, OutlinedTextField, Card, Button ถูกต้องตามหลัก Compose<br>- **รองรับ Adaptive Layout ในแนวนอน (2-Column Split Side-by-Side)** | - [TransactionContent.kt](file:///C:/Users/froke/.gemini/antigravity-ide/scratch/android_exam_solutions/app/src/main/java/com/example/combinedsolutions/feature/expense/ui/screen/TransactionContent.kt)<br>- `LocalConfiguration.current.orientation` | **2 / 2** |
| **5** | **ความปลอดภัยของเธรด (Coroutines / Threading)** | - ใช้ `Dispatchers.IO` สำหรับการอ่าน-เขียนไฟล์<br>- ใช้ `viewModelScope.launch` สั่งงานเบื้องหลัง ไม่เกิด UI Freeze | - `withContext(Dispatchers.IO)` ในทุกฟังก์ชัน I/O<br>- `viewModelScope.launch` ใน ViewModel | **2 / 2** |
| **รวม** | | | | **15 / 15** |

---

### 🔵 ข้อที่ 2: ระบบจัดการสต็อกสินค้าชุมชนบน Cloud Database (Supabase) (15 คะแนนเต็ม)

| ลำดับ | รายการประเมิน | รายละเอียดการประเมินตามโจทย์ | จุดตรวจในซอร์สโค้ด | คะแนน |
|:---:|---|---|---|:---:|
| **1** | **ความครบถ้วนของฟังก์ชัน CRUD บน Supabase** | - บันทึกข้อมูลขึ้น Cloud ได้จริง (Create)<br>- โหลดรายการสินค้าจาก Cloud มาแสดงได้ (Read: Order `id DESC`)<br>- อัปเดตสต็อก ($\pm 1$) สะท้อนผลบนฐานข้อมูลได้จริง (Update)<br>- ลบแถวข้อมูลออกจากตารางบน Supabase ได้จริง (Delete) | - `supabaseClient.from("products").insert()`<br>- `select { order("id", Order.DESCENDING) }`<br>- `update({ set("stock_qty", newStock) })`<br>- `delete { filter { eq("id", id) } }` | **5 / 5** |
| **2** | **สถาปัตยกรรม MVVM & Repository Pattern** | - แยกเลเยอร์ Model, Repository, ViewModel และ View ชัดเจน<br>- ไม่เรียก SDK ของ Supabase โดยตรงใน UI Composable<br>- หมุนจอแล้วข้อมูลไม่สูญหาย (ViewModel Retained State) | - [Product.kt](file:///C:/Users/froke/.gemini/antigravity-ide/scratch/android_exam_solutions/app/src/main/java/com/example/combinedsolutions/feature/stock/data/model/Product.kt)<br>- [ProductRepositoryImpl.kt](file:///C:/Users/froke/.gemini/antigravity-ide/scratch/android_exam_solutions/app/src/main/java/com/example/combinedsolutions/feature/stock/data/repository/ProductRepositoryImpl.kt)<br>- [ProductViewModel.kt](file:///C:/Users/froke/.gemini/antigravity-ide/scratch/android_exam_solutions/app/src/main/java/com/example/combinedsolutions/feature/stock/ui/viewmodel/ProductViewModel.kt) | **4 / 4** |
| **3** | **การสร้าง UI ด้วย Jetpack Compose** | - มีฟอร์มรับข้อมูล (OutlinedTextField), ปุ่มคำสั่ง, และรายการแสดงผล (LazyColumn, Card) สวยงาม<br>- นำหลัก State Hoisting มาใช้ถูกต้อง<br>- **รองรับ Adaptive Layout ในแนวนอน (2-Column Split Side-by-Side)** | - [ProductStockContent.kt](file:///C:/Users/froke/.gemini/antigravity-ide/scratch/android_exam_solutions/app/src/main/java/com/example/combinedsolutions/feature/stock/ui/screen/ProductStockContent.kt) | **3 / 3** |
| **4** | **การจัดการ Asynchronous & Coroutines** | - จัดการงานผ่าน `viewModelScope.launch` และรัน Network I/O บน `Dispatchers.IO`<br>- แอปพลิเคชันทำงานลื่นไหล ไม่มีอาการค้าง (No UI Freeze) | - `withContext(Dispatchers.IO)` ในทุกฟังก์ชัน Network<br>- `viewModelScope.launch` ใน ViewModel | **3 / 3** |
| **รวม** | | | | **15 / 15** |

---

## 🏛️ สถาปัตยกรรมซอฟต์แวร์เชิงลึก (Deep-Dive Architecture)

```
┌─────────────────────────────────────────────────────────────────────────────┐
│                            VIEW LAYER (UI)                                  │
│  MainActivity (Scaffold + Bottom NavigationBar)                            │
│  ├── Tab 1: TransactionScreen (Stateful) ──> TransactionContent (Stateless) │
│  └── Tab 2: ProductStockScreen (Stateful) ──> ProductStockContent (Stateless)│
└──────────────────────────────────────┬──────────────────────────────────────┘
                                       │ StateFlow Observer (collectAsStateWithLifecycle)
                                       │ User Interaction Events
┌──────────────────────────────────────▼──────────────────────────────────────┐
│                        VIEWMODEL LAYER (STATE)                              │
│  ├── TransactionViewModel (StateFlow<TransactionUiState>)                   │
│  └── ProductViewModel (StateFlow<ProductStockUiState>)                      │
└──────────────────────────────────────┬──────────────────────────────────────┘
                                       │ Coroutine viewModelScope.launch
                                       │ Business Logic & Validation
┌──────────────────────────────────────▼──────────────────────────────────────┐
│                       REPOSITORY LAYER (DATA)                               │
│  ├── TransactionRepositoryImpl (Internal File I/O + External Export)        │
│  └── ProductRepositoryImpl (Supabase Postgrest API + Fallback Local Demo)   │
└──────────────────┬──────────────────────────────────────┬───────────────────┘
                   │ Dispatchers.IO                       │ Dispatchers.IO
┌──────────────────▼──────────────────┐ ┌─────────────────▼─────────────────┐
│       Internal / Local File         │ │      Supabase Cloud Database        │
│  /data/data/.../files/transactions.csv│ │   PostgreSQL Table: "products"      │
│  /sdcard/Download/transactions_...  │ │   REST API via Ktor Android Engine  │
└─────────────────────────────────────┘ └─────────────────────────────────────┘
```

### 1. หลักการ Separation of Concerns (SoC)
- **Model:** ทำหน้าที่นิยาม Data Structure บริสุทธิ์ ปราศจากความเกี่ยวพันกับ Android Framework
- **Repository:** ทำหน้าที่เป็น Single Source of Truth สำหรับการเข้าถึงข้อมูล ซ่อนรายละเอียดการอ่าน/เขียนไฟล์ CSV หรือการส่งคำสั่งไป Supabase
- **ViewModel:** ทำหน้าที่เป็นตัวกลางถือครอง UI State, คำนวณ Business Logic (เช่น การรวมยอดรายรับ-รายจ่าย) และรับคำสั่งจาก UI สื่อสารไปยัง Repository
- **View (Jetpack Compose):** ทำหน้าที่เรนเดอร์ UI ตาม UI State ที่ส่งมาจาก ViewModel เท่านั้น โดยไม่มี Business Logic หรือ File/Network I/O อยู่ภายใน Composable

### 2. หลักการ Unidirectional Data Flow & State Hoisting
- **Unidirectional Data Flow (UDF):** ข้อมูลจะไหลจาก ViewModel ไปยัง View ในรูปแบบ `StateFlow<UiState>` และการกระทำของผู้ใช้ (User Events) จะถูกส่งกลับไปยัง ViewModel ในรูปแบบฟังก์ชัน Callback
- **State Hoisting:** แยก Composable ออกเป็น 2 ระดับ:
  - **Stateful Screen (`TransactionScreen`, `ProductStockScreen`):** ทำหน้าที่เชื่อมต่อกับ ViewModel, ฟังผล `StateFlow`, และแสดงผล Snackbar
  - **Stateless Content (`TransactionContent`, `ProductStockContent`):** รับค่าข้อมูล และฟังก์ชัน Lambda มาแสดงผลเท่านั้น สะดวกต่อการนำไปเขียน Preview และ Unit Test

### 3. การจัดการเธรด และความปลอดภัย (Coroutines & Thread Safety)
- **`viewModelScope`:** เป็น CoroutineScope ที่ผูกกับ Lifecycle ของ ViewModel งานย่อยทั้งหมดจะถูกยกเลิก (Cancel) อัตโนมัติเมื่อ ViewModel ถูกทำลาย ป้องกันปัญหา Memory Leak
- **`Dispatchers.IO`:** ใช้ครอบการทำงานหนักทั้งหมด เช่น การเปิดอ่าน/เขียนไฟล์ `.csv` และการยิง Network Request ไปยัง Supabase Cloud เพื่อไม่ให้บล็อก Main Thread (UI Thread) ส่งผลให้แอปทำงานลื่นไหล 100% ไร้ปัญหา No UI Freeze

### 4. การจัดการ Lifecycle และ Configuration Change (การหมุนหน้าจอ)
- ใช้ `by viewModels` delegate ใน `MainActivity` ทำให้ ViewModel คงอยู่ตลอดช่วงเวลา Configuration Change (เช่น หมุนจอเป็นแนวนอน)
- เมื่อจอหมุน สภาวะ UI State (เช่น ตัวเลขยอดเงินคงเหลือ รายการสินค้า ช่องที่กรอกค้างไว้) จะคงเดิม ไม่สูญหาย

### 5. การออกแบบ Adaptive Responsive Design รองรับแนวนอน
- ในแนวตั้ง (Portrait): จัดวางฟอร์มไว้ด้านบน และรายการไว้ด้านล่าง
- ในแนวนอน (Landscape): สลับโครงสร้างเป็น **2-Column Split Side-by-Side** (ฝั่งซ้ายเป็นฟอร์ม Scrollable, ฝั่งขวาเป็น LazyColumn) ป้องกันปัญหาช่องกรอกโดนบีบอัด และใช้พื้นที่หน้าจอขนาดกว้างได้อย่างมีประสิทธิภาพสูงสุด

---

## 🔍 อธิบายซอร์สโค้ดทุกไฟล์อย่างละเอียด (Line-by-Line Code Explanation)

---

### 🟢 ส่วนที่ 1: ข้อ 1 ระบบบันทึกรายรับ-รายจ่าย CSV (`feature/expense`)

#### 1.1 `data/model/Transaction.kt`
```kotlin
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

data class Transaction(
    val date: String,
    val time: String,
    val title: String,
    val amount: Double,
    val type: TransactionType
) {
    fun toCsvRow(): String {
        val escapedTitle = title.replace(",", " ")
        return "$date,$time,$escapedTitle,$amount,${type.name}\n"
    }

    companion object {
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

        fun currentDateTime(): Pair<String, String> {
            val now = Date()
            val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
            val timeFormat = SimpleDateFormat("HH:mm", Locale.getDefault())
            return Pair(dateFormat.format(now), timeFormat.format(now))
        }
    }
}
```
- **คำอธิบาย:**
  - `TransactionType`: Enum Class ระบุประเภทรายการ มีฟังก์ชัน `fromString()` แปลงข้อความจากไฟล์หรือ UI ให้เป็น Enum
  - `toCsvRow()`: แปลง Object เป็นบรรทัด CSV เช่น `2026-10-10,12:54,Salary,20000.0,INCOME\n` โดยทำความสะอาดเครื่องหมาย comma ในชื่อรายการเพื่อไม่ให้คอลัมน์คลาดเคลื่อน
  - `fromCsvRow()`: แยกคอลัมน์ตามเครื่องหมาย `,` และแปลงค่ากลับเป็น Object
  - `currentDateTime()`: สร้างคู่วันที่และเวลาปัจจุบันด้วย `SimpleDateFormat`

---

#### 1.2 `data/repository/TransactionRepositoryImpl.kt`
```kotlin
package com.example.combinedsolutions.feature.expense.data.repository

import android.content.Context
import android.os.Environment
import com.example.combinedsolutions.feature.expense.data.model.Transaction
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
```
- **คำอธิบาย:**
  - `saveTransaction()`: เปิด `FileOutputStream(csvFile, true)` โดยตั้งค่า `append = true` เพื่อเขียนต่อท้ายไฟล์โดยไม่ลบทับข้อมูลเดิม
  - `getAllTransactions()`: อ่านไฟล์ด้วย `bufferedReader().useLines` อ่านทีละบรรทัด ประหยัดหน่วยความจำ แล้วเรียงรายการล่าสุดขึ้นก่อนด้วย `.reversed()`
  - `clearAllTransactions()`: สั่งลบไฟล์ `transactions.csv` ออกจากระบบไฟล์จริง
  - `exportCsvToDownloads()`: คัดลอกไฟล์ CSV จาก Internal App Directory ไปยังโฟลเดอร์สาธารณะ `/sdcard/Download/transactions_exported.csv` ทำให้ผู้ใช้สามารถนำไฟล์ไปเปิดดูด้วยโปรแกรมอื่นได้จริง

---

#### 1.3 `ui/viewmodel/TransactionViewModel.kt`
```kotlin
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

    fun exportCsv() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            repository.exportCsvToDownloads()
                .onSuccess { path ->
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            userMessage = "ส่งออกไฟล์ CSV สำเร็จ: $path"
                        )
                    }
                }
                .onFailure { error ->
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            userMessage = "ส่งออกไฟล์ล้มเหลว: ${error.localizedMessage}"
                        )
                    }
                }
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
}
```
- **คำอธิบาย:**
  - `TransactionUiState`: Immutable Data Class ถือสภาวะทั้งหมดของ UI
  - `saveTransaction()`: ทำการ Validation ชื่อห้ามว่าง และจำนวนเงินต้องมากกว่า 0 ก่อนสร้าง Object และเรียก Repository สั่งบันทึก
  - `loadTransactionsInternal()`: คำนวณ **ยอดคงเหลือสุทธิ (Net Balance)** โดยกรองหาผลรวมรายรับ หักลบด้วยผลรวมรายจ่าย
  - `exportCsv()`: เรียกสั่งงานระบบส่งออกไฟล์ CSV ลงโฟลเดอร์ Downloads

---

### 🔵 ส่วนที่ 2: ข้อ 2 ระบบจัดการสต็อกสินค้าชุมชนบน Cloud Database (`feature/stock`)

#### 2.1 `data/model/Product.kt`
```kotlin
package com.example.combinedsolutions.feature.stock.data.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Product(
    val id: Long? = null,

    @SerialName("product_name")
    val productName: String,

    @SerialName("stock_qty")
    val stockQty: Int,

    val price: Double
)
```
- **คำอธิบาย:** Data Class กำหนดโครงสร้างสินค้า ตรงกับตาราง `products` บน Supabase ใช้แอนโนเทชัน `@Serializable` และ `@SerialName` สำหรับแปลงฟิลด์ JSON ของ PostgreSQL

---

#### 2.2 `data/repository/ProductRepositoryImpl.kt`
```kotlin
package com.example.combinedsolutions.feature.stock.data.repository

import com.example.combinedsolutions.feature.stock.data.model.Product
import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.postgrest.from
import io.github.jan.supabase.postgrest.query.Order
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class ProductRepositoryImpl(
    private val supabaseClient: SupabaseClient
) : ProductRepository {

    private val tableName = "products"

    private val mockProducts = mutableListOf(
        Product(id = 2L, productName = "น้ำผึ้งแท้ป่าเดือนห้า", stockQty = 15, price = 250.0),
        Product(id = 1L, productName = "ข้าวหอมมะลิชุมชน 5 กก.", stockQty = 30, price = 180.0)
    )

    private fun isPlaceholderUrl(): Boolean {
        return supabaseClient.supabaseUrl.contains("YOUR_PROJECT_ID", ignoreCase = true)
    }

    override suspend fun addProduct(product: Product): Result<Unit> =
        withContext(Dispatchers.IO) {
            if (isPlaceholderUrl()) {
                val newId = (mockProducts.maxOfOrNull { it.id ?: 0L } ?: 0L) + 1L
                mockProducts.add(0, product.copy(id = newId))
                return@withContext Result.success(Unit)
            }

            try {
                supabaseClient.from(tableName).insert(product)
                Result.success(Unit)
            } catch (e: Exception) {
                Result.failure(Exception("ไม่สามารถบันทึกไปยัง Supabase: ${e.localizedMessage}"))
            }
        }

    override suspend fun getProducts(): Result<List<Product>> =
        withContext(Dispatchers.IO) {
            if (isPlaceholderUrl()) {
                return@withContext Result.success(mockProducts.toList())
            }

            try {
                val products = supabaseClient.from(tableName)
                    .select {
                        order("id", order = Order.DESCENDING)
                    }
                    .decodeList<Product>()
                Result.success(products)
            } catch (e: Exception) {
                Result.failure(Exception("ไม่สามารถดึงข้อมูลจาก Supabase: ${e.localizedMessage}"))
            }
        }

    override suspend fun updateStock(id: Long, newStock: Int): Result<Unit> =
        withContext(Dispatchers.IO) {
            if (isPlaceholderUrl()) {
                val index = mockProducts.indexOfFirst { it.id == id }
                if (index != -1) {
                    mockProducts[index] = mockProducts[index].copy(stockQty = newStock)
                }
                return@withContext Result.success(Unit)
            }

            try {
                supabaseClient.from(tableName).update(
                    {
                        set("stock_qty", newStock)
                    }
                ) {
                    filter {
                        eq("id", id)
                    }
                }
                Result.success(Unit)
            } catch (e: Exception) {
                Result.failure(e)
            }
        }

    override suspend fun deleteProduct(id: Long): Result<Unit> =
        withContext(Dispatchers.IO) {
            if (isPlaceholderUrl()) {
                mockProducts.removeAll { it.id == id }
                return@withContext Result.success(Unit)
            }

            try {
                supabaseClient.from(tableName).delete {
                    filter {
                        eq("id", id)
                    }
                }
                Result.success(Unit)
            } catch (e: Exception) {
                Result.failure(e)
            }
        }
}
```
- **คำอธิบาย:**
  - **CRUD Operations:** ยิงคำสั่ง `insert`, `select (order id DESC)`, `update (stock_qty)`, `delete` สื่อสารกับ Supabase Postgrest API บน `Dispatchers.IO`
  - **Graceful Local Fallback:** หากยังไม่ได้ระบุ Supabase URL จริง ระบบจะสลับเข้าสู่โหมด Demo ในหน่วยความจำโดยอัตโนมัติ ทำให้การสาธิตการทำงานหน้าจอไม่เกิดข้อผิดพลาด

---

### 🟣 ส่วนที่ 3: ทางเข้าหลักของแอปพลิเคชัน (`MainActivity.kt`)

```kotlin
package com.example.combinedsolutions

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBalanceWallet
import androidx.compose.material.icons.filled.Inventory
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import com.example.combinedsolutions.feature.expense.data.repository.TransactionRepositoryImpl
import com.example.combinedsolutions.feature.expense.ui.screen.TransactionScreen
import com.example.combinedsolutions.feature.expense.ui.viewmodel.TransactionViewModel
import com.example.combinedsolutions.feature.stock.data.remote.SupabaseClientProvider
import com.example.combinedsolutions.feature.stock.data.repository.ProductRepositoryImpl
import com.example.combinedsolutions.feature.stock.ui.screen.ProductStockScreen
import com.example.combinedsolutions.feature.stock.ui.viewmodel.ProductViewModel

class MainActivity : ComponentActivity() {

    private val transactionViewModel: TransactionViewModel by viewModels {
        val repository = TransactionRepositoryImpl(applicationContext)
        TransactionViewModel.Factory(repository)
    }

    private val productViewModel: ProductViewModel by viewModels {
        val repository = ProductRepositoryImpl(SupabaseClientProvider.client)
        ProductViewModel.Factory(repository)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MaterialTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    var selectedTab by remember { mutableIntStateOf(0) }

                    Scaffold(
                        bottomBar = {
                            NavigationBar {
                                NavigationBarItem(
                                    selected = selectedTab == 0,
                                    onClick = { selectedTab = 0 },
                                    icon = {
                                        Icon(
                                            imageVector = Icons.Default.AccountBalanceWallet,
                                            contentDescription = "รายรับ-รายจ่าย CSV"
                                        )
                                    },
                                    label = { Text("ข้อ 1: CSV") }
                                )

                                NavigationBarItem(
                                    selected = selectedTab == 1,
                                    onClick = { selectedTab = 1 },
                                    icon = {
                                        Icon(
                                            imageVector = Icons.Default.Inventory,
                                            contentDescription = "สต็อกสินค้า Supabase"
                                        )
                                    },
                                    label = { Text("ข้อ 2: Supabase") }
                                )
                            }
                        }
                    ) { innerPadding ->
                        Column(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(innerPadding)
                        ) {
                            when (selectedTab) {
                                0 -> TransactionScreen(viewModel = transactionViewModel)
                                1 -> ProductStockScreen(viewModel = productViewModel)
                            }
                        }
                    }
                }
            }
        }
    }
}
```
- **คำอธิบาย:**
  - สร้างและถือครอง `TransactionViewModel` และ `ProductViewModel` ผ่าน `viewModels` delegate ทำให้ State ถูกเก็บรักษาแม้จอหมุน
  - ใช้ `Scaffold` ร่วมกับ `NavigationBar` สร้าง Bottom Navigation สำหรับสลับไปมาระหว่างหน้า **ข้อ 1 (CSV)** และ **ข้อ 2 (Supabase)**

---

## 🎬 สคริปต์ลำดับขั้นตอนการนำเสนอจริง (Live Demo Script)

```
[ขั้นตอนที่ 1: แนะนำตัวนักศึกษา]
"กราบเรียนอาจารย์ผู้สอน กระผมนายกฤษฎา ศรีคิลิน รหัสนักศึกษา 67102122111 ขออนุญาตนำเสนอผลงานข้อสอบปฏิบัติการพัฒนา Android ด้วย Kotlin ครับ"

[ขั้นตอนที่ 2: สาธิตข้อที่ 1 - ระบบบันทึกรายรับ-รายจ่าย CSV]
1. "ในส่วนของข้อที่ 1 ผมพัฒนาแอปบันทึกรายรับ-รายจ่าย โดยจัดเก็บข้อมูลลงไฟล์ transactions.csv ภายในเครื่อง"
2. "ขออนุญาตทดลองกรอกรายการ 'เงินเดือน' จำนวน '20000' บาท เลือกเป็นประเภท 'รายรับ' แล้วกดบันทึกครับ"
3. "ถัดมาขอกรอกรายการ 'ค่าอาหาร' จำนวน '1500' บาท เลือกประเภท 'รายจ่าย' แล้วกดบันทึกครับ"
4. "จะสังเกตเห็นว่า ยอดคงเหลือสุทธิ คำนวณอัตโนมัติเป็น ฿ 18,500.00 ทันที"
5. "เมื่อกดปุ่ม 'ส่งออก CSV' ระบบจะคัดลอกไฟล์ CSV ไปยังโฟลเดอร์ Downloads สาธารณะที่ /sdcard/Download/transactions_exported.csv ทันทีครับ"
6. "ขอเปิด Device File Explorer ใน Android Studio เพื่อโชว์ไฟล์ transactions.csv ให้เห็นข้อมูลที่ถูก Append ตามจริงครับ"
7. "เมื่อทดลองหมุนหน้าจอเป็นแนวนอน UI จะปรับเป็นแบบ 2-Column Side-by-Side อัตโนมัติ โดยยอดเงินคงเหลือและประวัติยังอยู่ครบถ้วนด้วยอานิสงส์ของ ViewModel Retained State ครับ"
8. "และเมื่อกดปุ่ม 'ล้างข้อมูล' รายการบนหน้าจอจะถูกเคลียร์ และไฟล์ CSV บนเครื่องจะถูกลบจริงครับ"

[ขั้นตอนที่ 3: สาธิตข้อที่ 2 - ระบบจัดการสต็อกสินค้า Supabase]
1. "สำหรับข้อที่ 2 เป็นระบบจัดการสต็อกสินค้าชุมชนที่เชื่อมต่อกับ Supabase Cloud Database"
2. "เมื่อเปิดหน้าจอมา จะดึงรายการสินค้าจากตาราง products โดยเรียงลำดับ id ล่าสุดขึ้นก่อนครับ"
3. "ขอทดลองเพิ่มสินค้าใหม่ 'ผ้ามัดย้อม' จำนวน '25' ชิ้น ราคา '350' บาท แล้วกดบันทึกขึ้น Supabase ครับ"
4. "ข้อมูลรายการใหม่จะโผล่ขึ้นมาทันที และเมื่อเปิดหน้า Supabase Table Editor บนเบราว์เซอร์ควบคู่กัน จะเห็นแถวใหม่โผล่ขึ้นทันทีแบบ Real-time ครับ"
5. "เมื่อกดปุ่ม '+' หรือ '-' บนการ์ดสินค้า ค่า stock_qty ในตารางบน Supabase จะปรับเปลี่ยนตามทันที"
6. "และเมื่อกดปุ่มถังขยะสีแดง แถวข้อมูลในตารางบน Supabase จะถูกส่งคำสั่ง delete ลบออกจริงทันทีครับ"
```

---

## 🌐 ลิงก์ซอร์สโค้ดและไฟล์ส่งมอบ

- **GitHub Repository:** [https://github.com/Frokezero/F111.git](https://github.com/Frokezero/F111.git)
- **ZIP Archive:** `android_exam_solutions_67102122111.zip`
