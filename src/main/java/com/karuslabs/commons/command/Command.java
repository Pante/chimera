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
import com.karuslabs.commons.command.completion.Completion;
import com.karuslabs.commons.locale.MessageTranslation;

import java.util.*;
import javax.annotation.Nonnull;

import org.bukkit.command.*;
import org.bukkit.plugin.Plugin;

import static com.karuslabs.commons.command.Patterns.preserveQuotes;
import static java.util.Collections.EMPTY_LIST;
import static java.util.stream.Collectors.toList;


public class Command extends org.bukkit.command.Command implements PluginIdentifiableCommand {    
    
    private Plugin plugin;
    private MessageTranslation translation;
    private CommandExecutor executor;
    private Map<String, Command> subcommands;
    private Map<Integer, Completion> completions;
    
    
    public Command(String name, Plugin plugin) {
        this(name, "", "", new ArrayList<>(), plugin, CommandExecutor.NONE);
    }
    
    public Command(String name, String description, String usage, List<String> aliases, Plugin plugin, CommandExecutor executor) {
        this(name, description, usage, aliases, plugin, executor, MessageTranslation.NONE, new HashMap<>(), new HashMap<>());
    }
    
    public Command(String name, String description, String usage, List<String> aliases, Plugin plugin, CommandExecutor executor, MessageTranslation translation, Map<String, Command> subcommands, Map<Integer, Completion> completions) {
        super(name, description, usage, aliases);
        this.plugin = plugin;
        this.executor = executor;
        this.translation = translation;
        this.subcommands = subcommands;
        this.completions = completions;
    }


    @Override
    public boolean execute(CommandSender sender, String label, String[] args) {
        return execute(new Context(sender, label, null, this), new Arguments(preserveQuotes(args)));
    }
    
    public boolean execute(Context context, Arguments arguments) {
        String argument = arguments.getLast().text();
        Command subcommand = subcommands.get(argument);
        
        if (subcommand != null) {
            arguments.trim();
            context.update(argument, subcommand);
            return subcommand.execute(context, arguments);
            
        } else {
            return executor.execute(context, arguments);
        }
    }
    
    
    @Override
    public @Nonnull List<String> tabComplete(CommandSender sender, String alias, String[] args) {
        return complete(sender, new Arguments(preserveQuotes(args)));
    }
    
    public @Nonnull List<String> complete(CommandSender sender, Arguments arguments) {
        if (arguments.length() == 0) {
            return EMPTY_LIST;
        } 
        
        String argument = arguments.get()[0];
        if (subcommands.containsKey(argument)) {
            arguments.trim();
            return subcommands.get(argument).complete(sender, arguments);
            
        } else if (arguments.length() == 1 && !subcommands.isEmpty()) {
            return subcommands.values().stream()
                    .filter(command -> sender.hasPermission(command.getPermission()) && command.getName().startsWith(argument))
                    .map(Command::getName)
                    .collect(toList());
        } else {
            return completions.getOrDefault(arguments.length() - 1, Completion.PLAYER_NAMES).complete(sender, arguments.getLast().text());
        }
    }
       

    @Override
    public Plugin getPlugin() {
        return plugin;
    }
    
    public CommandExecutor getExecutor() {
        return executor;
    }
    
    public void setExecutor(CommandExecutor executor) {
        this.executor = executor;
    }
        
    public MessageTranslation getTranslation() {
        return translation;
    }
    
    public Map<String, Command> getSubcommands() {
        return subcommands;
    }

    public Map<Integer, Completion> getCompletions() {
        return completions;
    }
    
}
