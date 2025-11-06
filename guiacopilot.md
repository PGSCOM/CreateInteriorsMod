# Guía de Compilación - Create: Interiors Mod
## NeoForge 21.1.192 | Minecraft 1.21.1

Esta guía detalla los pasos necesarios para compilar exitosamente el mod Create: Interiors en NeoForge 1.21.1.

---

## 🔴 Problemas Identificados

### 1. **Versión de Java Incorrecta**
- **Error**: `Dependency requires at least JVM runtime version 17. This build uses a Java 11 JVM.`
- **Actual**: Java 11
- **Requerido**: Java 17 o superior
- **Solución**: Actualizar a Java 21 (recomendado para Minecraft 1.21.1)

### 2. **Errores de Registro en Runtime**
Según la captura de pantalla del juego:
- `RuntimeException: Unexpected error while registering entry interiors:white_floor_chair to registry minecraft:block`
- `RuntimeException: Unexpected error while registering entry interiors:orange_floor_chair to registry minecraft:item`
- `ExceptionInInitializerError: null` al registrar `create:schematicannon` (bloque de Create)

### 3. **Problemas de Orden de Inicialización**
- Los bloques/items se están registrando en el orden incorrecto
- El `CreateRegistrate` no está completamente vinculado al EventBus antes de registrar entradas
- El Creative Tab se está registrando antes de que los items estén disponibles

### 4. **APIs Obsoletas de Minecraft 1.20 → 1.21.1**
Varios métodos comentados con `// TODO: Fix` debido a cambios de API:
- Métodos de blockstate con `.variants()` 
- Recetas sin el parámetro `RecipeOutput` correcto
- DataGen con APIs movidas en NeoForge

---

## ✅ Pasos para Solucionar

### **Paso 1: Actualizar Java a 21**

#### Opción A: Usar SDKMAN (Recomendado en Linux)
```bash
# Instalar SDKMAN si no está instalado
curl -s "https://get.sdkman.io" | bash
source "$HOME/.sdkman/bin/sdkman-init.sh"

# Instalar Java 21
sdk install java 21.0.5-tem
sdk use java 21.0.5-tem

# Verificar
java -version
```

#### Opción B: Usar apt (Ubuntu/Debian)
```bash
sudo apt update
sudo apt install openjdk-21-jdk -y
sudo update-alternatives --config java  # Seleccionar Java 21
java -version
```

#### Opción C: Configurar en el Dev Container
Actualizar `.devcontainer/devcontainer.json` para usar Java 21:
```json
{
  "image": "mcr.microsoft.com/devcontainers/java:21",
  "features": {
    "ghcr.io/devcontainers/features/java:1": {
      "version": "21"
    }
  }
}
```

---

### **Paso 2: Corregir el Orden de Inicialización del Mod**

**Archivo**: `neoforge/src/main/java/com/sudolev/interiors/neoforge/CreateInteriorsForge.java`

**Problema**: El `CreateRegistrate` debe vincularse al bus ANTES de cualquier llamada a `.register()` en los bloques/items.

**Corrección**:
```java
@Mod(CreateInteriors.ID)
public class CreateInteriorsForge {
    public CreateInteriorsForge(IEventBus modEventBus, ModContainer modContainer) {
        // PASO 1: Vincular Registrate al EventBus PRIMERO
        CreateInteriors.REGISTRATE.registerEventListeners(modEventBus);
        
        // PASO 2: Inicializar el mod (esto ejecuta CIBlocks.register(), etc.)
        String version = modContainer.getModInfo().getVersion().toString();
        CreateInteriors.init(version, "NeoForge", new PlatformImpl());
        
        // PASO 3: Registrar Creative Tab AL FINAL
        CITabImpl.register(modEventBus);
    }
}
```

---

### **Paso 3: Arreglar la Inicialización del Creative Tab**

**Archivo**: `neoforge/src/main/java/com/sudolev/interiors/content/registry/neoforge/CITabImpl.java`

**Problema**: El tab intenta acceder a `CIBlocks.CHAIRS` antes de que esté registrado.

