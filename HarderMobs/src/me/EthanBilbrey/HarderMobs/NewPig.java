package me.EthanBilbrey.HarderMobs;

import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.logging.Level;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Husk;
import org.bukkit.entity.Player;
import org.bukkit.entity.Zombie;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;

public class NewPig implements Listener
{
	private Entity entity;
	private Husk zombie;
	private int taskId;
	private int ticks;
	
	public NewPig() {}
	
	public NewPig(Location loc, Entity ent) 
	{
		spawnPig(loc, ent);
	}
	
	@SuppressWarnings("deprecation")
	public void spawnPig(Location loc, Entity ent) 
	{
		this.entity = ent;
		//Bukkit.getServer().getLogger().log(Level.INFO, ent.getUniqueId() + " entity - Spawned Attacker: " + this.entity.getType().toString() + " " + entity.getUniqueId());
		this.zombie = (Husk) loc.getWorld().spawnEntity(loc, EntityType.HUSK);
		//Bukkit.getServer().getLogger().log(Level.INFO, ent.getUniqueId() + " entity - Spawned Husk: " + this.entity.getType().toString() + " " + zombie.getUniqueId());
		this.zombie.setBaby();
		this.zombie.setCustomName(this.entity.getType().toString().toLowerCase().replace('_', ' '));
		this.zombie.setAgeLock(true);
		this.zombie.setSilent(true);
		this.zombie.setCanPickupItems(false);
		this.zombie.setInvisible(true);
		run();
	}
	public void run() 
	{
		ticks = 0;
		taskId = Bukkit.getServer().getScheduler().scheduleSyncRepeatingTask(Main.getPlugin(), () -> {
			this.entity.setRotation(this.zombie.getLocation().getYaw(), this.zombie.getLocation().getPitch());
			this.entity.setVelocity(this.zombie.getVelocity());
			if(ticks % 10 == 0) 
			{
				this.entity.teleport(this.zombie);
			}


			if(ticks >= 100) 
			{
				ticks = 0;
			}
			else 
			{
				ticks++;
			}
		}, 1L, 1L);
	}
	public void kill() 
	{
		cancelTask(taskId);
		this.zombie.remove();
		this.entity.remove();
		//Bukkit.getServer().getLogger().log(Level.INFO, "kill entity: " + this.entity.getType().toString() + " " + this.entity.getUniqueId());
		//Bukkit.getServer().getLogger().log(Level.INFO, "kill zombie: " + this.entity.getType().toString() + " " + this.zombie.getUniqueId());
		
	}
	public Entity getEntity() 
	{
		return this.entity;
	}
	public Husk getZombie() 
	{
		return this.zombie;
	}
	public void cancelTask(int taskId) 
	{
		Bukkit.getServer().getScheduler().cancelTask(taskId);
	}
	public UUID getRelatedEntity(UUID zombieId) 
	{
		if(this.zombie.getUniqueId().equals(zombieId)) 
		{
			return this.entity.getUniqueId();
		}
		return null;
	}
	public NewPig checkEntity()
	{
		if(ticks % 20 == 0) 
		{
			//Bukkit.getServer().getLogger().log(Level.INFO, "Checking...");
			List<Entity> entites = this.entity.getNearbyEntities(45, 20, 45);
			boolean playerNearby = false;
			for(Entity entity : entites) 
			{
				if(entity instanceof Player) 
				{
					playerNearby = true;
				}
			}
			if(!playerNearby) 
			{
				return this;
			}
		}
		
		return null;
	}
	@Override
	public String toString() {
		return "Attacker: " + this.entity.getType().toString() + " entId: " + this.entity.getEntityId() + " entUUID: " + this.entity.getUniqueId() + " Zombie: " + this.zombie.getUniqueId() + "\n";
	}
}
