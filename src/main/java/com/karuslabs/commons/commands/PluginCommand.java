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

import java.util.List;

import org.bukkit.command.*;
import org.bukkit.plugin.Plugin;


/**
 * Represents a {@link com.karuslabs.commons.commands.Command} of a plugin.
 */
public abstract class PluginCommand extends Command implements PluginIdentifiableCommand {
    
    protected Plugin plugin;
    
    
    /**
     * Creates a new command with the name, plugin and criteria specified.
     * 
     * @param name the name of the command
     * @param plugin the owning plugin of the command
     * @param criteria the criteria which must be met for execution to proceed
     */
    public PluginCommand(String name, Plugin plugin, Criteria criteria) {
        super(name, criteria);
        this.plugin = plugin;
    }
    
    /**
     * Constructs this with the name, plugin, criteria, description, permission message and aliases specified.
     * 
     * @param name the name of the command
     * @param plugin the owning plugin of the command
     * @param criteria the criteria which must be met for execution to proceed
     * @param description the description of the command
     * @param message the permission message of the command
     * @param aliases the aliases of the command
     */
    public PluginCommand(String name, Plugin plugin, Criteria criteria, String description, String message, List<String> aliases) {
        super(name, criteria, description, message, aliases);
        this.plugin = plugin;
    }
    
    
    /**
     * @return the owning plugin of the command
     */
    @Override
    public Plugin getPlugin() {
        return plugin;
    }
    
}
