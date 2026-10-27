package com.example.ui.screens

import android.content.Intent
import android.net.Uri
import android.provider.Settings
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.ui.platform.LocalContext
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Bolt
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Folder
import androidx.compose.material.icons.filled.FolderOpen
import androidx.compose.material.icons.filled.SdCard
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Smartphone
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.data.LauncherUiState
import com.example.model.DriverProfile
import com.example.model.GameExecutable
import com.example.ui.LauncherViewModel
import com.example.ui.theme.KaguneAwakeningGlow
import com.example.ui.theme.KaguneCrimson
import com.example.ui.theme.QuinqueAccent
import com.example.ui.theme.QuinqueSilver
import com.example.ui.theme.StatusGreen

// Sleek dark-blue/cyan palette matching the user's reference photo
val LauncherCardBg = Color(0xFF101C30)
val LauncherCardBorder = Color(0xFF1E3557)
val LauncherActivePill = Color(0xFF285494)
val LauncherInactivePill = Color(0xFF0C1626)
val LauncherNeonBlue = Color(0xFF4DA6FF)

@Composable
fun LauncherHomeScreen(
    uiState: LauncherUiState,
    viewModel: LauncherViewModel,
    onNavigateToDrivers: () -> Unit,
    onNavigateToControls: () -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val scrollState = rememberScrollState()
    var showFileSelectorDialog by remember { mutableStateOf(false) }
    var showSaveProfileDialog by remember { mutableStateOf(false) }
    var showResolutionModal by remember { mutableStateOf(false) }

    // Dialog form states
    var newProfileName by remember { mutableStateOf("Mi Modo Personalizado") }
    var newProfileDesc by remember { mutableStateOf("Ajuste optimizado para mi dispositivo") }
    var newProfileIcon by remember { mutableStateOf("⚡") }

    var customExeTitle by remember { mutableStateOf("Tokyo Ghoul (Ruta Personalizada)") }
    var customExePath by remember { mutableStateOf("/storage/emulated/0/Games/Tokyo Ghoul re Call to Exist/TokyoGhoul_re_CallToExist.exe") }
    var customIsExternal by remember { mutableStateOf(false) }

    // Android System File Picker for any .exe / ISO / Zip
    val filePickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        uri?.let {
            val fileName = it.lastPathSegment?.substringAfterLast("/") ?: "TokyoGhoul_re_CallToExist.exe"
            val pickedPath = it.path ?: "/storage/emulated/0/Download/$fileName"
            val isExternal = pickedPath.contains("external") || pickedPath.contains("storage/") && !pickedPath.contains("emulated/0")
            viewModel.addGameExecutable(
                title = fileName,
                path = pickedPath,
                isExternal = isExternal,
                locationLabel = if (isExternal) "Tarjeta SD / Externa" else "Almacenamiento Interno"
            )
        }
    }

    val infiniteTransition = rememberInfiniteTransition(label = "pulse_kakugan")
    val kakuganPulse by infiniteTransition.animateFloat(
        initialValue = 0.95f,
        targetValue = 1.05f,
        animationSpec = infiniteRepeatable(
            animation = tween(1400, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "kakugan_scale"
    )

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    listOf(
                        Color(0xFF070E1A),
                        Color(0xFF091424),
                        Color(0xFF050B14)
                    )
                )
            )
            .verticalScroll(scrollState)
            .padding(horizontal = 14.dp, vertical = 10.dp)
            .testTag("launcher_home_screen"),
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        // TOP HEADER - MATCHING PHOTO EXACTLY WITH USER COVER ART
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = LauncherCardBg.copy(alpha = 0.85f)),
            border = androidx.compose.foundation.BorderStroke(1.dp, LauncherCardBorder)
        ) {
            Column(modifier = Modifier.padding(14.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    // Big Pill Action Button: JOGAR / EJECUTAR
                    Surface(
                        onClick = { viewModel.launchGame() },
                        shape = RoundedCornerShape(26.dp),
                        color = Color(0xFF0B2144),
                        border = androidx.compose.foundation.BorderStroke(2.dp, LauncherNeonBlue),
                        modifier = Modifier
                            .height(52.dp)
                            .width(125.dp)
                            .shadow(10.dp, RoundedCornerShape(26.dp), spotColor = LauncherNeonBlue)
                            .testTag("btn_launch_jogar")
                    ) {
                        Box(contentAlignment = Alignment.Center) {
                            Text(
                                text = "JOGAR",
                                color = Color.White,
                                fontSize = 18.sp,
                                fontWeight = FontWeight.Black,
                                letterSpacing = 2.sp
                            )
                        }
                    }

                    // Game Title & Japanese Subtitle
                    Column(
                        modifier = Modifier
                            .weight(1f)
                            .padding(horizontal = 8.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = uiState.gameConfig.gameTitle,
                            color = Color.White,
                            fontSize = 19.sp,
                            fontWeight = FontWeight.Black,
                            letterSpacing = 1.5.sp
                        )
                        Text(
                            text = uiState.gameConfig.gameSubtitle,
                            color = LauncherNeonBlue,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.ExtraBold,
                            letterSpacing = 1.sp
                        )
                        Text(
                            text = uiState.gameConfig.characterTagline,
                            color = Color(0xFF8BA5CC),
                            fontSize = 8.sp,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                    }

                    // Official Tokyo Ghoul:re Call to Exist Icon (user attachment)
                    Box(
                        modifier = Modifier
                            .size(60.dp)
                            .scale(kakuganPulse)
                            .clip(RoundedCornerShape(14.dp))
                            .background(Color(0xFF0A1220))
                            .border(1.5.dp, LauncherNeonBlue, RoundedCornerShape(14.dp))
                            .shadow(8.dp, RoundedCornerShape(14.dp), spotColor = LauncherNeonBlue)
                            .testTag("internal_box_art_icon"),
                        contentAlignment = Alignment.Center
                    ) {
                        Image(
                            painter = painterResource(id = R.drawable.tg_box_art),
                            contentDescription = "Tokyo Ghoul re Call to Exist Box Art Icon",
                            modifier = Modifier
                                .fillMaxSize()
                                .clip(RoundedCornerShape(14.dp)),
                            contentScale = ContentScale.Crop
                        )
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))

                // EXECUTABLE PATH WITH FILE SELECTION TRIGGER
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { showFileSelectorDialog = true }
                        .padding(vertical = 2.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = "${uiState.gameConfig.exePath} • toque para trocar",
                        color = Color(0xFF7FA2CE),
                        fontSize = 10.5.sp,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        modifier = Modifier.weight(1f)
                    )
                    Surface(
                        shape = RoundedCornerShape(8.dp),
                        color = Color(0xFF162D4D),
                        border = androidx.compose.foundation.BorderStroke(1.dp, LauncherNeonBlue.copy(alpha = 0.6f))
                    ) {
                        Text(
                            text = "CAMBIAR",
                            color = Color.White,
                            fontSize = 9.sp,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.padding(horizontal = 6.dp, vertical = 3.dp)
                        )
                    }
                }

                // FOOTER LINKS: Licenças • Enviar log • Restaurar padrões
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 4.dp),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Text(
                        text = "Licenças",
                        color = Color(0xFF5A7EA8),
                        fontSize = 10.sp,
                        modifier = Modifier.clickable { }
                    )
                    Text(
                        text = "Enviar log",
                        color = Color(0xFF5A7EA8),
                        fontSize = 10.sp,
                        modifier = Modifier.clickable { }
                    )
                    Text(
                        text = "Restaurar padrões",
                        color = LauncherNeonBlue,
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.clickable { viewModel.restoreDefaults() }
                    )
                    Text(
                        text = "• Fijar como Lanzador Home",
                        color = Color(0xFF64B5F6),
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.clickable {
                            try {
                                val intent = Intent(Settings.ACTION_HOME_SETTINGS)
                                context.startActivity(intent)
                            } catch (_: Exception) {
                                try {
                                    val intent = Intent(Settings.ACTION_SETTINGS)
                                    context.startActivity(intent)
                                } catch (_: Exception) {}
                            }
                        }
                    )
                }
            }
        }

        // QUICK PROFILE SELECTOR (PERFILES DE DRIVERS & RENDIMIENTO)
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .testTag("profiles_quick_selector_card"),
            shape = RoundedCornerShape(14.dp),
            colors = CardDefaults.cardColors(containerColor = LauncherCardBg),
            border = androidx.compose.foundation.BorderStroke(1.dp, LauncherCardBorder)
        ) {
            Column(modifier = Modifier.padding(10.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        Text(
                            text = "PERFILES DE DRIVERS Y RENDIMIENTO",
                            color = Color.White,
                            fontSize = 11.5.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Surface(
                            shape = RoundedCornerShape(6.dp),
                            color = LauncherActivePill
                        ) {
                            Text(
                                text = uiState.gameConfig.activeProfileName,
                                color = LauncherNeonBlue,
                                fontSize = 9.sp,
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                            )
                        }
                    }

                    IconButton(
                        onClick = { showSaveProfileDialog = true },
                        modifier = Modifier.size(24.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Add,
                            contentDescription = "Guardar Perfil",
                            tint = LauncherNeonBlue,
                            modifier = Modifier.size(18.dp)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))

                LazyRow(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(uiState.profiles) { profile ->
                        val isSelected = uiState.activeProfileId == profile.id
                        Surface(
                            onClick = { viewModel.applyProfile(profile.id) },
                            shape = RoundedCornerShape(10.dp),
                            color = if (isSelected) LauncherActivePill else LauncherInactivePill,
                            border = androidx.compose.foundation.BorderStroke(
                                1.5.dp,
                                if (isSelected) LauncherNeonBlue else LauncherCardBorder
                            ),
                            modifier = Modifier.testTag("profile_chip_${profile.id}")
                        ) {
                            Row(
                                modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(4.dp)
                            ) {
                                Text(text = profile.iconEmoji, fontSize = 12.sp)
                                Column {
                                    Text(
                                        text = profile.name,
                                        color = if (isSelected) Color.White else Color(0xFF7FA2CE),
                                        fontSize = 10.5.sp,
                                        fontWeight = FontWeight.Bold
                                    )
                                    Text(
                                        text = "${profile.resolution} • ${profile.driverName.take(12)}",
                                        color = if (isSelected) Color(0xFFB0D2FF) else Color(0xFF4A688C),
                                        fontSize = 8.sp
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }

        // OFFLINE MODE STATUS BANNER
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .testTag("offline_mode_banner"),
            shape = RoundedCornerShape(14.dp),
            colors = CardDefaults.cardColors(containerColor = Color(0xFF0C1B2E)),
            border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFF1E4670))
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 12.dp, vertical = 9.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier.weight(1f)
                ) {
                    Box(
                        modifier = Modifier
                            .size(10.dp)
                            .clip(CircleShape)
                            .background(StatusGreen)
                    )
                    Column {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            Text(
                                text = "MODO OFFLINE 100% ACTIVO",
                                color = Color.White,
                                fontSize = 11.5.sp,
                                fontWeight = FontWeight.Bold
                            )
                            Surface(
                                shape = RoundedCornerShape(4.dp),
                                color = StatusGreen.copy(alpha = 0.2f)
                            ) {
                                Text(
                                    text = "LOCAL",
                                    color = StatusGreen,
                                    fontSize = 8.sp,
                                    fontWeight = FontWeight.Black,
                                    modifier = Modifier.padding(horizontal = 4.dp, vertical = 1.dp)
                                )
                            }
                        }
                        Text(
                            text = "Juego y emulador listos sin conexión a internet • Steam DRM Bypass activado",
                            color = Color(0xFF8BA5CC),
                            fontSize = 9.sp
                        )
                    }
                }
                Switch(
                    checked = uiState.gameConfig.isOfflineModeActive,
                    onCheckedChange = { viewModel.toggleOfflineMode(it) },
                    colors = SwitchDefaults.colors(
                        checkedThumbColor = Color.White,
                        checkedTrackColor = StatusGreen,
                        uncheckedTrackColor = LauncherInactivePill
                    ),
                    modifier = Modifier.scale(0.8f)
                )
            }
        }

        // SNAPDRAGON 865 COMPATIBILITY & OPTIMIZATION CARD
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .testTag("sd865_compatibility_card"),
            shape = RoundedCornerShape(14.dp),
            colors = CardDefaults.cardColors(
                containerColor = if (uiState.activeProfileId == "profile_snapdragon_865")
                    Color(0xFF162A47) else LauncherCardBg
            ),
            border = androidx.compose.foundation.BorderStroke(
                1.5.dp,
                if (uiState.activeProfileId == "profile_snapdragon_865")
                    LauncherNeonBlue else Color(0xFF23446B)
            )
        ) {
            Column(modifier = Modifier.padding(12.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        Text(text = "🔥", fontSize = 16.sp)
                        Column {
                            Text(
                                text = "COMPATIBLE CON SNAPDRAGON 865",
                                color = Color.White,
                                fontSize = 12.5.sp,
                                fontWeight = FontWeight.Black,
                                letterSpacing = 0.5.sp
                            )
                            Text(
                                text = "GPU Adreno 650 • CPU Kryo 585 Octa-Core",
                                color = LauncherNeonBlue,
                                fontSize = 9.5.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }

                    if (uiState.activeProfileId == "profile_snapdragon_865") {
                        Surface(
                            shape = RoundedCornerShape(8.dp),
                            color = StatusGreen.copy(alpha = 0.25f),
                            border = androidx.compose.foundation.BorderStroke(1.dp, StatusGreen)
                        ) {
                            Text(
                                text = "✓ PERFIL ACTIVO",
                                color = StatusGreen,
                                fontSize = 9.sp,
                                fontWeight = FontWeight.Black,
                                modifier = Modifier.padding(horizontal = 6.dp, vertical = 4.dp)
                            )
                        }
                    } else {
                        Button(
                            onClick = { viewModel.applySnapdragon865Preset() },
                            colors = ButtonDefaults.buttonColors(containerColor = LauncherActivePill),
                            shape = RoundedCornerShape(8.dp),
                            modifier = Modifier.height(30.dp)
                        ) {
                            Text("Aplicar SD865", fontSize = 9.5.sp, fontWeight = FontWeight.Bold)
                        }
                    }
                }

                Spacer(modifier = Modifier.height(6.dp))

                Text(
                    text = "Ajuste específico para Adreno 650: Turnip v24.2 + DXVK 2.3.1 Async + FSR 540p/720p. Elimina micro-tirones y previene sobrecalentamiento en Tokyo Ghoul.",
                    color = Color(0xFF8BA5CC),
                    fontSize = 10.sp
                )
            }
        }

        // PERFORMANCE MODE BANNER (MODO DESEMPENHO)
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(14.dp),
            colors = CardDefaults.cardColors(containerColor = LauncherCardBg),
            border = androidx.compose.foundation.BorderStroke(1.dp, LauncherCardBorder)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 14.dp, vertical = 10.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Bolt,
                            contentDescription = "Desempenho",
                            tint = Color(0xFFFFD54F),
                            modifier = Modifier.size(18.dp)
                        )
                        Text(
                            text = "MODO DESEMPENHO",
                            color = Color.White,
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Black,
                            letterSpacing = 0.5.sp
                        )
                    }
                    Text(
                        text = "24-60 FPS estáveis e mais leve. Ligue se o jogo não segura 30 FPS.",
                        color = Color(0xFF8BA5CC),
                        fontSize = 10.sp
                    )
                }
                Switch(
                    checked = uiState.gameConfig.performanceMode,
                    onCheckedChange = { viewModel.togglePerformanceMode(it) },
                    colors = SwitchDefaults.colors(
                        checkedThumbColor = Color.White,
                        checkedTrackColor = LauncherNeonBlue,
                        uncheckedTrackColor = LauncherInactivePill
                    )
                )
            }
        }

        // TWO-COLUMN CONFIGURATION GRID
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            // LEFT COLUMN
            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                // 1. Resolução (con acceso rápido y botón modal para más opciones)
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(14.dp),
                    colors = CardDefaults.cardColors(containerColor = LauncherCardBg),
                    border = androidx.compose.foundation.BorderStroke(1.dp, LauncherCardBorder)
                ) {
                    Column(modifier = Modifier.padding(10.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "Resolução",
                                color = Color.White,
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold
                            )
                            Text(
                                text = "Personalizar",
                                color = LauncherNeonBlue,
                                fontSize = 9.5.sp,
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier.clickable { showResolutionModal = true }
                            )
                        }
                        Spacer(modifier = Modifier.height(8.dp))
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(4.dp)
                        ) {
                            listOf(
                                PillOption("360p", "+FPS"),
                                PillOption("540p", "leve"),
                                PillOption("720p", "padrão"),
                                PillOption("1080p", "nítido")
                            ).forEach { opt ->
                                val isSelected = uiState.gameConfig.resolution.equals(opt.key, ignoreCase = true)
                                Surface(
                                    onClick = { viewModel.setResolution(opt.key) },
                                    shape = RoundedCornerShape(12.dp),
                                    color = if (isSelected) LauncherActivePill else LauncherInactivePill,
                                    border = androidx.compose.foundation.BorderStroke(
                                        1.dp,
                                        if (isSelected) LauncherNeonBlue else Color.Transparent
                                    ),
                                    modifier = Modifier
                                        .weight(1f)
                                        .testTag("pill_${opt.key}")
                                ) {
                                    Column(
                                        modifier = Modifier.padding(vertical = 6.dp, horizontal = 2.dp),
                                        horizontalAlignment = Alignment.CenterHorizontally
                                    ) {
                                        Text(
                                            text = opt.key,
                                            color = if (isSelected) Color.White else Color(0xFF7FA2CE),
                                            fontSize = 11.sp,
                                            fontWeight = FontWeight.Bold,
                                            maxLines = 1
                                        )
                                        if (opt.subtitle.isNotEmpty()) {
                                            Text(
                                                text = opt.subtitle,
                                                color = if (isSelected) Color(0xFF90CAF9) else Color(0xFF4A688C),
                                                fontSize = 8.5.sp,
                                                maxLines = 1
                                            )
                                        }
                                    }
                                }
                            }
                        }
                    }
                }

                // 2. Tela (Aspect Ratio)
                ConfigPillCard(
                    title = "Tela",
                    options = listOf(
                        PillOption("16:9", ""),
                        PillOption("Esticar", "")
                    ),
                    selectedKey = uiState.gameConfig.screenAspect,
                    onSelect = { viewModel.setScreenAspect(it) }
                )

                // 3. Painel de FPS
                ConfigToggleCard(
                    title = "Painel de FPS",
                    subtitle = if (uiState.gameConfig.showFpsPanel)
                        "${uiState.combat.currentFps} FPS • GPU ${uiState.combat.gpuUsagePercent}% (Flotante)"
                    else "mostra FPS, CPU e GPU",
                    checked = uiState.gameConfig.showFpsPanel,
                    onCheckedChange = { viewModel.toggleFpsPanel(it) }
                )

                // 4. Save do jogo
                ConfigActionCard(
                    title = "Save do jogo",
                    subtitle = uiState.gameConfig.saveInfo,
                    actionLabel = "Importar",
                    onAction = { viewModel.importSaveGame() }
                )
            }

            // RIGHT COLUMN
            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                // 1. Antisserrilhado
                ConfigPillCard(
                    title = "Antisserrilhado",
                    options = listOf(
                        PillOption("Off", ""),
                        PillOption("FXAA", ""),
                        PillOption("FXAA+", "")
                    ),
                    selectedKey = uiState.gameConfig.antiAliasing,
                    onSelect = { viewModel.setAntiAliasing(it) }
                )

                // 2. Escala na tela (FSR / CAS)
                ConfigPillCard(
                    title = "Escala na tela",
                    options = listOf(
                        PillOption("Normal", ""),
                        PillOption("FSR", ""),
                        PillOption("CAS", "")
                    ),
                    selectedKey = uiState.gameConfig.upscalingMode,
                    onSelect = { viewModel.setUpscaling(it) }
                )

                // 3. Save 100%
                ConfigToggleCard(
                    title = "Save 100%",
                    subtitle = "tudo liberado; o seu save fica a salvo",
                    checked = uiState.gameConfig.unlockSave100,
                    onCheckedChange = { viewModel.toggleSave100(it) }
                )

                // 4. Driver da GPU
                ConfigActionCard(
                    title = "Driver da GPU",
                    subtitle = uiState.gameConfig.selectedDriverName,
                    actionLabel = "Trocar",
                    onAction = { onNavigateToDrivers() }
                )
            }
        }

        // FULL WIDTH: CONTROLES CARD
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(14.dp),
            colors = CardDefaults.cardColors(containerColor = LauncherCardBg),
            border = androidx.compose.foundation.BorderStroke(1.dp, LauncherCardBorder)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 14.dp, vertical = 12.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = "Controles",
                        color = Color.White,
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = "${uiState.gameConfig.controllerProfileName} • Layout Táctil Kagune Adaptado",
                        color = Color(0xFF8BA5CC),
                        fontSize = 10.5.sp
                    )
                }

                Surface(
                    onClick = onNavigateToControls,
                    shape = RoundedCornerShape(18.dp),
                    color = LauncherActivePill,
                    border = androidx.compose.foundation.BorderStroke(1.dp, LauncherNeonBlue),
                    modifier = Modifier.testTag("btn_mapear_controles")
                ) {
                    Text(
                        text = "MAPEAR",
                        color = Color.White,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Black,
                        letterSpacing = 1.sp,
                        modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
                    )
                }
            }
        }

        // BOTTOM TIP ADVICE
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(10.dp))
                .background(Color(0xFF091426))
                .border(1.dp, Color(0xFF142742), RoundedCornerShape(10.dp))
                .padding(10.dp)
        ) {
            Text(
                text = "Toque numa opção; cada uma faz um jutsu/técnica. ⚠ FXAA+, FSR e CAS pesam; se o FPS cair, use Off e Normal.",
                color = Color(0xFF7FA2CE),
                fontSize = 10.5.sp,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth()
            )
        }
    }

    // MODAL 1: FILE / STORAGE SELECTOR (INTERNAL & EXTERNAL)
    if (showFileSelectorDialog) {
        AlertDialog(
            onDismissRequest = { showFileSelectorDialog = false },
            title = {
                Text(
                    text = "Seleccionar Archivo del Juego",
                    color = Color.White,
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp
                )
            },
            text = {
                Column(
                    modifier = Modifier.verticalScroll(rememberScrollState()),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Text(
                        text = "Elige un ejecutable (.exe) o archivo desde tu almacenamiento interno o externo:",
                        color = Color(0xFF8BA5CC),
                        fontSize = 11.5.sp
                    )

                    // Button: Open Native Android File Explorer (SAF)
                    Button(
                        onClick = {
                            showFileSelectorDialog = false
                            filePickerLauncher.launch("*/*")
                        },
                        modifier = Modifier.fillMaxWidth(),
                        colors = ButtonDefaults.buttonColors(containerColor = LauncherActivePill),
                        shape = RoundedCornerShape(10.dp)
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Icon(imageVector = Icons.Default.FolderOpen, contentDescription = "Examinar")
                            Text("Abrir Explorador de Archivos de Android", fontSize = 11.5.sp, fontWeight = FontWeight.Bold)
                        }
                    }

                    Text(
                        text = "Rutas Detectadas / Agregadas:",
                        color = Color.White,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold
                    )

                    uiState.gameConfig.addedExecutables.forEach { exe ->
                        Surface(
                            onClick = {
                                viewModel.selectGameExecutable(exe.id)
                                showFileSelectorDialog = false
                            },
                            shape = RoundedCornerShape(10.dp),
                            color = if (exe.isSelected) LauncherActivePill else LauncherInactivePill,
                            border = androidx.compose.foundation.BorderStroke(
                                1.dp,
                                if (exe.isSelected) LauncherNeonBlue else LauncherCardBorder
                            ),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Row(
                                modifier = Modifier.padding(10.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Row(
                                    modifier = Modifier.weight(1f),
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                                ) {
                                    Icon(
                                        imageVector = if (exe.isExternalStorage) Icons.Default.SdCard else Icons.Default.Smartphone,
                                        contentDescription = "Tipo almacenamiento",
                                        tint = if (exe.isSelected) LauncherNeonBlue else Color(0xFF7FA2CE)
                                    )
                                    Column {
                                        Text(
                                            text = exe.title,
                                            color = Color.White,
                                            fontSize = 12.sp,
                                            fontWeight = FontWeight.Bold
                                        )
                                        Text(
                                            text = exe.path,
                                            color = Color(0xFF8BA5CC),
                                            fontSize = 9.sp,
                                            maxLines = 1,
                                            overflow = TextOverflow.Ellipsis
                                        )
                                        Text(
                                            text = "${exe.storageLocationLabel} • ${exe.fileSizeFormatted}",
                                            color = if (exe.isSelected) LauncherNeonBlue else Color(0xFF5A7EA8),
                                            fontSize = 8.5.sp
                                        )
                                    }
                                }
                                if (exe.isSelected) {
                                    Icon(
                                        imageVector = Icons.Default.Check,
                                        contentDescription = "Activo",
                                        tint = StatusGreen
                                    )
                                }
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(4.dp))

                    Text(
                        text = "O ingresar ruta manual:",
                        color = Color.White,
                        fontSize = 11.5.sp,
                        fontWeight = FontWeight.Bold
                    )

                    OutlinedTextField(
                        value = customExePath,
                        onValueChange = { customExePath = it },
                        modifier = Modifier.fillMaxWidth(),
                        label = { Text("Ruta interna o externa", fontSize = 11.sp) },
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = LauncherNeonBlue,
                            unfocusedBorderColor = LauncherCardBorder,
                            focusedTextColor = Color.White,
                            unfocusedTextColor = Color.White
                        ),
                        singleLine = true
                    )

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text("¿Es tarjeta MicroSD / USB externo?", color = Color(0xFF8BA5CC), fontSize = 11.sp)
                        Switch(
                            checked = customIsExternal,
                            onCheckedChange = { customIsExternal = it },
                            colors = SwitchDefaults.colors(checkedThumbColor = Color.White, checkedTrackColor = LauncherNeonBlue)
                        )
                    }

                    Button(
                        onClick = {
                            val name = customExePath.substringAfterLast("/").ifBlank { "TokyoGhoul_re_CallToExist.exe" }
                            viewModel.addGameExecutable(
                                title = name,
                                path = customExePath,
                                isExternal = customIsExternal,
                                locationLabel = if (customIsExternal) "Almacenamiento Externo (SD)" else "Almacenamiento Interno"
                            )
                            showFileSelectorDialog = false
                        },
                        modifier = Modifier.fillMaxWidth(),
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF1E4C8A))
                    ) {
                        Text("Agregar y Seleccionar Esta Ruta", fontSize = 11.sp)
                    }
                }
            },
            confirmButton = {
                TextButton(onClick = { showFileSelectorDialog = false }) {
                    Text("Cerrar", color = QuinqueSilver)
                }
            },
            containerColor = LauncherCardBg
        )
    }

    // MODAL 2: SAVE CUSTOM DRIVER PROFILE
    if (showSaveProfileDialog) {
        AlertDialog(
            onDismissRequest = { showSaveProfileDialog = false },
            title = {
                Text("Guardar Perfil de Drivers Actual", color = Color.White, fontWeight = FontWeight.Bold)
            },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    Text(
                        text = "Guarda la combinación actual (${uiState.gameConfig.selectedDriverName}, ${uiState.gameConfig.resolution}, DXVK) para cambiar en 1 toque:",
                        color = Color(0xFF8BA5CC),
                        fontSize = 11.5.sp
                    )

                    OutlinedTextField(
                        value = newProfileName,
                        onValueChange = { newProfileName = it },
                        modifier = Modifier.fillMaxWidth(),
                        label = { Text("Nombre del Perfil") },
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = LauncherNeonBlue,
                            unfocusedBorderColor = LauncherCardBorder,
                            focusedTextColor = Color.White,
                            unfocusedTextColor = Color.White
                        )
                    )

                    OutlinedTextField(
                        value = newProfileDesc,
                        onValueChange = { newProfileDesc = it },
                        modifier = Modifier.fillMaxWidth(),
                        label = { Text("Descripción") },
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = LauncherNeonBlue,
                            unfocusedBorderColor = LauncherCardBorder,
                            focusedTextColor = Color.White,
                            unfocusedTextColor = Color.White
                        )
                    )

                    Row(
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text("Ícono:", color = Color.White, fontSize = 12.sp)
                        listOf("⚡", "💎", "⚖️", "🔋", "🚀", "🎮").forEach { emoji ->
                            Surface(
                                onClick = { newProfileIcon = emoji },
                                shape = CircleShape,
                                color = if (newProfileIcon == emoji) LauncherActivePill else LauncherInactivePill,
                                border = androidx.compose.foundation.BorderStroke(1.dp, if (newProfileIcon == emoji) LauncherNeonBlue else Color.Transparent),
                                modifier = Modifier.size(32.dp)
                            ) {
                                Box(contentAlignment = Alignment.Center) {
                                    Text(text = emoji, fontSize = 14.sp)
                                }
                            }
                        }
                    }
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        viewModel.saveCustomProfile(newProfileName, newProfileDesc, newProfileIcon)
                        showSaveProfileDialog = false
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = LauncherNeonBlue)
                ) {
                    Text("Guardar Perfil", color = Color.Black, fontWeight = FontWeight.Bold)
                }
            },
            dismissButton = {
                TextButton(onClick = { showSaveProfileDialog = false }) {
                    Text("Cancelar", color = QuinqueSilver)
                }
            },
            containerColor = LauncherCardBg
        )
    }

    // MODAL 3: EXPANDED RESOLUTION CHANGER MODAL
    if (showResolutionModal) {
        AlertDialog(
            onDismissRequest = { showResolutionModal = false },
            title = {
                Text("Cambiar Resolución del Emulador", color = Color.White, fontWeight = FontWeight.Bold)
            },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Text(
                        text = "Selecciona una resolución adaptada a la potencia de tu GPU:",
                        color = Color(0xFF8BA5CC),
                        fontSize = 11.5.sp
                    )

                    val resolutionOptions = listOf(
                        Triple("360p (640x360)", "360p", "Máximo ahorro y FPS extremos"),
                        Triple("480p (854x480)", "480p", "Excelente para procesadores de gama media"),
                        Triple("540p (960x540)", "540p", "Recomendado para 60 FPS estables"),
                        Triple("720p (1280x720)", "720p", "Resolución nativa de Tokyo Ghoul en emulador"),
                        Triple("900p (1600x900)", "900p", "Mayor nitidez sin consumo excesivo"),
                        Triple("1080p (1920x1080)", "1080p", "Modo Full HD para Snapdragon 8 Gen 2/3"),
                        Triple("1440p 2K (2560x1440)", "1440p", "Calidad Ultra para tablets y gama alta")
                    )

                    resolutionOptions.forEach { (label, key, note) ->
                        val isSel = uiState.gameConfig.resolution.contains(key)
                        Surface(
                            onClick = {
                                viewModel.setResolution(key)
                                showResolutionModal = false
                            },
                            shape = RoundedCornerShape(10.dp),
                            color = if (isSel) LauncherActivePill else LauncherInactivePill,
                            border = androidx.compose.foundation.BorderStroke(1.dp, if (isSel) LauncherNeonBlue else LauncherCardBorder),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Row(
                                modifier = Modifier.padding(10.dp),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Column {
                                    Text(text = label, color = Color.White, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                                    Text(text = note, color = Color(0xFF8BA5CC), fontSize = 9.sp)
                                }
                                if (isSel) {
                                    Icon(imageVector = Icons.Default.Check, contentDescription = "Activo", tint = StatusGreen)
                                }
                            }
                        }
                    }
                }
            },
            confirmButton = {
                TextButton(onClick = { showResolutionModal = false }) {
                    Text("Cerrar", color = QuinqueSilver)
                }
            },
            containerColor = LauncherCardBg
        )
    }
}

