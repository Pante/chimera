/*
 * Copyright (C) 2017 Karus Labs
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.karuslabs.commons.commands;

import java.util.*;

import org.bukkit.plugin.Plugin;


public class CommandBuilder {
    
    private Plugin plugin;
    private Command command;
    
    
    public CommandBuilder(Plugin plugin) {
        this.plugin = plugin;
        command = null;
    }
    
    
    public CommandBuilder newCommand(String name) {
        command = new Command(name, plugin, NestedCommandCallable.NONE, TabCallable.INSTANCE);
        return this;
    }
    
    
    public CommandBuilder description(String description) {
        command.setDescription(description);
        return this;
    }
    
    public CommandBuilder aliases(List<String> aliases) {
        command.setAliases(aliases);
        return this;
    }
    
    public CommandBuilder permission(String permission) {
        command.setPermission(permission);
        return this;
    }
    
    public CommandBuilder message(String message) {
        command.setPermissionMessage(message);
        return this;
    }
    
    public CommandBuilder usage(String usage) {
        command.setUsage(usage);
        return this;
    }
    
    public CommandBuilder label(String label) {
        command.setLabel(label);
        return this;
    }
    
    public CommandBuilder nestedCommands(Map<String, Command> commands) {
        command.setNestedCommands(commands);
        return this;
    }
    
    public CommandBuilder nestedCommand(String name, Command command) {
        this.command.getNestedCommands().put(name, command);
        return this;
    }
    
    public CommandBuilder commandCallable(CommandCallable callable) {
        command.setCommandCallable(callable);
        return this;
    }
    
    public CommandBuilder tabCallable(TabCallable callable) {
        command.setTabCallable(callable);
        return this;
    }
    
    
    public Command get() {
        return command;
    }
    
}
