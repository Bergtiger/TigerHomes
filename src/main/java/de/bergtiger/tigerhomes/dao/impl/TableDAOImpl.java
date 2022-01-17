package de.bergtiger.tigerhomes.dao.impl;

import de.bergtiger.tigerhomes.dao.TableDAO;
import de.bergtiger.tigerhomes.dao.TigerConnection;
import de.bergtiger.tigerhomes.exception.NoSQLConnectionException;
import de.bergtiger.tigerhomes.utils.TigerLogger;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.logging.Level;

public class TableDAOImpl implements TableDAO {

	@Override
	public void createPlayers() throws NoSQLConnectionException {
		if (TigerConnection.hasConnection()) {
			PreparedStatement st = null;
			try {
				st = TigerConnection.conn().prepareStatement(String.format(
						"CREATE TABLE IF NOT EXISTS %s (" +
						"%s INT UNSIGNED AUTO_INCREMENT PRIMARY KEY, " +
						"%s VARCHAR(16) NOT NULL UNIQUE, " +
						"%s CHAR(36) NOT NULL UNIQUE)",
						PLAYERS,
						PID,
						NAME,
						UUID));
				st.executeUpdate();
			} catch (SQLException e) {
				TigerLogger.log(Level.SEVERE, "Could not create players.", e);
			} finally {
				TigerConnection.closeResources(null, st);
			}
		} else {
			throw new NoSQLConnectionException();
		}
	}

	@Override
	public void createWorlds() throws NoSQLConnectionException {
		if (TigerConnection.hasConnection()) {
			PreparedStatement st = null;
			try {
				st = TigerConnection.conn().prepareStatement(String.format(
						"CREATE TABLE IF NOT EXISTS %s (" +
						"%s INT UNSIGNED AUTO_INCREMENT PRIMARY KEY, " +
						"%s VARCHAR(255) NOT NULL UNIQUE)",
						WORLDS,
						WID,
						NAME));
				st.executeUpdate();
			} catch (SQLException e) {
				TigerLogger.log(Level.SEVERE, "Could not create worlds.", e);
			} finally {
				TigerConnection.closeResources(null, st);
			}
		} else {
			throw new NoSQLConnectionException();
		}
	}

	@Override
	public void createHomes() throws NoSQLConnectionException {
		if (TigerConnection.hasConnection()) {
			PreparedStatement st = null;
			try {
				st = TigerConnection.conn().prepareStatement(String.format(
						"CREATE TABLE IF NOT EXISTS %s (" +
						"%s INT UNSIGNED AUTO_INCREMENT PRIMARY KEY, " +
						"%s INT UNSIGNED NOT NULL, " +
						"%s INT UNSIGNED NOT NULL, " +
						"%s VARCHAR(255), " +
						"%s FLOAT(10,4), " +
						"%s FLOAT(10,4), " +
						"%s FLOAT(10,4), " +
						"%s FLOAT(6,2), " +
						"%s FLOAT(6,2), " +
						"%s TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP," +
						"FOREIGN KEY (%s) REFERENCES %s(%s)," +
						"FOREIGN KEY (%s) REFERENCES %s(%s)," +
						"UNIQUE KEY unique_home (%s, %s))",
						HOMES,
						HID,
						PID,
						WID,
						NAME,
						X, Y, Z,
						YAW, PITCH,
						TIME_CREATED,
						PID, PLAYERS, PID,
						WID, WORLDS, WID,
						PID, NAME));
				st.executeUpdate();
			} catch (SQLException e) {
				TigerLogger.log(Level.SEVERE, "Could not create homes.", e);
			} finally {
				TigerConnection.closeResources(null, st);
			}
		} else {
			throw new NoSQLConnectionException();
		}
	}
}
