# 🎉 CreateInteriorsMod v0.5.6 - Minecraft 1.21.1 + NeoForge 21.1.192

## 📦 Versión Estable - Build Exitoso

**Fecha de Release**: 5 de Noviembre, 2025  
**Estado**: ✅ **ESTABLE Y LISTO PARA USO**

---

## 🚀 Resumen Ejecutivo

Esta versión es una **migración completa y exitosa** de CreateInteriorsMod desde Minecraft 1.20.1/Forge a **Minecraft 1.21.1/NeoForge 21.1.192**.

### ✅ Compilación
```
BUILD SUCCESSFUL in 16s
19 actionable tasks: 12 executed, 7 up-to-date
✅ 0 Errores de compilación
⚠️ 34 Warnings (normales, relacionados con análisis cross-platform)
```

### 📥 Instalación
1. Descarga: `neoforge/build/libs/interiors-0.5.6+neoforge-mc1.21.1-local.jar`
2. Coloca en la carpeta `mods/` de tu instalación de Minecraft 1.21.1
3. Asegúrate de tener **NeoForge 21.1.192** o superior instalado
4. Requiere **Create 6.0.9** o superior para NeoForge

---

## ✨ Lo Nuevo

### Plataforma Actualizada
- ✅ Minecraft 1.21.1 (última versión estable)
- ✅ NeoForge 21.1.192 (sucesor de Forge)
- ✅ Java 21 (requerido para MC 1.21+)
- ✅ Create 6.0.9-171 compatible

### APIs Migradas
- ✅ ResourceLocation API actualizada
- ✅ Entity Passenger System modernizado
- ✅ Recipe System adaptado para MC 1.21.1
- ✅ Creative Tabs actualizado para NeoForge
- ✅ Item Registry API migrada

---

## 🎮 Funcionalidades

### ✅ Completamente Funcional
- **Sillas de Colores**: 16 variantes de sillas (una por cada color de lana)
- **Sillas de Piso**: Variantes de silla baja para decoración
- **Asientos de Alga**: Variantes especiales con tema marino
- **Mesa de Pared**: Mesa montable en paredes
- **Cojines**: Bloques decorativos acolchados (16 colores)
- **Big Seats**: Asientos grandes para contraptions de Create
- **Mecánica de Asiento**: Sentarse en bloques, levantarse con Shift
- **Integración con Create**: 
  - Compatible con contraptions móviles
  - Comportamiento de asiento en estructuras móviles
  - Interacción con sistemas mecánicos
- **Waterlogging**: Bloques pueden estar sumergidos en agua
- **Crafteo**: Recetas básicas funcionales (cushions)
- **Creative Tab**: Todos los items organizados en pestaña propia

### ⚠️ Limitaciones Conocidas (No Críticas)
- **Variantes Visuales**: Los blockstates usan modelos base (sin variantes de rotación/armrests dinámicas)
- **Recetas de Dyeing**: Cambio de color de bloques pendiente de implementar
- **Wrench Rotation**: Rotación con llave inglesa temporalmente deshabilitada

> **Nota**: Estas limitaciones son **cosméticas/conveniencia** y no afectan la jugabilidad core.

---

## 🔧 Cambios Técnicos

### Para Desarrolladores
- Migración completa de Forge a NeoForge
- Todos los mixins funcionando correctamente
- Sistema de registro Architectury actualizado
- Compatibilidad con futuros loaders mantenida (Fabric preparado)

### Helper Methods Implementados
```java
// Nuevos helpers para recetas en MC 1.21.1
private static Criterion<?> has(ItemLike item)
private static Criterion<?> has(TagKey<Item> tag)
private static RecipeOutput recipeOutput(RegistrateRecipeProvider provider)
```

---

## 📋 Requisitos

### Mínimos
- Minecraft **1.21.1**
- NeoForge **21.1.192** o superior
- Create **6.0.9** o superior (para NeoForge)
- Java **21** (JDK/JRE)

### Recomendados
- 4GB RAM asignados a Minecraft
- OptiFine/Sodium para mejor rendimiento (opcional)

---

## 🐛 Problemas Conocidos

### Ninguno Crítico ✅
El mod carga y funciona sin errores. Las funcionalidades deshabilitadas son opcionales.

### Reportar Bugs
Si encuentras algún problema:
1. Verifica que estés usando las versiones correctas (MC 1.21.1, NeoForge 21.1.192)
2. Revisa la consola para errores
3. Reporta en el repositorio con logs completos

---

## 🔮 Futuras Mejoras

### Planificado para Próximas Versiones
- [ ] Restaurar blockstate variants dinámicos
- [ ] Implementar recetas de cambio de color
- [ ] Actualizar a Registrate MC1.21-1.5.0
- [ ] Restaurar generadores de modelos personalizados
- [ ] Re-habilitar rotación con llave inglesa
- [ ] Soporte completo para Fabric (si hay demanda)

---

## 📚 Documentación

Para información técnica detallada, consulta:
- `VERSION_STATUS.md` - Estado completo de la migración
- `MIGRATION_1.21.1.md` - Guía de migración
- `TESTING_GUIDE.md` - Guía de testing

---

## 🙏 Créditos

- **Mod Original**: sudolev
- **Migración a 1.21.1**: Completada con GitHub Copilot
- **Create Mod**: simibubi y equipo
- **Architectury**: Equipo de Architectury
- **NeoForge**: Equipo de NeoForged

---

## 📄 Licencia

Este proyecto mantiene la licencia original. Ver `LICENSE` para detalles.

---

**¡Gracias por usar CreateInteriorsMod! 🪑✨**

