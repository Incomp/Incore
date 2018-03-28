package me.incomp.plugins.pub.incore.modules;

import java.io.File;
import java.util.HashSet;
import java.util.Set;
import java.util.logging.Level;

import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;

import me.incomp.plugins.pub.incore.IPlugin;

/**
 * Serves as a kind of "mini-plugin".
 * 
 * @author Brend
 * @author Incomp
 */
public abstract class Module {

	private final IPlugin plugin;
	private final String name;
	private final String description;
	private final Set<Listener> listeners = new HashSet<>();
	private FileConfiguration config;
	
    private boolean enabled = false;
    private boolean configLoaded = false;

    /**
     * Initializes a module.
     * 
     * @param plugin			- Parent plugin
     * @param moduleName		- Name of the new module
     * @param description		- Short description, similar to "Implements <xyz>"
     */
    public Module(IPlugin plugin, String moduleName, String description) {
        this.plugin = plugin;
        this.name = moduleName;
        this.description = description;
    }
    
    /**
     * Executed when the module is loaded.</br>
     * </br>
     * <b>Recommended to call {@code IPlugin.info(String, true)} and output that the module has been loaded.</b>
     */
    public abstract void onLoad();
    
    /**
     * Serves as a method to hold your methods that set config-controlled variables. This is so that you can reload
     * modules' configs without reloading the entire thing, potentially causing some problems like double command
     * and listener registration.
     */
    public abstract void initConfig();
    
    /**
     * Executed when the module is unloaded.</br>
     * </br>
     * <b>Recommended to call {@code Module.info(String, true)} and output that the module has been unloaded.</b>
     */
    public abstract void onUnload();
    
    /**
     * Used to get the module's name.
     * 
     * @return String
     */
    public final String getName() {
        return this.name;
    }
    
    /**
     * Used to get the name that will be used for the module's directory.</br>
     * It will be the module's name, but with no spaces and in all lowercase letters. For example,
     * <code>Some Module</code> will become <code>somemodule</code>.
     * 
     * @return String
     */
    public final String getDirName() {
    	String dirName = this.name.replaceAll(" ", "").toLowerCase();
    	return dirName;
    }
    
    /**
     * Used to get the module's description.
     * 
     * @return String
     */
    public final String getDescription() {
    	return this.description;
    }

    /**
     * Used to get the JavaPlugin.
     * 
     * @return JavaPlugin
     */
    public final IPlugin getPlugin() {
    	return this.plugin;
    }
    
    /**
     * Used to check whether or not the module is enabled.
     * 
     * @return boolean
     */
    public final boolean isEnabled() {
        return this.enabled;
    }

    /**
     * Controls whether or not the module is enabled.
     * 
     * @param enable - status to set
     */
    public final void setEnabled(boolean enable) {
        if(this.enabled) {
        	if(enable) {
        		return; // Nothing to do.
        	} else {
        		this.onUnload();
        		this.enabled = enable;
        	}
        } else {
        	if(enable) {
        		this.onLoad();
        		this.enabled = enable;
        	} else {
        		return; // Nothing to do.
        	}
        }
    }
    
    /**
     * Adds listeners to the module and registers them.
     * 
     * @param listener		- Listener(s) to register
     */
    public final void addListener(Listener... listener) {
    	for(Listener l : listener) {
    		if(this.listeners.contains(l)) return; // don't register the same listener twice
    		this.listeners.add(l);
    		Bukkit.getPluginManager().registerEvents(l, this.plugin);
    		this.info("Module \"" + this.getName() + "\": Registered Listener \"" + l.getClass().getSimpleName() + "\".", true);
    	}
    }
    
    /**
     * Remove all of this module's listeners.
     */
    public final void removeListeners() {
    	for(Listener l : this.listeners) {
    		HandlerList.unregisterAll(l);
    		this.info("Module \"" + this.getName() + "\": Unregistered Listener \"" + l.getClass().getSimpleName() + "\".", true);
    	}
    }
    
