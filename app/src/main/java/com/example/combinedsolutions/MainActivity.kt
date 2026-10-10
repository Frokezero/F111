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
