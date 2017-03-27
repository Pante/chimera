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
package com.karuslabs.commons.commands.executors;

import com.karuslabs.commons.commands.Command;
import com.karuslabs.commons.commands.extensions.Extension;

import java.util.*;

import org.bukkit.command.CommandSender;


public class CommandExtensionExecutor implements CommandExecutor {

    private CommandExecutor executor;
    private Map<String, Extension>  extensions;
    
    
    public CommandExtensionExecutor(CommandExecutor executor) {
        this(executor, new HashMap<>());
    }
    
    public CommandExtensionExecutor(CommandExecutor executor, Map<String, Extension> extensions) {
        this.executor = executor;
        this.extensions = extensions;
    }
    
    
    @Override
    public void execute(CommandSender sender, Command command, String label, String[] args) {
        if (args.length >= 1 && extensions.containsKey(args[0])) {
            extensions.get(args[0]).execute(sender, command, label, args);
            
        } else {
           executor.execute(sender, command, label, args);
        }
    }
    
    
    public Map<String, Extension> getExtensions() {
        return extensions;
    }
    
    public void setExtensions(Map<String, Extension> extensions) {
        this.extensions = extensions;
    }
    
}
