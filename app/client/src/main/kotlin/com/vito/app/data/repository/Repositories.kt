package com.vito.app.data.repository

import com.vito.app.data.model.*
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

interface AuthRepository {
    suspend fun login(username: String, password: String): Result<User>
    suspend fun logout()
    fun getCurrentUser(): Flow<User?>
    fun isLoggedIn(): Boolean
}

class MockAuthRepository : AuthRepository {
    private val _currentUser = MutableStateFlow<User?>(null)
    
    override suspend fun login(username: String, password: String): Result<User> {
        delay(1000) // Simulate network delay
        return if (username == "oussama" && password == "oussama") {
            val user = User(
                id = "user1",
                alias = "Oussama",
                role = UserRole.CLIENT,
                walletBalance = 2500,
                preferredPayment = PaymentMethod.CASH,
                savedCards = listOf(
                    SavedCard("card1", "Visa", "4242", 12, 2025),
                    SavedCard("card2", "Mastercard", "8888", 6, 2026)
                )
            )
            _currentUser.value = user
            Result.success(user)
        } else {
            Result.failure(Exception("Invalid credentials"))
        }
    }
    
    override suspend fun logout() {
        _currentUser.value = null
    }
    
    override fun getCurrentUser(): Flow<User?> = _currentUser.asStateFlow()
    
    override fun isLoggedIn(): Boolean = _currentUser.value != null
}

interface RideRepository {
    suspend fun estimateFare(pickupLat: Double, pickupLng: Double, dropoffLat: Double, dropoffLng: Double, category: VehicleType): Int
    suspend fun requestRide(ride: RideRequest): String
    fun observeRideStatus(rideId: String): Flow<RideStatus>
    fun searchNearbyDrivers(lat: Double, lng: Double): Flow<List<DriverInfo>>
}

class MockRideRepository : RideRepository {
    private val rides = MutableStateFlow<Map<String, RideRequest>>(emptyMap())
    private val mockDrivers = listOf(
        DriverInfo("driver1", "Ahmed", rating = 4.8f, vehicleModel = "Toyota Camry", vehiclePlate = "ABC 123", vehicleType = VehicleType.STANDARD, isOnline = true, currentLat = 37.7849, currentLng = -122.4094),
        DriverInfo("driver2", "Fatima", rating = 4.9f, vehicleModel = "Honda Civic", vehiclePlate = "XYZ 789", vehicleType = VehicleType.PREMIUM, isOnline = true, currentLat = 37.7851, currentLng = -122.4096),
        DriverInfo("driver3", "Omar", rating = 4.7f, vehicleModel = "Toyota Prius", vehiclePlate = "DEF 456", vehicleType = VehicleType.STANDARD, isOnline = true, currentLat = 37.7848, currentLng = -122.4092)
    )
    
    override suspend fun estimateFare(pickupLat: Double, pickupLng: Double, dropoffLat: Double, dropoffLng: Double, category: VehicleType): Int {
        delay(500)
        val baseFare = when (category) {
            VehicleType.STANDARD -> 750
            VehicleType.PREMIUM -> 1200
            VehicleType.BIKE -> 500
            VehicleType.AUTO -> 900
            VehicleType.VAN -> 1500
        }
        return baseFare
    }
    
    override suspend fun requestRide(ride: RideRequest): String {
        delay(800)
        val rideId = "ride_${System.currentTimeMillis()}"
        val rideWithDriver = ride.copy(
            id = rideId,
            status = RideStatus.CONFIRMED,
            driverId = "driver1",
            driver = mockDrivers.first()
        )
        rides.value = rides.value + (rideId to rideWithDriver)
        return rideId
    }
    
    override fun observeRideStatus(rideId: String): Flow<RideStatus> {
        return MutableStateFlow(RideStatus.CONFIRMED)
    }
    
    override fun searchNearbyDrivers(lat: Double, lng: Double): Flow<List<DriverInfo>> {
        return MutableStateFlow(mockDrivers)
    }
}

interface SendRepository {
    suspend fun estimateFare(size: PackageSize, weight: Float, distance: Double): Int
    suspend fun requestPackage(req: PackageRequest): String
    fun observePackageStatus(packageId: String): Flow<PackageStatus>
}

class MockSendRepository : SendRepository {
    private val packages = MutableStateFlow<Map<String, PackageRequest>>(emptyMap())
    
    override suspend fun estimateFare(size: PackageSize, weight: Float, distance: Double): Int {
        delay(500)
        val baseFare = when (size) {
            PackageSize.SMALL -> 500
            PackageSize.MEDIUM -> 800
            PackageSize.LARGE -> 1200
        }
        return baseFare + (weight * 50).toInt()
    }
    
