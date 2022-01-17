package de.bergtiger.tigerhomes.cmd;

import de.bergtiger.tigerhomes.TigerHomes;
import de.bergtiger.tigerhomes.bdo.Home;
import de.bergtiger.tigerhomes.dao.TigerConnection;
import de.bergtiger.tigerhomes.exception.NoSQLConnectionException;
import de.bergtiger.tigerhomes.utils.HomeUtils;
import de.bergtiger.tigerhomes.utils.TimeUtils;
import de.bergtiger.tigerhomes.utils.lang.Lang;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;

import java.util.List;

import static de.bergtiger.tigerhomes.utils.permission.TigerPermission.*;

public class HomeInfo implements CommandExecutor, TabCompleter {

	public static final String CMD = "homeinfo";

	private static HomeInfo instance;

	public static HomeInfo inst() {
		if (instance == null)
			instance = new HomeInfo();
		return instance;
	}

	private HomeInfo() {}

	@Override
	public boolean onCommand(CommandSender cs, Command cmd, String label, String[] args) {
		Bukkit.getScheduler().runTaskAsynchronously(TigerHomes.inst(), () -> handle(cs, args));
		return false;
	}

	/**
	 * Command: /homeinfo [uuid]:[home]
	 * @param cs who performed the command
	 * @param args parameters
	 */
	private void handle(CommandSender cs, String[] args) {
		// check permission
		if (hasPermission(cs, ADMIN, HOME_INFO, HOME_OTHER_INFO)) {
			// get home
			String uuid = cs.getName();
			String home = "";

			if (args.length > 0) {
				if (args[0].contains(":")) {
					// get uuid and home
					String[] a = args[0].split(":");
					if (a.length == 1) {
						uuid = a[0];
					} else if (a.length == 2) {
						uuid = a[0];
						home = a[1];
					} else {
						// Not a correct home argument
						cs.spigot().sendMessage(Lang.build(Lang.NOHOMEFORMAT.get().replace(Lang.VALUE, args[0])));
						return;
					}
					// check if allowed uuid
					if (!(hasPermission(cs, ADMIN, HOME_OTHER_INFO) || cs.getName().equalsIgnoreCase(uuid))) {
						// Not an allowed player
						cs.spigot().sendMessage(Lang.build(Lang.NOPERMISSION.get()));
						return;
					}
				} else {
					home = args[0];
				}
			}

			try {
				Home h = TigerConnection.getHomeDAO().getHome(uuid, home);
				// show home
				if (h != null) {
					// header
					cs.spigot().sendMessage(Lang.build(Lang.INFO_HEADER.get()
							.replace(Lang.VALUE, HomeUtils.name(cs, uuid, home))));
					// location
					cs.spigot().sendMessage(Lang.build(Lang.INFO_LOCATION.get()
							.replace(Lang.VALUE, Lang.FORMAT_LOCATION.get()
									.replace(Lang.X, HomeUtils.format(h.getX()))
									.replace(Lang.Y, HomeUtils.format(h.getY()))
									.replace(Lang.Z, HomeUtils.format(h.getZ()))
									.replace(Lang.YAW, HomeUtils.format(h.getYaw()))
									.replace(Lang.PITCH, HomeUtils.format(h.getPitch()))
									.replace(Lang.W, h.getW()))));
					// created
					cs.spigot().sendMessage(Lang.build(Lang.INFO_CREATED.get()
							.replace(Lang.VALUE, TimeUtils.formatted(h.getT()))));
				} else {
					cs.spigot().sendMessage(Lang.build(Lang.NOSUCHHOME.get().replace(Lang.VALUE, HomeUtils.name(cs, uuid, home))));
				}
			} catch (NoSQLConnectionException e) {
				cs.spigot().sendMessage(Lang.build(Lang.NOCONNECTION.get()));
				TigerConnection.noConnection();
			}
		} else {
			cs.spigot().sendMessage(Lang.build(Lang.NOPERMISSION.get()));
		}
	}

	@Override
	public List<String> onTabComplete(CommandSender cs, Command cmd, String label, String[] args) {
		return HomeUtils.onTabComplete(cs, args, HOME_INFO, HOME_OTHER_INFO);
	}
}
