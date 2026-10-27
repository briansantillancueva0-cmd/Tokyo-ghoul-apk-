package com.example

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import com.example.data.LauncherRepository
import com.example.model.ControlTheme
import com.example.model.HudDisplayStyle
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [36])
class ExampleRobolectricTest {

  @Test
  fun `read string from context`() {
    val context = ApplicationProvider.getApplicationContext<Context>()
    val appName = context.getString(R.string.app_name)
    assertEquals("TG re Launcher", appName)
  }

  @Test
  fun `test adapted controls input feedback and ougi awakening`() {
    val repository = LauncherRepository()
    // Test prominent Fist Melee B button
    repository.recordInputFeedback("B")
    assertEquals(1, repository.uiState.value.combat.comboCounter)
    assertTrue(repository.uiState.value.combat.damageDealtTotal > 0)
    assertTrue(repository.uiState.value.lastInputDetected.contains("Puñetazo"))

    // Test Ougi Kakuja Awakening skill [奥]
    repository.recordInputFeedback("奥")
    assertEquals(2, repository.uiState.value.combat.comboCounter)
    assertTrue(repository.uiState.value.combat.isAwakened)
    assertTrue(repository.uiState.value.combat.gpuUsagePercent >= 80)

    // Test L3 (Sprint) and R3 (Lock-on / Camera)
    repository.recordInputFeedback("L3")
    assertTrue(repository.uiState.value.lastInputDetected.contains("L3: Esprint"))

    repository.recordInputFeedback("R3")
    assertTrue(repository.uiState.value.lastInputDetected.contains("R3: Fijar Blanco"))
  }

  @Test
  fun `test floating hud telemetry ticks and minimization`() {
    val repository = LauncherRepository()
    // Verify initial telemetry values
    val initial = repository.uiState.value.combat
    assertEquals(23, initial.cpuLoadPercent)
    assertEquals(763, initial.cpuFreqMhz)
    assertEquals(33, initial.gpuUsagePercent)
    assertEquals(851, initial.ramUsedMb)
    assertEquals(80, initial.ramPercent)

    // Tick telemetry
    repository.tickRealtimeTelemetry()
    val updated = repository.uiState.value.combat
    assertTrue(updated.currentFps in 55..65)
    assertTrue(updated.fpsHistory.isNotEmpty())

    // Move HUD
    repository.updateFloatingHudPosition(120f, 250f)
    assertEquals(120f, repository.uiState.value.combat.hudOffsetX)
    assertEquals(250f, repository.uiState.value.combat.hudOffsetY)

    // Toggle minimize
    repository.toggleFloatingHudMinimized(true)
    assertTrue(repository.uiState.value.combat.isHudMinimized)

    // Change style
    repository.setHudDisplayStyle(HudDisplayStyle.RADIAL_GAUGES)
    assertEquals(HudDisplayStyle.RADIAL_GAUGES, repository.uiState.value.controls.hudStyle)

    // Change control theme
    repository.setControlTheme(ControlTheme.ANIME_ACTION_COMBAT)
    assertEquals(ControlTheme.ANIME_ACTION_COMBAT, repository.uiState.value.controls.controlTheme)
  }
}

