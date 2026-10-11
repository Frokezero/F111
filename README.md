# 📱 รายงานและซอร์สโค้ดส่งงานข้อสอบปฏิบัติการพัฒนา Android ด้วย Kotlin

### 👤 ข้อมูลนักศึกษา
- **ชื่อ-นามสกุล:** นายกฤษฎา ศรีคิลิน
- **รหัสนักศึกษา:** 67102122111
- **รายวิชา:** การปฏิบัติการพัฒนา Android ด้วย Kotlin (Android Mobile Development)
- **วันเวลาที่นำเสนอ:** วันที่ 11 ตุลาคม 2569 เวลา 9:00 น. เป็นต้นไป (ห้องพักครู ชั้น 3 อาคาร 7)
- **GitHub Repository:** [https://github.com/Frokezero/F111.git](https://github.com/Frokezero/F111.git)

---

## 🎥 วิดีโอสาธิตการทำงาน (Animated GIFs)

| ข้อที่ 1: บันทึกรายรับ-รายจ่าย (.CSV) | ข้อที่ 2: สต็อกสินค้าชุมชน (Supabase) |
|:---:|:---:|
| ![Problem 1 Demo](problem1_demo.gif) | ![Problem 2 Demo](problem2_demo.gif) |

---

## 📌 สรุปสาระสำคัญของโปรเจกต์ (Exam Solutions Overview)

โปรเจกต์นี้เป็นการรวมศูนย์และพัฒนาแอปพลิเคชัน Android ที่สมบูรณ์ตามโจทย์ข้อสอบปฏิบัติ 2 ข้อหลัก โดยใช้สถาปัตยกรรม **MVVM (Model-View-ViewModel)**, **Repository Pattern**, **Coroutines (`Dispatchers.IO`)**, และ **Jetpack Compose (Material 3)** ที่รองรับ **Adaptive Responsive Layout (หมุนจอเป็นแนวนอนแบ่ง 2 คอลัมน์)**:

1. **ข้อที่ 1: ระบบบันทึกรายรับ-รายจ่ายส่วนบุคคลด้วยไฟล์ CSV (.CSV) [15 คะแนนเต็ม]**
   - คำนวณยอดคงเหลือสุทธิ (รายรับ - รายจ่าย) แบบ Real-time
   - บันทึก อ่าน แยกคอลัมน์ และลบไฟล์ `transactions.csv` บน Internal App Storage
   - เพิ่มระบบ **ส่งออกไฟล์ CSV จริง (Export CSV)** ไปยังโฟลเดอร์สาธารณะ `/sdcard/Download/transactions_exported.csv` บนตัวเครื่อง

2. **ข้อที่ 2: ระบบจัดการสต็อกสินค้าชุมชนบน Cloud Database (Supabase) [15 คะแนนเต็ม]**
   - รองรับฟังก์ชัน CRUD ครบวงจร: **C**reate (เพิ่มสินค้า), **R**ead (ดึงรายการเรียงล่าสุด `id DESC`), **U**pdate (ปรับสต็อก $\pm 1$), **D**elete (ลบตาม ID)
   - ติดต่อ Supabase Cloud Database ผ่าน Kotlin Postgrest SDK + Ktor + Kotlinx Serialization
   - มีระบบ **Graceful Fallback / Interactive Local Demo Mode** สำหรับทดสอบ UI ได้อย่างไร้ข้อผิดพลาดแม้ยินยังไม่ได้ใส่ Supabase Key

---

## 📂 โครงสร้างโฟลเดอร์ใน Repository

```
android_exam_solutions/
├── app/                        # [แอปรวมหลัก] รองรับ Navigation Bar สลับหน้า ข้อ 1 และ ข้อ 2
│   └── src/main/java/com/example/combinedsolutions/
│       ├── MainActivity.kt     # ทางเข้าหลักพร้อม Bottom Navigation
│       ├── feature/expense/    # ซอร์สโค้ด ข้อที่ 1 (CSV Expense Tracker)
│       └── feature/stock/      # ซอร์สโค้ด ข้อที่ 2 (Supabase Community Stock)
├── Problem1_Expense_CSV/       # [โปรเจกต์แยก ข้อ 1] สำหรับเปิดตรงใน Android Studio
├── Problem2_Community_Stock_Supabase/ # [โปรเจกต์แยก ข้อ 2] สำหรับเปิดตรงใน Android Studio
├── problem1_demo.gif           # GIF สาธิตการทำงาน ข้อ 1
├── problem2_demo.gif           # GIF สาธิตการทำงาน ข้อ 2
├── MASTER_GUIDE.md             # คู่มือคำอธิบายซอร์สโค้ดอย่างละเอียดและสคริปต์การนำเสนอ
└── README.md                   # เอกสารข้อมูลการส่งงานนักศึกษา
```

---

## 🎬 ขั้นตอนการสาธิตต่อหน้าผู้สอน (Live Demo Walkthrough)

### 🟢 สาธิตข้อที่ 1 (CSV Expense Tracker):
1. เปิดแอป เลือกแท็บ **`ข้อ 1: CSV`**
2. **ทดสอบเพิ่มรายการ:**
   - กรอกชื่อรายการ "เงินเดือน", จำนวนเงิน "20000", เลือกประเภท "รายรับ" -> กด "บันทึก"
   - กรอกชื่อรายการ "ค่าอาหาร", จำนวนเงิน "1500", เลือกประเภท "รายจ่าย" -> กด "บันทึก"
   - ชี้ให้อาจารย์เห็นยอดคงเหลือสุทธิคำนวณถูกต้องอัตโนมัติ (`฿ 18,500.00`)
3. **ทดสอบส่งออกไฟล์ CSV:**
   - กดปุ่ม **"ส่งออก CSV"** -> แสดงข้อความแจ้งเตือนบันทึกไฟล์ไปที่ `/sdcard/Download/transactions_exported.csv`
4. **ตรวจสอบไฟล์ใน Device File Explorer:**
   - เปิด **View -> Tool Windows -> Device File Explorer** ใน Android Studio
   - ชี้ตำแหน่งไฟล์ Internal Storage (`/data/data/com.example.combinedsolutions/files/transactions.csv`) และ External Download (`/sdcard/Download/transactions_exported.csv`)
5. **ทดสอบหมุนหน้าจอ (Configuration Change):**
   - หมุนจอ Emulator เป็นแนวนอน (Landscape) แสดงผลแบบ **2-Column Split** สวยงาม และข้อมูลยอดเงินคงครบถ้วน State ไม่สูญหาย
6. **ทดสอบล้างข้อมูล:**
   - กดปุ่ม "ล้างข้อมูล" -> รายการหน้าจอเคลียร์หมด และไฟล์ CSV บนเครื่องถูกลบจริง

### 🔵 สาธิตข้อที่ 2 (Supabase Community Stock):
1. เปิดแอป เลือกแท็บ **`ข้อ 2: Supabase`**
2. **ทดสอบระบบ CRUD ร่วมกับ Supabase Table Editor:**
   - เปิดเบราว์เซอร์หน้า Supabase Table Editor ตาราง `products` ไว้คู่กับ Emulator
   - **Create:** กรอกชื่อสินค้า "ผ้ามัดย้อม", สต็อก "25", ราคา "350" -> กด "บันทึกขึ้น Supabase" -> โชว์แถวใหม่โผล่บน Supabase
   - **Update:** กดปุ่ม `+1` หรือ `-1` บนการ์ดสินค้าในแอป -> ค่า `stock_qty` บน Supabase อัปเดตทันที
   - **Delete:** กดปุ่มถังขยะสีแดง -> แถวสินค้าบน Supabase ถูกลบออกทันที
3. **ทดสอบหมุนหน้าจอ:** หมุนจอเป็นแนวนอนโชว์ 2-Column Responsive Layout
