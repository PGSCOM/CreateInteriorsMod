package com.sudolev.interiors.neoforge;

import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.ModContainer;

import com.sudolev.interiors.CreateInteriors;
import com.sudolev.interiors.content.registry.neoforge.CITabImpl;

@Mod(CreateInteriors.ID)
public class CreateInteriorsForge {
	public CreateInteriorsForge(IEventBus modEventBus, ModContainer modContainer) {
		// CRÍTICO: Vincular CreateRegistrate al EventBus ANTES de cualquier otra operación
		CreateInteriors.REGISTRATE.registerEventListeners(modEventBus);

		// Ahora sí inicializar el mod (esto carga CIBlocks, CIEntities, etc.)
		String version = modContainer.getModInfo().getVersion().toString();
		CreateInteriors.init(version, "NeoForge", new PlatformImpl());
		
		// Registrar creative tab DESPUÉS de que todo esté inicializado
		CITabImpl.register(modEventBus);
	}
}
