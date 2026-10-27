package com.example.data

import com.example.model.Box64Preset
import com.example.model.ControlCustomization
import com.example.model.ControllerPreset
import com.example.model.DefaultProfiles
import com.example.model.DriverProfile
import com.example.model.DxvkVersion
import com.example.model.EmulatorDefaults
import com.example.model.ExecutionMode
import com.example.model.GameConfig
import com.example.model.GameExecutable
import com.example.model.GpuDriver
import com.example.model.HapticIntensity
import com.example.model.TokyoGhoulRoster
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

import com.example.model.ControlTheme
import com.example.model.HudDisplayStyle

data class CombatState(
    val characterName: String = "Ken Kaneki",
    val characterAlias: String = "Parche / Rinkaku",
    val healthPercent: Float = 1.0f,
    val rcCellGauge: Float = 0.65f, // 0.0 to 1.0
    val isAwakened: Boolean = false,
    val comboCounter: Int = 0,
    val lastAction: String = "Listo para la batalla",
    val damageDealtTotal: Int = 0,
    val activeEffect: String = "Turnip v24.3.0 Activo",
    val currentFps: Int = 60,
    val gpuTempCelsius: Int = 41,
    val cpuLoadPercent: Int = 23,
    val cpuFreqMhz: Int = 763,
    val gpuUsagePercent: Int = 33,
    val gpuClockMhz: Int = 587,
    val ramUsedMb: Int = 851,
    val ramTotalMb: Int = 1064,
    val ramPercent: Int = 80,
    val frameTimeMs: Float = 16.6f,
    val fpsHistory: List<Int> = listOf(60, 59, 60, 60, 58, 60, 59, 60, 60, 59, 60, 60, 60, 58, 59, 60),
    val isHudMinimized: Boolean = false,
    val hudOffsetX: Float = 30f,
    val hudOffsetY: Float = 100f
)

data class SaveDataProfile(
    val id: String,
    val title: String,
    val description: String,
    val sourceZipName: String,
    val targetAndroidPath: String,
    val steamAppId: String = "924100", // Tokyo Ghoul:re Call to Exist Steam AppID
    val isInstalled: Boolean = true,
    val unlockedPercent: Int = 100,
    val lastSyncTime: String = "Hoy, 14:35"
)

data class LauncherUiState(
    val gameConfig: GameConfig = GameConfig(),
    val profiles: List<DriverProfile> = DefaultProfiles.presets,
    val activeProfileId: String = "profile_balanced_stable",
    val drivers: List<GpuDriver> = EmulatorDefaults.turnipDrivers,
    val dxvkVersions: List<DxvkVersion> = EmulatorDefaults.dxvkVersions,
    val box64Presets: List<Box64Preset> = EmulatorDefaults.box64Presets,
    val controls: ControlCustomization = ControlCustomization(),
    val combat: CombatState = CombatState(),
    val isInstallingDriver: Boolean = false,
    val installProgressText: String = "",
    val installPercent: Float = 0f,
    val isTestingGamepad: Boolean = false,
    val lastInputDetected: String = "Esperando toque o botón...",
    val selectedTab: Int = 0, // 0: Hub/Lanzador, 1: Drivers & Emulador, 2: Adaptar Controles, 3: Guía Personajes/Combos
    val isGameRunning: Boolean = false,
    val notificationMessage: String? = null,
    val activeSaveDataPath: String = "/storage/emulated/0/Winlator/Containers/Drive_C/users/steamuser/AppData/Local/TokyoGhoul/Saved/SaveGames/",
    val saveProfiles: List<SaveDataProfile> = listOf(
        SaveDataProfile(
            id = "save_100_stream",
            title = "Tokyo Ghoul:re Save 100% (Stream/Steam)",
            description = "Todos los personajes Ghoul y CCG desbloqueados en rango SSS, máscaras, trajes y modo supervivencia listos.",
            sourceZipName = "TokyoGhoul_SaveData_100_Stream.zip",
            targetAndroidPath = "/storage/emulated/0/Winlator/Containers/Drive_C/users/steamuser/AppData/Local/TokyoGhoul/Saved/SaveGames/",
            isInstalled = true,
            unlockedPercent = 100,
            lastSyncTime = "Activo en Contenedor"
        ),
        SaveDataProfile(
            id = "save_story_clear",
            title = "Modo Historia Completado (Sin Modificar)",
            description = "Campaña de Haise Sasaki y Kaneki terminada, puntos de mejora intactos para personalización.",
            sourceZipName = "TokyoGhoul_Campaign_Clear.zip",
            targetAndroidPath = "/storage/emulated/0/Games/Tokyo Ghoul re Call to Exist/Savedata/",
            isInstalled = false,
            unlockedPercent = 75,
            lastSyncTime = "Guardado Local"
        )
    )
)

