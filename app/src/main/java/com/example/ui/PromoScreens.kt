package com.example.ui

import android.widget.Space
import androidx.compose.animation.*
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.*
import com.example.ui.theme.*
import kotlinx.coroutines.delay

@Composable
fun AppLogo(modifier: Modifier = Modifier, sizeDp: Int = 110) {
    Box(
        modifier = modifier.size(sizeDp.dp),
        contentAlignment = Alignment.Center
    ) {
        val context = LocalContext.current
        val resId = context.resources.getIdentifier("promo_facil_logo_1780180323044", "drawable", context.packageName)
        if (resId != 0) {
            Image(
                painter = painterResource(id = resId),
                contentDescription = "Promo Fácil Logo",
                modifier = Modifier
                    .fillMaxSize()
                    .clip(RoundedCornerShape(sizeDp.coerceIn(10, 30).dp)),
                contentScale = ContentScale.Fit
            )
        } else {
            // Elegant Vector Fallback
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(YellowVibrant, shape = RoundedCornerShape((sizeDp / 4).dp)),
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Icon(
                        imageVector = Icons.Default.ShoppingBag,
                        contentDescription = "Logo",
                        tint = BackgroundDark,
                        modifier = Modifier.size((sizeDp * 0.45).dp)
                    )
                    Text(
                        text = "%",
                        color = BackgroundDark,
                        fontWeight = FontWeight.ExtraBold,
                        fontSize = (sizeDp * 0.2).sp
                    )
                }
            }
        }
    }
}

// Draw Category Badges dynamically instead of raw unprovided resource files
@Composable
fun CategoryAvatar(
    category: String,
    modifier: Modifier = Modifier,
    size: Int = 48,
    tintColor: Color = YellowVibrant
) {
    val icon = when (category.lowercase()) {
        "padaria" -> Icons.Default.BreakfastDining
        "açougue" -> Icons.Default.Restaurant
        "supermercado" -> Icons.Default.ShoppingCart
        else -> Icons.Default.GridOn
    }
    Box(
        modifier = modifier
            .size(size.dp)
            .background(tintColor.copy(alpha = 0.15f), shape = CircleShape)
            .border(1.dp, tintColor.copy(alpha = 0.3f), CircleShape),
        contentAlignment = Alignment.Center
    ) {
        Icon(
            imageVector = icon,
            contentDescription = category,
            tint = tintColor,
            modifier = Modifier.size((size * 0.55).dp)
        )
    }
}

// Procedural visual cards representing products to avoid missing assets
@Composable
fun ProductProceduralImage(
    productName: String,
    category: String,
    modifier: Modifier = Modifier
) {
    val (gradient, icon) = when (category.lowercase()) {
        "açougue" -> Pair(
            Brush.linearGradient(listOf(Color(0xFF8B0000), Color(0xFFFF4500))),
            Icons.Default.Restaurant
        )
        "padaria" -> Pair(
            Brush.linearGradient(listOf(Color(0xFFD2691E), Color(0xFFF4A460))),
            Icons.Default.BreakfastDining
        )
        "supermercado" -> Pair(
            Brush.linearGradient(listOf(Color(0xFF006400), Color(0xFF32CD32))),
            Icons.Default.ShoppingCart
        )
        else -> Pair(
            Brush.linearGradient(listOf(Color(0xFF4B0082), Color(0xFF9370DB))),
            Icons.Default.LocalMall
        )
    }

    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(130.dp)
            .background(gradient)
            .padding(12.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Icon(
                imageVector = icon,
                contentDescription = productName,
                tint = Color.White,
                modifier = Modifier.size(48.dp)
            )
            Spacer(modifier = Modifier.height(6.dp))
            Text(
                text = productName.uppercase(),
                color = Color.White,
                fontWeight = FontWeight.ExtraBold,
                fontSize = 11.sp,
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .background(Color.Black.copy(alpha = 0.4f), RoundedCornerShape(4.dp))
                    .padding(horizontal = 6.dp, vertical = 2.dp)
            )
        }
    }
}

// ---------------- SPLASH SCREEN ----------------
@Composable
fun SplashScreen(viewModel: PromoViewModel) {
    LaunchedEffect(Unit) {
        delay(2500)
        viewModel.navigateTo(PromoScreen.LOGIN, clearHistory = true)
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black),
        contentAlignment = Alignment.Center
    ) {
        // Shopping bg subtle pattern custom canvas rendering
        Box(
            modifier = Modifier
                .fillMaxSize()
                .drawBehind {
                    val cols = 8
                    val rows = 12
                    val stepX = size.width / cols
                    val stepY = size.height / rows
                    for (c in 0 until cols) {
                        for (r in 0 until rows) {
                            if ((c + r) % 2 == 0) {
                                drawCircle(
                                    color = Color.White.copy(alpha = 0.02f),
                                    radius = 12f,
                                    center = Offset(c * stepX + stepX / 2, r * stepY + stepY / 2)
                                )
                            }
                        }
                    }
                }
        )

        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier.padding(24.dp)
        ) {
            AppLogo(sizeDp = 130)
            Spacer(modifier = Modifier.height(24.dp))
            
            Text(
                text = "PROMO FÁCIL",
                style = MaterialTheme.typography.displayMedium,
                color = TextWhite,
                fontWeight = FontWeight.ExtraBold,
                letterSpacing = 2.sp
            )
            
            Spacer(modifier = Modifier.height(8.dp))
            
            Text(
                text = "As melhores ofertas perto de você!",
                style = MaterialTheme.typography.bodyMedium,
                color = TextGrayLight,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(48.dp))
            
            CircularProgressIndicator(
                color = YellowVibrant,
                strokeWidth = 3.dp,
                modifier = Modifier.size(28.dp).testTag("splash_loader")
            )
            
            Spacer(modifier = Modifier.height(24.dp))
            
            TextButton(
                onClick = { viewModel.navigateTo(PromoScreen.LOGIN, clearHistory = true) },
                colors = ButtonDefaults.textButtonColors(contentColor = YellowVibrant),
                modifier = Modifier.testTag("skip_splash_button")
            ) {
                Text("Pular introdução →", fontWeight = FontWeight.SemiBold)
            }
        }
    }
}

