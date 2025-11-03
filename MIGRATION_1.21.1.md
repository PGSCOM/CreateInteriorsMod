# Migration to Minecraft 1.21.1

This document describes the changes made to migrate Create: Interiors from Minecraft 1.20.1 to 1.21.1.

## Version Changes

### Minecraft and Loaders
- **Minecraft**: 1.20.1 → 1.21.1
- **Forge**: 47.2.6 → **NeoForge** 21.1.72
- **Fabric Loader**: 0.16.9 (unchanged)
- **Fabric API**: 0.92.3 → 0.110.0

### Build Tools
- **Java**: 17 → 21 (required for 1.21.1)
- **Architectury Plugin**: 3.4.161 → 3.4.166
- **Architectury Loom**: 1.9.428 → 1.10.85

### Mappings
- **Quilt Mappings**: build 23 → build 7 (for 1.21.1)
- **Parchment**: 2023.09.03 → 2024.11.17

### Dependencies
- **Create (Fabric)**: 6.0.0.0$$1649 → 0.5.1-f-build.1417+mc1.21.1
- **Create (NeoForge)**: 6.0.1-20 → 0.5.1-i-build.1414+mc1.21.1
- **Registrate**: MC1.20-1.3.3 → MC1.21-1.5.0
- **Ponder**: 1.0.36 → 1.1.1
- **Flywheel**: 1.0.1 → 1.1.0
- **Mod Menu**: 7.2.2 → 11.0.3

## Major Changes

### 1. Forge → NeoForge Migration

NeoForge is the successor to Forge for Minecraft 1.20.5+. Key changes:

#### Directory Structure
- Renamed `forge/` directory to `neoforge/`
- Renamed `forge/gradle.properties` platform setting to `loom.platform=neoforge`

#### Package Names
- Changed all `com.sudolev.interiors.forge` packages to `com.sudolev.interiors.neoforge`
- Updated imports throughout the codebase

#### API Changes
**UtilsImpl.java**:
- `net.minecraftforge.*` → `net.neoforged.*`
- `MavenVersionStringHelper.artifactVersionToString()` → `info.getVersion().toString()`
- Platform name changed from "Forge" to "NeoForge"

**CreateInteriorsForge.java**:
- `net.minecraftforge.fml.common.Mod` → `net.neoforged.fml.common.Mod`
- `net.minecraftforge.eventbus.api.IEventBus` → `net.neoforged.bus.api.IEventBus`
- `FMLJavaModLoadingContext.get().getModEventBus()` → Constructor injection of `IEventBus`
- `MinecraftForge.EVENT_BUS` → `NeoForge.EVENT_BUS`
- Constructor now takes `(IEventBus modEventBus, ModContainer modContainer)` parameters

**CITabImpl.java**:
- `net.minecraftforge.registries.DeferredRegister` → `net.neoforged.neoforge.registries.DeferredRegister`
- `RegistryObject<CreativeModeTab>` → `Supplier<CreativeModeTab>`
- Changed from `::build` syntax to lambda `() -> ... .build()`

**CIBlocksImpl.java**:
- `net.minecraftforge.client.model.generators.*` → `net.neoforged.neoforge.client.model.generators.*`

#### Metadata Files
- Renamed `META-INF/mods.toml` to `META-INF/neoforge.mods.toml`
- Updated dependency format:
  - `mandatory = true` → `type = "required"`
  - `modId = "forge"` → `modId = "neoforge"`
- Updated version range references from `forge_version` to `neoforge_version`

#### Build Configuration
**build.gradle.kts**:
- Changed `architectury.forge()` to `architectury.neoForge()`
- Changed `loom.forge {}` to `loom.neoForge {}`
- Updated dependency configurations:
  - `forge()` → `neoForge()`
  - Package names in artifacts: `create-$MC` → `create-neoforge-$MC`
  - `Ponder-Forge` → `Ponder-NeoForge`
  - `flywheel-forge` → `flywheel-neoforge`
  - `mixinextras-forge` → `mixinextras-neoforge`
  - Added `:slim` classifier to Create dependency

**settings.gradle.kts**:
- Updated `include("forge")` to `include("neoforge")`
- Changed Maven repository from `maven.minecraftforge.net` to `maven.neoforged.net/releases`

### 2. Fabric Changes

**build.gradle.kts**:
- Simplified Create version handling (removed string splitting logic)
- Version format changed to use `+mc` separator instead of `$$`

**common/build.gradle.kts**:
- Updated Create Fabric dependency version format

### 3. Build Script Updates

**gradle.properties**:
- All version numbers updated as listed above
- Removed `forge_version`, added `neoforge_version`
- Removed `create_forge_version`, added `create_neoforge_version`

**build.gradle.kts**:
- Updated `processResources` to use `neoforge.mods.toml` instead of `mods.toml`
- Changed property references from `forge_*` to `neoforge_*`
- Updated Java toolchain from 17 to 21

## Testing

Due to network restrictions in the build environment, the build could not be fully tested. The changes follow standard migration patterns from Forge to NeoForge for 1.21.1, based on:

1. Official NeoForge migration documentation
2. Architectury multiloader patterns
3. Create mod's own migration to 1.21.1
4. Standard Minecraft 1.21.1 modding practices

## Next Steps

After resolving network/repository access:
1. Run `./gradlew build` to compile all loaders
2. Test NeoForge build in-game
3. Test Fabric build in-game
4. Verify all features work correctly
5. Update CI/CD pipelines if needed

## Notes

- This migration maintains backward compatibility with existing world saves where possible
- Resource packs and data packs may need minor updates
- Client and server should be updated together
