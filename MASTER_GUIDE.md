# 📚 คู่มือคำอธิบายซอร์สโค้ดอย่างละเอียด (Complete Master Guide)
### โครงสร้างแอปรวมข้อสอบปฏิบัติ Android ด้วย Kotlin (CSV & Supabase)

โปรเจกต์นี้ได้รับการพัฒนาและรวมศูนย์ (Consolidated) ให้เป็นแอปพลิเคชัน Android ที่สมบูรณ์ สามารถสลับใช้งานระหว่าง **ข้อ 1 (บันทึกรายรับ-รายจ่าย CSV)** และ **ข้อ 2 (สต็อกสินค้า Supabase)** ได้อย่างสะดวกผ่าน Navigation Bar ด้านล่าง

🔗 **GitHub Repository:** [https://github.com/Frokezero/F111.git](https://github.com/Frokezero/F111.git)

---

## 🏛️ สถาปัตยกรรมภาพรวม (Architecture Overview)

โปรเจกต์นี้ถูกออกแบบโดยยึดหลัก **MVVM (Model-View-ViewModel)** ร่วมกับ **Repository Pattern** และ **State Hoisting** ใน **Jetpack Compose**:

```
 ┌─────────────────────────────────────────────────────────┐
 │                      VIEW LAYER                         │
 │  (Jetpack Compose: MainActivity / Screen / Content)     │
 └───────────────────────────┬─────────────────────────────┘
                             │ StateFlow / User Events
 ┌───────────────────────────▼─────────────────────────────┐
 │                    VIEWMODEL LAYER                      │
 │      (TransactionViewModel / ProductViewModel)          │
 └───────────────────────────┬─────────────────────────────┘
                             │ Coroutines (viewModelScope)
 ┌───────────────────────────▼─────────────────────────────┐
 │                    REPOSITORY LAYER                     │
 │   (TransactionRepositoryImpl / ProductRepositoryImpl)   │
 └─────────────┬─────────────────────────────┬─────────────┘
               │ Dispatchers.IO              │ Dispatchers.IO
 ┌─────────────▼─────────────┐ ┌─────────────▼─────────────┐
 │  Internal File Storage    │ │   Supabase Cloud DB       │
 │    (transactions.csv)     │ │    (PostgreSQL REST)      │
 └───────────────────────────┘ └─────────────────────────┘
```

---

## 📖 อธิบายซอร์สโค้ดทุกส่วนอย่างละเอียด (Detailed Code Explanation)

---

### 🟢 ส่วนที่ 1: ข้อ 1 ระบบบันทึกรายรับ-รายจ่ายส่วนบุคคลด้วยไฟล์ CSV (`feature/expense`)

#### 1.1 `data/model/Transaction.kt` (Model & Serialization)
- **`TransactionType`**: Enum Class ระบุประเภทรายการ (`INCOME` หรือ `EXPENSE`) มี companion object `fromString()` ช่วยแปลงข้อความภาษาไทย/อังกฤษเป็น Enum อย่างปลอดภัย
- **`Transaction`**: Data Class เก็บฟิลด์ `date`, `time`, `title`, `amount`, `type`
  - **`toCsvRow()`**: แปลง Data Class เป็น String แถว CSV เช่น `2026-10-10,12:08,Salary,15000.0,INCOME\n` โดยทำความสะอาดเครื่องหมาย comma ในชื่อรายการเพื่อป้องกันโครงสร้าง CSV เสียหาย
  - **`fromCsvRow()`**: Parse บรรทัด CSV String กลับเป็น `Transaction?` โดยแยกตามเครื่องหมาย comma
  - **`currentDateTime()`**: สร้างคู่ String วันที่ (`yyyy-MM-dd`) และเวลา (`HH:mm`) ปัจจุบันด้วย `SimpleDateFormat`

#### 1.2 `data/repository/TransactionRepository.kt` & `TransactionRepositoryImpl.kt` (File I/O Pipeline)
- **`TransactionRepository`**: Interface กำหนดสัญญาการทำงาน (Contract): `saveTransaction`, `getAllTransactions`, `clearAllTransactions`
- **`TransactionRepositoryImpl`**:
  - **`csvFile`**: อ้างอิงพาธ `context.filesDir/transactions.csv` (Internal Storage)
  - **`saveTransaction()`**: ครอบด้วย `withContext(Dispatchers.IO)` เปิด `FileOutputStream(csvFile, true)` (Append Mode) เพื่อเขียนแถวต่อท้ายโดยไม่ทับของเดิม
  - **`getAllTransactions()`**: เปิดอ่านด้วย `bufferedReader().useLines` อ่านทีละบรรทัด แปลงเป็น List แล้วเรียงรายการล่าสุดขึ้นก่อนด้วย `.reversed()`
  - **`clearAllTransactions()`**: ตรวจสอบหากไฟล์มีอยู่ ให้สั่ง `csvFile.delete()` เพื่อลบไฟล์ออกจากเครื่องจริง

#### 1.3 `ui/viewmodel/TransactionViewModel.kt` (State Management & Business Logic)
- **`TransactionUiState`**: Immutable Data Class ถือครอง UI State ทั้งหมด (`titleInput`, `amountInput`, `selectedType`, `transactions`, `netBalance`, `isLoading`, `userMessage`)
- **`TransactionViewModel`**:
  - **`_uiState`**: ใช้ `MutableStateFlow` และเปิดเผยเป็น Read-only `StateFlow` ผ่าน `uiState`
  - **`saveTransaction()`**:
    1. ตรวจสอบความถูกต้องของข้อมูล (Validation) ชื่อห้ามว่าง และจำนวนเงินต้อง > 0
    2. เรียก `repository.saveTransaction()` ใน `viewModelScope.launch`
    3. เมื่อสำเร็จให้ล้างช่องกรอกข้อมูล อัปเดตข้อความแจ้งเตือน และโหลดรายการใหม่
  - **`loadTransactionsInternal()`**: คำนวณ **ยอดคงเหลือสุทธิ (Net Balance)** โดยกรองหาผลรวมรายรับ (`INCOME`) หักลบด้วยผลรวมรายจ่าย (`EXPENSE`)
  - **`clearAllData()`**: เรียกสั่งลบไฟล์ CSV และเคลียร์รายการบนหน้าจอทั้งหมด
  - **`ViewModelProvider.Factory`**: Custom Factory สำหรับ Pass `TransactionRepository` เข้า ViewModel

#### 1.4 `ui/screen/TransactionScreen.kt` & `TransactionContent.kt` (Jetpack Compose UI)
- **State Hoisting**:
  - **`TransactionScreen`** (Stateful): ทำหน้าที่สังเกตการณ์ `uiState` ด้วย `collectAsStateWithLifecycle()` และจัดการ Snackbar
  - **`TransactionContent`** (Stateless): ทำหน้าที่แสดงผล UI Components ตามข้อมูลที่รับเข้ามา
- **UI Components**:
  - **Card ยอดคงเหลือสุทธิ**: แสดงผลยอดสุทธิ์ เปลี่ยนสีตัวเลขเป็นสีเขียวเมื่อยอด $\ge 0$ และสีแดงเมื่อติดลบ
  - **OutlinedTextField & RadioButton**: ฟอร์มรับข้อมูลชื่อรายการ จำนวนเงิน และเลือกประเภท (รายรับ/รายจ่าย)
  - **Button & OutlinedButton**: ปุ่ม "บันทึกข้อมูล" และปุ่ม "ล้างข้อมูล"
  - **LazyColumn & Card**: แสดงรายการประวัติแต่ละแถว มีการจัดฟอร์แมตตัวเลขด้วย `DecimalFormat("#,##0.00")`

---

### 🔵 ส่วนที่ 2: ข้อ 2 ระบบจัดการสต็อกสินค้าชุมชนบน Cloud Database (`feature/stock`)

#### 2.1 `data/model/Product.kt` (Model & JSON Serialization)
- **`Product`**: Data Class ติดตั้ง `@Serializable` สำหรับใช้งานร่วมกับ `kotlinx.serialization`
- **`@SerialName("product_name")`** & **`@SerialName("stock_qty")`**: Mapping ชื่อคอลัมน์ใน Supabase PostgreSQL กับชื่อ Property ใน Kotlin

#### 2.2 `data/remote/SupabaseClientProvider.kt` (Cloud Connection)
- **`SupabaseClientProvider`**: Singleton Object ถือครอง instance ของ `SupabaseClient` ติดตั้งฟีเจอร์ `Postgrest` สำหรับทำ RESTful Queries ไปยัง Supabase Cloud

#### 2.3 `data/repository/ProductRepository.kt` & `ProductRepositoryImpl.kt` (Supabase CRUD)
- **`ProductRepository`**: Interface กำหนดสัญญา CRUD: `addProduct`, `getProducts`, `updateStock`, `deleteProduct`
- **`ProductRepositoryImpl`**: สั่งงาน Supabase SDK บน `Dispatchers.IO`:
  - **C (Create)**: `supabaseClient.postgrest["products"].insert(product)`
  - **R (Read)**: `supabaseClient.postgrest["products"].select { order("id", Order.DESCENDING) }.decodeList<Product>()`
  - **U (Update)**: `supabaseClient.postgrest["products"].update({ set("stock_qty", newStock) }) { filter { eq("id", id) } }`
  - **D (Delete)**: `supabaseClient.postgrest["products"].delete { filter { eq("id", id) } }`

#### 2.4 `ui/viewmodel/ProductViewModel.kt` (UI State & Network Operations)
- **`ProductStockUiState`**: Immutable State ถือช่องกรอก, รายการสินค้า `List<Product>`, สถานะโหลด และข้อความแจ้งเตือน
- **`ProductViewModel`**:
  - **`addProduct()`**: ตรวจสอบชื่อ, สต็อก ($\ge 0$), และราคา ($\ge 0$) แล้วสั่งยิง Insert ขึ้น Cloud
  - **`updateStock(product, delta)`**: คำนวณจำนวนสต็อกใหม่ (`product.stockQty + delta`) ตรวจสอบไม่ให้ติดลบ แล้วส่งคำสั่ง Update ไปยัง Supabase ทันที
  - **`deleteProduct(product)`**: ส่งคำสั่ง Delete ลบแถวสินค้าตาม `id`

#### 2.5 `ui/screen/ProductStockScreen.kt` & `ProductStockContent.kt` (Compose UI)
- **TopBar**: แสดงชื่อแอปพลิเคชัน พร้อมปุ่ม Refresh สั่งดึงรายการล่าสุด
- **Form Card**: ฟอร์มรับข้อมูล "ชื่อสินค้า", "จำนวนคงเหลือ", "ราคา" พร้อมปุ่ม "บันทึกขึ้น Supabase"
- **LazyColumn & ProductStockCard**: แสดงการ์ดสินค้าแต่ละรายการ มีปุ่ม `+1` / `-1` สต็อก และปุ่มถังขยะสีแดงสั่งลบสินค้า

---

### 🟣 ส่วนที่ 3: แอปรวมทางเข้าหลัก (`MainActivity.kt`)

#### `MainActivity.kt`
- ทำหน้าที่เป็นทางเข้าหลักของแอปพลิเคชัน (Main Entry Point)
- สร้าง Instance ของทั้ง `TransactionViewModel` และ `ProductViewModel` ผ่าน `viewModels` delegate (Retained State เมื่อหมุนหน้าจอ)
- ใช้ **`Scaffold` + `NavigationBar` (Bottom Navigation)**:
  - **Tab 1: "ข้อ 1: CSV"**: เรียกแสดงผล `TransactionScreen(viewModel = transactionViewModel)`
  - **Tab 2: "ข้อ 2: Supabase"**: เรียกแสดงผล `ProductStockScreen(viewModel = productViewModel)`

---

## 🎯 คำแนะนำการรันและการนำเสนอ (Live Demo Instructions)

1. **การรันบน Android Studio / Emulator:**
   - สั่ง `./gradlew :app:assembleDebug` หรือกดปุ่ม **Run App** (`com.example.combinedsolutions`)
   - แอปจะเปิดขึ้นมาบน Emulator พร้อม Navigation Bar ด้านล่าง
2. **การสาธิตข้อ 1 (CSV):**
   - กรอกข้อมูลรายรับ/รายจ่าย -> กด "บันทึกข้อมูล"
   - เปิดแถบ **View -> Tool Windows -> Device File Explorer** ไปที่ `/data/data/com.example.combinedsolutions/files/transactions.csv` เพื่อโชว์ไฟล์จริงให้อาจารย์ดู
   - ทดสอบกดปุ่ม **Rotate Screen** ของ Emulator โชว์ว่าตัวเลขและประวัติคงอยู่ครบถ้วน
3. **การสาธิตข้อ 2 (Supabase):**
   - นำ URL และ Anon Key ของโปรเจกต์ Supabase ของคุณใส่ใน [SupabaseClientProvider.kt](file:///C:/Users/froke/.gemini/antigravity-ide/scratch/android_exam_solutions/app/src/main/java/com/example/combinedsolutions/feature/stock/data/remote/SupabaseClientProvider.kt)
   - เปิดหน้าเบราว์เซอร์ Supabase **Table Editor -> products** ไว้ข้างๆ จอ Emulator
   - ทดสอบกดเพิ่มสินค้า, กดปุ่ม `+1`/`-1` ปรับสต็อก และกดปุ่มลบ เพื่อโชว์การเปลี่ยนแปลง Real-time บน Supabase
