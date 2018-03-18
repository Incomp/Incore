package me.incomp.plugins.pub.incore.modules.itemban;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bukkit.Material;
import org.bukkit.potion.PotionEffectType;

import me.incomp.plugins.pub.incore.IPlugin;
import me.incomp.plugins.pub.incore.messaging.Formatter;
import me.incomp.plugins.pub.incore.modules.Module;

/**
 * Main class for the Item Ban module.
 * 
 * @author Incomp
 * @since Mar 16, 2018
 */
public class ItemBanModule extends Module {

	private Map<Material, Integer> bannedItems = new HashMap<>();
	private Map<PotionEffectType, Integer> bannedPotions = new HashMap<>();
	private String itemBanMessage = Formatter.color("&4That item can't be used.");
	private boolean removeFromInventory = false;
	private long timerDelay = 0L;
	private long timerPeriod = 40L;
	
	private ItemBanTimer timer;
	
	public ItemBanModule(IPlugin plugin) {
		super(plugin, "Item Ban", "Implements server-wide item bans.");
	}

	@Override
	public void onLoad() {
		this.info(" === INCORE MODULE: " + this.getName() + " === ");
		this.info("Reading from config...");
		this.initConfig();
		this.info("Done.");
		
		this.info("Registering listeners...");
		this.addListener(new ItemBanListener(this));
		this.info("Done.");
		
		this.info(" === INCORE MODULE: " + this.getName() + " === ");
	}
	
	@Override
	public void initConfig() {
		List<String> items = this.getConfig().getStringList("bannedItems");
		if(!items.isEmpty()) {
			// Loop through.
			for(String i : items) {
				String[] component = i.split(":");
				String matName = component[0];
				int metadata; 
				
				try {
					metadata = Integer.parseInt(component[1]);
				} catch (NumberFormatException e) {
					this.warn("ItemBanModule: Invalid metadata for banned item \"" + matName + "\". Fucking mong. Learn what an integer is. Skipping this entry.");
					continue; // Skip.
				}
				
				Material material = Material.valueOf(matName);
				
				if(material == null) {
					this.warn("ItemBanModule: Banned item list contains an item called \"" + matName + "\". There is no such item with that name, dipshit. Skipping this entry.");
					continue; // Skip to next entry.
				}
				
				this.bannedItems.put(material, metadata);
			}
		}
		
		List<String> pots = this.getConfig().getStringList("bannedPotionEffects");
		if(!pots.isEmpty()) {
			// Loop fucking through.
			for(String p : pots) {
				String[] component = p.split(":");
				String effectName = component[0];
				int amp;
				
				try {
					amp = Integer.parseInt(component[1]);
				} catch (NumberFormatException e) {
					this.warn("ItemBanModule: Invalid metadata for banned potion effect \"" + effectName + "\". Learn what an integer is. Skipping this entry.");
					continue; // Skip.
				}
				
				PotionEffectType effect = PotionEffectType.getByName(effectName);
				
				if(effect == null) {
					this.warn("ItemBanModule: Banned potion effect list contains an effect called \"" + effectName + "\". There is no such effect with that name, cunt. Skipping this entry.");
					continue; // Skip to next entry.
				}
				
				this.bannedPotions.put(effect, amp);
			}
		}
		
		this.itemBanMessage = Formatter.color(this.getConfig().getString("general.itemBanMessage", "&4That item can't be used."));
		this.removeFromInventory = this.getConfig().getBoolean("general.removeFromInventory", false);
		this.timerDelay = Long.valueOf(this.getConfig().getInt("timer.delay", 0));
		this.timerPeriod = Long.valueOf(this.getConfig().getInt("timer.period", 40));
		this.timer = new ItemBanTimer(this, this.timerDelay, this.timerPeriod);
	}

	@Override
	public void onUnload() {
		this.info(" === INCORE MODULE: " + this.getName() + " === ");
		this.info("Unregistering listeners...");
		this.removeListeners();
		this.info("Done.");
		this.info(" === INCORE MODULE: " + this.getName() + " === ");
	}

	/**
	 * Gets an unmodifiable version of the item blacklist.
	 * 
	 * @return item blacklist
	 */
	public Map<Material, Integer> getItemBlacklist(){
		return Collections.unmodifiableMap(this.bannedItems);
	}
	
	/**
	 * Gets an unmodifiable version of the potion blacklist.
	 * 
	 * @return potion blacklist
	 */
	public Map<PotionEffectType, Integer> getPotionEffectBlacklist(){
		return Collections.unmodifiableMap(this.bannedPotions);
	}
	
	/**
	 * Gets the configured ban message.
	 * 
	 * @return ban message
	 */
	public String getItemBanMessage() {
		return this.itemBanMessage;
	}
	
	/**
	 * Gets whether or not the module should remove blacklisted items from players' inventories
	 * when they try to use them.
	 * 
	 * @return boolean
	 */
	public boolean willRemoveFromInventory() {
		return this.removeFromInventory;
	}
	
	/**
	 * Gets the configured {@code ItemBanTimer} delay, which is the amount of ticks the runnable
	 * will wait before starting.
	 * 
	 * @return delay
	 */
	public long getTimerDelay() {
		return this.timerDelay;
	}
	
	/**
	 * Gets the configured {@code ItemBanTimer} period, which is the amount of ticks the runnable
	 * will wait between running again.
	 * 
	 * @return period
	 */
	public long getTimerPeriod() {
		return this.timerPeriod;
	}
}
