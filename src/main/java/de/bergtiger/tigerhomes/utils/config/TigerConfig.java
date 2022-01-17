package de.bergtiger.tigerhomes.utils.config;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.logging.Level;

import de.bergtiger.tigerhomes.TigerHomes;
import de.bergtiger.tigerhomes.utils.TigerLogger;
import de.bergtiger.tigerhomes.utils.lang.Lang;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

public class TigerConfig {

	private static final String
		CONFIG = "config",
		DB = "database";
	public static final String
		HOST = DB + ".host",
		PORT = DB + ".port",
		USER = DB + ".user",
		PASSWORD = DB + ".password",
		DATABASE = DB + ".database",

		HOME_ON_DEATH = CONFIG + ".homeOnDeath",

		PAGE_SIZE = CONFIG + ".page.size";
	
	private static TigerConfig instance;

	public static TigerConfig inst() {
		if (instance == null)
			instance = new TigerConfig();
		return instance;
	}

	private TigerConfig() {
	}
	
	public static void load() {
		TigerConfig tc = inst();
		tc.handleConfiguration();
		tc.handleLanguage();
	}
	
	private void handleConfiguration() {
		// empty cache
		values.clear();
		// config
		FileConfiguration cfg = TigerHomes.inst().getConfig();
		// page_size
		if (cfg.contains(PAGE_SIZE)) {
			// check matching integer (\\d*)
			if(!cfg.getString(PAGE_SIZE).matches("[1-9]\\d*")) {
				TigerLogger.log(
						Level.WARNING, 
						String.format("%s has to be a positive integer. Found '%s' and set to default '15'", PAGE_SIZE, cfg.getString(PAGE_SIZE)));
				cfg.set(PAGE_SIZE, 15);
			}
		} else {
			cfg.addDefault(PAGE_SIZE, 15);
		}
		// sql
		// database
		if (!cfg.contains(DATABASE)) {
			cfg.addDefault(DATABASE, "database");
		}
		// host
		if (!cfg.contains(HOST)) {
			cfg.addDefault(HOST, "localhost");
		}
		// port
		if (!cfg.contains(PORT)) {
			cfg.addDefault(PORT, 3306);
		}
		// user
		if (!cfg.contains(USER)) {
			cfg.addDefault(USER, "user");
		}
		// password
		if (!cfg.contains(PASSWORD)) {
			cfg.addDefault(PASSWORD, "password");
		}
		// teleport to home on death
		if (cfg.contains(HOME_ON_DEATH)) {
			if (!cfg.getString(HOME_ON_DEATH).matches("(?i)(true|false)")) {
				TigerLogger.log(
						Level.WARNING,
						String.format("%s has to be a boolean. Found '%s' and set to default 'true'", HOME_ON_DEATH, cfg.getString(HOME_ON_DEATH)));
				cfg.set(HOME_ON_DEATH, true);
			}
		} else {
			cfg.addDefault(HOME_ON_DEATH, true);
		}
		// save
		cfg.options().header("TigerList");
		cfg.options().copyHeader(true);
		cfg.options().copyDefaults(true);
		TigerHomes.inst().saveConfig();
	}
	
	/**
	 * load and save language file.
	 */
	private void handleLanguage() {
		try {
			// language file
			File file = new File(TigerHomes.inst().getDataFolder(), "lang.yml");
			YamlConfiguration cfg = YamlConfiguration.loadConfiguration(file);
			// path for each enum
			String path;
			// go threw each enum
			for (Lang l : Lang.values()) {
				path = l.name().replaceAll("_", ".");
				// if enum exists load, else save
				if (cfg.contains(path))
					l.set(cfg.getString(path));
				else
					cfg.addDefault(path, l.get());
			}
			// options
			cfg.options().header("Language file for DailyJoin");
			cfg.options().copyHeader(true);
			cfg.options().copyDefaults(true);
			// save file
			cfg.save(file);
		} catch (IOException e) {
			TigerLogger.log(Level.SEVERE, "handleLanguage: could not save language file", e);
		}
	}
	
	private final HashMap<String, String> values = new HashMap<>();

	/**
	 * check if configuration has a value.
	 * @param key identifier
	 * @return true if configuration contains this key
	 */
	public boolean hasValue(String key) {
		if (values.containsKey(key)) {
			return true;
		}
		return TigerHomes.inst().getConfig().contains(key);
	}
	
	/**
	 * get string value from configuration.
	 * @param key identifier
	 * @return value as string
	 */
	public String getValue(String key) {
		if (!values.containsKey(key)) {
			values.put(key, TigerHomes.inst().getConfig().getString(key));
		}
		return values.get(key);
	}

	/**
	 * get string value from configuration
	 * @param key identifier
	 * @return value as string not null
	 */
	public String getValueSave(String key) {
		String value = getValue(key);
		return value != null ? value : "";
	}

	/**
	 * get boolean value from configuration.
	 * @param key identifier
	 * @return value as boolean
	 */
	public boolean getBoolean(String key) {
		return Boolean.parseBoolean(getValue(key));
	}

	/**
	 * get integer value from configuration.
	 * @param key identifier
	 * @return value as integer
	 */
	public Integer getInteger(String key) {
		try {
			return Integer.valueOf(getValue(key));
		} catch (NumberFormatException e) {
			TigerLogger.log(Level.SEVERE,
					String.format("getInteger(%s) got an exception please check your configuration.", key), e);
		}
		return null;
	}
}
