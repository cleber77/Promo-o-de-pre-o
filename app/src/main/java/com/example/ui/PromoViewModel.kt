package com.example.ui

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.database.PromoDatabase
import com.example.data.model.*
import com.example.data.repository.PromoRepository
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.util.UUID

enum class PromoScreen {
    SPLASH,
    LOGIN,
    CADASTRO,
    ESCOLHER_TIPO_CONTA,
    HOME_CLIENTE,
    DETALHE_PROMOCAO,
    LISTA_LOJAS,
    PERFIL_LOJA,
    SEGUINDO,
    NOTIFICACOES,
    PAINEL_EMPRESA,
    CADASTRAR_PRODUTO,
    PLANOS_PAGAMENTO
}

class PromoViewModel(application: Application) : AndroidViewModel(application) {

    private val database = PromoDatabase.getDatabase(application)
    private val repository = PromoRepository(database.promoDao())

    // Navigation Stack for stable back-button press
    private val _currentScreen = MutableStateFlow(PromoScreen.SPLASH)
    val currentScreen: StateFlow<PromoScreen> = _currentScreen.asStateFlow()
    private val backStack = mutableListOf<PromoScreen>()

    // Persistent and state variables
    private val _currentUser = MutableStateFlow<UserEntity?>(null)
    val currentUser: StateFlow<UserEntity?> = _currentUser.asStateFlow()

    private val _selectedPromo = MutableStateFlow<PromoEntity?>(null)
    val selectedPromo: StateFlow<PromoEntity?> = _selectedPromo.asStateFlow()

    private val _selectedStore = MutableStateFlow<StoreEntity?>(null)
    val selectedStore: StateFlow<StoreEntity?> = _selectedStore.asStateFlow()

    // Business dashboard states
    private val _myStore = MutableStateFlow<StoreEntity?>(null)
    val myStore: StateFlow<StoreEntity?> = _myStore.asStateFlow()

    // Filters and search logic
    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()

    private val _categoryFilter = MutableStateFlow("Todas")
    val categoryFilter: StateFlow<String> = _categoryFilter.asStateFlow()

