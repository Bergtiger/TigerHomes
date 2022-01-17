package de.bergtiger.tigerhomes.cmd;

import de.bergtiger.tigerhomes.TigerHomes;
import de.bergtiger.tigerhomes.dao.TigerConnection;
import de.bergtiger.tigerhomes.exception.NoSQLConnectionException;
import de.bergtiger.tigerhomes.utils.HomeUtils;
import de.bergtiger.tigerhomes.utils.lang.Lang;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;

import java.util.List;

import static de.bergtiger.tigerhomes.utils.permission.TigerPermission.*;

public class HomeDelete implements CommandExecutor, TabCompleter {

	public static String CMD = "homedel";

	private static HomeDelete instance;

	public static HomeDelete inst() {
		if (instance == null)
			instance = new HomeDelete();
		return instance;
	}

	private HomeDelete() {}

	@Override
	public boolean onCommand(CommandSender cs, Command cmd, String label, String[] args) {
		Bukkit.getScheduler().runTaskAsynchronously(TigerHomes.inst(), () -> handle(cs, args));
		return false;
	}

	/**
	 * Command: /homedel [uuid]:[home]
	 * @param cs who performed the command
	 * @param args parameters
	 */
	private void handle(CommandSender cs, String[] args) {
		// check permission
		if (hasPermission(cs, ADMIN, HOME_DELETE, HOME_OTHER_DELETE)) {
			// get data
			String home = "";
			String uuid = cs.getName();

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
					if (!(hasPermission(cs, ADMIN, HOME_OTHER_DELETE) || cs.getName().equalsIgnoreCase(uuid))) {
						// Not an allowed player
						cs.spigot().sendMessage(Lang.build(Lang.NOPERMISSION.get()));
						return;
					}
				} else {
					home = args[0];
				}
			}
			try {
				// remove from database
				Integer i = TigerConnection.getHomeDAO().deleteHome(uuid, home);
				// check success
				if (i != null) {
					if (i > 0) {
						// successful deleted home
						cs.spigot().sendMessage(Lang.build(
								Lang.DELETE_SUCCESS.get().replace(Lang.VALUE, HomeUtils.name(cs, uuid, home))));
					} else {
						// no such home
						cs.spigot().sendMessage(Lang.build(
								Lang.NOSUCHHOME.get().replace(Lang.VALUE, HomeUtils.name(cs, uuid, home))));
					}
				} else {
					// something went wrong
					cs.spigot().sendMessage(Lang.build(
							Lang.DELETE_ERROR.get().replace(Lang.VALUE, HomeUtils.name(cs, uuid, home))));
				}
			} catch (NoSQLConnectionException e) {
				cs.spigot().sendMessage(Lang.build(
						Lang.NOCONNECTION.get(),
						HomeUtils.cmd(CMD, args),
						Lang.HOVER_NOCONNECTION.get(),
						null));
				TigerConnection.noConnection();
			}
		} else {
			cs.spigot().sendMessage(Lang.build(Lang.NOPERMISSION.get()));
		}
	}

	@Override
	public List<String> onTabComplete(CommandSender cs, Command cmd, String label, String[] args) {
		return HomeUtils.onTabComplete(cs, args, HOME_DELETE, HOME_OTHER_DELETE);
	}
}
