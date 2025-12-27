package me.EthanBilbrey.HarderMobs;

import java.util.ArrayList;
import java.util.ConcurrentModificationException;
import java.util.List;
import java.util.logging.Level;

import org.bukkit.Bukkit;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Monster;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class AttractedToLight implements Listener
{
	public static List<MobLightHandler> list = new ArrayList<MobLightHandler>();
	
	@EventHandler
	public void onPlayerMove(PlayerMoveEvent e) 
	{
		try 
		{
			List<Entity> nearbyEntities = e.getPlayer().getNearbyEntities(50, 50, 50);
			for(Entity ent : nearbyEntities) 
			{
				if(ent instanceof Monster) 
				{
					boolean exists = false;
					for(MobLightHandler mlh : list) 
					{
						if(ent.equals(mlh.getMob())) 
						{
							exists = true;
						}
					}
					if(!exists) 
					{
						//Bukkit.getServer().getLogger().log(Level.INFO, "New Mob Added");
						list.add(new MobLightHandler((LivingEntity) ent));
					}
				}
			}
		}
		catch(ConcurrentModificationException exc) {}
	}
	@EventHandler
	public void onPlayerLeave(PlayerQuitEvent e) 
	{
		for(MobLightHandler mlh : list) 
		{
			mlh.cancelTask();
			try
			{
				mlh.getArmorStand().remove();
			}catch(NullPointerException exc) {}
			mlh.as = null;
		}
	}
	@EventHandler
	public void onEntityDeath(EntityDeathEvent e) 
	{
		if(e.getEntity() instanceof Monster) 
		{
			try 
			{
				for(MobLightHandler mlh : list) 
				{
					if(mlh.getMob().equals(e.getEntity())) 
					{
						mlh.cancelTask();
						mlh.mob = null;
						try 
						{
							mlh.getArmorStand().remove();
						}catch(NullPointerException exc) {}
						list.remove(mlh);
					}
				}
			}
			catch(ConcurrentModificationException exc) {}
		}
	}
}
