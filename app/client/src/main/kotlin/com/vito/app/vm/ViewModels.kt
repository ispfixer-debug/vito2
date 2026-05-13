package com.vito.app.vm

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.vito.app.data.model.*
import com.vito.app.data.repository.*
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class AuthViewModel(private val authRepo: AuthRepository = MockAuthRepository()) : ViewModel() {
    private val _user = MutableStateFlow<User?>(null)
    val user: StateFlow<User?> = _user.asStateFlow()
    
    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()
    
    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error.asStateFlow()
    
    init {
        viewModelScope.launch {
            authRepo.getCurrentUser().collect { _user.value = it }
        }
    }
    
    suspend fun login(username: String, password: String): Boolean {
        _isLoading.value = true
        _error.value = null
        return authRepo.login(username, password).fold(
            onSuccess = { _user.value = it; _isLoading.value = false; true },
            onFailure = { _error.value = it.message; _isLoading.value = false; false }
        )
    }
    
    suspend fun logout() {
        authRepo.logout()
        _user.value = null
    }
    
    fun isLoggedIn() = authRepo.isLoggedIn()
}

class RideViewModel(private val rideRepo: RideRepository = MockRideRepository()) : ViewModel() {
    private val _pickup = MutableStateFlow<Pair<Double, Double>?>(null)
    val pickup: StateFlow<Pair<Double, Double>?> = _pickup.asStateFlow()
    
    private val _dropoff = MutableStateFlow<Pair<Double, Double>?>(null)
    val dropoff: StateFlow<Pair<Double, Double>?> = _dropoff.asStateFlow()
    
    private val _stops = MutableStateFlow<List<Stop>>(emptyList())
    val stops: StateFlow<List<Stop>> = _stops.asStateFlow()
    
    private val _category = MutableStateFlow(VehicleType.STANDARD)
    val category: StateFlow<VehicleType> = _category.asStateFlow()
    
    private val _fare = MutableStateFlow(0)
    val fare: StateFlow<Int> = _fare.asStateFlow()
    
    private val _status = MutableStateFlow<RideStatus?>(null)
    val status: StateFlow<RideStatus?> = _status.asStateFlow()
    
    private val _driver = MutableStateFlow<DriverInfo?>(null)
    val driver: StateFlow<DriverInfo?> = _driver.asStateFlow()
    
    private val _drivers = MutableStateFlow<List<DriverInfo>>(emptyList())
    val nearbyDrivers: StateFlow<List<DriverInfo>> = _drivers.asStateFlow()
    
    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()
    
    fun setPickup(lat: Double, lng: Double) {
        _pickup.value = lat to lng
        estimateFare()
    }
    
    fun setDropoff(lat: Double, lng: Double) {
        _dropoff.value = lat to lng
        estimateFare()
    }
    
    fun addStop(stop: Stop) {
        _stops.value = _stops.value + stop.copy(order = _stops.value.size)
    }
    
    fun removeStop(index: Int) {
        _stops.value = _stops.value.toMutableList().apply { removeAt(index) }
    }
    
    fun setCategory(cat: VehicleType) {
        _category.value = cat
        estimateFare()
    }
    
    fun estimateFare() {
        val p = _pickup.value ?: return
        val d = _dropoff.value ?: return
        viewModelScope.launch {
            _fare.value = rideRepo.estimateFare(p.first, p.second, d.first, d.second, _category.value)
        }
    }
    
    fun searchDrivers() {
        val p = _pickup.value ?: return
        viewModelScope.launch {
            rideRepo.searchNearbyDrivers(p.first, p.second).collect { _drivers.value = it }
        }
    }
    
    suspend fun requestRide(clientId: String, pickupAddr: String, dropoffAddr: String): String? {
        val p = _pickup.value ?: return null
        val d = _dropoff.value ?: return null
        _isLoading.value = true
        val ride = RideRequest(
            id = "",
            clientId = clientId,
            pickupAddress = pickupAddr, pickupLat = p.first, pickupLng = p.second,
            dropoffAddress = dropoffAddr, dropoffLat = d.first, dropoffLng = d.second,
            stops = _stops.value, vehicleCategory = _category.value, fareEstimate = _fare.value
        )
        return rideRepo.requestRide(ride).also { _isLoading.value = false }
    }
    
    fun observeRide(rideId: String) {
        viewModelScope.launch {
            rideRepo.observeRideStatus(rideId).collect { _status.value = it }
        }
    }
}

class SendViewModel(private val sendRepo: SendRepository = MockSendRepository()) : ViewModel() {
    private val _package = MutableStateFlow<PackageRequest?>(null)
    val package_: StateFlow<PackageRequest?> = _package.asStateFlow()
    
    private val _fare = MutableStateFlow(0)
    val fare: StateFlow<Int> = _fare.asStateFlow()
    
    private val _status = MutableStateFlow<PackageStatus?>(null)
    val status: StateFlow<PackageStatus?> = _status.asStateFlow()
    
    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()
    
    fun estimateFare(size: PackageSize, weight: Float, distance: Double) { 
        viewModelScope.launch {
            _fare.value = sendRepo.estimateFare(size, weight, distance)
        }
    }
    
