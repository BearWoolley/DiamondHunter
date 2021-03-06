package uk.co.joshuawoolley.diamondhunter.blockhandler;

import java.util.HashSet;
import java.util.List;

import org.bukkit.GameMode;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;

import uk.co.joshuawoolley.diamondhunter.DiamondHunter;
import uk.co.joshuawoolley.diamondhunter.broadcaster.BroadcastHandler;
import uk.co.joshuawoolley.diamondhunter.database.Queries;

/**
 * 
 * @author Josh Woolley
 *
 */
public class BlockBreakHandler implements Listener {
	
	private DiamondHunter plugin;
	private BroadcastHandler bh;
	private Queries query;
	
	private HashSet<Block> vein;
	private HashSet<Block> whitelist;
	
	public BlockBreakHandler(DiamondHunter instance, BroadcastHandler bh, Queries q) {
		plugin = instance;
		this.bh = bh;
		whitelist = new HashSet<Block>();
		
		query = q;
	}
	
	/**
	 * Event for when a player breaks a block
	 * @param event
	 * 			The break event
	 */
	@SuppressWarnings("deprecation")
	@EventHandler
	public void onBlockBreak(BlockBreakEvent event) {
		Player p = event.getPlayer();
		Block b = event.getBlock();
		
		if (!p.hasPermission("dh.exempt")) {
			if (!plugin.getConfig().getBoolean("logCreative") && p.getGameMode() == GameMode.CREATIVE) {
				return;
			} else {
				List<String> blocks = plugin.getConfig().getStringList("blocks");
				for (String s : blocks) {
					if (s.equals(String.valueOf(b.getTypeId()))) {
						vein = new HashSet<Block>();
						if (!whitelist.contains(b)) {
							vein.clear();
							countBlocks(b.getTypeId(), b, vein);
							bh.printNotice(p.getDisplayName(), vein.size(), b);

							logBreak(b, p);
						} else {
							whitelist.remove(b);
						}
					}
				}
			}
		}
	}
	
	/**
	 * Adds all the blocks around it to the vein HashSet
	 * 
	 * @param id
	 * 			id of the block
	 * @param b
	 * 			The block
	 * @param collected
	 * 			The HashSet that block should be added to
	 */
	@SuppressWarnings("deprecation")
	private void countBlocks(int id, Block b, HashSet<Block> collected) {
		if(b.getTypeId() != id) { return; }
		
		if(collected.contains(b)) { return; }
		
		collected.add(b);
		whitelist.add(b);
		
		countBlocks(id, b.getRelative(BlockFace.DOWN), collected);
		countBlocks(id, b.getRelative(BlockFace.EAST), collected);
		countBlocks(id, b.getRelative(BlockFace.EAST_NORTH_EAST), collected);
		countBlocks(id, b.getRelative(BlockFace.EAST_SOUTH_EAST), collected);
		countBlocks(id, b.getRelative(BlockFace.NORTH), collected);
		countBlocks(id, b.getRelative(BlockFace.NORTH_EAST), collected);
		countBlocks(id, b.getRelative(BlockFace.NORTH_NORTH_EAST), collected);
		countBlocks(id, b.getRelative(BlockFace.NORTH_NORTH_WEST), collected);
		countBlocks(id, b.getRelative(BlockFace.NORTH_WEST), collected);
		countBlocks(id, b.getRelative(BlockFace.SOUTH), collected);
		countBlocks(id, b.getRelative(BlockFace.SOUTH_EAST), collected);
		countBlocks(id, b.getRelative(BlockFace.SOUTH_SOUTH_EAST), collected);
		countBlocks(id, b.getRelative(BlockFace.SOUTH_SOUTH_WEST), collected);
		countBlocks(id, b.getRelative(BlockFace.SOUTH_WEST), collected);
		countBlocks(id, b.getRelative(BlockFace.UP), collected);
		countBlocks(id, b.getRelative(BlockFace.WEST), collected);
		countBlocks(id, b.getRelative(BlockFace.WEST_NORTH_WEST), collected);
		countBlocks(id, b.getRelative(BlockFace.WEST_SOUTH_WEST), collected);
	}
	
	/**
	 * Logs block to MySQL database or SQLite file
	 * @param b
	 * 		The block broken
	 * @param p
	 * 		The player that broke the block
	 */
	private void logBreak(Block b, Player p) {
		String blockName = b.getType().toString().toLowerCase().replace("_"," ");
		String world = b.getWorld().toString();
		
		String tempWorld[] = world.split("=");
		world = tempWorld[1].substring(0, tempWorld[1].length()-1);
		
		String location = "X: " + b.getX() + " Y: " + b.getY() + " Z: " + b.getZ();
		
		query.insertBlock(p.getUniqueId().toString(), blockName, vein.size(), world, location);
	}
}
