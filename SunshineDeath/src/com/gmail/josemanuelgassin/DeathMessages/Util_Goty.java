package com.gmail.josemanuelgassin.DeathMessages;

import java.util.List;
import java.util.Random;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

class Util_Goty
{
  _DeathMessages main;
  
  Util_Goty(_DeathMessages instance)
  {
    this.main = instance;
  }
  
  Random r = new Random();
  
  boolean tienePerm(CommandSender s, String p)
  {
    if (s.hasPermission(p)) {
      return true;
    }
    s.sendMessage(this.main.label + ChatColor.RED + "You don't have the required permissions for this command!");
    return false;
  }
  
  void recargarConfig(CommandSender s)
  {
    this.main.cargarCFG(false);
    s.sendMessage(this.main.label + ChatColor.GREEN + "Config has been successfully reloaded!");
  }
  
  void listaComandos(CommandSender s)
  {
    s.sendMessage(this.main.label + ChatColor.GREEN + "Command List:");
    s.sendMessage(this.main.prel + " 1 " + this.main.posl + "/dm : Shows this list of commands.");
    s.sendMessage(this.main.prel + " 2 " + this.main.posl + "/dm all : Toggles both, Death and General Messages.");
    s.sendMessage(this.main.prel + " 3 " + this.main.posl + "/dm dm : Toggles death messages on/off.");
    s.sendMessage(this.main.prel + " 4 " + this.main.posl + "/dm gm : Toggles general messages(join/leave/kick) on/off.");
    s.sendMessage(this.main.prel + " 5 " + this.main.posl + "/dm reload : Reloads the config file.");
    s.sendMessage(this.main.prel + " * " + this.main.posl + ChatColor.DARK_PURPLE + "Remember, you can use both '/dm' or '/deathmessages' for commands.");
  }
  
  boolean esJugador(CommandSender s)
  {
    if ((s instanceof Player)) {
      return true;
    }
    s.sendMessage(this.main.label + "This command is only executable BY PLAYERS!");
    return false;
  }
  
  Object obtenerValorAleatorioDeLista(List<?> lista)
  {
    return lista.get(this.r.nextInt(lista.size()));
  }
}
