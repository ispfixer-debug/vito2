# Vito - Multi-Service Platform

Vito is a native Android multi-service platform for rides, package delivery, and mart ordering.

## Apps

- **Vito Client** (`com.vito.app`) - Rider app
- **Vito Driver** (`com.vito.app.driver`) - Driver app  
- **Vito Admin** (`com.vito.app.admin`) - Admin dashboard

## Features

- QR Authentication (no phone/email)
- Ride Booking (VitoCar)
- Package Delivery (VitoSend)
- Mart Orders (VitoMart)
- Real-time Chat
- In-app Wallet
- Stripe/Google Pay support
- Biometric App Lock

## Architecture

- **Package**: `com.vito.*`
- **Language**: Kotlin 1.9.22
- **UI**: Jetpack Compose + Material 3
- **Backend**: Firebase Cloud Functions

## Build

```bash
./gradlew assembleDebug
```

## Files

```
app/
├── client/       # Vito Client app
├── driver/       # Vito Driver app
└── admin/       # Vito Admin app
core/            # Shared models
functions/      # Firebase Cloud Functions
releases/       # Built APKs
```

## APKs

- vito-client-debug.apk (65MB)
- vito-driver-debug.apk (57MB)
- vito-admin-debug.apk (59MB)

## Login

Demo: oussama / oussama

## License

Proprietary
