package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Gamepad
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Smartphone
import androidx.compose.material.icons.filled.SportsEsports
import androidx.compose.material.icons.filled.Tune
import androidx.compose.material.icons.filled.Vibration
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.LauncherUiState
import com.example.model.ControllerPreset
import com.example.model.HapticIntensity
import com.example.ui.LauncherViewModel
import com.example.ui.components.VirtualControllerOverlay
import com.example.ui.theme.GhoulBorder
import com.example.ui.theme.GhoulDarkBg
import com.example.ui.theme.GhoulSurfaceCard
import com.example.ui.theme.GhoulSurfaceElevated
import com.example.ui.theme.GhoulTextMuted
import com.example.ui.theme.GhoulTextSecondary
import com.example.ui.theme.KaguneAwakeningGlow
import com.example.ui.theme.KaguneCrimson
import com.example.ui.theme.KaguneDarkRed
import com.example.ui.theme.QuinqueAccent
import com.example.ui.theme.QuinqueSilver
import com.example.ui.theme.StatusGreen

@Composable
fun ControlAdapterScreen(
    uiState: LauncherUiState,
    viewModel: LauncherViewModel,
    onLaunchGameTest: () -> Unit,
    modifier: Modifier = Modifier
) {
    val scrollState = rememberScrollState()

    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(scrollState)
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // SCREEN TITLE
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text(
                    text = "ADAPTADOR DE CONTROLES",
                    color = Color.White,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Black,
                    letterSpacing = 1.sp
                )
                Text(
                    text = "Mapeo táctil adaptado para Tokyo Ghoul:re",
                    color = KaguneCrimson,
                    fontSize = 12.sp
                )
            }
            Button(
                onClick = onLaunchGameTest,
                colors = ButtonDefaults.buttonColors(containerColor = KaguneCrimson),
                shape = RoundedCornerShape(10.dp),
                modifier = Modifier.testTag("btn_test_in_game")
            ) {
                Text("Probar en Juego", fontSize = 11.sp, fontWeight = FontWeight.Bold)
            }
        }

        // PRESET CAROUSEL
        Text(
            text = "Distribuciones Predefinidas",
            color = Color.White,
            fontSize = 14.sp,
            fontWeight = FontWeight.Bold
        )

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            ControllerPreset.values().forEach { preset ->
                val isSelected = uiState.controls.activePreset == preset
                Surface(
                    onClick = { viewModel.setControllerPreset(preset) },
                    shape = RoundedCornerShape(10.dp),
                    color = if (isSelected) KaguneDarkRed.copy(alpha = 0.5f) else GhoulSurfaceCard,
                    border = androidx.compose.foundation.BorderStroke(
                        1.dp,
                        if (isSelected) KaguneCrimson else GhoulBorder
                    ),
                    modifier = Modifier
                        .weight(1f)
                        .testTag("preset_${preset.name}")
                ) {
                    Column(
                        modifier = Modifier.padding(8.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = preset.label,
                            color = if (isSelected) Color.White else QuinqueSilver,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            maxLines = 1
                        )
                    }
                }
            }
        }

        // TOUCH HUD CALIBRATION CONTROLS
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(14.dp),
            colors = CardDefaults.cardColors(containerColor = GhoulSurfaceCard),
            border = androidx.compose.foundation.BorderStroke(1.dp, GhoulBorder)
        ) {
            Column(
                modifier = Modifier.padding(14.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Text(
                    text = "Ajustes de Tamaño y Visibilidad Táctil",
                    color = Color.White,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold
                )

                // Scale Slider
                Column {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text("Escala de Botones", color = QuinqueSilver, fontSize = 12.sp)
                        Text(
                            "${(uiState.controls.globalScale * 100).toInt()}%",
                            color = QuinqueAccent,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                    Slider(
                        value = uiState.controls.globalScale,
                        onValueChange = { viewModel.updateGlobalScale(it) },
                        valueRange = 0.7f..1.35f,
                        colors = SliderDefaults.colors(
                            thumbColor = KaguneCrimson,
                            activeTrackColor = KaguneCrimson,
                            inactiveTrackColor = GhoulDarkBg
                        )
                    )
                }

                // Opacity Slider
                Column {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text("Opacidad / Transparencia Táctil", color = QuinqueSilver, fontSize = 12.sp)
                        Text(
                            "${(uiState.controls.opacity * 100).toInt()}%",
                            color = QuinqueAccent,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                    Slider(
                        value = uiState.controls.opacity,
                        onValueChange = { viewModel.updateOpacity(it) },
                        valueRange = 0.25f..1.0f,
                        colors = SliderDefaults.colors(
                            thumbColor = QuinqueAccent,
                            activeTrackColor = QuinqueAccent,
                            inactiveTrackColor = GhoulDarkBg
                        )
                    )
                }

                // Haptic Intensity Selector
                Column {
                    Text("Respuesta Háptica (Vibración de Impacto)", color = QuinqueSilver, fontSize = 12.sp)
                    Spacer(modifier = Modifier.height(6.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        HapticIntensity.values().forEach { intensity ->
                            val isSel = uiState.controls.hapticIntensity == intensity
                            Surface(
                                onClick = { viewModel.updateHapticIntensity(intensity) },
                                shape = RoundedCornerShape(8.dp),
                                color = if (isSel) KaguneCrimson else GhoulDarkBg,
                                modifier = Modifier.weight(1f)
                            ) {
                                Box(
                                    modifier = Modifier.padding(vertical = 8.dp),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(
                                        text = intensity.label.take(8),
                                        color = Color.White,
                                        fontSize = 10.sp,
                                        fontWeight = FontWeight.Bold
                                    )
                                }
                            }
                        }
                    }
                }

                // THEME SELECTOR: Anime Combat (Photo) vs Classic Console
                Column {
                    Text("Tema Visual e Iconografía de Botones", color = QuinqueSilver, fontSize = 12.sp)
                    Spacer(modifier = Modifier.height(6.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        listOf(
                            com.example.model.ControlTheme.ANIME_ACTION_COMBAT,
                            com.example.model.ControlTheme.CLASSIC_CONSOLE
                        ).forEach { theme ->
                            val isSel = uiState.controls.controlTheme == theme
                            Surface(
                                onClick = { viewModel.setControlTheme(theme) },
                                shape = RoundedCornerShape(8.dp),
                                color = if (isSel) KaguneDarkRed else GhoulDarkBg,
                                border = androidx.compose.foundation.BorderStroke(
                                    1.dp,
                                    if (isSel) KaguneCrimson else Color.Transparent
                                ),
                                modifier = Modifier.weight(1f)
                            ) {
                                Text(
                                    text = theme.label.take(24),
                                    color = if (isSel) Color.White else QuinqueSilver,
                                    fontSize = 10.sp,
                                    fontWeight = FontWeight.Bold,
                                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 8.dp)
                                )
                            }
                        }
                    }
                }

                // Toggles for Hexagon Skills & Utility Dock
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text("Botones Hexagonales (奥 • 術 • ミト)", color = Color.White, fontSize = 13.sp, fontWeight = FontWeight.Bold)
                        Text("Técnicas ninja / Ougi despertar Kakuja inspiradas en la foto", color = GhoulTextMuted, fontSize = 11.sp)
                    }
                    Switch(
                        checked = uiState.controls.showHexSkills,
                        onCheckedChange = { viewModel.toggleHexSkills(it) },
                        colors = SwitchDefaults.colors(
                            checkedThumbColor = Color.White,
                            checkedTrackColor = QuinqueAccent
                        )
                    )
                }

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text("Barra Lateral de Utilidades (⚙️ ☰ 👁️ ⏸️ ↔️)", color = Color.White, fontSize = 13.sp, fontWeight = FontWeight.Bold)
                        Text("Acceso rápido a pausa, ocultar botones y ajustes", color = GhoulTextMuted, fontSize = 11.sp)
                    }
                    Switch(
                        checked = uiState.controls.showUtilityDock,
                        onCheckedChange = { viewModel.toggleUtilityDock(it) },
                        colors = SwitchDefaults.colors(
                            checkedThumbColor = Color.White,
                            checkedTrackColor = QuinqueAccent
                        )
                    )
                }

                // Macro Kakuja
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text("Macro de Despertar Kakuja (RB+RT)", color = Color.White, fontSize = 13.sp, fontWeight = FontWeight.Bold)
                        Text("Botón directo en la barra superior para activar modo ciempiés", color = GhoulTextMuted, fontSize = 11.sp)
                    }
                    Switch(
                        checked = uiState.controls.enableKaguneComboMacro,
                        onCheckedChange = { viewModel.toggleMacro(it) },
                        colors = SwitchDefaults.colors(
                            checkedThumbColor = Color.White,
                            checkedTrackColor = KaguneCrimson
                        )
                    )
                }

                // Floating HUD Overlay Toggle & Style
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text("Overlay Flotante de Rendimiento", color = Color.White, fontSize = 13.sp, fontWeight = FontWeight.Bold)
                        Text("Panel móvil con FPS, medidores de GPU/CPU y RAM en tiempo real", color = GhoulTextMuted, fontSize = 11.sp)
                    }
                    Switch(
                        checked = uiState.controls.showPerformanceOverlay,
                        onCheckedChange = {
                            viewModel.togglePerfHud(it)
                            viewModel.toggleFpsPanel(it)
                        },
                        colors = SwitchDefaults.colors(
                            checkedThumbColor = Color.White,
                            checkedTrackColor = StatusGreen
                        )
                    )
                }

                if (uiState.controls.showPerformanceOverlay) {
                    Column {
                        Text("Estilo de Visualización del Overlay", color = QuinqueSilver, fontSize = 11.sp)
                        Spacer(modifier = Modifier.height(4.dp))
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            listOf(
                                com.example.model.HudDisplayStyle.RADIAL_GAUGES,
                                com.example.model.HudDisplayStyle.DETAILED_STATS,
                                com.example.model.HudDisplayStyle.FPS_GRAPH
                            ).forEach { style ->
                                val isSel = uiState.controls.hudStyle == style
                                Surface(
                                    onClick = { viewModel.setHudDisplayStyle(style) },
                                    shape = RoundedCornerShape(8.dp),
                                    color = if (isSel) Color(0xFF1E3A63) else GhoulDarkBg,
                                    border = androidx.compose.foundation.BorderStroke(
                                        1.dp,
                                        if (isSel) Color(0xFF00E5FF) else Color.Transparent
                                    ),
                                    modifier = Modifier.weight(1f)
                                ) {
                                    Text(
                                        text = style.shortName,
                                        color = if (isSel) Color.White else QuinqueSilver,
                                        fontSize = 10.sp,
                                        fontWeight = FontWeight.Bold,
                                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 6.dp)
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }

        // INTERACTIVE LIVE CONTROLLER TEST CANVASES
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(14.dp),
            colors = CardDefaults.cardColors(containerColor = GhoulSurfaceElevated),
            border = androidx.compose.foundation.BorderStroke(1.dp, KaguneCrimson.copy(alpha = 0.5f))
        ) {
            Column(
                modifier = Modifier.padding(14.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Área de Prueba Interactiva de Botones",
                        color = Color.White,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = "Toca abajo para probar",
                        color = KaguneCrimson,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold
                    )
                }

                // Interactive Controller Simulation Box
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(230.dp)
                        .clip(RoundedCornerShape(12.dp))
                        .background(GhoulDarkBg)
                        .border(1.dp, GhoulBorder, RoundedCornerShape(12.dp))
                ) {
                    VirtualControllerOverlay(
                        customization = uiState.controls.copy(globalScale = 0.9f),
                        onButtonPressed = { btn -> viewModel.handleVirtualButtonPress(btn) },
                        modifier = Modifier.fillMaxSize()
                    )
                }

                // Last Input Indicator
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(8.dp))
                        .background(GhoulDarkBg)
                        .padding(horizontal = 12.dp, vertical = 8.dp)
                ) {
                    Text(
                        text = uiState.lastInputDetected,
                        color = QuinqueAccent,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }

        // PHYSICAL GAMEPAD SUPPORT SECTION
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(14.dp),
            colors = CardDefaults.cardColors(containerColor = GhoulSurfaceCard),
            border = androidx.compose.foundation.BorderStroke(1.dp, GhoulBorder)
        ) {
            Column(modifier = Modifier.padding(14.dp)) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.SportsEsports,
                        contentDescription = "Mando Físico",
                        tint = QuinqueAccent,
                        modifier = Modifier.size(20.dp)
                    )
                    Text(
                        text = "Soporte de Mando Físico (Bluetooth / USB)",
                        color = Color.White,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
                Spacer(modifier = Modifier.height(6.dp))
                Text(
                    text = "Compatible con mandos Xbox Series X/S, PlayStation DualSense/DualShock 4, Razer Kishi, GameSir G8 y Backbone One sin necesidad de configuración adicional mediante el controlador XInput de Wine.",
                    color = GhoulTextSecondary,
                    fontSize = 11.5.sp
                )
            }
        }
    }
}