// ---------------- LOGIN SCREEN ----------------
@Composable
fun LoginScreen(viewModel: PromoViewModel) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var passwordVisible by remember { mutableStateOf(false) }
    
    val authError by viewModel.authError.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(BackgroundDark)
            .verticalScroll(rememberScrollState())
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(40.dp))
        
        AppLogo(sizeDp = 100)
        
        Spacer(modifier = Modifier.height(16.dp))
        
        Text(
            text = "PROMO FÁCIL",
            style = MaterialTheme.typography.titleLarge,
            color = TextWhite,
            fontWeight = FontWeight.ExtraBold
        )
        Text(
            text = "As melhores ofertas perto de você!",
            style = MaterialTheme.typography.bodyMedium,
            color = TextGrayLight
        )

        Spacer(modifier = Modifier.height(40.dp))

        // Form Fields
        OutlinedTextField(
            value = email,
            onValueChange = { email = it },
            label = { Text("E-mail") },
            leadingIcon = { Icon(Icons.Default.Mail, contentDescription = "Email", tint = YellowVibrant) },
            colors = OutlinedTextFieldDefaults.colors(
                focusedTextColor = TextWhite,
                unfocusedTextColor = TextWhite,
                focusedBorderColor = YellowVibrant,
                unfocusedBorderColor = BorderColor,
                focusedLabelColor = YellowVibrant,
                unfocusedLabelColor = TextGrayMuted
            ),
            modifier = Modifier
                .fillMaxWidth()
                .testTag("email_input"),
            shape = RoundedCornerShape(12.dp),
            singleLine = true
        )

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = password,
            onValueChange = { password = it },
            label = { Text("Senha") },
            leadingIcon = { Icon(Icons.Default.Lock, contentDescription = "Senha", tint = YellowVibrant) },
            trailingIcon = {
                IconButton(onClick = { passwordVisible = !passwordVisible }) {
                    Icon(
                        imageVector = if (passwordVisible) Icons.Default.Visibility else Icons.Default.VisibilityOff,
                        contentDescription = "Ocultar senha",
                        tint = TextGrayMuted
                    )
                }
            },
            colors = OutlinedTextFieldDefaults.colors(
                focusedTextColor = TextWhite,
                unfocusedTextColor = TextWhite,
                focusedBorderColor = YellowVibrant,
                unfocusedBorderColor = BorderColor,
                focusedLabelColor = YellowVibrant,
                unfocusedLabelColor = TextGrayMuted
            ),
            visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
            modifier = Modifier
                .fillMaxWidth()
                .testTag("password_input"),
            shape = RoundedCornerShape(12.dp),
            singleLine = true
        )

        authError?.let {
            Spacer(modifier = Modifier.height(12.dp))
            Text(text = it, color = RedAlert, style = MaterialTheme.typography.bodyMedium, fontWeight = FontWeight.SemiBold)
        }

        Spacer(modifier = Modifier.height(8.dp))

        Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.CenterEnd) {
            Text(
                text = "Esqueceu sua senha?",
                color = YellowVibrant,
                fontWeight = FontWeight.Bold,
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier
                    .clickable { viewModel.showToast("Link de redefinição enviado!") }
                    .testTag("forgot_password_link")
            )
        }

        Spacer(modifier = Modifier.height(28.dp))

        // Main Login button
        Button(
            onClick = { viewModel.login(email, password) },
            colors = ButtonDefaults.buttonColors(containerColor = YellowVibrant, contentColor = BackgroundDark),
            modifier = Modifier
                .fillMaxWidth()
                .height(52.dp)
                .testTag("login_button"),
            shape = RoundedCornerShape(12.dp)
        ) {
            Text("Entrar", fontWeight = FontWeight.Bold, fontSize = 16.sp)
        }

        Spacer(modifier = Modifier.height(20.dp))

        // Separator
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            HorizontalDivider(modifier = Modifier.weight(1f), color = BorderColor)
            Text(
                text = " ou continue com ",
                color = TextGrayMuted,
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier.padding(horizontal = 8.dp)
            )
            HorizontalDivider(modifier = Modifier.weight(1f), color = BorderColor)
        }

        Spacer(modifier = Modifier.height(20.dp))

        // Social login buttons
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Google
            OutlinedButton(
                onClick = { viewModel.login("cliente@promofacil.com", "123456") },
                modifier = Modifier
                    .weight(1f)
                    .height(48.dp)
                    .testTag("google_login_button"),
                shape = RoundedCornerShape(12.dp),
                border = BorderStroke(1.dp, BorderColor),
                colors = ButtonDefaults.outlinedButtonColors(contentColor = TextWhite)
            ) {
                Icon(
                    imageVector = Icons.Default.PlayArrow,
                    contentDescription = "Google",
                    tint = YellowVibrant,
                    modifier = Modifier.size(18.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text("Google", fontWeight = FontWeight.Bold)
            }

            // Facebook
            OutlinedButton(
                onClick = { viewModel.login("loja@bomcorte.com", "123456") },
                modifier = Modifier
                    .weight(1f)
                    .height(48.dp)
                    .testTag("facebook_login_button"),
                shape = RoundedCornerShape(12.dp),
                border = BorderStroke(1.dp, BorderColor),
                colors = ButtonDefaults.outlinedButtonColors(contentColor = TextWhite)
            ) {
                Icon(
                    imageVector = Icons.Default.Facebook,
                    contentDescription = "Facebook",
                    tint = Color(0xFF1877F2),
                    modifier = Modifier.size(18.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text("Facebook", fontWeight = FontWeight.Bold)
            }
        }

        Spacer(modifier = Modifier.weight(1f))
        Spacer(modifier = Modifier.height(24.dp))

        Row(
            horizontalArrangement = Arrangement.Center,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Ainda não tem conta? ", color = TextWhite)
            Text(
                text = "Cadastre-se",
                color = YellowVibrant,
                fontWeight = FontWeight.ExtraBold,
                modifier = Modifier
                    .clickable { viewModel.navigateTo(PromoScreen.CADASTRO) }
                    .testTag("signup_link")
            )
        }
        
        Spacer(modifier = Modifier.height(12.dp))
    }
}

// ---------------- CADASTRO SCREEN ----------------
@Composable
fun CadastroScreen(viewModel: PromoViewModel) {
    var fullName by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var phone by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }
    var passwordVisible by remember { mutableStateOf(false) }
    var acceptTerms by remember { mutableStateOf(false) }

    val authError by viewModel.authError.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(BackgroundDark)
            .verticalScroll(rememberScrollState())
            .padding(24.dp)
    ) {
        Spacer(modifier = Modifier.height(16.dp))

        // Custom Top Bar styled from scratch
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(
                onClick = { viewModel.navigateBack() },
                modifier = Modifier
                    .background(CardBackground, CircleShape)
                    .size(40.dp)
                    .testTag("back_button_cadastro")
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = "Voltar",
                    tint = TextWhite
                )
            }
            Text(
                text = "Criar conta",
                style = MaterialTheme.typography.titleLarge,
                color = TextWhite,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(horizontal = 16.dp)
            )
        }

        Spacer(modifier = Modifier.height(32.dp))

        // Inputs
        OutlinedTextField(
            value = fullName,
            onValueChange = { fullName = it },
            label = { Text("Nome completo") },
            leadingIcon = { Icon(Icons.Default.Person, contentDescription = "User", tint = YellowVibrant) },
            colors = OutlinedTextFieldDefaults.colors(
                focusedTextColor = TextWhite,
                unfocusedTextColor = TextWhite,
                focusedBorderColor = YellowVibrant,
                unfocusedBorderColor = BorderColor,
                focusedLabelColor = YellowVibrant,
                unfocusedLabelColor = TextGrayMuted
            ),
            modifier = Modifier
                .fillMaxWidth()
                .testTag("register_name_input"),
            shape = RoundedCornerShape(12.dp),
            singleLine = true
        )

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = email,
            onValueChange = { email = it },
            label = { Text("E-mail") },
            leadingIcon = { Icon(Icons.Default.Mail, contentDescription = "Email", tint = YellowVibrant) },
            colors = OutlinedTextFieldDefaults.colors(
                focusedTextColor = TextWhite,
                unfocusedTextColor = TextWhite,
                focusedBorderColor = YellowVibrant,
                unfocusedBorderColor = BorderColor,
                focusedLabelColor = YellowVibrant,
                unfocusedLabelColor = TextGrayMuted
            ),
            modifier = Modifier
                .fillMaxWidth()
                .testTag("register_email_input"),
            shape = RoundedCornerShape(12.dp),
            singleLine = true
        )

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = phone,
            onValueChange = { phone = it },
            label = { Text("Telefone") },
            leadingIcon = { Icon(Icons.Default.Phone, contentDescription = "Phone", tint = YellowVibrant) },
            colors = OutlinedTextFieldDefaults.colors(
                focusedTextColor = TextWhite,
                unfocusedTextColor = TextWhite,
                focusedBorderColor = YellowVibrant,
                unfocusedBorderColor = BorderColor,
                focusedLabelColor = YellowVibrant,
                unfocusedLabelColor = TextGrayMuted
            ),
            modifier = Modifier
                .fillMaxWidth()
                .testTag("register_phone_input"),
            shape = RoundedCornerShape(12.dp),
            singleLine = true,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone)
        )

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = password,
            onValueChange = { password = it },
            label = { Text("Senha") },
            leadingIcon = { Icon(Icons.Default.Lock, contentDescription = "Lock", tint = YellowVibrant) },
            colors = OutlinedTextFieldDefaults.colors(
                focusedTextColor = TextWhite,
                unfocusedTextColor = TextWhite,
                focusedBorderColor = YellowVibrant,
                unfocusedBorderColor = BorderColor,
                focusedLabelColor = YellowVibrant,
                unfocusedLabelColor = TextGrayMuted
            ),
            visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
            modifier = Modifier
                .fillMaxWidth()
                .testTag("register_password_input"),
            shape = RoundedCornerShape(12.dp),
            singleLine = true
        )

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = confirmPassword,
            onValueChange = { confirmPassword = it },
            label = { Text("Confirmar Senha") },
            leadingIcon = { Icon(Icons.Default.Lock, contentDescription = "Lock", tint = YellowVibrant) },
            colors = OutlinedTextFieldDefaults.colors(
                focusedTextColor = TextWhite,
                unfocusedTextColor = TextWhite,
                focusedBorderColor = YellowVibrant,
                unfocusedBorderColor = BorderColor,
                focusedLabelColor = YellowVibrant,
                unfocusedLabelColor = TextGrayMuted
            ),
            visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
            modifier = Modifier
                .fillMaxWidth()
                .testTag("register_confirm_password_input"),
            shape = RoundedCornerShape(12.dp),
            singleLine = true
        )

        authError?.let {
            Spacer(modifier = Modifier.height(12.dp))
            Text(text = it, color = RedAlert, style = MaterialTheme.typography.bodyMedium, fontWeight = FontWeight.SemiBold)
        }

        Spacer(modifier = Modifier.height(20.dp))

        // Terms Checkbox
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Checkbox(
                checked = acceptTerms,
                onCheckedChange = { acceptTerms = it },
                colors = CheckboxDefaults.colors(
                    checkedColor = YellowVibrant,
                    uncheckedColor = TextGrayMuted,
                    checkmarkColor = BackgroundDark
                ),
                modifier = Modifier.testTag("terms_checkbox")
            )
            Text(
                text = "Li e aceito os Termos de Uso e Política de Privacidade",
                color = TextWhite,
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier
                    .clickable { acceptTerms = !acceptTerms }
                    .padding(start = 4.dp)
            )
        }

        Spacer(modifier = Modifier.height(28.dp))

        // Register Button
        Button(
            onClick = {
                if (!acceptTerms) {
                    viewModel.showToast("Você precisa aceitar os termos de uso.")
                } else {
                    viewModel.signUp(fullName, email, phone, password, confirmPassword)
                }
            },
            colors = ButtonDefaults.buttonColors(containerColor = YellowVibrant, contentColor = BackgroundDark),
            modifier = Modifier
                .fillMaxWidth()
                .height(52.dp)
                .testTag("register_submit_button"),
            shape = RoundedCornerShape(12.dp)
        ) {
            Text("Cadastrar", fontWeight = FontWeight.Bold, fontSize = 16.sp)
        }

        Spacer(modifier = Modifier.weight(1f))
        Spacer(modifier = Modifier.height(24.dp))

        Row(
            horizontalArrangement = Arrangement.Center,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Já tem uma conta? ", color = TextWhite)
            Text(
                text = "Entrar",
                color = YellowVibrant,
                fontWeight = FontWeight.ExtraBold,
                modifier = Modifier
                    .clickable { viewModel.navigateTo(PromoScreen.LOGIN) }
                    .testTag("login_link_from_register")
            )
        }
        
        Spacer(modifier = Modifier.height(12.dp))
    }
}

