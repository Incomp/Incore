package me.incomp.plugins.pub.incore;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.logging.Level;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

import me.incomp.plugins.pub.incore.commands.CommandException;
import me.incomp.plugins.pub.incore.commands.CommandPermissionsException;
import me.incomp.plugins.pub.incore.commands.CommandUsageException;
import me.incomp.plugins.pub.incore.commands.CommandsManager;
import me.incomp.plugins.pub.incore.commands.MissingNestedCommandException;
import me.incomp.plugins.pub.incore.commands.WrappedCommandException;
import me.incomp.plugins.pub.incore.commands.bukkit.CommandsManagerRegistration;
import me.incomp.plugins.pub.incore.messaging.FormatType;
import me.incomp.plugins.pub.incore.messaging.Formatter;
import me.incomp.plugins.pub.incore.modules.Module;

/**
 * @author Incomp
 * @since Jul 8, 2017
 */
public abstract class IPlugin extends JavaPlugin {
	
	protected static IPlugin INSTANCE;
	
	// sk89q's command framework
	protected CommandsManager<CommandSender> commands;
	protected CommandsManagerRegistration cmdReg;
	
	// Modules
	protected Set<Module> modules = new HashSet<>();
	
	@Override
	public abstract void onEnable();
	
	@Override
	public abstract void onDisable();
	
	public abstract void registerCommands();
	
	public static IPlugin getInstance() {
		return INSTANCE;
	}
	
	/**
	 * A necessary method. Put this near the top of onEnable().
	 */
	protected void initCommands(){
		this.commands = new CommandsManager<CommandSender>() {
			@Override
			public boolean hasPermission(CommandSender sender, String perm) {
				return sender instanceof ConsoleCommandSender || sender.hasPermission(perm);
			}
		};
		
		this.cmdReg = new CommandsManagerRegistration(this, this.commands);
	}
	
	public CommandsManager<CommandSender> getCommandsManager(){
		return this.commands;
	}
	
	public CommandsManagerRegistration getCommandRegistration() {
		return this.cmdReg;
	}
	
