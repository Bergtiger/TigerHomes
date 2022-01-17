package de.bergtiger.tigerhomes.cmd;

import de.bergtiger.tigerhomes.TigerHomes;
import de.bergtiger.tigerhomes.bdo.Home;
import de.bergtiger.tigerhomes.dao.TigerConnection;
import de.bergtiger.tigerhomes.exception.NoSQLConnectionException;
import de.bergtiger.tigerhomes.utils.HomeUtils;
import de.bergtiger.tigerhomes.utils.lang.Lang;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.bukkit.permissions.PermissionAttachmentInfo;

import java.util.List;
import java.util.NoSuchElementException;

import static de.bergtiger.tigerhomes.utils.permission.TigerPermission.*;

public class HomeSet implements CommandExecutor, TabCompleter {

	public static String CMD = "homeset";

	private static HomeSet instance;

	public static HomeSet inst() {
		if (instance == null)
			instance = new HomeSet();
		return instance;
	}

	private HomeSet() {}

	@Override
	public boolean onCommand(CommandSender cs, Command cmd, String label, String[] args) {
		Bukkit.getScheduler().runTaskAsynchronously(TigerHomes.inst(), () -> handle(cs, args));
		return false;
	}

	/**
	 * Command: /homeset [uuid]:[home]
	 * @param cs who performed the command
	 * @param args parameters
	 */
	private void handle(CommandSender cs, String[] args) {
		if (cs instanceof Player) {
			// check permission
			if (hasPermission(cs, ADMIN, HOME_SET, HOME_OTHER_SET)) {
				// get data
				String uuid = cs.getName();
				String home = "";
				// get named home
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
						if (!(hasPermission(cs, ADMIN, HOME_OTHER_SET) || cs.getName().equalsIgnoreCase(uuid))) {
							// Not an allowed player
							cs.spigot().sendMessage(Lang.build(Lang.NOPERMISSION.get()));
							return;
						}
					} else {
						home = args[0];
					}
				}

				// TODO check not allowed home names ?

				try {
					// get amount of homes
					Integer amount = TigerConnection.getHomeDAO().countHomes(uuid, home);
					// check allowed amount
					// Check with limit
					// Get Limit
					Integer limit = 0;
					try {
						limit = cs.getEffectivePermissions().parallelStream()
								.map(PermissionAttachmentInfo::getPermission)
								.filter(p -> p.contains(HOME_LIMIT.get()))
								.mapToInt(p -> {
									try {
										String[] s = p.split("\\.");
										return Integer.parseInt(s[s.length - 1]);
									} catch (NumberFormatException ignored) {
									}
									return 0;
								}).max().getAsInt();
					} catch (NoSuchElementException ignored) {
					}
					// check if player can add a new home
					if (amount < limit || hasPermission(cs, ADMIN, HOME_LIMIT_NONE)) {
						// create home
						Home h = new Home(uuid, home, ((Player) cs).getLocation());
						// save home
						Integer id = TigerConnection.getHomeDAO().updateHome(uuid, h);
						// check success
						if (id != null) {
							if (id > 0) {
								// insert new
								cs.spigot().sendMessage(Lang.build(Lang.SET_INSERT.get().replace(Lang.VALUE, HomeUtils.name(cs, uuid, home))));
							} else {
								// updated old
								cs.spigot().sendMessage(Lang.build(Lang.SET_UPDATE.get().replace(Lang.VALUE, HomeUtils.name(cs, uuid, home))));
							}
						} else {
							// something went wrong
							cs.spigot().sendMessage(Lang.build(Lang.SET_ERROR.get().replace(Lang.VALUE, HomeUtils.name(cs, uuid, home))));
						}
					} else {
						// to many homes
						cs.spigot().sendMessage(Lang.build(Lang.SET_TO_MANY_HOMES.get().replace(Lang.VALUE, limit.toString())));
					}
				} catch (NoSQLConnectionException e) {
					cs.spigot().sendMessage(Lang.build(Lang.NOCONNECTION.get()));
					TigerConnection.noConnection();
				}
			} else {
				cs.spigot().sendMessage(Lang.build(Lang.NOPERMISSION.get()));
			}
		} else {
			// not a player
			cs.spigot().sendMessage(Lang.build(Lang.NOPLAYER.get()));
		}
	}

	@Override
	public List<String> onTabComplete(CommandSender cs, Command cmd, String label, String[] args) {
		return HomeUtils.onTabComplete(cs, args, HOME_SET, HOME_OTHER_SET);
	}
}
