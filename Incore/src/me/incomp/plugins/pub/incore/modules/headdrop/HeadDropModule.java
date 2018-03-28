package me.incomp.plugins.pub.incore.modules.headdrop;

import org.bukkit.event.Listener;

import me.incomp.plugins.pub.incore.IPlugin;
import me.incomp.plugins.pub.incore.modules.Module;

/**
 * A mini-plugin that implements head dropping.
 * 
 * @author Incomp
 * @since Mar 16, 2018
 */
public class HeadDropModule extends Module {

	private IPlugin plugin;
	private int dropChance = 15;
	private boolean combatOnly = true;
	private Listener listener;
	
	public HeadDropModule(IPlugin plugin) {
		super(plugin, "Head Drop", "Implements on-death head dropping.");
	}

	public int getDropChance() {
		return this.dropChance;
	}
	
	public boolean onlyCombatDrops() {
		return this.combatOnly;
	}
	
	@Override
	public void onLoad() {
		this.info(" === INCORE MODULE: " + this.getName() + " === ");
		this.info("Reading from config...");
		this.initConfig();
		this.info("Done.");
		
		this.info("Registering listeners...");
		this.listener = new HeadDropListener(this);
		this.addListener(listener);
		this.info("Done.");
				
		this.info("Startup complete.");
		this.info(" === INCORE MODULE: " + this.getName() + " === ");
	}

	@Override
	public void initConfig() {
		this.dropChance = this.getConfig().getInt("general.dropChance", 15);
		
		if(this.dropChance > 100) this.warn("general.dropChance has a value over 100, which is invalid. Assuming value of 100.", true);
		if(this.dropChance < 0) this.warn("general.dropChance has a value under 0, which is invalid. Assuming value of 0.", true);
		
		this.combatOnly = this.getConfig().getBoolean("general.combatOnly", true);
	}
	
	@Override
	public void onUnload() {
		this.info(" === INCORE MODULE: " + this.getName() + " === ");
		this.info("Unregistering listeners...");
		this.removeListeners();
		this.info("Done.");
		this.info(" === INCORE MODULE: " + this.getName() + " === ");
	}
}
