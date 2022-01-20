package uk.co.joshuawoolley.diamondhunter.commandhandler;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import uk.co.joshuawoolley.diamondhunter.DiamondHunter;
import uk.co.joshuawoolley.diamondhunter.ReportHandler;
import uk.co.joshuawoolley.diamondhunter.broadcaster.BroadcastHandler;
import uk.co.joshuawoolley.diamondhunter.database.Queries;

/**
 * @author Josh Woolley
 */
public class CommandHandler implements CommandExecutor {

	private DiamondHunter dh;
	private BroadcastHandler bh;
	private Queries query;
	private ReportHandler report;
	private String tag;

	public CommandHandler(DiamondHunter instance, Queries q, BroadcastHandler bh) {
		dh = instance;
		query = q;
		this.bh = bh;
		report = new ReportHandler(query);
		tag = translateMessages("tag");
	}

	public boolean onCommand(CommandSender sender, Command command,
			String label, String[] args) {
		if (command.getName().equalsIgnoreCase("dh")) {
			if (args.length > 0) {
				Player p = (Player) sender;

				if (args[0].equalsIgnoreCase("reload")) {
					if (p.hasPermission("dh.reload")) {
						dh.reloadConfig();
						sender.sendMessage(tag + translateMessages("reload"));
						return true;
					} else {
						sender.sendMessage(tag
								+ translateMessages("noPermission"));
						return true;
					}
				} else if (args[0].equalsIgnoreCase("off")) {
					if (p.hasPermission("dh.getnotice")) {
						if (bh.disableNotices(p.getName())) {
							sender.sendMessage(tag
									+ translateMessages("disabledNotice"));
							return true;
						} else {
							sender.sendMessage(tag
									+ translateMessages("alreadyDisabled"));
							return true;
						}
					} else {
						sender.sendMessage(tag
								+ translateMessages("noPermission"));
						return true;
					}
				} else if (args[0].equalsIgnoreCase("on")) {
					if (p.hasPermission("dh.getnotice")) {
						if (bh.renableNotices(p.getName())) {
							sender.sendMessage(tag
									+ translateMessages("renableNotice"));
							return true;
						} else {
							sender.sendMessage(tag
									+ translateMessages("alreadyEnabled"));
							return true;
						}
					} else {
						sender.sendMessage(tag
								+ translateMessages("noPermission"));
						return true;
					}
				} else if (args[0].equalsIgnoreCase("report")) {
					if (p.hasPermission("dh.report")) {
						if (args.length > 1) {
							String name = args[1];
							@SuppressWarnings("deprecation")
							UUID id = Bukkit.getServer().getOfflinePlayer(name)
									.getUniqueId();
							String uuid = id.toString();
							double amount = getAmount(uuid);
							int numberOfPages = (int) Math.ceil((amount / 5));
							if (args.length > 2) {
								int page = Integer.valueOf(args[2]);
								if (page <= numberOfPages) {
									report.printReport(p, page, numberOfPages,
											uuid);
									return true;
								} else {
									sender.sendMessage(ChatColor.DARK_RED
											+ "ERROR: No page exists");
									return true;
								}
							} else {
								report.printReport(p, 1, numberOfPages, uuid);
								return true;
							}
						} else {
							sender.sendMessage(tag
									+ translateMessages("reportIncorrect"));
							return true;
						}
					} else {
						sender.sendMessage(tag
								+ translateMessages("noPermission"));
						return true;
					}
				}
			} else {
				sender.sendMessage(tag
						+ "The following commands are available!");
				sender.sendMessage(ChatColor.RED + "/dh reload:"
						+ ChatColor.AQUA
						+ " Reloads the Diamond Hunter config.");
				sender.sendMessage(ChatColor.RED + "/dh off:" + ChatColor.AQUA
						+ " Turns block notices off.");
				sender.sendMessage(ChatColor.RED + "/dh on:" + ChatColor.AQUA
						+ " Turns block notices on.");
				sender.sendMessage(ChatColor.RED
						+ "/dh report <player> [page]:" + ChatColor.AQUA
						+ " Get report about a certain player");
				sender.sendMessage("");
				sender.sendMessage(ChatColor.GOLD
						+ "Please report any bugs or problems at");
				sender.sendMessage(ChatColor.AQUA
						+ "http://dev.bukkit.org/server-mods/diamondhunter/");
				sender.sendMessage(ChatColor.GOLD
						+ "Diamond Hunter created by Jwoolley.");
				return true;
			}
		}
		return false;
	}
	
	/**
	 * Changes the message in to colour form
	 * 
	 * @param field
	 * 			The message that the colour needs adding to
	 * @return string of the message
	 */
	@SuppressWarnings("static-access")
	private String translateMessages(String field) {
		return ChatColor.translateAlternateColorCodes('&', dh.messageData.get(field));
	}

	private double getAmount(String uuid) {
		double amount = 0;
		ResultSet res = query.countBlocks(uuid);
		try {
			if(res.next()) {
				amount = Integer.valueOf(res.getString("Amount"));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return amount;
	}
}
