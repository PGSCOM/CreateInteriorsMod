architectury.neoForge()

loom {
	runs.configureEach {
		vmArg("-Dmixin.debug.export=true")
		vmArg("-Dmixin.env.remapRefMap=true")
		vmArg("-Dmixin.env.refMapRemappingFile=${projectDir}/build/createSrgToMcp/output.srg")
	}

	// Configurar mixins usando la DSL moderna de Loom
	mixin {
		// Requerido por Loom al configurar el AP de Mixin de forma clásica
		useLegacyMixinAp.set(true)
		add(sourceSets.main.get(), "interiors-common.mixins.json")
	}
}

dependencies {
	neoForge("net.neoforged:neoforge:${"neoforge_version"()}")

	// Create para NeoForge en 1.21.1
	modImplementation("com.simibubi.create:create-${"minecraft_version"()}:${"create_neoforge_version"()}:slim") { isTransitive = false }
	modImplementation("io.github.llamalad7:mixinextras-neoforge:${"mixin_extras_version"()}")
	
	// Registrate necesario para las clases de registro
	modCompileOnly("com.tterrag.registrate:Registrate:${"registrate_version"()}") { isTransitive = false }
	
	// JSR-305 annotations
	compileOnly("com.google.code.findbugs:jsr305:3.0.2")
}

operator fun String.invoke() = rootProject.ext[this] as? String ?: error("No property \"$this\"")