    override suspend fun requestPackage(req: PackageRequest): String {
        delay(800)
        val packageId = "pkg_${System.currentTimeMillis()}"
        packages.value = packages.value + (packageId to req.copy(id = packageId, status = PackageStatus.CONFIRMED))
        return packageId
    }
    
    override fun observePackageStatus(packageId: String): Flow<PackageStatus> {
        return MutableStateFlow(PackageStatus.CONFIRMED)
    }
}

interface MartRepository {
    fun getProducts(): Flow<List<MartProduct>>
    fun getProductsByCategory(category: String): Flow<List<MartProduct>>
    suspend fun placeOrder(order: MartOrder): String
    fun observeOrderStatus(orderId: String): Flow<MartOrderStatus>
}

class MockMartRepository : MartRepository {
    private val products = listOf(
        MartProduct("p1", "Cola Classic", "Refreshing cola", 200, "Drinks", stock = 50),
        MartProduct("p2", "Orange Juice", "Fresh orange juice", 350, "Drinks", stock = 30),
        MartProduct("p3", "Chips", "Salty potato chips", 150, "Snacks", stock = 100),
        MartProduct("p4", "Chocolate Bar", "Milk chocolate", 250, "Snacks", stock = 80),
        MartProduct("p5", " bottled water", "500ml water", 100, "Drinks", stock = 200),
        MartProduct("p6", "Energy Bar", "High energy snack", 300, "Snacks", stock = 60),
        MartProduct("p7", "Iced Tea", "Lemon iced tea", 280, "Drinks", stock = 40),
        MartProduct("p8", "Cookies", "Chocolate cookies", 380, "Snacks", stock = 25)
    )
    
    private val orders = MutableStateFlow<Map<String, MartOrder>>(emptyMap())
    
    override fun getProducts(): Flow<List<MartProduct>> {
        return MutableStateFlow(products)
    }
    
    override fun getProductsByCategory(category: String): Flow<List<MartProduct>> {
        return MutableStateFlow(products.filter { it.category == category })
    }
    
    override suspend fun placeOrder(order: MartOrder): String {
        delay(1000)
        val orderId = "order_${System.currentTimeMillis()}"
        orders.value = orders.value + (orderId to order.copy(id = orderId, status = MartOrderStatus.PLACED))
        return orderId
    }
    
    override fun observeOrderStatus(orderId: String): Flow<MartOrderStatus> {
        return MutableStateFlow(MartOrderStatus.PLACED)
    }
}

interface ChatRepository {
    fun getMessages(chatId: String): Flow<List<ChatMessage>>
    suspend fun sendMessage(chatId: String, senderId: String, senderAlias: String, text: String, imageUri: String? = null): String
    suspend fun markAsRead(chatId: String, messageId: String)
}

class MockChatRepository : ChatRepository {
    private val messages = MutableStateFlow<Map<String, List<ChatMessage>>>(emptyMap())
    
    override fun getMessages(chatId: String): Flow<List<ChatMessage>> {
        val existing = messages.value[chatId] ?: emptyList()
        return MutableStateFlow(existing)
    }
    
    override suspend fun sendMessage(chatId: String, senderId: String, senderAlias: String, text: String, imageUri: String?): String {
        delay(300)
        val msg = ChatMessage(
            id = "msg_${System.currentTimeMillis()}",
            chatId = chatId,
            senderId = senderId,
            senderAlias = senderAlias,
            text = text,
            imageUrl = imageUri,
            timestamp = System.currentTimeMillis()
        )
        val current = messages.value[chatId] ?: emptyList()
        messages.value = messages.value + (chatId to (current + msg))
        return msg.id
    }
    
    override suspend fun markAsRead(chatId: String, messageId: String) {
        val current = messages.value[chatId] ?: return
        val updated = current.map { if (it.id == messageId) it.copy(isRead = true) else it }
        messages.value = messages.value + (chatId to updated)
    }
}

interface ActivityRepository {
    fun getRecentActivity(userId: String): Flow<List<ActivityItem>>
}

class MockActivityRepository : ActivityRepository {
    override fun getRecentActivity(userId: String): Flow<List<ActivityItem>> {
        val items = listOf(
            ActivityItem("a1", ActivityType.RIDE, "Ride to Downtown", "Completed", "Completed", System.currentTimeMillis() - 3600000, "car"),
            ActivityItem("a2", ActivityType.MART, "Order from VitoMart", "Delivered", "Delivered", System.currentTimeMillis() - 7200000, "store"),
            ActivityItem("a3", ActivityType.PACKAGE, "Package sent", "Delivered", "Delivered", System.currentTimeMillis() - 10800000, "shipping"),
            ActivityItem("a4", ActivityType.RIDE, "Ride to Airport", "Cancelled", "Cancelled", System.currentTimeMillis() - 14400000, "car")
        )
        return MutableStateFlow(items)
    }
}