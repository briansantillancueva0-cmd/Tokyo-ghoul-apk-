package com.example.model

enum class ExecutionMode(val title: String, val subtitle: String) {
    EMULATOR_CONTAINER("Contenedor Emulador Box64/Wine", "Ejecución local nativa con traducción x86_64 a ARM64"),
    SIMULATION_COMBAT_TEST("Modo Pruebas y Combate Kagune", "Simulador en tiempo real para adaptar controles, haptics y combos"),
    STREAMING_BRIDGE("Moonlight / Sunshine PC Bridge", "Conexión directa por red local a tu PC de escritorio")
}

data class GameExecutable(
    val id: String,
    val title: String,
    val path: String,
    val isExternalStorage: Boolean = false,
    val storageLocationLabel: String = "Almacenamiento Interno",
    val fileSizeFormatted: String = "14.2 GB",
    val isSelected: Boolean = false
)

data class GameConfig(
    val exePath: String = "/storage/emulated/0/Games/Tokyo Ghoul re Call to Exist/TokyoGhoul_re_CallToExist.exe",
    val gameTitle: String = "TOKYO GHOUL",
    val gameSubtitle: String = ":re CALL TO EXIST",
    val characterTagline: String = "東京喰種:re • O GHOUL DE UM OLHO SÓ / SEGADOR NEGRO",
    val resolution: String = "720p", // 360p, 540p, 720p, 1080p
    val screenAspect: String = "16:9", // 16:9, Esticar
    val antiAliasing: String = "FXAA", // Off, FXAA, FXAA+
    val upscalingMode: String = "FSR", // Normal, FSR, CAS
    val performanceMode: Boolean = true,
    val showFpsPanel: Boolean = true,
    val unlockSave100: Boolean = true,
    val saveInfo: String = "Save atual: 142 KB • 27/10/2026 14:10",
    val selectedDriverName: String = "Turnip Gen8 v36 (Vulkan 1.4.359)",
    val controllerProfileName: String = "Xbox 360 - PS3",
    val launchArguments: String = "-dx11 -USEALLAVAILABLECORES -high -novid -NoVSync",
    val audioDriver: String = "PulseAudio (40ms Low Latency)",
    val winePrefix: String = "Wine 9.0 WOW64 Windows 10 x64",
    val targetFps: Int = 60,
    val selectedDriverId: String = "turnip_24_3_mesa",
    val selectedDxvkId: String = "dxvk_2_3_1_async",
    val selectedBox64PresetId: String = "box64_perf",
    val activeProfileId: String = "profile_snapdragon_865",
    val activeProfileName: String = "Snapdragon 865 (Adreno 650)",
    val isOfflineModeActive: Boolean = true,
    val steamOfflineBypass: Boolean = true,
    val detectedSoC: String = "Snapdragon 865 (Adreno 650)",
    val isSnapdragon865Optimized: Boolean = true,
    val executionMode: ExecutionMode = ExecutionMode.SIMULATION_COMBAT_TEST,
    val hapticFeedbackEnabled: Boolean = true,
    val addedExecutables: List<GameExecutable> = listOf(
        GameExecutable(
            id = "exe_default_tg",
            title = "TokyoGhoul_re_CallToExist.exe",
            path = "/storage/emulated/0/Games/Tokyo Ghoul re Call to Exist/TokyoGhoul_re_CallToExist.exe",
            isExternalStorage = false,
            storageLocationLabel = "Interno (/storage/emulated/0/Games)",
            fileSizeFormatted = "14.2 GB",
            isSelected = true
        ),
        GameExecutable(
            id = "exe_winlator_tg",
            title = "TokyoGhoul_re (Carpeta Winlator)",
            path = "/storage/emulated/0/Winlator/Containers/Drive_C/Games/TokyoGhoul/TokyoGhoul_re_CallToExist.exe",
            isExternalStorage = false,
            storageLocationLabel = "Contenedor Winlator C:",
            fileSizeFormatted = "14.2 GB",
            isSelected = false
        ),
        GameExecutable(
            id = "exe_sd_card_tg",
            title = "Tokyo Ghoul (Tarjeta SD Externa)",
            path = "/storage/0000-0000/JuegosPC/TokyoGhoul_re/TokyoGhoul_re_CallToExist.exe",
            isExternalStorage = true,
            storageLocationLabel = "SD Card Externa (/storage/MicroSD)",
            fileSizeFormatted = "14.2 GB",
            isSelected = false
        )
    )
)
