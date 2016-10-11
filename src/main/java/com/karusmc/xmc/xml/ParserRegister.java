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
package com.karusmc.xmc.xml;

import com.karusmc.xmc.core.*;

import java.util.*;

import org.bukkit.command.CommandSender;

/**
 *
 * @author PanteLegacy @ karusmc.com
 */
public class ParserRegister extends XMCommand implements Dispatcher {
    
    private Map<String, XMCommand> commands;
    
    
    public ParserRegister() {
        super(null, null);
        commands = new HashMap<>();
    }
    
    
    @Override
    public boolean execute(CommandSender sender, String commandLabel, String[] args) {
        throw new UnsupportedOperationException();
    }
    

    @Override
    public Map<String, XMCommand> getCommands() {
        return commands;
    }
    
    public void setCommands(Map<String, XMCommand> commands) {
        this.commands = commands;
    }
    
}
