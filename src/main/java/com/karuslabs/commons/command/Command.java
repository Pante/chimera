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

import com.karuslabs.commons.command.completion.CommandCompleter;

import java.util.*;
import java.util.regex.Pattern;

import org.bukkit.command.*;
import org.bukkit.plugin.Plugin;

    
public class Command extends org.bukkit.command.Command implements PluginIdentifiableCommand {
    
    public static final Pattern PRESERVE_QUOTES = Pattern.compile(" (?=(([^'\"]*['\"]){2})*[^'\"]*$)");
    
    
    private Plugin plugin;
    private CommandExecutor executor;
    private Map<String, Command> subcommands;
    private Map<Integer, CommandCompleter> completers;
    
    
    public Command(Plugin plugin, String name, String description, String usage, List<String> aliases) {
        super(name, description, usage, aliases);
        this.plugin = plugin;
        this.executor = CommandExecutor.NONE;
        this.subcommands = new HashMap<>();
        this.completers = new HashMap<>();
    }

    
    @Override
    public boolean execute(CommandSender sender, String label, String[] args) {
        return execute(new CommandContext(sender, label, this), new Arguments(PRESERVE_QUOTES.split(String.join(" ", args))));
    }
    
    public boolean execute(CommandContext context, Arguments args) {
        Command subcommand;
        if (args.hasLength(0) && (subcommand = subcommands.get(args.get(0))) != null) {
            context.setCallingCommand(this);
            context.setCalleeCommand(subcommand);
            
            args.trim();
            
            return subcommand.execute(context, args);

        } else {
            return executor.execute(context, args);
        }
    }
    
    
    @Override
    public List<String> tabComplete(CommandSender sender, String alias, String[] args) {
        return tabComplete(new CommandContext(sender, alias, this), new Arguments(PRESERVE_QUOTES.split(String.join(" ", args))));
    }
    
    public List<String> tabComplete(CommandContext context, Arguments args) {
        Command subcommand;
        if (args.hasLength(2) && (subcommand = subcommands.get(args.get(0))) != null) {
            context.setCallingCommand(this);
            context.setCalleeCommand(subcommand);
            
            args.trim();
            
            return subcommand.tabComplete(context, args);
            
        } else {
            return null;
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
    
}
