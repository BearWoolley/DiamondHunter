package uk.co.joshuawoolley.diamondhunter.broadcaster;

import java.util.ArrayList;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;

import uk.co.joshuawoolley.diamondhunter.DiamondHunter;

/**
 * 
 * @author Josh Woolley
 *
 */
public class BroadcastHandler {
	
	private DiamondHunter dh;
	private ArrayList<String> disabled;

	public BroadcastHandler(DiamondHunter instance) {
		dh = instance;
		disabled = new ArrayList<String>();
	}
	
	@SuppressWarnings("static-access")
	public void printNotice(String player, int amount, Block b) {
		for (Player p : Bukkit.getServer().getOnlinePlayers()) {
			if(p.hasPermission("dh.getnotice")) {
				if(!disabled.contains(p.getName())){
					String blockName = b.getType().toString().toLowerCase().replace("_", " ");
					String tag = dh.messageData.get("tag");
					String message = dh.messageData.get("adminNotice");
					if (message.contains("%player")) {
						message = message.replace("%player", player);
					}
					if (message.contains("%amount")) {
						message = message.replace("%amount",String.valueOf(amount));
					}
					if (message.contains("%block")) {
						message = message.replace("%block", blockName);
					}
					if (message.contains("%x")) {
						message = message.replace("%x",String.valueOf(b.getX()));
					}
					if (message.contains("%y")) {
						message = message.replace("%y",String.valueOf(b.getY()));
					}
					if (message.contains("%z")) {
						message = message.replace("%z",String.valueOf(b.getZ()));
					}
					if (message.contains("%world")) {
						String world = b.getWorld().toString();

						String tempWorld[] = world.split("=");
						world = tempWorld[1].substring(0,tempWorld[1].length() - 1);
						message = message.replace("%world",String.valueOf(world));
					}

					tag = ChatColor.translateAlternateColorCodes('&', tag);
					message = ChatColor.translateAlternateColorCodes('&',message);

					p.sendMessage(tag + message);
				}
			}
		}
	}
	
	/**
	 * Adds the user in to the ArrayList to disable notices for that person
	 * 
	 * @param name
	 * 			User to disable
	 * @return true or false depending if they are already in the ArrayList
	 */
	public boolean disableNotices(String name) {
		if(!containsPlayer(name)){
			disabled.add(name);
			return true;
		} else {
			return false;
		}
	}
	
	/**
	 * Removes the user from the ArrayList
	 * 
	 * @param name
	 * 			User to disable
	 * @return true or false depending if they are already in the ArrayList
	 */
	public boolean renableNotices(String name) {
		if(containsPlayer(name)){
			disabled.remove(name);
			return true;
		} else {
			return false;
		}
	}
	
	public boolean containsPlayer(String name) {
		if(disabled.contains(name)){
			return true;
		} else {
			return false;
		}
	}
}
