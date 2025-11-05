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

		CreateInteriors.init();
		CITabImpl.register(modEventBus);
		// TODO: Fix for NeoForge 1.21.1 - registerEventListeners expects Forge IEventBus
		// Need to verify if Create's Registrate has NeoForge-compatible version
		// CreateInteriors.REGISTRATE.registerEventListeners(modEventBus);
	}
}
