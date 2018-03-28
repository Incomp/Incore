package me.incomp.plugins.pub.incore.modules.pay2cmd;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.bukkit.configuration.file.FileConfiguration;

import me.incomp.plugins.pub.incore.IPlugin;
import me.incomp.plugins.pub.incore.modules.Module;

/**
 * Main class for the Pay2Cmd module.
 * 
 * @author Incomp
 * @since Mar 21, 2018
 */
public class Pay2CmdModule extends Module {

	private boolean useConfirm = true;
	private int confirmTimeout = 15;
	private ConfirmMethod method = ConfirmMethod.GUI;
	
	private Map<String, Double> commands = new HashMap<>();
	
	public Pay2CmdModule(IPlugin plugin) {
		super(plugin, "Pay2Cmd", "Implements per-command costs.");
	}

	@Override
	public void onLoad() {
		this.info(" === INCORE MODULE: " + this.getName() + " === ");
		this.info("Reading from config...");
		this.initConfig();
		this.info("Done.");
		
		this.info("Registering listeners...");
		// TODO lol
		this.info("Done.");
				
		this.info("Startup complete.");
		this.info(" === INCORE MODULE: " + this.getName() + " === ");
	}

	@Override
	public void initConfig() {
		FileConfiguration conf = this.getConfig();
		
		this.useConfirm = conf.getBoolean("general.confirm", true);
		this.confirmTimeout = conf.getInt("general.confirm-timeout", 15);
		this.method = ConfirmMethod.valueOf(conf.getString("general.confirm-method", "GUI"));
		
		// Make sure the commands section is valid.
		if(!conf.isConfigurationSection("commands")) {
			this.severe("Pay2Cmd: Invalid configuration. You must have a \"commands\" section.");
			this.setEnabled(false);
			return;
		}
		
		// Bring the commands and their costs into memory.
		// If the costs are not configured correctly, this
		// will cause problems.
		for(String s : conf.getKeys(false)) {
			this.commands.put(s, conf.getDouble(s));
		}	
	}

	@Override
	public void onUnload() {
		this.info(" === INCORE MODULE: " + this.getName() + " === ");
		this.info("Unregistering listeners...");
		this.removeListeners();
		this.info("Done.");
		this.info(" === INCORE MODULE: " + this.getName() + " === ");
	}
	
	public boolean willConfirm() {
		return this.useConfirm;
	}
	
	public int getConfirmTimeout() {
		return this.confirmTimeout;
	}
	
	public ConfirmMethod getConfirmMethod() {
		return this.method;
	}
	
	public Map<String, Double> getCommands(){
		return Collections.unmodifiableMap(this.commands);
	}
	
	public double getCost(String cmd) {
		if(this.commands.containsKey(cmd)) {
			return this.commands.get(cmd);
		} else return 0;
	}
}
