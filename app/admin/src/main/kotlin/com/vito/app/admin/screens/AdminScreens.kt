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

enum class AdminScreen {
    DASHBOARD, QR, USERS, DOCUMENTS, LIVE, MART, FINANCE, AUDIT, CHAT
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable fun AdminMainScreen() {
    var currentScreen by remember { mutableStateOf(AdminScreen.DASHBOARD) }
    
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Vito Admin") },
                navigationIcon = {
                    IconButton(onClick = { /* toggle drawer */ }) { Icon(Icons.Default.Menu, null) }
                }
            )
        }
    ) { padding ->
        Column(Modifier.fillMaxSize().padding(padding)) {
            ScrollableTabRow(
                selectedTabIndex = currentScreen.ordinal,
                modifier = Modifier.fillMaxWidth()
            ) {
                AdminScreen.entries.forEach { screen ->
                    Tab(
                        selected = currentScreen == screen,
                        onClick = { currentScreen = screen },
                        text = { Text(screen.name.replace("_", " ")) }
                    )
                }
            }
            
            Box(Modifier.weight(1f)) {
                when (currentScreen) {
                    AdminScreen.DASHBOARD -> DashboardScreen()
                    AdminScreen.QR -> QrManagementScreen()
                    AdminScreen.USERS -> UserManagementScreen()
                    AdminScreen.DOCUMENTS -> DocumentReviewScreen()
                    AdminScreen.LIVE -> LiveMonitorScreen()
                    AdminScreen.MART -> MartManagementScreen()
                    AdminScreen.FINANCE -> FinanceScreen()
                    AdminScreen.AUDIT -> AuditLogScreen()
                    AdminScreen.CHAT -> ChatMonitorScreen()
                }
            }
        }
    }
}

private data class NavItem(val label: String, val icon: ImageVector, val screen: AdminScreen)

private val items = listOf(
    NavItem("Dashboard", Icons.Default.Dashboard, AdminScreen.DASHBOARD),
    NavItem("QR Management", Icons.Default.QrCode, AdminScreen.QR),
    NavItem("Users", Icons.Default.People, AdminScreen.USERS),
    NavItem("Documents", Icons.Default.Description, AdminScreen.DOCUMENTS),
    NavItem("Live Monitor", Icons.Default.Map, AdminScreen.LIVE),
    NavItem("Mart", Icons.Default.Store, AdminScreen.MART),
    NavItem("Finance", Icons.Default.Payment, AdminScreen.FINANCE),
    NavItem("Audit Log", Icons.Default.History, AdminScreen.AUDIT),
    NavItem("Chat Monitor", Icons.Default.Chat, AdminScreen.CHAT)
)

@Composable fun DashboardScreen() {
    var totalUsers by remember { mutableStateOf(1247) }
    var activeDrivers by remember { mutableStateOf(89) }
    var ridesToday by remember { mutableStateOf(456) }
    var revenueToday by remember { mutableStateOf(12450.00) }
    
    Column(Modifier.fillMaxSize().padding(16.dp)) {
        Text("Dashboard", style = MaterialTheme.typography.headlineMedium)
        Spacer(Modifier.height(16.dp))
        
        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            StatCard("Total Users", totalUsers.toString(), Icons.Default.People, Modifier.weight(1f))
            StatCard("Active Drivers", activeDrivers.toString(), Icons.Default.DirectionsCar, Modifier.weight(1f))
        }
        Spacer(Modifier.height(8.dp))
        
        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            StatCard("Rides Today", ridesToday.toString(), Icons.Default.Commute, Modifier.weight(1f))
            StatCard("Revenue", "$${String.format("%.2f", revenueToday)}", Icons.Default.Payment, Modifier.weight(1f))
        }
        
        Spacer(Modifier.height(24.dp))
        
        Card(Modifier.fillMaxWidth()) {
            Column(Modifier.padding(16.dp)) {
                Text("Recent Activity", style = MaterialTheme.typography.titleMedium)
                Spacer(Modifier.height(8.dp))
                LazyColumn {
                    items(listOf("Ride completed - $12.50", "New user registered", "Package delivered")) { activity ->
                        Text(activity, modifier = Modifier.padding(vertical = 4.dp))
                    }
                }
            }
        }
    }
}

