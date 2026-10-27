package com.example.ui.components

import android.content.Context
import android.os.Build
import android.os.VibrationEffect
import android.os.Vibrator
import android.os.VibratorManager
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowLeft
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.AspectRatio
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.FastForward
import androidx.compose.material.icons.filled.FlashOn
import androidx.compose.material.icons.filled.Gamepad
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.NearMe
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.draw.scale
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Fill
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.ControlCustomization
import com.example.model.ControlTheme
import com.example.model.HapticIntensity
import com.example.ui.theme.GhoulDarkBg
import com.example.ui.theme.KaguneAwakeningGlow
import com.example.ui.theme.KaguneCrimson
import com.example.ui.theme.KaguneDarkRed
import com.example.ui.theme.QuinqueAccent
import com.example.ui.theme.QuinqueSilver
import kotlin.math.roundToInt

// Custom metallic silver-white palette matching the user's reference photo
val MetallicButtonBg = Color(0xF2EAEFF7)
val MetallicButtonBorder = Color(0xFFC0D0E6)
val MetallicIconColor = Color(0xFF1E2F4D)
val HexagonSkillBg = Color(0xF5F0F4FA)

@Composable
fun VirtualControllerOverlay(
    customization: ControlCustomization,
    onButtonPressed: (String) -> Unit,
    onPauseClick: () -> Unit = {},
    onSettingsClick: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    var areControlsLocallyVisible by remember { mutableStateOf(customization.areControlsVisible) }

    fun performHaptic() {
        val duration = customization.hapticIntensity.durationMs
        if (duration <= 0) return
        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                val vibratorManager = context.getSystemService(Context.VIBRATOR_MANAGER_SERVICE) as? VibratorManager
                vibratorManager?.defaultVibrator?.vibrate(
                    VibrationEffect.createOneShot(duration, VibrationEffect.DEFAULT_AMPLITUDE)
                )
            } else {
                @Suppress("DEPRECATION")
                val vibrator = context.getSystemService(Context.VIBRATOR_SERVICE) as? Vibrator
                @Suppress("DEPRECATION")
                vibrator?.vibrate(duration)
            }
        } catch (_: Exception) {}
    }

    Box(
        modifier = modifier
            .fillMaxSize()
            .testTag("virtual_controller_overlay")
    ) {
        // MAIN TOUCH BUTTONS (Controllable via visibility toggle)
        AnimatedVisibility(
            visible = areControlsLocallyVisible,
            enter = fadeIn(),
            exit = fadeOut(),
            modifier = Modifier.fillMaxSize()
        ) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .alpha(customization.opacity)
                    .scale(customization.globalScale)
            ) {
                // TOP SHOULDER TRIGGERS & MACRO
                Row(
                    modifier = Modifier
                        .align(Alignment.TopCenter)
                        .fillMaxSize()
                        .padding(horizontal = 20.dp, vertical = 8.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.Top
                ) {
                    // Left Shoulders + L3
                    Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                        AdaptiveTriggerButton(
                            label = "LT",
                            subLabel = "Fijar",
                            testTag = "btn_trigger_lt",
                            onClick = {
                                performHaptic()
                                onButtonPressed("LT")
                            }
                        )
                        AdaptiveTriggerButton(
                            label = "LB",
                            subLabel = "Guardia",
                            testTag = "btn_bumper_lb",
                            onClick = {
                                performHaptic()
                                onButtonPressed("LB")
                            }
                        )
                        AdaptiveTriggerButton(
                            label = "L3",
                            subLabel = "Esprint",
                            testTag = "btn_trigger_l3",
                            onClick = {
                                performHaptic()
                                onButtonPressed("L3")
                            }
                        )
                    }

                    // Center Kakuja Awakening Combo Macro
                    if (customization.enableKaguneComboMacro) {
                        Surface(
                            onClick = {
                                performHaptic()
                                onButtonPressed("RB+RT")
                            },
                            shape = RoundedCornerShape(20.dp),
                            color = KaguneDarkRed.copy(alpha = 0.9f),
                            border = androidx.compose.foundation.BorderStroke(1.5.dp, KaguneAwakeningGlow),
                            modifier = Modifier
                                .padding(top = 4.dp)
                                .testTag("btn_macro_awakening")
                        ) {
                            Row(
                                modifier = Modifier.padding(horizontal = 14.dp, vertical = 6.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(6.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.FlashOn,
                                    contentDescription = "Awakening",
                                    tint = KaguneAwakeningGlow,
                                    modifier = Modifier.size(16.dp)
                                )
                                Text(
                                    text = "RB+RT KAKUJA",
                                    color = Color.White,
                                    fontSize = 11.5.sp,
                                    fontWeight = FontWeight.Black
                                )
                            }
                        }
                    }

                    // Right Shoulders + R3
                    Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                        AdaptiveTriggerButton(
                            label = "R3",
                            subLabel = "Lock",
                            testTag = "btn_trigger_r3",
                            onClick = {
                                performHaptic()
                                onButtonPressed("R3")
                            }
                        )
                        AdaptiveTriggerButton(
                            label = "RB",
                            subLabel = "Kagune",
                            testTag = "btn_bumper_rb",
                            onClick = {
                                performHaptic()
                                onButtonPressed("RB")
                            }
                        )
                        AdaptiveTriggerButton(
                            label = "RT",
                            subLabel = "Especial",
                            testTag = "btn_trigger_rt",
                            onClick = {
                                performHaptic()
                                onButtonPressed("RT")
                            }
                        )
                    }
                }

                // BOTTOM LEFT: ANALOG JOYSTICK & D-PAD & DEDICATED L3 BUTTON
                Column(
                    modifier = Modifier
                        .align(Alignment.BottomStart)
                        .padding(start = 24.dp, bottom = 18.dp),
                    verticalArrangement = Arrangement.spacedBy(14.dp)
                ) {
                    // D-Pad above the analog stick
                    AnimeDPadCross(
                        onDirectionPress = { dir ->
                            performHaptic()
                            onButtonPressed("DPAD_$dir")
                        },
                        modifier = Modifier.padding(start = 12.dp)
                    )

                    // Big Analog Joystick + Dedicated L3 Action Button
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        AnimeAnalogStick(
                            label = "L3",
                            onStickMoved = { dir -> onButtonPressed("MOVE_$dir") },
                            onCenterClick = {
                                performHaptic()
                                onButtonPressed("L3")
                            },
                            testTag = "left_analog_stick"
                        )

                        DedicatedStickButton(
                            label = "L3",
                            subLabel = "Sprint",
                            testTag = "btn_dedicated_l3",
                            onClick = {
                                performHaptic()
                                onButtonPressed("L3")
                            }
                        )
                    }
                }

                // BOTTOM RIGHT: DEDICATED R3 BUTTON + ADAPTED ACTION CLUSTER
                Row(
                    modifier = Modifier
                        .align(Alignment.BottomEnd)
                        .padding(end = 56.dp, bottom = 18.dp),
                    verticalAlignment = Alignment.Bottom,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    DedicatedStickButton(
                        label = "R3",
                        subLabel = "Lock",
                        testTag = "btn_dedicated_r3",
                        onClick = {
                            performHaptic()
                            onButtonPressed("R3")
                        }
                    )

                    AdaptedActionCluster(
                        theme = customization.controlTheme,
                        showHexSkills = customization.showHexSkills,
                        onButtonPressed = { btn ->
                            performHaptic()
                            onButtonPressed(btn)
                        }
                    )
                }
            }
        }

        // RIGHT-EDGE VERTICAL UTILITY DOCK (MATCHING USER PHOTO)
        if (customization.showUtilityDock) {
            Column(
                modifier = Modifier
                    .align(Alignment.CenterEnd)
                    .padding(end = 6.dp)
                    .clip(RoundedCornerShape(20.dp))
                    .background(Color(0xCC0E1A2C))
                    .border(1.dp, Color(0xFF264670), RoundedCornerShape(20.dp))
                    .padding(vertical = 6.dp, horizontal = 4.dp)
                    .testTag("utility_dock_column"),
                verticalArrangement = Arrangement.spacedBy(6.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // 1. Settings ⚙️
                UtilityDockIcon(
                    icon = Icons.Default.Settings,
                    contentDesc = "Ajustes",
                    tag = "btn_dock_settings",
                    onClick = onSettingsClick
                )

                // 2. Menu / Controls ☰
                UtilityDockIcon(
                    icon = Icons.Default.Menu,
                    contentDesc = "Menú",
                    tag = "btn_dock_menu",
                    onClick = onSettingsClick
                )

                // 3. Eye / Visibility Toggle 👁️
                UtilityDockIcon(
                    icon = if (areControlsLocallyVisible) Icons.Default.Visibility else Icons.Default.VisibilityOff,
                    contentDesc = "Ocultar Controles",
                    tag = "btn_dock_visibility",
                    onClick = { areControlsLocallyVisible = !areControlsLocallyVisible }
                )

                // 4. Pause ⏸️
                UtilityDockIcon(
                    icon = Icons.Default.Pause,
                    contentDesc = "Pausa",
                    tag = "btn_dock_pause",
                    onClick = onPauseClick
                )

                // 5. Aspect Ratio / Stretch ↔️
                UtilityDockIcon(
                    icon = Icons.Default.AspectRatio,
                    contentDesc = "Pantalla",
                    tag = "btn_dock_aspect",
                    onClick = { onButtonPressed("TOGGLE_ASPECT_RATIO") }
                )
            }
        }
    }
}

