package com.example.communitystock.data.remote

import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.createSupabaseClient
import io.github.jan.supabase.postgrest.Postgrest

/**
 * ตั้งค่าและถือครอง Instance ของ Supabase Client
 * ให้นำ URL และ anon key จากหน้า Supabase Dashboard -> Project Settings -> API มาใส่ที่นี่
 */
object SupabaseClientProvider {

    // ⚠️ แทนที่ด้วยข้อมูลโปรเจกต์ Supabase ของคุณ
    private const val SUPABASE_URL = "https://YOUR_PROJECT_ID.supabase.co"
    private const val SUPABASE_ANON_KEY = "YOUR_SUPABASE_ANON_PUBLIC_KEY"

    val client: SupabaseClient by lazy {
        createSupabaseClient(
            supabaseUrl = SUPABASE_URL,
            supabaseKey = SUPABASE_ANON_KEY
        ) {
            install(Postgrest)
        }
    }
}