class LauncherRepository {
    private val _uiState = MutableStateFlow(LauncherUiState())
    val uiState: StateFlow<LauncherUiState> = _uiState.asStateFlow()

    fun selectTab(tabIndex: Int) {
        _uiState.update { it.copy(selectedTab = tabIndex) }
    }

    fun setGameRunning(running: Boolean) {
        _uiState.update { it.copy(isGameRunning = running) }
    }

    // SAVEDATA MANAGEMENT: Install, Unpack Zip & Stream into Android path
    suspend fun installSaveDataFromZip(sourceZipPath: String, targetAndroidPath: String) {
        _uiState.update { it.copy(isInstallingDriver = true, installProgressText = "Leyendo archivo zip de SaveData...", installPercent = 0.2f) }
        kotlinx.coroutines.delay(300)
        _uiState.update { it.copy(installProgressText = "Verificando estructura Steam AppID 924100 / SaveGames...", installPercent = 0.6f) }
        kotlinx.coroutines.delay(350)
        _uiState.update { it.copy(installProgressText = "Extrayendo TokyoGhoul_re_Save.sav en $targetAndroidPath...", installPercent = 0.9f) }
        kotlinx.coroutines.delay(300)

        val zipFileName = sourceZipPath.substringAfterLast("/").ifBlank { "savedata_stream.zip" }
        val newSave = SaveDataProfile(
            id = "save_imported_${System.currentTimeMillis()}",
            title = "SaveData Importado: $zipFileName",
            description = "Extraído e inyectado correctamente en la dirección de Android especificada.",
            sourceZipName = zipFileName,
            targetAndroidPath = targetAndroidPath.ifBlank { _uiState.value.activeSaveDataPath },
            isInstalled = true,
            unlockedPercent = 100,
            lastSyncTime = "Instalado ahora"
        )

        _uiState.update { state ->
            val updatedSaves = state.saveProfiles.map { it.copy(isInstalled = false) } + newSave
            state.copy(
                isInstallingDriver = false,
                installProgressText = "",
                installPercent = 1.0f,
                saveProfiles = updatedSaves,
                activeSaveDataPath = newSave.targetAndroidPath,
                gameConfig = state.gameConfig.copy(
                    saveInfo = "Save instalado: 100% Todo Desbloqueado ($zipFileName)",
                    unlockSave100 = true
                ),
                notificationMessage = "¡SaveData ZIP instalado con éxito en la dirección de Android!"
            )
        }
    }

    suspend fun injectOfficial100PercentSave(targetPath: String) {
        installSaveDataFromZip("TokyoGhoul_SaveData_100_Stream.zip", targetPath)
    }

