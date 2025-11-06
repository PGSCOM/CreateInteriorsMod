architectury {
	common {
		for(p in rootProject.subprojects) {
			if(p != project) {
				this@common.add(p.name)
			}
		}
	}
}

dependencies {
	// We depend on fabric loader here to use the fabric @Environment annotations and get the mixin dependencies
	// Do NOT use other classes from fabric loader
	modImplementation("net.fabricmc:fabric-loader:${"fabric_loader_version"()}")
	// Compile against Create (NeoForge artifact) in common para 1.21.1
	// beware of differences across platforms!
	// dependencies must also be pulled in to minimize problems, from remapping issues to compile errors.
	// All dependencies except Flywheel and Registrate are NOT safe to use!
	// Flywheel and Registrate must also be used carefully due to differences.
		// Usar el classifier slim para evitar integraciones opcionales.
		// Deshabilitar transitivas para no arrastrar FTB/Vanillin/CC que no están en nuestros repos.
		modCompileOnly("com.simibubi.create:create-${"minecraft_version"()}:${"create_neoforge_version"()}:slim") {
			isTransitive = false
		}

	// Añadir explícitamente Catnip y Registrate para cubrir los tipos usados en 'common'
	// Usamos versión dinámica para Catnip dentro de 1.21.1; sólo compileOnly
	modCompileOnly("net.createmod.catnip:Catnip-NeoForge-${"minecraft_version"()}:+") { isTransitive = false }
	modCompileOnly("com.tterrag.registrate:Registrate:${"registrate_version"()}") { isTransitive = false }

	// API de NeoForge necesaria porque ciertas clases de Create (p.ej., SeatEntity) referencian tipos de NeoForge
	// Compilación únicamente; no se empaqueta.
	modCompileOnly("net.neoforged:neoforge:${"neoforge_version"()}")

	// required for proper remapping and compiling
	modCompileOnly("net.fabricmc.fabric-api:fabric-api:${"fabric_api_version"()}+${"minecraft_version"()}")

	// JSR-305 annotations (needed for @ParametersAreNonnullByDefault)
	compileOnly("com.google.code.findbugs:jsr305:3.0.2")

	annotationProcessor(implementation("io.github.llamalad7:mixinextras-common:${"mixin_extras_version"()}")!!)
}

tasks.processResources {
	// must be part of primary mod to be findable
	exclude("resourcepacks/")

	// don't add development or to-do files into built jar
	exclude("**/*.bbmodel", "**/*.lnk", "**/*.xcf", "**/*.md", "**/*.txt", "**/*.blend", "**/*.blend1")
}

sourceSets.main {
	resources { // include generated resources in resources
		srcDir("src/generated/resources")
		exclude(".cache/**")
		exclude("assets/create/**")
	}
}

operator fun String.invoke(): String {
	return rootProject.ext[this] as? String
		?: throw IllegalStateException("Property $this is not defined")
}