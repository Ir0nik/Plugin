package com.gmail.josemanuelgassin.DeathMessages;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

class Metodos_Generales
{
  _DeathMessages main;
  
  Metodos_Generales(_DeathMessages instance)
  {
    this.main = instance;
  }
  
  String formateador(String frase)
  {
    frase = frase.replaceAll("&", "§");
    frase = frase.replaceAll("%\\[n\\]", "Ã±");
    frase = frase.replaceAll("%\\[a1\\]", "á");
    frase = frase.replaceAll("%\\[a2\\]", "à");
    frase = frase.replaceAll("%\\[a3\\]", "â");
    frase = frase.replaceAll("%\\[a4\\]", "Ã¥");
    frase = frase.replaceAll("%\\[a5\\]", "Ã¤");
    frase = frase.replaceAll("%\\[e1\\]", "�");
    frase = frase.replaceAll("%\\[e2\\]", "�");
    frase = frase.replaceAll("%\\[e3\\]", "ê");
    frase = frase.replaceAll("%\\[i1\\]", "Ã­");
    frase = frase.replaceAll("%\\[i2\\]", "Ã¬");
    frase = frase.replaceAll("%\\[i3\\]", "Ã®");
    frase = frase.replaceAll("%\\[o1\\]", "Ã³");
    frase = frase.replaceAll("%\\[o2\\]", "Ã²");
    frase = frase.replaceAll("%\\[o3\\]", "Ã´");
    frase = frase.replaceAll("%\\[o4\\]", "Ã¶");
    frase = frase.replaceAll("%\\[o5\\]", "Ã¸");
    frase = frase.replaceAll("%\\[u1\\]", "Ãº");
    frase = frase.replaceAll("%\\[u2\\]", "Ã¹");
    frase = frase.replaceAll("%\\[u3\\]", "Ã»");
    frase = frase.replaceAll("%\\[u4\\]", "Ã¼");
    frase = frase.replaceAll("%\\[simbol1\\]", "â˜ ");
    frase = frase.replaceAll("%\\[ae\\]", "Ã¦");
    
    return frase;
  }
  
  void comunicadorDeMundos(String mensaje, String mundoOrigen, boolean mensajeMuerte)
  {
    List<World> mundosAComunicar = new ArrayList();
    if (this.main.FC.getBoolean("Linked_Worlds.Enable"))
    {
      if (this.main.FC.getStringList("Linked_Worlds.World_List").contains(mundoOrigen)) {
        for (String mundo : this.main.FC.getStringList("Linked_Worlds.World_List"))
        {
          World w = null;
          try
          {
            w = Bukkit.getWorld(mundo);
            Bukkit.getWorld(mundo).getName();
          }
          catch (Exception ex)
          {
            continue;
          }
          mundosAComunicar.add(w);
        }
      }
      if (mundosAComunicar.isEmpty()) {
        mundosAComunicar.add(Bukkit.getWorld(mundoOrigen));
      }
    }
    if (mundosAComunicar.isEmpty()) {
      mundosAComunicar = Bukkit.getWorlds();
    }
    broadcaster(mensaje, mundosAComunicar, mensajeMuerte);
  }
  
  void broadcaster(String mensaje, List<World> mundosAComunicar, boolean mensajeMuerte)
  {
    Iterator localIterator2;
    label200:
    for (Iterator localIterator1 = mundosAComunicar.iterator(); localIterator1.hasNext(); localIterator2.hasNext())
    {
      World w = (World)localIterator1.next();
      if ((!this.main.FC.getStringList("Silenced_Worlds").isEmpty()) && (this.main.FC.getStringList("Silenced_Worlds").contains(w.getName()))) {
        break label200;
      }
      localIterator2 = w.getPlayers().iterator(); continue;
    }
  }
}
