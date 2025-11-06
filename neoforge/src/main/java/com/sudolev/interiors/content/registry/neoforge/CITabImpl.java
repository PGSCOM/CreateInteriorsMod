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

public class CITabImpl {
	private static DeferredRegister<CreativeModeTab> REGISTER;
	private static DeferredHolder<CreativeModeTab, CreativeModeTab> TAB;

	public static void ensureInitialized() {
		if (REGISTER != null) return;
		
		REGISTER = DeferredRegister.create(Registries.CREATIVE_MODE_TAB, CreateInteriors.ID);
		
		// Lazy import de CIBlocks para evitar carga prematura
		TAB = REGISTER.register("main", () -> {
			return CreativeModeTab.builder()
				.title(Component.literal(CreateInteriors.NAME))
				.icon(() -> {
					try {
						return com.sudolev.interiors.content.registry.CIBlocks.CHAIRS.get(DyeColor.RED).asStack(1);
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
				.build();
		});
	}

	public static void register(IEventBus modEventBus) {
		ensureInitialized();
		REGISTER.register(modEventBus);
	}

	public static ResourceKey<CreativeModeTab> getKey() {
		return ResourceKey.create(Registries.CREATIVE_MODE_TAB, CreateInteriors.asResource("main"));
	}

	public static CreativeModeTab get() {
		ensureInitialized();
		return TAB.get();
	}
	
	public static DeferredHolder<CreativeModeTab, CreativeModeTab> getTab() {
		ensureInitialized();
		return TAB;
	}
}
