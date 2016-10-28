/*
 * Copyright (C) 2016 PanteLegacy @ karusmc.com
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
package com.karusmc.xmc.core;

import com.karusmc.xmc.xml.*;

import java.io.File;
import java.util.*;

import org.bukkit.plugin.Plugin;

/**
 *
 * @author PanteLegacy @ karusmc.com
 */
public class CommandManager {
    
    private CommandMapProxy proxy;
    private Plugin owningPlugin;
    
    
    public CommandManager(Plugin owningPlugin) {
        this(owningPlugin, new CommandMapProxy(owningPlugin.getServer()));
    }
    
    public CommandManager(Plugin owningPlugin, CommandMapProxy proxy) {
        this.proxy = proxy;
        this.owningPlugin = owningPlugin;
    }
    
    
    public void loadCommands(File file) {
        load(ParserBuilder.createCommandParser(), file);
    }
    
    public void loadConfiguration(File file) {
        load(ParserBuilder.createConfigurationParser(), file);
    }
    
    private void load(Parser parser, File commandsFile) {
        parser.registerAll(proxy.getPluginCommands(owningPlugin.getName(), XMCommand.class));
        parser.parse(commandsFile);
    }
      
    
    public void loadAll(File commandFile, File configurationFile) {
        Map<String, XMCommand> commands = proxy.getPluginCommands(owningPlugin.getName(), XMCommand.class);
        
        Parser commandParser = ParserBuilder.createCommandParser();
        Parser configurationParser = ParserBuilder.createConfigurationParser();
        
        commandParser.registerAll(commands);
        configurationParser.registerAll(commands);
        
        commandParser.parse(commandFile);
        configurationParser.parse(configurationFile);
    }
    
    
    public void register(XMCommand command) {
        proxy.register(command.getPlugin().getName(), command);
    }
    
    
    public void registerAll(Map<String, XMCommand> commands) {
        proxy.registerAll(commands);
    }
    
    
    public void unregister(String name) {
        proxy.unregister(name);
    }
    
    public void unregisterAll() {
        proxy.clearCommands();
    }
    
    
    public CommandMapProxy getCommandMapProxy() {
        return proxy;
    }

}
