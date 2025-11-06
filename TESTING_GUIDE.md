# Post-Migration Testing Guide

This guide explains how to test and finalize the 1.21.1 migration after the code changes have been completed.

## Prerequisites

An environment with full internet access to:
- Maven Central
- maven.neoforged.net
- maven.architectury.dev
- maven.fabricmc.net
- maven.createmod.net
- Other mod repositories

## Step 1: Build Verification

### Clean and Build
```bash
./gradlew clean
./gradlew build
```

### Expected Outcome
- All three projects (common, neoforge, fabric) should compile successfully
- JAR files should be generated in:
  - `neoforge/build/libs/`
  - `fabric/build/libs/`

### Common Build Issues and Fixes

#### Issue: Dependency Resolution Failures
**Symptoms**: Cannot download Create, Ponder, Flywheel, or other dependencies
**Solutions**:
- Verify internet connectivity
- Check if version numbers match available releases
- Try updating to latest compatible versions in `gradle.properties`

#### Issue: API Method Not Found Errors
**Symptoms**: Compilation errors about missing methods in Create, NeoForge, or Minecraft APIs
**Common Changes in 1.21.1**:
- Check Create's GitHub for breaking changes
- Review NeoForge migration guide for API differences
- Some common changes:
  - Constructor injection changes in mod initializers
  - Registry system updates
  - Block property changes
  - Entity attribute changes

#### Issue: Mixin Application Failures
**Symptoms**: Errors about mixin targets not found
**Solutions**:
- Verify mixin target classes still exist in 1.21.1
- Check if target method signatures have changed
- Update mixin selectors if needed

## Step 2: In-Game Testing

### NeoForge Testing
1. Install NeoForge 21.1.72+ for Minecraft 1.21.1
2. Place the built JAR from `neoforge/build/libs/` into the mods folder
3. Install Create for NeoForge 1.21.1 (matching version in gradle.properties)
4. Launch the game

### Fabric Testing
1. Install Fabric Loader 0.16.9+ for Minecraft 1.21.1
2. Install Fabric API 0.110.0+ for Minecraft 1.21.1
3. Place the built JAR from `fabric/build/libs/` into the mods folder
4. Install Create for Fabric 1.21.1 (matching version in gradle.properties)
5. Launch the game

### Test Checklist
- [ ] Game launches without crashes
- [ ] Mod loads successfully
- [ ] Creative tab appears
- [ ] All blocks are visible in creative inventory
- [ ] Chairs can be placed and interacted with
- [ ] Seats can be sat on
- [ ] Armrest configurations work (right-click with wrench)
- [ ] Back size can be toggled (sneak + right-click with wrench)
- [ ] Floor chairs work correctly
- [ ] Wall-mounted tables place and function
- [ ] Kelp seats work as expected
- [ ] Seatwood planks can be crafted
- [ ] All chair colors are available
- [ ] Chairs work on contraptions
- [ ] Entities can be picked up by chairs
- [ ] Textures render correctly
- [ ] No missing texture errors
- [ ] Crafting recipes work
- [ ] Waterlogging works on applicable blocks
- [ ] Multiplayer compatibility (if applicable)

## Step 3: Version-Specific Adjustments

### If Create API Has Changed
Check the Create mod's changelog for 1.21.1 and update:
- Registrate usage patterns
- Ponder integration
- Contraption behavior interfaces
- Movement behavior implementations

### If Rendering Issues Occur
- Verify model JSON files are compatible with 1.21.1 format
- Check blockstate JSON files
- Ensure texture paths are correct
- Verify render layers are properly set

### If Recipe Issues Occur
- Data generation might need to be re-run
- Recipe format may have changed
- Update recipe JSON files if needed

## Step 4: Performance Testing

Run performance tests to ensure:
- No memory leaks
- Reasonable frame rates
- No lag spikes when placing/breaking blocks
- Contraptions with chairs perform well

## Step 5: Compatibility Testing

Test with common mods:
- JEI/REI (recipe viewing)
- Mod Menu (Fabric)
- Other Create addons
- Decorative mods

## Step 6: Update Documentation

Once testing is complete:
- Update CHANGELOG.md with version changes
- Update version badge in README.md if needed
- Document any known issues
- Update wiki/documentation sites

## Known Potential Issues

### 1. Constructor Injection in NeoForge
The main mod class now uses constructor injection. If this causes issues:
```java
public CreateInteriorsForge(IEventBus modEventBus, ModContainer modContainer) {
    // Constructor parameters are injected by NeoForge
}
```

### 2. DeferredRegister Changes
NeoForge changed from `RegistryObject<T>` to `Supplier<T>`:
```java
// Old (Forge)
public static final RegistryObject<CreativeModeTab> TAB = ...

// New (NeoForge)
public static final Supplier<CreativeModeTab> TAB = ...
```

### 3. Pack Format
Ensure `pack.mcmeta` uses format 34 for 1.21.1 resource packs.

### 4. Java 21 Features
The project now requires Java 21. Ensure:
- JDK 21 is installed
- IDE is configured for Java 21
- CI/CD pipelines use Java 21

## Troubleshooting Resources

- NeoForge Discord: https://discord.neoforged.net/
- Create Mod Discord: https://discord.gg/create
- Architectury Discord: https://discord.gg/architectury
- Fabric Discord: https://discord.gg/v6v4pMv

## Success Criteria

The migration is complete when:
1. ✅ Project builds without errors
2. ✅ Both NeoForge and Fabric versions launch in-game
3. ✅ All features work as expected
4. ✅ No console errors or warnings
5. ✅ Performance is acceptable
6. ✅ Compatible with other popular mods

## Rollback Plan

If critical issues are found:
1. Create a backup branch of this work
2. Review specific errors
3. Consult Create mod's migration guide
4. Join relevant Discord servers for help
5. Consider incremental fixes rather than full rollback

## Notes

- Version numbers in gradle.properties may need adjustment based on actual available versions
- The migration follows standard patterns and should work with minimal adjustments
- Some minor fixes may be needed for specific API changes
- Community feedback will help identify any remaining issues
