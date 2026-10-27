package com.example.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.LauncherRepository
import com.example.data.LauncherUiState
import com.example.model.ControlTheme
import com.example.model.ControllerPreset
import com.example.model.ExecutionMode
import com.example.model.GameConfig
import com.example.model.HapticIntensity
import com.example.model.HudDisplayStyle
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch

class LauncherViewModel(
    private val repository: LauncherRepository = LauncherRepository()
) : ViewModel() {

    val uiState: StateFlow<LauncherUiState> = repository.uiState

    init {
        // Real-time telemetry background ticker loop
        viewModelScope.launch {
            while (isActive) {
                delay(650)
                repository.tickRealtimeTelemetry()
            }
        }
    }

    fun selectTab(tabIndex: Int) {
        repository.selectTab(tabIndex)
    }

    fun launchGame() {
        repository.setGameRunning(true)
    }

    fun exitGame() {
        repository.setGameRunning(false)
    }

    // PROFILE MANAGEMENT
    fun applyProfile(profileId: String) {
        repository.applyProfile(profileId)
    }

    fun saveCustomProfile(name: String, description: String, iconEmoji: String) {
        repository.saveCustomProfile(name, description, iconEmoji)
    }

    fun deleteProfile(profileId: String) {
        repository.deleteProfile(profileId)
    }

    // SAVEDATA ZIP & STREAM INSTALLATION WITH ANDROID PATH
    fun installSaveDataFromZip(sourceZipPath: String, targetAndroidPath: String) {
        viewModelScope.launch {
            repository.installSaveDataFromZip(sourceZipPath, targetAndroidPath)
        }
    }

    fun injectSteam100PercentSave(targetPath: String) {
        viewModelScope.launch {
            repository.injectOfficial100PercentSave(targetPath)
        }
    }

    // ORIGINAL DRIVERS & GAME FILES INSTALLATION
    fun installOriginalDriverFile(driverName: String, filePath: String, targetContainer: String) {
        viewModelScope.launch {
            repository.installOriginalDriverFile(driverName, filePath, targetContainer)
        }
    }

    fun installOriginalGamePackage(zipOrPath: String, targetFolder: String) {
        viewModelScope.launch {
            repository.installOriginalGamePackage(zipOrPath, targetFolder)
        }
    }

    // EXECUTABLE & STORAGE MANAGEMENT (Internal & External Storage)
    fun addGameExecutable(title: String, path: String, isExternal: Boolean, locationLabel: String) {
        repository.addGameExecutable(title, path, isExternal, locationLabel)
    }

    fun selectGameExecutable(exeId: String) {
        repository.selectGameExecutable(exeId)
    }

    fun removeGameExecutable(exeId: String) {
        repository.removeGameExecutable(exeId)
    }

    fun selectDriver(driverId: String) {
        repository.selectDriver(driverId)
        val selected = repository.uiState.value.drivers.firstOrNull { it.id == driverId }
        if (selected != null) {
            val current = repository.uiState.value.gameConfig
            repository.updateGameConfig(current.copy(selectedDriverName = "${selected.name} (${selected.version})"))
        }
    }

    fun selectDxvk(dxvkId: String) {
        repository.selectDxvk(dxvkId)
    }

    fun selectBox64(presetId: String) {
        repository.selectBox64Preset(presetId)
    }

    fun toggleOfflineMode(enabled: Boolean) {
        val current = repository.uiState.value.gameConfig
        repository.updateGameConfig(current.copy(isOfflineModeActive = enabled))
    }

    fun applySnapdragon865Preset() {
        applyProfile("profile_snapdragon_865")
    }

    fun updateConfig(config: GameConfig) {
        repository.updateGameConfig(config)
    }

    // RESOLUTION CHANGER
    fun setResolution(res: String) {
        val current = repository.uiState.value.gameConfig
        repository.updateGameConfig(current.copy(resolution = res))
    }

    fun setScreenAspect(aspect: String) {
        val current = repository.uiState.value.gameConfig
        repository.updateGameConfig(current.copy(screenAspect = aspect))
    }

    fun setAntiAliasing(aa: String) {
        val current = repository.uiState.value.gameConfig
        repository.updateGameConfig(current.copy(antiAliasing = aa))
    }

    fun setUpscaling(mode: String) {
        val current = repository.uiState.value.gameConfig
        repository.updateGameConfig(current.copy(upscalingMode = mode))
    }

    fun togglePerformanceMode(enabled: Boolean) {
        val current = repository.uiState.value.gameConfig
        repository.updateGameConfig(current.copy(performanceMode = enabled))
    }

    fun toggleFpsPanel(enabled: Boolean) {
        val current = repository.uiState.value.gameConfig
        repository.updateGameConfig(current.copy(showFpsPanel = enabled))
        val currentControls = repository.uiState.value.controls
        repository.updateControls(currentControls.copy(showPerformanceOverlay = enabled))
    }

    fun toggleSave100(enabled: Boolean) {
        val current = repository.uiState.value.gameConfig
        repository.updateGameConfig(current.copy(unlockSave100 = enabled))
    }

    fun importSaveGame() {
        val current = repository.uiState.value.gameConfig
        repository.updateGameConfig(
            current.copy(
                saveInfo = "Save atual: 142 KB • Importado com Sucesso!",
                unlockSave100 = true
            )
        )
    }

    fun cycleDriver() {
        val drivers = repository.uiState.value.drivers
        val currentDriverId = repository.uiState.value.gameConfig.selectedDriverId
        val currentIndex = drivers.indexOfFirst { it.id == currentDriverId }
        val nextIndex = (currentIndex + 1) % drivers.size
        selectDriver(drivers[nextIndex].id)
    }

    fun restoreDefaults() {
        repository.updateGameConfig(GameConfig())
        repository.setControllerPreset(ControllerPreset.GHOUL_BALANCED)
    }

    fun updateExePath(newPath: String) {
        val current = repository.uiState.value.gameConfig
        repository.updateGameConfig(current.copy(exePath = newPath))
    }

    fun updateExecutionMode(mode: ExecutionMode) {
        val current = repository.uiState.value.gameConfig
        repository.updateGameConfig(current.copy(executionMode = mode))
    }

    fun installCustomDriver(driverName: String) {
        viewModelScope.launch {
            repository.simulateInstallCustomDriver(driverName)
        }
    }

    fun setControllerPreset(preset: ControllerPreset) {
        repository.setControllerPreset(preset)
    }

    fun updateGlobalScale(scale: Float) {
        val current = repository.uiState.value.controls
        repository.updateControls(current.copy(globalScale = scale))
    }

    fun updateOpacity(opacity: Float) {
        val current = repository.uiState.value.controls
        repository.updateControls(current.copy(opacity = opacity))
    }

    fun updateHapticIntensity(intensity: HapticIntensity) {
        val current = repository.uiState.value.controls
        repository.updateControls(current.copy(hapticIntensity = intensity))
    }

    fun toggleMacro(enabled: Boolean) {
        val current = repository.uiState.value.controls
        repository.updateControls(current.copy(enableKaguneComboMacro = enabled))
    }

    fun togglePerfHud(enabled: Boolean) {
        val current = repository.uiState.value.controls
        repository.updateControls(current.copy(showPerformanceOverlay = enabled))
    }

    fun handleVirtualButtonPress(buttonName: String) {
        repository.recordInputFeedback(buttonName)
    }

    fun selectCharacter(charId: String) {
        repository.selectCharacterInCombat(charId)
    }

    fun resetCombatStats() {
        repository.resetCombatStats()
    }

    fun updateFloatingHudPosition(x: Float, y: Float) {
        repository.updateFloatingHudPosition(x, y)
    }

    fun toggleFloatingHudMinimized(minimized: Boolean? = null) {
        repository.toggleFloatingHudMinimized(minimized)
    }

    fun setHudDisplayStyle(style: HudDisplayStyle) {
        repository.setHudDisplayStyle(style)
    }

    fun setControlTheme(theme: ControlTheme) {
        repository.setControlTheme(theme)
    }

    fun toggleControlsVisibility(visible: Boolean? = null) {
        repository.toggleControlsVisibility(visible)
    }

    fun toggleHexSkills(enabled: Boolean) {
        val current = repository.uiState.value.controls
        repository.updateControls(current.copy(showHexSkills = enabled))
    }

    fun toggleUtilityDock(enabled: Boolean) {
        val current = repository.uiState.value.controls
        repository.updateControls(current.copy(showUtilityDock = enabled))
    }

    fun dismissNotification() {
        repository.dismissNotification()
    }
}
