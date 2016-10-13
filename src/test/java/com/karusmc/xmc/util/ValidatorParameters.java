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
public class ValidatorParameters {
    
    private static ConfigurableCommand command;
    
    private static ConsoleCommandSender console;
    private static Player player;
    
    private static Set<String> worlds;
    
    
    static {
        command = mock(ConfigurableCommand.class);
        
        console = mock(ConsoleCommandSender.class);
        player = mock(Player.class);
        when(player.getWorld()).thenReturn(mock(World.class));
        
        worlds = mock(Set.class);
    }
    
    
    public static Object[] canUse_Parameter(boolean consoleAllowed, boolean isConsole, boolean hasPermission, boolean expected) {
        command = mock(ConfigurableCommand.class);
        
        when(command.isConsoleAllowed()).thenReturn(consoleAllowed);
        when(command.testPermissionSilent(any())).thenReturn(hasPermission);
        
        return new Object[] {command, getSender(isConsole), expected};
    }
    
    
    public static Object[] canUseInWorld_Parameter(boolean hasBlacklist, boolean isConsole, boolean contains, boolean expected) {
        command = mock(ConfigurableCommand.class);
        
        when(command.getWorlds()).thenReturn(worlds);
        when(command.hasBlacklist()).thenReturn(hasBlacklist);
        
        when(worlds.contains(any(String.class))).thenReturn(contains);
        
        return new Object[] {command, getSender(isConsole), expected};
    }
    
    
    private static CommandSender getSender(boolean isConsole) {
        if (isConsole) {
            return console;
        }
        
        return player;
    }
    
}
