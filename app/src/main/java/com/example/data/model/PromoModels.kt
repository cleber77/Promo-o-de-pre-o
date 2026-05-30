package com.example.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "users")
data class UserEntity(
    @PrimaryKey val id: String,
    val name: String,
    val email: String,
    val phone: String,
    val accountType: String, // "CLIENT", "BUSINESS", "NONE"
    val businessPlan: String = "BASIC", // "BASIC", "PROFESSIONAL", "PREMIUM"
    val currentCity: String = "São Paulo, SP"
)

@Entity(tableName = "stores")
data class StoreEntity(
    @PrimaryKey val id: String,
    val ownerId: String,
    val name: String,
    val category: String, // "Padaria", "Açougue", "Supermercado", "Mais"
    val logoDrawableName: String, // Placeholder description or identifier
    val description: String,
    val address: String,
    val latitude: Double,
    val longitude: Double,
    val rating: Double,
    val followersCount: Int,
    val reviewCount: Int,
    val phone: String = "(11) 98765-4321"
)

@Entity(tableName = "promotions")
data class PromoEntity(
    @PrimaryKey val id: String,
    val storeId: String,
    val storeName: String,
    val productName: String,
    val imageResName: String, // Name of illustration
    val normalPrice: Double,
    val promoPrice: Double,
    val discountPercent: Int,
    val category: String,
    val description: String,
    val validUntil: String,
    val views: Int = 0,
    val createdAt: Long = System.currentTimeMillis()
)

@Entity(tableName = "follows")
data class FollowEntity(
    @PrimaryKey val id: String, // userId + "_" + storeId
    val userId: String,
    val storeId: String,
    val notificationsEnabled: Boolean = true
)

@Entity(tableName = "notifications")
data class NotificationEntity(
    @PrimaryKey val id: String,
    val userId: String,
    val storeId: String,
    val storeName: String,
    val message: String,
    val isRead: Boolean = false,
    val createdAtLabel: String = "agora",
    val timestamp: Long = System.currentTimeMillis()
)
