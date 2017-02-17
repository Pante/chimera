/*
 * Copyright (C) 2017 PanteLegacy @ karusmc.com
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
 * Represents a {@link com.karusmc.commons.commands.Command} belonging to a plug-in
 */
public abstract class PluginCommand extends Command implements PluginIdentifiableCommand {
    
    protected Plugin plugin;
    
    
    /**
     * Constructs this with the specified name, plug-in and criteria.
     * 
     * @param name The name of this command
     * @param plugin The owning plug-in of this command
     * @param criteria The execution criteria
     */
    public PluginCommand(String name, Plugin plugin, Criteria criteria) {
        super(name, criteria);
        this.plugin = plugin;
    }
    
    /**
     * Constructs this with the specified name, plug-in, criteria, description, message and aliases.
     * 
     * @param name The name of this command
     * @param plugin The owning plug-in of this command
     * @param criteria The execution criteria
     * @param description The description of this command
     * @param message The permission message of this command
     * @param aliases The aliases of this command
     */
    public PluginCommand(String name, Plugin plugin, Criteria criteria, String description, String message, List<String> aliases) {
        super(name, criteria, description, message, aliases);
        this.plugin = plugin;
    }
    
    
    /**
     * @return The owning plug-in of this command
     */
    @Override
    public Plugin getPlugin() {
        return plugin;
    }
    
}
