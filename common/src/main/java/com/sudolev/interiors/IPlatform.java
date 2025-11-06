package com.sudolev.interiors;

import com.sudolev.interiors.content.entity.BigSeatEntity;
import com.tterrag.registrate.util.entry.EntityEntry;

/**
 * An interface to provide platform-specific implementations to the common module,
 * avoiding @ExpectPlatform issues during initialization.
 */
public interface IPlatform {
    EntityEntry<BigSeatEntity> createSeat();
    void setupCreativeTab();
}
