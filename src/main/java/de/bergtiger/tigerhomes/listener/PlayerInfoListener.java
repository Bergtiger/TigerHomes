package de.bergtiger.tigerhomes.listener;

import de.bergtiger.tigerhomes.dao.TigerConnection;
import de.bergtiger.tigerhomes.exception.NoSQLConnectionException;
import de.bergtiger.tigerhomes.utils.TigerLogger;
import de.bergtiger.tigerlist.event.PlayerHoverEvent;
import de.bergtiger.tigerlist.event.PlayerInfoEvent;
import de.bergtiger.tigerhomes.utils.lang.Lang;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import java.util.List;
import java.util.logging.Level;
import java.util.stream.Collectors;

/**
 * Handles TigerList player information commands.
 */
public class PlayerInfoListener implements Listener {

	private static PlayerInfoListener instance;

	public static PlayerInfoListener inst() {
		if (instance == null)
			instance = new PlayerInfoListener();
		return instance;
	}

	private PlayerInfoListener() {}

	@EventHandler
	public void onPlayerInfo(PlayerInfoEvent event) {
		if (!event.isCancelled()) {
			try {
				// load data
				List<String> homes = TigerConnection.getHomeDAO().getHomeNames(event.getPlayer().getUuid(), null);
				if (homes != null) {
					// create TextComponent
					if (!homes.isEmpty()) {
						// has homes
						event.addMessage(Lang.build(
								Lang.PLAYER_INFO_MESSAGE.get().replace(Lang.VALUE, Integer.toString(homes.size())),
								"/homes " + event.getPlayer().getName(),
								buildHover(homes),
								null));
					} else {
						// has no homes
						event.addMessage(Lang.build(
								Lang.PLAYER_INFO_MESSAGE.get().replace(Lang.VALUE, "0"),
								"/homes " + event.getPlayer().getName(),
								null,
								null));
					}
				}
			} catch (NoSQLConnectionException e) {
				TigerLogger.log(Level.SEVERE, String.format(
						"Could not load homes for PlayerInfoEvent from Player '%s'", event.getPlayer().getName()), e);
			}
		}
	}

	private String buildHover(List<String> homes) {
		return homes.stream()
				.map(s -> Lang.PLAYER_INFO_HOVER.get().replace(Lang.VALUE, s))
				.collect(Collectors.joining(",\n"));
	}

	@EventHandler
	public void onPlayerHover(PlayerHoverEvent event) {
		if (!event.isCancelled()) {
			try {
				// load data, only amount
				Integer amount = TigerConnection.getHomeDAO().countHomes(event.getPlayer().getUuid());
				if (amount != null) {
					// create TextComponent
					event.addMessage(Lang.build(Lang.PLAYER_HOVER_MESSAGE.get().replace(Lang.VALUE, amount.toString())));
				}
			} catch (NoSQLConnectionException e) {
				TigerLogger.log(Level.SEVERE, String.format(
						"Could not load homes for PlayerHoverEvent from Player '%s'", event.getPlayer().getName()),e);
			}
		}
	}
}
