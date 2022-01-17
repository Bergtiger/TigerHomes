package de.bergtiger.tigerhomes.cmd;

import de.bergtiger.tigerhomes.TigerHomes;
import de.bergtiger.tigerhomes.bdo.Home;
import de.bergtiger.tigerhomes.dao.TigerConnection;
import de.bergtiger.tigerhomes.exception.NoSQLConnectionException;
import de.bergtiger.tigerhomes.utils.HomeUtils;
import de.bergtiger.tigerhomes.utils.lang.Lang;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static de.bergtiger.tigerhomes.utils.permission.TigerPermission.*;

public class HomeList implements CommandExecutor, TabCompleter {

	public static String CMD = "homes";

	private static HomeList instance;

	public static HomeList inst() {
		if (instance == null)
			instance = new HomeList();
		return instance;
	}

	private HomeList() {}

	@Override
	public boolean onCommand(CommandSender cs, Command cmd, String label, String[] args) {
		Bukkit.getScheduler().runTaskAsynchronously(TigerHomes.inst(), () -> handle(cs, args));
		return false;
	}

	/**
	 * Command: /homes [uuid]
	 * @param cs who performed the command
	 * @param args parameter
	 */
	private void handle(CommandSender cs, String[] args) {
		// check permission
		if (hasPermission(cs, ADMIN, HOME_LIST, HOME_OTHER_LIST)) {
			String uuid = cs.getName();
			// check for arguments
			if (args.length >= 1) {
				uuid = args[0];
				if (!(hasPermission(cs, ADMIN, HOME_OTHER_LIST) || cs.getName().equalsIgnoreCase(uuid))) {
					// Not an allowed player
					cs.spigot().sendMessage(Lang.build(Lang.NOPERMISSION.get()));
					return;
				}
			}
			// load homes
			try {
				List<Home> homes = TigerConnection.getHomeDAO().getHomes(uuid);
				if (homes != null && !homes.isEmpty()) {
					// show homes
					List<TextComponent> messages = new ArrayList<>();

					messages.add(Lang.build(Lang.LIST_HEADER.get().replace(Lang.VALUE, uuid)));
					for (int i = 0; i < homes.size(); i++) {
						// add delimiter
						if (i > 0)
							messages.add(Lang.build(Lang.LIST_DELIMITER.get()));
						// get home
						Home h = homes.get(i);
						// build message
						messages.add(Lang.build(
								Lang.LIST_HOME.get().replace(Lang.VALUE, HomeUtils.name(cs, uuid, h.getName())),
								HomeUtils.cmd(HomeTeleport.CMD, uuid, h.getName()),
								HomeUtils.hover(h),
								null));
					}
					cs.spigot().sendMessage(Lang.combine(messages));
				} else {
					// no homes
					cs.spigot().sendMessage(Lang.build(Lang.LIST_NOHOMES.get()));
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
		// players names
		if (args.length == 1) {
			if (hasPermission(cs, ADMIN, HOME_OTHER_LIST)) {
				try {
					return TigerConnection.getPlayerDAO().getPlayerNames(args[0]);
				} catch (NoSQLConnectionException e) {
					TigerConnection.noConnection();
				}
			}
		}
		return Collections.emptyList();
	}
}
