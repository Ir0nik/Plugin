package fr.sunshine.spawner;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Logger;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.scheduler.BukkitScheduler;

public class SunshineSpawnerFunctions
{
  private SunshineSpawner plugin;
  private final String prefix = SunshineSpawner.getPrefix();
  private final String NO_PERM = ChatColor.translateAlternateColorCodes('&', this.prefix + " &cVous n'avez pas la permission requise !");
  
  public SunshineSpawnerFunctions(SunshineSpawner plugin)
  {
    this.plugin = plugin;
  }
  
  public boolean showHelp(CommandSender sender)
  {
    Player player = null;
    if ((sender instanceof Player)) {
      player = (Player)sender;
    }
    if (player == null)
    {
      sender.sendMessage(ChatColor.YELLOW + "==== SunshineSpawner - Aide - Commandes pour console ====");
      sender.sendMessage(ChatColor.GREEN + "- /SunshineSpawner give <player> <spawnerType> : Give un spawner au joueur specifie!");
      sender.sendMessage(ChatColor.YELLOW + "==== SunshineSpawner - Aide - Commandes pour console ====");
      return true;
    }
    sender.sendMessage(ChatColor.YELLOW + "*** SunshineSpawner - Aide ***");
    sender.sendMessage(ChatColor.YELLOW + "Les commandes disponibles sont :");
    if (player.hasPermission("sunshinespawner.give")) {
      sender.sendMessage(ChatColor.GREEN + "- /SunshineSpawner give <player> <spawnerType> : Give un spawner au joueur specifie!");
    }
    if (player.hasPermission("sunshinespawner.chgowner")) {
      sender.sendMessage(ChatColor.GREEN + "- /SunshineSpawner chgowner <pseudoDuNouveauProprietaire> : Change le propriétaire d'un spawner !");
    }
    if (player.hasPermission("sunshinespawner.remove")) {
      sender.sendMessage(ChatColor.GREEN + "- /SunshineSpawner remove : Detruit un spawner qui ne vous appartient pas !");
    }
    if ((player.hasPermission("sunshinespawner.takeall")) && 
      (player.hasPermission("sunshinespawner.takeall.others"))) {
      sender.sendMessage(ChatColor.GREEN + "- /SunshineSpawner takeAll <proprietaireSpawner> : DETRUIT tous les spawners du joueurs et vous les donne dans votre inventaire !");
    }
    if (player.hasPermission("sunshinespawner.info")) {
      sender.sendMessage(ChatColor.GRAY + "- /SunshineSpawner info : Affiche la liste de vos spawners !");
    }
    if (player.hasPermission("sunshinespawner.info.others")) {
      sender.sendMessage(ChatColor.GREEN + "- /SunshineSpawner info <joueur> : Affiche la liste des spawners d'un joueur");
    }
    sender.sendMessage(ChatColor.RED + "Vous pouvez également casser vos spawners Ã  la pioche pour les récupérer !");
    return true;
  }
  
  public Location str2loc(String strLoc)
  {
    String[] arrayLoc = strLoc.split("//");
    
    Location location = new Location(Bukkit.getWorld(arrayLoc[0]), Double.parseDouble(arrayLoc[1]), Double.parseDouble(arrayLoc[2]), Double.parseDouble(arrayLoc[3]));
    return location;
  }
  
  public String addPrefix(String message)
  {
    return this.prefix + message;
  }
  
  public String noPermissions()
  {
    return this.NO_PERM;
  }
  
  public boolean chgOwner(String pos, Player newOwner, CommandSender sender)
  {
    try
    {
      this.plugin.c = this.plugin.MySQL.open();
      PreparedStatement ps = this.plugin.c.prepareStatement("UPDATE `spawnerlist` SET player = ? WHERE spawnerlocation = ?;");
      ps.setString(1, newOwner.getName());
      ps.setString(2, pos);
      ps.executeUpdate();
      sender.sendMessage(ChatColor.GREEN + addPrefix(new StringBuilder("Le nouveau propriétaire du spawner est :").append(newOwner.getName()).toString()));
      this.plugin.c.close();
      return true;
    }
    catch (SQLException e)
    {
      sender.sendMessage(ChatColor.RED + addPrefix("Erreur lors du changement de propriétaire."));
      e.getStackTrace();
    }
    return false;
  }
  
  public boolean removeSpawnerDB(String spawnerLocation)
  {
    try
    {
      this.plugin.c = this.plugin.MySQL.open();
      PreparedStatement ps = this.plugin.c.prepareStatement("DELETE FROM spawnerlist WHERE spawnerlocation = ?;");
      ps.setString(1, spawnerLocation);
      ps.executeUpdate();
      this.plugin.c.close();
      return true;
    }
    catch (SQLException e)
    {
      e.printStackTrace();
    }
    return false;
  }
  
  public String setSpawner(Player player, Block bloc)
  {
    int itemId = player.getInventory().getHeldItemSlot();
    ItemStack iStack = player.getInventory().getItem(itemId);
    short durability = iStack.getDurability();
    if (durability < 1) {
      durability = 90;
    }
    EntityType spawnerType = EntityType.fromId(durability);
    short spawnerId = 90;
    String name = "";
    if (spawnerType == null)
    {
      name = "Pig";
      durability = 90;
    }
    else
    {
      name = spawnerType.getName();
      spawnerId = spawnerType.getTypeId();
    }
    if (name.isEmpty())
    {
      name = "Pig";
      durability = 90;
    }
    else
    {
      String f = name.substring(0, 1);
      name = name.toLowerCase().replaceFirst(f, f.toUpperCase());
    }
    Bukkit.getScheduler().scheduleSyncDelayedTask(this.plugin, new SunshineSpawnerTasks(spawnerId, durability, bloc, this.plugin, player), 0L);
    return name;
  }
  
