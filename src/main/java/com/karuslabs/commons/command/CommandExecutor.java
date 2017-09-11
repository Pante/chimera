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

import java.util.List;
import java.util.function.BiConsumer;

import org.bukkit.command.CommandSender;

import static java.util.stream.Collectors.toList;
import static org.bukkit.ChatColor.*;


@FunctionalInterface
public interface CommandExecutor {

    public boolean execute(Context context, Arguments arguments);
    
    
    public static boolean wrap(Context context, Arguments arguments, BiConsumer<Command, CommandSender> executor) {
        Command command = context.getParentCommand();
        CommandSender sender = context.getSender();
        
        if (command != null && sender.hasPermission(command.getPermission())) {
            executor.accept(command, sender);
        }
        
        return true;
    }
    
    
    public static final CommandExecutor ALIASES = (context, arguments) -> wrap(context, arguments, 
        (command, sender) -> sender.sendMessage(GOLD + "Aliases: " + RED + command.getAliases().toString()
    ));
    
    public static final CommandExecutor DESCRIPTION = (context, arguments) -> wrap(context, arguments, 
        (command, sender) -> sender.sendMessage(GOLD  + "Description: " + RED + command.getDescription() + GOLD  +"\nUsage: " + RED + command.getUsage())
    );
    
    public static final CommandExecutor HELP = (context, arguments) -> wrap(context, arguments, 
        (command, sender) -> {
            List<String> names = command.getSubcommands().values().stream()
                    .filter(subcommand -> sender.hasPermission(subcommand.getPermission()))
                    .map(Command::getName)
                    .collect(toList());

            sender.sendMessage(GOLD + "==== Help for: " + command.getName() + " ====");
            sender.sendMessage(GOLD + "Description: " + RED + command.getDescription());
            sender.sendMessage(GOLD + "Usage: " + RED + command.getUsage());
            sender.sendMessage(GOLD + "\n==== Subcommands: ====" + "\n" + RED + names);
        }
    );
    
    public static final CommandExecutor NONE = (context, arguments) -> true;

}
