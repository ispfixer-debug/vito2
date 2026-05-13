package com.vito.app.driver

import android.app.Activity
import android.os.Bundle
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.res.stringResource
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

enum class DriverScreen {
    HOME, RIDE, EARNINGS, PROFILE, QR_SCAN
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DriverMainScreen() {
    var isOnline by remember { mutableStateOf(false) }
    var currentScreen by remember { mutableStateOf(DriverScreen.HOME) }
    var hasActiveJob by remember { mutableStateOf(false) }
    var isVerified by remember { mutableStateOf(false) }
    var earnings by remember { mutableStateOf(12500) } // cents
    
    if (hasActiveJob) {
        JobAlertDialog(
            onAccept = { hasActiveJob = false; currentScreen = DriverScreen.RIDE },
            onDismiss = { hasActiveJob = false }
        )
    }
    
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Vito Driver") },
                actions = {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(if (isOnline) "Online" else "Offline")
                        Switch(
                            checked = isOnline,
                            onCheckedChange = { isOnline = it },
                            enabled = isVerified,
                            modifier = Modifier.padding(end = 8.dp)
                        )
                    }
                }
            )
        },
        bottomBar = {
            NavigationBar {
                NavigationBarItem(
                    icon = { Icon(Icons.Default.Home, null) },
                    label = { Text("Home") },
                    selected = currentScreen == DriverScreen.HOME,
                    onClick = { currentScreen = DriverScreen.HOME }
                )
                NavigationBarItem(
                    icon = { Icon(Icons.Default.DirectionsCar, null) },
                    label = { Text("Ride") },
                    selected = currentScreen == DriverScreen.RIDE,
                    onClick = { currentScreen = DriverScreen.RIDE }
                )
                NavigationBarItem(
                    icon = { Icon(Icons.Default.Payment, null) },
                    label = { Text("Earnings") },
                    selected = currentScreen == DriverScreen.EARNINGS,
                    onClick = { currentScreen = DriverScreen.EARNINGS }
                )
                NavigationBarItem(
                    icon = { Icon(Icons.Default.Person, null) },
                    label = { Text("Profile") },
                    selected = currentScreen == DriverScreen.PROFILE,
                    onClick = { currentScreen = DriverScreen.PROFILE }
                )
            }
        }
    ) { padding ->
        when (currentScreen) {
            DriverScreen.HOME -> DriverHomeTab(isOnline = isOnline, onSimulateAlert = { hasActiveJob = true })
            DriverScreen.RIDE -> DriverRideTab(isOnline = isOnline)
            DriverScreen.EARNINGS -> DriverEarningsTab(earnings = earnings)
            DriverScreen.PROFILE -> DriverProfileTab(isVerified = isVerified)
            DriverScreen.QR_SCAN -> QRScanScreen()
        }
    }
}

@Composable
fun JobAlertDialog(onAccept: () -> Unit, onDismiss: () -> Unit) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("New Ride Request") },
        text = {
            Column {
                Text("Pickup: 123 Main Street")
                Text("Dropoff: San Francisco Airport")
                Text("Fare: $24.50", style = MaterialTheme.typography.titleLarge)
                Text("Distance: 8.5 miles")
                Spacer(Modifier.height(8.dp))
                Text("Client: John D.", style = MaterialTheme.typography.labelMedium)
            }
        },
        confirmButton = { Button(onClick = onAccept) { Text("Accept") } },
        dismissButton = { OutlinedButton(onClick = onDismiss) { Text("Decline") } }
    )
}

@Composable
fun DriverHomeTab(isOnline: Boolean, onSimulateAlert: () -> Unit) {
    Column(Modifier.fillMaxSize().padding(16.dp)) {
        Text("Dashboard", style = MaterialTheme.typography.headlineMedium)
        Spacer(Modifier.height(16.dp))
        
        Card(Modifier.fillMaxWidth()) {
            Column(Modifier.padding(16.dp)) {
                Text("Status", style = MaterialTheme.typography.labelMedium)
                Text(
                    if (isOnline) "You're Online" else "Go Online to receive requests",
                    style = MaterialTheme.typography.titleMedium
                )
            }
        }
        
        Spacer(Modifier.height(16.dp))
        
        Button(
            onClick = onSimulateAlert,
            modifier = Modifier.fillMaxWidth(),
            enabled = isOnline
        ) { Text("Simulate Job Alert") }
        
        if (!isOnline) {
            Spacer(Modifier.height(8.dp))
            Text(
                "Go online to receive ride requests",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.error
            )
        }
    }
}

