package de.bergtiger.tigerhomes.dao.impl;

import de.bergtiger.tigerhomes.bdo.Home;
import de.bergtiger.tigerhomes.dao.HomeDAO;
import de.bergtiger.tigerhomes.dao.TigerConnection;
import de.bergtiger.tigerhomes.exception.NoSQLConnectionException;
import de.bergtiger.tigerhomes.utils.TigerLogger;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;

import static de.bergtiger.tigerhomes.dao.TableDAO.*;

public class HomeDAOImpl implements HomeDAO {

	@Override
	public Integer updateHome(String uuid, Home home) throws NoSQLConnectionException {
		if (uuid != null && !uuid.isBlank() && home != null) {
			if (TigerConnection.hasConnection()) {
				PreparedStatement st = null;
				ResultSet rs = null;
				try {
					st = TigerConnection.conn().prepareStatement(String.format(
							"INSERT INTO %s (%s, %s, %s, %s, %s, %s, %s, %s) VALUES (" +
									"(SELECT %s FROM %s WHERE %s LIKE ?), " +
									"(SELECT %s FROM %s WHERE %s LIKE ?), " +
									"?, ?, ?, ?, ?, ?) " +
									"On DUPLICATE KEY UPDATE " +
									"%s = VALUES(%s)," +
									"%s = VALUES(%s)," +
									"%s = VALUES(%s)," +
									"%s = VALUES(%s)," +
									"%s = VALUES(%s)," +
									"%s = VALUES(%s)",
							HOMES,
							PID, WID, NAME, X, Y, Z, YAW, PITCH,
							PID, PLAYERS, uuid.length() > 16 ? UUID : NAME,
							WID, WORLDS, NAME,

							WID, WID,
							X, X,
							Y, Y,
							Z, Z,
							YAW, YAW,
							PITCH, PITCH), Statement.RETURN_GENERATED_KEYS);
					// set parameter
					st.setString(1, uuid);
					st.setString(2, home.getW());
					st.setString(3, home.getName());
					st.setDouble(4, home.getX());
					st.setDouble(5, home.getY());
					st.setDouble(6, home.getZ());
					st.setFloat(7, home.getYaw());
					st.setFloat(8, home.getPitch());
					// execute update
					st.executeUpdate();
					// return generated key
					rs = st.getGeneratedKeys();
					if (rs.next()) {
						return rs.getInt(1);
					}
					return 0;
				} catch (SQLException e) {
					TigerLogger.log(Level.SEVERE, String.format("Could not update Home %s", home), e);
				} finally {
					TigerConnection.closeResources(rs, st);
				}
			} else {
				throw new NoSQLConnectionException();
			}
		}
		return null;
	}

	@Override
	public Integer deleteHome(String uuid, String home) throws NoSQLConnectionException {
		if (uuid != null && !uuid.isBlank() && home != null) {
			if (TigerConnection.hasConnection()) {
				PreparedStatement st = null;
				try {
					st = TigerConnection.conn().prepareStatement(String.format(
							"DELETE FROM %s " +
									"WHERE %s LIKE ? AND %s = " +
									"(SELECT %s FROM %s WHERE %s LIKE ?)",
							HOMES,
							NAME, PID,
							PID, PLAYERS, uuid.length() > 16 ? UUID : NAME));
					// set parameter
					st.setString(1, home);
					st.setString(2, uuid);
					// execute update
					return st.executeUpdate();
				} catch (SQLException e) {
					TigerLogger.log(Level.SEVERE, String.format("Could not delete Home '%s' from player '%s'", home, uuid), e);
				} finally {
					TigerConnection.closeResources(null, st);
				}
			} else {
				throw new NoSQLConnectionException();
			}
		}
		return null;
	}

