package me.EthanBilbrey.HarderMobs;

import java.util.ConcurrentModificationException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.logging.Level;

import org.bukkit.Bukkit;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.EnderDragon;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Monster;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerQuitEvent;


public class MobSpawn implements Listener
{
	public static Map<UUID, NewPig> entities = new HashMap<>();
	public static UUID lastDead;
	
	@EventHandler
	public void playerMove(PlayerMoveEvent e) 
	{
		try 
		{
			for(Map.Entry<UUID, NewPig> entry : entities.entrySet()) 
			{
				if(entry.getValue().checkEntity() != null)
				{
					try
					{
					entry.getValue().kill();
					entities.remove(entry.getKey());
					}
					catch(ConcurrentModificationException exc) {}
				}
			}
		}catch(ConcurrentModificationException exc) {}
		/*
		List<Entity> nearbyEntities = e.getPlayer().getNearbyEntities(10, 10, 10);
		//Bukkit.getServer().getLogger().log(Level.INFO, "Nearby Attackers: " + entities.size());
		for(Entity entity : nearbyEntities) 
		{
			UUID entId = entity.getUniqueId();
			if(entity instanceof LivingEntity 
					&& !(entity instanceof Monster) 
					&& !(entity instanceof EnderDragon) 
					&& !(entity instanceof Player) 
					&& entities.size() < 50)
			{				
								
				if(!entities.containsKey(entId) && !entity.getUniqueId().equals(lastDead)) {
					//Bukkit.getServer().getLogger().log(Level.INFO, "New Attacker: " + entity.getType().toString() + " " + entity.getUniqueId());
					NewPig ent = new NewPig(entity.getLocation(), entity);
					entities.put(entity.getUniqueId(), ent);
				}
				else if(entities.containsKey(entId) && entity.isDead()) {
					//Bukkit.getServer().getLogger().log(Level.INFO, "Attacker Died: " + entity.getType().toString() + " " + entity.getUniqueId());
					entities.get(entId).kill();
					entities.remove(entId);
				}
			}
		}
		*/
	}
	@EventHandler
	public void onDeath(EntityDeathEvent e) 
	{
		Entity entity = e.getEntity();
		if(entity instanceof LivingEntity 
				&& !(entity instanceof Monster) 
				&& !(entity instanceof EnderDragon) 
				&& !(entity instanceof Player))
		{
			UUID entId = e.getEntity().getUniqueId();
			if(entities.containsKey(entId)) 
			{
				//Bukkit.getServer().getLogger().log(Level.INFO, "Attacker Died: " + e.getEntity().getType().toString() + " " + e.getEntity().getUniqueId());
				lastDead = entId;
				entities.get(entId).kill();
				entities.remove(entId);
			}
		}
	}
	@EventHandler
	public void entityHit(EntityDamageByEntityEvent e) 
	{
		UUID entityId = null;
		for(Map.Entry<UUID, NewPig> entry : entities.entrySet()) 
		{
			NewPig piggy = entry.getValue();
			//Bukkit.getServer().getLogger().log(Level.INFO, "Zombie entityId: " + piggy.getZombie().getUniqueId());
			//Bukkit.getServer().getLogger().log(Level.INFO, "Event entityId: " + e.getDamager().getUniqueId());
			if(piggy.getZombie().getUniqueId().equals(e.getDamager().getUniqueId())) 
			{
				entityId = piggy.getEntity().getUniqueId();
			}
		}
		if(entityId != null) 
		{
			Double damage = e.getDamage();
			Double finalDamage = damage;
			e.setDamage(finalDamage);
			
		}
		else 
		{
			Entity entity = e.getEntity();
			UUID entId = entity.getUniqueId();
			if(entity instanceof LivingEntity 
					&& !(entity instanceof Monster) 
					&& !(entity instanceof EnderDragon) 
					&& !(entity instanceof Player)
					&& !(entity instanceof ArmorStand)
					&& entities.size() < 50)
			{				
								
				if(!entities.containsKey(entId) && !entity.getUniqueId().equals(lastDead)) {
					//Bukkit.getServer().getLogger().log(Level.INFO, "New Attacker: " + entity.getType().toString() + " " + entity.getUniqueId());
					NewPig ent = new NewPig(entity.getLocation(), entity);
					entities.put(entity.getUniqueId(), ent);
					//other mobs of the same type will also attack if nearby
					List<Entity> nearbyEntities = ent.getEntity().getNearbyEntities(25.0, 25.0, 25.0);
					for(Entity nearbyEntity : nearbyEntities) 
					{
						if(nearbyEntity.getType().equals(ent.getEntity().getType())) 
						{
							NewPig newPig = new NewPig(nearbyEntity.getLocation(), nearbyEntity);
							entities.put(nearbyEntity.getUniqueId(), newPig);
						}
					}
				}
				else if(entities.containsKey(entId) && entity.isDead()) {
					//Bukkit.getServer().getLogger().log(Level.INFO, "Attacker Died: " + entity.getType().toString() + " " + entity.getUniqueId());
					entities.get(entId).kill();
					entities.remove(entId);
				}
			}
		}
	}
	@EventHandler
	public void onLeave(PlayerQuitEvent e) 
	{
		for(Map.Entry<UUID, NewPig> entry : entities.entrySet()) 
		{
			entry.getValue().kill();
		}
	}
}