// ---------------- ESCOLHER TIPO DE CONTA SCREEN ----------------
@Composable
fun EscolherTipoContaScreen(viewModel: PromoViewModel) {
    var selectedType by remember { mutableStateOf("") } // "CLIENT" or "BUSINESS"

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(BackgroundDark)
            .padding(24.dp)
    ) {
        Spacer(modifier = Modifier.height(16.dp))

        IconButton(
            onClick = { viewModel.navigateBack() },
            modifier = Modifier
                .background(CardBackground, CircleShape)
                .size(40.dp)
                .testTag("back_button_tipo_conta")
        ) {
            Icon(
                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                contentDescription = "Voltar",
                tint = TextWhite
            )
        }

        Spacer(modifier = Modifier.height(32.dp))

        Text(
            text = "Escolha o tipo de conta",
            style = MaterialTheme.typography.displaySmall,
            color = TextWhite,
            fontWeight = FontWeight.ExtraBold,
            modifier = Modifier.fillMaxWidth()
        )
        
        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = "Selecione uma opção para continuar",
            style = MaterialTheme.typography.bodyLarge,
            color = TextGrayLight
        )

        Spacer(modifier = Modifier.height(40.dp))

        // Card 1: Empresa / Loja
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(CardBackground, RoundedCornerShape(16.dp))
                .border(
                    width = 2.dp,
                    color = if (selectedType == "BUSINESS") YellowVibrant else Color.Transparent,
                    shape = RoundedCornerShape(16.dp)
                )
                .clickable { selectedType = "BUSINESS" }
                .padding(20.dp)
                .testTag("card_tipo_empresa")
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Shop Icon circular
                Box(
                    modifier = Modifier
                        .size(52.dp)
                        .background(YellowVibrant.copy(alpha = 0.15f), CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Store,
                        contentDescription = "Estabelecimento",
                        tint = YellowVibrant,
                        modifier = Modifier.size(28.dp)
                    )
                }

                Spacer(modifier = Modifier.width(16.dp))

                Column(
                    modifier = Modifier.weight(1f)
                ) {
                    Text(
                        text = "Empresa / Loja",
                        color = TextWhite,
                        fontWeight = FontWeight.Bold,
                        style = MaterialTheme.typography.titleMedium
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "Divulgue suas promoções e alcance mais clientes",
                        color = TextGrayLight,
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        // Card 2: Cliente
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(CardBackground, RoundedCornerShape(16.dp))
                .border(
                    width = 2.dp,
                    color = if (selectedType == "CLIENT") YellowVibrant else Color.Transparent,
                    shape = RoundedCornerShape(16.dp)
                )
                .clickable { selectedType = "CLIENT" }
                .padding(20.dp)
                .testTag("card_tipo_cliente")
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .size(52.dp)
                        .background(YellowVibrant.copy(alpha = 0.15f), CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Person,
                        contentDescription = "Cliente",
                        tint = YellowVibrant,
                        modifier = Modifier.size(28.dp)
                    )
                }

                Spacer(modifier = Modifier.width(16.dp))

                Column(
                    modifier = Modifier.weight(1f)
                ) {
                    Text(
                        text = "Cliente",
                        color = TextWhite,
                        fontWeight = FontWeight.Bold,
                        style = MaterialTheme.typography.titleMedium
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "Encontre as melhores ofertas perto de você",
                        color = TextGrayLight,
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
            }
        }

        Spacer(modifier = Modifier.weight(1f))

        // Confirm Button
        Button(
            onClick = {
                if (selectedType.isEmpty()) {
                    viewModel.showToast("Selecione uma opção para continuar.")
                } else {
                    viewModel.setAccountTypeSelection(selectedType)
                }
            },
            colors = ButtonDefaults.buttonColors(containerColor = YellowVibrant, contentColor = BackgroundDark),
            modifier = Modifier
                .fillMaxWidth()
                .height(52.dp)
                .testTag("select_account_type_submit"),
            shape = RoundedCornerShape(12.dp)
        ) {
            Text("Confirmar", fontWeight = FontWeight.Bold, fontSize = 16.sp)
        }
        
        Spacer(modifier = Modifier.height(16.dp))
    }
}

// ---------------- CLIENT BOTTOM NAVIGATION CONTAINER & MAPPINGS ----------------
@Composable
fun ClientMainContainer(
    viewModel: PromoViewModel,
    activeTab: String,
    onTabSelected: (String) -> Unit,
    content: @Composable (PaddingValues) -> Unit
) {
    Scaffold(
        modifier = Modifier.fillMaxSize(),
        containerColor = BackgroundDark,
        bottomBar = {
            ClientBottomNavBar(activeTab = activeTab, onTabSelected = onTabSelected)
        }
    ) { innerPadding ->
        content(innerPadding)
    }
}

@Composable
fun ClientBottomNavBar(
    activeTab: String,
    onTabSelected: (String) -> Unit
) {
    NavigationBar(
        containerColor = BackgroundDark,
        tonalElevation = 8.dp,
        modifier = Modifier
            .border(width = 1.dp, color = BorderColor, shape = RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp))
            .windowInsetsPadding(WindowInsets.navigationBars)
    ) {
        val tabs = listOf(
            Triple("Início", Icons.Default.Home, Icons.Outlined.Home),
            Triple("Lojas", Icons.Default.Store, Icons.Outlined.Store),
            Triple("Seguindo", Icons.Default.Favorite, Icons.Outlined.FavoriteBorder),
            Triple("Alertas", Icons.Default.Notifications, Icons.Outlined.Notifications),
            Triple("Perfil", Icons.Default.AccountCircle, Icons.Outlined.AccountCircle)
        )

        tabs.forEach { (label, filledIcon, outlinedIcon) ->
            val isActive = activeTab == label
            NavigationBarItem(
                selected = isActive,
                onClick = { onTabSelected(label) },
                icon = {
                    Icon(
                        imageVector = if (isActive) filledIcon else outlinedIcon,
                        contentDescription = label,
                        tint = if (isActive) YellowVibrant else TextGrayMuted
                    )
                },
                label = {
                    Text(
                        text = label,
                        color = if (isActive) YellowVibrant else TextGrayMuted,
                        fontWeight = if (isActive) FontWeight.Bold else FontWeight.Normal,
                        fontSize = 11.sp
                    )
                },
                colors = NavigationBarItemDefaults.colors(
                    indicatorColor = YellowVibrant.copy(alpha = 0.1f)
                ),
                modifier = Modifier.testTag("nav_tab_${label.lowercase()}")
            )
        }
    }
}

// ---------------- LOCAL REUSABLE DRAW COMPONENTS ----------------
@Composable
fun PromoMiniCard(
    promo: PromoEntity,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .width(180.dp)
            .clickable(onClick = onClick)
            .testTag("promo_card_${promo.id}"),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = CardBackground)
    ) {
        Column {
            Box {
                ProductProceduralImage(productName = promo.productName, category = promo.category)
                // green badge
                Box(
                    modifier = Modifier
                        .padding(6.dp)
                        .background(GreenPromo, RoundedCornerShape(4.dp))
                        .padding(horizontal = 6.dp, vertical = 2.dp)
                        .align(Alignment.TopStart)
                ) {
                    Text(
                        text = "-${promo.discountPercent}%",
                        color = Color.White,
                        fontWeight = FontWeight.Bold,
                        fontSize = 11.sp
                    )
                }
            }
            Column(modifier = Modifier.padding(10.dp)) {
                Text(
                    text = promo.productName,
                    color = TextWhite,
                    fontWeight = FontWeight.Bold,
                    fontSize = 14.sp,
                    maxLines = 1
                )
                Spacer(modifier = Modifier.height(2.dp))
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.Store, "Store", tint = TextGrayMuted, modifier = Modifier.size(12.dp))
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = promo.storeName,
                        color = TextGrayLight,
                        fontSize = 11.sp,
                        maxLines = 1
                    )
                }
                Spacer(modifier = Modifier.height(6.dp))
                Row(
                    verticalAlignment = Alignment.Bottom,
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    Text(
                        text = "R$ ${String.format("%.2f", promo.promoPrice)}",
                        color = YellowVibrant,
                        fontWeight = FontWeight.Bold,
                        fontSize = 15.sp
                    )
                    Text(
                        text = "R$ ${String.format("%.2f", promo.normalPrice)}",
                        color = TextGrayMuted,
                        textDecoration = TextDecoration.LineThrough,
                        fontSize = 11.sp
                    )
                }
            }
        }
    }
}

