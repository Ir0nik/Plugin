package fr.sunshine.spawner;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Logger;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.World.Environment;
import org.bukkit.block.Block;
import org.bukkit.block.CreatureSpawner;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockExpEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.inventory.PlayerInventory;

public class SunshineSpawnerListener
  implements Listener
{
  private SunshineSpawner plugin;
  private SunshineSpawnerFunctions functions;
  
  public SunshineSpawnerListener(SunshineSpawner plugin)
  {
    this.plugin = plugin;
    this.functions = new SunshineSpawnerFunctions(plugin);
  }
  
  @EventHandler(priority=EventPriority.MONITOR, ignoreCancelled=true)
  public void onBlockPlaceEvent(BlockPlaceEvent e)
  {
    if (e.getBlockPlaced().getTypeId() == 52)
    {
      String spawnerType = this.functions.setSpawner(e.getPlayer(), e.getBlock());
      Player player = e.getPlayer();
      Block bloc = e.getBlockPlaced();
      Location bloc_loc = bloc.getLocation();
      String[] world = bloc_loc.getWorld().toString().split("=");
      String spawnerLocation = world[1].replace("}", "") + "//" + bloc_loc.getBlockX() + "//" + bloc_loc.getBlockY() + "//" + bloc_loc.getBlockZ();
      try
      {
        this.plugin.c = this.plugin.MySQL.open();
        PreparedStatement ps = this.plugin.c.prepareStatement("INSERT INTO `spawnerlist`(spawnerlocation, player, spawnertype) VALUES (?, ?, ?);");
        ps.setString(1, spawnerLocation);
        ps.setString(2, player.getName());
        ps.setString(3, spawnerType);
        ps.executeUpdate();
        this.plugin.c.close();
      }
      catch (SQLException e1)
      {
        this.plugin.log.severe("Erreur lors de l'insertion de donnee dans la BDD.");
        e1.printStackTrace();
      }
    }
  }
  
  @EventHandler(priority=EventPriority.MONITOR, ignoreCancelled=true)
  public void onBlockBreakEvent(BlockBreakEvent e)
  {
    if (e.getBlock().getTypeId() == 52)
    {
      Player player = e.getPlayer();
      Block bloc = e.getBlock();
      Location bloc_loc = bloc.getLocation();
      String[] world = bloc_loc.getWorld().toString().split("=");
      String spawnerLocation = world[1].replace("}", "") + "//" + bloc_loc.getBlockX() + "//" + bloc_loc.getBlockY() + "//" + bloc_loc.getBlockZ();
      CreatureSpawner mobSpawner = (CreatureSpawner)bloc.getState();
      try
      {
        this.plugin.c = this.plugin.MySQL.open();
        PreparedStatement ps = this.plugin.c.prepareStatement("SELECT * FROM spawnerlist WHERE spawnerlocation = ?;");
        ps.setString(1, spawnerLocation);
        ResultSet res = ps.executeQuery();
        if (!res.next())
        {
          player.sendMessage(ChatColor.RED + "Le spawner que vous essayez de detruire est un spawner naturel ou n'a pas de proprietaire. Pour plus d'infos contactez un admin.");
          e.setCancelled(true);
        }
        else if (!res.getString("player").equals(player.getName()))
        {
          player.sendMessage(ChatColor.RED + "Ce spawner ne vous appartient pas ! Il appartient à  " + res.getString("player") + ".");
          e.setCancelled(true);
        }
        else if ((res.getString("player").equals(player.getName())) && (mobSpawner.getCreatureTypeName().equalsIgnoreCase("bat")) && (player.getWorld().getEnvironment() == World.Environment.NETHER))
        {
          if (player.getInventory().firstEmpty() == -1)
          {
            player.sendMessage(ChatColor.RED + this.functions.addPrefix("Merci de vider votre inventaire pour recevoir le spawner lorsque vous le cassez ! "));
            e.setCancelled(true);
          }
          else if (this.functions.removeSpawnerDB(spawnerLocation))
          {
            this.functions.giveSpawner(player, "skeleton");
          }
        }
        else if (res.getString("player").equals(player.getName()))
        {
          if (player.getInventory().firstEmpty() == -1)
          {
            player.sendMessage(ChatColor.RED + this.functions.addPrefix("Merci de vider votre inventaire pour recevoir le spawner lorsque vous le cassez ! "));
            e.setCancelled(true);
          }
          else if (this.functions.removeSpawnerDB(spawnerLocation))
          {
            this.functions.giveSpawner(player, mobSpawner.getCreatureTypeName());
          }
        }
        this.plugin.c.close();
      }
      catch (SQLException e1)
      {
        this.plugin.log.severe("Erreur lors de l'insertion de donnee dans la BDD.");
        e1.printStackTrace();
      }
    }
  }
  
  @EventHandler(priority=EventPriority.LOW)
  public void onEntityExplode(EntityExplodeEvent event)
  {
    List<?> destroyed = event.blockList();
    Iterator<?> it = destroyed.iterator();
    while (it.hasNext())
    {
      Block block = (Block)it.next();
      if (block.getTypeId() == 52) {
        it.remove();
      }
    }
  }
  
  @EventHandler(priority=EventPriority.LOW)
  public void onBlockExpEvent(BlockExpEvent e)
  {
    if (e.getBlock().getTypeId() == 52) {
      e.setExpToDrop(0);
    }
  }
}
