package com.sudolev.interiors.content.registry.neoforge;

import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.DyeColor;

import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredRegister;

import com.sudolev.interiors.CreateInteriors;
import com.sudolev.interiors.content.registry.CIBlocks;

import java.util.function.Supplier;

public class CITabImpl {
	private static final DeferredRegister<CreativeModeTab> REGISTER = DeferredRegister.create(Registries.CREATIVE_MODE_TAB, CreateInteriors.ID);

	public static final Supplier<CreativeModeTab> TAB = REGISTER.register("main",
		() -> CreativeModeTab.builder()
			.title(Component.literal(CreateInteriors.NAME))
			.icon(() -> CIBlocks.CHAIRS.get(DyeColor.RED).asStack(1))
			.displayItems((parameters, output) -> CreateInteriors.REGISTRATE
				.getAll(Registries.BLOCK).stream()
				.map(entry -> entry.get().asItem())
				.forEach(output::accept))
			.build());

	public static void register(IEventBus modEventBus) {
		REGISTER.register(modEventBus);
	}

	public static ResourceKey<CreativeModeTab> getKey() {
		return TAB.get().getBackgroundLocation() != null ? 
			ResourceKey.create(Registries.CREATIVE_MODE_TAB, TAB.get().getBackgroundLocation()) : 
			null;
	}

	public static CreativeModeTab get() {
		return TAB.get();
	}
}