// ---------------- SCREEN 5 — HOME (CLIENTE) ----------------
@Composable
fun HomeClienteScreen(viewModel: PromoViewModel) {
    val context = LocalContext.current
    val query by viewModel.searchQuery.collectAsState()
    val category by viewModel.categoryFilter.collectAsState()
    val promos by viewModel.filteredPromotions.collectAsState()
    val stores by viewModel.filteredStores.collectAsState()
    val notifications by viewModel.myNotifications.collectAsState()
    val unread = notifications.count { !it.isRead }

    val categories = listOf("Todas", "Padaria", "Açougue", "Supermercado", "Mais")

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(bottom = 80.dp)
    ) {
        // Top Header
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp, vertical = 16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(Icons.Default.LocationOn, "Localizacao", tint = YellowVibrant, modifier = Modifier.size(24.dp))
                Spacer(modifier = Modifier.width(6.dp))
                Text(
                    text = "São Paulo, SP ▼",
                    color = TextWhite,
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp,
                    modifier = Modifier.clickable { viewModel.showToast("Mudar de cidade em breve!") }
                )
            }
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .background(CardBackground, CircleShape)
                    .clickable { viewModel.navigateTo(PromoScreen.NOTIFICACOES) },
                contentAlignment = Alignment.Center
            ) {
                Icon(Icons.Default.Notifications, "Notificacoes", tint = TextWhite)
                if (unread > 0) {
                    Box(
                        modifier = Modifier
                            .align(Alignment.TopEnd)
                            .size(16.dp)
                            .background(RedAlert, CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(text = "$unread", color = Color.White, fontSize = 9.sp, fontWeight = FontWeight.Bold)
                    }
                }
            }
        }

        // Search Bar
        HorizontalSearchBar(
            query = query,
            placeholder = "Buscar ofertas e lojas",
            onQueryChanged = { viewModel.updateSearchQuery(it) }
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Categories list
        LazyRow(
            contentPadding = PaddingValues(horizontal = 16.dp),
            horizontalArrangement = Arrangement.spacedBy(10.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            items(categories) { cat ->
                val selected = category == cat
                Box(
                    modifier = Modifier
                        .background(
                            if (selected) YellowVibrant else CardBackground,
                            shape = RoundedCornerShape(20.dp)
                        )
                        .clickable { viewModel.setCategoryFilter(cat) }
                        .padding(horizontal = 16.dp, vertical = 8.dp)
                        .testTag("cat_pill_$cat")
                ) {
                    Text(
                        text = cat,
                        color = if (selected) BackgroundDark else TextWhite,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Featured Promo Section
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Promoções em destaque",
                color = TextWhite,
                fontWeight = FontWeight.Bold,
                style = MaterialTheme.typography.titleMedium
            )
            Text(
                text = "Ver todas",
                color = YellowVibrant,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.clickable { viewModel.showToast("Todas as ofertas ativadas!") }
            )
        }

        Spacer(modifier = Modifier.height(12.dp))

        if (promos.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(140.dp)
                    .padding(horizontal = 20.dp),
                contentAlignment = Alignment.Center
            ) {
                Text("Nenhuma promoção encontrada.", color = TextGrayMuted, textAlign = TextAlign.Center)
            }
        } else {
            LazyRow(
                contentPadding = PaddingValues(horizontal = 16.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                items(promos) { pr ->
                    PromoMiniCard(promo = pr, onClick = { viewModel.viewPromoDetail(pr) })
                }
            }
        }

        Spacer(modifier = Modifier.height(28.dp))

        // Near Shops Section
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Lojas próximas",
                color = TextWhite,
                fontWeight = FontWeight.Bold,
                style = MaterialTheme.typography.titleMedium
            )
            Text(
                text = "Ver vizinhas",
                color = YellowVibrant,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.clickable { viewModel.navigateTo(PromoScreen.LISTA_LOJAS) }
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        if (stores.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(120.dp),
                contentAlignment = Alignment.Center
            ) {
                Text("Nenhuma estabelecimento por perto.", color = TextGrayMuted)
            }
        } else {
            LazyRow(
                contentPadding = PaddingValues(horizontal = 16.dp),
                horizontalArrangement = Arrangement.spacedBy(20.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                items(stores) { valStore ->
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier
                            .clickable { viewModel.viewStoreDetail(valStore.id) }
                            .testTag("near_store_${valStore.id}")
                    ) {
                        CategoryAvatar(category = valStore.category, size = 64)
                        Spacer(modifier = Modifier.height(6.dp))
                        Text(
                            text = if (valStore.name.length > 12) valStore.name.take(10) + ".." else valStore.name,
                            color = TextWhite,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun HorizontalSearchBar(
    query: String,
    placeholder: String,
    onQueryChanged: (String) -> Unit
) {
    OutlinedTextField(
        value = query,
        onValueChange = onQueryChanged,
        placeholder = { Text(placeholder, color = TextGrayMuted) },
        leadingIcon = { Icon(Icons.Default.Search, contentDescription = "Buscar", tint = YellowVibrant) },
        colors = OutlinedTextFieldDefaults.colors(
            focusedTextColor = TextWhite,
            unfocusedTextColor = TextWhite,
            focusedContainerColor = CardBackground,
            unfocusedContainerColor = CardBackground,
            focusedBorderColor = YellowVibrant,
            unfocusedBorderColor = BorderColor
        ),
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp)
            .testTag("search_bar_input"),
        shape = RoundedCornerShape(12.dp),
        singleLine = true
    )
}

// ---------------- SCREEN 6 — DETALHE DA PROMOÇÃO ----------------
@Composable
fun DetalhePromocaoScreen(viewModel: PromoViewModel) {
    val promo by viewModel.selectedPromo.collectAsState()
    val follows by viewModel.myFollows.collectAsState()

    val currentPromo = promo ?: return

    val isFollowing = follows.any { it.storeId == currentPromo.storeId }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(BackgroundDark)
    ) {
        // Image Header
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
        ) {
            ProductProceduralImage(
                productName = currentPromo.productName,
                category = currentPromo.category,
                modifier = Modifier.fillMaxSize()
            )

            // Transparent Header Overlay
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp)
                    .align(Alignment.TopCenter),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(
                    onClick = { viewModel.navigateBack() },
                    modifier = Modifier.background(Color.Black.copy(alpha = 0.4f), CircleShape)
                ) {
                    Icon(Icons.AutoMirrored.Filled.ArrowBack, "Voltar", tint = Color.White)
                }

                IconButton(
                    onClick = { viewModel.showToast("Promoção compartilhada com sucesso!") },
                    modifier = Modifier.background(Color.Black.copy(alpha = 0.4f), CircleShape)
                ) {
                    Icon(Icons.Default.Share, "Compartilhar", tint = Color.White)
                }
            }

            // Green discount badge overlay
            Box(
                modifier = Modifier
                    .align(Alignment.BottomStart)
                    .padding(20.dp)
                    .background(GreenPromoDazzle, RoundedCornerShape(4.dp))
                    .padding(horizontal = 12.dp, vertical = 6.dp)
            ) {
                Text(
                    text = "DESCONTO DE -${currentPromo.discountPercent}%",
                    color = Color.White,
                    fontWeight = FontWeight.Bold,
                    fontSize = 12.sp
                )
            }
        }

        // Description / pricing information details
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(BackgroundDarkElevated, RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp))
                .padding(24.dp)
        ) {
            Text(
                text = currentPromo.productName,
                style = MaterialTheme.typography.displaySmall,
                color = TextWhite,
                fontWeight = FontWeight.ExtraBold
            )

            Spacer(modifier = Modifier.height(10.dp))

            // Clickable store row redirects to Store profile
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .clickable { viewModel.viewStoreDetail(currentPromo.storeId) }
                    .background(CardBackground, RoundedCornerShape(12.dp))
                    .padding(horizontal = 12.dp, vertical = 8.dp)
            ) {
                CategoryAvatar(category = currentPromo.category, size = 32)
                Spacer(modifier = Modifier.width(10.dp))
                Column {
                    Text(text = currentPromo.storeName, color = TextWhite, fontWeight = FontWeight.Bold, fontSize = 14.sp)
                    Text(text = "Toque para ver perfil completo", color = TextGrayLight, fontSize = 11.sp)
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Pricing Info
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Column {
                    Text(text = "Preço Promocional", color = TextGrayLight, fontSize = 12.sp)
                    Text(
                        text = "R$ ${String.format("%.2f", currentPromo.promoPrice)}",
                        color = GreenPromoDazzle,
                        fontWeight = FontWeight.Black,
                        fontSize = 32.sp
                    )
                }

                Column {
                    Text(text = "Preço Normal", color = TextGrayLight, fontSize = 12.sp)
                    Text(
                        text = "R$ ${String.format("%.2f", currentPromo.normalPrice)}",
                        color = TextGrayMuted,
                        textDecoration = TextDecoration.LineThrough,
                        fontSize = 18.sp
                    )
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            val savings = currentPromo.normalPrice - currentPromo.promoPrice
            Box(
                modifier = Modifier
                    .background(GreenPromo.copy(alpha = 0.15f), RoundedCornerShape(8.dp))
                    .border(1.dp, GreenPromo.copy(alpha = 0.4f), RoundedCornerShape(8.dp))
                    .padding(horizontal = 12.dp, vertical = 6.dp)
            ) {
                Text(
                    text = "Economize R$ ${String.format("%.2f", savings)}!",
                    color = GreenPromoDazzle,
                    fontWeight = FontWeight.Bold,
                    fontSize = 14.sp
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            // About section
            Text(text = "Descrição", color = TextWhite, fontWeight = FontWeight.Bold, fontSize = 16.sp)
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = currentPromo.description,
                color = TextGrayLight,
                style = MaterialTheme.typography.bodyMedium
            )

            Spacer(modifier = Modifier.height(20.dp))

            // Time Valid
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(Icons.Default.DateRange, "Calendario", tint = YellowVibrant, modifier = Modifier.size(20.dp))
                Spacer(modifier = Modifier.width(6.dp))
                Text(text = "Oferta válida até: ${currentPromo.validUntil}", color = YellowVibrant, fontWeight = FontWeight.Bold)
            }

            Spacer(modifier = Modifier.height(32.dp))

            // Actions footer
            Row(
                modifier = Modifier.fillMaxWidth().navigationBarsPadding(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Button(
                    onClick = { viewModel.viewStoreDetail(currentPromo.storeId) },
                    colors = ButtonDefaults.buttonColors(containerColor = YellowVibrant, contentColor = BackgroundDark),
                    modifier = Modifier
                        .weight(1.3f)
                        .height(50.dp)
                        .testTag("action_ver_loja"),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Text("Ver loja", fontWeight = FontWeight.Bold)
                }

                OutlinedButton(
                    onClick = { viewModel.toggleFollowStore(currentPromo.storeId) },
                    border = BorderStroke(1.dp, YellowVibrant),
                    colors = ButtonDefaults.outlinedButtonColors(contentColor = YellowVibrant),
                    modifier = Modifier
                        .weight(1f)
                        .height(50.dp)
                        .testTag("action_seguir_loja"),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Text(text = if (isFollowing) "√ Seguindo" else "♡ Seguir", fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}

// ---------------- SCREEN 7 — LOJAS / ESTABELECIMENTOS ----------------
@Composable
fun LojasScreen(viewModel: PromoViewModel) {
    val query by viewModel.searchQuery.collectAsState()
    val category by viewModel.categoryFilter.collectAsState()
    val stores by viewModel.filteredStores.collectAsState()

    val categories = listOf("Todas", "Padaria", "Açougue", "Supermercado", "Mais")

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(bottom = 76.dp)
    ) {
        Spacer(modifier = Modifier.height(16.dp))

        // Custom Header
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Lojas",
                style = MaterialTheme.typography.headlineMedium,
                color = TextWhite,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.weight(1f)
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Search text field
        OutlinedTextField(
            value = query,
            onValueChange = { viewModel.updateSearchQuery(it) },
            placeholder = { Text("Buscar lojas...", color = TextGrayMuted) },
            leadingIcon = { Icon(Icons.Default.Search, contentDescription = "Buscar", tint = YellowVibrant) },
            colors = OutlinedTextFieldDefaults.colors(
                focusedTextColor = TextWhite,
                unfocusedTextColor = TextWhite,
                focusedContainerColor = CardBackground,
                unfocusedContainerColor = CardBackground,
                focusedBorderColor = YellowVibrant,
                unfocusedBorderColor = BorderColor
            ),
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp)
                .testTag("stores_list_search_input"),
            shape = RoundedCornerShape(12.dp),
            singleLine = true
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Horizontal Category Filter
        LazyRow(
            contentPadding = PaddingValues(horizontal = 16.dp),
            horizontalArrangement = Arrangement.spacedBy(10.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            items(categories) { cat ->
                val selected = category == cat
                Box(
                    modifier = Modifier
                        .background(
                            if (selected) YellowVibrant else CardBackground,
                            shape = RoundedCornerShape(20.dp)
                        )
                        .clickable { viewModel.setCategoryFilter(cat) }
                        .padding(horizontal = 16.dp, vertical = 8.dp)
                ) {
                    Text(
                        text = cat,
                        color = if (selected) BackgroundDark else TextWhite,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        // List stores
        if (stores.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                contentAlignment = Alignment.Center
            ) {
                Text("Nenhuma loja corresponde a seleção.", color = TextGrayMuted)
            }
        } else {
            LazyColumn(
                contentPadding = PaddingValues(horizontal = 20.dp, vertical = 8.dp),
                verticalArrangement = Arrangement.spacedBy(14.dp),
                modifier = Modifier.weight(1f)
            ) {
                items(stores) { st ->
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { viewModel.viewStoreDetail(st.id) }
                            .testTag("store_card_${st.id}"),
                        colors = CardDefaults.cardColors(containerColor = CardBackground),
                        shape = RoundedCornerShape(14.dp)
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            CategoryAvatar(category = st.category, size = 52)
                            
                            Spacer(modifier = Modifier.width(16.dp))

                            Column(
                                modifier = Modifier.weight(1f)
                            ) {
                                Text(
                                    text = st.name,
                                    color = TextWhite,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 16.sp
                                )
                                Spacer(modifier = Modifier.height(2.dp))
                                Row(
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Text(text = st.category, color = TextGrayLight, fontSize = 12.sp)
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Icon(Icons.Default.LocationOn, "distance", tint = TextGrayMuted, modifier = Modifier.size(12.dp))
                                    Text(text = " ${st.rating.coerceIn(4.0, 5.0).toString()}00m", color = TextGrayLight, fontSize = 12.sp) // Custom representative distance
                                }
                                Spacer(modifier = Modifier.height(4.dp))
                                Row(
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Icon(Icons.Default.Star, "rating", tint = YellowVibrant, modifier = Modifier.size(14.dp))
                                    Spacer(modifier = Modifier.width(2.dp))
                                    Text(text = "${st.rating}", color = YellowVibrant, fontWeight = FontWeight.Bold, fontSize = 12.sp)
                                }
                            }

                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
                                contentDescription = "Ir",
                                tint = TextGrayMuted
                            )
                        }
                    }
                }
            }
        }
    }
}

// ---------------- SCREEN 8 — PERFIL DA LOJA ----------------
@Composable
fun PerfilLojaScreen(viewModel: PromoViewModel) {
    val store by viewModel.selectedStore.collectAsState()
    val promos by viewModel.allPromotions.collectAsState()
    val follows by viewModel.myFollows.collectAsState()

    val currentStore = store ?: return

    val storePromotions = promos.filter { it.storeId == currentStore.id }
    val isFollowing = follows.any { it.storeId == currentStore.id }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(BackgroundDark)
    ) {
        // Transparent top bar
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(
                onClick = { viewModel.navigateBack() },
                modifier = Modifier
                    .background(CardBackground, CircleShape)
                    .size(40.dp)
            ) {
                Icon(Icons.AutoMirrored.Filled.ArrowBack, "Voltar", tint = TextWhite)
            }

            IconButton(
                onClick = { viewModel.showToast("Perfil de loja compartilhado!") },
                modifier = Modifier
                    .background(CardBackground, CircleShape)
                    .size(40.dp)
            ) {
                Icon(Icons.Default.Share, "Compartilhar", tint = TextWhite)
            }
        }

        // Hero info block
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            CategoryAvatar(category = currentStore.category, size = 80)
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = currentStore.name,
                style = MaterialTheme.typography.displayMedium,
                color = TextWhite,
                fontWeight = FontWeight.ExtraBold,
                textAlign = TextAlign.Center
            )
            Text(text = currentStore.category, color = TextGrayLight, style = MaterialTheme.typography.bodyLarge)

            Spacer(modifier = Modifier.height(10.dp))

            // Rating Stars
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(Icons.Default.Star, "Rating", tint = YellowVibrant, modifier = Modifier.size(20.dp))
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                    text = "${currentStore.rating} (${currentStore.reviewCount} avaliações)",
                    color = YellowVibrant,
                    fontWeight = FontWeight.Bold,
                    fontSize = 14.sp
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Stat Counter Row
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(CardBackground, RoundedCornerShape(12.dp))
                    .padding(16.dp),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(text = "${currentStore.reviewCount}", color = TextWhite, fontWeight = FontWeight.Black, fontSize = 20.sp)
                    Text(text = "Avaliações", color = TextGrayLight, fontSize = 11.sp)
                }
                Box(modifier = Modifier.width(1.dp).height(32.dp).background(BorderColor))
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(text = "${currentStore.followersCount}", color = TextWhite, fontWeight = FontWeight.Black, fontSize = 20.sp)
                    Text(text = "Seguidores", color = TextGrayLight, fontSize = 11.sp)
                }
                Box(modifier = Modifier.width(1.dp).height(32.dp).background(BorderColor))
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(text = "${storePromotions.size}", color = TextWhite, fontWeight = FontWeight.Black, fontSize = 20.sp)
                    Text(text = "Promoções", color = TextGrayLight, fontSize = 11.sp)
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            // description about store
            Column(modifier = Modifier.fillMaxWidth(), horizontalAlignment = Alignment.Start) {
                Text(text = "Sobre a loja", color = TextWhite, fontWeight = FontWeight.Bold, fontSize = 16.sp)
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = currentStore.description,
                    color = TextGrayLight,
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        // Promotions Header
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(text = "Promoções ativas", color = TextWhite, fontWeight = FontWeight.Bold, fontSize = 18.sp)
        }

        Spacer(modifier = Modifier.height(10.dp))

        // Store promotional grid items list
        if (storePromotions.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                contentAlignment = Alignment.Center
            ) {
                Text("Sem promoções cadastradas.", color = TextGrayMuted)
            }
        } else {
            LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                contentPadding = PaddingValues(horizontal = 24.dp, vertical = 8.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp),
                modifier = Modifier.weight(1f)
            ) {
                items(storePromotions) { promoObj ->
                    Card(
                        modifier = Modifier
                            .clickable { viewModel.viewPromoDetail(promoObj) }
                            .testTag("store_promo_${promoObj.id}"),
                        shape = RoundedCornerShape(10.dp),
                        colors = CardDefaults.cardColors(containerColor = CardBackground)
                    ) {
                        Column {
                            ProductProceduralImage(productName = promoObj.productName, category = promoObj.category)
                            Column(modifier = Modifier.padding(8.dp)) {
                                Text(text = promoObj.productName, color = TextWhite, fontWeight = FontWeight.Bold, fontSize = 13.sp, maxLines = 1)
                                Spacer(modifier = Modifier.height(4.dp))
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                                ) {
                                    Text(text = "R$ ${String.format("%.2f", promoObj.promoPrice)}", color = YellowVibrant, fontWeight = FontWeight.Bold, fontSize = 13.sp)
                                    Text(text = "R$ ${String.format("%.2f", promoObj.normalPrice)}", color = TextGrayMuted, textDecoration = TextDecoration.LineThrough, fontSize = 10.sp)
                                }
                            }
                        }
                    }
                }
            }
        }

        // Floating Footer Action Bar
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(CardBackground)
                .padding(16.dp)
                .navigationBarsPadding(),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Button(
                onClick = { viewModel.toggleFollowStore(currentStore.id) },
                colors = ButtonDefaults.buttonColors(containerColor = YellowVibrant, contentColor = BackgroundDark),
                modifier = Modifier
                    .weight(1.5f)
                    .height(48.dp)
                    .testTag("store_profile_follow_action"),
                shape = RoundedCornerShape(12.dp)
            ) {
                Text(text = if (isFollowing) "√ Seguindo Loja" else "♡ Seguir", fontWeight = FontWeight.Bold, fontSize = 14.sp)
            }

            OutlinedButton(
                onClick = { viewModel.showToast("Ligando para ${currentStore.phone}...") },
                border = BorderStroke(1.dp, BorderColor),
                colors = ButtonDefaults.outlinedButtonColors(contentColor = TextWhite),
                modifier = Modifier
                    .weight(1f)
                    .height(48.dp)
                    .testTag("store_profile_phone_action"),
                shape = RoundedCornerShape(12.dp)
            ) {
                Icon(Icons.Default.Phone, "Ligar", modifier = Modifier.size(16.dp))
                Spacer(modifier = Modifier.width(6.dp))
                Text("Ligar", fontWeight = FontWeight.Bold, fontSize = 14.sp)
            }
        }
    }
}

