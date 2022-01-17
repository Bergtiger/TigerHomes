package de.bergtiger.tigerhomes.utils.lang;

import de.bergtiger.tigerhomes.cmd.*;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.chat.hover.content.Text;

import java.util.Collection;

public enum Lang implements Constants {

	/** CommandSender has no Permission.*/
	NOPERMISSION
			("&cNo Permission"),
	/** No SQL connection*/
	NOCONNECTION
			("&cNo Connection"),
	/** No such existing home, home_name as VALUE*/
	NOSUCHHOME
			(String.format("&cNo such home &6'&e%s&6'", VALUE)),
	/** Not a Number, VALUE for wrong number value*/
	NONUMBER
			(String.format("&6'&e%s&6' &cis not a number", VALUE)),
	/** Only player can teleport*/
	NOPLAYER
			("&cYou have to be a player to teleport"),
	/** Player did not use a home format*/
	NOHOMEFORMAT
			(String.format("&6'&e%s&6' &cis not a home format. &6[&eplayer&6]&f:&6[&ehome_name&6]", VALUE)),
	/** Need WorldEdit*/
	NOWORLDEDIT
			("&eWorldEdit &cis needed to perform this command"),
	/** Need a selection*/
	NOSELECTION
			("&cYou need a selection to perform this command"),
	/** No complete selection*/
	NOCOMPLETESELECTION
			("&cYou need a complete selection to perform this command"),
	/** More information in no_connection_message, on click repeat command*/
	HOVER_NOCONNECTION
			("&cCould not perform this command. Please try again."),

	/** Time format !WITHOUT COLORS!*/
	FORMAT_TIME
			("dd.MM.yyyy, HH:mm"),
	/** how home should be formatted*/
	FORMAT_HOME
			("&e%s&6:&e%s"),
	/** how unnamed home should be visualized*/
	FORMAT_DEFAULT
			("&odefault"),
	/** how home location should be formatted*/
	FORMAT_LOCATION
			(String.format("&e%s&f, &e%s&f, &e%s&f &e%s&f, &e%s &e%s", X, Y, Z, YAW, PITCH, W)),

	/** insert home success, home_name as VALUE*/
	SET_INSERT
			(String.format("&aCreated a new Home &6'&e%s&6' &aat your location.", VALUE)),
	/** update home success, home_name as VALUE*/
	SET_UPDATE
			(String.format("&aUpdated your Home &6'&e%s&6' &awith your location.", VALUE)),
	/** error on home insert/update, home_name as VALUE*/
	SET_ERROR
			(String.format("&cCould not created Home &6'&e%s&6'", VALUE)),
	/** player has to many homes, amount as VALUE*/
	SET_TO_MANY_HOMES
			(String.format("&cYou can only have &6%s &chomes", VALUE)),

	/** home header, VALUE as player*/
	LIST_HEADER
			(String.format("&a---<&eHomes from &6'&e%s&6'&a>---\n", VALUE)),
	/** home entry in list*/
	LIST_HOME
			(String.format("&e%s", VALUE)),
	/** home delimiter to separate entries*/
	LIST_DELIMITER
			("&6, "),
	/** home hover format,
	 * NAME for LIST_HOVER_NAME,
	 * LOCATION for LIST_HOVER_LOCATION,
	 * CREATED for LIST_HOVER_TIME,
	 * ISSAVE for LIST_HOVER_ISSAVE*/
	LIST_HOVER_FORMAT
			(String.format("%s\n%s\n%s\n%s", NAME, LOCATION, CREATED, ISSAVE)),
	/** hover name, VALUE as name*/
	LIST_HOVER_NAME
			(String.format("&e%s", VALUE)),
	/** hover created time, VALUE as time*/
	LIST_HOVER_TIME
			(String.format("&aCreated&6: %s", VALUE)),
	/** hover location, VALUE as location_format*/
	LIST_HOVER_LOCATION
			(String.format("&aLocation&6: %s", VALUE)),
	/** empty home list*/
	LIST_NOHOMES
			("No Homes"),

	RELOAD_SUCCESS
			("&aReload successful."),
	RELOAD_ERROR
			("&cReload error"),

	PLUGIN_HEADER
			(String.format("&a---<&e%s&a>---", VALUE)),
	PLUGIN_VERSION
			(String.format("&aVersion&6: &e%s", VALUE)),
	PLUGIN_CONNECTION_OWN
			(String.format("&aConnection&6: &e%s", VALUE)),
	PLUGIN_CONNECTION_FOREIGN
			(String.format("&aConnection via &e'&fTigerList&e'&6: &e%s", VALUE)),

