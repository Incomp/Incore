package me.incomp.plugins.pub.incore.modules.itemban;

import java.util.Map.Entry;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

/**
 * Used to remove potion effects.
 * 
 * @author Incomp
 * @since Mar 17, 2018
 */
public class ItemBanTimer extends BukkitRunnable {

	private final ItemBanModule module;
	private final long delay;
	private final long period;
	
	private boolean enabled = false;
	
	public ItemBanTimer(ItemBanModule module, long delay, long period) {
		this.module = module;
		this.delay = delay;
		this.period = period;
	}
	
	@Override
	public void run() {
		if(this.module.getPotionEffectBlacklist().isEmpty()) {
			this.module.warn("ItemBanTimer: Since there are no blacklisted potion effects, I'll disable myself.");
			this.stop();
		}
		
		if(Bukkit.getOnlinePlayers().isEmpty()) return; // Do nothing if no players are online.
		
		for(Player p : Bukkit.getOnlinePlayers()) {
			// Loop through the blacklist for each player.
			for(Entry<PotionEffectType, Integer> entry : this.module.getPotionEffectBlacklist().entrySet()) {
				PotionEffectType effect = entry.getKey();
				int amp = entry.getValue();
				
				// Check the player's potion effects.
				for(PotionEffect playerEffect : p.getActivePotionEffects()) {
					if(playerEffect.getType() == effect) { // The player a blacklisted effect type
						if(amp == 0) { // Amplifer is 0; the effect will be removed.
							p.removePotionEffect(effect);
							continue;
						} else {
							// The amplifier is a specified value.
							if(playerEffect.getAmplifier() >= amp) { // If the player's effect amplifer is 	EQUAL TO OR GREATER THAN what's blacklisted...
								p.removePotionEffect(effect); // ...then we remove it.
								continue;
							} else continue; // Do nothing; the specific effect/amplifier combo is permitted.
						}
					} else continue; // Do nothing; the player's effect isn't in the blacklist.
				}
			}
		}
	}
	
	public void start() {
		this.enabled = true;
		this.runTaskTimer(this.module.getPlugin(), this.delay, this.period);
	}
	
	public void stop() {
		this.enabled = false;
		this.cancel();
	}
	
	public boolean isEnabled() {
		return this.enabled;
	}
}
