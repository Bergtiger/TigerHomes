package de.bergtiger.tigerhomes.dao;

import de.bergtiger.tigerhomes.exception.NoSQLConnectionException;
import de.bergtiger.tigerhomes.utils.TigerLogger;

import java.util.logging.Level;

public interface TableDAO {
	/** player table*/
	String PLAYERS = "players",
	/** homes table*/
	HOMES = "homes",
	/** worlds table*/
	WORLDS = "worlds",
	/** player identification*/
	PID = "pid",
	/** player name as String*/
	NAME = "name",
	/** player uuid as String*/
	UUID = "uuid",
	/** home identification*/
	HID = "hid",
	/** world identification*/
	WID = "wid",
	/** x position as Double*/
	X = "x",
	/** y position as Double*/
	Y = "y",
	/** z position as Double*/
	Z = "z",
	/** Yaw position as Float*/
	YAW = "yaw",
	/** Pitch position as Float*/
	PITCH = "pitch",
	/** Time home was created*/
	TIME_CREATED = "t";

	default void createTables() {
		try {
			// create players
			createPlayers();
			// create worlds
			createWorlds();
			// create homes
			createHomes();
		} catch (NoSQLConnectionException e) {
			TigerLogger.log(Level.SEVERE, "createTables: no sql connection", e);
			TigerConnection.noConnection();
		}
	}

	void createPlayers() throws NoSQLConnectionException;

	void createWorlds() throws NoSQLConnectionException;

	void createHomes() throws NoSQLConnectionException;
}
