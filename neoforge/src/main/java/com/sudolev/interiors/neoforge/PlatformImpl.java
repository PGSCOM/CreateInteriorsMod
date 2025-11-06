package com.sudolev.interiors.neoforge;

import com.sudolev.interiors.CreateInteriors;
import com.sudolev.interiors.IPlatform;
import com.sudolev.interiors.content.entity.BigSeatEntity;
import com.sudolev.interiors.content.registry.neoforge.CIBlocksImpl;
import com.tterrag.registrate.util.entry.EntityEntry;
import net.minecraft.world.entity.MobCategory;

public class PlatformImpl implements IPlatform {
    @Override
    public EntityEntry<BigSeatEntity> createSeat() {
        return CreateInteriors.REGISTRATE
                .<BigSeatEntity>entity("big_seat", BigSeatEntity::new, MobCategory.MISC)
                .properties(b -> b.fireImmune()
                        .sized(.25f, .85f)
                        .setTrackingRange(5)
                        .setUpdateInterval(Integer.MAX_VALUE)
                        .setShouldReceiveVelocityUpdates(false))
                .renderer(() -> BigSeatEntity.Render::new)
                .register();
    }

    @Override
    public void setupCreativeTab() {
        CIBlocksImpl.setupCreativeTab();
    }
}
