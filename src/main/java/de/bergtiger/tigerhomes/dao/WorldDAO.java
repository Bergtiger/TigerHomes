package de.bergtiger.tigerhomes.dao;

import de.bergtiger.tigerhomes.exception.NoSQLConnectionException;

public interface WorldDAO {

	void updateWorlds() throws NoSQLConnectionException;
}
