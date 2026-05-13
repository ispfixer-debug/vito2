package com.vito.app.admin.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp

@Composable fun DashboardScreen() {
    Column(Modifier.fillMaxSize().padding(16.dp)) {
        Text("Dashboard", style = MaterialTheme.typography.headlineMedium)
    }
}

@Composable fun QrManagementScreen() {
    Column(Modifier.fillMaxSize().padding(16.dp)) {
        Text("QR Management", style = MaterialTheme.typography.headlineMedium)
    }
}

@Composable fun UserManagementScreen() {
    Column(Modifier.fillMaxSize().padding(16.dp)) {
        Text("Users", style = MaterialTheme.typography.headlineMedium)
    }
}

@Composable fun DocumentReviewScreen() {
    Column(Modifier.fillMaxSize().padding(16.dp)) {
        Text("Document Review", style = MaterialTheme.typography.headlineMedium)
    }
}

@Composable fun LiveMonitorScreen() {
    Column(Modifier.fillMaxSize().padding(16.dp)) {
        Text("Live Monitor", style = MaterialTheme.typography.headlineMedium)
    }
}

@Composable fun MartManagementScreen() {
    Column(Modifier.fillMaxSize().padding(16.dp)) {
        Text("Mart Management", style = MaterialTheme.typography.headlineMedium)
    }
}

@Composable fun FinanceScreen() {
    Column(Modifier.fillMaxSize().padding(16.dp)) {
        Text("Finance", style = MaterialTheme.typography.headlineMedium)
    }
}

@Composable fun AuditLogScreen() {
    Column(Modifier.fillMaxSize().padding(16.dp)) {
        Text("Audit Log", style = MaterialTheme.typography.headlineMedium)
    }
}

@Composable fun ChatMonitorScreen() {
    Column(Modifier.fillMaxSize().padding(16.dp)) {
        Text("Chat Monitor", style = MaterialTheme.typography.headlineMedium)
    }
}

@Composable private fun StatCard(title: String, value: String, icon: ImageVector, modifier: Modifier = Modifier) {
    Card(modifier = modifier) {
        Column(modifier = Modifier.padding(16.dp)) {
            Icon(imageVector = icon, contentDescription = null, modifier = Modifier.size(24.dp))
            Spacer(modifier = Modifier.height(8.dp))
            Text(text = value, style = MaterialTheme.typography.headlineSmall)
            Text(text = title, style = MaterialTheme.typography.bodySmall)
        }
    }
}