package com.vito.app.ui.main

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.vito.app.R
import com.vito.app.data.model.VehicleType
import com.vito.app.vm.*

@Composable
fun MainScreen(onLogout: () -> Unit = {}) {
    var selectedTab by remember { mutableStateOf(0) }
    val tabs = listOf(
        stringResource(R.string.nav_ride),
        stringResource(R.string.nav_send),
        stringResource(R.string.nav_mart),
        stringResource(R.string.nav_activity),
        stringResource(R.string.nav_profile)
    )
    
    Scaffold(
        bottomBar = {
            NavigationBar {
                tabs.forEachIndexed { index, title ->
                    NavigationBarItem(
                        icon = { Text(title.take(1)) },
                        label = { Text(title) },
                        selected = selectedTab == index,
                        onClick = { selectedTab = index }
                    )
                }
            }
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            when (selectedTab) {
                0 -> RideTabScreen()
                1 -> SendTabScreen()
                2 -> MartTabScreen()
                3 -> ActivityTabScreen()
                4 -> ProfileTabScreen(onLogout = onLogout)
            }
        }
    }
}

@Composable
fun RideTabScreen() {
    val vm: RideViewModel = viewModel()
    val fare by vm.fare.collectAsState()
    val isLoading by vm.isLoading.collectAsState()
    
    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        Text(
            stringResource(R.string.nav_ride),
            style = MaterialTheme.typography.headlineMedium
        )
        Spacer(modifier = Modifier.height(16.dp))
        
        Text(stringResource(R.string.where_to))
        Spacer(modifier = Modifier.height(8.dp))
        
        if (fare > 0) {
            Card(modifier = Modifier.fillMaxWidth()) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(stringResource(R.string.estimated_fare, "$${fare / 100.0}"))
                    Text("$${fare / 100.0}", style = MaterialTheme.typography.headlineSmall)
                }
            }
        }
        Spacer(modifier = Modifier.height(16.dp))
        
        Button(
            onClick = { vm.setPickup(37.7849, -122.4094); vm.setDropoff(37.7749, -122.4194) },
            modifier = Modifier.fillMaxWidth(),
            enabled = !isLoading
        ) {
            Text(stringResource(R.string.ride_now))
        }
    }
}

@Composable
fun SendTabScreen() {
    val vm: SendViewModel = viewModel()
    val isLoading by vm.isLoading.collectAsState()
    
    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        Text(stringResource(R.string.nav_send), style = MaterialTheme.typography.headlineMedium)
        Spacer(modifier = Modifier.height(16.dp))
        
        Button(
            onClick = { /* Request package */ },
            modifier = Modifier.fillMaxWidth(),
            enabled = !isLoading
        ) {
            Text(stringResource(R.string.send_package))
        }
    }
}

@Composable
fun MartTabScreen() {
    val vm: MartViewModel = viewModel()
    val products by vm.products.collectAsState()
    val cart by vm.cart.collectAsState()
    
    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
            Text(stringResource(R.string.nav_mart), style = MaterialTheme.typography.headlineMedium)
            Text("${stringResource(R.string.cart)} (${cart.size})")
        }
        Spacer(modifier = Modifier.height(16.dp))
        
        LazyColumn(modifier = Modifier.weight(1f)) {
            items(products) { product ->
                Card(modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp)) {
                    Row(modifier = Modifier.padding(12.dp), horizontalArrangement = Arrangement.SpaceBetween) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text(product.name, style = MaterialTheme.typography.titleMedium)
                            Text("$${product.price / 100.0}", style = MaterialTheme.typography.bodyMedium)
                        }
                        Button(onClick = { vm.addToCart(product) }) {
                            Text(stringResource(R.string.add))
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun ActivityTabScreen() {
    val vm: ActivityViewModel = viewModel()
    val activities by vm.activities.collectAsState()
    
    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        Text(stringResource(R.string.nav_activity), style = MaterialTheme.typography.headlineMedium)
        Spacer(modifier = Modifier.height(16.dp))
        
        LazyColumn {
            items(activities) { item ->
                Card(modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp)) {
                    Row(modifier = Modifier.padding(12.dp), horizontalArrangement = Arrangement.SpaceBetween) {
                        Column {
                            Text(item.title, style = MaterialTheme.typography.titleMedium)
                            Text(item.subtitle, style = MaterialTheme.typography.bodyMedium)
                        }
                        Text(item.status, style = MaterialTheme.typography.labelMedium)
                    }
                }
            }
        }
    }
}

@Composable
fun ProfileTabScreen(onLogout: () -> Unit) {
    val vm: AuthViewModel = viewModel()
    val user by vm.user.collectAsState()
    
    val walletBalance = user?.walletBalance ?: 0
    val alias = user?.alias ?: "User"
    
    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        Text(stringResource(R.string.nav_profile), style = MaterialTheme.typography.headlineMedium)
        Spacer(modifier = Modifier.height(16.dp))
        
        Card(modifier = Modifier.fillMaxWidth()) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(stringResource(R.string.alias), style = MaterialTheme.typography.labelMedium)
                Text(alias, style = MaterialTheme.typography.titleLarge)
            }
        }
        Spacer(modifier = Modifier.height(8.dp))
        
        Card(modifier = Modifier.fillMaxWidth()) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(stringResource(R.string.wallet_balance, "$${walletBalance / 100.0}"), style = MaterialTheme.typography.labelMedium)
                Spacer(modifier = Modifier.height(8.dp))
                Button(onClick = { /* Top up */ }, modifier = Modifier.fillMaxWidth()) {
                    Text(stringResource(R.string.top_up))
                }
            }
        }
        Spacer(modifier = Modifier.height(16.dp))
        
        Button(
            onClick = { onLogout() },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(stringResource(R.string.logout))
        }
    }
}