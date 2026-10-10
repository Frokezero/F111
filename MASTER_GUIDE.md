# 📚 คู่มือและเฉลยข้อสอบปฏิบัติการพัฒนา Android ด้วย Kotlin (ฉบับสมบูรณ์)

โครงสร้างโฟลเดอร์สำหรับส่งงานและโค้ดตัวอย่างถูกจัดเตรียมไว้เรียบร้อยในโฟลเดอร์:
`android_exam_solutions/`
- [Problem1_Expense_CSV](file:///C:/Users/froke/.gemini/antigravity-ide/scratch/android_exam_solutions/Problem1_Expense_CSV) *(ข้อ 1: บันทึกรายรับ-รายจ่าย CSV)*
- [Problem2_Community_Stock_Supabase](file:///C:/Users/froke/.gemini/antigravity-ide/scratch/android_exam_solutions/Problem2_Community_Stock_Supabase) *(ข้อ 2: ระบบจัดการสต็อกสินค้าชุมชน Supabase)*

---

## 📑 สรุปการให้คะแนนตามเกณฑ์ (Scoring Rubrics Checklist)

### 🟢 ข้อที่ 1: ระบบบันทึกรายรับ-รายจ่ายส่วนบุคคลด้วยไฟล์ CSV (15 คะแนน)
| เกณฑ์ | รายละเอียด | จุดตรวจในโค้ด | คะแนน |
|---|---|---|:---:|
| **1. สถาปัตยกรรม MVVM & Repository** | แยก Model, Repository, ViewModel, View ชัดเจน / ไม่มี Logic ใน UI / จอหมุนแล้ว State ไม่หาย | [TransactionRepository](file:///C:/Users/froke/.gemini/antigravity-ide/scratch/android_exam_solutions/Problem1_Expense_CSV/app/src/main/java/com/example/expensetracker/data/repository/TransactionRepository.kt), [TransactionViewModel](file:///C:/Users/froke/.gemini/antigravity-ide/scratch/android_exam_solutions/Problem1_Expense_CSV/app/src/main/java/com/example/expensetracker/ui/viewmodel/TransactionViewModel.kt) | **4** |
| **2. ระบบไฟล์ CSV** | Append ข้อมูลลง `.csv` ถูกต้อง / Parse บรรทัดต่อบรรทัด / ลบไฟล์จากเครื่องจริง | [TransactionRepositoryImpl](file:///C:/Users/froke/.gemini/antigravity-ide/scratch/android_exam_solutions/Problem1_Expense_CSV/app/src/main/java/com/example/expensetracker/data/repository/TransactionRepositoryImpl.kt) (`FileOutputStream(file, true)`) | **4** |
| **3. การคำนวณและแสดงผล (Business Logic)** | คำนวณยอดสุทธิ (รับ - จ่าย) ถูกต้อง / LazyColumn แสดงชื่อ, วันที่, เวลา, จำนวนเงินแยกสี | [TransactionContent](file:///C:/Users/froke/.gemini/antigravity-ide/scratch/android_exam_solutions/Problem1_Expense_CSV/app/src/main/java/com/example/expensetracker/ui/screen/TransactionContent.kt) | **3** |
| **4. UI ด้วย Jetpack Compose** | Layout ตรงตามภาพ / RadioButton, OutlinedTextField, Card, Button ถูกต้องตามหลัก State Hoisting | [TransactionScreen](file:///C:/Users/froke/.gemini/antigravity-ide/scratch/android_exam_solutions/Problem1_Expense_CSV/app/src/main/java/com/example/expensetracker/ui/screen/TransactionScreen.kt) & [TransactionContent](file:///C:/Users/froke/.gemini/antigravity-ide/scratch/android_exam_solutions/Problem1_Expense_CSV/app/src/main/java/com/example/expensetracker/ui/screen/TransactionContent.kt) | **2** |
| **5. Thread Safety (Coroutines)** | `Dispatchers.IO` สำหรับ File I/O / `viewModelScope.launch` เบื้องหลัง ไม่เกิด UI Freeze | มีการครอบ `withContext(Dispatchers.IO)` ทุกฟังก์ชัน I/O | **2** |
| **รวม** | | | **15** |

---

### 🔵 ข้อที่ 2: ระบบจัดการสต็อกสินค้าชุมชนบน Cloud Database (Supabase) (15 คะแนน)
| เกณฑ์ | รายละเอียด | จุดตรวจในโค้ด | คะแนน |
|---|---|---|:---:|
| **1. ความครบถ้วนของ CRUD** | Create (Insert แถวใหม่) / Read (ดึงรายการเรียงล่าสุด) / Update (ปรับสต็อก ±1 ทันที) / Delete (ลบตาม ID) | [ProductRepositoryImpl](file:///C:/Users/froke/.gemini/antigravity-ide/scratch/android_exam_solutions/Problem2_Community_Stock_Supabase/app/src/main/java/com/example/communitystock/data/repository/ProductRepositoryImpl.kt) | **5** |
| **2. สถาปัตยกรรม MVVM & Repository** | แยกเลเยอร์ชัดเจน / ไม่เรียก SDK ตรงใน UI / หมุนจอแล้ว State ไม่หาย | [ProductViewModel](file:///C:/Users/froke/.gemini/antigravity-ide/scratch/android_exam_solutions/Problem2_Community_Stock_Supabase/app/src/main/java/com/example/communitystock/ui/viewmodel/ProductViewModel.kt) | **4** |
| **3. การสร้าง UI ด้วย Jetpack Compose** | ฟอร์ม OutlinedTextField, Button, LazyColumn, Card สวยงาม / State Hoisting | [ProductStockContent](file:///C:/Users/froke/.gemini/antigravity-ide/scratch/android_exam_solutions/Problem2_Community_Stock_Supabase/app/src/main/java/com/example/communitystock/ui/screen/ProductStockContent.kt) | **3** |
| **4. Asynchronous & Coroutines** | `viewModelScope.launch` และ Network I/O บน `Dispatchers.IO` ลื่นไหลไม่กระตุก | มีการจัดการ Network Request เบื้องหลังทั้งหมด | **3** |
| **รวม** | | | **15** |

---

## 🚀 ขั้นตอนการนำโปรเจกต์ไปเปิดและรันใน Android Studio

### 🔹 สำหรับข้อที่ 1 (CSV App):
1. เปิด **Android Studio** -> เลือก **New Project** -> **Empty Activity (Compose)**
2. ตั้ง Package Name เป็น: `com.example.expensetracker`
3. คัดลอกโฟลเดอร์และไฟล์จาก [Problem1_Expense_CSV](file:///C:/Users/froke/.gemini/antigravity-ide/scratch/android_exam_solutions/Problem1_Expense_CSV) ไปทับในโปรเจกต์:
   - `Transaction.kt` -> วางที่ `data/model/`
   - `TransactionRepository.kt` & `TransactionRepositoryImpl.kt` -> วางที่ `data/repository/`
   - `TransactionViewModel.kt` -> วางที่ `ui/viewmodel/`
   - `TransactionScreen.kt` & `TransactionContent.kt` -> วางที่ `ui/screen/`
   - `MainActivity.kt` -> วางที่ root package
4. กด **Sync Project with Gradle Files** และกดปุ่ม **Run App** บน Emulator

---

### 🔹 สำหรับข้อที่ 2 (Supabase App):
1. **ตั้งค่าฐานข้อมูล Supabase (ใช้เวลา 1 นาที)**:
   - เข้า [https://supabase.com](https://supabase.com) เปิดโปรเจกต์ของคุณ
   - ไปที่เมนู **SQL Editor** -> กด **New query**
   - คัดลอกโค้ดจาก [supabase_schema.sql](file:///C:/Users/froke/.gemini/antigravity-ide/scratch/android_exam_solutions/Problem2_Community_Stock_Supabase/supabase_schema.sql) ไปวางแล้วกด **Run**
   - ไปที่ **Project Settings -> API** คัดลอก **Project URL** และ **Project API Keys (anon public)**
2. **ใส่ API Key ในโปรเจกต์ Android**:
   - เปิดไฟล์ [SupabaseClientProvider.kt](file:///C:/Users/froke/.gemini/antigravity-ide/scratch/android_exam_solutions/Problem2_Community_Stock_Supabase/app/src/main/java/com/example/communitystock/data/remote/SupabaseClientProvider.kt)
   - วางค่า `SUPABASE_URL` และ `SUPABASE_ANON_KEY` ที่ได้จากข้อ 1
3. สร้างโปรเจกต์ Android Package `com.example.communitystock` แล้วคัดลอกไฟล์ทั้งหมดจาก [Problem2_Community_Stock_Supabase](file:///C:/Users/froke/.gemini/antigravity-ide/scratch/android_exam_solutions/Problem2_Community_Stock_Supabase) ไปวาง
4. ตรวจสอบว่าใน `AndroidManifest.xml` มี `<uses-permission android:name="android.permission.INTERNET" />`
5. กด **Sync Project with Gradle Files** และ **Run App**

---

## 🎯 แนวทางการสาธิตต่อหน้าผู้สอน (Live Demo Walkthrough)

### การสาธิตข้อ 1 (วันที่ 11 ตค. 9:00 น.):
1. **การบันทึกข้อมูลและตรวจสอบไฟล์ CSV**:
   - กรอกข้อมูลชื่อรายการ เช่น "parttime", จำนวนเงิน "1000", เลือก "รายรับ" -> กด "บันทึกข้อมูล"
   - กรอกข้อมูลชื่อรายการ "food", จำนวนเงิน "500", เลือก "รายจ่าย" -> กด "บันทึกข้อมูล"
   - ใน Android Studio เปิดแถบ **View -> Tool Windows -> Device File Explorer**
   - ไปที่พาธ: `/data/data/com.example.expensetracker/files/transactions.csv`
   - ดับเบิ้ลคลิกเปิดไฟล์ จะเห็นข้อมูลที่ถูก Append ตามจริง
2. **การทดสอบการหมุนหน้าจอ**:
   - กดปุ่ม Rotate หน้าจอของ Emulator หมุนเป็นแนวนอนและแนวตั้ง
   - ชี้ให้อาจารย์เห็นว่ายอดคงเหลือสุทธิและรายการประวัติยังคงอยู่ครบถ้วน ไม่หายไป (ด้วยอานิสงส์ของ ViewModel)
3. **การลบข้อมูล**:
   - กดปุ่ม "ล้างข้อมูล" -> รายการหน้าจอเคลียร์หมด และไฟล์ `transactions.csv` ใน Device Explorer จะถูกลบจริง

### การสาธิตข้อ 2:
1. **Live Demo ควบคู่ Table Editor**:
   - เปิดหน้าจอเบราว์เซอร์ Supabase **Table Editor -> ตาราง products** ไว้ข้างๆ จอ Emulator
   - **Create**: กรอกชื่อสินค้า จำนวน ราคา ในแอป แล้วกดบันทึก -> แสดงให้อาจารย์เห็นว่าแถวใหม่โผล่บน Supabase ทันที
   - **Update**: กดปุ่ม `+1` หรือ `-1` บนการ์ดสินค้าในแอป -> ค่าในคอลัมน์ `stock_qty` บน Supabase จะเปลี่ยนตามทันที
   - **Delete**: กดปุ่มถังขยะสีแดง -> แถวข้อมูลในตารางบน Supabase จะถูกลบทันที
2. **ทดสอบหมุนหน้าจอ**:
   - หมุนจอ Emulator เพื่อยืนยันว่า State ของ ViewModel ไม่ถูกทำลาย
