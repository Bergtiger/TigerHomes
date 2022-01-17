package de.bergtiger.tigerhomes.bdo;

import de.bergtiger.tigerhomes.utils.permission.TigerPermission;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.stream.Collectors;

public class HomePlayer {

	String uuid;
	Long lastTeleport;
	HashMap<String, Integer> maxHomes = new HashMap<>();

	public int getMaxHomes() {
		return 0;
	}

	public int getMaxHomes(String world) {
		return 0;
	}

	private void loadMaxHomes() {
		Player p = getPlayer();
		p.getEffectivePermissions().stream()
				.filter(perm -> perm.getPermission().startsWith(TigerPermission.HOME_LIMIT.get()))
				.map(perm -> perm.getPermission().substring(TigerPermission.HOME_LIMIT.get().length()))
				.forEach(perm -> {
					System.out.println("Permission: " + perm);
					String[] args = perm.split("\\.");
					System.out.println("args: " + args + ", " + args.length);
					if (args.length == 1) {
						// amount
						try {
							int amount = Integer.parseInt(args[0]);
						} catch (NumberFormatException e) {
							// log error
						}
					} else if (args.length == 2) {
						// world // amount
					} else {
						// log error
					}
				});
	}

	private Player getPlayer() {
		return null;
	}
}
