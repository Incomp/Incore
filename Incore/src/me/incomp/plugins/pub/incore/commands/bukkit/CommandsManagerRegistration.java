// $Id$
/*
 * WorldEdit
 * Copyright (C) 2011 sk89q <http://www.sk89q.com> and contributors
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 */

package me.incomp.plugins.pub.incore.commands.bukkit;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import org.bukkit.command.CommandExecutor;
import org.bukkit.plugin.Plugin;

import me.incomp.plugins.pub.incore.commands.Command;
import me.incomp.plugins.pub.incore.commands.CommandPermissions;
import me.incomp.plugins.pub.incore.commands.CommandsManager;

/**
 * @author zml2008
 */
public class CommandsManagerRegistration extends CommandRegistration {
    protected CommandsManager<?> commands;

    public CommandsManagerRegistration(Plugin plugin, CommandsManager<?> commands) {
        super(plugin);
        this.commands = commands;
    }

    public CommandsManagerRegistration(Plugin plugin, CommandExecutor executor, CommandsManager<?> commands) {
        super(plugin, executor);
        this.commands = commands;
    }

    public boolean register(Class<?> clazz) {
        return registerAll(commands.registerAndReturn(clazz));
    }

    public boolean registerAll(List<Command> registered) {
        List<CommandInfo> toRegister = new ArrayList<CommandInfo>();
        for (Command command : registered) {
            String[] permissions = null;
            Method cmdMethod = commands.getMethods().get(null).get(command.aliases()[0]);
            if (cmdMethod != null && cmdMethod.isAnnotationPresent(CommandPermissions.class)) {
                permissions = cmdMethod.getAnnotation(CommandPermissions.class).value();
            }

            toRegister.add(new CommandInfo(command.usage(), command.desc(), command.aliases(), commands, permissions));
        }

        return register(toRegister);
    }
}
