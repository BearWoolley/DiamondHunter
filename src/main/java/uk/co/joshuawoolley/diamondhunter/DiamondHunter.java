package uk.co.joshuawoolley.diamondhunter;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashMap;

import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import uk.co.joshuawoolley.diamondhunter.blockhandler.BlockBreakHandler;
import uk.co.joshuawoolley.diamondhunter.broadcaster.BroadcastHandler;
import uk.co.joshuawoolley.diamondhunter.commandhandler.CommandHandler;
import uk.co.joshuawoolley.diamondhunter.database.MySQL;
import uk.co.joshuawoolley.diamondhunter.database.Queries;
import uk.co.joshuawoolley.diamondhunter.database.SQLite;

/**
 * 
 * @author Josh Woolley
 *
 */
public class DiamondHunter extends JavaPlugin {
	
	private MessagesFile mf;
	
	public static HashMap<String, String> messageData = new HashMap<String, String>();
	private BroadcastHandler bh;
	
	private Queries query = null;
	private Connection c = null;

	public void onEnable() {
		this.saveDefaultConfig();
		
		mf = new MessagesFile(this);
		mf.createFile();
		messageData = mf.loadMessages();
		
		if(this.getConfig().getBoolean("mysql")){
			String hostname = this.getConfig().getString("hostname");
			String port = this.getConfig().getString("port");
			String database = this.getConfig().getString("database");
			String username = this.getConfig().getString("username");
			String password = this.getConfig().getString("password");
			
			MySQL mySQL = new MySQL(this, hostname, port, database, username, password);
			
			try {
				c = mySQL.openConnection();
				query = new Queries(c);
				query.createMySQLTable();
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			} catch (SQLException e) {
				e.printStackTrace();
			}
			
		} else {
			SQLite sqlite = new SQLite(this, "/diamondhunter.db");
			
			try {
				c = sqlite.openConnection();
				query = new Queries(c);
				query.createSQLiteTable();
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			} catch (SQLException e) {
				e.printStackTrace();
			}
			
		}
		
		bh = new BroadcastHandler(this);
		
		PluginManager pm = this.getServer().getPluginManager();
		pm.registerEvents(new BlockBreakHandler(this, bh, query), this);
		
		this.getCommand("dh").setExecutor(new CommandHandler(this, query, bh));
		getLogger().info("Diamond Hunter has been enabled");
	}
	
	public void onDisable() {
		getLogger().info("Diamond Hunter has been disabled");
	}
}
