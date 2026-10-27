package com.example

import com.example.data.LauncherRepository
import com.example.model.ControllerPreset
import com.example.model.HapticIntensity
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Test

class ExampleUnitTest {
    @Test
    fun testLauncherInitialState() {
        val repo = LauncherRepository()
        val state = repo.uiState.value

        assertNotNull(state.gameConfig)
        assertEquals("TOKYO GHOUL", state.gameConfig.gameTitle)
        assertEquals("720p", state.gameConfig.resolution)
        assertTrue(state.drivers.isNotEmpty())
        assertTrue(state.drivers.any { it.isSelected })
        assertTrue(state.profiles.isNotEmpty())
    }

    @Test
    fun testProfileSwitching() {
        val repo = LauncherRepository()
        // Switch to Extreme Performance 60 FPS profile
        repo.applyProfile("profile_perf_extreme")
        val state = repo.uiState.value

        assertEquals("profile_perf_extreme", state.activeProfileId)
        assertEquals("540p", state.gameConfig.resolution)
        assertEquals("turnip_24_3_mesa", state.gameConfig.selectedDriverId)
        assertEquals("dxvk_2_3_1_async", state.gameConfig.selectedDxvkId)
        assertTrue(state.gameConfig.performanceMode)

        // Switch to 1080p Ultra Quality profile
        repo.applyProfile("profile_quality_1080p")
        val qualityState = repo.uiState.value
        assertEquals("profile_quality_1080p", qualityState.activeProfileId)
        assertEquals("1080p", qualityState.gameConfig.resolution)
        assertEquals("CAS", qualityState.gameConfig.upscalingMode)
    }

    @Test
    fun testAddInternalAndExternalExecutable() {
        val repo = LauncherRepository()
        // Add external SD card game
        repo.addGameExecutable(
            title = "TokyoGhoul_SDCard.exe",
            path = "/storage/0000-0000/Games/TokyoGhoul_re_CallToExist.exe",
            isExternal = true,
            locationLabel = "Tarjeta SD Externa"
        )
        val state = repo.uiState.value

        assertEquals("/storage/0000-0000/Games/TokyoGhoul_re_CallToExist.exe", state.gameConfig.exePath)
        assertTrue(state.gameConfig.addedExecutables.any { it.isExternalStorage && it.title == "TokyoGhoul_SDCard.exe" })
    }

    @Test
    fun testSaveDataZipInstallation() = runBlocking {
        val repo = LauncherRepository()
        repo.installSaveDataFromZip(
            sourceZipPath = "/storage/emulated/0/Download/stream_save_100.zip",
            targetAndroidPath = "/storage/emulated/0/Winlator/Containers/Drive_C/SavedGames/"
        )
        val state = repo.uiState.value

        assertTrue(state.gameConfig.unlockSave100)
        assertTrue(state.saveProfiles.any { it.sourceZipName == "stream_save_100.zip" })
    }

    @Test
    fun testDriverSelection() {
        val repo = LauncherRepository()
        repo.selectDriver("turnip_24_2_aggressive")
        val state = repo.uiState.value

        val selected = state.drivers.firstOrNull { it.isSelected }
        assertEquals("turnip_24_2_aggressive", selected?.id)
        assertEquals("turnip_24_2_aggressive", state.gameConfig.selectedDriverId)
    }

    @Test
    fun testControllerPresetApplication() {
        val repo = LauncherRepository()
        repo.setControllerPreset(ControllerPreset.CCG_INVESTIGATOR)
        val state = repo.uiState.value

        assertEquals(ControllerPreset.CCG_INVESTIGATOR, state.controls.activePreset)
        assertEquals(HapticIntensity.STRONG, state.controls.hapticIntensity)
    }

    @Test
    fun testCombatComboFeedback() {
        val repo = LauncherRepository()
        repo.recordInputFeedback("X")
        val state = repo.uiState.value

        assertEquals(1, state.combat.comboCounter)
        assertEquals(140, state.combat.damageDealtTotal)
    }
}
