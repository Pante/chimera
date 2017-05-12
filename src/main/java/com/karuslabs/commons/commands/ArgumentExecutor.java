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

import java.util.*;

import org.bukkit.command.CommandSender;


public abstract class ArgumentExecutor implements CommandExecutor {
    
    protected Argument[] arguments;
    protected Argument defaultArgument;

    
    public ArgumentExecutor(int size, Argument defaultArgument) {
        this(new Argument[size], defaultArgument);
    }
    
    public ArgumentExecutor(Argument[] arguments, Argument defaultArgument) {
        this.arguments = arguments;
        this.defaultArgument = defaultArgument;
    }
    
    
    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        int lastIndex = args.length - 1;    
        Argument argument;
        
        if (lastIndex < 0) {
            return Collections.EMPTY_LIST;
                    
        } else if (lastIndex < arguments.length && (argument = arguments[lastIndex]) != null) {
            return argument.complete(sender, command, args[lastIndex]);
            
        } else {
            return defaultArgument.complete(sender, command, args[lastIndex]);
        }
    }

    
    public Argument[] getArguments() {
        return arguments;
    }
    
    public void setArguments(Argument[] arguments) {
        this.arguments = arguments;
    }
    

    public Argument getDefaultArgument() {
        return defaultArgument;
    }
    
}