@Composable
fun UtilityDockIcon(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    contentDesc: String,
    tag: String,
    onClick: () -> Unit
) {
    Surface(
        onClick = onClick,
        shape = CircleShape,
        color = Color(0xFF162A45),
        border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFF385E8C)),
        modifier = Modifier
            .size(32.dp)
            .testTag(tag)
    ) {
        Box(contentAlignment = Alignment.Center) {
            Icon(
                imageVector = icon,
                contentDescription = contentDesc,
                tint = Color.White,
                modifier = Modifier.size(17.dp)
            )
        }
    }
}

// ADAPTED ACTION CLUSTER: Replicates the photo with prominent B Melee Fist, X Shuriken, Y Spiral, A Jump & Hexagon Skills
@Composable
fun AdaptedActionCluster(
    theme: ControlTheme,
    showHexSkills: Boolean,
    onButtonPressed: (String) -> Unit
) {
    Column(
        horizontalAlignment = Alignment.End,
        verticalArrangement = Arrangement.spacedBy(6.dp)
    ) {
        // TOP: Hexagonal Jutsu / Awakening Skills (奥 • 術 • ミト)
        if (showHexSkills) {
            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(end = 12.dp)
            ) {
                // Hexagon 1: ミト (Combo / Assist)
                HexagonSkillButton(
                    kanji = "ミト",
                    sub = "Combo",
                    tag = "btn_hex_combo",
                    onClick = { onButtonPressed("ミト") }
                )

                // Hexagon 2: 奥 (Ougi - Ultimate Kakuja)
                HexagonSkillButton(
                    kanji = "奥",
                    sub = "Kakuja",
                    highlightColor = KaguneCrimson,
                    tag = "btn_hex_ougi",
                    onClick = { onButtonPressed("奥") }
                )

                // Hexagon 3: 術 (Jutsu - Kagune Art)
                HexagonSkillButton(
                    kanji = "術",
                    sub = "Kagune",
                    highlightColor = QuinqueAccent,
                    tag = "btn_hex_jutsu",
                    onClick = { onButtonPressed("術") }
                )
            }
        }

        // MAIN ACTION BUTTONS: B (Big Melee Fist), X (Shuriken), Y (Spiral), A (Jump)
        Box(
            modifier = Modifier
                .size(200.dp)
                .testTag("action_buttons_diamond"),
            contentAlignment = Alignment.Center
        ) {
            // [Y] TOP: Spiral Vortex Attack
            AnimeCircleButton(
                letter = "Y",
                subText = "Espiral",
                iconType = ButtonIconType.SPIRAL_VORTEX,
                size = 54.dp,
                modifier = Modifier
                    .align(Alignment.TopCenter)
                    .offset(y = (-4).dp),
                testTag = "btn_action_y",
                onClick = { onButtonPressed("Y") }
            )

            // [X] LEFT: 4-Point Shuriken / Claw Attack
            AnimeCircleButton(
                letter = "X",
                subText = "Shuriken",
                iconType = ButtonIconType.SHURIKEN_STAR,
                size = 54.dp,
                modifier = Modifier
                    .align(Alignment.CenterStart)
                    .offset(x = 6.dp),
                testTag = "btn_action_x",
                onClick = { onButtonPressed("X") }
            )

            // [A] BOTTOM-LEFT: Acrobatics / Jump
            AnimeCircleButton(
                letter = "A",
                subText = "Salto",
                iconType = ButtonIconType.JUMP_FIGURE,
                size = 54.dp,
                modifier = Modifier
                    .align(Alignment.BottomStart)
                    .offset(x = 22.dp, y = (-2).dp),
                testTag = "btn_action_a",
                onClick = { onButtonPressed("A") }
            )

            // [B] BOTTOM-CENTER / RIGHT: LARGE MELEE FIST BUTTON (PROMINENT FROM PHOTO!)
            AnimeCircleButton(
                letter = "B",
                subText = "Golpe",
                iconType = ButtonIconType.MELEE_FIST,
                size = 76.dp, // Noticeably larger as in user's photo
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .offset(x = 8.dp, y = 6.dp),
                testTag = "btn_action_b",
                onClick = { onButtonPressed("B") }
            )

            // SHOULDER QUICK BUTTONS [RB & LB]
            Row(
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .offset(x = 8.dp, y = (-2).dp),
                horizontalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                SmallShoulderCircle(
                    label = "LB",
                    tag = "btn_circle_lb",
                    onClick = { onButtonPressed("LB") }
                )
                SmallShoulderCircle(
                    label = "RB",
                    tag = "btn_circle_rb",
                    onClick = { onButtonPressed("RB") }
                )
            }
        }
    }
}

