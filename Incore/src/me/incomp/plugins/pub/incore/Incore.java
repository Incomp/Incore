package me.incomp.plugins.pub.incore;

import me.incomp.plugins.pub.incore.CmdIncore.CmdIncoreParent;
import me.incomp.plugins.pub.incore.CmdModule.CmdModuleParent;
import me.incomp.plugins.pub.incore.modules.headdrop.HeadDropModule;
import me.incomp.plugins.pub.incore.modules.itemban.ItemBanModule;

public class Incore extends IPlugin {
	
	@Override
	public void onEnable() {
		INSTANCE = this;
		
		this.info(" === INCORE === ");
		this.info("Hey there. I'm Incore.");
		
		this.info("Loading config...");
		this.loadConfig();
		this.info("Done.");
		
		this.info("Registering commands...");
		this.initCommands();
		this.registerCommands();
		this.info("Done.");
		
		this.info("Adding modules...");
		// DEV: Add more modules here as you see fit.
		this.addModule(new HeadDropModule(this));
		this.addModule(new ItemBanModule(this));
		this.info("Done.");
		
		this.info(" === INCORE === ");
	}

	@Override
	public void onDisable() {
		this.info(" === INCORE === ");
		this.info("It's time to move on.");
		
		this.info("Unregistering listeners...");
		this.unreg();
		this.info("Done.");
		
		this.info("Unregistering commands...");
		this.cmdReg.unregisterCommands();
		this.info("Done.");
		
		this.info("12 btw haHAA.");
		INSTANCE = null;
		this.info(" === INCORE === ");
	}

	@Override
	public void registerCommands() {
		this.cmdReg.register(CmdIncoreParent.class);
		this.cmdReg.register(CmdModuleParent.class);
	}
}
