package de.bergtiger.tigerhomes.utils.isSave;

import org.bukkit.Location;

public class IsSave_1_17_1 {

	public String check(Location loc) {
		if (loc != null) {
			return switch (loc.getBlock().getType()) {
				case LAVA, LAVA_CAULDRON -> "&4Lava";
				case FIRE, SOUL_FIRE -> "&cFire";
				case WATER -> "&9Water";
				case RAIL, POWERED_RAIL, DETECTOR_RAIL, ACTIVATOR_RAIL -> "&fI like &itrains!";
				case ACACIA_FENCE, BIRCH_FENCE, CRIMSON_FENCE, JUNGLE_FENCE, OAK_FENCE, SPRUCE_FENCE, WARPED_FENCE, NETHER_BRICK_FENCE -> "&fIts a little big tight";
				case ACACIA_DOOR, DARK_OAK_DOOR, BIRCH_DOOR, CRIMSON_DOOR, IRON_DOOR, JUNGLE_DOOR, OAK_DOOR, SPRUCE_DOOR, WARPED_DOOR -> "&fHodor";
				case ACACIA_TRAPDOOR, BIRCH_TRAPDOOR, CRIMSON_TRAPDOOR, DARK_OAK_TRAPDOOR, IRON_TRAPDOOR, JUNGLE_TRAPDOOR, OAK_TRAPDOOR, SPRUCE_TRAPDOOR, WARPED_TRAPDOOR -> "&fWatch your step.";
				default -> "&aisSave";
			};
		}
		return "-";
	}
}
