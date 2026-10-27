package com.example.model

data class DriverProfile(
    val id: String,
    val name: String,
    val description: String,
    val iconEmoji: String = "⚡",
    val driverId: String,
    val driverName: String,
    val dxvkId: String,
    val dxvkName: String,
    val box64PresetId: String,
    val resolution: String, // "360p", "540p", "720p", "1080p"
    val antiAliasing: String, // "Off", "FXAA", "FXAA+"
    val upscalingMode: String, // "Normal", "FSR", "CAS"
    val performanceMode: Boolean,
    val targetFps: Int = 60,
    val isDefault: Boolean = false,
    val badgeTag: String = "Recomendado"
)

object DefaultProfiles {
    val presets = listOf(
        DriverProfile(
            id = "profile_snapdragon_865",
            name = "Snapdragon 865 (Adreno 650)",
            description = "Configuración perfecta para el chip SD865. Pipeline cache agresivo para evitar caídas en Tokyo Ghoul UE4.",
            iconEmoji = "🔥",
            driverId = "turnip_24_2_aggressive",
            driverName = "Turnip v24.2.0 (Adreno 650)",
            dxvkId = "dxvk_2_3_1_async",
            dxvkName = "DXVK 2.3.1 Async",
            box64PresetId = "box64_sd865",
            resolution = "540p",
            antiAliasing = "FXAA",
            upscalingMode = "FSR",
            performanceMode = true,
            targetFps = 60,
            isDefault = true,
            badgeTag = "Adreno 650"
        ),
        DriverProfile(
            id = "profile_perf_extreme",
            name = "Rendimiento Extremo (60 FPS)",
            description = "Prioriza tasa de cuadros fluida en Unreal Engine 4 con compilación asíncrona de shaders y FSR.",
            iconEmoji = "⚡",
            driverId = "turnip_24_3_mesa",
            driverName = "Turnip v24.3.0 Mesa Git",
            dxvkId = "dxvk_2_3_1_async",
            dxvkName = "DXVK 2.3.1 Async",
            box64PresetId = "box64_perf",
            resolution = "540p",
            antiAliasing = "FXAA",
            upscalingMode = "FSR",
            performanceMode = true,
            targetFps = 60,
            isDefault = true,
            badgeTag = "Máximo FPS"
        ),
        DriverProfile(
            id = "profile_quality_1080p",
            name = "Calidad Visual 1080p Ultra",
            description = "Máxima nitidez en texturas del Kagune, reflejos nocturnos de Tokio y filtrado FXAA+ en alta definición.",
            iconEmoji = "💎",
            driverId = "turnip_24_3_mesa",
            driverName = "Turnip v24.3.0 Mesa Git",
            dxvkId = "dxvk_2_3_1_async",
            dxvkName = "DXVK 2.3.1 Async",
            box64PresetId = "box64_balanced",
            resolution = "1080p",
            antiAliasing = "FXAA+",
            upscalingMode = "CAS",
            performanceMode = false,
            targetFps = 45,
            isDefault = true,
            badgeTag = "Alta Definición"
        ),
        DriverProfile(
            id = "profile_balanced_stable",
            name = "Equilibrado Tokyo Ghoul",
            description = "Excelente balance térmico y gráfico. Ideal para sesiones continuas de combate sin tirones.",
            iconEmoji = "⚖️",
            driverId = "turnip_24_2_aggressive",
            driverName = "Turnip v24.2.0 Aggressive Cache",
            dxvkId = "dxvk_2_3_1_async",
            dxvkName = "DXVK 2.3.1 Async",
            box64PresetId = "box64_perf",
            resolution = "720p",
            antiAliasing = "FXAA",
            upscalingMode = "FSR",
            performanceMode = true,
            targetFps = 60,
            isDefault = true,
            badgeTag = "Estable 720p"
        ),
        DriverProfile(
            id = "profile_battery_saver",
            name = "Ahorro de Batería & Térmico",
            description = "Reduce la carga de GPU/CPU manteniendo la temperatura baja y alargando la duración de batería.",
            iconEmoji = "🔋",
            driverId = "turnip_24_1_stable",
            driverName = "Turnip v24.1.0 LTS",
            dxvkId = "dxvk_2_2",
            dxvkName = "DXVK 2.2.0 Standard",
            box64PresetId = "box64_safe",
            resolution = "360p",
            antiAliasing = "Off",
            upscalingMode = "Normal",
            performanceMode = true,
            targetFps = 30,
            isDefault = true,
            badgeTag = "Baja Temp"
        )
    )
}
