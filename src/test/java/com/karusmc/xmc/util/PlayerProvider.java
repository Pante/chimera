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
package com.karusmc.xmc.util;

import com.karusmc.xmc.core.*;

import java.util.Set;

import org.bukkit.World;
import org.bukkit.command.*;
import org.bukkit.entity.Player;

import static org.mockito.Mockito.*;

/**
 *
 * @author PanteLegacy @ karusmc.com
 */
public class PlayerProvider {
    private static ConsoleCommandSender console;
    private static Player player;
    
    private static Set<String> worlds;
    private static World world;
    
    
    static { 
        console = mock(ConsoleCommandSender.class);
        player = mock(Player.class);
        world = mock(World.class);
        
        when(player.getWorld()).thenReturn(world);
        
        worlds = mock(Set.class);
    }
    
    
    public static Object[] provideParameters() {
        return new Object[] {
            parameters(true, true, true, true),
            parameters(true, false, true, false),
            
            parameters(true, true, false, false),
            parameters(true, false, false, false),
            
            parameters(false, true, true, false),
            parameters(false, false, true, true),
            
            parameters(false, true, false, false),
            parameters(false, false, false, false)
        };
    }
    
    
    public static Object[] parameters(boolean consoleAllowed, boolean isConsole, boolean hasPermission, boolean expected) {
        ConfigurableCommand command = mock(ConfigurableCommand.class);
        
        when(command.isConsoleAllowed()).thenReturn(consoleAllowed);
        when(command.testPermissionSilent(any())).thenReturn(hasPermission);
        
        return new Object[] {command, getSender(isConsole), expected};
    }
    
    
    private static CommandSender getSender(boolean isConsole) {
        if (isConsole) {
            return console;
        }
        
        return player;
    }
    
}
