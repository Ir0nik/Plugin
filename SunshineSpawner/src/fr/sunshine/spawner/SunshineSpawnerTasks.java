package fr.sunshine.spawner;

import org.bukkit.ChatColor;
import org.bukkit.World;
import org.bukkit.World.Environment;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.block.CreatureSpawner;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;

public class SunshineSpawnerTasks
  implements Runnable
{
  short entityID;
  Block block;
  short d;
  SunshineSpawner plugin;
  Player player;
  
  public SunshineSpawnerTasks(short entityID, short d, Block block, SunshineSpawner plugin, Player player)
  {
    this.entityID = entityID;
    this.block = block;
    this.d = d;
    this.plugin = plugin;
    this.player = player;
  }
  
  public boolean setSpawner(Block target, String arg)
  {
    EntityType type = EntityType.fromName(arg);
    if (type == null) {
      return false;
    }
    CreatureSpawner testSpawner = null;
    try
    {
      testSpawner = (CreatureSpawner)target.getState();
    }
    catch (Exception e)
    {
      return false;
    }
    testSpawner.setSpawnedType(type);
    target.getState().update();
    return true;
  }
  
  public boolean setSpawner1(Block target, EntityType bat)
  {
    EntityType type = EntityType.BAT;
    if (type == null) {
      return false;
    }
    CreatureSpawner testSpawner = null;
    try
    {
      testSpawner = (CreatureSpawner)target.getState();
    }
    catch (Exception e)
    {
      return false;
    }
    testSpawner.setSpawnedType(type);
    target.getState().update();
    return true;
  }
  
  public void run()
  {
    EntityType spawnerType = EntityType.fromId(this.d);
    if (spawnerType == null) {
      return;
    }
    if ((spawnerType == EntityType.SKELETON) && (this.player.getWorld().getEnvironment() == World.Environment.NETHER))
    {
      this.player.sendMessage("§cIl est interdit de poser des spawners de squelettes dans le nether , veuillez le casser ! (Voir reglement)");
      setSpawner1(this.block, EntityType.BAT);
      return;
    }
    if (setSpawner(this.block, spawnerType.getName())) {
      this.player.sendMessage(ChatColor.GREEN + "Spawner de " + spawnerType.getName() + " placé");
    } else {
      this.player.sendMessage(ChatColor.RED + "Erreur lors du placement de spawner.");
    }
  }
}
