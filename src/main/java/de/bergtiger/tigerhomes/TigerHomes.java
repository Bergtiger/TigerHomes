package de.bergtiger.tigerhomes;

import de.bergtiger.tigerhomes.cmd.*;
import de.bergtiger.tigerhomes.dao.TigerConnection;
import de.bergtiger.tigerhomes.listener.DeathListener;
import de.bergtiger.tigerhomes.listener.JoinListener;
import de.bergtiger.tigerhomes.listener.PlayerInfoListener;
import de.bergtiger.tigerhomes.listener.TigerListConnectListener;
import de.bergtiger.tigerhomes.utils.TigerLogger;
import de.bergtiger.tigerhomes.utils.config.TigerConfig;
import org.bukkit.Bukkit;
import org.bukkit.event.HandlerList;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

public class TigerHomes extends JavaPlugin {

	private static TigerHomes instance;

	public static TigerHomes inst() {
		return instance;
	}

	@Override
	public void onEnable() {
		// set instance
		instance = this;
		// set logger
		TigerLogger.setLogger(getLogger());
		// load configuration
		TigerConfig.load();
		// start sql
		TigerConnection.inst().reload();
		// add commands
		// set
		getCommand(HomeSet.CMD).setExecutor(HomeSet.inst());
		getCommand(HomeSet.CMD).setTabCompleter(HomeSet.inst());
		// info
		getCommand(HomeInfo.CMD).setExecutor(HomeInfo.inst());
		getCommand(HomeInfo.CMD).setTabCompleter(HomeInfo.inst());
		// list
		getCommand(HomeList.CMD).setExecutor(HomeList.inst());
		getCommand(HomeList.CMD).setTabCompleter(HomeList.inst());
		// delete
		getCommand(HomeDelete.CMD).setExecutor(HomeDelete.inst());
		getCommand(HomeDelete.CMD).setTabCompleter(HomeDelete.inst());
		// teleport
		getCommand(HomeTeleport.CMD).setExecutor(HomeTeleport.inst());
		getCommand(HomeTeleport.CMD).setTabCompleter(HomeTeleport.inst());
		// help
		getCommand(HomeHelp.CMD).setExecutor(HomeHelp.inst());
		getCommand(HomeHelp.CMD).setTabCompleter(HomeHelp.inst());
		// search
		getCommand(HomeSearch.CMD).setExecutor(HomeSearch.inst());
		getCommand(HomeSearch.CMD).setTabCompleter(HomeSearch.inst());
		// plugin
		getCommand(HomePlugin.CMD).setExecutor(HomePlugin.inst());
		getCommand(HomePlugin.CMD).setTabCompleter(HomePlugin.inst());
		// add listener
		handleListener();
	}

	@Override
	public void onDisable() {
		// disable sql connection
		TigerConnection.inst().closeConnection();
	}

	public void handleListener() {
		PluginManager pm = Bukkit.getPluginManager();
		// unregister all listener
		HandlerList.unregisterAll(this);
		// register listener
		if (TigerConfig.inst().getBoolean(TigerConfig.HOME_ON_DEATH))
			pm.registerEvents(DeathListener.inst(), this);
		//
		if (pm.isPluginEnabled("TigerList")) {
			// add player info listener
			pm.registerEvents(PlayerInfoListener.inst(), this);
			pm.registerEvents(TigerListConnectListener.inst(), this);
		} else {
			// add player join listener
			pm.registerEvents(JoinListener.inst(), this);
		}
	}
}
