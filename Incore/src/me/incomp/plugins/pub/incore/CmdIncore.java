package me.incomp.plugins.pub.incore;

import org.bukkit.command.CommandSender;

import me.incomp.plugins.pub.incore.commands.Command;
import me.incomp.plugins.pub.incore.commands.CommandContext;
import me.incomp.plugins.pub.incore.commands.CommandException;
import me.incomp.plugins.pub.incore.commands.NestedCommand;
import me.incomp.plugins.pub.incore.messaging.FormatType;
import me.incomp.plugins.pub.incore.messaging.Formatter;
import me.incomp.plugins.pub.incore.messaging.MsgPreset;

/**
 * <short description of class>
 * 
 * @author Incomp
 * @since Mar 10, 2018
 */
public class CmdIncore {
	

	public static class CmdIncoreParent {
		// Base command.
		@Command(aliases = {"incore"}, desc = "Base command for Incore.", min = 0, max = -1, usage = "<help|access|reload|modules>") // Will have to adjust max when I decide the syntax.
		@NestedCommand(CmdIncore.class)
		public static void cmdMain(final CommandContext cn, CommandSender sn) throws CommandException {}
	}
	
	// Base command for modules.
	/*
	@Command(aliases = {"module", "modules", "m"}, desc = "Base command for Incore's modules.", min = 0, max = -1) // Will have to adjust max when I decide the syntax.
	@NestedCommand(CmdModule.class)
	public void cmdModuleParent(final CommandContext cn, CommandSender sn) throws CommandException {}
	*/
	
	// Reload command.
	@Command(aliases = {"reload", "r"}, desc = "Reloads Incore.", min = 0, max = 0)
	public static void cmdReload(final CommandContext cn, CommandSender sn) throws CommandException {
		if(Perm.RELOAD.has(sn)) {
			Incore.getInstance().reloadConfig();
			if(Incore.getInstance() == null) {
				Formatter.debug("why is the instance null LOL");
			}
			sn.sendMessage(Formatter.format("Incore", "Successfully reloaded and ready to bust some nuts.", FormatType.NORMAL));
			return;
			
		} else {
			MsgPreset.NO_ACCESS.send(sn);
			return;
		}
	}
	
	// Permission check command
	@Command(aliases = {"perm", "perms", "access"}, desc = "Lists all of Incore's permission nodes, their descriptions, and whether or not you have them.", min = 0, max = 1)
	public static void cmdPerm(final CommandContext cn, CommandSender sn) throws CommandException {
		// TODO: This shit
	}
}
