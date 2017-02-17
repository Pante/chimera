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

import com.karuslabs.commons.commands.xml.CommandsComponent;
import com.karuslabs.commons.commands.xml.CommandParser;
import com.karuslabs.commons.core.xml.SetterParser;

import java.io.File;
import java.util.Map;

import org.bukkit.plugin.Plugin;


/**
 * Acts as a facade for registering and parsing commands from XML Documents.
 */
public class Manager {
    
    private CommandMapProxy proxy;
    private SetterParser<Map<String, Command>> parser;
    
    
    /**
     * Constructs this with the specified owning plug-in.
     * 
     * @param owningPlugin The owning plug-in of this CommandManager
     */
    public Manager(Plugin owningPlugin) {
        this(new CommandMapProxy(owningPlugin.getServer()), new CommandParser(new CommandsComponent()));
    }
    
    /**
     * Constructs this with the specified CommandMapProxy and Parser.
     * 
     * @param proxy A CommandMapProxy
     * @param parser A Parser
     */
    public Manager(CommandMapProxy proxy, SetterParser<Map<String, Command>> parser) {
        this.proxy = proxy;
        this.parser = parser;
    }

    
    /**
     * Parses command information from the file and registers the commands.
     * 
     * @param commandsFile The file containing the command information
     * @param commands The commands to be parsed and registered
     */
    public void load(File commandsFile, Map<String, Command> commands) {
        parser.parse(commandsFile, commands);
        proxy.register(commands);
    }

    
    /**
     * @return a CommandMapProxy
     */
    public CommandMapProxy getProxy() {
        return proxy;
    }

    
    /**
     * @return a SetterParser
     */
    public SetterParser<Map<String, Command>> getParser() {
        return parser;
    }

}
