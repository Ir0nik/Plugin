package com.gmail.josemanuelgassin.DeathMessages;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.commons.lang.WordUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Blaze;
import org.bukkit.entity.CaveSpider;
import org.bukkit.entity.Creeper;
import org.bukkit.entity.Enderman;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Fireball;
import org.bukkit.entity.Ghast;
import org.bukkit.entity.Giant;
import org.bukkit.entity.IronGolem;
import org.bukkit.entity.PigZombie;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.entity.Silverfish;
import org.bukkit.entity.Skeleton;
import org.bukkit.entity.Skeleton.SkeletonType;
import org.bukkit.entity.Snowball;
import org.bukkit.entity.Snowman;
import org.bukkit.entity.Spider;
import org.bukkit.entity.TNTPrimed;
import org.bukkit.entity.Wither;
import org.bukkit.entity.Wolf;
import org.bukkit.entity.Zombie;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

class Listener_Jugador
  implements Listener
{
  _DeathMessages main;
  
  Listener_Jugador(_DeathMessages instance)
  {
    this.main = instance;
  }
  
  String escogerMensajeAleatorio(String causa)
  {
    List<String> MensajesPosibles = this.main.FC.getStringList(causa);
    return (String)this.main.u_Goty.obtenerValorAleatorioDeLista(MensajesPosibles);
  }
  
  @EventHandler(priority=EventPriority.MONITOR)
  void jugadorMuere(PlayerDeathEvent e)
  {
    try
    {
      if (e.getDeathMessage().equals("")) {
        return;
      }
    }
    catch (NullPointerException ex)
    {
      return;
    }
    if (this.main.FC.getBoolean("Debug"))
    {
      try
      {
        Bukkit.broadcastMessage(ChatColor.GREEN + "Victim Name = " + e.getEntity().getName());
      }
      catch (Exception ex)
      {
        Bukkit.broadcastMessage(ChatColor.RED + "Victim Name = NONE");
      }
      try
      {
        Bukkit.broadcastMessage(ChatColor.GREEN + "World Name = " + e.getEntity().getWorld().getName());
      }
      catch (Exception ex)
      {
        Bukkit.broadcastMessage(ChatColor.RED + "World Name = NONE");
      }
      try
      {
        Bukkit.broadcastMessage(ChatColor.GREEN + "Last Damage Cause Type = " + e.getEntity().getLastDamageCause().getEventName());
      }
      catch (Exception ex)
      {
        Bukkit.broadcastMessage(ChatColor.RED + "Last Damage Cause Type = NONE");
      }
      try
      {
        Bukkit.broadcastMessage(ChatColor.GREEN + "Last Damage Cause = " + e.getEntity().getLastDamageCause().getCause());
      }
      catch (Exception ex)
      {
        Bukkit.broadcastMessage(ChatColor.RED + "Last Damage Cause = NONE");
      }
      try
      {
        Bukkit.broadcastMessage(ChatColor.GREEN + "Damager = " + ((EntityDamageByEntityEvent)e.getEntity().getLastDamageCause()).getDamager());
      }
      catch (Exception ex)
      {
        Bukkit.broadcastMessage(ChatColor.RED + "Damager = NONE");
      }
    }
    e.setDeathMessage("");
    

    String causa = "Unknown";
    
    Player v = e.getEntity();
    String vn = v.getName();
    if (v.getLastDamageCause() == null)
    {
      if (this.main.FC.getInt("PvP_Death_Messages_Handling") == 3) {
        return;
      }
      String MM = escogerMensajeAleatorio(causa).replaceAll("%world", v.getWorld().getName()).replaceAll("%player", vn);
      this.main.m_Generales.comunicadorDeMundos(this.main.m_Generales.formateador(MM), e.getEntity().getWorld().getName(), true);
      return;
    }
    EntityDamageEvent ki = v.getLastDamageCause();
    EntityDamageEvent.DamageCause ca = ki.getCause();
    if ((ki instanceof EntityDamageByEntityEvent))
    {
      EntityDamageByEntityEvent kie = (EntityDamageByEntityEvent)ki;
      Entity damager = kie.getDamager();
      if ((damager instanceof Player))
      {
        if (this.main.FC.getInt("PvP_Death_Messages_Handling") == 1) {
          return;
        }
        if (ca == EntityDamageEvent.DamageCause.THORNS)
        {
          causa = "Thorns";
          Player a = v.getKiller();
          String an = null;
          try
          {
            an = a.getName();
          }
          catch (NullPointerException ex)
          {
            this.main.logger.log(Level.SEVERE, this.main.label + ChatColor.RED + "Other Plugin Plugin is causing conflics with Death Messages: The Death Cause is 'Thorns Enchantment' but the server can't get the assasins name! This is caused when other plugin tries to use NPCs/Mobs or Commands as Players.");
            return;
          }
          String MM = escogerMensajeAleatorio(causa).replaceAll("%world", v.getWorld().getName()).replaceAll("%killer", an).replaceAll("%player", vn);
          e.setDeathMessage(this.main.m_Generales.formateador(MM));
          this.main.m_Generales.comunicadorDeMundos(this.main.m_Generales.formateador(MM), e.getEntity().getWorld().getName(), true);
          return;
        }
        ItemStack it = ((Player)damager).getItemInHand();
        String matNom = it != null ? it.getType().toString() : "AIR";
        if (ca == EntityDamageEvent.DamageCause.ENTITY_ATTACK) {
          if (matNom == "AIR") {
            causa = "PvPFist";
          } else {
            causa = "PvPWeapon";
          }
        }
        matNom = WordUtils.capitalize(matNom.toLowerCase().replaceAll("_", " "));
        
        Player a = v.getKiller();
        String an = null;
        try
        {
          an = a.getName();
        }
        catch (NullPointerException ex)
        {
          this.main.logger.log(Level.SEVERE, this.main.label + ChatColor.RED + "Other Server Plugin is causing conflics with Death Messages: The Death Cause is 'PvP Melee Attack' but the server can't get the assasins name! This is caused when other plugin tries to use NPCs/Mobs or Commands as Players.");
          return;
        }
        String MM = escogerMensajeAleatorio(causa);
        String armaNom = null;
        if (MM.contains("%weapon_name"))
        {
          ItemMeta metaD = it.getItemMeta();
          if (metaD.getDisplayName() != null)
          {
            armaNom = metaD.getDisplayName();
            MM = MM.replaceAll("%weapon_name", armaNom);
          }
        }
        if ((MM.contains("%weapon_material")) || (armaNom == null)) {
          MM = MM.replaceAll("%weapon_material", matNom).replaceAll("%weapon_name", matNom);
        }
        MM = MM.replaceAll("%world", v.getWorld().getName()).replaceAll("%killer", an).replaceAll("%player", vn);
        this.main.m_Generales.comunicadorDeMundos(this.main.m_Generales.formateador(MM), e.getEntity().getWorld().getName(), true);
        return;
      }
      if (ca == EntityDamageEvent.DamageCause.ENTITY_ATTACK)
      {
        if (this.main.FC.getInt("PvP_Death_Messages_Handling") == 3) {
          return;
        }
        if ((damager instanceof Creeper)) {
          causa = "Creeper";
        }
        if ((damager instanceof Zombie)) {
          causa = "Zombie";
        }
        if ((damager instanceof Spider)) {
          causa = "Spider";
        }
        if ((damager instanceof CaveSpider)) {
          causa = "CaveSpider";
        }
        if ((damager instanceof Enderman)) {
          causa = "Enderman";
        }
        if ((damager instanceof Silverfish)) {
          causa = "Silverfish";
        }
        if (damager.getType().equals(EntityType.MAGMA_CUBE)) {
          causa = "MagmaCube";
        }
        if (damager.getType().equals(EntityType.SLIME)) {
          causa = "Slime";
        }
        if ((damager instanceof Wolf)) {
          causa = "Wolf";
        }
        if ((damager instanceof PigZombie)) {
          causa = "PigZombie";
        }
        if ((damager instanceof IronGolem)) {
          causa = "IronGolem";
        }
        if ((damager instanceof Giant)) {
          causa = "Giant";
        }
        if ((damager instanceof Wither)) {
          causa = "Wither";
        }
        if (damager.getType() == EntityType.SKELETON)
        {
          Skeleton esqueleto = (Skeleton)damager;
          if (esqueleto.getSkeletonType().equals(Skeleton.SkeletonType.NORMAL)) {
            causa = "SkeletonMelee";
          }
          if (esqueleto.getSkeletonType().equals(Skeleton.SkeletonType.WITHER)) {
            causa = "WitherSkeleton";
          }
        }
      }
      else if (ca == EntityDamageEvent.DamageCause.PROJECTILE)
      {
        Projectile pro = (Projectile)damager;
        if ((pro.getShooter() instanceof Player))
        {
          if (this.main.FC.getInt("PvP_Death_Messages_Handling") == 1) {
            return;
          }
          if ((pro instanceof Arrow)) {
            causa = "PvPBow";
          }
          Player a = v.getKiller();
          String an = null;
          try
          {
            an = a.getName();
          }
          catch (NullPointerException ex)
          {
            this.main.logger.log(Level.SEVERE, this.main.label + ChatColor.RED + "Other Server Plugin is causing conflics with Death Messages: The Death Cause is 'PvP Projectile Attack' but the server can't get the assasins name! This is caused when other plugin tries to use NPCs/Mobs or Commands as Players.");
            return;
          }
          String MM = escogerMensajeAleatorio(causa).replaceAll("%world", v.getWorld().getName()).replaceAll("%killer", an).replaceAll("%player", vn);
          this.main.m_Generales.comunicadorDeMundos(this.main.m_Generales.formateador(MM), e.getEntity().getWorld().getName(), true);
          return;
        }
        if (this.main.FC.getInt("PvP_Death_Messages_Handling") == 3) {
          return;
        }
        if ((pro instanceof Arrow))
        {
          if ((pro.getShooter() instanceof Skeleton)) {
            causa = "SkeletonArcher";
          } else {
            causa = "DispenserArrow";
          }
        }
        else if ((pro instanceof Snowball))
        {
          if ((pro.getShooter() instanceof Snowman)) {
            causa = "Snowman";
          }
        }
        else if ((pro instanceof Fireball)) {
          if ((pro.getShooter() instanceof Ghast)) {
            causa = "Ghast";
          } else if ((pro.getShooter() instanceof Blaze)) {
            causa = "Blaze";
          } else if ((pro.getShooter() instanceof Wither)) {
            causa = "Wither";
          } else {
            causa = "DispenserFireball";
          }
        }
      }
      if (this.main.FC.getInt("PvP_Death_Messages_Handling") == 3) {
        return;
      }
      if ((ca == EntityDamageEvent.DamageCause.ENTITY_EXPLOSION) || (ca == EntityDamageEvent.DamageCause.BLOCK_EXPLOSION)) {
        if ((damager instanceof TNTPrimed)) {
          causa = "TNT";
        }
      }
    }
    else
    {
      if (ca == EntityDamageEvent.DamageCause.DROWNING) {
        causa = "Drowning";
      }
      if (ca == EntityDamageEvent.DamageCause.STARVATION) {
        causa = "Starvation";
      }
      if (ca == EntityDamageEvent.DamageCause.CONTACT) {
        causa = "Cactus";
      }
      if (ca == EntityDamageEvent.DamageCause.CUSTOM) {
        causa = "Unknown";
      }
      if (ca == EntityDamageEvent.DamageCause.FIRE) {
        causa = "FireBlock";
      }
      if (ca == EntityDamageEvent.DamageCause.FIRE_TICK) {
        causa = "FireEffect";
      }
      if (ca == EntityDamageEvent.DamageCause.LAVA) {
        causa = "Lava";
      }
      if (ca == EntityDamageEvent.DamageCause.LIGHTNING) {
        causa = "Lightning";
      }
      if (ca == EntityDamageEvent.DamageCause.POISON) {
        causa = "Poison";
      }
      if (ca == EntityDamageEvent.DamageCause.SUFFOCATION) {
        causa = "Suffocation";
      }
      if (ca == EntityDamageEvent.DamageCause.VOID) {
        causa = "Void";
      }
      if (ca == EntityDamageEvent.DamageCause.FALL) {
        causa = "Fall";
      }
      if (ca == EntityDamageEvent.DamageCause.SUICIDE) {
        causa = "Suicide";
      }
      if (ca == EntityDamageEvent.DamageCause.MAGIC) {
        causa = "PotionofHarming";
      }
      if (ca == EntityDamageEvent.DamageCause.WITHER) {
        causa = "WitherEffect";
      }
      if (ca == EntityDamageEvent.DamageCause.FALLING_BLOCK) {
        causa = "Anvil";
      }
    }
    String MM = escogerMensajeAleatorio(causa).replaceAll("%world", v.getWorld().getName()).replaceAll("%player", vn);
    this.main.m_Generales.comunicadorDeMundos(this.main.m_Generales.formateador(MM), e.getEntity().getWorld().getName(), true);
  }
}