	@Override
	public Home getHome(String uuid, String home) throws NoSQLConnectionException {
		if (uuid != null && !uuid.isBlank() && home != null) {
			if (TigerConnection.hasConnection()) {
				PreparedStatement st = null;
				ResultSet rs = null;
				try {
					st = TigerConnection.conn().prepareStatement(String.format(
							"SELECT * FROM %s AS h " +
									"JOIN %s AS p ON h.%s = p.%s " +
									"JOIN %s AS w ON h.%s = w.%s " +
									"WHERE p.%s LIKE ? AND h.%s LIKE ?",
							HOMES,
							PLAYERS, PID, PID,
							WORLDS, WID, WID,
							uuid.length() > 16 ? UUID : NAME,
							NAME), ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_READ_ONLY);
					// set parameter
					st.setString(1, uuid);
					st.setString(2, home);
					// execute query
					rs = st.executeQuery();
					// load home
					if (rs.next()) {
						Home h = new Home();
						// home_uuid
						h.setUuid(rs.getString("p." + UUID));
						// home_name
						h.setName(rs.getString("h." + NAME));
						// world_name
						h.setW(rs.getString("w." + NAME));
						// x
						h.setX(rs.getDouble("h." + X));
						// y
						h.setY(rs.getDouble("h." + Y));
						// z
						h.setZ(rs.getDouble("h." + Z));
						// yaw
						h.setYaw(rs.getFloat("h." + YAW));
						// pitch
						h.setPitch(rs.getFloat("h." + PITCH));
						// time
						h.setT(rs.getTimestamp("h." + TIME_CREATED).toLocalDateTime());
						return h;
					}
				} catch (SQLException e) {
					TigerLogger.log(Level.SEVERE, String.format("Could not load home '%s' from player '%s'", home, uuid), e);
				} finally {
					TigerConnection.closeResources(rs, st);
				}
			} else {
				throw new NoSQLConnectionException();
			}
		}
		return null;
	}

	@Override
	public List<Home> getHomes(String uuid) throws NoSQLConnectionException {
		if (uuid != null && !uuid.isBlank()) {
			if (TigerConnection.hasConnection()) {
				PreparedStatement st = null;
				ResultSet rs = null;
				try {
					st = TigerConnection.conn().prepareStatement(String.format(
							"SELECT * FROM %s AS h " +
									"JOIN %s AS p ON h.%s = p.%s " +
									"JOIN %s AS w ON h.%s = w.%s " +
									"WHERE p.%s LIKE ?",
							HOMES,
							PLAYERS, PID, PID,
							WORLDS, WID, WID,
							uuid.length() > 16 ? UUID : NAME), ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_READ_ONLY);
					// set uuid
					st.setString(1, uuid);
					// execute query
					rs = st.executeQuery();
					List<Home> homes = new ArrayList<>();
					// load homes
					while (rs.next()) {
						Home h = new Home();
						// home_uuid
						h.setUuid(rs.getString("p." + UUID));
						// home_name
						h.setName(rs.getString("h." + NAME));
						// world_name
						h.setW(rs.getString("w." + NAME));
						// x
						h.setX(rs.getDouble("h." + X));
						// y
						h.setY(rs.getDouble("h." + Y));
						// z
						h.setZ(rs.getDouble("h." + Z));
						// yaw
						h.setYaw(rs.getFloat("h." + YAW));
						// pitch
						h.setPitch(rs.getFloat("h." + PITCH));
						// time
						h.setT(rs.getTimestamp("h." + TIME_CREATED).toLocalDateTime());
						homes.add(h);
					}
					return homes;
				} catch (SQLException e) {
					TigerLogger.log(Level.SEVERE, String.format("Could not load homes from player '%s'", uuid), e);
				} finally {
					TigerConnection.closeResources(rs, st);
				}
			} else {
				throw new NoSQLConnectionException();
			}
		}
		return null;
	}

	@Override
	public List<String> getHomeNames(String uuid, String home) throws NoSQLConnectionException {
		if (uuid != null && !uuid.isBlank()) {
			if (home == null)
				home = "";
			if (TigerConnection.hasConnection()) {
				PreparedStatement st = null;
				ResultSet rs = null;
				try {
					st = TigerConnection.conn().prepareStatement(String.format(
							"SELECT %s " +
							"FROM %s AS h " +
							"JOIN %s AS p ON h.%s = p.%s " +
							"WHERE h.%s LIKE ? AND p.%s LIKE ?",
							"h." + NAME,
							HOMES,
							PLAYERS, PID, PID,
							NAME, uuid.length() > 16 ? UUID : NAME),
							ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_READ_ONLY);
					// set parameter
					st.setString(1, home + "%");
					st.setString(2, uuid);
					// execute query
					rs = st.executeQuery();
					// load names
					List<String> names = new ArrayList<>();
					while(rs.next()) {
						names.add(rs.getString(1));
					}
					return names;
				} catch (SQLException e) {
					TigerLogger.log(Level.SEVERE, String.format("Could not load home names for player '%s' witch starts with '%s'", uuid, home), e);
				} finally {
					TigerConnection.closeResources(rs, st);
				}
			} else {
				throw new NoSQLConnectionException();
			}
		}
		return null;
	}

