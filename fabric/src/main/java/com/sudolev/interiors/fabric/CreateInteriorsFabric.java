package com.sudolev.interiors.fabric;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.loader.api.FabricLoader;
import com.sudolev.interiors.CreateInteriors;

public class CreateInteriorsFabric implements ModInitializer {

	@Override
	public void onInitialize() {
		// Get version and platform name directly to avoid Architectury timing issues
		String version = FabricLoader.getInstance()
			.getModContainer(CreateInteriors.ID)
			.map(container -> container.getMetadata().getVersion().getFriendlyString())
			.orElse("UNKNOWN");
		CreateInteriors.init(version, "Fabric", new PlatformImpl());
		
		CreateInteriors.REGISTRATE.register();
	}
}
