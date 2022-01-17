package de.bergtiger.tigerhomes.utils.isSave;

import org.bukkit.Bukkit;
import org.bukkit.Location;

public class IsSaveHandler {

	public static String isSave (Location loc) {
		if (Bukkit.getBukkitVersion().contains("1.17.1"))
			return new IsSave_1_17_1().check(loc);
		return "";
	}
}
