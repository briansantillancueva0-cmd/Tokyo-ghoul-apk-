package com.example.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ElectricBolt
import androidx.compose.material.icons.filled.Speed
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.CombatState
import com.example.ui.theme.GhoulDarkBg
import com.example.ui.theme.KaguneAwakeningGlow
import com.example.ui.theme.KaguneCrimson
import com.example.ui.theme.KaguneDarkRed
import com.example.ui.theme.QuinqueAccent
import com.example.ui.theme.QuinqueSilver
import com.example.ui.theme.StatusGreen

@Composable
fun PerformanceHud(
    combatState: CombatState,
    driverName: String,
    dxvkVersion: String,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 14.dp, vertical = 6.dp)
            .testTag("performance_hud_container")
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.Top
        ) {
            // LEFT: Health & RC Cells Kagune Gauge
            Column(
                modifier = Modifier
                    .clip(RoundedCornerShape(10.dp))
                    .background(GhoulDarkBg.copy(alpha = 0.85f))
                    .border(1.dp, KaguneDarkRed.copy(alpha = 0.6f), RoundedCornerShape(10.dp))
                    .padding(horizontal = 10.dp, vertical = 6.dp)
                    .testTag("ghoul_health_hud"),
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .size(10.dp)
                            .clip(CircleShape)
                            .background(if (combatState.isAwakened) KaguneAwakeningGlow else KaguneCrimson)
                    )
                    Text(
                        text = combatState.characterName.uppercase(),
                        color = Color.White,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Black
                    )
                    Text(
                        text = "• ${combatState.characterAlias}",
                        color = QuinqueSilver.copy(alpha = 0.7f),
                        fontSize = 10.sp
                    )
                }

                // HP Bar
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    Text(text = "HP", color = StatusGreen, fontSize = 9.sp, fontWeight = FontWeight.Bold)
                    LinearProgressIndicator(
                        progress = { combatState.healthPercent },
                        modifier = Modifier
                            .size(width = 110.dp, height = 6.dp)
                            .clip(RoundedCornerShape(3.dp)),
                        color = StatusGreen,
                        trackColor = Color.DarkGray
                    )
                }

                // RC Cells / Kagune Awakening Bar
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    Text(
                        text = "RC",
                        color = if (combatState.isAwakened) KaguneAwakeningGlow else KaguneCrimson,
                        fontSize = 9.sp,
                        fontWeight = FontWeight.Bold
                    )
                    LinearProgressIndicator(
                        progress = { combatState.rcCellGauge },
                        modifier = Modifier
                            .size(width = 110.dp, height = 6.dp)
                            .clip(RoundedCornerShape(3.dp)),
                        color = if (combatState.isAwakened) KaguneAwakeningGlow else KaguneCrimson,
                        trackColor = Color.DarkGray
                    )
                    if (combatState.isAwakened) {
                        Text(
                            text = "KAKUJA!",
                            color = KaguneAwakeningGlow,
                            fontSize = 8.sp,
                            fontWeight = FontWeight.Black
                        )
                    }
                }
            }

            // RIGHT: Turnip & FPS Telemetry Monitor
            Column(
                modifier = Modifier
                    .clip(RoundedCornerShape(10.dp))
                    .background(GhoulDarkBg.copy(alpha = 0.88f))
                    .border(1.dp, QuinqueAccent.copy(alpha = 0.4f), RoundedCornerShape(10.dp))
                    .padding(horizontal = 8.dp, vertical = 4.dp)
                    .testTag("emulator_telemetry_hud"),
                horizontalAlignment = Alignment.End,
                verticalArrangement = Arrangement.spacedBy(2.dp)
            ) {
                // FPS and Resolution
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Speed,
                        contentDescription = "FPS",
                        tint = StatusGreen,
                        modifier = Modifier.size(12.dp)
                    )
                    Text(
                        text = "${combatState.currentFps} FPS",
                        color = StatusGreen,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.ExtraBold
                    )
                    Text(
                        text = "• 720p UE4",
                        color = QuinqueSilver.copy(alpha = 0.8f),
                        fontSize = 9.sp
                    )
                }

                // Active Driver
                Text(
                    text = driverName,
                    color = QuinqueAccent,
                    fontSize = 9.sp,
                    fontWeight = FontWeight.SemiBold
                )

                // DXVK & Hardware Stats
                Text(
                    text = "$dxvkVersion | ${combatState.gpuTempCelsius}°C | CPU ${combatState.cpuLoadPercent}%",
                    color = QuinqueSilver.copy(alpha = 0.75f),
                    fontSize = 8.sp
                )
            }
        }

        // CENTER: Live Combo Counter and Action Feed
        if (combatState.comboCounter > 0) {
            Column(
                modifier = Modifier
                    .align(Alignment.Center)
                    .padding(top = 40.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "${combatState.comboCounter} HITS",
                    color = if (combatState.comboCounter > 5) KaguneAwakeningGlow else QuinqueAccent,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Black
                )
                Text(
                    text = "${combatState.damageDealtTotal} DMG",
                    color = Color.White,
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}
