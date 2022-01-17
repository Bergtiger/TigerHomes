package de.bergtiger.tigerhomes.utils.permission;

import org.bukkit.command.CommandSender;

public enum TigerPermission {

    ADMIN		("tigerhomes.admin"),

	HOME_SET			("tigerhomes.home.set"),
	HOME_OTHER_SET		("tigerhomes.home.set.other"),

	HOME_DELETE			("tigerhomes.home.del"),
	HOME_OTHER_DELETE	("tigerhomes.home.del.other"),

	HOME_LIST			("tigerhomes.home.list"),
	HOME_OTHER_LIST		("tigerhomes.home.list.other"),

	HOME_INFO			("tigerhomes.home.info"),
	HOME_OTHER_INFO		("tigerhomes.home.info.other"),

	HOME_LIMIT			("tigerhomes.home.limit."),
	HOME_LIMIT_NONE		("tigerhomes.home.limit.none"),

	HOME_COOLDOWN		("tigerhomes.home.cooldown"),
	HOME_COOLDOWN_NONE	("tigerhomes.home.cooldown.none"),

	HOME_WARMUP			("tigerhomes.home.warmup"),
	HOME_WARMUP_NONE	("tigerhomes.home.warmup.none"),

	HOME_TELEPORT		("tigerhomes.home.teleport"),
	HOME_OTHER_TELEPORT	("tigerhomes.home.teleport.other"),

	SEARCH				("tigerhomes.search"),
	RELOAD				("tigerhomes.reload"),
	PLUGIN_INFO			("tigerhomes.plugin");

    private final String permission;

    TigerPermission(String permission) {this.permission = permission; }

    /**
     * get permission string
     * @return permission
     */
    public String get() {return permission; }

    /**
     * check if CommandSender has permission
     *
     * @param cs {@link CommandSender}
     * @param permission {@link TigerPermission} to check
     * @return true if cs has any of the permissions
     */
    public static boolean hasPermission(CommandSender cs, TigerPermission...permission) {
        if (cs != null && permission != null) {
            for(TigerPermission p : permission) {
                if(cs.hasPermission(p.permission))
                    return true;
            }
        }
        return false;
    }

	public static boolean hasPermissionAny(CommandSender cs) {
		if (cs != null) {
			for (TigerPermission p : TigerPermission.values()) {
				if (cs.hasPermission(p.permission))
					return true;
			}
		}
		return false;
	}
}
