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

import com.karuslabs.commons.commands.Command;
import com.karuslabs.commons.commands.CommandBuilder;

import java.util.*;
import org.bukkit.ChatColor;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;


public class Parser {
    
    private CommandBuilder builder;
    
    
    public Parser(CommandBuilder builder) {
        this.builder = builder;
    }
    
    
    public Map<String, Command> parse(String path) {
        return parse(YamlConfiguration.loadConfiguration(getClass().getClassLoader().getResourceAsStream(path)));
    }
    
    public Map<String, Command> parse(ConfigurationSection root) {
        return parseCommands(root, false);
    }
    
    protected Map<String, Command> parseCommands(ConfigurationSection root, boolean includeAliases) {
        Map<String, Command> commands = new HashMap<>(0);
        root.getKeys(false).stream().map(key -> parseCommand(root.getConfigurationSection(key)))
                .forEach(command -> {
                    commands.put(command.getName(), command);
                    if (includeAliases) command.getAliases().forEach(alias -> commands.put(alias, command));
                });
        
        return commands;
    }
    
    protected Command parseCommand(ConfigurationSection section) {        
        Command command =  builder.command(section.getName())
                .description(ChatColor.translateAlternateColorCodes('&', section.getString("description")))
                .aliases(section.getStringList("aliases"))
                .permission(section.getString("permission"))
                .message(ChatColor.translateAlternateColorCodes('&', section.getString("permission-message")))
                .usage(ChatColor.translateAlternateColorCodes('&', section.getString("usage"))).build();
        
        ConfigurationSection nested = section.getConfigurationSection("nested-commands");
        if (nested != null) {
            command.setNestedCommands(parseCommands(nested, true));
        }
        
        return command;
    }
    
}