enum class ButtonIconType {
    MELEE_FIST,
    SHURIKEN_STAR,
    SPIRAL_VORTEX,
    JUMP_FIGURE
}

@Composable
fun AnimeCircleButton(
    letter: String,
    subText: String,
    iconType: ButtonIconType,
    size: androidx.compose.ui.unit.Dp,
    modifier: Modifier = Modifier,
    testTag: String,
    onClick: () -> Unit
) {
    Surface(
        onClick = onClick,
        shape = CircleShape,
        color = MetallicButtonBg,
        border = androidx.compose.foundation.BorderStroke(2.5.dp, MetallicButtonBorder),
        shadowElevation = 8.dp,
        modifier = modifier
            .size(size)
            .testTag(testTag)
    ) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            // Draw vector glyph based on user's photo
            Canvas(modifier = Modifier.size(size * 0.58f)) {
                val center = Offset(this.size.width / 2, this.size.height / 2)
                val radius = this.size.minDimension / 2

                when (iconType) {
                    ButtonIconType.MELEE_FIST -> {
                        // Bold Melee Fist Glyph (like B in photo)
                        val fistPath = Path().apply {
                            // Fist outline
                            addRoundRect(
                                androidx.compose.ui.geometry.RoundRect(
                                    left = center.x - radius * 0.7f,
                                    top = center.y - radius * 0.65f,
                                    right = center.x + radius * 0.7f,
                                    bottom = center.y + radius * 0.75f,
                                    cornerRadius = androidx.compose.ui.geometry.CornerRadius(12f, 12f)
                                )
                            )
                        }
                        drawPath(fistPath, color = MetallicIconColor)

                        // 4 Finger segment slits
                        for (i in 0..3) {
                            val startX = center.x - radius * 0.45f + i * (radius * 0.3f)
                            drawLine(
                                color = MetallicButtonBg,
                                start = Offset(startX, center.y - radius * 0.5f),
                                end = Offset(startX, center.y + radius * 0.2f),
                                strokeWidth = 3f,
                                cap = StrokeCap.Round
                            )
                        }
                    }

                    ButtonIconType.SHURIKEN_STAR -> {
                        // 4-point Shuriken / Ninja Star glyph (like X in photo)
                        val starPath = Path().apply {
                            moveTo(center.x, center.y - radius)
                            lineTo(center.x + radius * 0.25f, center.y - radius * 0.25f)
                            lineTo(center.x + radius, center.y)
                            lineTo(center.x + radius * 0.25f, center.y + radius * 0.25f)
                            lineTo(center.x, center.y + radius)
                            lineTo(center.x - radius * 0.25f, center.y + radius * 0.25f)
                            lineTo(center.x - radius, center.y)
                            lineTo(center.x - radius * 0.25f, center.y - radius * 0.25f)
                            close()
                        }
                        drawPath(starPath, color = MetallicIconColor)
                        drawCircle(color = MetallicButtonBg, radius = radius * 0.22f, center = center)
                    }

                    ButtonIconType.SPIRAL_VORTEX -> {
                        // Chakra / Kagune Spiral Vortex (like Y in photo)
                        drawCircle(
                            color = MetallicIconColor,
                            radius = radius * 0.82f,
                            center = center,
                            style = Stroke(width = 4f)
                        )
                        drawCircle(
                            color = MetallicIconColor,
                            radius = radius * 0.48f,
                            center = center,
                            style = Stroke(width = 3.5f)
                        )
                        drawCircle(color = MetallicIconColor, radius = radius * 0.2f, center = center)
                    }

                    ButtonIconType.JUMP_FIGURE -> {
                        // Jumping silhouette figure (like A in photo)
                        drawCircle(color = MetallicIconColor, radius = radius * 0.26f, center = Offset(center.x, center.y - radius * 0.5f))
                        drawLine(
                            color = MetallicIconColor,
                            start = Offset(center.x, center.y - radius * 0.25f),
                            end = Offset(center.x, center.y + radius * 0.25f),
                            strokeWidth = 5f,
                            cap = StrokeCap.Round
                        )
                        // Left leg jumping
                        drawLine(
                            color = MetallicIconColor,
                            start = Offset(center.x, center.y + radius * 0.25f),
                            end = Offset(center.x - radius * 0.55f, center.y + radius * 0.7f),
                            strokeWidth = 5f,
                            cap = StrokeCap.Round
                        )
                        // Right leg bent
                        drawLine(
                            color = MetallicIconColor,
                            start = Offset(center.x, center.y + radius * 0.25f),
                            end = Offset(center.x + radius * 0.55f, center.y + radius * 0.65f),
                            strokeWidth = 5f,
                            cap = StrokeCap.Round
                        )
                    }
                }
            }

            // Small badge with button letter (B, X, Y, A) at corner
            Surface(
                shape = CircleShape,
                color = Color(0xFF14243B),
                modifier = Modifier
                    .align(Alignment.BottomStart)
                    .padding(4.dp)
                    .size(16.dp)
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Text(
                        text = letter,
                        color = Color.White,
                        fontSize = 9.sp,
                        fontWeight = FontWeight.Black
                    )
                }
            }
        }
    }
}

