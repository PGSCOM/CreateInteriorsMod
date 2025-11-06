package com.sudolev.interiors.neoforge;

import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.ModContainer;
import net.neoforged.neoforge.common.NeoForge;

import com.sudolev.interiors.CreateInteriors;
import com.sudolev.interiors.content.registry.neoforge.CITabImpl;

@Mod(CreateInteriors.ID)
public class CreateInteriorsForge {
	public CreateInteriorsForge(IEventBus modEventBus, ModContainer modContainer) {
		IEventBus forgeEventBus = NeoForge.EVENT_BUS;

		// Get version and platform name directly to avoid Architectury timing issues
		String version = modContainer.getModInfo().getVersion().toString();
		CreateInteriors.init(version, "NeoForge", new PlatformImpl());
		
		// Register everything after Architectury has initialized
		// CIBlocks must be loaded after @ExpectPlatform injection is complete
		CITabImpl.register(modEventBus);

		// Vincular CreateRegistrate al EventBus de NeoForge (requiere Registrate MC1.21)
		CreateInteriors.REGISTRATE.registerEventListeners(modEventBus);
	}
}
