package de.bergtiger.tigerhomes.listener;

import de.bergtiger.tigerhomes.TigerHomes;
import de.bergtiger.tigerhomes.dao.TigerConnection;
import de.bergtiger.tigerhomes.exception.NoSQLConnectionException;
import de.bergtiger.tigerhomes.utils.TigerLogger;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import java.util.logging.Level;

/**
 * Simplified player join handler.
 * Add a player into Database. It does not check for players with same name or update old players.
 */
public class JoinListener implements Listener {

	private static JoinListener instance;

	public static JoinListener inst() {
		if (instance == null)
			instance = new JoinListener();
		return instance;
	}

	private JoinListener() {}

	@EventHandler
	public void onJoin(PlayerJoinEvent e) {
		Bukkit.getScheduler().runTaskAsynchronously(TigerHomes.inst(), () -> handlePlayerJoin(e.getPlayer()));
	}

	/**
	 * Handle player join event.
	 * @param p
	 */
	private void handlePlayerJoin(Player p) {
		try {
			TigerConnection.getPlayerDAO().updatePlayer(p.getName(), p.getUniqueId().toString());
		} catch (NoSQLConnectionException e) {
			TigerLogger.log(Level.SEVERE, String.format("Could not handle player join from %s", p.getName()), e);
			TigerConnection.noConnection();
		}
	}
}
