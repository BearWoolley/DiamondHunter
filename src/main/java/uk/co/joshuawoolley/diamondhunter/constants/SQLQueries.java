package uk.co.joshuawoolley.diamondhunter.constants;

/**
* @author Josh Woolley
*/
public final class SQLQueries {
	
	public final static String CREATE_MYSQL = "CREATE TABLE IF NOT EXISTS diamondhunter ( id INT (6) NOT NULL AUTO_INCREMENT, uuid VARCHAR (40) NOT NULL, block VARCHAR (20) NOT NULL, amount INT (2) NOT NULL, world VARCHAR (30), date TIMESTAMP DEFAULT CURRENT_TIMESTAMP, location VARCHAR (50), PRIMARY KEY (id));";
	
	public final static String CREATE_SQLITE = "CREATE TABLE IF NOT EXISTS diamondhunter ( id INTEGER PRIMARY KEY, uuid VARCHAR (40) NOT NULL, block VARCHAR (20) NOT NULL, amount INT (2) NOT NULL, world VARCHAR (30), date TIMESTAMP DEFAULT CURRENT_TIMESTAMP, location VARCHAR (50));";
	
	public final static String INSERT_BLOCK_BREAK = "INSERT INTO diamondhunter (uuid, block, amount, world, location) VALUES (?, ?, ?, ?, ?);";
	
	public final static String GET_PLAYER_COUNT_QUERY = "SELECT COUNT(*) AS Amount FROM diamondhunter WHERE uuid = '";
}