@Composable fun QrManagementScreen() {
    var selectedRole by remember { mutableStateOf("client") }
    var batchCount by remember { mutableStateOf(5) }
    var generatedCodes by remember { mutableStateOf<List<String>>(emptyList()) }
    
    LaunchedEffect(Unit) {
        generatedCodes = listOf("QR-CLIENT-001", "QR-CLIENT-002", "QR-DRIVER-001")
    }
    
    Column(Modifier.fillMaxSize().padding(16.dp)) {
        Text("QR Code Generator", style = MaterialTheme.typography.headlineMedium)
        Spacer(Modifier.height(16.dp))
        
        Card(Modifier.fillMaxWidth()) {
            Column(Modifier.padding(16.dp)) {
                Text("Generate New QR Codes", style = MaterialTheme.typography.titleMedium)
                Spacer(Modifier.height(8.dp))
                
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text("Role:")
                    Spacer(Modifier.width(8.dp))
                    FilterChip(
                        selected = selectedRole == "client",
                        onClick = { selectedRole = "client" },
                        label = { Text("Client") }
                    )
                    Spacer(Modifier.width(4.dp))
                    FilterChip(
                        selected = selectedRole == "driver",
                        onClick = { selectedRole = "driver" },
                        label = { Text("Driver") }
                    )
                }
                Spacer(Modifier.height(8.dp))
                
                OutlinedTextField(
                    value = batchCount.toString(),
                    onValueChange = { batchCount = it.toIntOrNull() ?: 1 },
                    label = { Text("Batch Count") },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true
                )
                Spacer(Modifier.height(8.dp))
                
                Button(
                    onClick = {
                        val codes = (1..batchCount).map { "QR-${selectedRole.uppercase()}-${String.format("%03d", it)}" }
                        generatedCodes = codes
                    },
                    modifier = Modifier.fillMaxWidth()
                ) { Text("Generate") }
            }
        }
        
        Spacer(Modifier.height(16.dp))
        
        Text("Generated Codes (${generatedCodes.size})", style = MaterialTheme.typography.titleMedium)
        Spacer(Modifier.height(8.dp))
        
        LazyColumn {
            items(generatedCodes) { code ->
                Card(Modifier.fillMaxWidth().padding(vertical = 2.dp)) {
                    Row(Modifier.padding(12.dp), horizontalArrangement = Arrangement.SpaceBetween) {
                        Text(code)
                        IconButton(onClick = { /* copy */ }) { Icon(Icons.Default.ContentCopy, null) }
                    }
                }
            }
        }
    }
}

@Composable fun UserManagementScreen() {
    var searchQuery by remember { mutableStateOf("") }
    val users = remember {
        listOf(
            "user1" to "John Doe",
            "user2" to "Jane Smith",
            "driver1" to "Ahmed M.",
            "driver2" to "Fatima K."
        )
    }
    
    Column(Modifier.fillMaxSize().padding(16.dp)) {
        Text("User Management", style = MaterialTheme.typography.headlineMedium)
        Spacer(Modifier.height(16.dp))
        
        OutlinedTextField(
            value = searchQuery,
            onValueChange = { searchQuery = it },
            label = { Text("Search users") },
            modifier = Modifier.fillMaxWidth(),
            leadingIcon = { Icon(Icons.Default.Search, null) }
        )
        Spacer(Modifier.height(16.dp))
        
        LazyColumn {
            items(users) { (id, name) ->
                Card(Modifier.fillMaxWidth().padding(vertical = 4.dp)) {
                    Row(Modifier.padding(12.dp), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                        Column {
                            Text(name, style = MaterialTheme.typography.titleMedium)
                            Text(id, style = MaterialTheme.typography.bodySmall)
                        }
                        Row {
                            IconButton(onClick = { /* suspend */ }) { Icon(Icons.Default.Block, null) }
                            IconButton(onClick = { /* delete */ }) { Icon(Icons.Default.Delete, null) }
                        }
                    }
                }
            }
        }
    }
}

