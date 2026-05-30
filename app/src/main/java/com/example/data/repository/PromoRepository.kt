package com.example.data.repository

import com.example.data.database.PromoDao
import com.example.data.model.*
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class PromoRepository(private val promoDao: PromoDao) {

    // --- Users ---
    fun getUserById(id: String): Flow<UserEntity?> = promoDao.getUserById(id)
    
    suspend fun getUserByEmailDirect(email: String): UserEntity? = withContext(Dispatchers.IO) {
        promoDao.getUserByEmailDirect(email)
    }

    suspend fun insertUser(user: UserEntity) = withContext(Dispatchers.IO) {
        promoDao.insertUser(user)
    }

    suspend fun updateUser(user: UserEntity) = withContext(Dispatchers.IO) {
        promoDao.updateUser(user)
    }

    // --- Stores ---
    fun getAllStores(): Flow<List<StoreEntity>> = promoDao.getAllStores()

    fun getStoreById(id: String): Flow<StoreEntity?> = promoDao.getStoreById(id)

    fun getStoreByOwnerId(ownerId: String): Flow<StoreEntity?> = promoDao.getStoreByOwnerId(ownerId)

    suspend fun getStoreByOwnerIdDirect(ownerId: String): StoreEntity? = withContext(Dispatchers.IO) {
        promoDao.getStoreByOwnerIdDirect(ownerId)
    }

    suspend fun insertStore(store: StoreEntity) = withContext(Dispatchers.IO) {
        promoDao.insertStore(store)
    }

    suspend fun updateStoreFollowerCount(id: String, count: Int) = withContext(Dispatchers.IO) {
        promoDao.updateStoreFollowerCount(id, count)
    }

    // --- Promotions ---
    fun getAllPromotions(): Flow<List<PromoEntity>> = promoDao.getAllPromotions()

    fun getPromotionsByStore(storeId: String): Flow<List<PromoEntity>> = promoDao.getPromotionsByStore(storeId)

    fun getPromotionById(id: String): Flow<PromoEntity?> = promoDao.getPromotionById(id)

    suspend fun insertPromotion(promotion: PromoEntity) = withContext(Dispatchers.IO) {
        promoDao.insertPromotion(promotion)
    }

    suspend fun deletePromotion(id: String) = withContext(Dispatchers.IO) {
        promoDao.deletePromotion(id)
    }

    // --- Follows ---
    fun getFollowsByUser(userId: String): Flow<List<FollowEntity>> = promoDao.getFollowsByUser(userId)

    fun getFollow(userId: String, storeId: String): Flow<FollowEntity?> = promoDao.getFollow(userId, storeId)
    
    suspend fun getFollowDirect(userId: String, storeId: String): FollowEntity? = withContext(Dispatchers.IO) {
        promoDao.getFollowDirect(userId, storeId)
    }

    suspend fun followStore(userId: String, storeId: String) = withContext(Dispatchers.IO) {
        val id = "${userId}_${storeId}"
        promoDao.insertFollow(FollowEntity(id = id, userId = userId, storeId = storeId, notificationsEnabled = true))
        
        // Update store count
        val storeFlow = promoDao.getStoreById(storeId)
        val storeObj = storeFlow.firstOrNull() ?: return@withContext
        if (storeObj != null) {
            promoDao.updateStoreFollowerCount(storeId, storeObj.followersCount + 1)
        }
    }

    suspend fun unfollowStore(userId: String, storeId: String) = withContext(Dispatchers.IO) {
        promoDao.deleteFollow(userId, storeId)
        
        // Update store count
        val storeFlow = promoDao.getStoreById(storeId)
        val storeObj = storeFlow.firstOrNull() ?: return@withContext
        if (storeObj != null) {
            val newCount = (storeObj.followersCount - 1).coerceAtLeast(0)
            promoDao.updateStoreFollowerCount(storeId, newCount)
        }
    }

    suspend fun toggleNotifications(userId: String, storeId: String) = withContext(Dispatchers.IO) {
        val follow = promoDao.getFollowDirect(userId, storeId)
        if (follow != null) {
            promoDao.insertFollow(follow.copy(notificationsEnabled = !follow.notificationsEnabled))
        }
    }

    // --- Notifications ---
    fun getNotificationsByUser(userId: String): Flow<List<NotificationEntity>> = promoDao.getNotificationsByUser(userId)

    suspend fun insertNotification(notification: NotificationEntity) = withContext(Dispatchers.IO) {
        promoDao.insertNotification(notification)
    }

    suspend fun markAllNotificationsAsRead(userId: String) = withContext(Dispatchers.IO) {
        promoDao.markAllNotificationsAsRead(userId)
    }

    // --- Seeding ---
    suspend fun seedInitialData(defaultUserId: String = "client_123") = withContext(Dispatchers.IO) {
        val existingStores = promoDao.getAllStores().firstOrNull() ?: emptyList()
        if (existingStores.isEmpty()) {
            // Seed stores
            val stores = listOf(
                StoreEntity(
                    id = "store_1",
                    ownerId = "owner_1",
                    name = "Padaria Sabor do Pão",
                    category = "Padaria",
                    logoDrawableName = "bread",
                    description = "Forno á lenha, pães sempre quentes, bolos recheados e doces tradicionais franceses.",
                    address = "Rua das Flores, 123",
                    latitude = -23.5505,
                    longitude = -46.6333,
                    rating = 4.8,
                    followersCount = 380,
                    reviewCount = 89,
                    phone = "(11) 98765-1212"
                ),
                StoreEntity(
                    id = "store_2",
                    ownerId = "owner_2",
                    name = "Açougue Bom Corte",
                    category = "Açougue",
                    logoDrawableName = "meat",
                    description = "Carnes e cortes especiais premium com garantia de procedência, higiene e preços imbatíveis.",
                    address = "Av. Paulista, 456",
                    latitude = -23.5595,
                    longitude = -46.6583,
                    rating = 4.7,
                    followersCount = 1200,
                    reviewCount = 128,
                    phone = "(11) 97654-2323"
                ),
                StoreEntity(
                    id = "store_3",
                    ownerId = "owner_3",
                    name = "Mercado Preço Bom",
                    category = "Supermercado",
                    logoDrawableName = "market",
                    description = "Tudo para o seu lar em um único lugar. O menor preço da região em hortifrúti, mercearia e produtos de limpeza.",
                    address = "Alameda Lorena, 789",
                    latitude = -23.5655,
                    longitude = -46.6633,
                    rating = 4.6,
                    followersCount = 850,
                    reviewCount = 145,
                    phone = "(11) 96543-3434"
                ),
                StoreEntity(
                    id = "store_4",
                    ownerId = "owner_4",
                    name = "Loja da Economia",
                    category = "Mais",
                    logoDrawableName = "shop",
                    description = "Utensílios, ferramentas e produtos variados com grandes ofertas e ótimas marcas.",
                    address = "Rua Augusta, 1011",
                    latitude = -23.5605,
                    longitude = -46.6533,
                    rating = 4.5,
                    followersCount = 420,
                    reviewCount = 67,
                    phone = "(11) 95432-4545"
                )
            )
            for (store in stores) {
                promoDao.insertStore(store)
            }

            // Seed promotions
            val promos = listOf(
                PromoEntity(
                    id = "promo_1",
                    storeId = "store_2",
                    storeName = "Açougue Bom Corte",
                    productName = "Coxão Mole",
                    imageResName = "meat",
                    normalPrice = 44.90,
                    promoPrice = 35.90,
                    discountPercent = 20,
                    category = "Açougue",
                    description = "Carne bovina de primeira qualidade, corte macio, saboroso e com baixo teor de gordura. Perfeito para grelhar, assar ou guisados do dia a dia.",
                    validUntil = "25/05/2026",
                    views = 124,
                    createdAt = System.currentTimeMillis() - 600000
                ),
                PromoEntity(
                    id = "promo_2",
                    storeId = "store_1",
                    storeName = "Padaria Sabor do Pão",
                    productName = "Pão Francês quentinho",
                    imageResName = "bread",
                    normalPrice = 14.80,
                    promoPrice = 11.10,
                    discountPercent = 25,
                    category = "Padaria",
                    description = "Pão francês crocante feito no forno a lenha, saindo de hora em hora. Perfeito com manteiga derretida.",
                    validUntil = "02/06/2026",
                    views = 95,
                    createdAt = System.currentTimeMillis() - 1200000
                ),
                PromoEntity(
                    id = "promo_3",
                    storeId = "store_3",
                    storeName = "Mercado Preço Bom",
                    productName = "Arroz Tipo 1 (5kg)",
                    imageResName = "market",
                    normalPrice = 24.90,
                    promoPrice = 18.90,
                    discountPercent = 24,
                    category = "Supermercado",
                    description = "Arroz agulhinha tipo 1 de grãos nobres selecionados, soltinho e super fofinho. Garanta a alimentação da sua família com economia.",
                    validUntil = "05/06/2026",
                    views = 210,
                    createdAt = System.currentTimeMillis() - 1800000
                ),
                PromoEntity(
                    id = "promo_4",
                    storeId = "store_4",
                    storeName = "Loja da Economia",
                    productName = "Detergente Concentrado",
                    imageResName = "shop",
                    normalPrice = 7.90,
                    promoPrice = 3.95,
                    discountPercent = 50,
                    category = "Mais",
                    description = "Detergente concentrado com alto rendimento e tecnologia de remoção instantânea de gordura pesada.",
                    validUntil = "29/05/2026",
                    views = 153,
                    createdAt = System.currentTimeMillis() - 2400000
                )
            )
            for (promo in promos) {
                promoDao.insertPromotion(promo)
            }
        }

        // Seed notifications if empty
        val existingNotifs = promoDao.getNotificationsByUser(defaultUserId).firstOrNull() ?: emptyList()
        if (existingNotifs.isEmpty()) {
            val notifs = listOf(
                NotificationEntity(
                    id = "notif_1",
                    userId = defaultUserId,
                    storeId = "store_1",
                    storeName = "Padaria Sabor do Pão",
                    message = "Nova promoção: Pão Francês 25% de desconto",
                    isRead = false,
                    createdAtLabel = "agora",
                    timestamp = System.currentTimeMillis()
                ),
                NotificationEntity(
                    id = "notif_2",
                    userId = defaultUserId,
                    storeId = "store_2",
                    storeName = "Açougue Bom Corte",
                    message = "Nova promoção: Coxão Mole por R$ 35,90",
                    isRead = false,
                    createdAtLabel = "5m",
                    timestamp = System.currentTimeMillis() - 300000
                ),
                NotificationEntity(
                    id = "notif_3",
                    userId = defaultUserId,
                    storeId = "store_3",
                    storeName = "Mercado Preço Bom",
                    message = "Nova promoção: Arroz 5kg por R$ 18,90",
                    isRead = false,
                    createdAtLabel = "1h",
                    timestamp = System.currentTimeMillis() - 3600000
                ),
                NotificationEntity(
                    id = "notif_4",
                    userId = defaultUserId,
                    storeId = "store_4",
                    storeName = "Loja da Economia",
                    message = "Nova coleção de produtos com até 50% OFF!",
                    isRead = true,
                    createdAtLabel = "2h",
                    timestamp = System.currentTimeMillis() - 7200000
                )
            )
            for (notif in notifs) {
                promoDao.insertNotification(notif)
            }
        }
    }
}
