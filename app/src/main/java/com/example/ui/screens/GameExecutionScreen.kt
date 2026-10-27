package com.example.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Tune
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.data.LauncherUiState
import com.example.ui.LauncherViewModel
import com.example.ui.components.PerformanceHud
import com.example.ui.components.VirtualControllerOverlay
import com.example.ui.theme.GhoulDarkBg
import com.example.ui.theme.GhoulSurfaceElevated
import com.example.ui.theme.KaguneAwakeningGlow
import com.example.ui.theme.KaguneCrimson
import com.example.ui.theme.KaguneDarkRed
import com.example.ui.theme.QuinqueAccent
import com.example.ui.theme.QuinqueSilver
import com.example.ui.theme.StatusGreen

import com.example.ui.components.FloatingPerformanceHud

@Composable
fun GameExecutionScreen(
    uiState: LauncherUiState,
    viewModel: LauncherViewModel,
    modifier: Modifier = Modifier
) {
    var isPaused by remember { mutableStateOf(false) }

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(Color.Black)
            .testTag("game_execution_screen")
    ) {
        // BACKGROUND 3D KEY ART (TOKYO GHOUL BATTLEFIELD)
        Image(
            painter = painterResource(id = R.drawable.tg_banner_hero),
            contentDescription = "Emulated Game Canvas",
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop
        )

        // Dark Vignette Overlay for focus & readability
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    Brush.radialGradient(
                        colors = listOf(
                            Color.Transparent,
                            Color.Black.copy(alpha = 0.55f),
                            Color.Black.copy(alpha = 0.85f)
                        )
                    )
                )
        )

        // DYNAMIC COMBAT VISUAL FEEDBACK (Kagune Tentacle Slashes on press)
        Canvas(modifier = Modifier.fillMaxSize()) {
            if (uiState.combat.comboCounter > 0) {
                val center = Offset(size.width * 0.5f, size.height * 0.45f)
                val slashColor = if (uiState.combat.isAwakened) KaguneAwakeningGlow else KaguneCrimson

                // Draw fiery Kagune arc
                drawCircle(
                    brush = Brush.radialGradient(
                        listOf(slashColor.copy(alpha = 0.35f), Color.Transparent),
                        center = center,
                        radius = 280f
                    ),
                    center = center,
                    radius = 280f
                )
            }
        }

        // TOP COMBAT HUD (HP, RC Cells & Combo Counters)
        val activeDriver = uiState.drivers.firstOrNull { it.isSelected }
        PerformanceHud(
            combatState = uiState.combat,
            driverName = activeDriver?.name ?: "Turnip v24.3.0",
            dxvkVersion = uiState.gameConfig.selectedDxvkId.take(8),
            modifier = Modifier.align(Alignment.TopCenter)
        )

        // ADAPTED VIRTUAL TOUCH CONTROLLER OVERLAY (Matching User Reference Photo)
        VirtualControllerOverlay(
            customization = uiState.controls,
            onButtonPressed = { btn -> viewModel.handleVirtualButtonPress(btn) },
            onPauseClick = { isPaused = true },
            onSettingsClick = { isPaused = true },
            modifier = Modifier.fillMaxSize()
        )

        // FLOATING REAL-TIME FPS & GPU USAGE OVERLAY (Draggable Anywhere Across Screen)
        if (uiState.controls.showPerformanceOverlay || uiState.gameConfig.showFpsPanel) {
            FloatingPerformanceHud(
                combatState = uiState.combat,
                resolution = uiState.gameConfig.resolution,
                hudStyle = uiState.controls.hudStyle,
                driverName = activeDriver?.name ?: "Turnip v24.3.0 (Adreno 650)",
                viewModel = viewModel
            )
        }

        // PAUSE & IN-GAME SETTINGS MODAL
        if (isPaused) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.Black.copy(alpha = 0.75f))
                    .testTag("in_game_pause_modal"),
                contentAlignment = Alignment.Center
            ) {
                Card(
                    modifier = Modifier
                        .fillMaxWidth(0.85f)
                        .clip(RoundedCornerShape(18.dp)),
                    colors = CardDefaults.cardColors(containerColor = GhoulSurfaceElevated),
                    border = androidx.compose.foundation.BorderStroke(1.5.dp, KaguneCrimson)
                ) {
                    Column(
                        modifier = Modifier.padding(18.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "PAUSA • AJUSTES RÁPIDOS",
                                color = Color.White,
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Black
                            )
                            IconButton(onClick = { isPaused = false }) {
                                Icon(
                                    imageVector = Icons.Default.Close,
                                    contentDescription = "Cerrar",
                                    tint = Color.White
                                )
                            }
                        }

                        // Opacity in game
                        Column {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text("Opacidad Táctil", color = QuinqueSilver, fontSize = 12.sp)
                                Text("${(uiState.controls.opacity * 100).toInt()}%", color = QuinqueAccent, fontSize = 12.sp)
                            }
                            Slider(
                                value = uiState.controls.opacity,
                                onValueChange = { viewModel.updateOpacity(it) },
                                valueRange = 0.2f..1f,
                                colors = SliderDefaults.colors(
                                    thumbColor = KaguneCrimson,
                                    activeTrackColor = KaguneCrimson
                                )
                            )
                        }

                        // Driver Quick Info
                        Text(
                            text = "Driver en ejecución: ${uiState.gameConfig.selectedDriverName}",
                            color = QuinqueAccent,
                            fontSize = 11.sp
                        )

                        // Action Buttons
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            Button(
                                onClick = {
                                    viewModel.resetCombatStats()
                                    isPaused = false
                                },
                                colors = ButtonDefaults.buttonColors(containerColor = GhoulDarkBg),
                                modifier = Modifier.weight(1f)
                            ) {
                                Text("Reiniciar Combo", fontSize = 11.sp, color = QuinqueSilver)
                            }

                            Button(
                                onClick = {
                                    isPaused = false
                                    viewModel.exitGame()
                                },
                                colors = ButtonDefaults.buttonColors(containerColor = KaguneCrimson),
                                modifier = Modifier.weight(1f)
                            ) {
                                Text("Salir al Lanzador", fontSize = 11.sp, fontWeight = FontWeight.Bold)
                            }
                        }
                    }
                }
            }
        }
    }
}
