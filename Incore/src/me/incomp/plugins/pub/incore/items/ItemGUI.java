package me.incomp.plugins.pub.incore.items;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryAction;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

/**
 * A small utility class for making dynamic item interfaces.
 *
 * @author Incomp
 * @since Jul 25, 2017
 */
public abstract class ItemGUI {

	private JavaPlugin plugin;
	private Player player;
	private Inventory inv;
	private List<ItemStack> contents = new ArrayList<>();
	
	protected Listener clickHandler;
	
	public ItemGUI(JavaPlugin plugin, Player player, InventoryHolder holder, int rows, String invName){
		this.plugin = plugin;
		this.player = player;
		
		// Just spew something out if an invalid number of rows is given
		if(rows > 6){
			Bukkit.getLogger().warning("Tried to create an ItemGUI with too many rows (" + rows + "). Using 6 instead.");
			rows = 6;
		}else if(rows < 1){
			Bukkit.getLogger().warning("Tried to create an ItemGUI with too few rows (" + rows + "). Using 1 instead.");
			rows = 1;
		}
		
		this.inv = Bukkit.createInventory(holder, rows * 9, invName);
		this.clickHandler = new ClickHandler(this, plugin);
		
		this.build();
	}
	
	/**
	 * This is where you build the inventory.
	 */
	public abstract void build();
	
	/**
	 * Called when a player clicks on an item in the GUI.
	 * 
	 * @param player 			- Player who clicked
	 * @param item				- ItemStack that was clicked on
	 * @param clickType			- Type of click
	 * @param estimateAction	- What the player seems to be trying to do; this can be safely ignored
	 */
	public abstract void onItemClick(Player player, ItemStack item, ClickType clickType, InventoryAction estimateAction);
	
	public void open(){
		this.player.openInventory(this.inv);
	}
	
	protected Player getPlayer(){
		return this.player;
	}
	
	protected Inventory getInventory(){
		return this.inv;
	}
	
	protected List<ItemStack> getContents(){
		return this.contents;
	}
	
	protected void cleanContents(){
		this.contents.clear();
		this.inv.clear();
	}
	
	protected void setItem(int slot, ItemStack item){
		this.addItem(item);
		this.getInventory().setItem(slot, item);
	}
	
	private void addItem(ItemStack item){
		this.contents.add(item);
	}
	
	protected void nuke(){
		HandlerList.unregisterAll(this.clickHandler);
		this.player = null;
	}
	
	protected int getSlot(ItemStack item){
		for(int x = 0; x < this.inv.getSize(); x++){
			ItemStack compare = this.inv.getItem(x);
			
			if(compare == null) continue;
			if(compare.getType() == Material.AIR) continue;
			
			if(item.isSimilar(compare)){
				return x;
			}else continue;
		}
		return -1;
	}
	
	public class ClickHandler implements Listener {
		
		private final ItemGUI gui;
		
		public ClickHandler(ItemGUI gui, JavaPlugin plugin){
			this.gui = gui;
			Bukkit.getPluginManager().registerEvents(this, plugin);
		}
		
		@EventHandler(priority = EventPriority.HIGH)
		public void onItemClick(InventoryClickEvent event){
			if(!(event.getWhoClicked() instanceof Player)) return;
			if(!event.getClickedInventory().equals(this.gui.getInventory())) return;
			
			event.setCancelled(true);
			Player player = (Player) event.getWhoClicked();
			
			if(event.getClickedInventory() == null || event.getCurrentItem().getType() == Material.AIR){
				player.playSound(player.getLocation(), Sound.ENTITY_ZOMBIE_ATTACK_IRON_DOOR, 0.5f, 1.0f);
				return;
			}
			
			this.gui.onItemClick(player, event.getCurrentItem(), event.getClick(), event.getAction());
			return;
		}
		
		@EventHandler(priority = EventPriority.HIGH)
		public void onInventoryClose(InventoryCloseEvent event){
			if(!(event.getPlayer() instanceof Player)) return;
			
			Player player = (Player) event.getPlayer();
			Inventory inv = event.getInventory();
			
			if(!player.getUniqueId().equals(gui.getPlayer().getUniqueId())) return;
			if(!inv.equals(gui.getInventory())) return;
			
			gui.nuke();
		}
	}
}
