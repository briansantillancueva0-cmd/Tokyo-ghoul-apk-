package com.example.model

data class ComboMove(
    val title: String,
    val buttonSequence: List<String>,
    val description: String,
    val damageMultiplier: String
)

data class GhoulCharacter(
    val id: String,
    val name: String,
    val alias: String,
    val faction: String,
    val type: String, // Rinkaku, Ukaku, Koukaku, Bikaku, Quinque
    val description: String,
    val awakeningForm: String,
    val moves: List<ComboMove>
)

object TokyoGhoulRoster {
    val characters = listOf(
        GhoulCharacter(
            id = "kaneki",
            name = "Ken Kaneki",
            alias = "Parche / Segador Negro",
            faction = "Ghoul / Aogiri / Goat",
            type = "Rinkaku (4 Tentáculos Carmesí)",
            description = "Protagonista capaz de invocar tentáculos Rinkaku con regeneración acelerada y modo ciempiés Kakuja.",
            awakeningForm = "Kakuja Despertado: Múltiples extremidades y aumento del 150% de daño",
            moves = listOf(
                ComboMove(
                    title = "Danza Carmesí Rinkaku",
                    buttonSequence = listOf("X", "X", "X"),
                    description = "Cadena rápida de tres estocadas horizontales con los tentáculos de la espalda.",
                    damageMultiplier = "1.2x Daño Base"
                ),
                ComboMove(
                    title = "Empalamiento Sísmico",
                    buttonSequence = listOf("X", "Y"),
                    description = "Golpe frontal seguido de elevación con los tentáculos que lanza al enemigo por los aires.",
                    damageMultiplier = "1.8x Daño Crítico"
                ),
                ComboMove(
                    title = "Barrido Ghoul Devastador",
                    buttonSequence = listOf("Y", "Y"),
                    description = "Rotación de 360 grados barriendo a todos los soldados CCG en el área.",
                    damageMultiplier = "2.1x Área"
                ),
                ComboMove(
                    title = "Despertar: Furia de Ciempiés",
                    buttonSequence = listOf("RB", "+", "RT"),
                    description = "Gasto del 100% de la barra de Células RC para desatar el modo Kakuja sin límites.",
                    damageMultiplier = "3.5x Modo Berserk"
                )
            )
        ),
        GhoulCharacter(
            id = "sasaki",
            name = "Haise Sasaki",
            alias = "Mentor del Escuadrón Quinx",
            faction = "CCG - Inspector de Primera Clase",
            type = "Quinque Yukimura 1/3 + Rinkaku Híbrido",
            description = "Líder Quinx que combina el entrenamiento militar CCG con el poder latente de Kaneki.",
            awakeningForm = "Liberación del Ojo Rojo: Mezcla espada Quinque y tentáculo simultáneo",
            moves = listOf(
                ComboMove(
                    title = "Corte Preciso Yukimura",
                    buttonSequence = listOf("X", "X", "Y"),
                    description = "Dos tajos de espada seguidos de un corte ascendente de Quinque.",
                    damageMultiplier = "1.4x Daño Cortante"
                ),
                ComboMove(
                    title = "Bloqueo y Contragolpe Táctico",
                    buttonSequence = listOf("LB", "->", "X"),
                    description = "Parada perfecta con vaina Quinque y estocada inmediata que rompe la postura enemiga.",
                    damageMultiplier = "2.0x Parada Exitosa"
                ),
                ComboMove(
                    title = "Asalto Conjunto Quinx",
                    buttonSequence = listOf("RT"),
                    description = "Invocación de habilidad especial con ráfaga perforante frontal.",
                    damageMultiplier = "2.4x Especial"
                )
            )
        ),
        GhoulCharacter(
            id = "touka",
            name = "Touka Kirishima",
            alias = "Rabbit",
            faction = "Ghoul / Anteiku",
            type = "Ukaku (Alas de Fuego y Cristales)",
            description = "Especialista en movilidad aérea y proyectiles de alta velocidad de cristal endurecido.",
            awakeningForm = "Alas Ígneas Desplegadas: Ráfaga de proyectiles de fuego continuo",
            moves = listOf(
                ComboMove(
                    title = "Lluvia de Cristales Ukaku",
                    buttonSequence = listOf("A", "->", "Y"),
                    description = "Salto hacia atrás proyectando decenas de agujas de cristal de largo alcance.",
                    damageMultiplier = "1.5x Rango Distante"
                ),
                ComboMove(
                    title = "Embestida Aérea Veloz",
                    buttonSequence = listOf("B", "->", "X"),
                    description = "Impulso supersónico hacia adelante atravesando la defensa del oponente.",
                    damageMultiplier = "1.7x Penetración"
                ),
                ComboMove(
                    title = "Ciclón Carmesí de Penacho",
                    buttonSequence = listOf("RB", "+", "RT"),
                    description = "Erupción de fuego RC que aniquila grupos de asalto en vuelo.",
                    damageMultiplier = "3.0x Definitiva"
                )
            )
        ),
        GhoulCharacter(
            id = "arima",
            name = "Kisho Arima",
            alias = "El Dios de la Muerte del CCG",
            faction = "CCG - Clase Especial",
            type = "Quinques: Narukami (Rayo) & IXA (Taladro/Escudo)",
            description = "El combatiente invicto de la Comisión de Contramedidas Ghoul con armas duales de grado SSS.",
            awakeningForm = "Sobrecarga de Condensador RC: Rayos continuos y escudo impenetrable",
            moves = listOf(
                ComboMove(
                    title = "Descarga de Truenos Narukami",
                    buttonSequence = listOf("RT"),
                    description = "Dispara una andanada de relámpagos teledirigidos que electrocutan a los Ghouls.",
                    damageMultiplier = "2.2x Eléctrico"
                ),
                ComboMove(
                    title = "Barrera Impenetrable IXA",
                    buttonSequence = listOf("LB"),
                    description = "Despliega una muralla protectora con IXA en modo defensivo que absorbe todo daño.",
                    damageMultiplier = "100% Absorción"
                ),
                ComboMove(
                    title = "Taladro Espiral IXA",
                    buttonSequence = listOf("Y", "Y", "Y"),
                    description = "Transforma la lanza en un barreno giratorio que pulveriza cualquier armadura Kakuja.",
                    damageMultiplier = "2.8x Perforante"
                )
            )
        )
    )
}
