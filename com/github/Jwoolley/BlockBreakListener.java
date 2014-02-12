package com.github.Jwoolley;

import java.io.*;
import java.util.Date;

import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;

// Referenced classes of package com.github.Jwoolley:
//            DiamondHunter, BroadcastHandler

public class BlockBreakListener
    implements Listener
{

    public BlockBreakListener(DiamondHunter instance, BroadcastHandler instance2)
    {
        d = new Date();
        dh = instance;
        bh = instance2;
    }

    @EventHandler
    public void onBlockBreak(BlockBreakEvent event)
    {
        Player player = event.getPlayer();
        Block b = event.getBlock();
        if(event.getBlock().getTypeId() == 56 && dh.getConfig().getBoolean("DiamondNotice"))
            displayNotice(b, player, "Diamond", ChatColor.AQUA);
        else
        if(event.getBlock().getTypeId() == 129 && dh.getConfig().getBoolean("EmeraldNotice"))
            displayNotice(b, player, "Emerald", ChatColor.GREEN);
        else
        if(event.getBlock().getTypeId() == 73 && dh.getConfig().getBoolean("RedstoneNotice"))
            displayNotice(b, player, "Redstone", ChatColor.RED);
        else
        if(event.getBlock().getTypeId() == 21 && dh.getConfig().getBoolean("LapisNotice"))
            displayNotice(b, player, "Lapis", ChatColor.BLUE);
        else
        if(event.getBlock().getTypeId() == 15 && dh.getConfig().getBoolean("IronNotice"))
            displayNotice(b, player, "Iron", ChatColor.GRAY);
        else
        if(event.getBlock().getTypeId() == 16 && dh.getConfig().getBoolean("CoalNotice"))
            displayNotice(b, player, "Coal", ChatColor.DARK_GRAY);
        else
        if(event.getBlock().getTypeId() == 14 && dh.getConfig().getBoolean("GoldNotice"))
            displayNotice(b, player, "Gold", ChatColor.GOLD);
    }

    public void displayNotice(Block b, Player player, String block, ChatColor color)
    {
        Player aplayer[];
        int j = (aplayer = Bukkit.getServer().getOnlinePlayers()).length;
        for(int i = 0; i < j; i++)
        {
            Player p = aplayer[i];
            if(p.hasPermission("dh.getnotice") && !bh.containsPlayer(p) && !player.hasPermission("dh.exempt"))
                p.sendMessage((new StringBuilder()).append(ChatColor.GOLD).append(dh.getConfig().getString("Tag")).append(" ").append(player.getDisplayName()).append(" ").append(color).append(dh.getConfig().getString((new StringBuilder(String.valueOf(block))).append("Message").toString())).append(" X: ").append(b.getX()).append(" Y: ").append(b.getY()).append(" Z: ").append(b.getZ()).toString());
        }

        if(!player.hasPermission("dh.exempt") && dh.getConfig().getBoolean("LogToFile"))
        {
            d.setTime(System.currentTimeMillis());
            World w = b.getWorld();
            String details = (new StringBuilder("[")).append(d).append("]").append(" ").append(player.getName()).append(" ").append(dh.getConfig().getString((new StringBuilder(String.valueOf(block))).append("Message").toString())).append(" X: ").append(b.getX()).append(" Y: ").append(b.getY()).append(" Z: ").append(b.getZ()).toString();
            String f = (new StringBuilder(String.valueOf(w.getName()))).append(".txt").toString();
            logToFile(details, f);
        }
    }

    public void logToFile(String message, String f)
    {
        try
        {
            File dataFolder = dh.getDataFolder();
            if(!dataFolder.exists())
                dataFolder.mkdir();
            File saveTo = new File(dh.getDataFolder(), f);
            if(!saveTo.exists())
                saveTo.createNewFile();
            FileWriter fw = new FileWriter(saveTo, true);
            PrintWriter pw = new PrintWriter(fw);
            pw.println(message);
            pw.close();
        }
        catch(IOException e)
        {
            e.printStackTrace();
        }
    }

    private Date d;
    private static DiamondHunter dh;
    private static BroadcastHandler bh;
}