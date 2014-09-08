package com.gmail.josemanuelgassin.DeathMessages;

import java.util.HashMap;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

class Commands
  implements CommandExecutor
{
  _DeathMessages main;
  
  Commands(_DeathMessages instance)
  {
    this.main = instance;
  }
  
  public boolean onCommand(CommandSender sender, Command cmd, String comando, String[] args)
  {
    if ((comando.equalsIgnoreCase("deathmessages")) || (comando.equalsIgnoreCase("dm")))
    {
      if (args.length == 0)
      {
        if (!this.main.u_Goty.tienePerm(sender, "deathmessages.user")) {
          return true;
        }
        this.main.u_Goty.listaComandos(sender);
        return true;
      }
      if (args.length == 1)
      {
        if (args[0].equalsIgnoreCase("reload"))
        {
          if (!this.main.u_Goty.tienePerm(sender, "deathmessages.admin")) {
            return true;
          }
          this.main.u_Goty.recargarConfig(sender);
          return true;
        }
        if (args[0].equalsIgnoreCase("all"))
        {
          if (!this.main.u_Goty.esJugador(sender)) {
            return true;
          }
          if (!this.main.u_Goty.tienePerm(sender, "deathmessages.user")) {
            return true;
          }
          Player j = (Player)sender;
          String jn = j.getName();
          if (this.main.jugadoresToggleados.containsKey(jn)) {
            if (((Integer)this.main.jugadoresToggleados.get(jn)).intValue() == 1)
            {
              this.main.jugadoresToggleados.remove(jn);
              j.sendMessage(this.main.label + ChatColor.GREEN + "Both (Death/General) Messages are visible again!");
              return true;
            }
          }
          this.main.jugadoresToggleados.put(jn, Integer.valueOf(1));
          j.sendMessage(this.main.label + ChatColor.GREEN + "Both (Death/General) Messages have been hidden!");
          return true;
        }
        if (args[0].equalsIgnoreCase("dm"))
        {
          if (!this.main.u_Goty.esJugador(sender)) {
            return true;
          }
          if (!this.main.u_Goty.tienePerm(sender, "deathmessages.user")) {
            return true;
          }
          Player j = (Player)sender;
          String jn = j.getName();
          if (this.main.jugadoresToggleados.containsKey(jn)) {
            if (((Integer)this.main.jugadoresToggleados.get(jn)).intValue() == 2)
            {
              this.main.jugadoresToggleados.remove(jn);
              j.sendMessage(this.main.label + ChatColor.GREEN + "Death Messages are visible again!");
              return true;
            }
          }
          this.main.jugadoresToggleados.put(jn, Integer.valueOf(2));
          j.sendMessage(this.main.label + ChatColor.GREEN + "Death Messages have been hidden!");
          return true;
        }
        if (args[0].equalsIgnoreCase("gm"))
        {
          if (!this.main.u_Goty.esJugador(sender)) {
            return true;
          }
          if (!this.main.u_Goty.tienePerm(sender, "deathmessages.user")) {
            return true;
          }
          Player j = (Player)sender;
          String jn = j.getName();
          if (this.main.jugadoresToggleados.containsKey(jn)) {
            if (((Integer)this.main.jugadoresToggleados.get(jn)).intValue() == 3)
            {
              this.main.jugadoresToggleados.remove(jn);
              j.sendMessage(this.main.label + ChatColor.GREEN + "General Messages are visible again!");
              return true;
            }
          }
          this.main.jugadoresToggleados.put(jn, Integer.valueOf(3));
          j.sendMessage(this.main.label + ChatColor.GREEN + "General Messages have been hidden!");
          return true;
        }
      }
    }
    return false;
  }
}