  public void getInfo(String playerName, CommandSender sender)
  {
    try
    {
      this.plugin.c = this.plugin.MySQL.open();
      PreparedStatement ps = this.plugin.c.prepareStatement("SELECT * FROM spawnerlist WHERE player = ?;");
      ps.setString(1, playerName);
      ResultSet res = ps.executeQuery();
      if (!res.next())
      {
        sender.sendMessage(ChatColor.YELLOW + addPrefix(new StringBuilder("Le joueur ").append(playerName).append(" n'a pas de spawner.").toString()));
        return;
      }
      sender.sendMessage(ChatColor.YELLOW + addPrefix(new StringBuilder("Le joueur ").append(playerName).append(" posséde le(s) spawner(s) :").toString()));
      res.beforeFirst();
      while (res.next())
      {
        Location loc = str2loc(res.getString("spawnerlocation"));
        String[] world = loc.getWorld().toString().split("=");
        String location = "Map: " + world[1].replace("}", "") + ";X=" + Integer.toString(loc.getBlockX()) + ";Y=" + Integer.toString(loc.getBlockY()) + ";Z=" + Integer.toString(loc.getBlockZ());
        sender.sendMessage(ChatColor.YELLOW + "Spawner numéro : " + ChatColor.GREEN + res.getString("ID") + ChatColor.YELLOW + "\n Position du spawner : " + ChatColor.GREEN + location);
      }
      this.plugin.c.close();
      return;
    }
    catch (SQLException e)
    {
      e.printStackTrace();
    }
  }
  
  public boolean giveSpawner(Player player, String spawnerEntityType)
  {
    EntityType spawnerType = EntityType.fromName(spawnerEntityType);
    short durability = spawnerType.getTypeId();
    String spawnerName = spawnerType.getName();
    ItemStack newSpawner = new ItemStack(Material.MOB_SPAWNER, 1, durability);
    newSpawner.setDurability(durability);
    ItemMeta im = newSpawner.getItemMeta();
    im.setDisplayName("Spawner de " + spawnerName);
    newSpawner.setItemMeta(im);
    PlayerInventory inventory = player.getInventory();
    if (inventory.firstEmpty() == -1)
    {
      player.sendMessage(ChatColor.RED + addPrefix("Vous n'avez pas recu le spawner car votre inventaire est plein ! Veuillez vider votre inventaire, il vous sera distribué dans 30 sec"));
    }
    else
    {
      int invSlot = inventory.firstEmpty();
      inventory.setItem(invSlot, newSpawner);
      player.sendMessage(ChatColor.YELLOW + addPrefix(new StringBuilder("Vous avez recu un spawner de ").append(spawnerName).toString()));
      return true;
    }
    return false;
  }
  
  public void takeAllSpawner(String playerOwner, String playerReceiver)
  {
    Player receiver = Bukkit.getPlayer(playerReceiver);
    Player owner = Bukkit.getPlayer(playerOwner);
    try
    {
      this.plugin.c = this.plugin.MySQL.open();
      PreparedStatement ps = this.plugin.c.prepareStatement("SELECT * FROM spawnerlist WHERE player = ?;");
      ps.setString(1, playerOwner);
      ResultSet res = ps.executeQuery();
      if (!res.next())
      {
        owner.sendMessage(ChatColor.YELLOW + addPrefix(new StringBuilder("Le joueur ").append(receiver.getName()).append(" n'a pas de spawner.").toString()));
        return;
      }
      res.beforeFirst();
      while (res.next())
      {
        if (Bukkit.getPlayer(playerReceiver).getInventory().firstEmpty() == -1)
        {
          Bukkit.getPlayer(playerReceiver).sendMessage(ChatColor.RED + addPrefix("Merci de vider votre inventaire pour recevoir le(s) spawner(s) grÃ Â¢ce au takeAll ! "));
          return;
        }
        if (!removeSpawnerDB(res.getString("spawnerlocation")))
        {
          owner.sendMessage(ChatColor.RED + addPrefix("Erreur lors de la commande. Merci de reporter ceci Ã Â  un admin ! Erreur: 1"));
          return;
        }
        Location loc = str2loc(res.getString("spawnerlocation"));
        if (!loc.getBlock().breakNaturally())
        {
          owner.sendMessage(ChatColor.RED + addPrefix("Erreur lors de la commande. Merci de reporter ceci Ã Â  un admin ! Erreur: 2"));
          return;
        }
        if (!giveSpawner(receiver, res.getString("spawnertype"))) {
          return;
        }
      }
      owner.sendMessage(ChatColor.GREEN + addPrefix("Tous les spawners ont étés distribués !"));
      this.plugin.c.close();
      return;
    }
    catch (SQLException e)
    {
      this.plugin.log.severe("Erreur lors de la lecture de donnees dans la BDD.");
      e.printStackTrace();
    }
  }
}