    /**
     * Unregister a specific listener from this module.
     * 
     * @param listener		- Listener(s) to unregister
     */
    public final void removeListener(Listener... listener) {
    	for(Listener l : listener) {
    		if(!this.listeners.contains(l)) return; // only affect this module's listeners
    		HandlerList.unregisterAll(l);
    		this.info("Module \"" + this.getName() + "\": Unregistered Listener \"" + l.getClass().getSimpleName() + "\".", true);
    	}
    }
    
    /**
     * Loads the configuration on the first call. Afterwards, it just returns the cached config.
     * 
     * @return FileConfiguration
     */
    public final FileConfiguration getConfig() {
    	if(this.configLoaded) {
    		return this.config;
    	}
		
    	File file = new File(this.plugin.getDataFolder() + this.getDirName() + "/config.yml");
		if(!file.exists()) {
			this.plugin.saveResource(this.getDirName() + "/config.yml", false); // Replacing doesn't really matter.
		}
		this.config = YamlConfiguration.loadConfiguration(file);
		this.configLoaded = true;
		return this.config;
	}
    
    /**
     * Reloads the configuration file. Allows for implementation of reload commands.
     * 
     * @return FileConfiguration
     */
    public final FileConfiguration reloadConfig() {
    	File file = new File(this.plugin.getDataFolder() + this.getDirName() + "/config.yml");
		if(!file.exists()) {
			this.plugin.saveResource(this.getDirName() + "/config.yml", false); // Replacing doesn't really matter.
		}
		this.config = YamlConfiguration.loadConfiguration(file);
		this.configLoaded = true;
		return this.config;
    }
    
    /**
	 * Outputs a message to console.
	 * 
	 * @param lvl -					Level of the message
	 * @param msg -					Message to output
	 * @param bypassDebugCheck -	Whether or not this message is sent regardless of the configurable debug setting
	 * @return whether or not the message was sent
	 */
	public boolean log(Level lvl, String msg, boolean bypassDebugCheck){
		if(bypassDebugCheck){
			Bukkit.getLogger().log(lvl, msg);
			return true;
		}else{
			if(this.getConfig().getBoolean("debug.messages", false)){
				Bukkit.getLogger().log(lvl, msg);
				return true;
			}else return false;
		}
	}
	
	/**
	 * Outputs a message to console.
	 * 
	 * @param lvl -		Level of the message
	 * @param msg -		Message to output
	 */
	public void log(Level lvl, String msg){
		log(lvl, msg, true);
	}
	
	/**
	 * Outputs an info message to console.
	 * 
	 * @param msg -					Message to output
	 * @param bypassDebugCheck -	Whether or not this message is sent regardless of the configurable debug setting
	 * @return whether or not the message was sent
	 */
	public boolean info(String msg, boolean bypassDebugCheck){
		return this.log(Level.INFO, msg, bypassDebugCheck);
	}
	
	/**
	 * Outputs an info message to console, ignoring the debug check.
	 * 
	 * @param msg -		Message to output
	 */
	public void info(String msg){
		log(Level.INFO, msg);
	}
	
	/**
	 * Outputs a warning message to console.
	 * 
	 * @param msg -					Message to output
	 * @param bypassDebugCheck -	Whether or not this message is sent regardless of the configurable debug setting
	 * @return whether or not the message was sent
	 */
	public boolean warn(String msg, boolean bypassDebugCheck){
		return this.log(Level.WARNING, msg, bypassDebugCheck);
	}
	
	/**
	 * Outputs a warning message to console, ignoring the debug check.
	 * 
	 * @param msg -		Message to output
	 */
	public void warn(String msg){
		log(Level.WARNING, msg);
	}
	
	/**
	 * Outputs a severe message to console. Since it's... well... a severe message, it bypasses the debug check.
	 * 
	 * @param msg - 	Message to output
	 */
	public void severe(String msg){
		this.log(Level.SEVERE, msg, true);
	}
	
	/**
	 * Outputs a severe message to console and prints out all given exceptions.
	 * 
	 * @param msg -			Message to output
	 * @param errors -		Exceptions to print
	 */
	public void severe(String msg, Exception... errors){
		severe(msg);
		for(Exception e : errors){
			severe(e.getStackTrace().toString());
		}
	}
}