// HEXAGON SKILL BUTTON: Custom Japanese Kanji / Special Jutsu Button (奥 • 術 • ミト)
@Composable
fun HexagonSkillButton(
    kanji: String,
    sub: String,
    highlightColor: Color = Color(0xFF1C2C47),
    tag: String,
    onClick: () -> Unit
) {
    Surface(
        onClick = onClick,
        shape = RoundedCornerShape(10.dp),
        color = HexagonSkillBg,
        border = androidx.compose.foundation.BorderStroke(1.8.dp, MetallicButtonBorder),
        shadowElevation = 6.dp,
        modifier = Modifier
            .size(width = 44.dp, height = 44.dp)
            .testTag(tag)
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier.padding(2.dp)
        ) {
            Text(
                text = kanji,
                color = highlightColor,
                fontSize = 17.sp,
                fontWeight = FontWeight.Black
            )
            Text(
                text = sub,
                color = Color(0xFF5A7294),
                fontSize = 7.5.sp,
                fontWeight = FontWeight.Bold
            )
        }
    }
}

@Composable
fun SmallShoulderCircle(
    label: String,
    tag: String,
    onClick: () -> Unit
) {
    Surface(
        onClick = onClick,
        shape = CircleShape,
        color = MetallicButtonBg,
        border = androidx.compose.foundation.BorderStroke(1.5.dp, MetallicButtonBorder),
        modifier = Modifier
            .size(34.dp)
            .testTag(tag)
    ) {
        Box(contentAlignment = Alignment.Center) {
            Text(
                text = label,
                color = MetallicIconColor,
                fontSize = 11.sp,
                fontWeight = FontWeight.Black
            )
        }
    }
}

