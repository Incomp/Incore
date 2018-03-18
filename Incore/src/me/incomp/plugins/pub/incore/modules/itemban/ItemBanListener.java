package me.incomp.plugins.pub.incore.modules.itemban;

import java.util.Map;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.PrepareItemCraftEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.Recipe;

import me.incomp.plugins.pub.incore.Perm;

/**
 * Contains the event handlers for the Item Ban module.
 * 
 * @author Incomp
 * @since Mar 17, 2018
 */
public class ItemBanListener implements Listener {

	private final ItemBanModule module;
	
	public ItemBanListener(ItemBanModule module) {
		this.module = module;
	}
	
	@EventHandler
	public void onInteract(PlayerInteractEvent event) {
		// I don't care about off-hand since I'm pretty sure you can't use it anyway.
		// Plus, I don't want this method's body to be called twice, since Spigot
		// calls it once for each hand.
		if(event.getHand() == EquipmentSlot.OFF_HAND) return;
		
		Player p = event.getPlayer();
		
		// Check for the bypass permission node.
		if(Perm.MODULE_ITEMBAN_BYPASS.has(p)) return; // Do nothing if they can bypass.
		
		ItemStack item = p.getInventory().getItemInMainHand();
		
		// Don't do anything if the player isn't holding an item.
		if(item == null) return;
		
		// Now for straight item checking.
		Map<Material, Integer> blacklist = this.module.getItemBlacklist();
		
		if(blacklist.containsKey(item.getType())) { // Item type is blacklisted.
			int i = blacklist.get(item.getType());
			
			// Check for sweeping blacklisting of all data values.
			if(i < 0) {
				event.setCancelled(true); // Cancel event
				
				if(this.module.willRemoveFromInventory()) {
					ItemStack result = item.clone();
					result.setAmount(item.getAmount() - 1);
					// If the remaining amount is over 0, put the right item with the right quantity
					// in the player's hand. Otherwise, just remove the item.
					p.getInventory().setItemInMainHand((result.getAmount() > 0) ? result : null);
					p.updateInventory();
				}
				
				if(!this.module.getItemBanMessage().isEmpty()) {
					p.sendMessage(this.module.getItemBanMessage());
				}
			} else { // Only a certain durability value is blacklisted, so we need to account for it.
				short dur = (short) i;
				if(item.getDurability() == dur) { // Item type and data are blacklisted.
					event.setCancelled(true);
					
					if(this.module.willRemoveFromInventory()) {
						ItemStack result = item.clone();
						result.setAmount(item.getAmount() - 1);
						p.getInventory().setItemInMainHand((result.getAmount() > 0) ? result : null);
						p.updateInventory();
					}
					
					if(!this.module.getItemBanMessage().isEmpty()) {
						p.sendMessage(this.module.getItemBanMessage());
					}
				}
			}
		} else return; // Item type is not blacklisted; do nothing.
	}
	
	@EventHandler
	public void onPrepareItemCraft(PrepareItemCraftEvent event) {
		Recipe r = event.getRecipe();
		ItemStack result = r.getResult();
		Material mat = result.getType();
		
		if(this.module.getItemBlacklist().containsKey(mat)) {
			int d = this.module.getItemBlacklist().get(mat);
			
			if(d < 0) { // Sweeping blacklist.
				event.getInventory().setResult(new ItemStack(Material.AIR));
			} else { // Specific shit.
				if(result.getDurability() >= (short) d) {
					event.getInventory().setResult(new ItemStack(Material.AIR));
				} else return; // Do nothing. The durability is fine.
			}
			
		} else return; // Do nothing.
	}
	
}