data class PillOption(val key: String, val subtitle: String)

@Composable
fun ConfigPillCard(
    title: String,
    options: List<PillOption>,
    selectedKey: String,
    onSelect: (String) -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(containerColor = LauncherCardBg),
        border = androidx.compose.foundation.BorderStroke(1.dp, LauncherCardBorder)
    ) {
        Column(modifier = Modifier.padding(10.dp)) {
            Text(
                text = title,
                color = Color.White,
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(8.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                options.forEach { opt ->
                    val isSelected = selectedKey.equals(opt.key, ignoreCase = true)
                    Surface(
                        onClick = { onSelect(opt.key) },
                        shape = RoundedCornerShape(12.dp),
                        color = if (isSelected) LauncherActivePill else LauncherInactivePill,
                        border = androidx.compose.foundation.BorderStroke(
                            1.dp,
                            if (isSelected) LauncherNeonBlue else Color.Transparent
                        ),
                        modifier = Modifier
                            .weight(1f)
                            .testTag("pill_${opt.key}")
                    ) {
                        Column(
                            modifier = Modifier.padding(vertical = 6.dp, horizontal = 2.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text(
                                text = opt.key,
                                color = if (isSelected) Color.White else Color(0xFF7FA2CE),
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                maxLines = 1
                            )
                            if (opt.subtitle.isNotEmpty()) {
                                Text(
                                    text = opt.subtitle,
                                    color = if (isSelected) Color(0xFF90CAF9) else Color(0xFF4A688C),
                                    fontSize = 8.5.sp,
                                    maxLines = 1
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun ConfigToggleCard(
    title: String,
    subtitle: String,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(containerColor = LauncherCardBg),
        border = androidx.compose.foundation.BorderStroke(1.dp, LauncherCardBorder)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 10.dp, vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = title,
                    color = Color.White,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = subtitle,
                    color = Color(0xFF8BA5CC),
                    fontSize = 9.sp,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }
            Switch(
                checked = checked,
                onCheckedChange = onCheckedChange,
                colors = SwitchDefaults.colors(
                    checkedThumbColor = Color.White,
                    checkedTrackColor = LauncherNeonBlue,
                    uncheckedTrackColor = LauncherInactivePill
                ),
                modifier = Modifier.scale(0.85f)
            )
        }
    }
}

@Composable
fun ConfigActionCard(
    title: String,
    subtitle: String,
    actionLabel: String,
    onAction: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(containerColor = LauncherCardBg),
        border = androidx.compose.foundation.BorderStroke(1.dp, LauncherCardBorder)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 10.dp, vertical = 10.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = title,
                    color = Color.White,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = subtitle,
                    color = Color(0xFF8BA5CC),
                    fontSize = 9.sp,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }

            Surface(
                onClick = onAction,
                shape = RoundedCornerShape(12.dp),
                color = LauncherActivePill,
                border = androidx.compose.foundation.BorderStroke(1.dp, LauncherNeonBlue),
                modifier = Modifier.testTag("action_${actionLabel.lowercase()}")
            ) {
                Text(
                    text = actionLabel,
                    color = Color.White,
                    fontSize = 10.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(horizontal = 10.dp, vertical = 5.dp)
                )
            }
        }
    }
}

