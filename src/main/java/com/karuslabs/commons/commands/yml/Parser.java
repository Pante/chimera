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
package com.karuslabs.commons.commands.yml;

import com.karuslabs.commons.collections.Maps;
import com.karuslabs.commons.commands.*;

import java.util.*;
import java.util.stream.Collectors;

import org.bukkit.ChatColor;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.plugin.Plugin;


public class Parser {
    
    private Plugin plugin;
    private Map<String, Extension> extensions;
    
    
    public Parser(Plugin plugin) {
        this(
            plugin, 
            Maps.builder("aliases", Extension.ALIASSES).put("description", Extension.DESCRIPTION).put("help", Extension.HELP).build()
        );
    }
    
    public Parser(Plugin plugin, Map<String, Extension> extensions) {
        this.plugin = plugin;
        this.extensions = extensions;
    }
    
    
    public List<Command> parse(ConfigurationSection config) {
        return config.getKeys(false).stream().map(name -> parseCommand(config.getConfigurationSection(name))).collect(Collectors.toList());
    }
    
    protected Command parseCommand(ConfigurationSection config) {
        CommandBuilder builder = parseCommandInformation(config);
        
        ConfigurationSection commandsSection = config.getConfigurationSection("subcommands");
        if (commandsSection != null) {
            parse(commandsSection).forEach(builder::subcommand);
        }
        
        ConfigurationSection extensionsSection = config.getConfigurationSection("extensions");
        if (extensionsSection != null) {
            extensionsSection.getKeys(false).forEach(
                name -> builder.extension(name, extensions.getOrDefault(extensionsSection.getString(name).toLowerCase(), Extension.NONE))
            );
        }
        
        return builder.build();
    }
    
    protected CommandBuilder parseCommandInformation(ConfigurationSection config) {
        return new CommandBuilder(plugin).name(config.getName())
                .description(ChatColor.translateAlternateColorCodes('&', config.getString("description", "")))
                .aliases(config.getStringList("aliases"))
                .permission(config.getString("permission", ""))
                .message(ChatColor.translateAlternateColorCodes('&', config.getString("permission-message", "")))
                .usage(ChatColor.translateAlternateColorCodes('&', config.getString("usage", "")));
    }

    
    public Map<String, Extension> getExtensions() {
        return extensions;
    }
    
}
