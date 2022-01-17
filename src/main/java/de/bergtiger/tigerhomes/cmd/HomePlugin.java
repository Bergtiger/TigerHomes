package de.bergtiger.tigerhomes.cmd;

import de.bergtiger.tigerhomes.TigerHomes;
import de.bergtiger.tigerhomes.dao.TigerConnection;
import de.bergtiger.tigerhomes.utils.config.TigerConfig;
import de.bergtiger.tigerhomes.utils.lang.Lang;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import static de.bergtiger.tigerhomes.utils.permission.TigerPermission.*;

public class HomePlugin implements CommandExecutor, TabCompleter {

	public static final String CMD = "tigerhomes", CMD_INFO = "info", CMD_RELOAD = "reload";

	private static HomePlugin instance;

	public static HomePlugin inst() {
		if (instance == null)
			instance = new HomePlugin();
		return instance;
	}

	private HomePlugin() {}

	@Override
	public boolean onCommand(CommandSender cs, Command cmd, String label, String[] args) {
		Bukkit.getScheduler().runTaskAsynchronously(TigerHomes.inst(), () -> handle(cs, args));
		return false;
	}

	/**
	 * Command: /tigerhomes [info/reload]
	 * @param cs who performed the command
	 * @param args parameter
	 */
	private void handle(CommandSender cs, String[] args) {
		if (args.length >= 1) {
			switch (args[0].toLowerCase()) {
				case CMD_INFO -> info(cs);
				case CMD_RELOAD -> reload(cs);
			}
		}
	}

	private void info(CommandSender cs) {
		if (hasPermission(cs, ADMIN, PLUGIN_INFO)) {
			// header
			cs.spigot().sendMessage(Lang.build(Lang.PLUGIN_HEADER.get()
					.replace(Lang.VALUE, TigerHomes.inst().getDescription().getName())));
			// version
			cs.spigot().sendMessage(Lang.build(Lang.PLUGIN_VERSION.get()
					.replace(Lang.VALUE, TigerHomes.inst().getDescription().getVersion())));
			// connection
			if (TigerConnection.isForeignConnection())
				cs.spigot().sendMessage(Lang.build(Lang.PLUGIN_CONNECTION_FOREIGN.get()
						.replace(Lang.VALUE, TigerConnection.hasConnection().toString())));
			else
				cs.spigot().sendMessage(Lang.build(Lang.PLUGIN_CONNECTION_OWN.get()
						.replace(Lang.VALUE, TigerConnection.hasConnection().toString())));
		} else {
			cs.spigot().sendMessage(Lang.build(Lang.NOPERMISSION.get()));
		}
	}

	private void reload(CommandSender cs) {
		if (hasPermission(cs, ADMIN, RELOAD)) {
			// stop connection
			TigerConnection.inst().closeConnection();
			// load configuration
			TigerHomes.inst().reloadConfig();
			TigerConfig.load();
			// start connection
			TigerConnection.inst().reload();
			// listener
			TigerHomes.inst().handleListener();
			// success
			cs.spigot().sendMessage(Lang.build(Lang.RELOAD_SUCCESS.get()));
		} else {
			cs.spigot().sendMessage(Lang.build(Lang.NOPERMISSION.get()));
		}
	}

	@Override
	public List<String> onTabComplete(CommandSender cs, Command cmd, String label, String[] args) {
		if (args.length == 1) {
			List<String> suggestions = new ArrayList<>();
			if (hasPermission(cs, ADMIN, RELOAD))
				suggestions.add(CMD_RELOAD);
			if (hasPermission(cs, ADMIN, PLUGIN_INFO))
				suggestions.add(CMD_INFO);
			return suggestions.stream().filter(s -> s.startsWith(args[0].toLowerCase())).collect(Collectors.toList());
		}
		return Collections.emptyList();
	}
}
