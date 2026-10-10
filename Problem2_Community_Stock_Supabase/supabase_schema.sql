-- ====================================================================
-- สคริปต์สร้างตารางสำหรับข้อที่ 2: ระบบจัดการสต็อกสินค้าชุมชน (Supabase CRUD)
-- นำโค้ดนี้ไปรันในแท็บ "SQL Editor" บน Supabase Dashboard
-- ====================================================================

-- 1. สร้างตาราง products ตามข้อกำหนดของโจทย์
create table if not exists products (
    id bigint primary key generated always as identity,
    product_name text not null,
    stock_qty int not null default 0,
    price double precision not null default 0.0,
    created_at timestamp with time zone default timezone('utc'::text, now()) not null
);

-- 2. ปิด RLS หรือเปิดสิทธิ์ Anon สำหรับการทดสอบ (ตามข้อกำหนดของโจทย์)
alter table products enable row level security;

-- สร้าง Policy ให้ผู้ใช้ทั่วไป (Anon Key) สามารถ Select, Insert, Update, Delete ได้อย่างสมบูรณ์
create policy "Allow all public operations" on products
    for all
    using (true)
    with check (true);

-- (ตัวเลือก) ข้อมูลตัวอย่างเริ่มต้นสำหรับการทดสอบ
insert into products (product_name, stock_qty, price) values
    ('ข้าวหอมมะลิชุมชน 1 กก.', 20, 45.0),
    ('กล้วยตากอบน้ำผึ้ง', 15, 35.0),
    ('น้ำผึ้งป่าแท้ 500 มล.', 8, 120.0);
