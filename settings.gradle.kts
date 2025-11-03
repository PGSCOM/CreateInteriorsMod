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
include("fabric")
include("neoforge")

// no colon because gradle
rootProject.name = "Create Interiors"