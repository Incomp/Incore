package me.incomp.plugins.pub.incore.items;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.DyeColor;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.EnchantmentStorageMeta;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.material.MaterialData;

/**
 * @author Incomp
 * @since Jul 15, 2016
 * 
 * A fancy and chainable util that allows you to create ItemStacks. Created by a fan of the
 * StringBuilder class for other fans of the StringBuilder class.
 */
public class ItemBuilder {
	private final ItemStack item;
	
	/**
	 * Constructs a brand-spankin' new ItemBuilder object. Uses {@link Material#STONE} for
	 * the base ItemStack with an amount of 1.
	 */
	public ItemBuilder(){
		this.item = new ItemStack(Material.STONE);
	}
	
	/**
	 * Constructs a brand-spankin' new ItemBuilder object. Uses a specified Material and
	 * a huge amount of 1 for the base ItemStack.
	 * 
	 * @param m -	Material to use for base ItemStack
	 */
	public ItemBuilder(Material m){
		this.item = new ItemStack(m);
	}
	
	/**
	 * Constructs a brand-spankin' new ItemBuilder. Uses a specified ItemStack as the
	 * base.
	 * 
	 * @param i -	Base ItemStack
	 */
	public ItemBuilder(ItemStack i){
		this.item = i;
	}
	
	/**
	 * Sets the amount of the work-in-progress ItemStack.
	 * 
	 * @param i -	New amount of items in the ItemStack
	 * @return ItemBuilder
	 */
	public ItemBuilder setAmount(int i){
		this.item.setAmount(i);
		return this;
	}
	
	/**
	 * Sets the Material of the work-in-progress ItemStack.
	 * 
	 * @param m
	 * @return ItemBuilder
	 */
	public ItemBuilder setMaterial(Material m){
		this.item.setType(m);
		return this;
	}
	
	/**
	 * Sets the durability of the work-in-progress ItemStack.
	 * 
	 * @param s -	Durability to set
	 * @return 		ItemBuilder
	 */
	public ItemBuilder setDurability(short s){
		this.item.setDurability(s);
		return this;
	}
	
	/**
	 * Sets the name of the work-in-progress ItemStack.
	 * 
	 * @param s -	New name to set
	 * @return 		ItemBuilder
	 */
	public ItemBuilder setName(String s){
		ItemMeta meta = this.item.getItemMeta();
		meta.setDisplayName(this.color(s));
		return this.setItemMeta(meta);
	}
	
	/**
	 * Adds some lines of lore to the work-in-progress ItemStack.
	 * 
	 * @param lore -	Lines of lore to add
	 * @return			ItemBuilder
	 */
	public ItemBuilder addLore(String... lore){
		ItemMeta meta = this.item.getItemMeta();
		List<String> curLore = new ArrayList<>();
		
		// Set current lore to the item's current lore (if it has any)
		if(meta.hasLore()){
			curLore = meta.getLore();
		}
		
		// Add all of the provided lore
		for(String s : lore){
			curLore.add(this.color(s));
		}
		
		meta.setLore(curLore);
		return this.setItemMeta(meta);
	}
	
	/**
	 * Replaces the lore of the work-in-progress ItemStack with a specified list of Strings.
	 * 
	 * @param lore -	New lore to set
	 * @return 			ItemBuilder
	 */
	public ItemBuilder setLore(List<String> lore){
		ItemMeta meta = this.item.getItemMeta();
		List<String> newLore = new ArrayList<>();
		for(String s : lore){
			newLore.add(this.color(s));
		}
		meta.setLore(newLore);
		return this.setItemMeta(meta);
	}
	
	/**
	 * Replaces the lore of the work-in-progress ItemStack with a specified array of Strings.
	 * 
	 * @param lore -	New lore to set
	 * @return 			ItemBuilder
	 */
	public ItemBuilder setLore(String... lore){
		ItemMeta meta = this.item.getItemMeta();
		List<String> newLore = new ArrayList<>();
		for(String s : lore){
			newLore.add(this.color(s));
		}
		meta.setLore(newLore);
		return this.setItemMeta(meta);
	}
	
