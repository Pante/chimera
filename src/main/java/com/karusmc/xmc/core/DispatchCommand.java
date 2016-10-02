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

import java.util.*;
import java.util.stream.Collectors;

import org.bukkit.command.*;
import org.bukkit.plugin.Plugin;

/**
 *
 * @author PanteLegacy @ karusmc.com
 */
public class DispatchCommand extends XMCommand {
    
    private Map<String, Command> commands;
    
    
    public DispatchCommand(Plugin owningPlugin, String name) {
        super(owningPlugin, name);
        commands = new HashMap<>();
    }
    
}
