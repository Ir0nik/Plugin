package com.gmail.josemanuelgassin.DeathMessages;

import java.io.File;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.FileConfigurationOptions;
import org.bukkit.plugin.PluginDescriptionFile;

class Util_VersionChecker
{
  _DeathMessages main;
  
  Util_VersionChecker(_DeathMessages instance)
  {
    this.main = instance;
  }
  
  void configVersionChecker()
  {
    String version = this.main.FC.getString("Version");
    boolean versionMala = false;
    

    File conf = new File(this.main.getDataFolder() + "/config.yml");
    if ((!this.main.pdfFile.getVersion().equals(version)) || (!conf.exists())) {
      versionMala = true;
    }
    if (versionMala)
    {
      File oldconf = new File(this.main.getDataFolder() + "/old_config.yml");
      conf.renameTo(oldconf);
      conf.delete();
      this.main.FC.options().copyDefaults(true);
      this.main.saveDefaultConfig();
      this.main.logger.log(Level.WARNING, "<<[ " + this.main.pdfFile.getName() + " ]>> " + "Fichero 'config.yml' anticuado, renombrado a 'old_config.yml' y creado fichero actualizado.");
    }
  }
}