	/**
	 * Replaces the lore of the work-in-progress ItemStack by splitting a single String into multiple Strings where a specified threshold
	 * is reached.
	 * 
	 * @param s -			Input String
	 * @param lineLength -	Cutoff length
	 * @return 				ItemBuilder
	 */
	public ItemBuilder setLore(String s, int lineLength){
		return this.setLore(this.splitToList(s, lineLength));
	}
	
	/**
	 * Replaces the lore of the work-in-progress ItemStack by splitting a single String into multiple Strings where the 32-character threshold
	 * is reached.
	 * 
	 * @param s -		Input String
	 * @return 			ItemBuilder
	 */
	public ItemBuilder setLore(String s){
		return this.setLore(this.splitToList(s, 32));
	}
	
	/**
	 * Clears all lore.
	 * 
	 * @return ItemBuilder
	 */
	public ItemBuilder clearLore(){
		ItemMeta meta = this.item.getItemMeta();
		meta.setLore(new ArrayList<String>());
		return this.setItemMeta(meta);
	}
	
	/**
	 * Adds all specified ItemFlags to the work-in-progress ItemStack.
	 * 
	 * @param flags - 	ItemFlags to add
	 * @return 			ItemBuilder
	 */
	public ItemBuilder addItemFlags(ItemFlag... flags){
		ItemMeta meta = this.item.getItemMeta();
		meta.addItemFlags(flags);
		return this.setItemMeta(meta);
	}
	
	/**
	 * Removes all specified ItemFlags from the work-in-progress ItemStack.
	 * 
	 * @param flags -	ItemFlags to remove
	 * @return 			ItemBuilder
	 */
	public ItemBuilder removeItemFlags(ItemFlag...flags){
		ItemMeta meta = this.item.getItemMeta();
		meta.removeItemFlags(flags);
		return this.setItemMeta(meta);
	}
	
	/**
	 * Clears all of the ItemFlags from the work-in-progress ItemStack.
	 * 
	 * @return ItemBuilder
	 */
	public ItemBuilder clearItemFlags(){
		ItemMeta meta = this.item.getItemMeta();
		for(ItemFlag flag : meta.getItemFlags()){
			meta.removeItemFlags(flag);
		}
		return this.setItemMeta(meta);
	}
	
	/**
	 * Adds an Enchantment with a specified level to the work-in-progress ItemStack.</br>
	 * If the Enchantment is already present on the item, its level will be overriden.
	 * 
	 * @param ench -	Enchantment to add
	 * @param level -	Enchantment level
	 * @return 			ItemBuilder
	 */
	public ItemBuilder addEnchantment(Enchantment ench, int level){
		if(!this.item.containsEnchantment(ench)){
			this.item.addUnsafeEnchantment(ench, level);
		}else{
			if(this.item.getEnchantmentLevel(ench) != level){
				Map<Enchantment, Integer> enchants = this.item.getEnchantments();
				
				// Clear current enchantments.
				for(Enchantment e : enchants.keySet()){
					this.item.removeEnchantment(e);
				}
				
				enchants.put(ench, level);
				this.item.addEnchantments(enchants);
			}
		}
		return this;
	}
		
	/**
	 * Adds a set of Enchantments to the work-in-progress ItemStack.
	 * 
	 * @param enchants -	Enchantments to add, along with their specified levels
	 * @return 				ItemBuilder
	 */
	public ItemBuilder addEnchantments(Map<Enchantment, Integer> enchants){
		this.item.addUnsafeEnchantments(enchants);
		return this;
	}
		
	/**
	 * Removes a specified Enchantment, or a bunch of them, to the work-in-progress ItemStack.
	 * 
	 * @param enchants - 	Enchantment(s) to remove
	 * @return 				ItemBuilder
	 */
	public ItemBuilder removeEnchantments(Enchantment... enchants){
		for(Enchantment ench : enchants){
			if(this.item.containsEnchantment(ench)){
				this.item.removeEnchantment(ench);
			}else continue;
		}
		return this;
	}
	
	/**
	 * Clears all of the Enchantments from the work-in-progress ItemStack.
	 * 
	 * @return ItemBuilder
	 */
	public ItemBuilder clearEnchantments(){
		Map<Enchantment, Integer> enchants = this.item.getEnchantments();
		for(Enchantment e : enchants.keySet()){
			this.item.removeEnchantment(e);
		}
		return this;
	}
	
