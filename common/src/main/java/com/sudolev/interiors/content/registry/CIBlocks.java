package com.sudolev.interiors.content.registry;


import com.simibubi.create.AllBlocks;
import com.simibubi.create.AllTags.AllItemTags;
import com.simibubi.create.Create;
import com.simibubi.create.content.contraptions.actors.seat.SeatInteractionBehaviour;
import com.simibubi.create.content.contraptions.actors.seat.SeatMovementBehaviour;
import com.simibubi.create.foundation.block.DyedBlockList;
import com.simibubi.create.foundation.data.AssetLookup;
import com.simibubi.create.foundation.data.SharedProperties;
import com.simibubi.create.foundation.item.ItemDescription;
import com.simibubi.create.foundation.utility.DyeHelper;

import net.minecraft.advancements.Criterion;
import net.minecraft.advancements.critereon.InventoryChangeTrigger;
import net.minecraft.advancements.critereon.ItemPredicate;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.data.recipes.ShapelessRecipeBuilder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;

import com.sudolev.interiors.CreateInteriors;
import com.sudolev.interiors.content.block.CushionBlock;
import com.sudolev.interiors.content.block.WallMountedTable;
import com.sudolev.interiors.content.block.chair.BigChairBlock;
import com.sudolev.interiors.content.block.chair.BigSeatMovementBehaviour;
import com.sudolev.interiors.content.block.chair.ChairBlock;
import com.sudolev.interiors.content.block.chair.DirectionalSeatBlock;
import com.sudolev.interiors.content.block.chair.FloorChairBlock;
import com.tterrag.registrate.providers.DataGenContext;
import com.tterrag.registrate.providers.RegistrateBlockstateProvider;
import com.tterrag.registrate.providers.RegistrateRecipeProvider;
import com.tterrag.registrate.util.entry.BlockEntry;

import org.jetbrains.annotations.ApiStatus;

import dev.architectury.injectables.annotations.ExpectPlatform;

import static com.simibubi.create.api.behaviour.interaction.MovingInteractionBehaviour.interactionBehaviour;
import static com.simibubi.create.api.behaviour.movement.MovementBehaviour.movementBehaviour;
import static com.simibubi.create.foundation.block.ProperWaterloggedBlock.WATERLOGGED;
import static com.simibubi.create.foundation.data.TagGen.axeOnly;
import static com.sudolev.interiors.CreateInteriors.REGISTRATE;

// TODO: is AllDisplayBehaviours.assignDataBehaviour necessary to replace?
@SuppressWarnings("unused")
public final class CIBlocks {

	// Moved setupCreativeTab() call from static block to register() method
	// to avoid Architectury @ExpectPlatform timing issues

	// Helper method for recipes in MC 1.21.1 - creates a Criterion from an ItemLike
	private static Criterion<?> has(ItemLike item) {
		return InventoryChangeTrigger.TriggerInstance.hasItems(item);
	}

	// Helper method for recipes in MC 1.21.1 - creates a Criterion from a TagKey
	private static Criterion<?> has(net.minecraft.tags.TagKey<net.minecraft.world.item.Item> tag) {
		return InventoryChangeTrigger.TriggerInstance.hasItems(ItemPredicate.Builder.item().of(tag).build());
	}

	// Helper method to cast RegistrateRecipeProvider to RecipeOutput for MC 1.21.1
	private static net.minecraft.data.recipes.RecipeOutput recipeOutput(RegistrateRecipeProvider provider) {
		return (net.minecraft.data.recipes.RecipeOutput) provider;
	}

	public static final BlockEntry<Block> SEATWOOD_PLANKS = REGISTRATE.block("seatwood_planks", Block::new)
		.initialProperties(SharedProperties::wooden)
		.properties(p -> p.mapColor(DyeColor.ORANGE))
		.transform(axeOnly())
		.tag(BlockTags.PLANKS)
		.item()
		.tag(ItemTags.PLANKS)
		.build()
		.register();