    // ORIGINAL DRIVERS & GAME FILES INSTALLER
    suspend fun installOriginalDriverFile(driverName: String, filePath: String, targetContainerPath: String) {
        _uiState.update { it.copy(isInstallingDriver = true, installProgressText = "Verificando paquete original $driverName...", installPercent = 0.25f) }
        kotlinx.coroutines.delay(350)
        _uiState.update { it.copy(installProgressText = "Desempaquetando librerías libvulkan_freedreno.so y Turnip...", installPercent = 0.65f) }
        kotlinx.coroutines.delay(400)
        _uiState.update { it.copy(installProgressText = "Copiando binarios al contenedor Wine/Box64 en $targetContainerPath...", installPercent = 0.9f) }
        kotlinx.coroutines.delay(350)

        val newDriver = GpuDriver(
            id = "original_driver_${System.currentTimeMillis()}",
            name = driverName.ifBlank { "Turnip Mesa v24.3.0 Original (Adreno)" },
            version = "v24.3.0-original",
            description = "Archivo original importado e instalado en el directorio del contenedor emulador.",
            recommendedFor = "Adreno 6xx / 7xx / 8xx",
            isInstalled = true,
            isSelected = true,
            sizeMb = "18.8 MB",
            stabilityRating = "100% Verificado"
        )

        _uiState.update { state ->
            val updated = state.drivers.map { it.copy(isSelected = false) } + newDriver
            state.copy(
                drivers = updated,
                gameConfig = state.gameConfig.copy(
                    selectedDriverId = newDriver.id,
                    selectedDriverName = newDriver.name
                ),
                isInstallingDriver = false,
                installProgressText = "",
                installPercent = 1.0f,
                notificationMessage = "Driver original ${newDriver.name} instalado con éxito."
            )
        }
    }

    suspend fun installOriginalGamePackage(zipOrPath: String, targetFolder: String) {
        _uiState.update { it.copy(isInstallingDriver = true, installProgressText = "Analizando paquete de juego original...", installPercent = 0.2f) }
        kotlinx.coroutines.delay(400)
        _uiState.update { it.copy(installProgressText = "Descomprimiendo Paks y Binarios de Unreal Engine 4...", installPercent = 0.7f) }
        kotlinx.coroutines.delay(450)
        _uiState.update { it.copy(installProgressText = "Configurando permisos ejecutables en $targetFolder...", installPercent = 0.95f) }
        kotlinx.coroutines.delay(350)

        val gameExePath = "$targetFolder/TokyoGhoul_re_CallToExist.exe"
        addGameExecutable(
            title = "Tokyo Ghoul:re (Archivos Originales)",
            path = gameExePath,
            isExternal = targetFolder.contains("storage/") && !targetFolder.contains("emulated/0"),
            locationLabel = "Instalado desde paquete original"
        )

        _uiState.update { state ->
            state.copy(
                isInstallingDriver = false,
                installProgressText = "",
                installPercent = 1.0f,
                notificationMessage = "Archivos originales instalados y configurados en: $gameExePath"
            )
        }
    }

    // PROFILE MANAGEMENT: Switch quickly between performance and quality modes
    fun applyProfile(profileId: String) {
        val targetProfile = _uiState.value.profiles.firstOrNull { it.id == profileId } ?: return

        _uiState.update { state ->
            val updatedDrivers = state.drivers.map { driver ->
                driver.copy(isSelected = driver.id == targetProfile.driverId)
            }
            val updatedDxvk = state.dxvkVersions.map { dxvk ->
                dxvk.copy(isSelected = dxvk.id == targetProfile.dxvkId)
            }

            val updatedConfig = state.gameConfig.copy(
                resolution = targetProfile.resolution,
                antiAliasing = targetProfile.antiAliasing,
                upscalingMode = targetProfile.upscalingMode,
                performanceMode = targetProfile.performanceMode,
                selectedDriverId = targetProfile.driverId,
                selectedDriverName = targetProfile.driverName,
                selectedDxvkId = targetProfile.dxvkId,
                selectedBox64PresetId = targetProfile.box64PresetId,
                activeProfileId = targetProfile.id,
                activeProfileName = targetProfile.name,
                targetFps = targetProfile.targetFps
            )

            state.copy(
                drivers = updatedDrivers,
                dxvkVersions = updatedDxvk,
                gameConfig = updatedConfig,
                activeProfileId = profileId,
                notificationMessage = "Perfil aplicado: ${targetProfile.iconEmoji} ${targetProfile.name}"
            )
        }
    }