    // UI Interactive States
    val allStores: StateFlow<List<StoreEntity>> = repository.getAllStores()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val allPromotions: StateFlow<List<PromoEntity>> = repository.getAllPromotions()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    // Filtered lists
    val filteredPromotions: StateFlow<List<PromoEntity>> = combine(
        allPromotions, searchQuery, categoryFilter
    ) { promos, query, category ->
        promos.filter { promo ->
            val matchesQuery = query.isEmpty() || 
                    promo.productName.contains(query, ignoreCase = true) || 
                    promo.storeName.contains(query, ignoreCase = true)
            val matchesCategory = category == "Todas" || promo.category.equals(category, ignoreCase = true)
            matchesQuery && matchesCategory
        }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val filteredStores: StateFlow<List<StoreEntity>> = combine(
        allStores, searchQuery, categoryFilter
    ) { stores, query, category ->
        stores.filter { store ->
            val matchesQuery = query.isEmpty() || store.name.contains(query, ignoreCase = true)
            val matchesCategory = category == "Todas" || store.category.equals(category, ignoreCase = true)
            matchesQuery && matchesCategory
        }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    // Flows linked to active logged-in Client User
    private val _myFollows = MutableStateFlow<List<FollowEntity>>(emptyList())
    val myFollows: StateFlow<List<FollowEntity>> = _myFollows.asStateFlow()

    private val _myNotifications = MutableStateFlow<List<NotificationEntity>>(emptyList())
    val myNotifications: StateFlow<List<NotificationEntity>> = _myNotifications.asStateFlow()

    // Active screen support trackers
    private val _seguindoTab = MutableStateFlow("Lojas") // "Lojas" or "Promoções"
    val seguindoTab: StateFlow<String> = _seguindoTab.asStateFlow()

    private val _planCycle = MutableStateFlow("Anual") // "Mensal" or "Anual"
    val planCycle: StateFlow<String> = _planCycle.asStateFlow()

    private val _selectedPlan = MutableStateFlow("BASIC") // "BASIC", "PROFESSIONAL", "PREMIUM"
    val selectedPlan: StateFlow<String> = _selectedPlan.asStateFlow()

    // Toast alerts channel
    private val _toastMessage = MutableStateFlow<String?>(null)
    val toastMessage: StateFlow<String?> = _toastMessage.asStateFlow()

    // Form inputs and error strings
    private val _authError = MutableStateFlow<String?>(null)
    val authError: StateFlow<String?> = _authError.asStateFlow()

    init {
        // Seed default database record mockups on start
        viewModelScope.launch {
            repository.seedInitialData()
            
            // Core test credentials:
            // 1. Client user
            val testClient = UserEntity(
                id = "client_123",
                name = "Cleber Geraldo",
                email = "cliente@promofacil.com",
                phone = "(11) 98888-7777",
                accountType = "CLIENT",
                currentCity = "São Paulo, SP"
            )
            repository.insertUser(testClient)

            // 2. Business user (Açougue Bom Corte owner)
            val testBusiness = UserEntity(
                id = "owner_2",
                name = "Carlos Bom Corte",
                email = "loja@bomcorte.com",
                phone = "(11) 97654-2323",
                accountType = "BUSINESS",
                businessPlan = "BASIC"
            )
            repository.insertUser(testBusiness)
        }

        // Keep follows and notifications updated when currentUser changes
        viewModelScope.launch {
            currentUser.collect { user ->
                if (user != null) {
                    launch {
                        repository.getFollowsByUser(user.id).collect {
                            _myFollows.value = it
                        }
                    }
                    launch {
                        repository.getNotificationsByUser(user.id).collect {
                            _myNotifications.value = it
                        }
                    }
                } else {
                    _myFollows.value = emptyList()
                    _myNotifications.value = emptyList()
                }
            }
        }
    }

    // --- Navigation Logic ---
    fun navigateTo(screen: PromoScreen, clearHistory: Boolean = false) {
        _authError.value = null
        if (clearHistory) {
            backStack.clear()
        } else {
            backStack.add(_currentScreen.value)
        }
        _currentScreen.value = screen
    }

    fun navigateBack() {
        if (backStack.isNotEmpty()) {
            _currentScreen.value = backStack.removeAt(backStack.size - 1)
        } else {
            // Default backward fallback
            _currentScreen.value = PromoScreen.LOGIN
        }
    }

    fun showToast(message: String) {
        _toastMessage.value = message
    }

    fun dismissToast() {
        _toastMessage.value = null
    }

    // --- Tab / Toggle controls ---
    fun setSeguindoTab(tab: String) {
        _seguindoTab.value = tab
    }

    fun togglePlanCycle() {
        _planCycle.value = if (_planCycle.value == "Anual") "Mensal" else "Anual"
    }

    fun selectPlan(plan: String) {
        _selectedPlan.value = plan
    }

    fun setCategoryFilter(category: String) {
        _categoryFilter.value = category
    }

    fun updateSearchQuery(query: String) {
        _searchQuery.value = query
    }

    fun viewPromoDetail(promo: PromoEntity) {
        _selectedPromo.value = promo
        navigateTo(PromoScreen.DETALHE_PROMOCAO)
    }

    fun viewStoreDetail(storeId: String) {
        viewModelScope.launch {
            repository.getStoreById(storeId).firstOrNull()?.let {
                _selectedStore.value = it
                navigateTo(PromoScreen.PERFIL_LOJA)
            }
        }
    }

    // --- Authentication Actions ---
    fun login(emailInput: String, passwordInput: String) {
        _authError.value = null
        val trimmedEmail = emailInput.trim()
        if (trimmedEmail.isEmpty() || passwordInput.isEmpty()) {
            _authError.value = "Por favor, preencha todos os campos."
            return
        }

        viewModelScope.launch {
            val user = repository.getUserByEmailDirect(trimmedEmail)
            if (user != null) {
                _currentUser.value = user
                
                if (user.accountType == "CLIENT") {
                    // Pre-fill seed notifications for client users
                    _currentScreen.value = PromoScreen.HOME_CLIENTE
                } else if (user.accountType == "BUSINESS") {
                    // Match business store
                    val store = repository.getStoreByOwnerIdDirect(user.id)
                    _myStore.value = store
                    _currentScreen.value = PromoScreen.PAINEL_EMPRESA
                } else {
                    _currentScreen.value = PromoScreen.ESCOLHER_TIPO_CONTA
                }
                showToast("Bem-vindo de volta, ${user.name}!")
            } else {
                // Procedural generation fallback to make trying other emails easy
                val parts = trimmedEmail.split("@")
                val defaultName = if (parts.isNotEmpty()) parts[0].uppercase() else "Usuário"
                val isLoja = trimmedEmail.contains("loja") || trimmedEmail.contains("empresa")
                
                val newUser = UserEntity(
                    id = UUID.randomUUID().toString(),
                    name = defaultName,
                    email = trimmedEmail,
                    phone = "(11) 99999-9999",
                    accountType = if (isLoja) "BUSINESS" else "CLIENT"
                )
                repository.insertUser(newUser)
                _currentUser.value = newUser
                
                if (newUser.accountType == "CLIENT") {
                    _currentScreen.value = PromoScreen.HOME_CLIENTE
                } else {
                    val defaultStore = StoreEntity(
                        id = "store_" + UUID.randomUUID().toString().take(6),
                        ownerId = newUser.id,
                        name = "Sua Nova Loja " + defaultName,
                        category = "Supermercado",
                        logoDrawableName = "market",
                        description = "Cadastre de forma simples e mude a descrição aqui nos detalhes da loja.",
                        address = "Avenida Principal, 1000",
                        latitude = -23.55,
                        longitude = -46.63,
                        rating = 5.0,
                        followersCount = 0,
                        reviewCount = 0
                    )
                    repository.insertStore(defaultStore)
                    _myStore.value = defaultStore
                    _currentScreen.value = PromoScreen.PAINEL_EMPRESA
                }
                showToast("Conta criada e login realizado com sucesso!")
            }
        }
    }

    fun signUp(fullName: String, emailInput: String, phoneInput: String, pwdInput: String, confirmPwdInput: String) {
        _authError.value = null
        if (fullName.isEmpty() || emailInput.isEmpty() || phoneInput.isEmpty() || pwdInput.isEmpty()) {
            _authError.value = "Por favor, preencha todos os campos."
            return
        }
        if (pwdInput != confirmPwdInput) {
            _authError.value = "As senhas não coincidem."
            return
        }

        viewModelScope.launch {
            val userCheck = repository.getUserByEmailDirect(emailInput.trim())
            if (userCheck != null) {
                _authError.value = "Este e-mail já está em uso."
                return@launch
            }

            val newUser = UserEntity(
                id = UUID.randomUUID().toString(),
                name = fullName,
                email = emailInput.trim(),
                phone = phoneInput,
                accountType = "NONE" // Forces Account Type Selection screen
            )
            repository.insertUser(newUser)
            _currentUser.value = newUser
            navigateTo(PromoScreen.ESCOLHER_TIPO_CONTA, clearHistory = true)
            showToast("Cadastro realizado! Escolha o tipo de conta.")
        }
    }

    fun setAccountTypeSelection(type: String) {
        val user = _currentUser.value ?: return
        viewModelScope.launch {
            val updatedUser = user.copy(accountType = type)
            repository.insertUser(updatedUser)
            _currentUser.value = updatedUser

            if (type == "CLIENT") {
                navigateTo(PromoScreen.HOME_CLIENTE, clearHistory = true)
                showToast("Seja bem-vindo como cliente!")
            } else {
                // Setup default store for newly register business
                val defaultStore = StoreEntity(
                    id = "store_" + UUID.randomUUID().toString().take(6),
                    ownerId = user.id,
                    name = "Sua nova Loja " + user.name,
                    category = "Supermercado",
                    logoDrawableName = "market",
                    description = "Divulgue suas promoções locais e decole nas vendas!",
                    address = "Av. Paulista, 1000",
                    latitude = -23.5505,
                    longitude = -46.6333,
                    rating = 5.0,
                    followersCount = 0,
                    reviewCount = 0
                )
                repository.insertStore(defaultStore)
                _myStore.value = defaultStore
                _selectedPlan.value = "BASIC"
                
                // Directly offer selecting subscription plans
                navigateTo(PromoScreen.PLANOS_PAGAMENTO, clearHistory = true)
                showToast("Escolha um plano de pagamento para ativar sua loja.")
            }
        }
    }

    // --- Customer Interactions ---
    fun toggleFollowStore(storeId: String) {
        val user = _currentUser.value ?: return
        viewModelScope.launch {
            val isFollowing = myFollows.value.any { it.storeId == storeId }
            if (isFollowing) {
                repository.unfollowStore(user.id, storeId)
                showToast("Você desseguiu esta loja.")
            } else {
                repository.followStore(user.id, storeId)
                showToast("Seguindo loja com sucesso!")
            }
            
            // Sync selected store view model detail counter
            _selectedStore.firstOrNull()?.let { current ->
                if (current.id == storeId) {
                    val latest = repository.getAllStores().firstOrNull()?.find { it.id == storeId }
                    if (latest != null) {
                        _selectedStore.value = latest
                    }
                }
            }
        }
    }

    fun toggleFollowNotificationEnabled(storeId: String) {
        val user = _currentUser.value ?: return
        viewModelScope.launch {
            repository.toggleNotifications(user.id, storeId)
            showToast("Preferencia de notificação salva.")
        }
    }

    fun markNotificationsRead() {
        val user = _currentUser.value ?: return
        viewModelScope.launch {
            repository.markAllNotificationsAsRead(user.id)
            showToast("Notificações marcadas como lidas")
        }
    }

    // --- Business Admin Actions ---
    fun registerPromotion(name: String, normalPriceStr: String, promoPriceStr: String, category: String, validUntil: String) {
        val user = _currentUser.value ?: return
        val store = _myStore.value ?: return
        
        val normalPrice = normalPriceStr.toDoubleOrNull() ?: 0.0
        val promoPrice = promoPriceStr.toDoubleOrNull() ?: 0.0
        if (name.isEmpty() || normalPrice <= 0.0 || promoPrice <= 0.0 || category.isEmpty() || validUntil.isEmpty()) {
            showToast("Por favor preencha todos os campos corretamente.")
            return
        }

        val discountVal = (((normalPrice - promoPrice) / normalPrice) * 100).toInt().coerceIn(1, 99)

        viewModelScope.launch {
            val newPromo = PromoEntity(
                id = "promo_" + UUID.randomUUID().toString().take(6),
                storeId = store.id,
                storeName = store.name,
                productName = name,
                imageResName = when(category.lowercase()) {
                    "açougue" -> "meat"
                    "padaria" -> "bread"
                    "supermercado" -> "market"
                    else -> "shop"
                },
                normalPrice = normalPrice,
                promoPrice = promoPrice,
                discountPercent = discountVal,
                category = category,
                description = "Oferta imperdível cadastrada na categoria " + category + ". Aproveite enquanto durarem os estoques!",
                validUntil = validUntil,
                views = 0,
                createdAt = System.currentTimeMillis()
            )
            repository.insertPromotion(newPromo)
            
            // Simulates push notification to all clients who follow this store
            // We write a notification to default client_123 so the client views it!
            val pushNotifClient = NotificationEntity(
                id = "push_notif_" + UUID.randomUUID().toString().take(6),
                userId = "client_123",
                storeId = store.id,
                storeName = store.name,
                message = "Nova promoção: ${newPromo.productName} por R$ ${String.format("%.2f", newPromo.promoPrice)}!",
                isRead = false,
                createdAtLabel = "agora",
                timestamp = System.currentTimeMillis()
            )
            repository.insertNotification(pushNotifClient)

            navigateTo(PromoScreen.PAINEL_EMPRESA)
            showToast("Oferta cadastrada e notificação enviada!")
        }
    }

    fun selectAndConfirmPlan(planName: String) {
        val user = _currentUser.value ?: return
        viewModelScope.launch {
            val updatedUser = user.copy(businessPlan = planName)
            repository.insertUser(updatedUser)
            _currentUser.value = updatedUser
            navigateTo(PromoScreen.PAINEL_EMPRESA, clearHistory = true)
            showToast("Plano $planName ativado com sucesso!")
        }
    }

    fun logout() {
        _currentUser.value = null
        _myStore.value = null
        navigateTo(PromoScreen.LOGIN, clearHistory = true)
        showToast("Sessão encerrada.")
    }
}