	public static final BlockEntry<WallMountedTable> WALL_MOUNTED_TABLE = REGISTRATE.block("wall_mounted_table", WallMountedTable::new)
		.initialProperties(SharedProperties::wooden)
		.properties(p -> p.mapColor(DyeColor.ORANGE))
		.transform(axeOnly())
		.blockstate((ctx, provider) -> {
			provider.getVariantBuilder(ctx.get())
				.forAllStatesExcept(state -> {
					int rotation = facing(state);
					return modelWithRotation(getExistingModelFile(provider, "block/wall_mounted_table"), rotation);
				}, WATERLOGGED);
		})
		.simpleItem()
		.register();

	public static final DyedBlockList<FloorChairBlock> FLOOR_CHAIRS = new DyedBlockList<>(color -> {
		String colorName = color.getSerializedName();
		return REGISTRATE.block(colorName + "_floor_chair", p -> new FloorChairBlock(p, color))
			.initialProperties(SharedProperties::wooden)
			.properties(p -> p.mapColor(color))
			.transform(axeOnly())
			.initialProperties(SharedProperties::wooden)
			.properties(p -> p.mapColor(color))
			.transform(axeOnly())
			.blockstate((ctx, provider) -> {
				provider.getVariantBuilder(ctx.get())
					.forAllStatesExcept(state -> {
						String armrest = state.getValue(ChairBlock.ARMRESTS).getSerializedName();
						String cropped_state = state.getValue(ChairBlock.CROPPED_BACK) ? "_cropped" : "";

						int rotation = facing(state);

						ResourceLocation top = Create.asResource("block/seat/top_" + colorName);
						ResourceLocation side = Create.asResource("block/seat/side_" + colorName);
						ResourceLocation sideTop = CreateInteriors.asResource("block/chair/side_top_" + colorName);

						Object model = customChairModelFile(provider, "block/floor_chair/" + armrest + cropped_state,
							"block/floor_chair/" + colorName + "_floor_chair_" + armrest + cropped_state,
							top, side, sideTop, side);
						return modelWithRotation(model, rotation);
					}, WATERLOGGED);
			})
			.recipe((ctx, provider) -> {
				ShapelessRecipeBuilder.shapeless(RecipeCategory.DECORATIONS, ctx.get())
					.requires(ItemTags.PLANKS)
					.requires(ItemTags.WOOL)
					.unlockedBy("has_planks", has(ItemTags.PLANKS))
					.save(recipeOutput(provider));
			})
			.onRegister(movementBehaviour(new SeatMovementBehaviour()))
			.onRegister(interactionBehaviour(new SeatInteractionBehaviour()))
//			.onRegister(assignDataBehaviour(new EntityNameDisplaySource(), "entity_name"))
			.onRegisterAfter(Registries.ITEM, v -> ItemDescription.useKey(v, "block.interiors.chair"))
			.tag(CITags.Blocks.FLOOR_CHAIRS)
			.item().tag(CITags.Items.FLOOR_CHAIRS)
			.model(AssetLookup.customBlockItemModel("floor_chair",
				colorName + "_floor_chair_" + ChairBlock.ArmrestConfiguration.DEFAULT.getSerializedName()))
			.build().register();
	});

