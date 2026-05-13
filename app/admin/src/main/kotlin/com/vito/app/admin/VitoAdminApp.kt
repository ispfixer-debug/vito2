package com.vito.app.admin

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
import com.vito.app.admin.screens.*

class VitoAdminApp : Activity() {
    override fun onCreate(b: Bundle?) {
        super.onCreate(b)
        setContentView(ComposeView(this).apply {
            setContent {
                MaterialTheme {
                    AdminMainScreen()
                }
            }
        })
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AdminMainScreen() {
    var selectedScreen by remember { mutableStateOf(0) }
    
    Scaffold { padding ->
        Row(Modifier.fillMaxSize().padding(padding)) {
            // Side nav
            NavigationRail {
                NavigationRailItem(icon = { Icon(Icons.Default.Dashboard, null) }, label = { Text("Dash") }, selected = selectedScreen == 0, onClick = { selectedScreen = 0 })
                NavigationRailItem(icon = { Icon(Icons.Default.QrCode, null) }, label = { Text("QR") }, selected = selectedScreen == 1, onClick = { selectedScreen = 1 })
                NavigationRailItem(icon = { Icon(Icons.Default.People, null) }, label = { Text("Users") }, selected = selectedScreen == 2, onClick = { selectedScreen = 2 })
                NavigationRailItem(icon = { Icon(Icons.Default.Description, null) }, label = { Text("Docs") }, selected = selectedScreen == 3, onClick = { selectedScreen = 3 })
                NavigationRailItem(icon = { Icon(Icons.Default.Map, null) }, label = { Text("Map") }, selected = selectedScreen == 4, onClick = { selectedScreen = 4 })
                NavigationRailItem(icon = { Icon(Icons.Default.Store, null) }, label = { Text("Mart") }, selected = selectedScreen == 5, onClick = { selectedScreen = 5 })
                NavigationRailItem(icon = { Icon(Icons.Default.Payment, null) }, label = { Text("Finance") }, selected = selectedScreen == 6, onClick = { selectedScreen = 6 })
                NavigationRailItem(icon = { Icon(Icons.Default.History, null) }, label = { Text("Audit") }, selected = selectedScreen == 7, onClick = { selectedScreen = 7 })
                NavigationRailItem(icon = { Icon(Icons.Default.Chat, null) }, label = { Text("Chat") }, selected = selectedScreen == 8, onClick = { selectedScreen = 8 })
            }
            
            // Content
            Box(Modifier.fillMaxSize().padding(16.dp)) {
                when (selectedScreen) {
                    0 -> DashboardScreen()
                    1 -> QrManagementScreen()
                    2 -> UserManagementScreen()
                    3 -> DocumentReviewScreen()
                    4 -> LiveMonitorScreen()
                    5 -> MartManagementScreen()
                    6 -> FinanceScreen()
                    7 -> AuditLogScreen()
                    8 -> ChatMonitorScreen()
                }
            }
        }
    }
}