	@Override
	public Integer countHomes(String uuid) throws NoSQLConnectionException {
		if (uuid != null && !uuid.isBlank()) {
			if (TigerConnection.hasConnection()) {
				PreparedStatement st = null;
				ResultSet rs = null;
				try {
					st = TigerConnection.conn().prepareStatement(String.format(
							"SELECT COUNT(*) " +
							"FROM %s AS h " +
							"JOIN %s AS p ON h.%s = p.%s " +
							"WHERE p.%s LIKE ?",
							HOMES,
							PLAYERS, PID, PID,
							uuid.length() > 16 ? UUID : NAME),
							ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_READ_ONLY);
					// set parameter
					st.setString(1, uuid);
					// execute query
					rs = st.executeQuery();
					if (rs.next()) {
						return rs.getInt(1);
					}
				} catch (SQLException e) {
					TigerLogger.log(Level.SEVERE, String.format("Could not count homes for player '%s'", uuid), e);
				} finally {
					TigerConnection.closeResources(rs, st);
				}
			} else {
				throw new NoSQLConnectionException();
			}
		}
		return null;
	}

	@Override
	public Integer countHomes(String uuid, String home) throws NoSQLConnectionException {
		if (uuid != null && !uuid.isBlank() && home != null) {
			if (TigerConnection.hasConnection()) {
				PreparedStatement st = null;
				ResultSet rs = null;
				try {
					st = TigerConnection.conn().prepareStatement(String.format(
							"SELECT COUNT(*) " +
							"FROM %s AS h " +
							"JOIN %s AS p ON h.%s = p.%s " +
							"WHERE h.%s NOT LIKE ? AND p.%s LIKE ?",
							HOMES,
							PLAYERS, PID, PID,
							NAME, uuid.length() > 16 ? UUID : NAME),
							ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_READ_ONLY);
					// set parameter
					st.setString(1, home);
					st.setString(2, uuid);
					// execute query
					rs = st.executeQuery();
					if (rs.next()) {
						return rs.getInt(1);
					}
				} catch (SQLException e) {
					TigerLogger.log(Level.SEVERE, String.format("Could not count homes for player '%s' without home '%s'", uuid, home), e);
				} finally {
					TigerConnection.closeResources(rs, st);
				}
			} else {
				throw new NoSQLConnectionException();
			}
		}
		return null;
	}

	@Override
	public List<Home> searchHomes(String world, double xMin, double xMax, double yMin, double yMax, double zMin, double zMax) throws NoSQLConnectionException {
		if (TigerConnection.hasConnection()) {
			PreparedStatement st = null;
			ResultSet rs = null;
			try {
				st = TigerConnection.conn().prepareStatement(String.format(
						"SELECT * " +
						"FROM %s AS h " +
						"JOIN %s AS p ON h.%s = p.%s " +
						"JOIN %s AS w ON h.%s = w.%s " +
						"WHERE w.%s LIKE ? AND h.%s BETWEEN ? AND ? AND h.%s BETWEEN ? AND ? AND h.%s BETWEEN ? AND ?",
						HOMES,
						PLAYERS, PID, PID,
						WORLDS, WID, WID,
						NAME, X, Y, Z),
						ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_READ_ONLY);
				// set parameter
				st.setString(1, world);
				st.setDouble(2, xMin);
				st.setDouble(3, xMax);
				st.setDouble(4, yMin);
				st.setDouble(5, yMax);
				st.setDouble(6, zMin);
				st.setDouble(7, zMax);
				// execute query
				rs = st.executeQuery();
				List<Home> homes = new ArrayList<>();
				while (rs.next()) {
					Home h = new Home();
					// home_uuid
					h.setUuid(rs.getString("p." + NAME));
					// home_name
					h.setName(rs.getString("h." + NAME));
					// world_name
					h.setW(rs.getString("w." + NAME));
					// x
					h.setX(rs.getDouble("h." + X));
					// y
					h.setY(rs.getDouble("h." + Y));
					// z
					h.setZ(rs.getDouble("h." + Z));
					// yaw
					h.setYaw(rs.getFloat("h." + YAW));
					// pitch
					h.setPitch(rs.getFloat("h." + PITCH));
					// time
					h.setT(rs.getTimestamp("h." + TIME_CREATED).toLocalDateTime());
					homes.add(h);
				}
				return homes;
			} catch (SQLException e) {
				TigerLogger.log(Level.SEVERE, String.format("Could not search homes in cuboid (%f, %f, %f), (%f, %f, %f)", xMin, yMin, zMin, xMax, yMax, zMax));
			} finally {
				TigerConnection.closeResources(rs, st);
			}
		} else {
			throw new NoSQLConnectionException();
		}
		return null;
	}
}
