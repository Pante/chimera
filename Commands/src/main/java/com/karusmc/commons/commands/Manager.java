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
package com.karusmc.commons.commands;

import com.karusmc.commons.commands.xml.*;
import com.karusmc.commons.core.xml.SetterParser;

import java.io.File;
import java.util.Map;

import org.bukkit.plugin.Plugin;


public class Manager {
    
    private CommandMapProxy proxy;
    private SetterParser<Map<String, Command>> parser;
    
    
    public Manager(Plugin owningPlugin) {
        this(new CommandMapProxy(owningPlugin.getServer()), new CommandParser(new CommandsComponent()));
    }
    
    public Manager(CommandMapProxy proxy, SetterParser<Map<String, Command>> parser) {
        this.proxy = proxy;
        this.parser = parser;
    }

    
    public void load(File commandsFile, Map<String, Command> commands) {
        parser.parse(commandsFile, commands);
        proxy.register(commands);
    }

    
    public CommandMapProxy getProxy() {
        return proxy;
    }

    public void setProxy(CommandMapProxy proxy) {
        this.proxy = proxy;
    }

    public SetterParser<Map<String, Command>> getParser() {
        return parser;
    }

    public void setParser(SetterParser<Map<String, Command>> parser) {
        this.parser = parser;
    }
    
}
