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

import org.bukkit.Server;

/**
 *
 * @author PanteLegacy @ karusmc.com
 */
public class CommandManager {
    
    private CommandMapProxy proxy;
    
    private Parser commandsParser; 
    private Parser configurationParser;
    
    
    public CommandManager(Server server) {
        proxy = new CommandMapProxy(server);
        commandsParser = ParserBuilder.buildCommandsParser("dtd/commands.dtd");
        configurationParser = ParserBuilder.buildConfigurationParser("dtd/configuration.dtd");
    }
    
    public CommandManager(CommandMapProxy proxy) {
        this.proxy = proxy;
        commandsParser = ParserBuilder.buildCommandsParser("dtd/commands.dtd");
        configurationParser = ParserBuilder.buildConfigurationParser("dtd/configuration.dtd");
    }
    
    
    public void loadCommands(File commandsFile, File configurationFile) {
        commandsParser.parse(commandsFile);
        configurationParser.parse(configurationFile);
    }
    
    
    public void register(XMCommand command) {
        proxy.register(command.getPlugin().getName(), command);
        
        commandsParser.register(command);
        configurationParser.register(command);
    }
    
    
    public void registerAll(Map<String, XMCommand> commands) {
        commands.values().forEach(command -> proxy.register(command.getPlugin().getName(), command));
        
        commandsParser.registerAll(commands);
        configurationParser.registerAll(commands);
    }
    
    
    public void unregister(XMCommand command) {
        proxy.unregister(command.getName());
        
        commandsParser.unregister(command);
        configurationParser.unregister(command);
    }
    
    public void unregisterAll() {
        proxy.clearCommands();
        
        commandsParser.unregisterAll();
        configurationParser.unregisterAll();
    }

}
