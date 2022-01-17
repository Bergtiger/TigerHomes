package de.bergtiger.tigerhomes.cmd;

import de.bergtiger.tigerhomes.TigerHomes;
import de.bergtiger.tigerhomes.utils.lang.Lang;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;

import java.util.Collections;
import java.util.List;

import static de.bergtiger.tigerhomes.utils.permission.TigerPermission.*;

public class HomeHelp implements CommandExecutor, TabCompleter {

	public static final String CMD = "homehelp";

	private static HomeHelp instance;

	public static HomeHelp inst() {
		if (instance == null)
			instance = new HomeHelp();
		return instance;
	}

	private HomeHelp() {}

	@Override
	public boolean onCommand(CommandSender cs, Command cmd, String label, String[] args) {
		Bukkit.getScheduler().runTaskAsynchronously(TigerHomes.inst(), () -> handle(cs));
		return false;
	}

	/**
	 * Command: /homehelp
	 * @param cs who performed the command
	 */
	public static void handle(CommandSender cs) {
		if (hasPermissionAny(cs)) {
			// header
			cs.spigot().sendMessage(Lang.build(Lang.HELP_HEADER.get()));
			// home
			if (hasPermission(cs, ADMIN, HOME_TELEPORT, HOME_OTHER_TELEPORT))
				cs.spigot().sendMessage(Lang.build(
						Lang.HELP_HOME.get(),
						null,
						null,
						"/" + HomeTeleport.CMD));
			// home set
			if (hasPermission(cs, ADMIN, HOME_SET, HOME_OTHER_SET))
				cs.spigot().sendMessage(Lang.build(
						Lang.HELP_SET.get(),
						null,
						null,
						"/" + HomeSet.CMD));
			// home list
			if (hasPermission(cs, ADMIN, HOME_LIST, HOME_OTHER_LIST))
				cs.spigot().sendMessage(Lang.build(
						Lang.HELP_LIST.get(),
						null,
						null,
						"/" + HomeList.CMD));
			// home info
			if (hasPermission(cs, ADMIN, HOME_INFO, HOME_OTHER_INFO))
				cs.spigot().sendMessage(Lang.build(
						Lang.HELP_INFO.get(),
						null,
						null,
						"/" + HomeInfo.CMD));
			// home delete
			if (hasPermission(cs, ADMIN, HOME_DELETE, HOME_OTHER_DELETE))
				cs.spigot().sendMessage(Lang.build(
						Lang.HELP_DELETE.get(),
						null,
						null,
						"/" + HomeDelete.CMD));
			// home search
			if (hasPermission(cs, ADMIN, SEARCH))
				cs.spigot().sendMessage(Lang.build(
						Lang.HELP_SEARCH.get(),
						null,
						null,
						"/" + HomeSearch.CMD));
			// home plugin
			if (hasPermission(cs, ADMIN, PLUGIN_INFO, RELOAD))
				cs.spigot().sendMessage(Lang.build(
						Lang.HELP_PLUGIN.get(),
						null,
						null,
						"/" + HomePlugin.CMD));
		} else {
			cs.spigot().sendMessage(Lang.build(Lang.NOPERMISSION.get()));
		}
	}

	@Override
	public List<String> onTabComplete(CommandSender cs, Command cmd, String label, String[] args) {
		return Collections.emptyList();
	}
}
