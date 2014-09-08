package fr.sunshine.spawner;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;

public class SunshineSpawnerCommandExecutor
  implements CommandExecutor
{
  private SunshineSpawnerFunctions functions;
  
  public SunshineSpawnerCommandExecutor(SunshineSpawner plugin)
  {
    this.functions = new SunshineSpawnerFunctions(plugin);
  }
  
  public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args)
  {
    Player player = null;
    if ((sender instanceof Player)) {
      player = (Player)sender;
    }
    if (player == null)
    {
      if (((cmd.getName().equalsIgnoreCase("sunshinespawner")) && (args.length > 2)) || ((cmd.getName().equalsIgnoreCase("ss")) && (args.length > 2) && 
        (args[0].equals("give"))))
      {
        Player playerReceiver = Bukkit.getPlayerExact(args[1]);
        String spawnerEntityType = args[2];
        if (playerReceiver == null)
        {
          sender.sendMessage(this.functions.addPrefix(ChatColor.RED + "Le joueur n'est pas connecté!"));
          return true;
        }
        this.functions.giveSpawner(playerReceiver, spawnerEntityType);
        return true;
      }
      sender.sendMessage(this.functions.addPrefix(ChatColor.RED + "Commande invalide, erreur d'argument !"));
      return this.functions.showHelp(sender);
    }
    if (((cmd.getName().equalsIgnoreCase("sunshinespawner")) && (args.length > 0)) || ((cmd.getName().equalsIgnoreCase("ss")) && (args.length > 0)))
    {
      if (args[0].equals("help")) {
        return this.functions.showHelp(sender);
      }
      if (args == null) {
        return this.functions.showHelp(sender);
      }
      if ((args[0].equals("chgowner")) && (args.length == 2))
      {
        if (!player.hasPermission("sunshinespawner.chgowner"))
        {
          sender.sendMessage(this.functions.noPermissions());
          return true;
        }
        Player playerReceiver = Bukkit.getPlayer(args[1]);
        if (playerReceiver == null)
        {
          sender.sendMessage(this.functions.addPrefix(ChatColor.RED + "Le joueur n'est pas en ligne !"));
          return true;
        }
        if (player.getTargetBlock(null, 20).getType() == Material.MOB_SPAWNER)
        {
          Location bloc_loc = player.getTargetBlock(null, 20).getLocation();
          String[] world = bloc_loc.getWorld().toString().split("=");
          String spawnerLocation = world[1].replace("}", "") + "//" + bloc_loc.getBlockX() + "//" + bloc_loc.getBlockY() + "//" + bloc_loc.getBlockZ();
          this.functions.chgOwner(spawnerLocation, playerReceiver, sender);
          return true;
        }
        sender.sendMessage(this.functions.addPrefix(ChatColor.RED + "Le bloc que vous visez n'est pas un mob spawner !"));
        return true;
      }
      if ((args[0].equals("give")) && (args.length > 2))
      {
        if (!player.hasPermission("sunshinespawner.give"))
        {
          sender.sendMessage(this.functions.noPermissions());
          return true;
        }
        Player playerReceiver = Bukkit.getPlayerExact(args[1]);
        String spawnerEntityType = args[2];
        if (playerReceiver == null)
        {
          sender.sendMessage(this.functions.addPrefix(ChatColor.RED + "Le joueur n'est pas connecté!"));
          return true;
        }
        if (EntityType.fromName(spawnerEntityType) == null)
        {
          sender.sendMessage(this.functions.addPrefix(ChatColor.RED + "Le type de spawner n'est pas valide !"));
          return true;
        }
        this.functions.giveSpawner(playerReceiver, spawnerEntityType);
        return true;
      }
      if (args[0].equals("info"))
      {
        if (!player.hasPermission("sunshinespawner.info"))
        {
          sender.sendMessage(this.functions.noPermissions());
          return true;
        }
        if (args.length == 2)
        {
          if (player.hasPermission("sunshinespawner.info.others"))
          {
            this.functions.getInfo(args[1], sender);
          }
          else
          {
            sender.sendMessage(this.functions.noPermissions());
            return true;
          }
        }
        else {
          this.functions.getInfo(player.getName(), sender);
        }
      }
      else if (args[0].equals("remove"))
      {
        if (!player.hasPermission("sunshinespawner.remove"))
        {
          sender.sendMessage(this.functions.noPermissions());
          return true;
        }
        if (args.length != 1)
        {
          sender.sendMessage(this.functions.addPrefix(ChatColor.RED + "Commande invalide, erreur d'argument !"));
        }
        else
        {
          if (player.getTargetBlock(null, 20).getType() == Material.MOB_SPAWNER)
          {
            String spawnerLocation = player.getTargetBlock(null, 20).getLocation().toString();
            boolean ans = this.functions.removeSpawnerDB(spawnerLocation);
            if (ans)
            {
              player.getTargetBlock(null, 20).breakNaturally();
              sender.sendMessage(this.functions.addPrefix(ChatColor.GREEN + "Spawner supprimé avec succès !"));
            }
            else
            {
              sender.sendMessage(this.functions.addPrefix(ChatColor.RED + "Erreur lors de la suppresion du spawner !"));
            }
            return true;
          }
          sender.sendMessage(this.functions.addPrefix(ChatColor.RED + "Le bloc que vous visez n'est pas un mob spawner !"));
        }
      }
      else if (args[0].equals("takeall"))
      {
        if (!player.hasPermission("sunshinespawner.takeall"))
        {
          sender.sendMessage(this.functions.noPermissions());
          return true;
        }
        if (args.length == 2)
        {
          if (!player.hasPermission("sunshinespawner.takeall.others"))
          {
            sender.sendMessage(this.functions.noPermissions());
            return true;
          }
          this.functions.takeAllSpawner(args[1], player.getName());
        }
        else
        {
          return this.functions.showHelp(sender);
        }
      }
      else
      {
        sender.sendMessage(ChatColor.RED + "Commande invalide, erreur d'argument !");
      }
    }
    else if ((cmd.getName().equalsIgnoreCase("sunshinespawner")) || (cmd.getName().equalsIgnoreCase("ss")))
    {
      return this.functions.showHelp(sender);
    }
    return true;
  }
}
