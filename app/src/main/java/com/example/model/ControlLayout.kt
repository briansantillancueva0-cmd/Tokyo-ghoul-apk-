package com.example.model

enum class HapticIntensity(val label: String, val durationMs: Long) {
    OFF("Desactivado", 0L),
    LIGHT("Ligero (5ms)", 8L),
    MEDIUM("Medio (18ms)", 22L),
    STRONG("Fuerte - Impacto Ghoul (35ms)", 40L)
}

enum class ControllerPreset(val label: String, val description: String) {
    GHOUL_BALANCED("Kagune Balanceado", "Distribución táctil óptima para combate rápido de Ghouls"),
    CCG_INVESTIGATOR("Investigador CCG", "Énfasis en guardias con Quinque, disparo a distancia y apuntado"),
    COMPACT("Modo Compacto", "Botones más pequeños para no tapar los bordes de la pantalla"),
    LARGE_HANDS("Dedos Grandes / Ergonomía", "Botones espaciados y de mayor diámetro para evitar toques falsos")
}

enum class ControlTheme(val label: String, val description: String) {
    ANIME_ACTION_COMBAT("Tokyo Ghoul / Anime Combate", "Botones circulares plateados, puño B, shuriken X, espiral Y, salto A y hexágonos de técnicas"),
    CLASSIC_CONSOLE("Consola Clásica (Xbox / DualShock)", "Botones circulares estándar con letras coloreadas X, Y, B, A"),
    MINIMAL_STEALTH("Modo Mínimo Transparente", "Siluetas de bajo contraste para máxima visibilidad de pantalla")
}

enum class HudDisplayStyle(val label: String, val shortName: String) {
    RADIAL_GAUGES("Medidores Circulares (Foto)", "Medidores"),
    COMPACT_PILL("Pastilla Compacta", "Pastilla"),
    DETAILED_STATS("Panel Completo Hardware", "Detallado"),
    FPS_GRAPH("Gráfico FPS en Vivo", "Gráfico")
}

data class ButtonPosition(
    val offsetXPercent: Float, // 0f to 1f relative to screen width/bounds
    val offsetYPercent: Float,
    val sizeDp: Float = 56f
)

data class ControlCustomization(
    val globalScale: Float = 1.0f,
    val opacity: Float = 0.88f,
    val hapticIntensity: HapticIntensity = HapticIntensity.MEDIUM,
    val activePreset: ControllerPreset = ControllerPreset.GHOUL_BALANCED,
    val controlTheme: ControlTheme = ControlTheme.ANIME_ACTION_COMBAT,
    val hudStyle: HudDisplayStyle = HudDisplayStyle.RADIAL_GAUGES,
    val showStickDeadzoneVisualizer: Boolean = true,
    val enableKaguneComboMacro: Boolean = true,
    val showPerformanceOverlay: Boolean = true,
    val showHexSkills: Boolean = true,
    val showUtilityDock: Boolean = true,
    val areControlsVisible: Boolean = true
)