	/**
	 * Adds a stored enchantment with a specified level to the work-in-progress ItemStack.</br>
	 * If the ItemStack already has said enchant, it will be overridden.</br>
	 * <br>
	 * <font color=red><b>This won't work if the ItemBuilder's ItemStack is not an enchanted book.</b></font>
	 * 
	 * @param ench -	Enchantment to add
	 * @param level -	Enchantment level
	 * @return 			ItemBuilder
	 */
	public ItemBuilder addStoredEnchantment(Enchantment ench, int level){
		if(this.item.getType() != Material.ENCHANTED_BOOK){
			Bukkit.getLogger().info("Cannot edit the stored enchantments of an item that is not an enchanted book.");
			return this;
		}
		
		EnchantmentStorageMeta esm = (EnchantmentStorageMeta) this.item.getItemMeta();
		if(!esm.getStoredEnchants().containsKey(ench)){
			esm.addStoredEnchant(ench, level, true);
		}else{
			Map<Enchantment, Integer> stored = esm.getStoredEnchants();
			esm.getStoredEnchants().clear();
			stored.put(ench, level);
			for(Entry<Enchantment, Integer> entry : stored.entrySet()){
				esm.addStoredEnchant(ench, level, true);
			}
		}
		return this.setItemMeta(esm);
	}
	
	/**
	 * Removes a specified stored enchantment from the work-in-progress ItemStack.</br>
	 * </br>
	 * <font color=red><b>This won't work if the ItemBuilder's ItemStack is not an enchanted book.</b></font>
	 * 
	 * @param ench -	Enchantment to remove from storage
	 * @return 			ItemBuilder
	 */
	public ItemBuilder removeStoredEnchantment(Enchantment ench){
		if(this.item.getType() != Material.ENCHANTED_BOOK){
			Bukkit.getLogger().info("Cannot edit the stored enchantments of an item that is not an enchanted book.");
			return this;
		}
		
		EnchantmentStorageMeta esm = (EnchantmentStorageMeta) this.item.getItemMeta();
		esm.removeStoredEnchant(ench);
		return this.setItemMeta(esm);
	}
	
	/**
	 * Clears all of the stored enchantments on the work-in-progress ItemStack.</br>
	 * </br>
	 * <font color=red><b>This won't work if the ItemBuilder's ItemStack is not an enchanted book.</b></font>
	 * 
	 * @return ItemBuilder
	 */
	public ItemBuilder clearStoredEnchantments(){
		if(this.item.getType() != Material.ENCHANTED_BOOK){
			Bukkit.getLogger().info("Cannot edit the stored enchantments of an item that is not an enchanted book.");
			return this;
		}
		
		EnchantmentStorageMeta esm = (EnchantmentStorageMeta) this.item.getItemMeta();
		esm.getStoredEnchants().clear();
		return this;
	}
	
	/**
	 * Sets the owner of the work-in-progress ItemStack, given that it's a player head.</br>
	 * </br>
	 * <font color=red><b>This won't work if the ItemBuilder's ItemStack is not a player head.</b></font>
	 * 
	 * @param s - 	Username of the owner to set
	 * @return 		ItemBuilder
	 */
	public ItemBuilder setOwner(String s){
		if(this.item.getType() != Material.SKULL_ITEM){
			Bukkit.getLogger().info("Cannot edit the owner of an item that is not a player skull.");
			return this;
		}
		
		if(this.item.getDurability() != 3){
			Bukkit.getLogger().info("Cannot edit the owner of an item that is not a player skull.");
			return this;
		}
		
		SkullMeta sm = (SkullMeta) this.item.getItemMeta();
		sm.setOwner(s);
		return this.setItemMeta(sm);
	}
	
	/**
	 * Sets the ItemMeta of the work-in-progress ItemStack.
	 * 
	 * @param im -	ItemMeta to set
	 * @return 		ItemBuilder
	 */
	public ItemBuilder setItemMeta(ItemMeta im){
		this.item.setItemMeta(im);
		return this;
	}
	
	/**
	 * Sets the MaterialData of the work-in-progress ItemStack.
	 * 
	 * @param md -	MaterialData to set
	 * @return 		ItemBuilder
	 */
	public ItemBuilder setMaterialData(MaterialData md){
		this.item.setData(md);
		return this;
	}
	
