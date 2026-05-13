package com.vito.app.driver

import android.app.Activity
import android.os.Bundle
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.unit.dp

class VitoDriverApp : Activity() {
    override fun onCreate(b: Bundle?) {
        super.onCreate(b)
        setContentView(ComposeView(this).apply {
            setContent {
                MaterialTheme {
                    DriverMainScreen()
                }
            }
        })
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DriverMainScreen() {
    var isOnline by remember { mutableStateOf(false) }
    var currentScreen by remember { mutableStateOf("home") }
    var hasActiveJob by remember { mutableStateOf(false) }
    
    if (hasActiveJob) {
        AlertDialog(
            onDismissRequest = { },
            title = { Text("New Ride Request") },
            text = { Text("Pickup: Main Street 123\nDropoff: Airport\nFare: $24.50") },
            confirmButton = { Button(onClick = { hasActiveJob = false; currentScreen = "ride" }) { Text("Accept") } },
            dismissButton = { OutlinedButton(onClick = { hasActiveJob = false }) { Text("Decline") } }
        )
    }
    
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Vito Driver") },
                actions = {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(if (isOnline) "Online" else "Offline")
                        Switch(checked = isOnline, onCheckedChange = { isOnline = it }, modifier = Modifier.padding(end = 8.dp))
                    }
                }
            )
        }
    ) { padding ->
        Column(Modifier.fillMaxSize().padding(padding).padding(16.dp)) {
            Text("Current Screen: $currentScreen", style = MaterialTheme.typography.headlineMedium)
            Spacer(Modifier.height(24.dp))
            Button(onClick = { hasActiveJob = true }, modifier = Modifier.fillMaxWidth()) { Text("Simulate Job Alert") }
            Spacer(Modifier.height(24.dp))
            NavigationBar {
                NavigationBarItem(icon = { Icon(Icons.Default.Home, null) }, label = { Text("Home") }, selected = currentScreen == "home", onClick = { currentScreen = "home" })
                NavigationBarItem(icon = { Icon(Icons.Default.DirectionsCar, null) }, label = { Text("Ride") }, selected = currentScreen == "ride", onClick = { currentScreen = "ride" })
                NavigationBarItem(icon = { Icon(Icons.Default.Payment, null) }, label = { Text("Earnings") }, selected = currentScreen == "earnings", onClick = { currentScreen = "earnings" })
                NavigationBarItem(icon = { Icon(Icons.Default.Person, null) }, label = { Text("Profile") }, selected = currentScreen == "profile", onClick = { currentScreen = "profile" })
            }
        }
    }
}