@Composable fun DocumentReviewScreen() {
    val pendingDocs = remember {
        listOf(
            Triple("driver1", "Ahmed M.", "Toyota Camry"),
            Triple("driver2", "Fatima K.", "Honda Civic")
        )
    }
    
    Column(Modifier.fillMaxSize().padding(16.dp)) {
        Text("Document Review", style = MaterialTheme.typography.headlineMedium)
        Spacer(Modifier.height(16.dp))
        
        Text("Pending (${pendingDocs.size})", style = MaterialTheme.typography.titleMedium)
        Spacer(Modifier.height(8.dp))
        
        LazyColumn {
            items(pendingDocs) { (id, name, vehicle) ->
                Card(Modifier.fillMaxWidth().padding(vertical = 4.dp)) {
                    Column(Modifier.padding(16.dp)) {
                        Text(name, style = MaterialTheme.typography.titleMedium)
                        Text("Vehicle: $vehicle")
                        Spacer(Modifier.height(8.dp))
                        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            Button(onClick = { /* approve */ }) { Text("Approve") }
                            OutlinedButton(onClick = { /* reject */ }) { Text("Reject") }
                        }
                    }
                }
            }
        }
    }
}

@Composable fun LiveMonitorScreen() {
    Column(Modifier.fillMaxSize().padding(16.dp)) {
        Text("Live Monitor", style = MaterialTheme.typography.headlineMedium)
        Spacer(Modifier.height(16.dp))
        
        Card(Modifier.fillMaxWidth()) {
            Column(Modifier.padding(16.dp)) {
                Text("Active Drivers: 12", style = MaterialTheme.typography.titleMedium)
                Text("Active Rides: 8", style = MaterialTheme.typography.titleMedium)
            }
        }
        
        Spacer(Modifier.height(16.dp))
        
        Card(Modifier.fillMaxWidth()) {
            Column(Modifier.padding(16.dp)) {
                Text("Live Map View", style = MaterialTheme.typography.titleMedium)
                Text("(Map placeholder)", style = MaterialTheme.typography.bodyMedium)
                Spacer(Modifier.height(8.dp))
                Row {
                    Icon(Icons.Default.LocationOn, null)
                    Text(" Driver 1 - En route")
                }
                Row {
                    Icon(Icons.Default.LocationOn, null)
                    Text(" Driver 2 - En route")
                }
            }
        }
    }
}

@Composable fun MartManagementScreen() {
    var selectedTab by remember { mutableStateOf(0) }
    val tabs = listOf("Products", "Orders", "Promotions")
    
    Column(Modifier.fillMaxSize().padding(16.dp)) {
        Text("Mart Management", style = MaterialTheme.typography.headlineMedium)
        Spacer(Modifier.height(16.dp))
        
        TabRow(selectedTabIndex = selectedTab) {
            tabs.forEachIndexed { index, title ->
                Tab(
                    selected = selectedTab == index,
                    onClick = { selectedTab = index },
                    text = { Text(title) }
                )
            }
        }
        
        Spacer(Modifier.height(16.dp))
        
        when (selectedTab) {
            0 -> ProductListTab()
            1 -> OrderListTab()
            2 -> PromotionListTab()
        }
    }
}

@Composable private fun ProductListTab() {
    val products = remember {
        listOf(
            "Cola Classic" to 200,
            "Chips" to 150,
            "Orange Juice" to 350
        )
    }
    
    LazyColumn {
        item {
            Button(onClick = { }, modifier = Modifier.fillMaxWidth()) { Text("Add Product") }
            Spacer(Modifier.height(8.dp))
        }
        items(products) { (name, price) ->
            Card(Modifier.fillMaxWidth().padding(vertical = 4.dp)) {
                Row(Modifier.padding(12.dp), horizontalArrangement = Arrangement.SpaceBetween) {
                    Text(name)
                    Text("$${price / 100.0}")
                }
            }
        }
    }
}

