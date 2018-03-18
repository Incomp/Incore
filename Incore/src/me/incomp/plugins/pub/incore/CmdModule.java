package me.incomp.plugins.pub.incore;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

import me.incomp.plugins.pub.incore.commands.Command;
import me.incomp.plugins.pub.incore.commands.CommandContext;
import me.incomp.plugins.pub.incore.commands.CommandException;
import me.incomp.plugins.pub.incore.commands.CommandPermissions;
import me.incomp.plugins.pub.incore.commands.NestedCommand;
import me.incomp.plugins.pub.incore.commons.StringUtil;
import me.incomp.plugins.pub.incore.messaging.FormatType;
import me.incomp.plugins.pub.incore.messaging.Formatter;
import me.incomp.plugins.pub.incore.modules.Module;

/**
 * Contains the <code>/module</code> command.
 * 
 * @author Incomp
 * @since Mar 10, 2018
 */
public class CmdModule {
		
	public static class CmdModuleParent {
		@Command(aliases = {"module", "modules", "m"}, desc = "Base command for Incore's modules.", min = 0, max = -1) // Will have to adjust max when I decide the syntax.
		@NestedCommand(CmdModule.class)
		public static void cmdModuleParent(final CommandContext cn, CommandSender sn) throws CommandException {}
	}
	
	@Command(aliases = {"list", "show", "view", "l"}, desc = "Used to list all modules.", min = 0, max = 0)
	@CommandPermissions("incore.modules.list")
	public static void cmdList(final CommandContext cn, CommandSender sn) throws CommandException {
		Set<Module> modules = Incore.getInstance().getModules();
		List<String> names = new ArrayList<String>();
		
		
		for(Module m : modules) {
			ChatColor color = (m.isEnabled()) ? ChatColor.DARK_GREEN : ChatColor.DARK_RED;
			names.add(color + m.getName());
		}
		
		String[] namesArray = names.toArray(new String[0]);
		String result = StringUtil.joinString(namesArray, ChatColor.WHITE + ", ");
		sn.sendMessage(Formatter.format("Modules", result, FormatType.NORMAL));
	}
	
	@Command(aliases = {"enable", "e"}, desc = "Used to enable and disable modules.", min = 1, max = -1)
	@CommandPermissions("incore.modules.manage")
	public static void cmdEnable(final CommandContext cn, CommandSender sn) throws CommandException {
		String moduleName = cn.getJoinedStrings(0);
		Module m = Incore.getInstance().getModule(moduleName);
		
		if(m == null) {
			sn.sendMessage(Formatter.format("Module", "No module found by &a\"" + moduleName + "\"&7.", FormatType.WARNING));
			return;
		}
		
		moduleName = m.getName();
		
		if(m.isEnabled()) {
			sn.sendMessage(Formatter.format("Module", "&a" + moduleName + " &7is already enabled.", FormatType.WARNING));
			return;
		}
		
		m.setEnabled(true);
		sn.sendMessage(Formatter.format("Module", "&a" + moduleName + " &7has been enabled.", FormatType.NORMAL));
	}
	
	@Command(aliases = {"disable", "d"}, desc = "Used to enable and disable modules.", min = 1, max = -1)
	@CommandPermissions("incore.modules.manage")
	public static void cmdDisable(final CommandContext cn, CommandSender sn) throws CommandException {
		String moduleName = cn.getJoinedStrings(0);
		Module m = Incore.getInstance().getModule(moduleName);
		
		if(m == null) {
			sn.sendMessage(Formatter.format("Module", "No module found by &a\"" + moduleName + "\"&7.", FormatType.WARNING));
			return;
		}
		
		moduleName = m.getName();
		
		if(!m.isEnabled()) {
			sn.sendMessage(Formatter.format("Module", "&a" + moduleName + " &7is already disabled.", FormatType.WARNING));
			return;
		}
		
		m.setEnabled(false);
		sn.sendMessage(Formatter.format("Module", "&a" + moduleName + " &7has been disabled.", FormatType.NORMAL));
	}
	
	@Command(aliases = {"reload", "r"}, desc = "Used to reload modules.", min = 1, max = -1)
	@CommandPermissions("incore.modules.manage")
	public static void cmdReloadModule(final CommandContext cn, CommandSender sn) throws CommandException {
		String moduleName = cn.getJoinedStrings(0);
		Module m = Incore.getInstance().getModule(moduleName);
		
		m.onUnload();
		m.onLoad();
		
		sn.sendMessage(Formatter.format("Module", "&a" + m.getName() + " &7has been disabled.", FormatType.NORMAL));
	}
}
