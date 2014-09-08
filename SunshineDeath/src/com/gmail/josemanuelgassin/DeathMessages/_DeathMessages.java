package com.gmail.josemanuelgassin.DeathMessages;

import java.util.HashMap;
import java.util.logging.Logger;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.PluginCommand;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

public class _DeathMessages
  extends JavaPlugin
{
  Logger logger = Logger.getLogger("Minecraft");
  PluginDescriptionFile pdfFile;
  _DeathMessages main = this;
  Commands co = new Commands(this.main);
  Util_Goty u_Goty = new Util_Goty(this.main);
  Util_VersionChecker u_VersionChecker = new Util_VersionChecker(this.main);
  Metodos_Generales m_Generales = new Metodos_Generales(this.main);
  String nombrePlugin;
  String versionPlugin;
  FileConfiguration FC;
  String label = ChatColor.DARK_RED + "[" + ChatColor.GOLD + " Death Messages " + ChatColor.DARK_RED + "]" + ChatColor.WHITE + " - ";
  String prel = ChatColor.DARK_RED + "[" + ChatColor.GOLD;
  String posl = ChatColor.DARK_RED + "]" + ChatColor.WHITE + " - " + ChatColor.GREEN;
  HashMap<String, Integer> jugadoresToggleados = new HashMap();
  
  public void onDisable()
  {
    if (this.pdfFile == null) {
      this.pdfFile = getDescription();
    }
    this.logger.info("<<[ " + this.pdfFile.getName() + " ]>> " + " [ Version ]=[ " + this.pdfFile.getVersion() + " ] [ Disabled! ]");
  }
  
  public void onEnable()
  {
    this.pdfFile = getDescription();
    cargarCFG(true);
    
    PluginManager pm = Bukkit.getPluginManager();
    pm.registerEvents(new Listener_Jugador(this), this);
    
    getCommand("dm").setExecutor(this.co);
    getCommand("deathmessages").setExecutor(this.co);
    
    this.logger.info("<<[ " + this.nombrePlugin + " ]>> " + " [ Version ]=[ " + this.versionPlugin + " ] [ Successfully Loaded! ]");
  }
  
  void cargarCFG(boolean inicio)
  {
    this.pdfFile = getDescription();
    this.nombrePlugin = this.pdfFile.getName();
    this.versionPlugin = this.pdfFile.getVersion();
    if (inicio) {
      saveDefaultConfig();
    }
    if (!inicio) {
      reloadConfig();
    }
    this.FC = getConfig();
    

    this.u_VersionChecker.configVersionChecker();
  }
}
