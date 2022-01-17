package de.bergtiger.tigerhomes.listener;

import de.bergtiger.tigerhomes.dao.TigerConnection;
import de.bergtiger.tigerlist.event.TigerListConnectEvent;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class TigerListConnectListener implements Listener {

	private static TigerListConnectListener instance;

	public static TigerListConnectListener inst() {
		if (instance == null)
			instance = new TigerListConnectListener();
		return instance;
	}

	private TigerListConnectListener() {}

	@EventHandler
	public void onConnect(TigerListConnectEvent e) {
		TigerConnection.inst().reload();
	}
}
