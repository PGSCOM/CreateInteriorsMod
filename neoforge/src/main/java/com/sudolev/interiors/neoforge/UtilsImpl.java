package com.sudolev.interiors.neoforge;

import java.util.List;

import net.minecraft.data.tags.TagsProvider.TagAppender;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.Entity;
import net.neoforged.fml.ModList;
import net.neoforged.fml.loading.FMLLoader;
import net.neoforged.neoforgespi.language.IModInfo;

import com.sudolev.interiors.CreateInteriors;
import com.tterrag.registrate.providers.RegistrateTagsProvider;

public abstract class UtilsImpl {
	public static String getVersion(String modid) {
		String versionString = "UNKNOWN";

		try {
			var modFile = ModList.get().getModFileById(modid);
			if (modFile == null) {
				CreateInteriors.LOGGER.warn("Mod file not found for ID: " + modid);
				return versionString;
			}
			
			List<IModInfo> infoList = modFile.getMods();
			if (infoList.size() > 1) {
				CreateInteriors.LOGGER.error("Multiple mods for ID: " + modid);
			}
			for (IModInfo info : infoList) {
				if (info.getModId().equals(modid)) {
					versionString = info.getVersion().toString();
					break;
				}
			}
		} catch (Exception e) {
			CreateInteriors.LOGGER.error("Failed to get version for mod: " + modid, e);
		}
		return versionString;
	}

	public static boolean isDevEnv() {
		return !FMLLoader.isProduction();
	}

	public static String platformName() {
		return "NeoForge";
	}

	public static CompoundTag getCustomData(Entity entity) {
		return entity.getPersistentData();
	}

	public static <T> TagAppender<T> tagAppender(RegistrateTagsProvider<T> prov, TagKey<T> tag) {
		return prov.addTag(tag);
	}
}
