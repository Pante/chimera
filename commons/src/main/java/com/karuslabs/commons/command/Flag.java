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

import static java.util.stream.Collectors.toList;


@FunctionalInterface
public interface Flag extends CommandExecutor {    
    
    @Override
    public default boolean execute(CommandSource source, Context context, Arguments arguments) {
        Command command = context.getParentCommand();
        if (command == null) {
            source.sendColouredTranslation("Invalid command");
            
        } else if (!source.hasPermission(command.getPermission())) {
            source.sendMessage(command.getPermissionMessage());
            
        } else {
            execute(source, command, context);
        }
        
        return true;
    }
    
    public void execute(CommandSource source, Command parent, Context context);
            
    
    public static final Flag ALIASES = (source, parent, context) -> source.sendColouredTranslation("Aliases", parent.getAliases().toString());
    
    public static final Flag DESCRIPTION = (source, parent, context) -> source.sendColouredTranslation("Description", parent.getDescription(), parent.getUsage());
    
    public static final Flag HELP = (source, parent, context) -> {
        List<String> names = parent.getSubcommands().values().stream()
                                   .filter(subcommand -> source.hasPermission(subcommand.getPermission()))
                                   .map(Command::getName)
                                   .collect(toList());
        
        source.sendColouredTranslation("Help header", parent.getName());
        source.sendColouredTranslation("Description", parent.getDescription(), parent.getUsage());
        source.sendColouredTranslation("Subcommands", names.toString());
    };
    
}
