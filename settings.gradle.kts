pluginManagement {
	repositories {
		maven { url = uri("https://maven.fabricmc.net/") }
		maven { url = uri("https://maven.architectury.dev/") }
		maven { url = uri("https://maven.neoforged.net/releases") }
		maven { url = uri("https://maven.quiltmc.org/repository/release") }
		gradlePluginPortal()
		mavenCentral()
	}
}

include("common")

// Determinar la versión de MC para decidir qué subproyectos incluir
val mcVersion = providers.gradleProperty("minecraft_version").orNull

// Incluir el submódulo Fabric solo cuando el objetivo sea 1.20.1
if (mcVersion == "1.20.1") {
	include("fabric")
	logger.lifecycle("Incluyendo subproyecto 'fabric' para Minecraft ${mcVersion}")
} else {
	logger.lifecycle("Omitiendo subproyecto 'fabric' para Minecraft ${mcVersion} (objetivo NeoForge)")
}

// Incluir el submódulo NeoForge solo cuando la versión de Minecraft lo soporte (NeoForge empieza en 1.20.2+)
if (mcVersion != null && mcVersion != "1.20.1") {
    include("neoforge")
    logger.lifecycle("Incluyendo subproyecto 'neoforge' para Minecraft ${mcVersion}")
} else {
    logger.lifecycle("Omitiendo subproyecto 'neoforge' para Minecraft ${mcVersion} (solo Fabric)")
}// no colon because gradle
rootProject.name = "Create Interiors"