package com.sudolev.interiors.fabric;

import com.sudolev.interiors.CreateInteriors;
import com.sudolev.interiors.IPlatform;
import com.sudolev.interiors.content.entity.BigSeatEntity;
import com.sudolev.interiors.content.registry.fabric.CIBlocksImpl;
import com.tterrag.registrate.util.entry.EntityEntry;
import net.minecraft.world.entity.MobCategory;

public class PlatformImpl implements IPlatform {
    @Override
    public EntityEntry<BigSeatEntity> createSeat() {
        return CreateInteriors.REGISTRATE
                .<BigSeatEntity>entity("big_seat", BigSeatEntity::new, MobCategory.MISC)
                .properties(b -> b.fireImmune()
                        .sized(.25f, .85f)
                        .trackRangeBlocks(5)
                        .trackedUpdateRate(Integer.MAX_VALUE)
                        .build())
                .renderer(() -> BigSeatEntity.Render::new)
                .register();
    }

    @Override
    public void setupCreativeTab() {
        CIBlocksImpl.setupCreativeTab();
    }
}
