package de.bergtiger.tigerhomes.dao;

import de.bergtiger.tigerhomes.bdo.Home;
import de.bergtiger.tigerhomes.exception.NoSQLConnectionException;

import java.util.List;

public interface HomeDAO {

	/**
	 * Update or insert a home.
	 * @param uuid of player whose home is updated
	 * @param home to update
	 * @return home identification
	 * @throws NoSQLConnectionException if no SQL connection available
	 */
	Integer updateHome(String uuid, Home home) throws NoSQLConnectionException;

	/**
	 * Delete a home from a player.
	 * @param uuid of player
	 * @param home name of home
	 * @return amount of changed rows
	 * @throws NoSQLConnectionException if no SQL connection available
	 */
	Integer deleteHome(String uuid, String home) throws NoSQLConnectionException;

	/**
	 * Get a home from a player.
	 * @param uuid of player
	 * @param home name of home
	 * @return a home
	 * @throws NoSQLConnectionException if no SQL connection available
	 */
	Home getHome(String uuid, String home) throws NoSQLConnectionException;

	/**
	 * Get all homes from a player.
	 * @param uuid of player
	 * @return a list containing all homes.
	 * @throws NoSQLConnectionException if no SQL connection available
	 */
	List<Home> getHomes(String uuid) throws NoSQLConnectionException;

	/**
	 * Get all names of all homes from a player.
	 * @param uuid of player
	 * @param home start of home names
	 * @return a list containing all matching home names
	 * @throws NoSQLConnectionException if no SQL connection available
	 */
	List<String> getHomeNames(String uuid, String home) throws NoSQLConnectionException;

	/**
	 * Count homes of a player.
	 * @param uuid of player
	 * @return amount of homes
	 * @throws NoSQLConnectionException if no SQL connection available
	 */
	Integer countHomes(String uuid) throws NoSQLConnectionException;

	/**
	 * Count homes of a player except given home name.
	 * @param uuid of player
	 * @param home exception
	 * @return amount of homes
	 * @throws NoSQLConnectionException if no SQL connection available
	 */
	Integer countHomes(String uuid, String home) throws NoSQLConnectionException;

	/**
	 * Search for homes in a cuboid area.
	 * @param xMin minimum edge
	 * @param xMax maximum edge
	 * @param yMin minimum edge
	 * @param yMax maximum edge
	 * @param zMin minimum edge
	 * @param zMax maximum edge
	 * @return a list containing all found homes
	 * @throws NoSQLConnectionException if no SQL connection available
	 */
	List<Home> searchHomes(String world, double xMin, double xMax, double yMin, double yMax, double zMin, double zMax) throws NoSQLConnectionException;
}
