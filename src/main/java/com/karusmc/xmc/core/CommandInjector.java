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

import java.lang.reflect.*;

import org.bukkit.Server;
import org.bukkit.command.*;

/**
 *
 * @author PanteLegacy @ karusmc.com
 */
public class CommandInjector {
    
    private Server server;
    private CommandMap commandMap;
    private Field field;
    
    
    public CommandInjector(Server server) {
        try {
            this.server = server;
            
            field = server.getClass().getDeclaredField("commandMap");
            field.setAccessible(true);
            
            commandMap = (CommandMap) field.get(server);
            
        } catch (ReflectiveOperationException e) {
            throw new IllegalArgumentException("Server class doest not contain field: commandMap", e);
        }
    }
    
    
    public void setCommandMap(CommandMap map) {
        try {
            Field modifier = field.getClass().getDeclaredField("modifiers");
            modifier.setAccessible(true);
            modifier.setInt(field, field.getModifiers() & ~Modifier.FINAL);

            field.set(server, map);
            commandMap = map;
            
        } catch (ReflectiveOperationException e) {
            throw new IllegalArgumentException("Failed to inject commandmap implementation", e);
        }
    }
    
    
    public CommandMap getCommandMap() {
        return commandMap;
    }
    
}
