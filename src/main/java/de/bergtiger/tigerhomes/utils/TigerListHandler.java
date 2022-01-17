package de.bergtiger.tigerhomes.utils;

import de.bergtiger.tigerlist.dao.TigerConnection;

import java.sql.Connection;

/**
 * Handles all direct TigerList methods
 */
public class TigerListHandler {

	/**
	 * Get connection from TigerList.
	 * @return Connection
	 */
	public static Connection getConnection() {
		return TigerConnection.conn();
	}

	/**
	 * Pass on noConnection to TigerList.
	 */
	public static void noConnection() {
		TigerConnection.noConnection();
	}
}
