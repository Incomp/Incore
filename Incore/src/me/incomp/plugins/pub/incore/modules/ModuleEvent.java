package me.incomp.plugins.pub.incore.modules;

import org.bukkit.event.Event;

/**
 * Base class for module-related events.
 * 
 * @author Incomp
 * @since Mar 22, 2018
 */
public abstract class ModuleEvent extends Event {

	private final Module module;
	
	public ModuleEvent(final Module module) {
		this.module = module;
	}
	
	/**
	 * Used to get the relevant module.
	 * 
	 * @return Module
	 */
	public Module getModule() {
		return this.module;
	}
}
