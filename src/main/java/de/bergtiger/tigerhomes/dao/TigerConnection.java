package de.bergtiger.tigerhomes.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;

import de.bergtiger.tigerhomes.dao.impl.HomeDAOImpl;
import de.bergtiger.tigerhomes.dao.impl.PlayerDAOImpl;
import de.bergtiger.tigerhomes.dao.impl.TableDAOImpl;

import de.bergtiger.tigerhomes.dao.impl.WorldDAOImpl;
import de.bergtiger.tigerhomes.exception.NoSQLConnectionException;
import de.bergtiger.tigerhomes.utils.TigerListHandler;
import de.bergtiger.tigerhomes.utils.TigerLogger;
import de.bergtiger.tigerhomes.utils.config.TigerConfig;
import org.bukkit.Bukkit;
import org.bukkit.scheduler.BukkitTask;

import de.bergtiger.tigerhomes.TigerHomes;
import static de.bergtiger.tigerhomes.utils.config.TigerConfig.*;

public class TigerConnection {
	
	private static Connection conn;
	private static TigerConnection instance;
	private static boolean foreignConnection = false;
	
	private String host, user, password, database;
	private int port;
	private BukkitTask thread = null;

	
	public static TigerConnection inst() {
		if(instance == null)
			instance = new TigerConnection();
		return instance;
	}
	
	private TigerConnection() {}

	/**
	 * Get foreign connection.
	 * @return true if the connection is not from this plugin
	 */
	public static boolean isForeignConnection() {
		return foreignConnection;
	}

	/**
	 * load connection data from configuration
	 */
	public void loadData() {
		TigerConfig c = TigerConfig.inst();
		// host
		if(c.hasValue(HOST))
			host = c.getValue(HOST);
		else
			TigerLogger.log(Level.SEVERE, "Missing value for " + HOST);
		// user
		if(c.hasValue(USER))
			user = c.getValue(USER);
		else
			TigerLogger.log(Level.SEVERE, "Missing value for " + USER);
		// password
		if(c.hasValue(PASSWORD))
			password = c.getValue(PASSWORD);
		else
			TigerLogger.log(Level.SEVERE, "Missing value for " + PASSWORD);
		// database
		if(c.hasValue(DATABASE))
			database = c.getValue(DATABASE);
		else
			TigerLogger.log(Level.SEVERE, "Missing value for " + DATABASE);
		// port
		if(c.hasValue(PORT))
			try {
				port = Integer.parseInt(c.getValue(PORT));
			} catch (NumberFormatException e) {
				TigerLogger.log(Level.SEVERE, "Wrong value for database.Port, has to be a number");
			}
		else
			TigerLogger.log(Level.SEVERE, "Missing value for " + PORT);
	}

	/**
	 * open a sql connection
	 */
	private void connect() {
		// if Thread exists stop
		closeThread();
		// if connection exists stop
		if(hasConnection())
			closeConnection();
		// try Connection
		try {
			// TODO only event listener add function for Event ?
			// check for TigerList as main connection
			if (Bukkit.getPluginManager().isPluginEnabled("TigerList")) {
				conn = TigerListHandler.getConnection();
				foreignConnection = true;
				TigerLogger.log(Level.INFO, "Using TigerList SQL-Connection");
			} else {
				openConnection();
				foreignConnection = false;
				TigerLogger.log(Level.INFO, "SQL-Connection");
			}
			getTableDAO().createTables();
		} catch (Exception e) {
			TigerLogger.log(Level.WARNING, "No Connection");
			TigerLogger.log(Level.WARNING, "Try SQL-Reconnection in 30 seconds.");
			thread = Bukkit.getScheduler().runTaskLaterAsynchronously(TigerHomes.inst(), this::connect, 30*20L);
		}
	}

	/**
	 * reloads connection and configuration.
	 * resets possible reconnect thread.
	 */
	public void reload(){
		// if Thread exists stop
		closeThread();
		// if connection exists stop
		if(hasConnection())
			closeConnection();
		// load new Data
		loadData();
		// connect
		connect();
		// update worlds
		try {
			getWorldDAO().updateWorlds();
		} catch (NoSQLConnectionException e) {
			TigerLogger.log(Level.SEVERE, "Could not update worlds. No connection", e);
		}
	}

	/**
	 * if a reconnect thread is running, stops it hopefully
	 */
	private void closeThread(){
		if(thread != null){
			thread.cancel();
			thread = null;
		}
	}

	/**
	 * opens actual connection, chooses driver and connection link
	 * @throws Exception went wrong
	 */
	private void openConnection() throws Exception{
		// old driver
		// Class.forName("com.mysql.jdbc.Driver");
		// TODO new sql driver
		Class.forName("com.mysql.cj.jdbc.Driver");
		conn = DriverManager.getConnection("jdbc:mysql://" + host + ":" + port + "/" + database, user, password);
	}
	
	/**
	 * get SQL-Connection.
	 * @return {@link Connection}
	 */
	public static Connection conn() {
		return conn;
	}
	
	/**
	 * Checks if the connection exists and is valid
	 * @return true when connection is valid
	 */
	public static Boolean hasConnection() {
		try {
			return (conn != null) && conn.isValid(1);
		} catch (SQLException ex) {
			return false;
		}
	}
	
	/**
	 * handle no connection exception
	 */
	public static void noConnection() {
		if (!foreignConnection) {
			// if Thread exists stop Thread
			instance.closeThread();
			// if has no Connection try new
			if (!hasConnection())
				// connect
				instance.connect();
		} else {
			// pass on noConnection
			TigerListHandler.noConnection();
		}
	}
	
	/**
	 * save close of resources
	 * @param st {@link PreparedStatement} to close
	 * @param rs {@link ResultSet} to close
	 */
	public static void closeResources(ResultSet rs, PreparedStatement st) {
		if(rs != null) {
			try {
				rs.close();
			} catch (SQLException ex) {
				TigerLogger.log(Level.SEVERE, null, ex);
			}
		}
		if(st != null) {
			try {
				st.close();
			} catch (SQLException ex) {
				TigerLogger.log(Level.SEVERE, null, ex);
			}
		}
	}
	
	/**
	 * if there is a Valid Connection the Connection will be closed
	 * after closing the connection value will be set to null
	 */
	public void closeConnection() {
		if (!foreignConnection) {
			try {
				closeThread();
				if (hasConnection()) {
					conn.close();
					TigerLogger.log(Level.INFO, "Logout");
				}
			} catch (SQLException ex) {
				TigerLogger.log(Level.SEVERE, null, ex);
			} finally {
				conn = null;
			}
		}
	}

	public static TableDAO getTableDAO() {
		return new TableDAOImpl();
	}

	public static HomeDAO getHomeDAO() {
		return new HomeDAOImpl();
	}

	public static PlayerDAO getPlayerDAO() {
		return new PlayerDAOImpl();
	}

	public static WorldDAO getWorldDAO() {
		return new WorldDAOImpl();
	}
}
