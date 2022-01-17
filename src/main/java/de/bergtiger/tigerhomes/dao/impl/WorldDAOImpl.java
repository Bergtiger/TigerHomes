package de.bergtiger.tigerhomes.dao.impl;

import de.bergtiger.tigerhomes.dao.TigerConnection;
import de.bergtiger.tigerhomes.dao.WorldDAO;
import de.bergtiger.tigerhomes.exception.NoSQLConnectionException;
import de.bergtiger.tigerhomes.utils.TigerLogger;
import org.bukkit.Bukkit;
import org.bukkit.World;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.logging.Level;

import static de.bergtiger.tigerhomes.dao.TableDAO.*;

public class WorldDAOImpl implements WorldDAO {

	@Override
	public void updateWorlds() throws NoSQLConnectionException {
		if (TigerConnection.hasConnection()) {
			PreparedStatement st = null;
			try {
				st = TigerConnection.conn().prepareStatement(String.format(
						"INSERT IGNORE INTO %s (%s) VALUES (?)",
						WORLDS, NAME));
				for(World w : Bukkit.getWorlds()) {
					st.clearParameters();
					st.setString(1, w.getName());
					st.addBatch();
				}
				st.executeBatch();
			} catch (SQLException e) {
				TigerLogger.log(Level.SEVERE, "Could not update worlds.", e);
			} finally {
				TigerConnection.closeResources(null, st);
			}
		} else {
			throw new NoSQLConnectionException();
		}
	}
}