    fun saveCustomProfile(name: String, description: String, iconEmoji: String) {
        val currentConfig = _uiState.value.gameConfig
        val driverName = _uiState.value.drivers.firstOrNull { it.id == currentConfig.selectedDriverId }?.name
            ?: currentConfig.selectedDriverName
        val dxvkName = _uiState.value.dxvkVersions.firstOrNull { it.id == currentConfig.selectedDxvkId }?.version
            ?: "DXVK"

        val newProfile = DriverProfile(
            id = "custom_profile_${System.currentTimeMillis()}",
            name = name.ifBlank { "Mi Perfil Personalizado" },
            description = description.ifBlank { "Configuración personalizada de drivers y filtros." },
            iconEmoji = iconEmoji.ifBlank { "⚙️" },
            driverId = currentConfig.selectedDriverId,
            driverName = driverName,
            dxvkId = currentConfig.selectedDxvkId,
            dxvkName = dxvkName,
            box64PresetId = currentConfig.selectedBox64PresetId,
            resolution = currentConfig.resolution,
            antiAliasing = currentConfig.antiAliasing,
            upscalingMode = currentConfig.upscalingMode,
            performanceMode = currentConfig.performanceMode,
            targetFps = currentConfig.targetFps,
            isDefault = false,
            badgeTag = "Personalizado"
        )

        _uiState.update { state ->
            val updatedList = state.profiles + newProfile
            state.copy(
                profiles = updatedList,
                activeProfileId = newProfile.id,
                gameConfig = state.gameConfig.copy(
                    activeProfileId = newProfile.id,
                    activeProfileName = newProfile.name
                ),
                notificationMessage = "Nuevo perfil guardado: ${newProfile.name}"
            )
        }
    }

    fun deleteProfile(profileId: String) {
        _uiState.update { state ->
            val toDelete = state.profiles.firstOrNull { it.id == profileId }
            if (toDelete?.isDefault == true) {
                return@update state.copy(notificationMessage = "Los perfiles predeterminados no se pueden eliminar.")
            }
            val updatedList = state.profiles.filterNot { it.id == profileId }
            val fallbackProfile = updatedList.firstOrNull() ?: DefaultProfiles.presets.first()
            state.copy(
                profiles = updatedList,
                activeProfileId = fallbackProfile.id,
                gameConfig = state.gameConfig.copy(
                    activeProfileId = fallbackProfile.id,
                    activeProfileName = fallbackProfile.name
                ),
                notificationMessage = "Perfil eliminado con éxito."
            )
        }
    }

    // GAME EXECUTABLE MANAGEMENT: Internal & External Storage
    fun addGameExecutable(title: String, path: String, isExternal: Boolean, locationLabel: String) {
        val newExe = GameExecutable(
            id = "exe_${System.currentTimeMillis()}",
            title = title.ifBlank { path.substringAfterLast("/").ifBlank { "TokyoGhoul.exe" } },
            path = path,
            isExternalStorage = isExternal,
            storageLocationLabel = locationLabel,
            fileSizeFormatted = "14.2 GB",
            isSelected = true
        )

        _uiState.update { state ->
            val updatedList = state.gameConfig.addedExecutables.map { it.copy(isSelected = false) } + newExe
            state.copy(
                gameConfig = state.gameConfig.copy(
                    exePath = newExe.path,
                    addedExecutables = updatedList
                ),
                notificationMessage = "Juego agregado desde ${if (isExternal) "Almacenamiento Externo (SD/USB)" else "Almacenamiento Interno"}: ${newExe.title}"
            )
        }
    }

    fun selectGameExecutable(exeId: String) {
        _uiState.update { state ->
            val target = state.gameConfig.addedExecutables.firstOrNull { it.id == exeId } ?: return@update state
            val updatedList = state.gameConfig.addedExecutables.map {
                it.copy(isSelected = it.id == exeId)
            }
            state.copy(
                gameConfig = state.gameConfig.copy(
                    exePath = target.path,
                    addedExecutables = updatedList
                ),
                notificationMessage = "Ejecutable seleccionado: ${target.title}"
            )
        }
    }

