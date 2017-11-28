/*
 * The MIT License
 *
 * Copyright 2017 Karus Labs.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package com.karuslabs.commons.command;

import com.karuslabs.commons.command.arguments.Arguments;

import org.bukkit.command.CommandSender;

import static org.bukkit.ChatColor.RED;


@FunctionalInterface
interface Parent extends CommandExecutor {    
    
    @Override
    public default boolean execute(Context context, Arguments arguments) {
        Command command = context.getParentCommand();
        CommandSender sender = context.getSender();
        
        if (command == null) {
            sender.sendMessage(RED + "Invalid command");
            
        } else if (!sender.hasPermission(command.getPermission())) {
            sender.sendMessage(RED + command.getPermissionMessage());
            
        } else {
            execute(command, sender);
        }
        
        return true;
    }
    
    void execute(Command parent, CommandSender sender);
    
}
