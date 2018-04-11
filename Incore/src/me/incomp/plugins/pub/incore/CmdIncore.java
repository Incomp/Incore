package me.incomp.plugins.pub.incore;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import me.incomp.plugins.pub.incore.commands.Command;
import me.incomp.plugins.pub.incore.commands.CommandContext;
import me.incomp.plugins.pub.incore.commands.CommandException;
import me.incomp.plugins.pub.incore.commands.CommandPermissions;
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
	@CommandPermissions("incore.reload")
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
	@CommandPermissions("incore.perms")
	public static void cmdPerm(final CommandContext cn, CommandSender sn) throws CommandException {
		if(cn.argsLength() > 0) { // Player is trying to check the permissions of someone else
			if(Perm.PERMS_OTHERS.has(sn)) {
				String targetName = cn.getString(0);
				Player target = Bukkit.getPlayer(targetName);
				
				List<String> output = new ArrayList<>();
				
				for(Perm p : Perm.values()) {
					StringBuilder sb = new StringBuilder();
					if(p.has(target)) {
						sb.append(ChatColor.GREEN + "" + ChatColor.BOLD + "✔ " + ChatColor.GRAY);
					} else sb.append(ChatColor.RED + "" + ChatColor.BOLD + "✘ " + ChatColor.GRAY);
					
					sb.append(p.getNode() + " (" + p.getDescription() + ")");
					output.add(sb.toString());
				}
				
				for(String s : output) {
					sn.sendMessage(s);
				}
				
				if(target == null || !target.isOnline()) { // Target player not found
					String msg = MsgPreset.NO_PLAYER_FOUND.get().replace("%target%", targetName);
					sn.sendMessage(msg);
					return;
				}
			} else {
				MsgPreset.NO_ACCESS.send(sn);
				return;
			}
		} else {
			List<String> output = new ArrayList<>();
			
			for(Perm p : Perm.values()) {
				StringBuilder sb = new StringBuilder();
				if(p.has(sn)) {
					sb.append(ChatColor.GREEN + "" + ChatColor.BOLD + "✔ " + ChatColor.GRAY);
				} else sb.append(ChatColor.RED + "" + ChatColor.BOLD + "✘ " + ChatColor.GRAY);
				
				sb.append(p.getNode() + " (" + p.getDescription() + ")");
				output.add(sb.toString());
			}
			
			for(String s : output) {
				sn.sendMessage(s);
			}
		}
		// TODO: This shit
	}
}
