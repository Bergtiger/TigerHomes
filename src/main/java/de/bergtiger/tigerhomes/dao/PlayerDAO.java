package de.bergtiger.tigerhomes.dao;

import de.bergtiger.tigerhomes.exception.NoSQLConnectionException;

import java.util.List;

public interface PlayerDAO {

	public Integer updatePlayer(String name, String uuid) throws NoSQLConnectionException;

	public List<String> getPlayerNames(String name) throws NoSQLConnectionException;

}