// DPAD CROSS WITH 4 ARROWS
@Composable
fun AnimeDPadCross(
    onDirectionPress: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .size(110.dp)
            .testTag("dpad_controls"),
        contentAlignment = Alignment.Center
    ) {
        // UP
        AnimeDPadPill(
            icon = Icons.Default.KeyboardArrowUp,
            tag = "dpad_up",
            modifier = Modifier.align(Alignment.TopCenter),
            onClick = { onDirectionPress("UP") }
        )
        // DOWN
        AnimeDPadPill(
            icon = Icons.Default.KeyboardArrowDown,
            tag = "dpad_down",
            modifier = Modifier.align(Alignment.BottomCenter),
            onClick = { onDirectionPress("DOWN") }
        )
        // LEFT
        AnimeDPadPill(
            icon = Icons.AutoMirrored.Filled.KeyboardArrowLeft,
            tag = "dpad_left",
            modifier = Modifier.align(Alignment.CenterStart),
            onClick = { onDirectionPress("LEFT") }
        )
        // RIGHT
        AnimeDPadPill(
            icon = Icons.AutoMirrored.Filled.KeyboardArrowRight,
            tag = "dpad_right",
            modifier = Modifier.align(Alignment.CenterEnd),
            onClick = { onDirectionPress("RIGHT") }
        )
    }
}

