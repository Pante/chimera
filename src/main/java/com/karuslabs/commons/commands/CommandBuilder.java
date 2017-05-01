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
    
    private Command command;
    
    
    public CommandBuilder(Plugin plugin) {
        command = new Command("", plugin, CommandExecutor.NONE, TabCompleter.PLAYER_NAMES);
    }
    
    
    public CommandBuilder name(String name) {
        command.setName(name);
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
    
    public CommandBuilder command(Command command) {
        command.getAliases().forEach(alias -> this.command.getNestedCommands().put(alias, command));
        this.command.getNestedCommands().put(command.getName(), command);
        this.command.getNestedNames().add(command.getName());
        
        return this;
    }
    
    public CommandBuilder extension(String name, Extension extension) {
        command.getExtensions().put(name, extension);
        command.getNestedNames().add(name);
        
        return this;
    }
    
    
    public CommandBuilder executor(CommandExecutor executor) {
        command.setExecutor(executor);
        return this;
    }
    
    public CommandBuilder completer(TabCompleter completer) {
        command.setTabCompleter(completer);
        return this;
    }
    
    
    public Command build() {
        return command;
    }
    
}
