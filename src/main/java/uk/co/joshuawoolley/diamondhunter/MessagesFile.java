package uk.co.joshuawoolley.diamondhunter;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

/**
 * 
 * @author Josh Woolley
 *
 */
public class MessagesFile {
	
	private DiamondHunter dh;
	
	private File f;
	
	public MessagesFile(DiamondHunter instance) {
		dh = instance;
		f = new File(dh.getDataFolder() + File.separator + "messages.yml");
	}
	
	/**
	 * Create messages.yml file if it doesn't exist
	 */
	public void createFile() {
		File f = new File(dh.getDataFolder() + File.separator + "messages.yml");
		if (!f.exists()) {
			try {
				f.createNewFile();
				saveMessages();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	/**
	 * Loads the messages from messages.yml and stores them in to a HashMap
	 * 
	 * @return HashMap of messages
	 */
	public HashMap<String, String> loadMessages() {
		FileConfiguration config = YamlConfiguration.loadConfiguration(f);
		HashMap<String, String> messageData = new HashMap<String, String>();
		for (String message : config.getConfigurationSection("").getKeys(false)) {
			messageData.put(message, config.getString(message));
		}
		return messageData;
	}

	
	/**
	 * Sets the messages in the messages.yml
	 * @param name
	 * @param message
	 */
	private void setMessage(String name, String message) {
		FileConfiguration config = YamlConfiguration.loadConfiguration(f);
		if (!config.isSet(name)) {
			config.set(name, message);
			try {
				config.save(f);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	/*
	 * Enters the messages that should be put in the messages.yml
	 */
	private void saveMessages(){
		setMessage("tag", "&6[&4DH&6]");
		setMessage("adminNotice", " %player &bhas mined %amount %block at location X: %x Y: %y Z: %z World: %world");
		setMessage("reload", "&2DiamondHunter has successfully been reloaded!");
		setMessage("noPermission", "&4You do not have permission to preform this command!");
		setMessage("disableNotice", "&2You have disabled Diamond Hunter notices");
		setMessage("alreadyDisabled", "&2You have already diabled Diamond Hunter notices");
		setMessage("renableNotice", "&2You have renabled Diamond Hunter notices");
		setMessage("alreadyEnabled", "&2Diamond Hunter notices are already enabled");
		setMessage("reportIncorrect", "&2No player entered, please use: /dh report <player>");
	}

}