@Composable
fun AnimeDPadPill(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    tag: String,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    Surface(
        onClick = onClick,
        shape = RoundedCornerShape(8.dp),
        color = MetallicButtonBg,
        border = androidx.compose.foundation.BorderStroke(1.5.dp, MetallicButtonBorder),
        modifier = modifier
            .size(32.dp)
            .testTag(tag)
    ) {
        Box(contentAlignment = Alignment.Center) {
            Icon(
                imageVector = icon,
                contentDescription = tag,
                tint = MetallicIconColor,
                modifier = Modifier.size(20.dp)
            )
        }
    }
}

// LARGE CIRCULAR ANALOG JOYSTICK WITH DIRECTION TICKS
@Composable
fun AnimeAnalogStick(
    label: String,
    onStickMoved: (String) -> Unit,
    onCenterClick: () -> Unit,
    testTag: String,
    modifier: Modifier = Modifier
) {
    var offsetX by remember { mutableFloatStateOf(0f) }
    var offsetY by remember { mutableFloatStateOf(0f) }
    val maxRadius = 50f

    Box(
        modifier = modifier
            .size(126.dp)
            .clip(CircleShape)
            .background(Color(0x33101E33))
            .border(2.dp, MetallicButtonBorder.copy(alpha = 0.7f), CircleShape)
            .testTag(testTag),
        contentAlignment = Alignment.Center
    ) {
        // Outer directional arrows (▲, ▼, ◄, ►)
        Canvas(modifier = Modifier.fillMaxSize()) {
            val c = Offset(size.width / 2, size.height / 2)
            val r = size.minDimension / 2 - 8.dp.toPx()
            val tickColor = MetallicButtonBorder.copy(alpha = 0.85f)

            // 4 directional dots / ticks
            drawCircle(color = tickColor, radius = 3.dp.toPx(), center = Offset(c.x, c.y - r))
            drawCircle(color = tickColor, radius = 3.dp.toPx(), center = Offset(c.x, c.y + r))
            drawCircle(color = tickColor, radius = 3.dp.toPx(), center = Offset(c.x - r, c.y))
            drawCircle(color = tickColor, radius = 3.dp.toPx(), center = Offset(c.x + r, c.y))
        }

        // Draggable Knob (Inner thumbstick)
        Box(
            modifier = Modifier
                .offset { IntOffset(offsetX.roundToInt(), offsetY.roundToInt()) }
                .size(54.dp)
                .shadow(6.dp, CircleShape)
                .clip(CircleShape)
                .background(MetallicButtonBg)
                .border(2.dp, Color.White, CircleShape)
                .pointerInput(Unit) {
                    detectDragGestures(
                        onDragEnd = {
                            offsetX = 0f
                            offsetY = 0f
                        },
                        onDragCancel = {
                            offsetX = 0f
                            offsetY = 0f
                        },
                        onDrag = { change, dragAmount ->
                            change.consume()
                            val nextX = (offsetX + dragAmount.x).coerceIn(-maxRadius, maxRadius)
                            val nextY = (offsetY + dragAmount.y).coerceIn(-maxRadius, maxRadius)
                            offsetX = nextX
                            offsetY = nextY

                            val dir = when {
                                nextY < -18 && kotlin.math.abs(nextX) < 18 -> "UP"
                                nextY > 18 && kotlin.math.abs(nextX) < 18 -> "DOWN"
                                nextX < -18 -> "LEFT"
                                nextX > 18 -> "RIGHT"
                                else -> "IDLE"
                            }
                            if (dir != "IDLE") {
                                onStickMoved(dir)
                            }
                        }
                    )
                },
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = label,
                fontSize = 11.sp,
                fontWeight = FontWeight.Black,
                color = MetallicIconColor
            )
        }
    }
}