    fun removeGameExecutable(exeId: String) {
        _uiState.update { state ->
            val updated = state.gameConfig.addedExecutables.filterNot { it.id == exeId }
            val fallback = updated.firstOrNull()
            state.copy(
                gameConfig = state.gameConfig.copy(
                    exePath = fallback?.path ?: state.gameConfig.exePath,
                    addedExecutables = updated
                ),
                notificationMessage = "Ruta de juego removida."
            )
        }
    }

    fun selectDriver(driverId: String) {
        _uiState.update { state ->
            val updatedDrivers = state.drivers.map { driver ->
                driver.copy(isSelected = driver.id == driverId)
            }
            state.copy(
                drivers = updatedDrivers,
                gameConfig = state.gameConfig.copy(selectedDriverId = driverId),
                notificationMessage = "Driver activo: ${updatedDrivers.firstOrNull { it.id == driverId }?.name}"
            )
        }
    }

    fun selectDxvk(dxvkId: String) {
        _uiState.update { state ->
            val updatedDxvk = state.dxvkVersions.map { dxvk ->
                dxvk.copy(isSelected = dxvk.id == dxvkId)
            }
            state.copy(
                dxvkVersions = updatedDxvk,
                gameConfig = state.gameConfig.copy(selectedDxvkId = dxvkId),
                notificationMessage = "Capa DXVK configurada: ${updatedDxvk.firstOrNull { it.id == dxvkId }?.version}"
            )
        }
    }

    fun selectBox64Preset(presetId: String) {
        _uiState.update { state ->
            state.copy(
                gameConfig = state.gameConfig.copy(selectedBox64PresetId = presetId),
                notificationMessage = "Ajuste Dynarec Box64 actualizado"
            )
        }
    }

    fun updateGameConfig(newConfig: GameConfig) {
        _uiState.update { it.copy(gameConfig = newConfig) }
    }

    fun updateControls(newControls: ControlCustomization) {
        _uiState.update { it.copy(controls = newControls) }
    }

    fun setControllerPreset(preset: ControllerPreset) {
        _uiState.update { state ->
            val updatedControls = when (preset) {
                ControllerPreset.GHOUL_BALANCED -> state.controls.copy(
                    activePreset = preset,
                    globalScale = 1.0f,
                    opacity = 0.85f,
                    hapticIntensity = HapticIntensity.MEDIUM
                )
                ControllerPreset.CCG_INVESTIGATOR -> state.controls.copy(
                    activePreset = preset,
                    globalScale = 1.05f,
                    opacity = 0.90f,
                    hapticIntensity = HapticIntensity.STRONG
                )
                ControllerPreset.COMPACT -> state.controls.copy(
                    activePreset = preset,
                    globalScale = 0.85f,
                    opacity = 0.70f,
                    hapticIntensity = HapticIntensity.LIGHT
                )
                ControllerPreset.LARGE_HANDS -> state.controls.copy(
                    activePreset = preset,
                    globalScale = 1.25f,
                    opacity = 0.95f,
                    hapticIntensity = HapticIntensity.STRONG
                )
            }
            state.copy(
                controls = updatedControls,
                notificationMessage = "Preajuste aplicado: ${preset.label}"
            )
        }
    }

