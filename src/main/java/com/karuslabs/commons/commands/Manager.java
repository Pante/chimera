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

import com.karuslabs.commons.commands.xml.*;
import com.karuslabs.commons.core.xml.SetterParser;

import java.io.File;
import java.util.Map;

import org.bukkit.plugin.Plugin;


/**
 * Represents a facade for accessing command-related functions.
 */
public class Manager {
    
    private CommandMapProxy proxy;
    private SetterParser<Map<String, Command>> parser;
    
    
    /**
     * Creates a new manager for the plug-in specified.
     * 
     * @param owningPlugin the owning plug-in of the manager
     */
    public Manager(Plugin owningPlugin) {
        this(new CommandMapProxy(owningPlugin.getServer()), new CommandParser(new CommandsComponent()));
    }
    
    /**
     * Creates a new manager for the plug-in with the <code>CommandMapProxy</code> and <code>Parser<code> specified
     * 
     * @param proxy a proxy
     * @param parser a parser
     */
    public Manager(CommandMapProxy proxy, SetterParser<Map<String, Command>> parser) {
        this.proxy = proxy;
        this.parser = parser;
    }

    
    /**
     * Loads and registers the commands from the file specified.
     * 
     * @param commandsFile the file to load the commands from
     * @param commands the commands to be registered
     */
    public void load(File commandsFile, Map<String, Command> commands) {
        parser.parse(commandsFile, commands);
        proxy.register(commands);
    }

    
    public CommandMapProxy getProxy() {
        return proxy;
    }


    public SetterParser<Map<String, Command>> getParser() {
        return parser;
    }

}