// ---------------- SCREEN 9 — SEGUINDO ----------------
@Composable
fun SeguindoScreen(viewModel: PromoViewModel) {
    val activeTab by viewModel.seguindoTab.collectAsState()
    val follows by viewModel.myFollows.collectAsState()
    val stores by viewModel.allStores.collectAsState()
    val promos by viewModel.allPromotions.collectAsState()

    val followedStoreIds = follows.map { it.storeId }
    val followedStoresList = stores.filter { followedStoreIds.contains(it.id) }
    val followedPromosList = promos.filter { followedStoreIds.contains(it.storeId) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(bottom = 76.dp)
    ) {
        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "Seguindo",
            style = MaterialTheme.typography.headlineMedium,
            color = TextWhite,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(horizontal = 20.dp)
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Custom Nav tabs Lojas | Promocoes
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp)
                .background(CardBackground, RoundedCornerShape(20.dp))
                .padding(4.dp)
        ) {
            Box(
                modifier = Modifier
                    .weight(1f)
                    .background(if (activeTab == "Lojas") YellowVibrant else Color.Transparent, RoundedCornerShape(16.dp))
                    .clickable { viewModel.setSeguindoTab("Lojas") }
                    .padding(vertical = 10.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "Lojas",
                    color = if (activeTab == "Lojas") BackgroundDark else TextWhite,
                    fontWeight = FontWeight.Bold
                )
            }

            Box(
                modifier = Modifier
                    .weight(1f)
                    .background(if (activeTab == "Promoções") YellowVibrant else Color.Transparent, RoundedCornerShape(16.dp))
                    .clickable { viewModel.setSeguindoTab("Promoções") }
                    .padding(vertical = 10.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "Promoções",
                    color = if (activeTab == "Promoções") BackgroundDark else TextWhite,
                    fontWeight = FontWeight.Bold
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        if (activeTab == "Lojas") {
            if (followedStoresList.isEmpty()) {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text("Você ainda não segue nenhuma loja.", color = TextGrayMuted, textAlign = TextAlign.Center)
                }
            } else {
                LazyColumn(
                    contentPadding = PaddingValues(horizontal = 20.dp, vertical = 8.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    items(followedStoresList) { storeObj ->
                        val followConfig = follows.find { it.storeId == storeObj.id }
                        val alertActive = followConfig?.notificationsEnabled ?: true
                        
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .background(CardBackground, RoundedCornerShape(14.dp))
                                .clickable { viewModel.viewStoreDetail(storeObj.id) }
                                .padding(14.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            CategoryAvatar(category = storeObj.category, size = 48)
                            Spacer(modifier = Modifier.width(16.dp))
                            Column(modifier = Modifier.weight(1f)) {
                                Text(text = storeObj.name, color = TextWhite, fontWeight = FontWeight.Bold, fontSize = 16.sp)
                                Text(text = storeObj.category, color = TextGrayLight, fontSize = 12.sp)
                            }

                            // Notification Alert toggler bell
                            IconButton(
                                onClick = { viewModel.toggleFollowNotificationEnabled(storeObj.id) },
                                modifier = Modifier.testTag("toggle_notif_${storeObj.id}")
                            ) {
                                Icon(
                                    imageVector = if (alertActive) Icons.Default.NotificationsActive else Icons.Default.NotificationsOff,
                                    tint = if (alertActive) YellowVibrant else TextGrayMuted,
                                    contentDescription = "Alertas"
                                )
                            }
                        }
                    }
                }
            }
        } else {
            if (followedPromosList.isEmpty()) {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text("Nenhuma promoção ativa das lojas seguidas.", color = TextGrayMuted, textAlign = TextAlign.Center)
                }
            } else {
                LazyVerticalGrid(
                    columns = GridCells.Fixed(2),
                    contentPadding = PaddingValues(horizontal = 20.dp, vertical = 8.dp),
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    items(followedPromosList) { promoPr ->
                        PromoMiniCard(promo = promoPr, onClick = { viewModel.viewPromoDetail(promoPr) })
                    }
                }
            }
        }
    }
}

