package me.EthanBilbrey.HarderMobs;

import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.logging.Level;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Creeper;
import org.bukkit.entity.Enderman;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Ghast;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.PigZombie;
import org.bukkit.entity.Piglin;
import org.bukkit.entity.PiglinBrute;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.entity.Skeleton;
import org.bukkit.entity.WitherSkeleton;
import org.bukkit.entity.Zoglin;
import org.bukkit.entity.Zombie;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.event.entity.EntityRegainHealthEvent;
import org.bukkit.event.entity.EntitySpawnEvent;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.material.MaterialData;
import org.bukkit.material.Openable;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.util.Vector;

@SuppressWarnings("deprecation")
public class Main extends JavaPlugin implements Listener
{

	private static Main main;
	
	public static Main getPlugin() { return main;}
	
	@Override
	public void onEnable() 
	{
		main = this;
		getServer().getPluginManager().registerEvents(new MobSpawn(), this);
		getServer().getPluginManager().registerEvents(new NewPig(), this);
		getServer().getPluginManager().registerEvents(new AttractedToLight(), this);
		getServer().getPluginManager().registerEvents(this, this);
		Bukkit.getScheduler().scheduleSyncRepeatingTask(this, () -> {
			for(Player p : Bukkit.getOnlinePlayers()) 
			{
				List<Entity> e = p.getNearbyEntities(10.0, 10.0, 10.0);
				for(Entity ent : e) 
				{
					if(ent instanceof PigZombie) 
					{
						PigZombie pz = (PigZombie) ent;
						pz.setAngry(true);
						pz.setAnger(100);
						pz.setTarget(p);
						
					}
					else if(ent instanceof Creeper) 
					{
						Creeper c = (Creeper) ent;
						c.setTarget(p);
						//gets points in space of player and creeper
						double pX = p.getLocation().getX();
						double pY = p.getLocation().getY();
						double pZ = p.getLocation().getZ();
						double cX = c.getLocation().getX();
						double cY = c.getLocation().getY();
						double cZ = c.getLocation().getZ();
						
						//makes it so the creeper will look at the player
						double x = pX - cX;
						double y = pY - cY;
						double z = pZ - cZ;
						Vector creeperLookDir = new Vector(x, y, z);
						c.getLocation().setDirection(creeperLookDir.normalize());
						
						//Calculate distance between player and creeper
						double distance = Math.sqrt(((pX - cX) * (pX - cX)) +
													((pY - cY) * (pY - cY)) +
													((pZ - cZ) * (pZ - cZ)));
						Block front = c.getTargetBlock((Set<Material>) null, 1);
						if(distance <= 10.0 &&
								(front.getType().equals(Material.GLASS) ||
								 front.getType().equals(Material.BLACK_STAINED_GLASS) ||
								 front.getType().equals(Material.BLACK_STAINED_GLASS_PANE) ||
								 front.getType().equals(Material.BLUE_STAINED_GLASS) ||
								 front.getType().equals(Material.BLUE_STAINED_GLASS_PANE) ||
								 front.getType().equals(Material.BROWN_STAINED_GLASS) ||
								 front.getType().equals(Material.BROWN_STAINED_GLASS_PANE) ||
								 front.getType().equals(Material.CYAN_STAINED_GLASS) ||
								 front.getType().equals(Material.CYAN_STAINED_GLASS_PANE) ||
								 front.getType().equals(Material.GLASS_PANE) ||
								 front.getType().equals(Material.GRAY_STAINED_GLASS) ||
								 front.getType().equals(Material.GRAY_STAINED_GLASS_PANE) ||
								 front.getType().equals(Material.GREEN_STAINED_GLASS) ||
								 front.getType().equals(Material.GREEN_STAINED_GLASS_PANE) ||
								 front.getType().equals(Material.LIGHT_BLUE_STAINED_GLASS) ||
								 front.getType().equals(Material.LIGHT_BLUE_STAINED_GLASS_PANE) ||
								 front.getType().equals(Material.LIGHT_GRAY_STAINED_GLASS) ||
								 front.getType().equals(Material.LIGHT_GRAY_STAINED_GLASS_PANE) ||
								 front.getType().equals(Material.LIME_STAINED_GLASS) ||
								 front.getType().equals(Material.LIME_STAINED_GLASS_PANE) ||
								 front.getType().equals(Material.MAGENTA_STAINED_GLASS) ||
								 front.getType().equals(Material.MAGENTA_STAINED_GLASS_PANE) ||
								 front.getType().equals(Material.ORANGE_STAINED_GLASS) ||
								 front.getType().equals(Material.ORANGE_STAINED_GLASS_PANE) ||
								 front.getType().equals(Material.PINK_STAINED_GLASS) ||
								 front.getType().equals(Material.PINK_STAINED_GLASS_PANE) ||
								 front.getType().equals(Material.PURPLE_STAINED_GLASS) ||
								 front.getType().equals(Material.PURPLE_STAINED_GLASS_PANE) ||
								 front.getType().equals(Material.RED_STAINED_GLASS) ||
								 front.getType().equals(Material.RED_STAINED_GLASS_PANE) ||
								 front.getType().equals(Material.WHITE_STAINED_GLASS) ||
								 front.getType().equals(Material.WHITE_STAINED_GLASS_PANE) ||
								 front.getType().equals(Material.YELLOW_STAINED_GLASS) ||
								 front.getType().equals(Material.YELLOW_STAINED_GLASS_PANE) ||
								 front.getType().equals(Material.OAK_DOOR) ||
								 front.getType().equals(Material.ACACIA_DOOR) ||
								 front.getType().equals(Material.BIRCH_DOOR) ||
								 front.getType().equals(Material.CRIMSON_DOOR) ||
								 front.getType().equals(Material.DARK_OAK_DOOR) ||
								 front.getType().equals(Material.IRON_DOOR) ||
								 front.getType().equals(Material.JUNGLE_DOOR) ||
								 front.getType().equals(Material.SPRUCE_DOOR) ||
								 front.getType().equals(Material.WARPED_DOOR))) 
						{
							c.ignite();
						}
						
					}
					else if(ent instanceof Zombie) 
					{
						Zombie z = (Zombie) ent;
						Block targetBlock = z.getTargetBlock((Set<Material>) null, 1);
						
						if(targetBlock.getType().equals(Material.OAK_DOOR) ||
						   targetBlock.getType().equals(Material.ACACIA_DOOR) ||
						   targetBlock.getType().equals(Material.BIRCH_DOOR) ||
						   targetBlock.getType().equals(Material.CRIMSON_DOOR) ||
						   targetBlock.getType().equals(Material.DARK_OAK_DOOR) ||
						   targetBlock.getType().equals(Material.IRON_DOOR) ||
						   targetBlock.getType().equals(Material.JUNGLE_DOOR) ||
						   targetBlock.getType().equals(Material.SPRUCE_DOOR) ||
						   targetBlock.getType().equals(Material.WARPED_DOOR) ||
						   targetBlock.getType().equals(Material.ACACIA_FENCE_GATE) ||
						   targetBlock.getType().equals(Material.BIRCH_FENCE_GATE) ||
						   targetBlock.getType().equals(Material.CRIMSON_FENCE_GATE) ||
						   targetBlock.getType().equals(Material.JUNGLE_FENCE_GATE) ||
						   targetBlock.getType().equals(Material.OAK_FENCE_GATE) ||
						   targetBlock.getType().equals(Material.SPRUCE_FENCE_GATE) ||
						   targetBlock.getType().equals(Material.WARPED_FENCE_GATE)) 
						{
							BlockState bs = targetBlock.getState();
							Openable openable = (Openable) bs.getData();
							if(!openable.isOpen()) 
							{
								openable.setOpen(true);
								bs.setData((MaterialData) openable);
								bs.update();
								p.getLocation().getWorld().playSound(p.getLocation(), Sound.BLOCK_WOODEN_DOOR_OPEN, 1.0f, 1.0f);
							}
						}
					}
					else if(ent instanceof Enderman) 
					{
						Enderman enderman = (Enderman) ent;
						enderman.setTarget(p);
					}
					else if(ent instanceof Piglin && ent.getLocation().distance(p.getLocation()) <= 5.0) 
					{
						UUID entId = ent.getUniqueId();
						if(!MobSpawn.entities.containsKey(entId) && !entId.equals(MobSpawn.lastDead)) 
						{
							Bukkit.getServer().getLogger().log(Level.INFO, "New Attacker: " + ent.getType().toString() + " " + entId);
							NewPig newPig = new NewPig(ent.getLocation(), ent);
							MobSpawn.entities.put(entId, newPig);
						}
					}
				}
				
				if(p.getFoodLevel() <= 5 && p.getHealth() > 1.0) 
				{
					p.damage(1.0);
				}
				
			}
		}, 0L, 20L);
		
		Bukkit.getScheduler().scheduleSyncRepeatingTask(this, () -> {
			for(Player p : Bukkit.getOnlinePlayers()) 
			{
				p.setFoodLevel(p.getFoodLevel() - 1);
			}
		}, 0L, 2400L);
	}
	@EventHandler
	public void onCreeperExplode(EntityExplodeEvent e) 
	{
		if(e.getEntity().getType().equals(EntityType.CREEPER)) 
		{
			e.getLocation().getWorld().createExplosion(e.getLocation(), 7.0f);
		}
	}
	@EventHandler
	public void onArrowHit(ProjectileHitEvent e) 
	{
		if(e.getEntity().getType().equals(EntityType.ARROW)) 
		{
			Projectile p = e.getEntity();
			if(p.getShooter() instanceof Skeleton && !(p.getShooter() instanceof WitherSkeleton)) 
			{
				int rand = (int) (Math.random() * 2) + 1;
				if(rand == 1) 
				{
					if(e.getHitBlock() == null) 
					{
						e.getHitEntity().getLocation().getWorld().createExplosion(e.getHitEntity().getLocation(), 1.5f);
						e.getEntity().remove();
					}
					else 
					{
						e.getHitBlock().getLocation().getWorld().createExplosion(e.getHitBlock().getLocation(), 1.5f);
						e.getEntity().remove();
					}
				}
			}
			else if(p.getShooter() instanceof WitherSkeleton) 
			{
				Arrow a = (Arrow) p;
				a.addCustomEffect(new PotionEffect(PotionEffectType.WITHER, 200, 1), true);
			}
		}
		if(e.getEntity().getType().equals(EntityType.FIREBALL)) 
		{
			Projectile p = e.getEntity();
			if(p.getShooter() instanceof Ghast) 
			{
				if(e.getHitBlock() == null) 
				{
					e.getHitEntity().getLocation().getWorld().createExplosion(e.getHitEntity().getLocation(), 7.0f);
					e.getEntity().remove();
				}
				else 
				{
					e.getHitBlock().getLocation().getWorld().createExplosion(e.getHitBlock().getLocation(), 7.0f);
					e.getEntity().remove();
				}
			}
			
		}
	}
	@EventHandler
	public void onBlockBreak(BlockBreakEvent e) 
	{
		Player player = e.getPlayer();
		Block block = e.getBlock();
		if(block.getType().getHardness() > 1.3f && player.getInventory().getItemInMainHand().getType().equals(Material.AIR)) 
		{
			player.damage(block.getType().getHardness() * 1.5f);
		}
	}
	@EventHandler
	public void onMobSpawn(EntitySpawnEvent e) 
	{
		if(e.getEntity() instanceof WitherSkeleton) 
		{
			int rand = (int) (Math.random() * 2) + 1;
			if(rand == 1) 
			{
				WitherSkeleton w = (WitherSkeleton) e.getEntity();
				w.getEquipment().setItemInMainHand(new ItemStack(Material.BOW));
			}
		}
	}
	@EventHandler
	public void onEntityHit(EntityDamageByEntityEvent e) 
	{
		if(e.getDamager() instanceof Player) 
		{
			Player p = (Player) e.getDamager();
			if(e.getEntity() instanceof Enderman) 
			{
				Enderman enderman = (Enderman) e.getEntity();
				enderman.setTarget(p);
				List<Entity> nearby = enderman.getNearbyEntities(20, 20, 20);
				for(Entity ent : nearby) 
				{
					if(ent instanceof Enderman) 
					{
						Enderman em = (Enderman) ent;
						em.setTarget(p);
					}
				}
			}
		}
	}
	@EventHandler
	public void onFoodLevelChange (FoodLevelChangeEvent event) {
	 
	    if(event.getEntity() instanceof Player) 
	    { 
	        Player player = (Player)event.getEntity();
	 
	        int oldFoodLevel = player.getFoodLevel();
	        int newFoodLevel = event.getFoodLevel();
	 
	        if(oldFoodLevel > newFoodLevel) {
	            int difference = oldFoodLevel - newFoodLevel;
	            event.setFoodLevel(newFoodLevel - difference);
	        }
	    }
	}
	@EventHandler
	public void onPlayerHeal(EntityRegainHealthEvent e) 
	{
		if(e.getEntity() instanceof Player) 
		{
			Player p = (Player) e.getEntity();
			if(e.getRegainReason().equals(EntityRegainHealthEvent.RegainReason.EATING) || e.getRegainReason().equals(EntityRegainHealthEvent.RegainReason.SATIATED)) 
			{
				p.setFoodLevel(p.getFoodLevel() - 1);
			}
		}
	}
}
