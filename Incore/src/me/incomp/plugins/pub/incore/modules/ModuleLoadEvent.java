package me.incomp.plugins.pub.incore.modules;

import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;

/**
 * Called when a Module is loaded.
 * 
 * @author Incomp
 * @since Mar 22, 2018
 */
public class ModuleLoadEvent extends ModuleEvent implements Cancellable {
	
	private static final HandlerList HANDLERS = new HandlerList();
	private boolean cancel = false;	
	
	public ModuleLoadEvent(final Module module) {
		super(module);
	}
	
	@Override
	public HandlerList getHandlers() {
		return HANDLERS;
	}
	
	public static HandlerList getHandlerList() {
		return HANDLERS;
	}

	@Override
	public boolean isCancelled() {
		return this.cancel;
	}

	@Override
	public void setCancelled(boolean cancel) {
		this.cancel = cancel;
	}
}
