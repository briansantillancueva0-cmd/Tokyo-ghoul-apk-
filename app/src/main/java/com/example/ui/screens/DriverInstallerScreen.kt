package com.example.ui.screens

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.AnimatedVisibility
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.CloudDownload
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.FlashOn
import androidx.compose.material.icons.filled.Folder
import androidx.compose.material.icons.filled.FolderZip
import androidx.compose.material.icons.filled.Memory
import androidx.compose.material.icons.filled.Restore
import androidx.compose.material.icons.filled.Save
import androidx.compose.material.icons.filled.Speed
import androidx.compose.material.icons.filled.Tune
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.RadioButton
import androidx.compose.material3.RadioButtonDefaults
import androidx.compose.material3.Surface
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.LauncherUiState
import com.example.data.SaveDataProfile
import com.example.model.DriverProfile
import com.example.model.GpuDriver
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
fun DriverInstallerScreen(
    uiState: LauncherUiState,
    viewModel: LauncherViewModel,
    modifier: Modifier = Modifier
) {
    val scrollState = rememberScrollState()

    // Modals
    var showCustomDriverDialog by remember { mutableStateOf(false) }
    var showSaveDataZipDialog by remember { mutableStateOf(false) }
    var showCreateProfileDialog by remember { mutableStateOf(false) }
    var showOriginalGameFilesDialog by remember { mutableStateOf(false) }

    // Forms
    var customDriverNameInput by remember { mutableStateOf("Turnip v24.3.5 Mesa Vulkan (Adreno 7xx UE4)") }
    var customDriverPathInput by remember { mutableStateOf("/storage/emulated/0/Download/turnip_v24.3.5.zip") }

    var saveZipPathInput by remember { mutableStateOf("/storage/emulated/0/Download/savedata_tokyoghoul_stream.zip") }
    var saveTargetAndroidPath by remember { mutableStateOf(uiState.activeSaveDataPath) }

    var originalGameZipPath by remember { mutableStateOf("/storage/emulated/0/Download/TokyoGhoul_re_CallToExist.zip") }
    var originalGameTargetFolder by remember { mutableStateOf("/storage/emulated/0/Games/Tokyo Ghoul re Call to Exist") }

    var newProfileName by remember { mutableStateOf("Perfil Personalizado UE4") }
    var newProfileDesc by remember { mutableStateOf("Ajuste a medida de controladores y resolución") }
    var newProfileIcon by remember { mutableStateOf("⚡") }

    // SAF file pickers
    val zipSavePickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        uri?.let {
            saveZipPathInput = it.path ?: "/storage/emulated/0/Download/savedata.zip"
            viewModel.installSaveDataFromZip(saveZipPathInput, saveTargetAndroidPath)
        }
    }

    val driverZipPickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        uri?.let {
            val fileName = it.lastPathSegment?.substringAfterLast("/") ?: "turnip_driver.zip"
            val filePath = it.path ?: "/storage/emulated/0/Download/$fileName"
            viewModel.installOriginalDriverFile(fileName, filePath, "/usr/lib/aarch64-linux-gnu/")
        }
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(scrollState)
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // HEADER TITLE
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text(
                    text = "DRIVERS Y ARCHIVOS ORIGINALES",
                    color = Color.White,
                    fontSize = 19.sp,
                    fontWeight = FontWeight.Black,
                    letterSpacing = 1.sp
                )
                Text(
                    text = "Turnip, DXVK, SaveData ZIP y Archivos del Juego",
                    color = LauncherNeonBlue,
                    fontSize = 11.5.sp
                )
            }
            Surface(
                shape = RoundedCornerShape(10.dp),
                color = GhoulSurfaceElevated,
                border = androidx.compose.foundation.BorderStroke(1.dp, StatusGreen.copy(alpha = 0.5f))
            ) {
                Row(
                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .size(6.dp)
                            .clip(CircleShape)
                            .background(StatusGreen)
                    )
                    Text(
                        text = "Vulkan 1.3 OK",
                        color = StatusGreen,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }

        // INSTALLATION PROGRESS OVERLAY IF ACTIVE
        AnimatedVisibility(visible = uiState.isInstallingDriver) {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("driver_install_progress_card"),
                shape = RoundedCornerShape(14.dp),
                colors = CardDefaults.cardColors(containerColor = Color(0xFF102647)),
                border = androidx.compose.foundation.BorderStroke(1.5.dp, LauncherNeonBlue)
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        CircularProgressIndicator(
                            color = LauncherNeonBlue,
                            modifier = Modifier.size(20.dp),
                            strokeWidth = 2.5.dp
                        )
                        Text(
                            text = "Instalando en el contenedor Android...",
                            color = Color.White,
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                    LinearProgressIndicator(
                        progress = { uiState.installPercent },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(6.dp)
                            .clip(RoundedCornerShape(3.dp)),
                        color = LauncherNeonBlue,
                        trackColor = GhoulDarkBg
                    )
                    Text(
                        text = uiState.installProgressText,
                        color = QuinqueSilver,
                        fontSize = 11.sp
                    )
                }
            }
        }

        // SECTION 1: USER DRIVER PROFILES (PERFILES DE RENDIMIENTO Y CALIDAD)
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(14.dp),
            colors = CardDefaults.cardColors(containerColor = LauncherCardBg),
            border = androidx.compose.foundation.BorderStroke(1.dp, LauncherCardBorder)
        ) {
            Column(modifier = Modifier.padding(14.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(
                            text = "Perfiles de Drivers Guardados",
                            color = Color.White,
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = "Cambia entre modos de rendimiento y calidad en 1 toque",
                            color = Color(0xFF8BA5CC),
                            fontSize = 10.5.sp
                        )
                    }
                    Button(
                        onClick = { showCreateProfileDialog = true },
                        colors = ButtonDefaults.buttonColors(containerColor = LauncherActivePill),
                        shape = RoundedCornerShape(8.dp),
                        modifier = Modifier.height(32.dp)
                    ) {
                        Text("+ Crear Perfil", fontSize = 10.5.sp, fontWeight = FontWeight.Bold)
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))

                uiState.profiles.forEach { profile ->
                    val isSelected = uiState.activeProfileId == profile.id
                    Surface(
                        onClick = { viewModel.applyProfile(profile.id) },
                        shape = RoundedCornerShape(10.dp),
                        color = if (isSelected) LauncherActivePill.copy(alpha = 0.6f) else LauncherInactivePill,
                        border = androidx.compose.foundation.BorderStroke(
                            1.dp,
                            if (isSelected) LauncherNeonBlue else LauncherCardBorder
                        ),
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 4.dp)
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
                                Text(text = profile.iconEmoji, fontSize = 18.sp)
                                Column {
                                    Row(
                                        verticalAlignment = Alignment.CenterVertically,
                                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                                    ) {
                                        Text(
                                            text = profile.name,
                                            color = Color.White,
                                            fontSize = 12.5.sp,
                                            fontWeight = FontWeight.Bold
                                        )
                                        Surface(
                                            shape = RoundedCornerShape(4.dp),
                                            color = if (isSelected) LauncherNeonBlue else Color(0xFF1E3557)
                                        ) {
                                            Text(
                                                text = if (isSelected) "ACTIVO" else profile.badgeTag,
                                                color = if (isSelected) Color.Black else Color.White,
                                                fontSize = 8.5.sp,
                                                fontWeight = FontWeight.Black,
                                                modifier = Modifier.padding(horizontal = 4.dp, vertical = 2.dp)
                                            )
                                        }
                                    }
                                    Text(
                                        text = "${profile.resolution} • ${profile.driverName} • ${profile.dxvkName} • ${profile.upscalingMode}",
                                        color = Color(0xFF8BA5CC),
                                        fontSize = 10.sp
                                    )
                                    Text(
                                        text = profile.description,
                                        color = QuinqueSilver.copy(alpha = 0.7f),
                                        fontSize = 9.sp
                                    )
                                }
                            }

                            if (!profile.isDefault) {
                                IconButton(
                                    onClick = { viewModel.deleteProfile(profile.id) },
                                    modifier = Modifier.size(28.dp)
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Delete,
                                        contentDescription = "Eliminar",
                                        tint = KaguneCrimson,
                                        modifier = Modifier.size(16.dp)
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }

        // SECTION 2: SAVEDATA STREAM EN ZIP CON DIRECCIÓN DE ANDROID
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(14.dp),
            colors = CardDefaults.cardColors(containerColor = LauncherCardBg),
            border = androidx.compose.foundation.BorderStroke(1.dp, LauncherCardBorder)
        ) {
            Column(modifier = Modifier.padding(14.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.FolderZip,
                            contentDescription = "SaveData Zip",
                            tint = Color(0xFFFFD54F),
                            modifier = Modifier.size(20.dp)
                        )
                        Text(
                            text = "SaveData Stream / Steam (Zip)",
                            color = Color.White,
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                    Button(
                        onClick = { showSaveDataZipDialog = true },
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF1E3A63)),
                        shape = RoundedCornerShape(8.dp),
                        modifier = Modifier.height(30.dp)
                    ) {
                        Text("Configurar Ruta", fontSize = 10.sp)
                    }
                }

                Spacer(modifier = Modifier.height(6.dp))

                Text(
                    text = "Dirección destino en Android (Wine/Winlator Steam Container):",
                    color = Color(0xFF8BA5CC),
                    fontSize = 10.sp
                )
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(6.dp))
                        .background(Color(0xFF091426))
                        .padding(8.dp)
                ) {
                    Text(
                        text = uiState.activeSaveDataPath,
                        color = LauncherNeonBlue,
                        fontSize = 10.sp,
                        fontFamily = androidx.compose.ui.text.font.FontFamily.Monospace
                    )
                }

                Spacer(modifier = Modifier.height(10.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    // Button 1: Inject 100% Stream Save
                    Button(
                        onClick = { viewModel.injectSteam100PercentSave(uiState.activeSaveDataPath) },
                        modifier = Modifier
                            .weight(1f)
                            .height(40.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = LauncherActivePill),
                        shape = RoundedCornerShape(10.dp)
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(4.dp)
                        ) {
                            Icon(imageVector = Icons.Default.FlashOn, contentDescription = "100%", modifier = Modifier.size(14.dp))
                            Text("Instalar Save 100% Stream", fontSize = 10.5.sp, fontWeight = FontWeight.Bold)
                        }
                    }

                    // Button 2: Import Custom Zip from device
                    OutlinedButton(
                        onClick = { zipSavePickerLauncher.launch("application/zip") },
                        modifier = Modifier
                            .weight(1f)
                            .height(40.dp),
                        shape = RoundedCornerShape(10.dp),
                        border = androidx.compose.foundation.BorderStroke(1.dp, LauncherNeonBlue)
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(4.dp)
                        ) {
                            Icon(imageVector = Icons.Default.Folder, contentDescription = "Examinar Zip", modifier = Modifier.size(14.dp))
                            Text("Importar ZIP de Save", fontSize = 10.5.sp, color = Color.White)
                        }
                    }
                }
            }
        }

        // SECTION 3: ORIGINAL DRIVERS & ORIGINAL GAME INSTALLER
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(14.dp),
            colors = CardDefaults.cardColors(containerColor = LauncherCardBg),
            border = androidx.compose.foundation.BorderStroke(1.dp, LauncherCardBorder)
        ) {
            Column(modifier = Modifier.padding(14.dp)) {
                Text(
                    text = "Instalación de Drivers y Archivos Originales",
                    color = Color.White,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "Inyecta librerías originales libvulkan o extrae el juego original en Android",
                    color = Color(0xFF8BA5CC),
                    fontSize = 11.sp
                )

                Spacer(modifier = Modifier.height(10.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Button(
                        onClick = { driverZipPickerLauncher.launch("*/*") },
                        modifier = Modifier
                            .weight(1f)
                            .height(42.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF1E3557)),
                        shape = RoundedCornerShape(10.dp)
                    ) {
                        Text("Instalar Driver Original (.so/.zip)", fontSize = 10.5.sp, color = Color.White)
                    }

                    Button(
                        onClick = { showOriginalGameFilesDialog = true },
                        modifier = Modifier
                            .weight(1f)
                            .height(42.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF1B2C47)),
                        shape = RoundedCornerShape(10.dp)
                    ) {
                        Text("Instalar Juego Original", fontSize = 10.5.sp, color = LauncherNeonBlue)
                    }
                }
            }
        }

        // SECTION 4: DRIVER CATALOG (TURNIP / VULKAN)
        Text(
            text = "Catálogo de Controladores Turnip / Vulkan",
            color = Color.White,
            fontSize = 14.sp,
            fontWeight = FontWeight.Bold
        )

        uiState.drivers.forEach { driver ->
            DriverCardItem(
                driver = driver,
                isSelected = driver.isSelected,
                onSelect = { viewModel.selectDriver(driver.id) }
            )
        }

        Spacer(modifier = Modifier.height(8.dp))

        // SECTION 5: DXVK TRANSLATION LAYER
        Text(
            text = "Capa de Traducción Direct3D 11 (DXVK)",
            color = Color.White,
            fontSize = 14.sp,
            fontWeight = FontWeight.Bold
        )

        uiState.dxvkVersions.forEach { dxvk ->
            Surface(
                onClick = { viewModel.selectDxvk(dxvk.id) },
                shape = RoundedCornerShape(12.dp),
                color = if (dxvk.isSelected) LauncherActivePill.copy(alpha = 0.5f) else LauncherCardBg,
                border = androidx.compose.foundation.BorderStroke(
                    1.dp,
                    if (dxvk.isSelected) LauncherNeonBlue else LauncherCardBorder
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("dxvk_item_${dxvk.id}")
            ) {
                Row(
                    modifier = Modifier.padding(12.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    RadioButton(
                        selected = dxvk.isSelected,
                        onClick = { viewModel.selectDxvk(dxvk.id) },
                        colors = RadioButtonDefaults.colors(
                            selectedColor = LauncherNeonBlue,
                            unselectedColor = GhoulTextMuted
                        )
                    )
                    Column(modifier = Modifier.weight(1f)) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            Text(
                                text = dxvk.title,
                                color = if (dxvk.isSelected) Color.White else QuinqueSilver,
                                fontSize = 13.sp,
                                fontWeight = FontWeight.Bold
                            )
                            if (dxvk.isAsync) {
                                Surface(
                                    shape = RoundedCornerShape(4.dp),
                                    color = StatusGreen.copy(alpha = 0.2f)
                                ) {
                                    Text(
                                        text = "ASYNC",
                                        color = StatusGreen,
                                        fontSize = 9.sp,
                                        fontWeight = FontWeight.Black,
                                        modifier = Modifier.padding(horizontal = 4.dp, vertical = 2.dp)
                                    )
                                }
                            }
                        }
                        Text(
                            text = dxvk.description,
                            color = GhoulTextSecondary,
                            fontSize = 11.sp
                        )
                    }
                }
            }
        }
    }

    // MODAL: SAVEDATA ZIP & ANDROID TARGET PATH DIALOG
    if (showSaveDataZipDialog) {
        AlertDialog(
            onDismissRequest = { showSaveDataZipDialog = false },
            title = {
                Text(
                    text = "Instalar SaveData Stream / Steam en ZIP",
                    color = Color.White,
                    fontWeight = FontWeight.Bold,
                    fontSize = 15.sp
                )
            },
            text = {
                Column(
                    modifier = Modifier.verticalScroll(rememberScrollState()),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Text(
                        text = "Ruta del archivo ZIP de guardado (Stream / Steam):",
                        color = Color(0xFF8BA5CC),
                        fontSize = 11.sp
                    )
                    OutlinedTextField(
                        value = saveZipPathInput,
                        onValueChange = { saveZipPathInput = it },
                        modifier = Modifier.fillMaxWidth(),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = LauncherNeonBlue,
                            unfocusedBorderColor = LauncherCardBorder,
                            focusedTextColor = Color.White,
                            unfocusedTextColor = Color.White
                        ),
                        singleLine = true
                    )

                    Text(
                        text = "Dirección de Android de destino (Contenedor Wine / Winlator):",
                        color = Color(0xFF8BA5CC),
                        fontSize = 11.sp
                    )
                    OutlinedTextField(
                        value = saveTargetAndroidPath,
                        onValueChange = { saveTargetAndroidPath = it },
                        modifier = Modifier.fillMaxWidth(),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = LauncherNeonBlue,
                            unfocusedBorderColor = LauncherCardBorder,
                            focusedTextColor = Color.White,
                            unfocusedTextColor = Color.White
                        )
                    )

                    Text(
                        text = "Rutas típicas de emulación en Android:",
                        color = Color.White,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold
                    )
                    listOf(
                        "Winlator" to "/storage/emulated/0/Winlator/Containers/Drive_C/users/steamuser/AppData/Local/TokyoGhoul/Saved/SaveGames/",
                        "Mobox / Box64" to "/storage/emulated/0/Mobox/drive_c/users/default/AppData/Local/TokyoGhoul/Saved/SaveGames/",
                        "Juego Interno" to "/storage/emulated/0/Games/Tokyo Ghoul re Call to Exist/Savedata/"
                    ).forEach { (label, path) ->
                        Surface(
                            onClick = { saveTargetAndroidPath = path },
                            shape = RoundedCornerShape(8.dp),
                            color = LauncherInactivePill,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Column(modifier = Modifier.padding(8.dp)) {
                                Text(text = label, color = LauncherNeonBlue, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                                Text(text = path, color = Color(0xFF8BA5CC), fontSize = 8.5.sp)
                            }
                        }
                    }
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        viewModel.installSaveDataFromZip(saveZipPathInput, saveTargetAndroidPath)
                        showSaveDataZipDialog = false
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = LauncherNeonBlue)
                ) {
                    Text("Extraer e Instalar en Android", color = Color.Black, fontWeight = FontWeight.Bold, fontSize = 11.sp)
                }
            },
            dismissButton = {
                TextButton(onClick = { showSaveDataZipDialog = false }) {
                    Text("Cancelar", color = QuinqueSilver)
                }
            },
            containerColor = LauncherCardBg
        )
    }

    // MODAL: INSTALL ORIGINAL GAME FILES
    if (showOriginalGameFilesDialog) {
        AlertDialog(
            onDismissRequest = { showOriginalGameFilesDialog = false },
            title = {
                Text("Instalar Archivos Originales del Juego", color = Color.White, fontWeight = FontWeight.Bold)
            },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    Text(
                        text = "Ruta del archivo original comprimido (.zip, .7z, .iso o carpeta):",
                        color = Color(0xFF8BA5CC),
                        fontSize = 11.sp
                    )
                    OutlinedTextField(
                        value = originalGameZipPath,
                        onValueChange = { originalGameZipPath = it },
                        modifier = Modifier.fillMaxWidth(),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = LauncherNeonBlue,
                            unfocusedBorderColor = LauncherCardBorder,
                            focusedTextColor = Color.White,
                            unfocusedTextColor = Color.White
                        )
                    )

                    Text(
                        text = "Carpeta de destino en Android:",
                        color = Color(0xFF8BA5CC),
                        fontSize = 11.sp
                    )
                    OutlinedTextField(
                        value = originalGameTargetFolder,
                        onValueChange = { originalGameTargetFolder = it },
                        modifier = Modifier.fillMaxWidth(),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = LauncherNeonBlue,
                            unfocusedBorderColor = LauncherCardBorder,
                            focusedTextColor = Color.White,
                            unfocusedTextColor = Color.White
                        )
                    )
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        viewModel.installOriginalGamePackage(originalGameZipPath, originalGameTargetFolder)
                        showOriginalGameFilesDialog = false
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = LauncherNeonBlue)
                ) {
                    Text("Descomprimir e Instalar", color = Color.Black, fontWeight = FontWeight.Bold)
                }
            },
            dismissButton = {
                TextButton(onClick = { showOriginalGameFilesDialog = false }) {
                    Text("Cancelar", color = QuinqueSilver)
                }
            },
            containerColor = LauncherCardBg
        )
    }

    // MODAL: CREATE CUSTOM PROFILE
    if (showCreateProfileDialog) {
        AlertDialog(
            onDismissRequest = { showCreateProfileDialog = false },
            title = {
                Text("Crear Nuevo Perfil de Drivers", color = Color.White, fontWeight = FontWeight.Bold)
            },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
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
                        showCreateProfileDialog = false
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = LauncherNeonBlue)
                ) {
                    Text("Crear y Guardar", color = Color.Black, fontWeight = FontWeight.Bold)
                }
            },
            dismissButton = {
                TextButton(onClick = { showCreateProfileDialog = false }) {
                    Text("Cancelar", color = QuinqueSilver)
                }
            },
            containerColor = LauncherCardBg
        )
    }
}

