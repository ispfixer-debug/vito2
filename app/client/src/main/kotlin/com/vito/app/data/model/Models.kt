package com.vito.app.data.model

data class User(
    val id: String,
    val alias: String,
    val photoUrl: String? = null,
    val role: UserRole = UserRole.CLIENT,
    val walletBalance: Int = 0, // in cents
    val stripeCustomerId: String? = null,
    val isVerified: Boolean = false,
    val preferredPayment: PaymentMethod = PaymentMethod.CASH,
    val savedCards: List<SavedCard> = emptyList(),
    val createdAt: Long = System.currentTimeMillis()
)

enum class UserRole {
    CLIENT, DRIVER, ADMIN
}

enum class PaymentMethod {
    CASH, CARD, GOOGLE_PAY
}

data class SavedCard(
    val id: String,
    val brand: String,
    val last4: String,
    val expiryMonth: Int,
    val expiryYear: Int
)

data class DriverInfo(
    val id: String,
    val alias: String,
    val photoUrl: String? = null,
    val rating: Float = 0f,
    val vehicleModel: String,
    val vehiclePlate: String,
    val vehicleType: VehicleType,
    val isOnline: Boolean = false,
    val currentLat: Double = 0.0,
    val currentLng: Double = 0.0
)

enum class VehicleType {
    STANDARD, PREMIUM, BIKE, AUTO, VAN
}

data class RideRequest(
    val id: String,
    val clientId: String,
    val pickupAddress: String,
    val pickupLat: Double,
    val pickupLng: Double,
    val dropoffAddress: String,
    val dropoffLat: Double,
    val dropoffLng: Double,
    val stops: List<Stop> = emptyList(),
    val vehicleCategory: VehicleType = VehicleType.STANDARD,
    val scheduledTime: Long? = null,
    val promoCode: String? = null,
    val fareEstimate: Int = 0, // in cents
    val status: RideStatus = RideStatus.SEARCHING,
    val driverId: String? = null,
    val driver: DriverInfo? = null,
    val createdAt: Long = System.currentTimeMillis()
)

enum class RideStatus {
    SEARCHING, CONFIRMED, DRIVER_ARRIVING, IN_PROGRESS, COMPLETED, CANCELLED
}

data class Stop(
    val address: String,
    val lat: Double,
    val lng: Double,
    val order: Int
)

data class PackageRequest(
    val id: String,
    val clientId: String,
    val pickupAddress: String,
    val pickupLat: Double,
    val pickupLng: Double,
    val dropoffAddress: String,
    val dropoffLat: Double,
    val dropoffLng: Double,
    val size: PackageSize = PackageSize.SMALL,
    val weight: Float = 0f,
    val isFragile: Boolean = false,
    val description: String = "",
    val photoUrl: String? = null,
    val recipientType: RecipientType = RecipientType.IN_APP,
    val recipientAlias: String? = null,
    val hasInsurance: Boolean = false,
    val fareEstimate: Int = 0,
    val status: PackageStatus = PackageStatus.SEARCHING,
    val driverId: String? = null,
    val driver: DriverInfo? = null,
    val proofPhoto: String? = null,
    val signature: String? = null,
    val createdAt: Long = System.currentTimeMillis()
)

enum class PackageSize {
    SMALL, MEDIUM, LARGE
}

enum class RecipientType {
    IN_APP, OPEN_DELIVERY
}

enum class PackageStatus {
    SEARCHING, CONFIRMED, PICKED_UP, IN_TRANSIT, DELIVERED, CANCELLED
}

data class MartProduct(
    val id: String,
    val name: String,
    val description: String = "",
    val price: Int, // in cents
    val category: String,
    val imageUrl: String? = null,
    val stock: Int = 100
)

data class MartOrder(
    val id: String,
    val clientId: String,
    val items: List<MartCartItem>,
    val deliveryAddress: String,
    val deliveryLat: Double,
    val deliveryLng: Double,
    val deliveryFee: Int = 0,
    val total: Int,
    val paymentMethod: PaymentMethod = PaymentMethod.CASH,
    val status: MartOrderStatus = MartOrderStatus.PLACED,
    val driverId: String? = null,
    val driver: DriverInfo? = null,
    val createdAt: Long = System.currentTimeMillis()
)

data class MartCartItem(
    val productId: String,
    val productName: String,
    val quantity: Int,
    val unitPrice: Int,
    val imageUrl: String? = null
)

enum class MartOrderStatus {
    PLACED, PREPARING, DRIVER_PICKUP, DELIVERING, DELIVERED, CANCELLED
}

data class ChatMessage(
    val id: String,
    val chatId: String,
    val senderId: String,
    val senderAlias: String,
    val text: String = "",
    val imageUrl: String? = null,
    val timestamp: Long = System.currentTimeMillis(),
    val isRead: Boolean = false
)

data class ActivityItem(
    val id: String,
    val type: ActivityType,
    val title: String,
    val subtitle: String,
    val status: String,
    val timestamp: Long,
    val icon: String
)

enum class ActivityType {
    RIDE, PACKAGE, MART
}