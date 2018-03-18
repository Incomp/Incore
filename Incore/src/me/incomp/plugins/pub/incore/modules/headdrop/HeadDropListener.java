package me.incomp.plugins.pub.incore.modules.headdrop;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.inventory.ItemStack;

import me.incomp.plugins.pub.incore.commons.MathUtil;
import me.incomp.plugins.pub.incore.items.ItemBuilder;

/**
 * A listener for {@code HeadDropModule.java}.
 * 
 * @author Incomp
 * @since Mar 16, 2018
 */
public class HeadDropListener implements Listener {

	private final HeadDropModule main;
	
	public HeadDropListener(HeadDropModule main) {
		this.main = main;
	}
	
	public HeadDropModule getModule() {
		return this.main;
	}
	
	@EventHandler
	public void onDeath(PlayerDeathEvent event) {
		Player p = event.getEntity();
		
		if(this.main.getDropChance() == 0) return; // No drop chance, so do nothing.
		
		if(this.main.onlyCombatDrops()) {
			if(p.getLastDamageCause().getCause() == DamageCause.ENTITY_ATTACK) return;
		}
		
		int roll = MathUtil.randomInt(1, 100);
		if(roll <= this.main.getDropChance()) { // Drop skull
			ItemStack item = ItemBuilder.getPlayerHead(p.getName()).setName(p.getName() + "'s Head").setAmount(1).toItemStack();
			p.getWorld().dropItem(p.getLocation(), item);
		} else return; // Unfortunate.
	}
}