	/**
	 * Copies over this ItemBuilder so you can branch off and make a mutated version. Awesome.
	 */
	public ItemBuilder clone(){
		return new ItemBuilder(this.item);
	}
	
	/**
	 * Gets the current ItemStack.
	 * 
	 * @return ItemStack
	 */
	public ItemStack toItemStack(){
		return this.item;
	}
	
	/**
	 * Colors a String with fancy color codes.
	 * 
	 * @param s -		Input String to colorize
	 * @return String
	 */
	private String color(String s){
		return ChatColor.translateAlternateColorCodes('&', s);
	}
	
	/**
	 * Takes a String and splits it into a list of Strings, dictated by a
	 * cutoff length.
	 * 
	 * @param input -	Input String to split	
	 * @param cutoff -	Cutoff length to split at
	 * @return 			List<String>
	 */
	private List<String> splitToList(String input, int cutoff){
		List<String> out = new ArrayList<String>();
		Pattern pat = Pattern.compile("\\G\\s*(.{1," + cutoff + "})(?=\\s|$)", Pattern.DOTALL);
		Matcher m = pat.matcher(input);
		while(m.find()){
			out.add(m.group(1));
		}
		return out;
	}
	
	/**
	 * A shortcut to get a skull item of a specified head type.
	 * 
	 * @param headType -	Type of head to set
	 * @return				ItemBuilder
	 */
	public static ItemBuilder getHead(HeadType headType){
		return new ItemBuilder(Material.SKULL_ITEM).setDurability(headType.getData());
	}
	
	/**
	 * A shortcut to get player's head as an item.
	 * 
	 * @param ownerName -	The player whose head will be retrieved
	 * @return				ItemBuilder
	 */
	public static ItemBuilder getPlayerHead(String ownerName){
		return new ItemBuilder(Material.SKULL_ITEM).setDurability((short) 3).setOwner(ownerName);
	}
	
	/**
	 * A shortcut to get an enchanted book with a set of predefined enchantments.
	 * 
	 * @param enchants -	Enchantments to put on the book
	 * @return				ItemBuilder
	 */
	public static ItemBuilder getEnchantedBook(Map<Enchantment, Integer> enchants){
		ItemBuilder ib = new ItemBuilder(Material.ENCHANTED_BOOK);
		for(Entry<Enchantment, Integer> entry : enchants.entrySet()){
			ib.addStoredEnchantment(entry.getKey(), entry.getValue());
		}
		return ib;
	}
	
	/**
	 * A shortcut to get a block of wool of a certain color.
	 * 
	 * @param color -	Desired color
	 * @return			ItemBuilder
	 */
	@SuppressWarnings("deprecation")
	public static ItemBuilder getWool(DyeColor color){
		return new ItemBuilder(Material.WOOL).setDurability(color.getWoolData());
	}
	
	/**
	 * A shortcut to get a block of stained glass of a certain color.</br>
	 * <b>Note:</b> Stained Glass was added in Minecraft 1.7.2.
	 * 
	 * @param color -	Desired color
	 * @return			ItemBuilder
	 */
	@SuppressWarnings("deprecation")
	public static ItemBuilder getStainedGlass(DyeColor color){
		return new ItemBuilder(Material.STAINED_GLASS).setDurability(color.getWoolData());
	}
	
	/**
	 * A shortcut to get a block of terracotta of a certain color.</br>
	 * <b>Note:</b> Hardened Clay was renamed to Terracotta in Minecraft 1.12.
	 * 
	 * @param color -	Desired color
	 * @return			ItemBuilder
	 */
	@SuppressWarnings("deprecation")
	public static ItemBuilder getTerracotta(DyeColor color){
		return new ItemBuilder(Material.STAINED_CLAY).setDurability(color.getWoolData());
	}
	
	/**
	 * A shortcut to get a banner of a certain base color.<br>
	 * <b>Note:</b> Banners were introduced in Minecraft 1.8.
	 * 
	 * @param color -	Desired base color
	 * @return			ItemBuilder
	 */
	@SuppressWarnings("deprecation")
	public static ItemBuilder getBanner(DyeColor color){
		return new ItemBuilder(Material.BANNER).setDurability(color.getWoolData());
	}
	
