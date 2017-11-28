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

import javax.annotation.Nonnull;

import java.util.List;

import static java.util.stream.Collectors.toList;
import static org.bukkit.ChatColor.*;


/**
 * Represents an executor which executes {@code Command}s.
 */
@FunctionalInterface
public interface CommandExecutor {
    
    /**
     * Executes the {@code Command} in the specified {@code Context} with the specified {@code Arguments}.
     * 
     * @param context the context which the command is executed in
     * @param arguments the arguments passed to the command
     * @return true if the execution was successful; else false
     */
    public boolean execute(@Nonnull Context context, @Nonnull Arguments arguments);
    

    /**
     * A {@code CommandExeuctor} which displays the aliases of the parent {@code Command}
     * if the {@code CommandSender} has the necessary permissions and the parent {@code Command} exists.
     */
    public static final CommandExecutor ALIASES = 
            (Parent) (command, sender) -> sender.sendMessage(GOLD + "Aliases: " + RED + command.getAliases().toString());
    
    /**
     * A {@code CommandExecutor} which displays the description of the parent {@code Command}
     * if the {@code CommandSender} has the necessary permissions and the parent {@code Command} exists.
     */
    public static final CommandExecutor DESCRIPTION = 
            (Parent) (command, sender) -> sender.sendMessage(GOLD  + "Description: " + RED + command.getDescription() + GOLD  +"\nUsage: " + RED + command.getUsage());
    /**
     * A {@code CommandExecutor} which displays the subcommands of the parent {@code Command} which the
     * {@code CommandSender} has the necessary permissions for, if the {@code CommandSender} has the necessary permission
     * for the parent {@code Command} and the parent {@code Command} exists.
     */
    public static final CommandExecutor HELP = (Parent) (command, sender) -> {
        List<String> names = command.getSubcommands().values().stream()
                .filter(subcommand -> sender.hasPermission(subcommand.getPermission()))
                .map(Command::getName)
                .collect(toList());

        sender.sendMessage(new String[] {
            GOLD + "==== Help for: " + command.getName() + " ====",
            GOLD + "Description: " + RED + command.getDescription(),
            GOLD + "Usage: " + RED + command.getUsage(),
            GOLD + "\n==== Subcommands: ====" + "\n" + RED + names
        });
    };
    
    /**
     * A {@code CommandExecutor} which displays a message to the {@code CommandSender} 
     * and returns {@code true} on executed.
     */
    public static final CommandExecutor NONE = (context, arguments) -> {
        context.getSender().sendMessage(RED + "No behaviour for this command.");
        return true;
    };

}