**Corrección**: Usar un `Supplier<ItemStack>` lazy para evitar acceso prematuro:

```java
public static void register(IEventBus modEventBus) {
    ensureInitialized();
    REGISTER.register(modEventBus);
}

public static void ensureInitialized() {
    if (REGISTER != null) return;
    
    REGISTER = DeferredRegister.create(Registries.CREATIVE_MODE_TAB, CreateInteriors.ID);
    
    TAB = REGISTER.register("main", () -> {
        return CreativeModeTab.builder()
            .title(Component.literal(CreateInteriors.NAME))
            .icon(() -> {
                // Lazy evaluation - solo se evalúa cuando el tab se renderiza
                try {
                    return CIBlocks.CHAIRS.get(DyeColor.RED).asStack(1);
                } catch (Exception e) {
                    CreateInteriors.LOGGER.error("Failed to create tab icon", e);
                    return ItemStack.EMPTY;
                }
            })
            .displayItems((parameters, output) -> {
                CreateInteriors.REGISTRATE.getAll(Registries.BLOCK).stream()
                    .map(entry -> {
                        try {
                            return entry.get().asItem();
                        } catch (Exception ex) {
                            return null;
                        }
                    })
                    .filter(Objects::nonNull)
                    .forEach(output::accept);
            })
            .build();
    });
}
```

---

### **Paso 4: Completar las APIs de BlockState para 1.21.1**

**Archivo**: `common/src/main/java/com/sudolev/interiors/content/registry/CIBlocks.java`

Los métodos `.blockstate()` están comentados. Necesitan migración a la nueva API.

**Ejemplo de migración** (WALL_MOUNTED_TABLE):
```java
.blockstate((ctx, provider) -> {
    provider.getVariantBuilder(ctx.get())
        .forAllStatesExcept(state -> {
            Direction facing = state.getValue(WallMountedTable.FACING);
            int rotation = switch (facing) {
                case NORTH -> 0;
                case EAST -> 90;
                case SOUTH -> 180;
                case WEST -> 270;
                default -> 0;
            };
            
            return ConfiguredModel.builder()
                .modelFile(provider.models().getExistingFile(
                    CreateInteriors.asResource("block/wall_mounted_table")))
                .rotationY(rotation)
                .build();
        }, WATERLOGGED);
})
```

**Aplicar patrón similar** para:
- `FLOOR_CHAIRS` (con variantes de armrest y cropped_back)
- `CHAIRS` (con variantes de armrest)
- `CUSHIONS` (con 16 colores)

---

### **Paso 5: Corregir Recetas para 1.21.1**

**Archivo**: `common/src/main/java/com/sudolev/interiors/content/registry/CIBlocks.java`

Las recetas `.recipe((c, p) -> ...)` están comentadas.

**Patrón de migración**:
```java
.recipe((ctx, provider) -> {
    ShapelessRecipeBuilder.shapeless(RecipeCategory.DECORATIONS, ctx.get())
        .requires(AllBlocks.SEAT.get())  // Ingredientes
        .requires(ItemTags.PLANKS)
        .unlockedBy("has_seat", has(AllBlocks.SEAT.get()))  // Criterio
        .save(provider);  // provider ya es RecipeOutput en 1.21.1
})
```

**Aplicar a todos los bloques** que tienen recetas comentadas.

---

### **Paso 6: Actualizar CIBlocksImpl para NeoForge 1.21.1**

**Archivo**: `neoforge/src/main/java/com/sudolev/interiors/content/registry/neoforge/CIBlocksImpl.java`

Los métodos de DataGen están comentados porque las clases se movieron.

**Imports correctos para NeoForge 1.21.1**:
```java
import net.neoforged.neoforge.client.model.generators.BlockStateProvider;
import net.neoforged.neoforge.client.model.generators.ConfiguredModel;
import net.neoforged.neoforge.client.model.generators.ModelFile;
```