    suspend fun requestPackage(req: PackageRequest): String {
        _isLoading.value = true
        return sendRepo.requestPackage(req).also { _isLoading.value = false }
    }
    
    fun observePackage(packageId: String) {
        viewModelScope.launch {
            sendRepo.observePackageStatus(packageId).collect { _status.value = it }
        }
    }
}

class MartViewModel(private val martRepo: MartRepository = MockMartRepository()) : ViewModel() {
    private val _products = MutableStateFlow<List<MartProduct>>(emptyList())
    val products: StateFlow<List<MartProduct>> = _products.asStateFlow()
    
    private val _category = MutableStateFlow("All")
    val selectedCategory: StateFlow<String> = _category.asStateFlow()
    
    private val _cart = MutableStateFlow<List<MartCartItem>>(emptyList())
    val cart: StateFlow<List<MartCartItem>> = _cart.asStateFlow()
    
    private val _orderStatus = MutableStateFlow<MartOrderStatus?>(null)
    val orderStatus: StateFlow<MartOrderStatus?> = _orderStatus.asStateFlow()
    
    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()
    
    init {
        loadProducts()
    }
    
    fun loadProducts() {
        viewModelScope.launch {
            martRepo.getProducts().collect { _products.value = it }
        }
    }
    
    fun filterByCategory(cat: String) {
        _category.value = cat
        viewModelScope.launch {
            if (cat == "All") martRepo.getProducts()
            else martRepo.getProductsByCategory(cat)
            .collect { _products.value = it }
        }
    }
    
    fun addToCart(product: MartProduct, qty: Int = 1) {
        val current = _cart.value.toMutableList()
        val existing = current.indexOfFirst { it.productId == product.id }
        if (existing >= 0) {
            val item = current[existing]
            current[existing] = item.copy(quantity = item.quantity + qty)
        } else {
            current.add(MartCartItem(product.id, product.name, qty, product.price, product.imageUrl))
        }
        _cart.value = current
    }
    
    fun removeFromCart(productId: String) {
        _cart.value = _cart.value.filter { it.productId != productId }
    }
    
    fun updateQuantity(productId: String, qty: Int) {
        if (qty <= 0) { removeFromCart(productId); return }
        val current = _cart.value.toMutableList()
        val idx = current.indexOfFirst { it.productId == productId }
        if (idx >= 0) current[idx] = current[idx].copy(quantity = qty)
        _cart.value = current
    }
    
    fun cartTotal() = _cart.value.sumOf { it.unitPrice * it.quantity }
    
    fun cartCount() = _cart.value.sumOf { it.quantity }
    
    suspend fun placeOrder(clientId: String, addr: String, lat: Double, lng: Double, payment: PaymentMethod): String? {
        if (_cart.value.isEmpty()) return null
        _isLoading.value = true
        val total = cartTotal()
        val order = MartOrder(
            id = "", clientId = clientId, items = _cart.value,
            deliveryAddress = addr, deliveryLat = lat, deliveryLng = lng,
            deliveryFee = 300, total = total + 300, paymentMethod = payment
        )
        return martRepo.placeOrder(order).also { 
            _isLoading.value = false
            _cart.value = emptyList()
        }
    }
    
    fun observeOrder(orderId: String) {
        viewModelScope.launch {
            martRepo.observeOrderStatus(orderId).collect { _orderStatus.value = it }
        }
    }
}

class ChatViewModel(private val chatRepo: ChatRepository = MockChatRepository()) : ViewModel() {
    private val _messages = MutableStateFlow<List<ChatMessage>>(emptyList())
    val messages: StateFlow<List<ChatMessage>> = _messages.asStateFlow()
    
    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()
    
    fun loadMessages(chatId: String) {
        viewModelScope.launch {
            chatRepo.getMessages(chatId).collect { _messages.value = it }
        }
    }
    
    suspend fun sendMessage(chatId: String, senderId: String, senderAlias: String, text: String, imageUri: String? = null) {
        _isLoading.value = true
        chatRepo.sendMessage(chatId, senderId, senderAlias, text, imageUri)
        _isLoading.value = false
    }
    
    suspend fun markRead(chatId: String, messageId: String) {
        chatRepo.markAsRead(chatId, messageId)
    }
}

class ActivityViewModel(private val actRepo: MockActivityRepository = MockActivityRepository()) : ViewModel() {
    private val _activities = MutableStateFlow<List<ActivityItem>>(emptyList())
    val activities: StateFlow<List<ActivityItem>> = _activities.asStateFlow()
    
    private val _filter = MutableStateFlow(ActivityType.RIDE)
    val filter: StateFlow<ActivityType> = _filter.asStateFlow()
    
    fun loadActivities(userId: String) {
        viewModelScope.launch {
            actRepo.getRecentActivity(userId).collect { _activities.value = it }
        }
    }
    
    fun setFilter(type: ActivityType) {
        _filter.value = type
    }
    
    fun filteredActivities() = _activities.value.filter { it.type == _filter.value }
}