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
package com.karuslabs.commons.commands.reference;

import com.karuslabs.commons.commands.*;

import java.util.*;
import java.util.stream.Collectors;

import org.bukkit.command.CommandSender;
import org.bukkit.plugin.Plugin;

import static com.karuslabs.commons.commands.Utility.trim;


/**
 * Represents a <code>Command</code> with subcommands.
 */
public class MarshallCommand extends PluginCommand implements Marshall {
    
    private Map<String, Command> subcommands;
      
    
    /**
     * Creates a new command with the name and owning plugin specified.
     * 
     * @param name the name
     * @param plugin the owning plugin
     */
    public MarshallCommand(String name, Plugin plugin) {
        this(name, plugin, Criteria.NONE);
    }
    
    /**
     * Creates a new command with the name, owning plugin and criteria specified.
     * 
     * @param name the name of the command
     * @param plugin the owning plugin of the command
     * @param criteria the criteria which must be met for execution to proceed
     */
    public MarshallCommand(String name, Plugin plugin, Criteria criteria) {
        this(name, plugin, criteria, new HashMap<>());
    }
    
    /**
     * Creates a new command with the name, owning plugin, criteria and subcommands specified.
     * 
     * @param name the name of the command
     * @param plugin the owning plugin of the command
     * @param criteria the criteria which must be met for execution to proceed
     * @param commands the subcommands
     */
    public MarshallCommand(String name, Plugin plugin, Criteria criteria, Map<String, Command> commands) {
        super(name, plugin, criteria);
        this.subcommands = commands;
    }
    
    
    /**
     * Executes {@link #execute(org.bukkit.command.CommandSender)} if there are no subcommands associated with the first argument; else the subcommand.
     * 
     * @param sender source object which is executing the command
     * @param args all arguments passed to the command, split via ' '
     */
    @Override
    public void execute(CommandSender sender, String[] args) {
        if (args.length >= 1 && subcommands.containsKey(args[0])) {
            subcommands.get(args[0]).execute(sender, trim(args));
            
        } else {
           execute(sender);
        }
    }
    
    /**
     * Does nothing. Executed by {@link #execute(org.bukkit.command.CommandSender, java.lang.String[])}, if there are no subcommands associated with the first argument.
     * 
     * @param sender source object which is executing the command
     */
    public void execute(CommandSender sender) {}
    
   
    /**
     * Returns the command's aliases if there are no arguments; else delegate tab completion to the subcommands.
     * 
     * @param sender source object which is executing the command
     * @param alias the alias used
     * @param args all arguments passed to the command, split via ' '
     * @return a list of tab-completions which will never be null
     */
    @Override
    public List<String> tabComplete(CommandSender sender, String alias, String[] args) {
        String argument;
        if (args.length == 1) {
            return subcommands.keySet().stream().filter(command -> command.startsWith(args[0])).collect(Collectors.toList());
            
        } else if (args.length >= 2 && subcommands.containsKey(argument = args[0])) {
            return subcommands.get(argument).tabComplete(sender, argument, trim(args));
            
        } else {
            return getAliases();
        }
    }
    
    
    @Override
    public Map<String, Command> getSubcommands() {
        return subcommands;
    }
    
}
