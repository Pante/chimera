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

import org.bukkit.command.CommandSender;
import org.bukkit.command.PluginIdentifiableCommand;
import org.bukkit.plugin.Plugin;


public class Command extends org.bukkit.command.Command implements PluginIdentifiableCommand {
    
    protected Plugin plugin;
    protected Map<String, Command> commands;
    protected CommandCallable command;
    protected TabCallable tab;
    
    
    public Command(String name, Plugin plugin, CommandCallable commandCallable, TabCallable tabCallable) {
        this(name, "", "", new ArrayList<>(0), plugin, new HashMap<>(0), commandCallable, tabCallable);
    }
    
    public Command(String name, String description, String usageMessage, List<String> aliases, Plugin plugin, Map<String, Command> nestedCommands, CommandCallable commandCallable, TabCallable tabCallable) {
        super(name, description, usageMessage, aliases);
        this.plugin = plugin;
        commands = nestedCommands;
        command = commandCallable;
        tab = tabCallable;
    }
    
    
    @Override
    public boolean execute(CommandSender sender, String label, String[] args) {
        command.onExecute(sender, this, label, args);
        return true;
    }
    
    @Override
    public List<String> tabComplete(CommandSender sender, String alias, String[] args) {
        return tab.complete(sender, this, alias, args);
    }
    
    
    @Override
    public Plugin getPlugin() {
        return plugin;
    }
    
    public Map<String, Command> getNestedCommands() {
        return commands;
    }
    
    public void setNestedCommands(Map<String, Command> commands) {
        this.commands = commands;
    }
    
    
    public CommandCallable getCommandCallable() {
        return command;
    }
    
    public void setCommandCallable(CommandCallable callable) {
        command = callable;
    }
    
    
    public TabCallable getTabCallable() {
        return tab;
    }
    
    public void setTabCallable(TabCallable callable) {
        tab = callable;
    }
    
}
