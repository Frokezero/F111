package com.example.combinedsolutions.feature.stock.data.remote

import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.createSupabaseClient
import io.github.jan.supabase.postgrest.Postgrest

/**
 * ตั้งค่าและถือครอง Instance ของ Supabase Client
 */
object SupabaseClientProvider {

    private const val SUPABASE_URL = "https://uhybdvidrrepetemzzty.supabase.co"
    private const val SUPABASE_ANON_KEY = "sb_publishable_n7jBbZb_hbu5qbERXm_bAQ_LwW-Vm5G"

    val client: SupabaseClient by lazy {
        createSupabaseClient(
            supabaseUrl = SUPABASE_URL,
            supabaseKey = SUPABASE_ANON_KEY
        ) {
            install(Postgrest)
        }
    }
}
