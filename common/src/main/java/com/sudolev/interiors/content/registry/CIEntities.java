package com.sudolev.interiors.content.registry;

import com.sudolev.interiors.CreateInteriors;
import com.sudolev.interiors.content.entity.BigSeatEntity;
import com.tterrag.registrate.util.entry.EntityEntry;

@SuppressWarnings("unused")
public final class CIEntities {
	public static EntityEntry<BigSeatEntity> BIG_SEAT;

	private static EntityEntry<BigSeatEntity> createSeat() {
		return CreateInteriors.platform.createSeat();
	}

	public static void register() {
		BIG_SEAT = createSeat();
	}
}