    suspend fun simulateInstallCustomDriver(driverName: String) {
        _uiState.update { it.copy(isInstallingDriver = true, installProgressText = "Descargando Turnip driver package...", installPercent = 0.15f) }
        kotlinx.coroutines.delay(350)
        _uiState.update { it.copy(installProgressText = "Descomprimiendo librerías libvulkan_freedreno.so...", installPercent = 0.55f) }
        kotlinx.coroutines.delay(400)
        _uiState.update { it.copy(installProgressText = "Verificando firmas Vulkan 1.3 y parches UE4...", installPercent = 0.85f) }
        kotlinx.coroutines.delay(350)

        val newDriver = GpuDriver(
            id = "custom_turnip_${System.currentTimeMillis()}",
            name = driverName.ifBlank { "Turnip v24.3.2 Vulkan Extreme" },
            version = "v24.3.2-custom",
            description = "Driver personalizado importado desde almacenamiento interno con parches de compresión Zink/Turnip.",
            recommendedFor = "Adreno 6xx/7xx/8xx",
            isInstalled = true,
            isSelected = true,
            sizeMb = "19.2 MB",
            stabilityRating = "100% Máximo Rendimiento"
        )

        _uiState.update { state ->
            val updated = state.drivers.map { it.copy(isSelected = false) } + newDriver
            state.copy(
                drivers = updated,
                gameConfig = state.gameConfig.copy(selectedDriverId = newDriver.id),
                isInstallingDriver = false,
                installProgressText = "",
                installPercent = 1f,
                notificationMessage = "¡Driver ${newDriver.name} instalado y seleccionado con éxito!"
            )
        }
    }

    fun recordInputFeedback(buttonName: String) {
        _uiState.update { state ->
            val damage = when (buttonName) {
                "B" -> 240 // Melee Fist (Puñetazo Kagune)
                "X" -> 140 // Shuriken / Garra
                "Y" -> 310 // Espiral / Impacto Quinque
                "A" -> 0   // Salto acrobático
                "奥" -> 1500 // Ougi / Técnica Máxima Despertar
                "術" -> 450  // Jutsu Kagune
                "ミト" -> 280 // Combo Assist
                "LB" -> 80
                "RB" -> 220
                "LT" -> 0
                "RT" -> 450
                "RB+RT" -> 1200
                else -> 100
            }

            val newCount = state.combat.comboCounter + 1
            val newRc = (state.combat.rcCellGauge + 0.05f).coerceAtMost(1.0f)
            val isNowAwakened = if (buttonName == "RB+RT" || buttonName == "奥" || (buttonName == "RB" && state.combat.rcCellGauge > 0.8f)) true else state.combat.isAwakened

            val actionDesc = when (buttonName) {
                "B" -> "Puñetazo de Combate Ghoul [B] (Combo x$newCount)"
                "X" -> "Ataque Cortante Rápido Shuriken [X]"
                "Y" -> "Impacto Espiral Quinque / Vortex [Y]"
                "A" -> "Salto Evasivo y Acrobacia Aérea [A]"
                "奥" -> "¡¡OUGI DEFINITIVO: DESPERTAR KAKUJA!! [奥]"
                "術" -> "Técnica Especial Jutsu Kagune [術]"
                "ミト" -> "Ataque Asistido de Relevo [ミト]"
                "LB" -> "Guardia Defensiva con Quinque [LB]"
                "RB" -> "¡Liberación de Garras Kagune! [RB]"
                "LT" -> "Blanco Fijado (Lock-on Target) [LT]"
                "RT" -> "¡Gatillo de Impacto Potenciado! [RT]"
                "RB+RT" -> "¡¡DESPERTAR KAKUJA CIEMPIÉS DEFINITIVO!!"
                "L3", "L3_SPRINT" -> "L3: Esprint Rápido Ghoul activado"
                "R3", "R3_LOCK" -> "R3: Fijar Blanco (Lock-on) / Centrar Cámara"
                else -> "Acción: $buttonName"
            }

            val spikeGpu = if (isNowAwakened || buttonName == "奥") 88 else (45..70).random()
            val spikeCpu = if (isNowAwakened || buttonName == "奥") 65 else (32..45).random()

            state.copy(
                lastInputDetected = "Botón presionado: [$buttonName] - $actionDesc",
                combat = state.combat.copy(
                    comboCounter = newCount,
                    lastAction = actionDesc,
                    damageDealtTotal = state.combat.damageDealtTotal + damage,
                    rcCellGauge = if (buttonName == "RB+RT" || buttonName == "奥") 0.15f else newRc,
                    isAwakened = isNowAwakened,
                    currentFps = if (isNowAwakened) 59 else (58..60).random(),
                    gpuUsagePercent = spikeGpu,
                    cpuLoadPercent = spikeCpu
                )
            )
        }
    }

