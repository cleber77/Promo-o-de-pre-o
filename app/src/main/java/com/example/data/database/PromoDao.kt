package com.example.data.database

import androidx.room.*
import com.example.data.model.*
import kotlinx.coroutines.flow.Flow

@Dao
interface PromoDao {

    // --- Users ---
    @Query("SELECT * FROM users WHERE id = :id LIMIT 1")
    fun getUserById(id: String): Flow<UserEntity?>

    @Query("SELECT * FROM users WHERE email = :email LIMIT 1")
    suspend fun getUserByEmailDirect(email: String): UserEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertUser(user: UserEntity)

    @Update
    suspend fun updateUser(user: UserEntity)

    // --- Stores ---
    @Query("SELECT * FROM stores")
    fun getAllStores(): Flow<List<StoreEntity>>

    @Query("SELECT * FROM stores WHERE id = :id LIMIT 1")
    fun getStoreById(id: String): Flow<StoreEntity?>

    @Query("SELECT * FROM stores WHERE ownerId = :ownerId LIMIT 1")
    fun getStoreByOwnerId(ownerId: String): Flow<StoreEntity?>

    @Query("SELECT * FROM stores WHERE ownerId = :ownerId LIMIT 1")
    suspend fun getStoreByOwnerIdDirect(ownerId: String): StoreEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertStore(store: StoreEntity)

    @Query("UPDATE stores SET followersCount = :count WHERE id = :id")
    suspend fun updateStoreFollowerCount(id: String, count: Int)

    // --- Promotions ---
    @Query("SELECT * FROM promotions ORDER BY createdAt DESC")
    fun getAllPromotions(): Flow<List<PromoEntity>>

    @Query("SELECT * FROM promotions WHERE storeId = :storeId ORDER BY createdAt DESC")
    fun getPromotionsByStore(storeId: String): Flow<List<PromoEntity>>

    @Query("SELECT * FROM promotions WHERE id = :id LIMIT 1")
    fun getPromotionById(id: String): Flow<PromoEntity?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPromotion(promotion: PromoEntity)

    @Query("DELETE FROM promotions WHERE id = :id")
    suspend fun deletePromotion(id: String)

    // --- Follows ---
    @Query("SELECT * FROM follows WHERE userId = :userId")
    fun getFollowsByUser(userId: String): Flow<List<FollowEntity>>

    @Query("SELECT * FROM follows WHERE userId = :userId AND storeId = :storeId LIMIT 1")
    fun getFollow(userId: String, storeId: String): Flow<FollowEntity?>

    @Query("SELECT * FROM follows WHERE userId = :userId AND storeId = :storeId LIMIT 1")
    suspend fun getFollowDirect(userId: String, storeId: String): FollowEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertFollow(follow: FollowEntity)

    @Query("DELETE FROM follows WHERE userId = :userId AND storeId = :storeId")
    suspend fun deleteFollow(userId: String, storeId: String)

    // --- Notifications ---
    @Query("SELECT * FROM notifications WHERE userId = :userId ORDER BY timestamp DESC")
    fun getNotificationsByUser(userId: String): Flow<List<NotificationEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertNotification(notification: NotificationEntity)

    @Query("UPDATE notifications SET isRead = 1 WHERE userId = :userId")
    suspend fun markAllNotificationsAsRead(userId: String)
}