	/**
	 * This just catches each command and tries to execute it if possible. Don't call it.
	 */
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args) {
		try {
			this.commands.execute(cmd.getName(), args, sender, sender);
		} catch (CommandPermissionsException e) {
			sender.sendMessage(Formatter.format("Access", "Access denied.", FormatType.ERROR));
		} catch (MissingNestedCommandException e) {
			sender.sendMessage(Formatter.format("Syntax", "You're missing a nested command. Try this:", FormatType.ERROR));
			sender.sendMessage(Formatter.format("Syntax", e.getUsage(), FormatType.ERROR));
		} catch (CommandUsageException e) {
			sender.sendMessage(Formatter.format("Syntax", e.getMessage() + " Try this:", FormatType.ERROR));
			sender.sendMessage(Formatter.format("Syntax", e.getUsage(), FormatType.ERROR));
		} catch (WrappedCommandException e) {
			if (e.getCause() instanceof NumberFormatException) {
				sender.sendMessage(Formatter.format("Syntax", "We needed a number, but you gave us a string!", FormatType.ERROR));
			} else {
				sender.sendMessage(Formatter.format("OH NO", "The end is upon us! Contact an Owner or Developer immediately.", FormatType.ERROR));
				e.printStackTrace();
			}
		} catch (CommandException e) {
			sender.sendMessage(Formatter.format("Syntax", "Well... something broke.", FormatType.ERROR));
			Formatter.sendExceptionDebug(e);
		}
		return true;
	}
	
	/**
	 * Used to add a module to an IPlugin.
	 * 
	 * @param m		- Module to add
	 */
	public void addModule(Module m) {
		this.info("IPlugin: Added module: " + m.getName(), true);
		if(this.modules.contains(m)) return; // Module is already added; do nothing.
		
		this.modules.add(m);
		// Check to make sure it's enabled in the backend.
		if(m.getConfig().getBoolean("general.enable", true)) {
			m.setEnabled(true);
		} else return; // Module is not enabled, so I won't enable it.
	}
	
	/**
	 * Used to add modules to an IPlugin. In DROVES.
	 * 
	 * @param modules	- Modules to add
	 */
	public void addModules(Module... modules) {
		for(Module m : modules) this.addModule(m);
	}
	
	/**
	 * Used to remove a module from an IPlugin.</br>
	 * This method probably shouldn't ever be called outside of <code>IPlugin.onDisable()</code>.
	 * 
	 * @param m		- Module to remove
	 */
	public void removeModule(Module m) {
		if(!this.modules.contains(m)) return; // No such module; do nothing.
		
		m.setEnabled(false);
		this.modules.remove(m);
	}
	
	/**
	 * Used to remove modules from an IPlugin. In DROVES.
	 * 
	 * @param modules	- Modules to remove
	 */
	public void removeModules(Module... modules) {
		for(Module m : modules) this.removeModule(m);
	}
	
	/**
	 * Used to remove a module from an IPlugin.</br>
	 * This method probably shouldn't ever be called outside of <code>IPlugin.onDisable()</code>.
	 * 
	 * @param moduleName	- Name of module to disable.
	 */
	public void removeModule(String moduleName) {
		for(Module m : this.modules) {
			if(m.getName().equalsIgnoreCase(moduleName)) {
				this.removeModule(m);
				break;
			}else continue;
		}
	}
	
	/**
	 * Used to remove modules via their names from an IPlugin. In DROVES.
	 * 
	 * @param moduleNames	- Names of modules to remove
	 */
	public void removeModules(String... moduleNames) {
		for(String s : moduleNames) this.removeModule(s);
	}
	
	/**
	 * Used to enable a module.
	 * 
	 * @param m		- Module to enable
	 */
	public void enableModule(Module m) {
		if(!this.modules.contains(m)) return; // No such module; do nothing.
		if(m.isEnabled()) return; // Module is already enabled; do nothing.
		
		m.setEnabled(true);
	}
	
	/**
	 * Used to enable a module via its name.
	 * 
	 * @param moduleName	- Name of module to enable
	 */
	public void enableModule(String moduleName) {
		for(Module m : this.modules) {
			if(m.getName().equalsIgnoreCase(moduleName)) {
				this.enableModule(m);
				break;
			}else continue;
		}
	}
	
	/**
	 * Used to disable a module.
	 * 
	 * @param m		- Module to disable
	 */
	public void disableModule(Module m) {
		if(!this.modules.contains(m)) return; // No such module; do nothing.
		if(!m.isEnabled()) return; // Module is already disabled; do nothing.
		
		m.setEnabled(false);
	}
	
	/**
	 * Used to disable a module via its name.
	 * 
	 * @param moduleName	- Name of module to disable
	 */
	public void disableModule(String moduleName) {
		for(Module m : this.modules) {
			if(m.getName().equalsIgnoreCase(moduleName)) {
				this.disableModule(m);
				break;
			}else continue;
		}
	}
	
	/**
	 * Used to get the list of all modules.
	 * 
	 * @return Set<Module>
	 */
	public Set<Module> getModules(){
		return Collections.unmodifiableSet(this.modules);
	}
	
	public Module getModule(String moduleName) {
		for(Module m : this.modules) {
			if(moduleName.equalsIgnoreCase(m.getName()) || moduleName.equalsIgnoreCase(m.getDirName())) {
				return m;
			} else continue;
		}
		
		this.info("IPlugin: Tried to get module by name \"" + moduleName + "\". No module found.");
		return null;
	}
	
	/**
	 * Disables the plugin.
	 */
	protected void disable(){
		Bukkit.getPluginManager().disablePlugin(this);
	}
	
	/**
	 * Registers the given listeners and outputs it to console.
	 * 
	 * @param listeners -		Listeners to register
	 */
	protected void register(Listener... listeners){
		for(Listener l : listeners){
			Bukkit.getPluginManager().registerEvents(l, this);
			this.info("Registered listener: " + l.getClass().getName(), true);
		}
	}
	
	/**
	 * Unregisters this whole plugin.
	 */
	protected void unreg(){
		HandlerList.unregisterAll(this);
		this.info("Unregistered all listeners from plugin: " + this.getDescription().getName() + " v" + this.getDescription().getVersion(), true);
	}
	
	/**
	 * Unregisters specified listeners.
	 * 
	 * @param listeners -		Listeners to unregister
	 */
	protected void unreg(Listener... listeners){
		for(Listener l : listeners){
			HandlerList.unregisterAll(l);
			this.info("Unregistered listener: " + l.getClass().getName(), true);
		}
	}
	
	/**
	 * Loads the configuration file.
	 */
	protected void loadConfig(){
		this.saveDefaultConfig();
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