	public static final DyedBlockList<BigChairBlock> CHAIRS = new DyedBlockList<>(color -> {
		String colorName = color.getSerializedName();

		return REGISTRATE.block(colorName + "_chair", p -> new BigChairBlock(p, color))
			.initialProperties(SharedProperties::wooden)
			.properties(p -> p.mapColor(color))
			.transform(axeOnly())
			.blockstate((ctx, provider) -> {
				provider.getVariantBuilder(ctx.get())
					.forAllStatesExcept(state -> {
						String armrest = state.getValue(ChairBlock.ARMRESTS).getSerializedName();
						String cropped_state = state.getValue(ChairBlock.CROPPED_BACK) ? "_cropped" : "";

						int rotation = facing(state);

						ResourceLocation top = Create.asResource("block/seat/top_" + colorName);
						ResourceLocation side = Create.asResource("block/seat/side_" + colorName);
						ResourceLocation sideTop = CreateInteriors.asResource("block/chair/side_top_" + colorName);

						Object model = customChairModelFile(provider, "block/chair/" + armrest + cropped_state,
							"block/chair/" + colorName + "_chair_" + armrest + cropped_state,
							top, side, sideTop, side);
						return modelWithRotation(model, rotation);
					}, WATERLOGGED);
			})
			.recipe((ctx, provider) -> {
				ShapelessRecipeBuilder.shapeless(RecipeCategory.DECORATIONS, ctx.get())
					.requires(ItemTags.PLANKS)
					.requires(ItemTags.WOOL)
					.unlockedBy("has_planks", has(ItemTags.PLANKS))
					.save(recipeOutput(provider));
			})
			.onRegister(movementBehaviour(new BigSeatMovementBehaviour()))
			.onRegister(interactionBehaviour(new SeatInteractionBehaviour()))
			//.onRegister(assignDataBehaviour(new EntityNameDisplaySource(), "entity_name"))
			.onRegisterAfter(Registries.ITEM, v -> ItemDescription.useKey(v, "block.interiors.chair"))
			.tag(CITags.Blocks.CHAIRS)
			.item()
			.tag(CITags.Items.CHAIRS)
			.model(AssetLookup.customBlockItemModel("chair", colorName + "_chair_" + ChairBlock.ArmrestConfiguration.DEFAULT.getSerializedName()))
			.build()
			.register();
	});

	public static final BlockEntry<BigChairBlock> KELP_CHAIR = REGISTRATE.block("kelp_chair", p -> new BigChairBlock(p, DyeColor.BLACK))
		.initialProperties(SharedProperties::wooden)
		.properties(p -> p.mapColor(DyeColor.BLACK))
		.transform(axeOnly())
		.blockstate((ctx, provider) -> {
			provider.getVariantBuilder(ctx.get())
				.forAllStatesExcept(state -> {
					String armrest = state.getValue(ChairBlock.ARMRESTS).getSerializedName();
					String cropped_state = state.getValue(ChairBlock.CROPPED_BACK) ? "_cropped" : "";

					int rotation = facing(state);

					return modelWithRotation(createModelFileWithExistingParent(provider,
						"block/chair/" + armrest + cropped_state,
						"block/chair/kelp_chair_" + armrest + cropped_state), rotation);
				}, WATERLOGGED);
		})
		.onRegister(movementBehaviour(new BigSeatMovementBehaviour()))
		.onRegister(interactionBehaviour(new SeatInteractionBehaviour()))
		.onRegisterAfter(Registries.ITEM, v -> ItemDescription.useKey(v, "block.interiors.chair"))
		.item()
		.model(AssetLookup.customBlockItemModel("chair", "kelp_chair_" + ChairBlock.ArmrestConfiguration.DEFAULT.getSerializedName()))
		.build()
		.register();

	public static final BlockEntry<FloorChairBlock> KELP_FLOOR_CHAIR = REGISTRATE.block("kelp_floor_chair", p -> new FloorChairBlock(p, DyeColor.BLACK))
		.initialProperties(SharedProperties::wooden)
		.properties(p -> p.mapColor(DyeColor.BLACK))
		.transform(axeOnly())
		.blockstate((ctx, provider) -> {
			provider.getVariantBuilder(ctx.get())
				.forAllStatesExcept(state -> {
					String armrest = state.getValue(ChairBlock.ARMRESTS).getSerializedName();
					String cropped_state = state.getValue(ChairBlock.CROPPED_BACK) ? "_cropped" : "";

					int rotation = facing(state);
					return modelWithRotation(createModelFileWithExistingParent(provider,
						"block/floor_chair/" + armrest + cropped_state,
						"block/chair/kelp_floor_chair_" + armrest + cropped_state), rotation);
				}, WATERLOGGED);
		})
		.onRegister(movementBehaviour(new SeatMovementBehaviour()))
		.onRegister(interactionBehaviour(new SeatInteractionBehaviour()))
		.onRegisterAfter(Registries.ITEM, v -> ItemDescription.useKey(v, "block.interiors.chair"))
		.item()
		.model(AssetLookup.customBlockItemModel("chair", "kelp_floor_chair_" + ChairBlock.ArmrestConfiguration.DEFAULT.getSerializedName()))
		.build()
		.register();

