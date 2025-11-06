package com.sudolev.interiors.content.registry.neoforge;

import com.sudolev.interiors.CreateInteriors;

import com.tterrag.registrate.providers.DataGenContext;
import com.tterrag.registrate.providers.RegistrateBlockstateProvider;

import org.jetbrains.annotations.ApiStatus;

// TODO: Fix imports for NeoForge 1.21.1 datagen API
// import net.neoforged.neoforge.client.model.generators.BlockStateProvider;
// import net.neoforged.neoforge.client.model.generators.ConfiguredModel;
// import net.neoforged.neoforge.client.model.generators.ModelFile;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;

@SuppressWarnings("unchecked")
public class CIBlocksImpl {
	// TODO: Fix BlockStateProvider for NeoForge 1.21.1 - datagen classes moved
	/* @ApiStatus.Internal
	public static <ModelFile> ModelFile customChairModelFile(Object o, String parent, String name,
															 ResourceLocation top, ResourceLocation side,
															 ResourceLocation sideTop, ResourceLocation sideFront) {
		BlockStateProvider p = (BlockStateProvider) o;
		return (ModelFile) p.models()
			.withExistingParent(name,
				p.modLoc(parent))
			.texture("top", top)
			.texture("side_top", sideTop)
			.texture("side_front", sideFront)
			.texture("side", side);
	}

	@ApiStatus.Internal
	public static <ModelFile> ModelFile getExistingModelFile(Object o, String name) {
		BlockStateProvider p = (BlockStateProvider) o;
		return (ModelFile) p.models().getExistingFile(p.modLoc(name));
	}

	@ApiStatus.Internal
	public static <ModelFile> ModelFile createModelFileWithExistingParent(Object p, String parent, String name) {
		BlockStateProvider provider = (BlockStateProvider) p;
		return (ModelFile) provider.models().withExistingParent(name, provider.modLoc(parent));
	} */

	// Temporary stub implementations to allow compilation
	@ApiStatus.Internal
	public static <ModelFile> ModelFile customChairModelFile(Object o, String parent, String name,
															 ResourceLocation top, ResourceLocation side,
															 ResourceLocation sideTop, ResourceLocation sideFront) {
		return null; // TODO: Implement with correct NeoForge 1.21.1 API
	}

	@ApiStatus.Internal
	public static <ModelFile> ModelFile getExistingModelFile(Object o, String name) {
		return null; // TODO: Implement with correct NeoForge 1.21.1 API
	}

	@ApiStatus.Internal
	public static <ModelFile> ModelFile createModelFileWithExistingParent(Object p, String parent, String name) {
		return null; // TODO: Implement with correct NeoForge 1.21.1 API
	}

	@ApiStatus.Internal
	public static <CM> CM modelWithRotation(Object model, int rotation) {
		// TODO: Fix ConfiguredModel for NeoForge 1.21.1
		return null; // Stub implementation
		/* return (CM) ConfiguredModel.builder()
			.modelFile((ModelFile) model)
			.rotationY(rotation)
			.build(); */
	}

	@ApiStatus.Internal
	public static void setupCreativeTab() {
		CITabImpl.ensureInitialized();
		CreateInteriors.REGISTRATE.setCreativeTab(CITabImpl.getTab());
	}

	@ApiStatus.Internal
	public static void simpleBlock(DataGenContext<Block, ?> c, RegistrateBlockstateProvider p, ResourceLocation texture) {
		// TODO: Fix for NeoForge 1.21.1 datagen
		// p.simpleBlock(c.get(), p.models().cubeAll(c.getName(), texture));
	}
}
