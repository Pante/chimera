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

import com.karuslabs.commons.commands.*;

import java.util.List;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.Plugin;


public class Parser {
    
    private Plugin plugin;
    private FileConfiguration config;
    private CommandBuilder builder;
    
    
    public Parser(Plugin plugin, FileConfiguration config, CommandBuilder builder) {
        this.plugin = plugin;
        this.config = config;
        this.builder = builder;
    }
    
    
    public List<Command> parse() {
        for (String command : config.getKeys(false)) {
            
        }
    }
    
    protected Command parse(String path) {
        return builder.newCommand(config.getString(path + ".name", ""))
                .description(config.getString(path + ".description", ""))
                .aliases(aliases)
                .permission(config.getString(path + ".permission", ""))
                .message(config.getString(path + ".message", ""))
                .usage(config.getString(path + ".usage", "")).get();
    }
    
}
