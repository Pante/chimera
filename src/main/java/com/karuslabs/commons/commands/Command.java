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
    protected CommandExecutor executor;
    protected TabCompleter completer;
    
    
    public Command(String name, Plugin plugin) {
        this(name, plugin, NestedCommandExecutor.NONE, TabCompleter.INSTANCE);
    }
    
    public Command(String name, Plugin plugin, CommandExecutor executor, TabCompleter completer) {
        this(name, "", "", new ArrayList<>(0), plugin, new HashMap<>(0), executor, completer);
    }
    
    public Command(String name, String description, String usageMessage, List<String> aliases, Plugin plugin, Map<String, Command> commands, CommandExecutor executor, TabCompleter completer) {
        super(name, description, usageMessage, aliases);
        this.plugin = plugin;
        this.commands = commands;
        this.executor = executor;
        this.completer = completer;
    }
    
    
    @Override
    public boolean execute(CommandSender sender, String label, String[] args) {
        executor.onExecute(sender, this, label, args);
        return true;
    }
    
    @Override
    public List<String> tabComplete(CommandSender sender, String alias, String[] args) {
        return completer.complete(sender, this, alias, args);
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
    
    
    public CommandExecutor getExecutor() {
        return executor;
    }
    
    public void setExecutor(CommandExecutor executor) {
        this.executor = executor;
    }
    
    
    public TabCompleter getTabCompleter() {
        return completer;
    }
    
    public void setTabCompleter(TabCompleter completer) {
        this.completer = completer;
    }
    
}
