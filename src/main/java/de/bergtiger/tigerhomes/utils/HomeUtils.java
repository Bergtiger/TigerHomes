package de.bergtiger.tigerhomes.utils;

import de.bergtiger.tigerhomes.bdo.Home;
import de.bergtiger.tigerhomes.dao.TigerConnection;
import de.bergtiger.tigerhomes.exception.NoSQLConnectionException;
import de.bergtiger.tigerhomes.utils.isSave.IsSaveHandler;
import de.bergtiger.tigerhomes.utils.lang.Lang;
import de.bergtiger.tigerhomes.utils.permission.TigerPermission;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import static de.bergtiger.tigerhomes.utils.permission.TigerPermission.*;

public class HomeUtils {

	/**
	 * Build hover for a home.
	 * @param home to format
	 * @return String
	 */
	public static String hover(Home home) {
		if (home != null) {
			return Lang.LIST_HOVER_FORMAT.get()
					.replace(Lang.NAME, home.getName().isBlank() ? Lang.FORMAT_DEFAULT.get() : home.getName())
					.replace(Lang.LOCATION, Lang.FORMAT_LOCATION.get()
						.replace(Lang.X, format(home.getX()))
						.replace(Lang.Y, format(home.getY()))
						.replace(Lang.Z, format(home.getZ()))
						.replace(Lang.YAW, format(home.getYaw()))
						.replace(Lang.PITCH, format(home.getPitch()))
						.replace(Lang.W, home.getW()))
					.replace(Lang.CREATED, TimeUtils.formatted(home.getT()))
					.replace(Lang.ISSAVE, isSave(home));
		}
		return null;
	}

	/**
	 * Format Double to String value.
	 * @param d to format
	 * @return String
	 */
	public static String format(Double d) {
		return d != null ? Integer.toString(d.intValue()) : "-";
	}

	/**
	 * Format Float to String value.
	 * @param f to format
	 * @return String
	 */
	public static String format(Float f) {
		return f != null ? Integer.toString(f.intValue()) : "-";
	}

	/**
	 * Check what is at home location.
	 * @param home to check
	 * @return String
	 */
	public static String isSave(Home home) {
		if (home != null) {
			return IsSaveHandler.isSave(home.getLocation());
		}
		return null;
	}

	/**
	 * Format home for chat.
	 * @param cs who performed the command
	 * @param uuid owner of the home
	 * @param home name of the home
	 * @return name of home formatted
	 */
	public static String name(CommandSender cs, String uuid, String home) {
		if (cs != null && uuid != null && !uuid.isBlank() && home != null) {
			if (home.isBlank())
				home = Lang.FORMAT_DEFAULT.get();
			return (cs.getName().equalsIgnoreCase(uuid) ||
					((cs instanceof Player) && ((Player)cs).getUniqueId().toString().equalsIgnoreCase(uuid))) ?
					home : String.format(Lang.FORMAT_HOME.get(), uuid, home);
		}
		return "";
	}

	public static String cmd(String cmd, String uuid, String home) {
		return "/" + cmd + " " + (uuid != null && !uuid.isBlank() ? (uuid + ":") : "") + home;
	}

	public static String cmd(String cmd, String[] args) {
		return "/" + cmd + " " + argsToString(args);
	}

	public static String argsToString(String[] args) {
		return (args != null && args.length > 0) ? String.join(" ", args) : "";
	}

	/**
	 *
	 * @param cs
	 * @param args
	 * @param me
	 * @param other
	 * @return
	 */
	public static List<String> onTabComplete(CommandSender cs, String[] args, TigerPermission me, TigerPermission other) {
		if (args.length == 1 && hasPermission(cs, ADMIN, me, other)) {
			String uuid = cs.getName();
			String home = args[0];

			if (args[0].contains(":")) {
				// get uuid and home
				String[] a = args[0].split(":");
				if (a.length == 1) {
					uuid = a[0];
				} else if (a.length == 2) {
					uuid = a[0];
					home = a[1];
				} else {
					return Collections.emptyList();
				}
				// check if allowed uuid
				if (!(hasPermission(cs, ADMIN, other) || cs.getName().equalsIgnoreCase(uuid))) {
					// Not an allowed player
					return Collections.emptyList();
				}
				try {
					// search
					List<String> names = TigerConnection.getHomeDAO().getHomeNames(uuid, home);
					if (names != null && !names.isEmpty()) {
						String finalUuid = uuid;
						return names.stream().map(s -> (finalUuid + ":" + s)).collect(Collectors.toList());
					}
				} catch (NoSQLConnectionException e) {
					TigerConnection.noConnection();
				}
			} else {
				try {
					// search
					List<String> names = TigerConnection.getHomeDAO().getHomeNames(uuid, home);
					if (names != null && !names.isEmpty()) {
						return names;
					}
				} catch (NoSQLConnectionException e) {
					TigerConnection.noConnection();
				}
			}
		}
		return Collections.emptyList();
	}
}
