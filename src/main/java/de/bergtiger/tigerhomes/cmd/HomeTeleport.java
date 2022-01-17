package de.bergtiger.tigerhomes.cmd;

import de.bergtiger.tigerhomes.TigerHomes;
import de.bergtiger.tigerhomes.bdo.Home;
import de.bergtiger.tigerhomes.dao.TigerConnection;
import de.bergtiger.tigerhomes.exception.NoSQLConnectionException;
import de.bergtiger.tigerhomes.utils.HomeUtils;
import de.bergtiger.tigerhomes.utils.lang.Lang;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

import java.util.List;

import static de.bergtiger.tigerhomes.utils.permission.TigerPermission.*;

public class HomeTeleport implements CommandExecutor, TabCompleter {

	public static String CMD = "home";

	private static HomeTeleport instance;

	public static HomeTeleport inst() {
		if (instance == null)
			instance = new HomeTeleport();
		return instance;
	}

	private HomeTeleport() {}

	@Override
	public boolean onCommand(CommandSender cs, Command cmd, String label, String[] args) {
		Bukkit.getScheduler().runTaskAsynchronously(TigerHomes.inst(), () -> handle(cs, args));
		return false;
	}

	/**
	 * Command: /home [uuid]:[home]
	 * @param cs who performed the command
	 * @param args parameters
	 */
	private void handle(CommandSender cs, String[] args) {
		if (cs instanceof Player) {
			// check permission
			if (hasPermission(cs, ADMIN, HOME_TELEPORT, HOME_OTHER_TELEPORT)) {
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
						if (!(hasPermission(cs, ADMIN, HOME_OTHER_TELEPORT) || cs.getName().equalsIgnoreCase(uuid))) {
							// Not an allowed player
							cs.spigot().sendMessage(Lang.build(Lang.NOPERMISSION.get()));
							return;
						}
					} else {
						home = args[0];
					}
				}
				try {
					// load home
					Home h = TigerConnection.getHomeDAO().getHome(uuid, home);
					// teleport
					if (h != null) {
						// teleport to location (with delay)
						teleport((Player)cs, h);
					} else {
						// no such home
						cs.spigot().sendMessage(Lang.build(Lang.NOSUCHHOME.get().replace(Lang.VALUE, HomeUtils.name(cs, uuid, home))));
					}
				} catch (NoSQLConnectionException e) {
					cs.spigot().sendMessage(Lang.build(Lang.NOCONNECTION.get()));
					TigerConnection.noConnection();
				}
			} else {
				cs.spigot().sendMessage(Lang.build(Lang.NOPERMISSION.get()));
			}
		} else {
			// Have to be a player
			cs.spigot().sendMessage(Lang.build(Lang.NOPLAYER.get()));
		}
	}

	/**
	 * Teleport a player with its vehicle and passengers.
	 * @param p to teleport
	 * @param home to teleport to
	 */
	private void teleport(Player p, Home home) {
		if (home.getLocation() != null) {
			Bukkit.getScheduler().runTask(TigerHomes.inst(), () -> {
				teleportVehicle(p, home.getLocation());
				p.spigot().sendMessage(Lang.build(Lang.TELEPORT_SUCCESS.get().replace(Lang.VALUE, HomeUtils.name(p, home.getUuid(), home.getName()))));
			});
		} else {
			p.spigot().sendMessage(Lang.build("&cCould not find your destination."));
		}
	}

	/**
	 * Recursive teleport of vehicle.
	 * @param e vehicle riding
	 * @param loc to teleport to
	 */
	private void teleportVehicle(Entity e, Location loc) {
		if (e.getVehicle() != null) {
			//System.out.println("vehicle " + e.getType());
			// recursive teleport vehicle
			teleportVehicle(e.getVehicle(), loc);
		} else {
			// last vehicle first to teleport
			List<Entity> passengers = e.getPassengers();
			e.eject();
			e.teleport(loc);
			passengers.forEach(e::addPassenger);
			//System.out.println("vehicle teleported " + e.getType());
		}
	}

	@Override
	public List<String> onTabComplete(CommandSender cs, Command cmd, String label, String[] args) {
		return HomeUtils.onTabComplete(cs, args, HOME_TELEPORT, HOME_OTHER_TELEPORT);
	}
}