@Composable private fun OrderListTab() {
    val orders = remember {
        listOf("Order #123", "Order #124", "Order #125")
    }
    
    LazyColumn {
        items(orders) { order ->
            Card(Modifier.fillMaxWidth().padding(vertical = 4.dp)) {
                Row(Modifier.padding(12.dp), horizontalArrangement = Arrangement.SpaceBetween) {
                    Text(order)
                    Text("Delivered", style = MaterialTheme.typography.labelMedium)
                }
            }
        }
    }
}

@Composable private fun PromotionListTab() {
    val promos = remember {
        listOf("VITO20" to "20% off", "FIRST50" to "$5 off")
    }
    
    LazyColumn {
        item {
            Button(onClick = { }, modifier = Modifier.fillMaxWidth()) { Text("Create Promotion") }
            Spacer(Modifier.height(8.dp))
        }
        items(promos) { (code, desc) ->
            Card(Modifier.fillMaxWidth().padding(vertical = 4.dp)) {
                Row(Modifier.padding(12.dp), horizontalArrangement = Arrangement.SpaceBetween) {
                    Text(code)
                    Text(desc)
                }
            }
        }
    }
}

@Composable fun FinanceScreen() {
    var selectedPeriod by remember { mutableStateOf("today") }
    val earnings = remember {
        mapOf("today" to 12450.0, "week" to 85000.0, "month" to 340000.0)
    }
    
    Column(Modifier.fillMaxSize().padding(16.dp)) {
        Text("Finance", style = MaterialTheme.typography.headlineMedium)
        Spacer(Modifier.height(16.dp))
        
        Card(Modifier.fillMaxWidth()) {
            Column(Modifier.padding(16.dp)) {
                Text("Revenue", style = MaterialTheme.typography.titleMedium)
                Text(
                    "$${String.format("%.2f", earnings[selectedPeriod])}",
                    style = MaterialTheme.typography.headlineLarge
                )
            }
        }
        
        Spacer(Modifier.height(16.dp))
        
        Text("Cash-out Requests", style = MaterialTheme.typography.titleMedium)
        Spacer(Modifier.height(8.dp))
        
        Card(Modifier.fillMaxWidth()) {
            Row(Modifier.padding(12.dp), horizontalArrangement = Arrangement.SpaceBetween) {
                Column { Text("Ahmed M."); Text("$45.00", style = MaterialTheme.typography.labelMedium) }
                Button(onClick = { }) { Text("Approve") }
            }
        }
    }
}

@Composable fun AuditLogScreen() {
    val logs = remember {
        listOf(
            "User logged in - user1",
            "Ride completed - ride_001",
            "Payment received - $12.50",
            "Driver went online - driver1"
        )
    }
    
    Column(Modifier.fillMaxSize().padding(16.dp)) {
        Text("Audit Log", style = MaterialTheme.typography.headlineMedium)
        Spacer(Modifier.height(16.dp))
        
        LazyColumn {
            items(logs) { log ->
                Card(Modifier.fillMaxWidth().padding(vertical = 2.dp)) {
                    Text(log, modifier = Modifier.padding(12.dp))
                }
            }
        }
    }
}

@Composable fun ChatMonitorScreen() {
    var searchQuery by remember { mutableStateOf("") }
    val conversations = remember {
        listOf(
            "ride_001" to listOf("Hello", "On my way", "Thanks"),
            "pkg_001" to listOf("Package picked up", "Delivered")
        )
    }
    
    Column(Modifier.fillMaxSize().padding(16.dp)) {
        Text("Chat Monitor", style = MaterialTheme.typography.headlineMedium)
        Spacer(Modifier.height(16.dp))
        
        OutlinedTextField(
            value = searchQuery,
            onValueChange = { searchQuery = it },
            label = { Text("Search by trip ID") },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(Modifier.height(16.dp))
        
        LazyColumn {
            items(conversations.toList()) { (tripId, messages) ->
                Card(Modifier.fillMaxWidth().padding(vertical = 4.dp)) {
                    Column(Modifier.padding(12.dp)) {
                        Text(tripId, style = MaterialTheme.typography.titleMedium)
                        messages.forEach { Text("• $it", style = MaterialTheme.typography.bodySmall) }
                    }
                }
            }
        }
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

private fun Modifier.clickable(onClick: () -> Unit): Modifier = this