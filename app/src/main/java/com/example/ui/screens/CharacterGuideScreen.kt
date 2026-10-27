package com.example.ui.screens

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
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.FlashOn
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
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
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.data.LauncherUiState
import com.example.model.ComboMove
import com.example.model.GhoulCharacter
import com.example.model.TokyoGhoulRoster
import com.example.ui.LauncherViewModel
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
fun CharacterGuideScreen(
    uiState: LauncherUiState,
    viewModel: LauncherViewModel,
    onLaunchGame: () -> Unit,
    modifier: Modifier = Modifier
) {
    val scrollState = rememberScrollState()
    var selectedCharacterId by remember { mutableStateOf(TokyoGhoulRoster.characters.first().id) }
    val character = TokyoGhoulRoster.characters.firstOrNull { it.id == selectedCharacterId }
        ?: TokyoGhoulRoster.characters.first()

    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(scrollState)
            .padding(14.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        // TITLE
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text(
                    text = "GUÍA DE COMBATE Y COMBOS",
                    color = Color.White,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Black,
                    letterSpacing = 1.sp
                )
                Text(
                    text = "Mapeo de botones adaptado por personaje",
                    color = QuinqueAccent,
                    fontSize = 11.5.sp
                )
            }
            Button(
                onClick = onLaunchGame,
                colors = ButtonDefaults.buttonColors(containerColor = KaguneCrimson),
                shape = RoundedCornerShape(10.dp)
            ) {
                Text("Entrenar", fontSize = 11.sp, fontWeight = FontWeight.Bold)
            }
        }

        // CHARACTER SELECTOR ROW
        LazyRow(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            items(TokyoGhoulRoster.characters) { charItem ->
                val isSelected = charItem.id == selectedCharacterId
                Surface(
                    onClick = { selectedCharacterId = charItem.id },
                    shape = RoundedCornerShape(12.dp),
                    color = if (isSelected) KaguneDarkRed.copy(alpha = 0.5f) else GhoulSurfaceCard,
                    border = androidx.compose.foundation.BorderStroke(
                        1.5.dp,
                        if (isSelected) KaguneCrimson else GhoulBorder
                    ),
                    modifier = Modifier.testTag("char_tab_${charItem.id}")
                ) {
                    Column(
                        modifier = Modifier.padding(horizontal = 14.dp, vertical = 8.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = charItem.name,
                            color = if (isSelected) Color.White else QuinqueSilver,
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = charItem.type.take(12),
                            color = if (isSelected) QuinqueAccent else GhoulTextMuted,
                            fontSize = 10.sp
                        )
                    }
                }
            }
        }

        // CHARACTER PROFILE HERO CARD WITH OFFICIAL COVER ART
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = GhoulSurfaceElevated),
            border = androidx.compose.foundation.BorderStroke(1.dp, KaguneCrimson.copy(alpha = 0.6f))
        ) {
            Column(modifier = Modifier.padding(14.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.tg_box_art),
                        contentDescription = "Box Art Thumbnail",
                        modifier = Modifier
                            .size(68.dp)
                            .clip(RoundedCornerShape(12.dp))
                            .border(1.dp, QuinqueSilver.copy(alpha = 0.4f), RoundedCornerShape(12.dp)),
                        contentScale = ContentScale.Crop
                    )

                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = character.name,
                            color = Color.White,
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Black
                        )
                        Text(
                            text = character.alias,
                            color = KaguneCrimson,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = "${character.faction} • ${character.type}",
                            color = QuinqueSilver.copy(alpha = 0.8f),
                            fontSize = 11.sp
                        )
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))

                Text(
                    text = character.description,
                    color = QuinqueSilver,
                    fontSize = 12.sp
                )

                Spacer(modifier = Modifier.height(8.dp))

                // Awakening Box
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(8.dp))
                        .background(KaguneDarkRed.copy(alpha = 0.35f))
                        .border(1.dp, KaguneCrimson.copy(alpha = 0.5f), RoundedCornerShape(8.dp))
                        .padding(8.dp)
                ) {
                    Row(
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
                            text = character.awakeningForm,
                            color = Color.White,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))

                Button(
                    onClick = {
                        viewModel.selectCharacter(character.id)
                        onLaunchGame()
                    },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(10.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = KaguneCrimson)
                ) {
                    Text("Seleccionar y Probar Combate", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                }
            }
        }

        // COMBO MOVELIST SECTION
        Text(
            text = "Lista de Movimientos y Botones Mapeados",
            color = Color.White,
            fontSize = 14.sp,
            fontWeight = FontWeight.Bold
        )

        character.moves.forEach { move ->
            ComboMoveCard(move = move)
        }
    }
}

@Composable
fun ComboMoveCard(move: ComboMove) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = GhoulSurfaceCard),
        border = androidx.compose.foundation.BorderStroke(1.dp, GhoulBorder)
    ) {
        Column(modifier = Modifier.padding(12.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = move.title,
                    color = Color.White,
                    fontSize = 13.5.sp,
                    fontWeight = FontWeight.Bold
                )
                Surface(
                    shape = RoundedCornerShape(6.dp),
                    color = GhoulSurfaceElevated
                ) {
                    Text(
                        text = move.damageMultiplier,
                        color = StatusGreen,
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(6.dp))

            // Controller Button Sequence Badges
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                move.buttonSequence.forEach { btn ->
                    if (btn == "->" || btn == "+") {
                        Text(
                            text = btn,
                            color = QuinqueSilver.copy(alpha = 0.6f),
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold
                        )
                    } else {
                        Surface(
                            shape = CircleShape,
                            color = when (btn) {
                                "X" -> Color(0xFF00E5FF).copy(alpha = 0.2f)
                                "Y" -> Color(0xFFFFB300).copy(alpha = 0.2f)
                                "B" -> KaguneCrimson.copy(alpha = 0.2f)
                                "A" -> Color(0xFF00E676).copy(alpha = 0.2f)
                                else -> GhoulSurfaceElevated
                            },
                            border = androidx.compose.foundation.BorderStroke(
                                1.dp,
                                when (btn) {
                                    "X" -> Color(0xFF00E5FF)
                                    "Y" -> Color(0xFFFFB300)
                                    "B" -> KaguneCrimson
                                    "A" -> Color(0xFF00E676)
                                    else -> QuinqueSilver
                                }
                            ),
                            modifier = Modifier.size(24.dp)
                        ) {
                            Box(contentAlignment = Alignment.Center) {
                                Text(
                                    text = btn,
                                    color = Color.White,
                                    fontSize = 10.sp,
                                    fontWeight = FontWeight.Black
                                )
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = move.description,
                color = GhoulTextSecondary,
                fontSize = 11.5.sp
            )
        }
    }
}
