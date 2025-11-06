package com.sudolev.interiors.content.registry.neoforge;

import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.ItemStack;
import java.util.Objects;

import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.DeferredHolder;

import com.sudolev.interiors.CreateInteriors;
import com.sudolev.interiors.content.registry.CIBlocks;

public class CITabImpl {
	private static final DeferredRegister<CreativeModeTab> REGISTER = DeferredRegister.create(Registries.CREATIVE_MODE_TAB, CreateInteriors.ID);

	public static final DeferredHolder<CreativeModeTab, CreativeModeTab> TAB = REGISTER.register("main",
		() -> CreativeModeTab.builder()
			.title(Component.literal(CreateInteriors.NAME))
			.icon(() -> {
				try {
					return CIBlocks.CHAIRS.get(DyeColor.RED).asStack(1);
				} catch (Exception e) {
					return ItemStack.EMPTY;
				}
			})
			.displayItems((parameters, output) -> CreateInteriors.REGISTRATE
				.getAll(Registries.BLOCK).stream()
				.map(entry -> {
					try {
						return entry.get().asItem();
					} catch (Exception ex) {
						return null;
					}
				})
				.filter(Objects::nonNull)
				.forEach(output::accept))
			.build());

	public static void register(IEventBus modEventBus) {
		REGISTER.register(modEventBus);
	}

	public static ResourceKey<CreativeModeTab> getKey() {
		// TODO: Fix for MC 1.21.1 - getBackgroundLocation() was removed
		// Creative tabs no longer have background locations in the same way
		return ResourceKey.create(Registries.CREATIVE_MODE_TAB, CreateInteriors.asResource("main"));
	}

	public static CreativeModeTab get() {
		return TAB.get();
	}
}