	public static final BlockEntry<DirectionalSeatBlock> KELP_SEAT = REGISTRATE.block("kelp_seat", p -> new DirectionalSeatBlock(p, DyeColor.BLACK))
		.initialProperties(SharedProperties::wooden)
		.properties(p -> p.mapColor(DyeColor.BLACK))
		.transform(axeOnly())
		.blockstate((ctx, provider) -> {
			provider.getVariantBuilder(ctx.get())
				.forAllStatesExcept(state -> {
					int rotation = facing(state);
					return modelWithRotation(getExistingModelFile(provider, "block/kelp_seat"), rotation);
				}, WATERLOGGED);
		})
		.onRegister(movementBehaviour(new SeatMovementBehaviour()))
		.onRegister(interactionBehaviour(new SeatInteractionBehaviour()))
		//.onRegister(assignDataBehaviour(new EntityNameDisplaySource(), "entity_name"))
		.onRegisterAfter(Registries.ITEM, v -> ItemDescription.useKey(v, "block.create.seat"))
		.simpleItem()
		.register();

	public static final DyedBlockList<CushionBlock> CUSHION_BLOCKS = new DyedBlockList<>(color -> {
		String colorName = color.getSerializedName();
		return REGISTRATE.block(colorName + "_cushion", CushionBlock::new)
			.initialProperties(SharedProperties::wooden)
			.properties(p -> p.mapColor(color))
			.transform(b -> b.tag(BlockTags.MINEABLE_WITH_AXE).tag(BlockTags.WOOL))
			.blockstate((c, p) -> {
				ResourceLocation texture = Create.asResource("block/seat/top_" + colorName);
				simpleBlock(c, p, texture);
			})
			.onRegisterAfter(Registries.ITEM, v -> ItemDescription.useKey(v, "block.interiors.cushion"))
			.recipe((c, p) ->
				ShapelessRecipeBuilder.shapeless(RecipeCategory.BUILDING_BLOCKS, c.get(), 2)
					.requires(ItemTags.PLANKS)
					.requires(DyeHelper.getWoolOfDye(color))
					.unlockedBy("has_planks", has(ItemTags.PLANKS))
					.save(recipeOutput(p), CreateInteriors.asResource("crafting/cushion/" + c.getName())))
			.simpleItem()
			.register();
	});

	public static void register() {
		// Load class and initialize all static block registrations
	}
	
	public static void setupTab() {
		// Called after all blocks are registered to setup the creative tab
		CreateInteriors.platform.setupCreativeTab();
	}

	private static int facing(BlockState state) {
		return switch(state.getValue(ChairBlock.FACING)) {
			case NORTH, UP, DOWN -> 0;
			case EAST -> 90;
			case SOUTH -> 180;
			case WEST -> 270;
		};
	}

	@ExpectPlatform
	@ApiStatus.Internal
	public static void setupCreativeTab() {
		throw new AssertionError();
	}

	@ExpectPlatform
	@ApiStatus.Internal
	public static <ModelFile> ModelFile createModelFileWithExistingParent(Object /* BlockStateProvider */ p, String parent, String name) {
		throw new AssertionError();
	}

	@ExpectPlatform
	@ApiStatus.Internal
	public static <ModelFile> ModelFile getExistingModelFile(Object /* BlockStateProvider */ p, String name) {
		throw new AssertionError();
	}

	@ExpectPlatform
	@ApiStatus.Internal
	public static <ModelFile> ModelFile customChairModelFile(Object /* BlockStateProvider */ p, String parent, String name,
															  ResourceLocation top, ResourceLocation side, ResourceLocation sideTop, ResourceLocation sideFront) {
		throw new AssertionError();
	}

	@ExpectPlatform
	@ApiStatus.Internal
	public static <ConfiguredModel> ConfiguredModel modelWithRotation(Object /* ModelFile */ model, int rotation) {
		throw new AssertionError();
	}

	@ExpectPlatform
	@ApiStatus.Internal
	public static void simpleBlock(DataGenContext<Block, ?> c, RegistrateBlockstateProvider p, ResourceLocation texture) {
		throw new AssertionError();
	}
}
