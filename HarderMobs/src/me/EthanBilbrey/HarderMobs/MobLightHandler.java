package me.EthanBilbrey.HarderMobs;

import java.util.ArrayList;
import java.util.ConcurrentModificationException;
import java.util.List;
import java.util.logging.Level;

import org.bukkit.Bukkit;
import org.bukkit.block.Block;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Monster;
import org.bukkit.entity.Player;
import org.bukkit.inventory.EquipmentSlot;

public class MobLightHandler 
{
	public LivingEntity mob;
	public int taskId;
	public ArmorStand as;
	
	public MobLightHandler(LivingEntity mob) 
	{
		this.mob = mob;
		startTrack();
	}
	public void startTrack() 
	{
		//Bukkit.getServer().getLogger().log(Level.INFO, "Moving started");
		taskId = Bukkit.getScheduler().scheduleSyncRepeatingTask(Main.getPlugin(), () -> {
			if(!(((Monster) mob).getTarget() instanceof Player)) 
			{
				move();
			}
			List<Entity> nearbyEntities = mob.getNearbyEntities(50, 50, 50);
			boolean playerNearby = false;
			for(Entity ent : nearbyEntities) 
			{
				if(ent instanceof Player) 
				{
					playerNearby = true;
				}
			}
			if(!playerNearby) 
			{
				try 
				{
					for(MobLightHandler m : AttractedToLight.list) 
					{
						if(m.getMob().equals(mob)) 
						{		
								AttractedToLight.list.remove(m);
						}
					}
					if(as != null) 
					{
						as.remove();
						as = null;
					}
					//Bukkit.getServer().getLogger().log(Level.INFO, "Task cancelled");
					cancelTask();
				}
				catch(ConcurrentModificationException exc) {}
			}
		}, 0L, 60L);
	}
	public void cancelTask() 
	{
		Bukkit.getServer().getScheduler().cancelTask(taskId);
	}
	
	public void move() 
	{
		List<Block> blocks = new ArrayList<Block>();
		for(int x = mob.getLocation().getBlockX() - 20; x <= mob.getLocation().getBlockX() + 20; x++) 
		{
			for(int y = mob.getLocation().getBlockY() - 5; y <= mob.getLocation().getBlockY() + 5; y++) 
			{
				for(int z = mob.getLocation().getBlockZ() - 20; z <= mob.getLocation().getBlockZ() + 20; z++) 
				{
					blocks.add(mob.getLocation().getWorld().getBlockAt(x, y, z));
				}
			}
		}
		
		Block brightestBlock = blocks.get(0);
		
		for(Block b : blocks) 
		{
			if(b.getLightFromBlocks() > brightestBlock.getLightFromBlocks()) 
			{
				brightestBlock = b;
			}
		}
		if(brightestBlock.getLightFromBlocks() > 10) 
		{
			if(as == null) 
			{
				as = (ArmorStand) brightestBlock.getLocation().getWorld().spawnEntity(brightestBlock.getLocation(), EntityType.ARMOR_STAND);
				as.setVisible(false);
				as.setInvulnerable(true);
				as.addEquipmentLock(EquipmentSlot.CHEST, ArmorStand.LockType.ADDING_OR_CHANGING);
				as.addEquipmentLock(EquipmentSlot.FEET, ArmorStand.LockType.ADDING_OR_CHANGING);
				as.addEquipmentLock(EquipmentSlot.HAND, ArmorStand.LockType.ADDING_OR_CHANGING);
				as.addEquipmentLock(EquipmentSlot.LEGS, ArmorStand.LockType.ADDING_OR_CHANGING);
				as.addEquipmentLock(EquipmentSlot.OFF_HAND, ArmorStand.LockType.ADDING_OR_CHANGING);
			}
			else 
			{
				as.teleport(brightestBlock.getLocation());
				//as.setVisible(false);
				as.setInvulnerable(true);
				as.addEquipmentLock(EquipmentSlot.CHEST, ArmorStand.LockType.ADDING_OR_CHANGING);
				as.addEquipmentLock(EquipmentSlot.FEET, ArmorStand.LockType.ADDING_OR_CHANGING);
				as.addEquipmentLock(EquipmentSlot.HAND, ArmorStand.LockType.ADDING_OR_CHANGING);
				as.addEquipmentLock(EquipmentSlot.LEGS, ArmorStand.LockType.ADDING_OR_CHANGING);
				as.addEquipmentLock(EquipmentSlot.OFF_HAND, ArmorStand.LockType.ADDING_OR_CHANGING);
			}
			
			((Monster) mob).setTarget(as);
			checkDistance();
		}
		else 
		{
			((Monster) mob).setTarget(null);
			try 
			{
				as.remove();
				as = null;
			}catch(NullPointerException exc) {}
		}
	}
	
	public void checkDistance() 
	{
		if(as.getLocation().distance(mob.getLocation()) <= 2) 
		{
			((Monster) mob).setTarget(null);
		}
	}
	
	public Entity getMob() {return mob;}
	public ArmorStand getArmorStand() {return as;}
}