	HELP_HEADER
			("&a---<&6TigerHomes&a>---"),
	HELP_HOME
			(String.format("&6/&e%s &f[&eplayer&f]&6:&f[&ehome&f]", HomeTeleport.CMD)),
	HELP_SET
			(String.format("&6/&e%s &f[&eplayer&f]&6:&f[&ehome&f]", HomeSet.CMD)),
	HELP_LIST
			(String.format("&6/&e%s &f[&eplayer&f]", HomeList.CMD)),
	HELP_INFO
			(String.format("&6/&e%s &f[&eplayer&f]&6:&f[&ehome&f]", HomeInfo.CMD)),
	HELP_SEARCH
			(String.format("&6/&e%s &f[&esel&f, &eradius&f]", HomeSearch.CMD)),
	HELP_DELETE
			(String.format("&6/&e%s &f[&eplayer&f]&6:&f[&ehome&f]", HomeDelete.CMD)),
	HELP_PLUGIN
			(String.format("&6/&e%s &f[&einfo&f,&ereload&f]", HomePlugin.CMD)),

	INFO_HEADER
			(String.format("&a---<(&e%s&a)>---", VALUE)),
	INFO_LOCATION
			(String.format("&aLocation&6: &e%s", VALUE)),
	INFO_CREATED
			(String.format("&aCreated&6: &e%s", VALUE)),
	INFO_SAVE
			(String.format("&aisSave&6: &e%s", VALUE)),

	PLAYER_INFO_MESSAGE
			(String.format("&aHomes&6: &e%s", VALUE)),
	PLAYER_INFO_HOVER
			(String.format("&e%s", VALUE)),
	PLAYER_HOVER_MESSAGE
			(String.format("&aHomes&6: &e%s", VALUE)),

	TELEPORT_SUCCESS
			(String.format("&aYou teleported to your Home &6'&e%s&6'", VALUE)),

	/** success of home delete, home_name as VALUE*/
	DELETE_SUCCESS
			(String.format("&aDeleted Home &6'&e%s&6' &asuccessfully.", VALUE)),
	/** error on home delete, home_name as VALUE*/
	DELETE_ERROR
			(String.format("&cCould not delete Home &6'&e%s&6'", VALUE));

	private String message;

	Lang(String message) {
		this.message = message;
	}

	/**
	 * Get message from this Enum
	 * @return String
	 */
	public String get() {
		return message;
	}

	/**
	 * Set message for this Enum
	 * @param message to set
	 */
	public void set(String message) {
		this.message = message;
	}

	/**
	 * Builds a TextComponent with text and colors
	 * @param args - Text
	 * @return TextComponent
	 */
	public static TextComponent build(String args) {
		return build(args, null, null, null);
	}

	/**
	 * Builds a TextComponent with colored text, and extras
	 * @param args - text
	 * @param cmd - onClick command
	 * @param hover - onHover text
	 * @param cmd_suggestion - onClick suggestion
	 * @return TextComponent
	 */
	public static TextComponent build(String args, String cmd, String hover, String cmd_suggestion) {
		if (args != null && !args.isBlank()) {
			// create a TextComponent and colorize
			TextComponent tc = colorize(new TextComponent(args), null);
			// add cmd
			if (cmd != null && !cmd.isBlank())
				tc.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, cmd));
			// add hover
			if (hover != null && !hover.isBlank()) {
				tc.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new Text(new TextComponent[] {colorize(new TextComponent(hover), null)})));
			}
			// add suggestion
			if (cmd_suggestion != null && !cmd_suggestion.isBlank())
				tc.setClickEvent(new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, cmd_suggestion));
			return tc;
		}
		return null;
	}

	/**
	 *
	 * @param tc to convert
	 * @param color prefix color
	 * @return TextComponent
	 */
	private static TextComponent colorize(TextComponent tc, ChatColor color) {
		// set prefix color
		if (color != null)
			tc.setColor(color);
		// get text
		String text = tc.getText();
		// check if text contains hex color
		if(text.contains("&#")) {
			// find first occurrence of hex color
			int i = text.indexOf("&#");
			// substring text before first hex color
			tc.setText(ChatColor.translateAlternateColorCodes('&', text.substring(0, i)));
			// substring text after first hex color
			tc.addExtra(colorize(new TextComponent(text.substring(i + 8)), ChatColor.of(text.substring(i + 1, i + 8))));
		} else {
			// text with legacy ChatColor
			tc.setText(ChatColor.translateAlternateColorCodes('&', text));
		}
		return tc;
	}

	/**
	 * Combines multiple BaseComponents in a single BaseComponents.
	 * @param messages to combine
	 * @return BaseComponent
	 */
	public static BaseComponent combine(Collection<? extends BaseComponent> messages) {
		if (messages != null && !messages.isEmpty()) {
			BaseComponent bc = new TextComponent();
			for (BaseComponent b : messages) {
				bc.addExtra(b);
			}
			return bc;
		}
		return null;
	}
}
