package de.bergtiger.tigerhomes.listener;

import de.bergtiger.tigerhomes.bdo.Home;
import de.bergtiger.tigerhomes.dao.TigerConnection;
import de.bergtiger.tigerhomes.exception.NoSQLConnectionException;
import de.bergtiger.tigerhomes.utils.TigerLogger;
import org.bukkit.Location;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;

import java.util.logging.Level;

public class DeathListener implements Listener {

	private static DeathListener instance;

	public static DeathListener inst() {
		if (instance == null)
			instance = new DeathListener();
		return instance;
	}

	private DeathListener() {}

	@EventHandler
	public void onDeath(PlayerDeathEvent e) {
		// get default home
		try {
			Home home = TigerConnection.getHomeDAO().getHome(e.getEntity().getUniqueId().toString(), "");
			if (home != null) {
				// teleport player
				Location loc = home.getLocation();
				if(loc != null) {
					e.getEntity().teleport(loc);
				} else {
					TigerLogger.log(Level.WARNING, String.format("Could not respawn Player '%s' at his default home, world '%s' not loaded.", e.getEntity().getName(), home.getName()));
				}
			} else {
				TigerLogger.log(Level.INFO, String.format("%s had no default home.", e.getEntity().getName()));
			}
		} catch (NoSQLConnectionException exception) {
			TigerLogger.log(Level.WARNING, String.format("Could not respawn Player '%s' at his default home, no connection", e.getEntity().getName()));
			TigerConnection.noConnection();
		}
	}
}