@Composable
fun DriverCardItem(
    driver: GpuDriver,
    isSelected: Boolean,
    onSelect: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .testTag("driver_item_${driver.id}"),
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (isSelected) LauncherActivePill.copy(alpha = 0.4f) else LauncherCardBg
        ),
        border = androidx.compose.foundation.BorderStroke(
            1.5.dp,
            if (isSelected) LauncherNeonBlue else LauncherCardBorder
        )
    ) {
        Column(modifier = Modifier.padding(14.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Icon(
                        imageVector = if (driver.isTurnip) Icons.Default.FlashOn else Icons.Default.Memory,
                        contentDescription = "Driver Icon",
                        tint = if (isSelected) LauncherNeonBlue else QuinqueAccent,
                        modifier = Modifier.size(20.dp)
                    )
                    Column {
                        Text(
                            text = driver.name,
                            color = Color.White,
                            fontSize = 13.5.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = "Versión: ${driver.version} • ${driver.sizeMb}",
                            color = GhoulTextMuted,
                            fontSize = 10.sp
                        )
                    }
                }

                Surface(
                    shape = RoundedCornerShape(6.dp),
                    color = if (isSelected) LauncherActivePill else LauncherInactivePill
                ) {
                    Text(
                        text = if (isSelected) "ACTIVO" else driver.stabilityRating,
                        color = if (isSelected) LauncherNeonBlue else Color.White,
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Black,
                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 3.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = driver.description,
                color = QuinqueSilver,
                fontSize = 11.5.sp
            )

            Spacer(modifier = Modifier.height(6.dp))

            Text(
                text = "Recomendado para: ${driver.recommendedFor}",
                color = Color(0xFF8BA5CC),
                fontSize = 10.sp,
                fontWeight = FontWeight.Medium
            )

            Spacer(modifier = Modifier.height(10.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.End
            ) {
                if (isSelected) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.CheckCircle,
                            contentDescription = "Seleccionado",
                            tint = StatusGreen,
                            modifier = Modifier.size(16.dp)
                        )
                        Text(
                            text = "Driver en uso por el emulador",
                            color = StatusGreen,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                } else {
                    Button(
                        onClick = onSelect,
                        colors = ButtonDefaults.buttonColors(containerColor = LauncherActivePill),
                        shape = RoundedCornerShape(8.dp),
                        modifier = Modifier.height(34.dp)
                    ) {
                        Text(
                            text = "Seleccionar Driver",
                            color = Color.White,
                            fontSize = 11.sp
                        )
                    }
                }
            }
        }
    }
}

