package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.*
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.ui.*
import com.example.ui.theme.*
import kotlinx.coroutines.delay

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MyApplicationTheme {
                PromoAppMain()
            }
        }
    }
}

@Composable
fun PromoAppMain() {
    val viewModel: PromoViewModel = viewModel()
    val currentScreen by viewModel.currentScreen.collectAsState()
    val toastMessage by viewModel.toastMessage.collectAsState()

    // Map customer tabs inside a persistent bottom scaffold for smooth transitions
    val clientTabs = listOf("Início", "Lojas", "Seguindo", "Alertas", "Perfil")
    var selectedClientTab by remember { mutableStateOf("Início") }

    // Auto-dismiss toast timer
    LaunchedEffect(toastMessage) {
        if (toastMessage != null) {
            delay(3000)
            viewModel.dismissToast()
        }
    }

    Box(modifier = Modifier.fillMaxSize().background(BackgroundDark)) {
        // Core active layout router
        when (currentScreen) {
            PromoScreen.SPLASH -> {
                SplashScreen(viewModel)
            }
            PromoScreen.LOGIN -> {
                LoginScreen(viewModel)
            }
            PromoScreen.CADASTRO -> {
                CadastroScreen(viewModel)
            }
            PromoScreen.ESCOLHER_TIPO_CONTA -> {
                EscolherTipoContaScreen(viewModel)
            }
            PromoScreen.DETALHE_PROMOCAO -> {
                DetalhePromocaoScreen(viewModel)
            }
            PromoScreen.PERFIL_LOJA -> {
                PerfilLojaScreen(viewModel)
            }
            PromoScreen.CADASTRAR_PRODUTO -> {
                CadastrarProdutoScreen(viewModel)
            }
            PromoScreen.PLANOS_PAGAMENTO -> {
                PlanosPagamentoScreen(viewModel)
            }
            PromoScreen.PAINEL_EMPRESA -> {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .statusBarsPadding()
                ) {
                    PainelEmpresaScreen(viewModel)
                }
            }
            // Client Navigation views
            PromoScreen.HOME_CLIENTE,
            PromoScreen.LISTA_LOJAS,
            PromoScreen.SEGUINDO,
            PromoScreen.NOTIFICACOES -> {
                // Keep selected navigation tab synced
                LaunchedEffect(currentScreen) {
                    selectedClientTab = when (currentScreen) {
                        PromoScreen.HOME_CLIENTE -> "Início"
                        PromoScreen.LISTA_LOJAS -> "Lojas"
                        PromoScreen.SEGUINDO -> "Seguindo"
                        PromoScreen.NOTIFICACOES -> "Alertas"
                        else -> "Início"
                    }
                }

                ClientMainContainer(
                    viewModel = viewModel,
                    activeTab = selectedClientTab,
                    onTabSelected = { tab ->
                        selectedClientTab = tab
                        when (tab) {
                            "Início" -> viewModel.navigateTo(PromoScreen.HOME_CLIENTE)
                            "Lojas" -> viewModel.navigateTo(PromoScreen.LISTA_LOJAS)
                            "Seguindo" -> viewModel.navigateTo(PromoScreen.SEGUINDO)
                            "Alertas" -> viewModel.navigateTo(PromoScreen.NOTIFICACOES)
                        }
                    }
                ) { innerPadding ->
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(innerPadding)
                            .statusBarsPadding()
                    ) {
                        if (selectedClientTab == "Perfil") {
                            // Aba 5: Perfil
                            ClientProfileScreen(viewModel = viewModel)
                        } else {
                            when (currentScreen) {
                                PromoScreen.HOME_CLIENTE -> HomeClienteScreen(viewModel = viewModel)
                                PromoScreen.LISTA_LOJAS -> LojasScreen(viewModel = viewModel)
                                PromoScreen.SEGUINDO -> SeguindoScreen(viewModel = viewModel)
                                PromoScreen.NOTIFICACOES -> NotificacoesScreen(viewModel = viewModel)
                                else -> HomeClienteScreen(viewModel = viewModel)
                            }
                        }
                    }
                }
            }
        }

        // Animated Toast Banner
        AnimatedVisibility(
            visible = toastMessage != null,
            enter = slideInVertically(initialOffsetY = { it }) + fadeIn(),
            exit = slideOutVertically(targetOffsetY = { it }) + fadeOut(),
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = 96.dp)
                .padding(horizontal = 24.dp)
        ) {
            toastMessage?.let { msg ->
                Card(
                    colors = CardDefaults.cardColors(containerColor = YellowVibrant),
                    shape = RoundedCornerShape(12.dp),
                    elevation = CardDefaults.cardElevation(defaultElevation = 8.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("floating_toast")
                ) {
                    Row(
                        modifier = Modifier.padding(14.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Default.Notifications,
                            contentDescription = "Notificação",
                            tint = BackgroundDark,
                            modifier = Modifier.size(20.dp)
                        )
                        Spacer(modifier = Modifier.width(10.dp))
                        Text(
                            text = msg,
                            color = BackgroundDark,
                            fontWeight = FontWeight.Bold,
                            fontSize = 14.sp
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun ClientProfileScreen(viewModel: PromoViewModel) {
    val user by viewModel.currentUser.collectAsState()
    val follows by viewModel.myFollows.collectAsState()
    val notifications by viewModel.myNotifications.collectAsState()

    val nameLabel = user?.name ?: "Cleber Geraldo"
    val emailLabel = user?.email ?: "cliente@promofacil.com"
    val phoneLabel = user?.phone ?: "(11) 98888-7777"

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(24.dp))

        Box(
            modifier = Modifier
                .size(96.dp)
                .background(CardBackground, CircleShape),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = Icons.Default.AccountCircle,
                contentDescription = "Foto de perfil",
                tint = YellowVibrant,
                modifier = Modifier.size(64.dp)
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = nameLabel,
            style = MaterialTheme.typography.headlineMedium,
            color = TextWhite,
            fontWeight = FontWeight.Bold
        )

        Text(
            text = emailLabel,
            color = TextGrayLight,
            style = MaterialTheme.typography.bodyMedium
        )

        Spacer(modifier = Modifier.height(28.dp))

        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = CardBackground)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(text = "${follows.size}", color = YellowVibrant, fontWeight = FontWeight.Bold, fontSize = 20.sp)
                    Text(text = "Lojas seguidas", color = TextGrayLight, fontSize = 11.sp)
                }
                Box(
                    modifier = Modifier
                        .width(1.dp)
                        .height(32.dp)
                        .background(BorderColor)
                )
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(text = "${notifications.size}", color = YellowVibrant, fontWeight = FontWeight.Bold, fontSize = 20.sp)
                    Text(text = "Avisos recebidos", color = TextGrayLight, fontSize = 11.sp)
                }
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        Column(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            ProfileInfoRow(label = "Telefone", value = phoneLabel)
            ProfileInfoRow(label = "Localização", value = "São Paulo, SP")
            ProfileInfoRow(
                label = "Termos e Condições",
                value = "Aceito em 2026",
                isClickable = true,
                onClick = { viewModel.showToast("Políticas vigentes lidas!") }
            )
        }

        Spacer(modifier = Modifier.height(36.dp))

        Button(
            onClick = { viewModel.logout() },
            colors = ButtonDefaults.buttonColors(containerColor = RedAlert, contentColor = Color.White),
            modifier = Modifier
                .fillMaxWidth()
                .height(48.dp)
                .testTag("profile_logout_button"),
            shape = RoundedCornerShape(10.dp)
        ) {
            Icon(Icons.Default.ExitToApp, "Sair")
            Spacer(modifier = Modifier.width(8.dp))
            Text("Sair da conta", fontWeight = FontWeight.Bold)
        }
    }
}

@Composable
fun ProfileInfoRow(
    label: String,
    value: String,
    isClickable: Boolean = false,
    onClick: () -> Unit = {}
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(CardBackground, RoundedCornerShape(10.dp))
            .then(if (isClickable) Modifier.clickable(onClick = onClick) else Modifier)
            .padding(16.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(text = label, color = TextGrayLight, fontSize = 14.sp)
        Text(text = value, color = TextWhite, fontWeight = FontWeight.Bold, fontSize = 14.sp)
    }
}