@Composable
fun AdaptiveTriggerButton(
    label: String,
    subLabel: String,
    testTag: String,
    onClick: () -> Unit
) {
    Surface(
        onClick = onClick,
        shape = RoundedCornerShape(10.dp),
        color = MetallicButtonBg,
        border = androidx.compose.foundation.BorderStroke(1.5.dp, MetallicButtonBorder),
        modifier = Modifier
            .size(width = 62.dp, height = 40.dp)
            .testTag(testTag)
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier.padding(2.dp)
        ) {
            Text(
                text = label,
                color = MetallicIconColor,
                fontSize = 13.sp,
                fontWeight = FontWeight.Black
            )
            Text(
                text = subLabel,
                color = Color(0xFF5A7294),
                fontSize = 8.sp,
                fontWeight = FontWeight.Bold
            )
        }
    }
}

@Composable
fun DedicatedStickButton(
    label: String,
    subLabel: String,
    testTag: String,
    onClick: () -> Unit
) {
    Surface(
        onClick = onClick,
        shape = CircleShape,
        color = MetallicButtonBg,
        border = androidx.compose.foundation.BorderStroke(2.dp, MetallicButtonBorder),
        shadowElevation = 6.dp,
        modifier = Modifier
            .size(46.dp)
            .testTag(testTag)
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier.padding(2.dp)
        ) {
            Text(
                text = label,
                color = MetallicIconColor,
                fontSize = 13.5.sp,
                fontWeight = FontWeight.Black
            )
            Text(
                text = subLabel,
                color = Color(0xFF5A7294),
                fontSize = 7.5.sp,
                fontWeight = FontWeight.Bold
            )
        }
    }
}