	/**
	 * A shortcut to get a bed of a certain color.</br>
	 * <b>Note:</b> Beds were made colorable in Minecraft 1.12.
	 * 
	 * @param color -	Desired color
	 * @return			ItemBuilder
	 */
	@SuppressWarnings("deprecation")
	public static ItemBuilder getBed(DyeColor color){
		return new ItemBuilder(Material.BED).setDurability(color.getWoolData());
	}
	
	/**
	 * A shortcut to get a block of a stained glass pane of a certain color.</br>
	 * <b>Note:</b> Stained Glass Panes were added in Minecraft 1.7.2.
	 * 
	 * @param color -	Desired color
	 * @return			ItemBuilder
	 */
	@SuppressWarnings("deprecation")
	public static ItemBuilder getGlassPane(DyeColor color){
		return new ItemBuilder(Material.STAINED_GLASS_PANE).setDurability(color.getWoolData());
	}
	
	/**
	 * A shortcut to get a certain type of glazed terracotta.</br>
	 * <b>Note:</b> Glazed Terracotta was added in Minecraft 1.12.
	 * <b>Note:</b> The type of Glazed Terracotta affects both the block's texture and color.
	 * 
	 * @param color -	Desired color
	 * @return			ItemBuilder
	 */
	public static ItemBuilder getGlazedTerracotta(DyeColor color){
		// Since terracotta isn't based on metadata, I'll have to use a switch.
		ItemBuilder ib = new ItemBuilder();
		
		switch(color){
			case BLACK:
				ib.setMaterial(Material.BLACK_GLAZED_TERRACOTTA);
				break;
			case BLUE:
				ib.setMaterial(Material.BLUE_GLAZED_TERRACOTTA);
				break;
			case BROWN:
				ib.setMaterial(Material.BROWN_GLAZED_TERRACOTTA);
				break;
			case CYAN:
				ib.setMaterial(Material.CYAN_GLAZED_TERRACOTTA);
				break;
			case GRAY:
				ib.setMaterial(Material.GRAY_GLAZED_TERRACOTTA);
				break;
			case GREEN:
				ib.setMaterial(Material.GREEN_GLAZED_TERRACOTTA);
				break;
			case LIGHT_BLUE:
				ib.setMaterial(Material.LIGHT_BLUE_GLAZED_TERRACOTTA);
				break;
			case LIME:
				ib.setMaterial(Material.LIME_GLAZED_TERRACOTTA);
				break;
			case MAGENTA:
				ib.setMaterial(Material.MAGENTA_GLAZED_TERRACOTTA);
				break;
			case ORANGE:
				ib.setMaterial(Material.ORANGE_GLAZED_TERRACOTTA);
				break;
			case PINK:
				ib.setMaterial(Material.PINK_GLAZED_TERRACOTTA);
				break;
			case PURPLE:
				ib.setMaterial(Material.PURPLE_GLAZED_TERRACOTTA);
				break;
			case RED:
				ib.setMaterial(Material.RED_GLAZED_TERRACOTTA);
				break;
			case SILVER:
				ib.setMaterial(Material.SILVER_GLAZED_TERRACOTTA);
				break;
			case WHITE:
				ib.setMaterial(Material.WHITE_GLAZED_TERRACOTTA);
				break;
			case YELLOW:
				ib.setMaterial(Material.YELLOW_GLAZED_TERRACOTTA);
				break;
			default:
				ib.setMaterial(Material.WHITE_GLAZED_TERRACOTTA);
				break;
		}
		
		return ib;
	}
	
