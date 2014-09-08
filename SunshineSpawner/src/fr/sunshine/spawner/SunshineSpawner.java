package fr.sunshine.spawner;

import java.io.File;
import java.sql.Connection;
import java.sql.Statement;
import java.util.logging.Logger;
import org.bukkit.ChatColor;
import org.bukkit.Server;
import org.bukkit.command.PluginCommand;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

public class SunshineSpawner
  extends JavaPlugin
{
  private static SunshineSpawner instance;
  private static String prefix = ChatColor.translateAlternateColorCodes('&', "&f[&eSunshineSpawner&f]&e ");
  Logger log;
  private SunshineSpawnerCommandExecutor commandExecutor = new SunshineSpawnerCommandExecutor(this);
  int taskID;
  MySQL MySQL;
  Connection c = null;
  
  public boolean loadConfigMySQL()
  {
    String user = getConfig().getString("configbdd.user");
    String pass = getConfig().getString("configbdd.password");
    String host = getConfig().getString("configbdd.server");
    String db_name = getConfig().getString("configbdd.database_name");
    String port = getConfig().getString("configbdd.port");
    this.MySQL = new MySQL(host, port, db_name, user, pass);
    try
    {
      this.c = this.MySQL.open();
      Statement statement = this.c.createStatement();
      statement.executeUpdate("CREATE TABLE IF NOT EXISTS `spawnerlist` (`ID` int(255) NOT NULL AUTO_INCREMENT,`spawnerlocation` varchar(255) NOT NULL,`player` varchar(255) NOT NULL,`spawnertype` varchar(255) NOT NULL,PRIMARY KEY (`ID`)) ENGINE=InnoDB DEFAULT CHARSET=latin1 AUTO_INCREMENT=1 ;");
      
      this.c.close();
      return true;
    }
    catch (Exception e)
    {
      this.log.severe("Erreur lors de la connexion a la BDD !");
    }
    return false;
  }
  
  public void onEnable()
  {
    instance = this;
    this.log = getLogger();
    if (!new File(getDataFolder(), "config.yml").exists())
    {
      saveDefaultConfig();
      this.log.info("Creation de la config ...");
    }
    if (!loadConfigMySQL())
    {
      this.log.severe("Le plugin va s'eteindre. Merci de configurer les informations de la base de donnes puis de redemarrer le serveur.");
      setEnabled(false);
      return;
    }
    getCommand("sunrisespawner").setExecutor(this.commandExecutor);
    getCommand("ss").setExecutor(this.commandExecutor);
    
    PluginManager pm = getServer().getPluginManager();
    pm.registerEvents(new SunshineSpawnerListener(this), this);
  }
  
  public static SunshineSpawner getInstance()
  {
    return instance;
  }
  
  public static String getPrefix()
  {
    return prefix;
  }
}
