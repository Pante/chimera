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
import com.karuslabs.commons.configuration.Configurations;

import java.util.*;
import java.util.stream.Collectors;

import org.bukkit.ChatColor;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.plugin.Plugin;


public class Parser {
    
    private Plugin plugin;
    private Map<String, Option> options;
    
    
    public Parser(Plugin plugin) {
        this(
            plugin, 
            Maps.builder("aliases", Option.ALIASSES).put("description", Option.DESCRIPTION).put("help", Option.HELP).build()
        );
    }
    
    public Parser(Plugin plugin, Map<String, Option> options) {
        this.plugin = plugin;
        this.options = options;
    }
    
    
    public List<Command> parse(ConfigurationSection config) {
        return config.getKeys(false).stream().map(name -> parseCommand(config.getConfigurationSection(name))).collect(Collectors.toList());
    }
    
    protected Command parseCommand(ConfigurationSection config) {
        CommandBuilder builder = parseCommandInformation(config);
        
        parse(Configurations.getOrBlank(config.getConfigurationSection("subcommands"))).forEach(builder::subcommand);
        
        ConfigurationSection optionsSection = Configurations.getOrBlank(config.getConfigurationSection("options"));
        optionsSection.getKeys(false).forEach(name -> builder.option(name, options.getOrDefault(optionsSection.getString(name).toLowerCase(), Option.NONE)));
        
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

    
    public Map<String, Option> getOptions() {
        return options;
    }
    
}