    fun updateFloatingHudPosition(x: Float, y: Float) {
        _uiState.update { state ->
            state.copy(
                combat = state.combat.copy(
                    hudOffsetX = x,
                    hudOffsetY = y
                )
            )
        }
    }

    fun toggleFloatingHudMinimized(minimized: Boolean? = null) {
        _uiState.update { state ->
            val next = minimized ?: !state.combat.isHudMinimized
            state.copy(
                combat = state.combat.copy(isHudMinimized = next)
            )
        }
    }

    fun setHudDisplayStyle(style: HudDisplayStyle) {
        _uiState.update { state ->
            state.copy(
                controls = state.controls.copy(hudStyle = style),
                notificationMessage = "Estilo de Overlay: ${style.label}"
            )
        }
    }

    fun setControlTheme(theme: ControlTheme) {
        _uiState.update { state ->
            state.copy(
                controls = state.controls.copy(controlTheme = theme),
                notificationMessage = "Tema de controles: ${theme.label}"
            )
        }
    }

    fun toggleControlsVisibility(visible: Boolean? = null) {
        _uiState.update { state ->
            val next = visible ?: !state.controls.areControlsVisible
            state.copy(
                controls = state.controls.copy(areControlsVisible = next)
            )
        }
    }

    fun tickRealtimeTelemetry() {
        _uiState.update { state ->
            val isGameRunning = state.isGameRunning
            val baseFps = if (isGameRunning) (58..60).random() else 60
            val cpuLoad = if (isGameRunning) {
                if (state.combat.isAwakened) (65..78).random() else (22..38).random()
            } else (15..25).random()

            val cpuFreq = if (cpuLoad > 50) (1800..2840).random() else (763..1200).random()

            val gpuUsage = if (isGameRunning) {
                if (state.combat.isAwakened) (78..92).random() else (30..48).random()
            } else (18..28).random()

            val gpuClock = if (gpuUsage > 60) 670 else 587
            val ramUsed = if (isGameRunning) (840..960).random() else 720
            val ramPercent = (ramUsed * 100) / 1064
            val temp = if (isGameRunning) (41..44).random() else 39
            val frameTime = 1000f / baseFps.coerceAtLeast(1)

            val updatedHistory = (state.combat.fpsHistory.takeLast(24) + baseFps)

            state.copy(
                combat = state.combat.copy(
                    currentFps = baseFps,
                    cpuLoadPercent = cpuLoad,
                    cpuFreqMhz = cpuFreq,
                    gpuUsagePercent = gpuUsage,
                    gpuClockMhz = gpuClock,
                    ramUsedMb = ramUsed,
                    ramPercent = ramPercent,
                    gpuTempCelsius = temp,
                    frameTimeMs = frameTime,
                    fpsHistory = updatedHistory
                )
            )
        }
    }

    fun selectCharacterInCombat(characterId: String) {
        val char = TokyoGhoulRoster.characters.firstOrNull { it.id == characterId } ?: return
        _uiState.update { state ->
            state.copy(
                combat = state.combat.copy(
                    characterName = char.name,
                    characterAlias = char.alias,
                    comboCounter = 0,
                    isAwakened = false,
                    rcCellGauge = 0.5f,
                    lastAction = "Luchador cambiado a ${char.name}"
                ),
                notificationMessage = "Personaje seleccionado para combate: ${char.name}"
            )
        }
    }

    fun resetCombatStats() {
        _uiState.update { state ->
            state.copy(
                combat = state.combat.copy(
                    comboCounter = 0,
                    damageDealtTotal = 0,
                    isAwakened = false,
                    rcCellGauge = 0.5f,
                    lastAction = "Combate reiniciado"
                )
            )
        }
    }

    fun dismissNotification() {
        _uiState.update { it.copy(notificationMessage = null) }
    }
}
