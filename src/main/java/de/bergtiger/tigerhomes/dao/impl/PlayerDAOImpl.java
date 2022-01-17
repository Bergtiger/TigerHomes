package de.bergtiger.tigerhomes.dao.impl;

import de.bergtiger.tigerhomes.dao.PlayerDAO;
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

public class PlayerDAOImpl implements PlayerDAO {

	@Override
	public Integer updatePlayer(String name, String uuid) throws NoSQLConnectionException {
		if (name != null && !name.isBlank() && uuid != null && !uuid.isBlank()) {
			if (TigerConnection.hasConnection()) {
				PreparedStatement st = null;
				ResultSet rs = null;
				try {
					st = TigerConnection.conn().prepareStatement(String.format(
							"INSERT INTO %s (%s, %s) VALUES (?, ?) " +
							"ON DUPLICATE KEY UPDATE %s = VALUES(%s)",
							PLAYERS, NAME, UUID,
							NAME, NAME), Statement.RETURN_GENERATED_KEYS);
					// set parameter
					st.setString(1, name);
					st.setString(2, uuid);
					// execute update
					st.executeUpdate();

					rs = st.getGeneratedKeys();
					if (rs.next()) {
						return rs.getInt(1);
					}
				} catch (SQLException e) {
					TigerLogger.log(Level.SEVERE, String.format("Could not update Player '%s'", name),e);
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
	public List<String> getPlayerNames(String name) throws NoSQLConnectionException {
		if (name != null && !name.isBlank()) {
			if (TigerConnection.hasConnection()) {
				PreparedStatement st = null;
				ResultSet rs = null;
				try {
					st = TigerConnection.conn().prepareStatement(String.format(
							"SELECT %s FROM %s WHERE %s LIKE ?",
							NAME, PLAYERS, NAME),
							ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_READ_ONLY);
					// set parameter
					st.setString(1, name + "%");
					// execute query
					rs = st.executeQuery();
					// load names
					List<String> names = new ArrayList<>();
					while(rs.next()) {
						names.add(rs.getString(1));
					}
					return names;
				} catch (SQLException e) {
					TigerLogger.log(Level.SEVERE, String.format("Could not load player names '%s'", name), e);
				} finally {
					TigerConnection.closeResources(rs, st);
				}
			} else {
				throw new NoSQLConnectionException();
			}
		}
		return null;
	}
}
