package uk.co.joshuawoolley.diamondhunter;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import uk.co.joshuawoolley.diamondhunter.database.Queries;

/**
 * @author Josh Woolley
 */
public class ReportHandler {
	
	private Queries query;
	
	public ReportHandler(Queries q) {
		query = q;
	}
	
	public void printReport(Player p, int page, int maxPages, String uuid) {
		p.sendMessage(ChatColor.AQUA + "================" + ChatColor.DARK_RED + "Player report" + ChatColor.AQUA + "==================");
		getReports(p, page, uuid);
		p.sendMessage(ChatColor.AQUA + "==================" + ChatColor.DARK_RED + "[Page " + page + "/" + maxPages + "]" + ChatColor.AQUA + "===================");
	}
	
	private void getReports(Player p, int page, String uuid) {
		int start = ((page * 5) - 5);
		String sqlQuery = "SELECT * FROM diamondhunter WHERE uuid = '" + uuid + "' LIMIT " + start + ",5;";
		
		ResultSet res = query.getPlayerInfo(sqlQuery);
		
		try {
			while(res.next()) {
				String block = res.getString("block");
				int amount = res.getInt("amount");
				String date = res.getString("date");
				String location = res.getString("location");
				String world = res.getString("world");
				String name = Bukkit.getServer().getOfflinePlayer(UUID.fromString(uuid)).getName();
				
				p.sendMessage(ChatColor.GOLD + name + " broke " + amount + " " + block + " at location " + location + " World: " + world + " at time: " + date); 
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

}
