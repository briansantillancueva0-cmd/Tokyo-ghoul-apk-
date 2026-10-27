package com.example.model

data class GpuDriver(
    val id: String,
    val name: String,
    val version: String,
    val description: String,
    val recommendedFor: String,
    val isInstalled: Boolean,
    val isSelected: Boolean = false,
    val sizeMb: String,
    val stabilityRating: String, // e.g. "98% Estable"
    val isTurnip: Boolean = true
)

data class DxvkVersion(
    val id: String,
    val version: String,
    val title: String,
    val description: String,
    val isAsync: Boolean,
    val isSelected: Boolean = false
)

data class Box64Preset(
    val id: String,
    val name: String,
    val description: String,
    val flags: String
)

object EmulatorDefaults {
    val turnipDrivers = listOf(
        GpuDriver(
            id = "turnip_24_3_mesa",
            name = "Turnip v24.3.0 Mesa Git",
            version = "v24.3.0-dev",
            description = "Compilado optimizado con parches para Unreal Engine 4 y DirectX 11. Rendimiento máximo en shaders pesados.",
            recommendedFor = "Adreno 7xx / 6xx (Snapdragon 8 Gen 1/2/3, 870, 888)",
            isInstalled = true,
            isSelected = true,
            sizeMb = "18.4 MB",
            stabilityRating = "99% Máximo FPS"
        ),
        GpuDriver(
            id = "turnip_24_2_aggressive",
            name = "Turnip v24.2.0 Aggressive Cache",
            version = "v24.2.0",
            description = "Pipeline cache agresivo para evitar tirones (stutter) durante la invocación de Kagune.",
            recommendedFor = "Adreno 6xx (Snapdragon 865, 870, 778G)",
            isInstalled = true,
            isSelected = false,
            sizeMb = "17.9 MB",
            stabilityRating = "96% Muy Estable"
        ),
        GpuDriver(
            id = "turnip_24_1_stable",
            name = "Turnip v24.1.0 LTS",
            version = "v24.1.0",
            description = "Versión estándar de largo soporte con cero fallos gráficos conocidos en texturas oscuras de Tokyo.",
            recommendedFor = "Todos los Adreno compatibles con Vulkan 1.3",
            isInstalled = true,
            isSelected = false,
            sizeMb = "16.8 MB",
            stabilityRating = "95% Estable"
        ),
        GpuDriver(
            id = "mali_panfrost_zink",
            name = "Mesa Panfrost / Zink v24.1",
            version = "v24.1-mali",
            description = "Capa de traducción Vulkan/OpenGL para chips MediaTek Dimensity y Samsung Exynos.",
            recommendedFor = "Mali-G710, Immortalis-G715, Mali-G68",
            isInstalled = false,
            isSelected = false,
            sizeMb = "22.1 MB",
            stabilityRating = "85% Experimental",
            isTurnip = false
        ),
        GpuDriver(
            id = "system_vulkan_stock",
            name = "Driver Stock del Sistema",
            version = "Vulkan 1.3 OEM",
            description = "Controlador gráfico provisto por el fabricante del teléfono (Android Default).",
            recommendedFor = "Dispositivos sin soporte de reemplazo en espacio de usuario",
            isInstalled = true,
            isSelected = false,
            sizeMb = "Sistema",
            stabilityRating = "70% Compatibilidad Básica",
            isTurnip = false
        )
    )

    val dxvkVersions = listOf(
        DxvkVersion(
            id = "dxvk_2_3_1_async",
            version = "DXVK 2.3.1 (Async Shaders)",
            title = "DXVK 2.3.1 Async (Recomendado)",
            description = "Compilación asíncrona de shaders D3D11 eliminando micro-congelamientos en batallas masivas.",
            isAsync = true,
            isSelected = true
        ),
        DxvkVersion(
            id = "dxvk_2_2",
            version = "DXVK 2.2.0 Standard",
            title = "DXVK 2.2.0 Estándar",
            description = "Menor consumo de memoria VRAM en terminales con 6GB u 8GB de RAM.",
            isAsync = false,
            isSelected = false
        ),
        DxvkVersion(
            id = "dxvk_1_10_3",
            version = "DXVK 1.10.3 Legacy",
            title = "DXVK 1.10.3 Legacy",
            description = "Para procesadores Snapdragon antiguos con controladores Adreno 618/620.",
            isAsync = false,
            isSelected = false
        )
    )

    val box64Presets = listOf(
        Box64Preset(
            id = "box64_sd865",
            name = "Snapdragon 865 (Kryo 585 + Adreno 650)",
            description = "Optimización específica para evitar memory leaks y crashes en chips SD865.",
            flags = "BOX64_DYNAREC_BIGBLOCK=1 BOX64_DYNAREC_STRONGMEM=1 BOX64_DYNAREC_FORWARD=128 BOX64_DYNAREC_FASTCALL=1"
        ),
        Box64Preset(
            id = "box64_perf",
            name = "Dynarec Performance (Tokyo Ghoul)",
            description = "Máxima velocidad para x86_64 -> ARM64. Habilita BIGBLOCK=2 y FAST_CALL.",
            flags = "BOX64_DYNAREC_BIGBLOCK=2 BOX64_DYNAREC_FORWARD=256 BOX64_DYNAREC_FASTCALL=1"
        ),
        Box64Preset(
            id = "box64_balanced",
            name = "Dynarec Equilibrado",
            description = "Buen balance entre temperatura del procesador y FPS estables.",
            flags = "BOX64_DYNAREC_BIGBLOCK=1 BOX64_DYNAREC_FORWARD=128"
        ),
        Box64Preset(
            id = "box64_safe",
            name = "Modo Compatibilidad Segura",
            description = "Previene cierres inesperados en cinemáticas del prólogo.",
            flags = "BOX64_DYNAREC_SAFE_MMAP=1 BOX64_DYNAREC_CALL=1"
        )
    )
}
