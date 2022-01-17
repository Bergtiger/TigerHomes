package de.bergtiger.tigerhomes.cmd;

import de.bergtiger.tigerhomes.TigerHomes;
import de.bergtiger.tigerhomes.bdo.Home;
import de.bergtiger.tigerhomes.dao.TigerConnection;
import de.bergtiger.tigerhomes.exception.IncompleteRegionException;
import de.bergtiger.tigerhomes.exception.NoRegionException;
import de.bergtiger.tigerhomes.exception.NoSQLConnectionException;
import de.bergtiger.tigerhomes.utils.lang.Lang;
import de.bergtiger.tigerhomes.utils.worldEdit.WorldEditHandler;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import static de.bergtiger.tigerhomes.utils.permission.TigerPermission.*;

// TODO show, delete, list, -v

public class HomeSearch implements CommandExecutor, TabCompleter {

	public static final String
			CMD = "homesearch",
			CMD_SELECT = "sel",
			CMD_RADIUS = "radius",
			PARAM_SHOW = "-s",
			PARAM_DELETE = "-d",
			PARAM_LIST = "-l",
			PARAM_VERTICAL = "-v";

	private static HomeSearch instance;

	public static HomeSearch inst() {
		if (instance == null)
			instance = new HomeSearch();
		return instance;
	}

	private HomeSearch() {}

	@Override
	public boolean onCommand(CommandSender cs, Command cmd, String label, String[] args) {
		Bukkit.getScheduler().runTaskAsynchronously(TigerHomes.inst(), () -> handle(cs, args));
		return false;
	}

	/**
	 *
	 * @param cs who performed the command
	 * @param args parameter
	 */
	private void handle(CommandSender cs, String[] args) {
		if (hasPermission(cs, ADMIN, SEARCH)) {
			if (cs instanceof Player) {
				if (args.length >= 1) {

					boolean
							ignoreVertical = true,
							showHomesInGame = false,
							deleteHomes = false,
							showHomesInChat = false;

					for (String s : args) {
						switch (s.toLowerCase()) {
							case PARAM_DELETE -> deleteHomes = true;
							case PARAM_LIST -> showHomesInChat = true;
							case PARAM_SHOW -> showHomesInGame = true;
							case PARAM_VERTICAL -> ignoreVertical = false;
						}
					}

					switch (args[0].toLowerCase()) {
						case CMD_SELECT -> {
							{
								// check WorldEdit
								if (Bukkit.getPluginManager().isPluginEnabled("WorldEdit")) {
									try {
										showHomeList(
												cs,
												WorldEditHandler.getHomes((Player) cs, ignoreVertical),
												showHomesInChat,
												showHomesInGame,
												deleteHomes);
									} catch (NoSQLConnectionException e) {
										// no connection
										// TODO on click repeat command
										cs.spigot().sendMessage(Lang.build(Lang.NOCONNECTION.get()));
										TigerConnection.noConnection();
									} catch (NoRegionException e) {
										cs.spigot().sendMessage(Lang.build(Lang.NOSELECTION.get()));
									} catch (IncompleteRegionException e) {
										cs.spigot().sendMessage(Lang.build(Lang.NOCOMPLETESELECTION.get()));
									}
								} else {
									cs.spigot().sendMessage(Lang.build(Lang.NOWORLDEDIT.get()));
								}
							}
						}
						case CMD_RADIUS -> {
							{
								// get radius
								// set Points
								int radius = 25;
								if (args.length >= 2) {
									try {
										radius = Integer.parseInt(args[1]);
									} catch (NumberFormatException e) {
										cs.spigot().sendMessage(Lang.build(Lang.NONUMBER.get().replace(Lang.VALUE, args[1])));
										return;
									}
								}
								Location l = ((Player) cs).getLocation();
								try {
									showHomeList(cs, TigerConnection.getHomeDAO().searchHomes(
											l.getWorld().getName(),
											l.getX() - radius,
											l.getX() + radius,
											ignoreVertical ?   0 : l.getY() - radius,
											ignoreVertical ? 255 : l.getY() + radius,
											l.getZ() - radius,
											l.getZ() + radius),
											showHomesInChat,
											showHomesInGame,
											deleteHomes);
								} catch (NoSQLConnectionException e) {
									// no connection
									// TODO on click repeat command
									cs.spigot().sendMessage(Lang.build(Lang.NOCONNECTION.get()));
								}
							}
						}
						default -> {
							// Help
						}
					}
				}
			} else {
				cs.spigot().sendMessage(Lang.build(Lang.NOPLAYER.get()));
			}
		} else {
			cs.spigot().sendMessage(Lang.build(Lang.NOPERMISSION.get()));
		}
	}

	private void showHomeList(CommandSender cs, List<Home> homes, boolean showHomesInChat, boolean showHomesInGame, boolean deleteHomes) {
		if (homes != null && !homes.isEmpty()) {
			System.out.println("homes: " + homes.size());
			// header
			// amount
			// showHomesInChat
			// showHomesInGame
			// deleteHomes
		} else {
			cs.spigot().sendMessage(Lang.build(Lang.LIST_NOHOMES.get()));
		}
	}

	@Override
	public List<String> onTabComplete(CommandSender cs, Command cmd, String label, String[] args) {
		if (hasPermission(cs, ADMIN, SEARCH)) {
			List<String> suggestions = new ArrayList<>();
			if (args.length == 1) {
				suggestions.add(CMD_SELECT);
				suggestions.add(CMD_RADIUS);
				return suggestions.stream().filter(s -> s.startsWith(args[0])).collect(Collectors.toList());
			} else if (args.length > 1) {
				if (Arrays.stream(args).noneMatch(s -> s.equalsIgnoreCase("-v")))
					suggestions.add("-v");	// ignore vertical
				return suggestions.stream().filter(s -> s.startsWith(args[1])).collect(Collectors.toList());
			}
		}
		return Collections.emptyList();
	}
}