// ---------------- SCREEN 10 — NOTIFICAÇÕES (CLIENTE) ----------------
@Composable
fun NotificacoesScreen(viewModel: PromoViewModel) {
    val notifications by viewModel.myNotifications.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(bottom = 76.dp)
    ) {
        Spacer(modifier = Modifier.height(16.dp))

        // Custom header inside bottom navigation bar layout
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Notificações",
                style = MaterialTheme.typography.headlineMedium,
                color = TextWhite,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.weight(1f)
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        if (notifications.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                contentAlignment = Alignment.Center
            ) {
                Text("Nenhuma notificação por enquanto.", color = TextGrayMuted)
            }
        } else {
            LazyColumn(
                modifier = Modifier.weight(1f),
                contentPadding = PaddingValues(horizontal = 20.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(notifications) { alert ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(CardBackground, RoundedCornerShape(12.dp))
                            .padding(14.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        CategoryAvatar(category = "All", size = 40)
                        
                        Spacer(modifier = Modifier.width(12.dp))

                        Column(
                            modifier = Modifier.weight(1f)
                        ) {
                            Text(text = alert.storeName, color = TextWhite, fontWeight = FontWeight.Bold, fontSize = 14.sp)
                            Spacer(modifier = Modifier.height(2.dp))
                            Text(text = alert.message, color = TextGrayLight, fontSize = 12.sp)
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(text = alert.createdAtLabel, color = TextGrayMuted, fontSize = 10.sp)
                        }

                        if (!alert.isRead) {
                            Box(
                                modifier = Modifier
                                    .size(8.dp)
                                    .background(RedAlert, CircleShape)
                            )
                        }
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = { viewModel.markNotificationsRead() },
            colors = ButtonDefaults.buttonColors(containerColor = YellowVibrant, contentColor = BackgroundDark),
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp)
                .height(48.dp)
                .testTag("mark_all_read_button"),
            shape = RoundedCornerShape(10.dp)
        ) {
            Text("Marcar todas como lidas", fontWeight = FontWeight.Bold)
        }
    }
}

// ---------------- SCREEN 11 — PAINEL DA EMPRESA (DASHBOARD) ----------------
@Composable
fun PainelEmpresaScreen(viewModel: PromoViewModel) {
    val store by viewModel.myStore.collectAsState()
    val user by viewModel.currentUser.collectAsState()
    val promos by viewModel.allPromotions.collectAsState()

    val currentStore = store ?: return
    val currentUser = user ?: return

    val myActivePromos = promos.filter { it.storeId == currentStore.id }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(24.dp)
    ) {
        Spacer(modifier = Modifier.height(16.dp))

        // Welcome Block
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text(
                    text = "Olá, ${currentUser.name}!",
                    style = MaterialTheme.typography.displaySmall,
                    color = TextWhite,
                    fontWeight = FontWeight.ExtraBold
                )
                Text(
                    text = "Bem-vindo ao seu painel administrativo",
                    color = TextGrayLight,
                    style = MaterialTheme.typography.bodyMedium
                )
            }
            IconButton(
                onClick = { viewModel.logout() },
                modifier = Modifier.background(CardBackground, CircleShape).testTag("logout_button")
            ) {
                Icon(Icons.Default.ExitToApp, "Sair", tint = RedAlert)
            }
        }

        Spacer(modifier = Modifier.height(28.dp))

        // Stats Visual Cards
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Card(
                modifier = Modifier.weight(1f),
                colors = CardDefaults.cardColors(containerColor = YellowVibrant)
            ) {
                Column(
                    modifier = Modifier.padding(16.dp)
                ) {
                    Text(
                        text = "${myActivePromos.size}",
                        color = BackgroundDark,
                        fontWeight = FontWeight.Black,
                        fontSize = 32.sp
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(text = "Promoções ativas", color = BackgroundDark.copy(alpha = 0.8f), fontWeight = FontWeight.Bold)
                }
            }

            Card(
                modifier = Modifier.weight(1f),
                colors = CardDefaults.cardColors(containerColor = YellowMuted)
            ) {
                Column(
                    modifier = Modifier.padding(16.dp)
                ) {
                    Text(
                        text = "1.2K",
                        color = BackgroundDark,
                        fontWeight = FontWeight.Black,
                        fontSize = 32.sp
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(text = "Visualizações", color = BackgroundDark.copy(alpha = 0.8f), fontWeight = FontWeight.Bold)
                }
            }
        }

        Spacer(modifier = Modifier.height(32.dp))

        // Management list options
        Text(text = "Gerenciamento", color = TextWhite, fontWeight = FontWeight.Bold, fontSize = 18.sp)
        
        Spacer(modifier = Modifier.height(14.dp))

        val options = listOf(
            Triple("🛍️ Meus produtos", "Veja suas ofertas e remova itens expirados", "meus_produtos"),
            Triple("➕ Nova promoção", "Cadastre descontos locais para chamar clientes", "nova_promocao"),
            Triple("💬 Pedidos de informações", "Responda a dúvidas via chat direto", "pedidos_info"),
            Triple("👥 Seguidores", "${currentStore.followersCount} seguidores engajados", "seguidores"),
            Triple("📊 Estatísticas", "Relatórios e inteligência de vendas", "estatisticas"),
            Triple("💳 Plano e pagamento", "Plano ${currentUser.businessPlan} asssinado", "plano_pagamento")
        )

        Column(
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            options.forEach { (title, subtitle, key) ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(CardBackground, RoundedCornerShape(12.dp))
                        .clickable {
                            when (key) {
                                "nova_promocao" -> viewModel.navigateTo(PromoScreen.CADASTRAR_PRODUTO)
                                "plano_pagamento" -> viewModel.navigateTo(PromoScreen.PLANOS_PAGAMENTO)
                                "meus_produtos" -> viewModel.showToast("Exibindo catálogo de produtos!")
                                "pedidos_info" -> viewModel.showToast("Dúvidas locais: 0 mensagens pendentes.")
                                "seguidores" -> viewModel.showToast("Sua loja tem ${currentStore.followersCount} seguidores!")
                                "estatisticas" -> viewModel.showToast("Estatísticas consolidadas da semana disponíveis!")
                            }
                        }
                        .padding(16.dp)
                        .testTag("dashboard_opt_$key"),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text(text = title, color = TextWhite, fontWeight = FontWeight.Bold, fontSize = 15.sp)
                        Spacer(modifier = Modifier.height(2.dp))
                        Text(text = subtitle, color = TextGrayLight, fontSize = 11.sp)
                    }
                    Icon(Icons.AutoMirrored.Filled.KeyboardArrowRight, "ir", tint = TextGrayMuted)
                }
            }
        }
    }
}

