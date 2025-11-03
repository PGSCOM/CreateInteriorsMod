architectury.neoForge()

loom {
	runs.configureEach {
		vmArg("-Dmixin.debug.export=true")
		vmArg("-Dmixin.env.remapRefMap=true")
		vmArg("-Dmixin.env.refMapRemappingFile=${projectDir}/build/createSrgToMcp/output.srg")
	}

	neoForge {
		mixinConfig("interiors-common.mixins.json")
		mixinConfig("interiors.mixins.json")
	}
}

dependencies {
	neoForge("net.neoforged:neoforge:${"neoforge_version"()}")

	// Create and its dependencies
	modImplementation("com.simibubi.create:create-neoforge-${"minecraft_version"()}:${"create_neoforge_version"()}:slim") { isTransitive = false }
	modImplementation("net.createmod.ponder:Ponder-NeoForge-${"minecraft_version"()}:${"ponder_version"()}")
	modImplementation("com.tterrag.registrate:Registrate:${"registrate_version"()}")
	modImplementation("dev.engine-room.flywheel:flywheel-neoforge-api-${"minecraft_version"()}:${"flywheel_version"()}")
	modImplementation("io.github.llamalad7:mixinextras-neoforge:${"mixin_extras_version"()}")
	modRuntimeOnly("dev.engine-room.flywheel:flywheel-neoforge-${"minecraft_version"()}:${"flywheel_version"()}")
}

operator fun String.invoke() = rootProject.ext[this] as? String ?: error("No property \"$this\"")