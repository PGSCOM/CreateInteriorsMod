# 🔧 FIX: Language Provider Version Error

## Error Encontrado en Testing

Al intentar cargar el mod en un servidor NeoForge 21.1.192, se presentaba el siguiente error:

```
[22:28:58 ERROR] Missing language provider javafml version [21,) wanted by 
interiors-0.5.6+neoforge-mc1.21.1-local.jar, found 4.0.41

FATAL: Mod File needs language provider javafml:21 or above to load
We have found 4.0.41
```

## Causa Raíz

El archivo `neoforge/src/main/resources/META-INF/neoforge.mods.toml` tenía una configuración incorrecta:

```toml
# INCORRECTO ❌
modLoader = "javafml"
loaderVersion = "[${neoforge_version},)"  # Esto se expande a [21.1.192,)
```

Esta configuración estaba requiriendo `javafml` versión 21 o superior, cuando en realidad NeoForge 21.1.192 usa `javafml` versión 4.0.41.

## Solución Aplicada

Cambié el `loaderVersion` al rango correcto:

```toml
# CORRECTO ✅
modLoader = "javafml"
loaderVersion = "[4,)"  # Compatible con javafml:4.0.41 de NeoForge 21.1.x
```

## Explicación Técnica

### ¿Qué es `javafml`?
- `javafml` es el **language provider** de NeoForge para mods escritos en Java
- Es la capa que permite a NeoForge cargar y ejecutar código de mods Java

### Versionado de `javafml`
- NeoForge 21.1.x usa `javafml` **versión 4.0.x**
- El número de versión de `javafml` NO se corresponde con la versión de NeoForge
- La confusión ocurrió al usar `${neoforge_version}` (21.1.192) como `loaderVersion`

### Rangos de Versión Correctos
```toml
# Para NeoForge 21.0.x - 21.1.x
loaderVersion = "[4,)"      # Acepta javafml 4.0.x y superiores

# Alternativas válidas:
loaderVersion = "[4.0,)"    # Más específico
loaderVersion = "[4,5)"     # Limita a versión major 4
```

## Verificación del Fix

### 1. Recompilación
```bash
export JAVA_HOME=/usr/lib/jvm/java-21-openjdk-amd64
./gradlew :neoforge:build
```

### 2. Verificación del JAR
```bash
unzip -p neoforge/build/libs/interiors-0.5.6+neoforge-mc1.21.1-local.jar \
  META-INF/neoforge.mods.toml | head -5
```

**Resultado**:
```toml
modLoader = "javafml"
loaderVersion = "[4,)"  ✅
license = "GPL-3.0"
```

### 3. Testing en Servidor
El mod ahora carga correctamente en servidores con:
- NeoForge 21.1.192
- javafml 4.0.41
- Java 21

## Archivos Modificados

```
neoforge/src/main/resources/META-INF/neoforge.mods.toml
  - Línea 2: loaderVersion = "[${neoforge_version},)" → loaderVersion = "[4,)"
```

## Referencias

- [NeoForge Mod Loader Documentation](https://docs.neoforged.net/docs/gettingstarted/modfiles/)
- [Language Providers in NeoForge](https://docs.neoforged.net/docs/misc/lang/)
- NeoForge 21.1.x usa FML Loader 4.0.x

## Status

✅ **FIX APLICADO Y VERIFICADO**

El mod ahora es compatible con:
- Clientes NeoForge 21.1.x
- Servidores NeoForge 21.1.x (incluyendo Arclight)
- javafml 4.0.x

---

**Fecha del Fix**: 5 de Noviembre, 2025  
**Versión**: 0.5.6+mc1.21.1-local (post-fix)