// ---------------- SCREEN 12 — CADASTRAR PRODUTO / PROMOÇÃO ----------------
@Composable
fun CadastrarProdutoScreen(viewModel: PromoViewModel) {
    var productName by remember { mutableStateOf("") }
    var normalPrice by remember { mutableStateOf("") }
    var promoPrice by remember { mutableStateOf("") }
    var categorySelection by remember { mutableStateOf("Açougue") }
    var validUntil by remember { mutableStateOf("") }

    val categories = listOf("Açougue", "Padaria", "Supermercado", "Mais")

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(BackgroundDark)
            .verticalScroll(rememberScrollState())
            .padding(24.dp)
    ) {
        Spacer(modifier = Modifier.height(16.dp))

        // Header section
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(
                onClick = { viewModel.navigateBack() },
                modifier = Modifier
                    .background(CardBackground, CircleShape)
                    .size(40.dp)
            ) {
                Icon(Icons.AutoMirrored.Filled.ArrowBack, "Voltar", tint = TextWhite)
            }
            Text(
                text = "Cadastrar produto",
                style = MaterialTheme.typography.titleLarge,
                color = TextWhite,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(horizontal = 16.dp)
            )
        }

        Spacer(modifier = Modifier.height(28.dp))

        // Upload Area mock
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(150.dp)
                .background(CardBackground, RoundedCornerShape(14.dp))
                .border(2.dp, BorderColor, RoundedCornerShape(14.dp))
                .clickable { viewModel.showToast("Selecione foto da galeria do celular!") }
                .testTag("upload_image_area"),
            contentAlignment = Alignment.Center
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Icon(Icons.Default.AddAPhoto, "Camera", tint = YellowVibrant, modifier = Modifier.size(36.dp))
                Spacer(modifier = Modifier.height(8.dp))
                Text("Adicionar foto do produto", color = TextGrayLight, fontWeight = FontWeight.Bold)
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Fields
        OutlinedTextField(
            value = productName,
            onValueChange = { productName = it },
            label = { Text("Nome do produto") },
            placeholder = { Text("Ex: Coxão Mole") },
            colors = OutlinedTextFieldDefaults.colors(
                focusedTextColor = TextWhite,
                unfocusedTextColor = TextWhite,
                focusedBorderColor = YellowVibrant,
                unfocusedBorderColor = BorderColor,
                focusedLabelColor = YellowVibrant,
                unfocusedLabelColor = TextGrayMuted
            ),
            modifier = Modifier
                .fillMaxWidth()
                .testTag("input_promo_name"),
            shape = RoundedCornerShape(12.dp),
            singleLine = true
        )

        Spacer(modifier = Modifier.height(16.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            OutlinedTextField(
                value = normalPrice,
                onValueChange = { normalPrice = it },
                label = { Text("Preço normal (R$)") },
                colors = OutlinedTextFieldDefaults.colors(
                    focusedTextColor = TextWhite,
                    unfocusedTextColor = TextWhite,
                    focusedBorderColor = YellowVibrant,
                    unfocusedBorderColor = BorderColor,
                    focusedLabelColor = YellowVibrant,
                    unfocusedLabelColor = TextGrayMuted
                ),
                modifier = Modifier
                    .weight(1f)
                    .testTag("input_normal_price"),
                shape = RoundedCornerShape(12.dp),
                singleLine = true,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal)
            )

            OutlinedTextField(
                value = promoPrice,
                onValueChange = { promoPrice = it },
                label = { Text("Preço Promo (R$)") },
                colors = OutlinedTextFieldDefaults.colors(
                    focusedTextColor = TextWhite,
                    unfocusedTextColor = TextWhite,
                    focusedBorderColor = YellowVibrant,
                    unfocusedBorderColor = BorderColor,
                    focusedLabelColor = YellowVibrant,
                    unfocusedLabelColor = TextGrayMuted
                ),
                modifier = Modifier
                    .weight(1f)
                    .testTag("input_promo_price"),
                shape = RoundedCornerShape(12.dp),
                singleLine = true,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal)
            )
        }

        Spacer(modifier = Modifier.height(20.dp))

        // Categories Selection
        Text("Categoria", color = TextWhite, fontWeight = FontWeight.Bold, fontSize = 14.sp)
        Spacer(modifier = Modifier.height(8.dp))
        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            items(categories) { cat ->
                val active = categorySelection == cat
                Box(
                    modifier = Modifier
                        .background(if (active) YellowVibrant else CardBackground, RoundedCornerShape(20.dp))
                        .clickable { categorySelection = cat }
                        .padding(horizontal = 16.dp, vertical = 8.dp)
                        .testTag("form_category_$cat")
                ) {
                    Text(
                        text = cat,
                        color = if (active) BackgroundDark else TextWhite,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        // Valid until
        OutlinedTextField(
            value = validUntil,
            onValueChange = { validUntil = it },
            label = { Text("Válido até") },
            placeholder = { Text("Ex: 25/05/2026") },
            leadingIcon = { Icon(Icons.Default.DateRange, "valid", tint = YellowVibrant) },
            colors = OutlinedTextFieldDefaults.colors(
                focusedTextColor = TextWhite,
                unfocusedTextColor = TextWhite,
                focusedBorderColor = YellowVibrant,
                unfocusedBorderColor = BorderColor,
                focusedLabelColor = YellowVibrant,
                unfocusedLabelColor = TextGrayMuted
            ),
            modifier = Modifier
                .fillMaxWidth()
                .testTag("input_valid_until"),
            shape = RoundedCornerShape(12.dp),
            singleLine = true
        )

        Spacer(modifier = Modifier.height(40.dp))

        // Save
        Button(
            onClick = {
                viewModel.registerPromotion(
                    name = productName,
                    normalPriceStr = normalPrice,
                    promoPriceStr = promoPrice,
                    category = categorySelection,
                    validUntil = validUntil
                )
            },
            colors = ButtonDefaults.buttonColors(containerColor = YellowVibrant, contentColor = BackgroundDark),
            modifier = Modifier
                .fillMaxWidth()
                .height(52.dp)
                .testTag("button_save_promo"),
            shape = RoundedCornerShape(12.dp)
        ) {
            Text("Salvar produto", fontWeight = FontWeight.Bold, fontSize = 16.sp)
        }
    }
}

// ---------------- SCREEN 13 — PLANO DE PAGAMENTO ----------------
@Composable
fun PlanosPagamentoScreen(viewModel: PromoViewModel) {
    val planCycle by viewModel.planCycle.collectAsState()
    val activePlan by viewModel.selectedPlan.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(BackgroundDark)
            .verticalScroll(rememberScrollState())
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(16.dp))

        // Row Top Bar
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(
                onClick = { viewModel.navigateBack() },
                modifier = Modifier
                    .background(CardBackground, CircleShape)
                    .size(40.dp)
            ) {
                Icon(Icons.AutoMirrored.Filled.ArrowBack, "Voltar", tint = TextWhite)
            }
            Text(
                text = "Plano de pagamento",
                style = MaterialTheme.typography.titleLarge,
                color = TextWhite,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(horizontal = 16.dp)
            )
        }

        Spacer(modifier = Modifier.height(20.dp))

        Text(
            text = "Escolha o plano ideal para sua empresa",
            color = TextGrayLight,
            style = MaterialTheme.typography.bodyLarge,
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(28.dp))

        // Toggle Billing Cycle
        Row(
            modifier = Modifier
                .background(CardBackground, RoundedCornerShape(24.dp))
                .padding(4.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .background(
                        if (planCycle == "Mensal") YellowVibrant else Color.Transparent,
                        RoundedCornerShape(20.dp)
                    )
                    .clickable { viewModel.togglePlanCycle() }
                    .padding(horizontal = 20.dp, vertical = 8.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "Mensal",
                    color = if (planCycle == "Mensal") BackgroundDark else TextWhite,
                    fontWeight = FontWeight.Bold
                )
            }

            Box(
                modifier = Modifier
                    .background(
                        if (planCycle == "Anual") YellowVibrant else Color.Transparent,
                        RoundedCornerShape(20.dp)
                    )
                    .clickable { viewModel.togglePlanCycle() }
                    .padding(horizontal = 20.dp, vertical = 8.dp),
                contentAlignment = Alignment.Center
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = "Anual",
                        color = if (planCycle == "Anual") BackgroundDark else TextWhite,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Box(
                        modifier = Modifier
                            .background(GreenPromo, CircleShape)
                            .padding(horizontal = 4.dp, vertical = 1.dp)
                    ) {
                        Text("-50%", color = Color.White, fontSize = 8.sp, fontWeight = FontWeight.Bold)
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(32.dp))

        // Plan Cards
        // Card 1 — Básico
        PlanSelectionCard(
            title = "Plano Básico",
            price = if (planCycle == "Anual") "R$ 14,90" else "R$ 29,90",
            features = listOf("Até 10 produtos", "Promoções ilimitadas"),
            tagHighlight = "Mais Popular",
            isActiveSelection = activePlan == "BASIC",
            onClick = { viewModel.selectPlan("BASIC") },
            testTagKey = "plan_basic"
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Card 2 — Profissional
        PlanSelectionCard(
            title = "Plano Profissional",
            price = if (planCycle == "Anual") "R$ 29,90" else "R$ 59,90",
            features = listOf("Até 50 produtos", "Promoções ilimitadas", "Destaque prioritário na busca"),
            isActiveSelection = activePlan == "PROFESSIONAL",
            onClick = { viewModel.selectPlan("PROFESSIONAL") },
            testTagKey = "plan_professional"
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Card 3 — Premium
        PlanSelectionCard(
            title = "Plano Premium",
            price = if (planCycle == "Anual") "R$ 49,90" else "R$ 99,90",
            features = listOf("Produtos ilimitados", "Destaque prioritário na busca", "Relatórios e estatísticas avançados"),
            isActiveSelection = activePlan == "PREMIUM",
            onClick = { viewModel.selectPlan("PREMIUM") },
            testTagKey = "plan_premium"
        )

        Spacer(modifier = Modifier.height(40.dp))

        // CTA
        Button(
            onClick = { viewModel.selectAndConfirmPlan(activePlan) },
            colors = ButtonDefaults.buttonColors(containerColor = YellowVibrant, contentColor = BackgroundDark),
            modifier = Modifier
                .fillMaxWidth()
                .height(52.dp)
                .testTag("confirm_plan_cta"),
            shape = RoundedCornerShape(12.dp)
        ) {
            Text("Continuar", fontWeight = FontWeight.Bold, fontSize = 16.sp)
        }
    }
}

@Composable
fun PlanSelectionCard(
    title: String,
    price: String,
    features: List<String>,
    tagHighlight: String? = null,
    isActiveSelection: Boolean,
    onClick: () -> Unit,
    testTagKey: String
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(CardBackground, RoundedCornerShape(16.dp))
            .border(
                width = 2.dp,
                color = if (isActiveSelection) YellowVibrant else Color.Transparent,
                shape = RoundedCornerShape(16.dp)
            )
            .clickable(onClick = onClick)
            .padding(20.dp)
            .testTag(testTagKey)
    ) {
        Column {
            tagHighlight?.let {
                Box(
                    modifier = Modifier
                        .background(YellowVibrant, RoundedCornerShape(4.dp))
                        .padding(horizontal = 8.dp, vertical = 2.dp)
                ) {
                    Text(text = it, color = BackgroundDark, fontWeight = FontWeight.Bold, fontSize = 10.sp)
                }
                Spacer(modifier = Modifier.height(8.dp))
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(text = title, color = TextWhite, fontWeight = FontWeight.Bold, fontSize = 20.sp)
                    Spacer(modifier = Modifier.height(2.dp))
                    Row(verticalAlignment = Alignment.Bottom) {
                        Text(text = price, color = YellowVibrant, fontWeight = FontWeight.ExtraBold, fontSize = 24.sp)
                        Text(text = "/mês", color = TextGrayLight, fontSize = 14.sp)
                    }
                }

                // Check circle
                Box(
                    modifier = Modifier
                        .size(24.dp)
                        .background(if (isActiveSelection) YellowVibrant else Color.Transparent, CircleShape)
                        .border(2.dp, YellowVibrant, CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    if (isActiveSelection) {
                        Icon(Icons.Default.Check, "Selected", tint = BackgroundDark, modifier = Modifier.size(16.dp))
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))
            HorizontalDivider(color = BorderColor)
            Spacer(modifier = Modifier.height(12.dp))

            Column(
                verticalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                features.forEach { feature ->
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.CheckCircle, "check", tint = GreenPromo, modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(text = feature, color = TextGrayLight, fontSize = 13.sp)
                    }
                }
            }
        }
    }
}