**Descomentar y actualizar los métodos**:
```java
@ApiStatus.Internal
public static ModelFile customChairModelFile(Object o, String parent, String name,
                                              ResourceLocation top, ResourceLocation side,
                                              ResourceLocation sideTop, ResourceLocation sideFront) {
    BlockStateProvider p = (BlockStateProvider) o;
    return p.models()
        .withExistingParent(name, p.modLoc(parent))
        .texture("top", top)
        .texture("side_top", sideTop)
        .texture("side_front", sideFront)
        .texture("side", side);
}
```

---

### **Paso 7: Verificar Dependencias en build.gradle.kts**

**Archivo**: `neoforge/build.gradle.kts`

Asegurar que las versiones sean compatibles:
```kotlin
dependencies {
    neoForge("net.neoforged:neoforge:21.1.192")
    
    // Create para NeoForge 1.21.1 (versión correcta)
    modImplementation("com.simibubi.create:create-1.21.1:6.0.9-171:slim") { 
        isTransitive = false 
    }
    
    // Registrate compatible con MC 1.21
    modCompileOnly("com.tterrag.registrate:Registrate:MC1.21-1.3.0+62") { 
        isTransitive = false 
    }
    
    // Mixin Extras
    modImplementation("io.github.llamalad7:mixinextras-neoforge:0.4.1")
}
```

---

### **Paso 8: Limpiar y Recompilar**

```bash
# Limpiar builds anteriores
./gradlew clean

# Compilar solo NeoForge
./gradlew :neoforge:build

# O compilar todo el proyecto
./gradlew build
```

---

### **Paso 9: Verificar en Juego**

Después de compilar exitosamente:

1. Copiar el JAR generado a la carpeta de mods:
   ```bash
   cp neoforge/build/libs/interiors-*-neoforge-*.jar ~/mods/
   ```

2. Ejecutar Minecraft 1.21.1 con NeoForge 21.1.192

3. Verificar que el mod carga sin errores:
   - No debe aparecer "Error loading mods"
   - Los bloques/items deben registrarse correctamente
   - El Creative Tab debe mostrar todos los items

---

## 📋 Checklist de Compilación

- [ ] **Java 21 instalado y configurado**
- [ ] **Orden de inicialización corregido en CreateInteriorsForge.java**
- [ ] **Creative Tab inicializado correctamente (CITabImpl.java)**
- [ ] **Métodos blockstate implementados para todos los bloques**
- [ ] **Recetas migradas a API 1.21.1**
- [ ] **CIBlocksImpl DataGen actualizado**
- [ ] **Dependencias verificadas en build.gradle.kts**
- [ ] **Build exitoso sin errores**
- [ ] **Mod carga en juego sin errores de registro**

---

## 🚀 Comandos Rápidos

```bash
# Verificar Java
java -version  # Debe mostrar versión 21+

# Limpiar proyecto
./gradlew clean

# Compilar NeoForge
./gradlew :neoforge:build --stacktrace

# Ejecutar cliente de prueba
./gradlew :neoforge:runClient
```

---

## 🐛 Problemas Conocidos

### Si persiste "Unexpected error while registering entry":
1. Verificar que `REGISTRATE.registerEventListeners(modEventBus)` se llame PRIMERO
2. Asegurar que ningún código estático en CIBlocks acceda a `.get()` antes de `.register()`
3. Revisar logs en `run/logs/latest.log` para detalles

### Si falla la compilación por APIs:
1. Buscar todos los `// TODO: Fix` en el código
2. Consultar la documentación de migración: `MIGRATION_1.21.1.md`
3. Revisar ejemplos en el repositorio de Create

---

## 📚 Referencias

- [NeoForge 1.21.1 Docs](https://docs.neoforged.net/docs/1.21.x/)
- [Create Mod GitHub](https://github.com/Creators-of-Create/Create)
- [Registrate Documentation](https://github.com/tterrag1098/Registrate)
- [Minecraft 1.21.1 Changelog](https://minecraft.wiki/w/Java_Edition_1.21.1)

---

**Última actualización**: 2025-11-06
**Versión del Mod**: 0.5.6
**Target**: Minecraft 1.21.1 + NeoForge 21.1.192