	/**
	 * A shortcut to get a certain color of a shulker box.</br>
	 * <b>Note:</b> Shulker Boxes were added in Minecraft 1.11.
	 * 
	 * @param color -	Desired color
	 * @return			ItemBuilder
	 */
	public static ItemBuilder getShulkerBox(DyeColor color){
		// Since shulker boxes aren't based on metadata, I'll have to use a switch.
		ItemBuilder ib = new ItemBuilder();
		
		switch(color){
			case BLACK:
				ib.setMaterial(Material.BLACK_SHULKER_BOX);
				break;
			case BLUE:
				ib.setMaterial(Material.BLUE_SHULKER_BOX);
				break;
			case BROWN:
				ib.setMaterial(Material.BROWN_SHULKER_BOX);
				break;
			case CYAN:
				ib.setMaterial(Material.CYAN_SHULKER_BOX);
				break;
			case GRAY:
				ib.setMaterial(Material.GRAY_SHULKER_BOX);
				break;
			case GREEN:
				ib.setMaterial(Material.GREEN_SHULKER_BOX);
				break;
			case LIGHT_BLUE:
				ib.setMaterial(Material.LIGHT_BLUE_SHULKER_BOX);
				break;
			case LIME:
				ib.setMaterial(Material.LIME_SHULKER_BOX);
				break;
			case MAGENTA:
				ib.setMaterial(Material.MAGENTA_SHULKER_BOX);
				break;
			case ORANGE:
				ib.setMaterial(Material.ORANGE_SHULKER_BOX);
				break;
			case PINK:
				ib.setMaterial(Material.PINK_SHULKER_BOX);
				break;
			case PURPLE:
				ib.setMaterial(Material.PURPLE_SHULKER_BOX);
				break;
			case RED:
				ib.setMaterial(Material.RED_SHULKER_BOX);
				break;
			case SILVER:
				ib.setMaterial(Material.SILVER_SHULKER_BOX);
				break;
			case WHITE:
				ib.setMaterial(Material.WHITE_SHULKER_BOX);
				break;
			case YELLOW:
				ib.setMaterial(Material.YELLOW_SHULKER_BOX);
				break;
			default:
				ib.setMaterial(Material.WHITE_SHULKER_BOX);
				break;
		}
		
		return ib;
	}

	/**
	 * Used to get the amount of empty slots in an {@link Inventory}. Armor contents are
	 * not considered.
	 * 
	 * @param inv	inventory to count
	 * @return		number of open slots
	 */
	public static int getOpenSlots(Inventory inv){
		int i = 0;
		for(int c = 0; c > (inv.getSize() - 1); c++){
			if(inv.getItem(c) == null){
				i++;
				continue;
			}else continue;
		}
		return i;
	}
	
	/**
	 * Gets the slot of the first matching ItemStack in an Inventory.
	 * 
	 * @param inv	Inventory whose contents will be checked
	 * @param i		ItemStack to index by
	 * @return		slot of the first ItemStack to match the given one
	 */
	public static int getSlot(Inventory inv, ItemStack i){
		for(int x = 0; x < inv.getSize(); x++){
			if(inv.getItem(x) == null) continue;
			if(inv.getItem(x).isSimilar(i)){
				return x;
			}else continue;
		}
		return -1;
	}
	
	/**
	 * Gets the next open slot in an Inventory.
	 * 
	 * @param inv	Inventory to check for an open slot
	 * @return		index of first open slot, if any
	 */
	public static int getNextOpenSlot(Inventory inv){
		for(int x = 0; x < inv.getSize(); x++){
			if(inv.getItem(x) == null || inv.getItem(x).getType() == Material.AIR){
				return x;
			}else continue;
		}
		return -1;
	}
	
	public enum HeadType {
		
		SKELETON(0),
		WITHER_SKELETON(1),
		ZOMBIE(2),
		PLAYER(3),
		CREEPER(4),
		ENDERDRAGON(5);
		
		private final byte data;
		
		private HeadType(int data){
			this.data = (byte) data;
		}
		
		public byte getData(){
			return this.data;
		}
	}
	
	public static ChatColor convertToChatColor(DyeColor dye) {
		switch(dye) {
			case BLACK: return ChatColor.BLACK;
			case BLUE: return ChatColor.BLUE;
			case BROWN: return ChatColor.WHITE;
			case CYAN: return ChatColor.DARK_AQUA;
			case GRAY: return ChatColor.DARK_GRAY;
			case GREEN: return ChatColor.DARK_GREEN;
			case LIGHT_BLUE: return ChatColor.AQUA;
			case LIME: return ChatColor.GREEN;
			case MAGENTA: return ChatColor.LIGHT_PURPLE;
			case ORANGE: return ChatColor.GOLD;
			case PINK: return ChatColor.RED;
			case PURPLE: return ChatColor.DARK_PURPLE;
			case RED: return ChatColor.RED;
			case SILVER: return ChatColor.GRAY;
			case WHITE: return ChatColor.WHITE;
			case YELLOW: return ChatColor.YELLOW;
			default: return ChatColor.WHITE;
			
		}
	}
}