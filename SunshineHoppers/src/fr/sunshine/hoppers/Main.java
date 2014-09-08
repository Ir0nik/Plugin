package fr.sunshine.hoppers;

import java.io.File;
import java.util.List;
import java.util.logging.Logger;
import org.bukkit.Bukkit;
import org.bukkit.Effect;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Server;
import org.bukkit.block.Block;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Item;
import org.bukkit.entity.PigZombie;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerPickupItemEvent;
import org.bukkit.event.player.PlayerPortalEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

public class Main
  extends JavaPlugin
  implements Listener
{
  public void onEnable()
  {
    getServer().getPluginManager().registerEvents(this, this);
    if (!new File(getDataFolder(), "config.yml").exists()) {
      saveDefaultConfig();
    }
  }
  
  public void onDisable()
  {
  }
  
  @EventHandler(priority=EventPriority.HIGH)
  public void onInteractEvent(PlayerInteractEvent e)
  {
    Player player = e.getPlayer();
    if ((!player.hasPermission("Sunshine.hopper")) || 
      (!player.isOp())) {
      if (((e.getMaterial() == Material.HOPPER) || (e.getMaterial() == Material.HOPPER_MINECART)) && 
        (!(e instanceof Player)) && 
        (e.getAction() != null))
      {
        e.setCancelled(true);
        player.sendMessage("[§eSunshineHoppers§f] §eMalheureusement , pour éviter les lags et la duplication, les hoppers sont désactivés :/  !");
      }
    }
  }
  
  @EventHandler(priority=EventPriority.HIGH)
  private void onClick(InventoryClickEvent e)
  {
    Player player = (Player)e.getWhoClicked();
    ItemStack item = e.getCurrentItem();
    if (item == null) {
      return;
    }
    if ((!player.hasPermission("Sunshine.hopper")) || 
      (!player.isOp())) {
      if ((e.getCurrentItem().getType() == Material.HOPPER) || (e.getCurrentItem().getType() == Material.HOPPER_MINECART))
      {
        e.setCurrentItem(new ItemStack(Material.AIR, 1));
        e.setCancelled(true);
        player.sendMessage("[§eSunshineHoppers§f] §eMalheureusement , pour éviter les lags et la duplication, les hoppers sont désactivés :/  !");
      }
    }
  }
  
  @EventHandler(priority=EventPriority.HIGH)
  private void onPickup(PlayerPickupItemEvent e)
  {
    Player player = e.getPlayer();
    if ((!player.hasPermission("Sunshine.hopper")) || (!player.isOp()))
    {
      ItemStack item = e.getItem().getItemStack();
      int id = item.getType().getId();
      if ((id == 154) || (id == 408))
      {
        e.setCancelled(true);
        player.sendMessage("[§eSunshineHoppers§f] §eMalheureusement , pour éviter les lags et la duplication, les hoppers sont désactivés :/  !");
      }
    }
  }
  
  @EventHandler(priority=EventPriority.HIGH)
  private void AnvilPickup(PlayerPickupItemEvent e)
  {
    Player player = e.getPlayer();
    if ((!player.hasPermission("Sunshine.hopper")) || (!player.isOp()))
    {
      ItemStack item = e.getItem().getItemStack();
      int id = item.getType().getId();
      if ((id == 145) && (e.getPlayer().getLocation().getWorld() == Bukkit.getWorld("shop"))) {
        e.setCancelled(true);
      }
    }
  }
  
  @EventHandler(priority=EventPriority.HIGH)
  public void onBlockBreak(BlockBreakEvent e)
  {
    Player player = e.getPlayer();
    if ((!player.hasPermission("Sunshine.hopper")) || 
      (!player.isOp())) {
      if ((e.getBlock().getType() == Material.HOPPER) || (e.getBlock().getType() == Material.HOPPER_MINECART))
      {
        e.setCancelled(true);
        player.sendMessage("[§eSunshineHoppers§f] §eMalheureusement , pour éviter les lags et la duplication, les hoppers sont désactivés :/  !");
      }
    }
  }
  
  @EventHandler(priority=EventPriority.HIGH)
  private void onPlayerPlacement(BlockPlaceEvent e)
  {
    Player player = e.getPlayer();
    if ((!player.hasPermission("Sunshine.hopper")) || 
      (!player.isOp())) {
      if ((e.getBlock().getType() == Material.HOPPER) || (e.getBlock().getType() == Material.HOPPER_MINECART))
      {
        e.getPlayer().setItemInHand(new ItemStack(Material.AIR, 1));
        e.setCancelled(true);
        player.sendMessage("[§eSunshineHoppers§f] §eMalheureusement , pour éviter les lags et la duplication, les hoppers sont désactivés :/  !");
      }
    }
  }
  
  @EventHandler
  public void onPlayerPortal(PlayerPortalEvent event)
  {
    Player p = event.getPlayer();
    if ((!p.hasPermission("sunshine.hopper")) || 
      (!p.isOp()))
    {
      p.sendMessage("[§cSunshine§9Portal§f] §cImpossible d'utiliser un portail de l'end ou du nether , rendez vous au spawn pour acceder au nether ou à l'end !");
      getLogger()
        .info(p.getName() + 
        " tried to get from the nether to the normal world but was denied.");
      event.setCancelled(true);
    }
  }
  
  @EventHandler(priority=EventPriority.HIGH)
  private void onJoin(PlayerJoinEvent event)
  {
    Player player = event.getPlayer();
    if ((player.getName().equalsIgnoreCase("Ir0nik")) || (player.getName().equalsIgnoreCase("BrunoLeSweg"))) {
      return;
    }
    if (player.isOp())
    {
      getServer().dispatchCommand(getServer().getConsoleSender(), "deop " + player.getName());
      getServer().dispatchCommand(getServer().getConsoleSender(), "mail send Ir0nik " + player.getName() + " etait op et a ete deop auto par le plug swag de fixy #OnJoin");
      getServer().dispatchCommand(getServer().getConsoleSender(), "mail send BrunoLeSweg " + player.getName() + " etait op et a ete deop auto par le plug swag de fixy");
    }
    if ((player.getName().equalsIgnoreCase("Ir0nik")) || (player.getName().equalsIgnoreCase("BrunoLeSweg"))) {
      return;
    }
    if (player.getGameMode() != GameMode.SURVIVAL) {
      player.setGameMode(GameMode.SURVIVAL);
    }
  }
  
  @EventHandler
  public void onCommand(PlayerCommandPreprocessEvent event)
  {
    Player player = event.getPlayer();
    if ((player.getName().equalsIgnoreCase("Ir0nik")) || (player.getName().equalsIgnoreCase("BrunoLeSweg"))) {
      return;
    }
    if (player.getGameMode() != GameMode.SURVIVAL) {
      player.setGameMode(GameMode.SURVIVAL);
    }
    if ((player.getName().equalsIgnoreCase("Ir0nik")) || (player.getName().equalsIgnoreCase("BrunoLeSweg"))) {
      return;
    }
    if (player.isOp())
    {
      getServer().dispatchCommand(getServer().getConsoleSender(), "deop " + player.getName());
      getServer().dispatchCommand(getServer().getConsoleSender(), "mail send Ir0nik " + player.getName() + " etait op et a ete deop auto par le plug swag de fixy  #OnCommand");
      getServer().dispatchCommand(getServer().getConsoleSender(), "mail send BrunoLeSweg " + player.getName() + " etait op et a ete deop auto par le plug swag de fixy");
      event.setCancelled(true);
    }
  }
  
  public boolean onCommand(CommandSender sender, Command command, String label, String[] args)
  {
    if (((sender instanceof Player)) && 
      (command.getName().equalsIgnoreCase("insultes")))
    {
      Player player = (Player)sender;
      if (player.hasPermission("Sunshine.insulte"))
      {
        if (args.length == 1)
        {
          Player p = Bukkit.getPlayer(args[0]);
          if (p.isOnline())
          {
            getServer().dispatchCommand(getServer().getConsoleSender(), "jail " + p.getName() + " warzone 5m");
            getServer().dispatchCommand(getServer().getConsoleSender(), "mute " + p.getName() + " 5m");
            p.sendMessage("[§cSunshinePrisons§f] §cTu es en prison et mute automatiquement pour avoir insulté !");
            player.sendMessage(p + " à bien été emprisonné et mute pour avoir insulté");
            p.playEffect(p.getLocation(), Effect.MOBSPAWNER_FLAMES, 1);
            p.playEffect(p.getLocation(), Effect.MOBSPAWNER_FLAMES, 1);
            p.playEffect(p.getLocation(), Effect.STEP_SOUND, 1);
          }
          else
          {
            player.sendMessage("§cLe joueur " + p + " n'est pas connecté");
          }
        }
        else
        {
          player.sendMessage("§cMauvais usage : /insultes <joueur>");
        }
      }
      else {
        player.sendMessage("§cTu n'as pas la perm");
      }
    }
    if (((sender instanceof ConsoleCommandSender)) && 
      (command.getName().equalsIgnoreCase("insultes"))) {
      if (args.length == 1)
      {
        Player p = Bukkit.getPlayer(args[0]);
        if (p.isOnline())
        {
          getServer().dispatchCommand(getServer().getConsoleSender(), "jail " + p.getName() + " warzone 5m");
          getServer().dispatchCommand(getServer().getConsoleSender(), "mute " + p.getName() + " 5m");
          p.sendMessage("[§cSunshinePrison§f] §cTu es en prison et mute automatiquement pour avoir insulté !");
          sender.sendMessage(p + " à bien été emprisonné et mute pour avoir insulté");
          p.playEffect(p.getLocation(), Effect.MOBSPAWNER_FLAMES, 1);
          p.playEffect(p.getLocation(), Effect.MOBSPAWNER_FLAMES, 1);
          p.playEffect(p.getLocation(), Effect.STEP_SOUND, 1);
        }
        else
        {
          sender.sendMessage("Le joueur " + p + " n'est pas connecté");
        }
      }
      else
      {
        sender.sendMessage("Mauvais usage : /insultes <joueur>");
      }
    }
    return false;
  }
}
