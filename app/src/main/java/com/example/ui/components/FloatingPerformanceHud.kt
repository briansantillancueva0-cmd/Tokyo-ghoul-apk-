package com.example.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.DragHandle
import androidx.compose.material.icons.filled.ExpandLess
import androidx.compose.material.icons.filled.ExpandMore
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material.icons.filled.Speed
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.CombatState
import com.example.model.HudDisplayStyle
import com.example.ui.LauncherViewModel
import com.example.ui.theme.GhoulDarkBg
import com.example.ui.theme.KaguneAwakeningGlow
import com.example.ui.theme.KaguneCrimson
import com.example.ui.theme.QuinqueAccent
import com.example.ui.theme.QuinqueSilver
import com.example.ui.theme.StatusGreen
import kotlin.math.roundToInt

// Color tokens for HUD matching the user's reference photo
val HudBackground = Color(0xF00B1424)
val HudBorder = Color(0xFF1E3A63)
val GaugeGreen = Color(0xFF69F0AE)
val GaugeOrange = Color(0xFFFF9100)
val GaugeCyan = Color(0xFF00E5FF)
val GaugeTrack = Color(0xFF16253B)

@Composable
fun FloatingPerformanceHud(
    combatState: CombatState,
    resolution: String,
    hudStyle: HudDisplayStyle,
    driverName: String,
    viewModel: LauncherViewModel,
    modifier: Modifier = Modifier
) {
    var offsetX by remember { mutableFloatStateOf(combatState.hudOffsetX) }
    var offsetY by remember { mutableFloatStateOf(combatState.hudOffsetY) }

    Box(
        modifier = modifier
            .offset { IntOffset(offsetX.roundToInt(), offsetY.roundToInt()) }
            .pointerInput(Unit) {
                detectDragGestures { change, dragAmount ->
                    change.consume()
                    offsetX = (offsetX + dragAmount.x).coerceAtLeast(0f)
                    offsetY = (offsetY + dragAmount.y).coerceAtLeast(0f)
                    viewModel.updateFloatingHudPosition(offsetX, offsetY)
                }
            }
            .shadow(12.dp, RoundedCornerShape(16.dp))
            .testTag("floating_perf_overlay")
    ) {
        if (combatState.isHudMinimized) {
            // MINIMIZED PILL (Compact Badge)
            Surface(
                onClick = { viewModel.toggleFloatingHudMinimized(false) },
                shape = RoundedCornerShape(20.dp),
                color = HudBackground,
                border = androidx.compose.foundation.BorderStroke(1.2.dp, HudBorder),
                modifier = Modifier.testTag("floating_hud_minimized_pill")
            ) {
                Row(
                    modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.DragHandle,
                        contentDescription = "Arrastrar",
                        tint = QuinqueSilver.copy(alpha = 0.7f),
                        modifier = Modifier.size(14.dp)
                    )
                    Box(
                        modifier = Modifier
                            .size(8.dp)
                            .clip(CircleShape)
                            .background(if (combatState.currentFps >= 55) GaugeGreen else GaugeOrange)
                    )
                    Text(
                        text = "${combatState.currentFps} FPS",
                        color = GaugeGreen,
                        fontSize = 11.5.sp,
                        fontWeight = FontWeight.Black
                    )
                    Text(
                        text = "• GPU ${combatState.gpuUsagePercent}%",
                        color = GaugeCyan,
                        fontSize = 10.5.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = "• ${combatState.gpuTempCelsius}°C",
                        color = QuinqueSilver,
                        fontSize = 10.sp
                    )
                    Icon(
                        imageVector = Icons.Default.ExpandMore,
                        contentDescription = "Expandir",
                        tint = Color.White,
                        modifier = Modifier.size(16.dp)
                    )
                }
            }
        } else {
            // EXPANDED FLOATING CARD MATCHING USER PHOTO
            Surface(
                shape = RoundedCornerShape(16.dp),
                color = HudBackground,
                border = androidx.compose.foundation.BorderStroke(1.5.dp, HudBorder),
                modifier = Modifier
                    .width(280.dp)
                    .testTag("floating_hud_expanded_card")
            ) {
                Column(
                    modifier = Modifier.padding(horizontal = 12.dp, vertical = 10.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    // TOP BAR: FPS • Resolution + Minimize Button + Close Button
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.DragHandle,
                                contentDescription = "Arrastrar Overlay",
                                tint = QuinqueSilver.copy(alpha = 0.7f),
                                modifier = Modifier.size(14.dp)
                            )
                            Text(
                                text = "${combatState.currentFps} FPS • $resolution",
                                color = Color.White,
                                fontSize = 13.sp,
                                fontWeight = FontWeight.Black,
                                letterSpacing = 0.5.sp,
                                modifier = Modifier.testTag("floating_hud_fps_text")
                            )
                        }

                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(2.dp)
                        ) {
                            // Minimize button [-]
                            Box(
                                modifier = Modifier
                                    .size(22.dp)
                                    .clip(CircleShape)
                                    .background(Color(0xFF1A2A42))
                                    .clickable { viewModel.toggleFloatingHudMinimized(true) }
                                    .testTag("btn_minimize_floating_hud"),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Remove,
                                    contentDescription = "Minimizar",
                                    tint = Color.White,
                                    modifier = Modifier.size(14.dp)
                                )
                            }

                            Spacer(modifier = Modifier.width(4.dp))

                            // Close button [X]
                            Box(
                                modifier = Modifier
                                    .size(22.dp)
                                    .clip(CircleShape)
                                    .background(Color(0xFF2E1720))
                                    .clickable { viewModel.toggleFpsPanel(false) }
                                    .testTag("btn_close_floating_hud"),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Close,
                                    contentDescription = "Cerrar",
                                    tint = KaguneCrimson,
                                    modifier = Modifier.size(13.dp)
                                )
                            }
                        }
                    }

                    // BODY CONTENT DEPENDING ON HUD STYLE
                    when (hudStyle) {
                        HudDisplayStyle.RADIAL_GAUGES -> {
                            // THREE CIRCULAR RADIAL GAUGES MATCHING USER PHOTO
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 4.dp),
                                horizontalArrangement = Arrangement.SpaceEvenly,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                // GAUGE 1: CPU % + CPU MHz
                                RadialTelemetryGauge(
                                    percent = combatState.cpuLoadPercent,
                                    primaryValue = "${combatState.cpuLoadPercent}%",
                                    subtitle = "CPU ${combatState.cpuFreqMhz} MHz",
                                    arcColor = GaugeGreen,
                                    testTag = "gauge_cpu"
                                )

                                // GAUGE 2: GPU % + GPU MHz
                                RadialTelemetryGauge(
                                    percent = combatState.gpuUsagePercent,
                                    primaryValue = "${combatState.gpuUsagePercent}%",
                                    subtitle = "GPU ${combatState.gpuClockMhz} MHz",
                                    arcColor = GaugeCyan,
                                    testTag = "gauge_gpu"
                                )

                                // GAUGE 3: RAM % + RAM MB
                                RadialTelemetryGauge(
                                    percent = combatState.ramPercent,
                                    primaryValue = "${combatState.ramPercent}%",
                                    subtitle = "RAM ${combatState.ramUsedMb} MB",
                                    arcColor = GaugeOrange,
                                    testTag = "gauge_ram"
                                )
                            }
                        }

                        HudDisplayStyle.DETAILED_STATS -> {
                            // COMPREHENSIVE HARDWARE MONITOR
                            Column(
                                modifier = Modifier.fillMaxWidth(),
                                verticalArrangement = Arrangement.spacedBy(4.dp)
                            ) {
                                TelemetryStatRow("GPU Adreno 650", "${combatState.gpuUsagePercent}% (${combatState.gpuClockMhz} MHz)", GaugeCyan)
                                TelemetryStatRow("CPU Kryo 585 Octa", "${combatState.cpuLoadPercent}% (${combatState.cpuFreqMhz} MHz)", GaugeGreen)
                                TelemetryStatRow("Memoria RAM", "${combatState.ramUsedMb} MB / ${combatState.ramTotalMb} MB (${combatState.ramPercent}%)", GaugeOrange)
                                TelemetryStatRow("Temperatura Térmica", "${combatState.gpuTempCelsius}°C (Snapdragon 865)", if (combatState.gpuTempCelsius > 48) KaguneCrimson else GaugeGreen)
                                TelemetryStatRow("Latencia de Cuadro", "${String.format("%.1f", combatState.frameTimeMs)} ms (DXVK Async)", QuinqueAccent)
                                TelemetryStatRow("Driver Vulkan", driverName, QuinqueSilver)
                            }
                        }

                        HudDisplayStyle.FPS_GRAPH -> {
                            // REAL-TIME FPS SPARKLINE GRAPH
                            Column(modifier = Modifier.fillMaxWidth()) {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ) {
                                    Text("Estabilidad 60 FPS (UE4)", color = QuinqueSilver, fontSize = 9.sp)
                                    Text(
                                        text = "Mín: ${combatState.fpsHistory.minOrNull() ?: 58} | Máx: ${combatState.fpsHistory.maxOrNull() ?: 60}",
                                        color = GaugeGreen,
                                        fontSize = 9.sp,
                                        fontWeight = FontWeight.Bold
                                    )
                                }
                                Spacer(modifier = Modifier.height(4.dp))
                                LiveFpsGraphCanvas(
                                    fpsPoints = combatState.fpsHistory,
                                    targetFps = 60,
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .height(48.dp)
                                        .clip(RoundedCornerShape(8.dp))
                                        .background(Color(0xFF09121F))
                                )
                            }
                        }

                        HudDisplayStyle.COMPACT_PILL -> {
                            // Handled in minimized branch above
                        }
                    }

                    // BOTTOM STYLE SWITCHER (MEDIDORES • DETALLES • GRÁFICO)
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 2.dp),
                        horizontalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        listOf(
                            HudDisplayStyle.RADIAL_GAUGES,
                            HudDisplayStyle.DETAILED_STATS,
                            HudDisplayStyle.FPS_GRAPH
                        ).forEach { style ->
                            val isSelected = hudStyle == style
                            Surface(
                                onClick = { viewModel.setHudDisplayStyle(style) },
                                shape = RoundedCornerShape(6.dp),
                                color = if (isSelected) Color(0xFF1E3A63) else Color(0xFF0D1726),
                                border = androidx.compose.foundation.BorderStroke(
                                    1.dp,
                                    if (isSelected) GaugeCyan else Color.Transparent
                                ),
                                modifier = Modifier.weight(1f)
                            ) {
                                Text(
                                    text = style.shortName,
                                    color = if (isSelected) Color.White else QuinqueSilver.copy(alpha = 0.6f),
                                    fontSize = 9.sp,
                                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                                    textAlign = TextAlign.Center,
                                    modifier = Modifier.padding(vertical = 3.dp)
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
fun RadialTelemetryGauge(
    percent: Int,
    primaryValue: String,
    subtitle: String,
    arcColor: Color,
    testTag: String,
    modifier: Modifier = Modifier
) {
    val animatedSweep by animateFloatAsState(
        targetValue = (percent.coerceIn(0, 100) / 100f) * 280f,
        label = "gauge_sweep"
    )

    Column(
        modifier = modifier.testTag(testTag),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Box(
            modifier = Modifier.size(54.dp),
            contentAlignment = Alignment.Center
        ) {
            Canvas(modifier = Modifier.size(50.dp)) {
                val strokeWidth = 5.dp.toPx()
                val diameter = size.minDimension - strokeWidth
                val topLeft = Offset(strokeWidth / 2, strokeWidth / 2)
                val arcSize = Size(diameter, diameter)

                // Track ring
                drawArc(
                    color = GaugeTrack,
                    startAngle = 130f,
                    sweepAngle = 280f,
                    useCenter = false,
                    topLeft = topLeft,
                    size = arcSize,
                    style = Stroke(width = strokeWidth, cap = StrokeCap.Round)
                )

                // Colored progress ring
                drawArc(
                    color = arcColor,
                    startAngle = 130f,
                    sweepAngle = animatedSweep,
                    useCenter = false,
                    topLeft = topLeft,
                    size = arcSize,
                    style = Stroke(width = strokeWidth, cap = StrokeCap.Round)
                )
            }

            Text(
                text = primaryValue,
                color = Color.White,
                fontSize = 11.sp,
                fontWeight = FontWeight.Black
            )
        }

        Spacer(modifier = Modifier.height(2.dp))

        Text(
            text = subtitle,
            color = QuinqueSilver.copy(alpha = 0.85f),
            fontSize = 7.5.sp,
            fontWeight = FontWeight.SemiBold,
            maxLines = 1
        )
    }
}

@Composable
fun TelemetryStatRow(label: String, value: String, accentColor: Color) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(text = label, color = QuinqueSilver, fontSize = 9.5.sp)
        Text(
            text = value,
            color = accentColor,
            fontSize = 9.5.sp,
            fontWeight = FontWeight.Bold
        )
    }
}

@Composable
fun LiveFpsGraphCanvas(
    fpsPoints: List<Int>,
    targetFps: Int,
    modifier: Modifier = Modifier
) {
    Canvas(modifier = modifier) {
        if (fpsPoints.isEmpty()) return@Canvas

        val w = size.width
        val h = size.height
        val minFps = 45f
        val maxFps = 65f

        // Dotted target 60 FPS guide
        val targetY = h - ((targetFps - minFps) / (maxFps - minFps) * h)
        drawLine(
            color = Color(0xFF2E4868),
            start = Offset(0f, targetY),
            end = Offset(w, targetY),
            strokeWidth = 1.dp.toPx()
        )

        // Plot FPS line
        val stepX = w / (fpsPoints.size - 1).coerceAtLeast(1)
        val path = Path()

        fpsPoints.forEachIndexed { index, fps ->
            val normalizedY = h - ((fps.toFloat() - minFps) / (maxFps - minFps) * h).coerceIn(0f, h)
            val posX = index * stepX
            if (index == 0) {
                path.moveTo(posX, normalizedY)
            } else {
                path.lineTo(posX, normalizedY)
            }
        }

        drawPath(
            path = path,
            color = GaugeGreen,
            style = Stroke(width = 2.dp.toPx(), cap = StrokeCap.Round)
        )
    }
}
