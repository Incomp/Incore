package me.incomp.plugins.pub.incore.modules.wait2cmd;

import me.incomp.plugins.pub.incore.IPlugin;
import me.incomp.plugins.pub.incore.modules.Module;

/**
 * A mini-plugin that implements cooldowns for commands.
 * 
 * @author Incomp
 * @since Mar 22, 2018
 */
public class Wait2CmdModule extends Module {

	public Wait2CmdModule(IPlugin plugin) {
		super(plugin, "Wait2Cmd", "Implements command cooldowns.");
	}

	@Override
	public void onLoad() {
		
	}

	@Override
	public void onUnload() {
		
	}
	
	@Override
	public void initConfig() {
		
	}
}