@Composable
fun DriverRideTab(isOnline: Boolean) {
    Column(Modifier.fillMaxSize().padding(16.dp)) {
        Text("Current Ride", style = MaterialTheme.typography.headlineMedium)
        Spacer(Modifier.height(16.dp))
        
        Card(Modifier.fillMaxWidth()) {
            Column(Modifier.padding(16.dp)) {
                Text("Pickup: 456 Oak Street")
                Text("Dropoff: Downtown Mall")
                Spacer(Modifier.height(8.dp))
                Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceEvenly) {
                    Button(onClick = { }) { Text("Arrived") }
                    Button(onClick = { }) { Text("Start Trip") }
                }
                Spacer(Modifier.height(8.dp))
                Button(
                    onClick = { },
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.errorContainer)
                ) { Text("Complete Trip") }
            }
        }
        
        Spacer(Modifier.height(16.dp))
        
        Button(onClick = { }, modifier = Modifier.fillMaxWidth()) { Text("Message Client") }
    }
}

@Composable
fun DriverEarningsTab(earnings: Int) {
    val today = earnings / 3
    val week = earnings
    val month = earnings * 4
    
    Column(Modifier.fillMaxSize().padding(16.dp)) {
        Text("Earnings", style = MaterialTheme.typography.headlineMedium)
        Spacer(Modifier.height(16.dp))
        
        Card(Modifier.fillMaxWidth()) {
            Column(Modifier.padding(16.dp)) {
                Text("Today's Total")
                Text("$${today / 100.0}", style = MaterialTheme.typography.headlineLarge)
            }
        }
        
        Spacer(Modifier.height(8.dp))
        
        Card(Modifier.fillMaxWidth()) {
            Column(Modifier.padding(16.dp)) {
                Text("This Week")
                Text("$${week / 100.0}", style = MaterialTheme.typography.headlineLarge)
            }
        }
        
        Spacer(Modifier.height(8.dp))
        
        Card(Modifier.fillMaxWidth()) {
            Column(Modifier.padding(16.dp)) {
                Text("This Month")
                Text("$${month / 100.0}", style = MaterialTheme.typography.headlineLarge)
            }
        }
        
        Spacer(Modifier.height(16.dp))
        
        Button(onClick = { }, modifier = Modifier.fillMaxWidth()) { Text("Request Payout") }
    }
}

@Composable
fun DriverProfileTab(isVerified: Boolean) {
    Column(Modifier.fillMaxSize().padding(16.dp)) {
        Text("Profile", style = MaterialTheme.typography.headlineMedium)
        Spacer(Modifier.height(16.dp))
        
        Card(Modifier.fillMaxWidth()) {
            Column(Modifier.padding(16.dp)) {
                Text("Alias: Ahmed")
                Text("Rating: 4.8 ★")
                Text("Vehicle: Toyota Camry (ABC 123)")
            }
        }
        
        Spacer(Modifier.height(8.dp))
        
        Card(Modifier.fillMaxWidth()) {
            Column(Modifier.padding(16.dp)) {
                Text("Verification Status", style = MaterialTheme.typography.labelMedium)
                if (isVerified) {
                    Text("Verified ✓", style = MaterialTheme.typography.titleMedium)
                } else {
                    Text("Pending Review", style = MaterialTheme.typography.titleMedium)
                }
            }
        }
    }
}

@Composable
fun QRScanScreen() {
    Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Icon(Icons.Default.QrCodeScanner, null, modifier = Modifier.size(64.dp))
            Spacer(Modifier.height(16.dp))
            Text("QR Scanner", style = MaterialTheme.typography.headlineMedium)
            Spacer(Modifier.height(8.dp))
            Text("Point camera at QR code")
        }
    }
}
