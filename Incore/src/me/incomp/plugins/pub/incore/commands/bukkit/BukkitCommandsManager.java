package me.incomp.plugins.pub.incore.commands.bukkit;

import org.bukkit.command.CommandSender;

import me.incomp.plugins.pub.incore.commands.CommandsManager;

public class BukkitCommandsManager extends CommandsManager<CommandSender> {
    @Override
    public boolean hasPermission(CommandSender player, String perm) {
        return player.hasPermission(perm);
    }
}
