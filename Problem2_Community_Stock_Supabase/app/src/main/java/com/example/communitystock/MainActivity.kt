package com.example.communitystock

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import com.example.communitystock.data.remote.SupabaseClientProvider
import com.example.communitystock.data.repository.ProductRepositoryImpl
import com.example.communitystock.ui.screen.ProductStockScreen
import com.example.communitystock.ui.viewmodel.ProductViewModel

class MainActivity : ComponentActivity() {

    // ViewModel Retained เมื่อหน้าจอหมุน (Configuration Change)
    private val viewModel: ProductViewModel by viewModels {
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
                    ProductStockScreen(viewModel = viewModel)
                }
            }
        }
    }
}
