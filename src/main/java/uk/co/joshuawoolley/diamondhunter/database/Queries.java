package uk.co.joshuawoolley.diamondhunter.database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import static uk.co.joshuawoolley.diamondhunter.constants.SQLQueries.*;

/**
 * @author Josh Woolley
 */
public class Queries {
	
	private Connection connection = null;

	public Queries(Connection connection) {
		this.connection = connection;
	}
	
	public boolean createMySQLTable() {
		try {
			Statement statement = connection.createStatement();
			statement.executeUpdate(CREATE_MYSQL);
			return true;
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
	}
	
	public boolean createSQLiteTable() {
		try {
			Statement statement = connection.createStatement();
			statement.executeUpdate(CREATE_SQLITE);
			return true;
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
	}
	
	public boolean excuteUpdate(String sql){
			try {
				Statement statement = connection.createStatement();
				statement.executeUpdate(sql);
				return true;
			} catch (SQLException e) {
				e.printStackTrace();
				return false;
			}
	}
	
	public ResultSet getPlayerInfo(String sql) {
		try {
			Statement statement = connection.createStatement();
			ResultSet res = statement.executeQuery(sql);
			return res;
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public boolean insertBlock(String uuid, String block, int amount, String world, String location) {
		PreparedStatement ps;
		try {
			ps = connection.prepareStatement(INSERT_BLOCK_BREAK);
			ps.setString(1, uuid);
			ps.setString(2, block);
			ps.setInt(3, amount);
			ps.setString(4, world);
			ps.setString(5, location);
			ps.executeUpdate();
			return true;
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
	}
	
	public ResultSet countBlocks(String uuid) {
		try {
			Statement statement = connection.createStatement();
			ResultSet res = statement.executeQuery(GET_PLAYER_COUNT_QUERY + uuid + "';");
			return res;
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public void closeConnection() throws SQLException {
		connection.close();
	}
}
