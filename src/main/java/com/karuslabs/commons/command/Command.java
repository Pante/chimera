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
import com.karuslabs.commons.locale.*;

import java.util.*;
import javax.annotation.Nonnull;

import org.bukkit.command.*;
import org.bukkit.plugin.Plugin;

import static com.karuslabs.commons.command.Patterns.preserveQuotes;
import static com.karuslabs.commons.command.completion.Completion.PLAYER_NAMES;
import static java.util.Collections.EMPTY_LIST;
import static java.util.stream.Collectors.toList;


/**
 * Represents a {@code Command} for a {@code Plugin}, which either delegates execution upon user input
 * to the {@code CommandExecutor} for this {@code Command}, or a subcommand of this {@code Command}.
 */
public class Command extends org.bukkit.command.Command implements PluginIdentifiableCommand, Translatable {    
    
    private Plugin plugin;
    private MessageTranslation translation;
    private CommandExecutor executor;
    private Map<String, Command> subcommands;
    private Map<Integer, Completion> completions;
    
    
    /**
     * Constructs a {@code Command} for the specified {@code Plugin} with the specified name.
     * 
     * @param name the name
     * @param plugin the Plugin
     */
    public Command(String name, Plugin plugin) {
        this(name, plugin, "", "", new ArrayList<>(), CommandExecutor.NONE);
    }
    
    /**
     * Constructs a {@code Command} for the specified {@code Plugin} with the specified 
     * name, description, usage, aliases and {@code CommandExeucutor}.
     * 
     * @param name the name
     * @param plugin the Plugin
     * @param description the description
     * @param usage the usage
     * @param aliases the aliases
     * @param executor the CommandExecutor
     */
    public Command(String name, Plugin plugin, String description, String usage, List<String> aliases, CommandExecutor executor) {
        this(name, plugin, MessageTranslation.NONE, description, usage, aliases, executor, new HashMap<>(), new HashMap<>());
    }
    
    /**
     * Constructs a {@code Command} for the specified {@code Plugin} with the specified name, {@code MessageTranslation}, 
     * description, usage, aliases, {@code CommandExeuctor}, subcommands, {@code Completion}s.
     * 
     * @param name the name
     * @param plugin the Plugin
     * @param translation the MessageTranslation
     * @param description the description
     * @param usage the usage
     * @param aliases the aliases
     * @param executor the CommandExecutor
     * @param subcommands the subcommands
     * @param completions the Completions
     */
    public Command(String name, Plugin plugin, MessageTranslation translation, String description, String usage, List<String> aliases, CommandExecutor executor, Map<String, Command> subcommands, Map<Integer, Completion> completions) {
        super(name, description, usage, aliases);
        this.plugin = plugin;
        this.translation = translation;
        this.executor = executor;
        this.subcommands = subcommands;
        this.completions = completions;
    }

    
    /**
     * Merges the arguments enclosed in quotation marks which contains spaces, and 
     * delegates execution to {@link #execute(Context, Arguments)}
     * 
     * @param sender the source object which is executing this Command
     * @param label the label of the command used
     * @param args all arguments passed to this Command, split via ' '
     * @return true if the command was successful; else false
     */
    @Override
    public boolean execute(CommandSender sender, String label, String[] args) {
        return execute(new Context(sender, label, null, this), new Arguments(preserveQuotes(args)));
    }
    
    /**
     * Delegates execution to a subcommand with an updated {@code Context} and trimmed {@code Arguments},
     * or the {@code CommandExecutor} for this {@code Command} if no subcommand with 
     * a name equal to the last argument exists.
     * 
     * @param context the context which this command is executed in
     * @param arguments the arguments passed to this Command
     * @return true if the command was successful; else false
     */
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
    
    
    /**
     * Merges the arguments enclosed in quotation marks which contains spaces, and
     * delegates tab completion to {@link #complete(CommandSender, Arguments)}.
     * 
     * @param sender the source object which is executing this Command
     * @param alias the alias of the command used
     * @param args all arguments passed to this Command, split via ' '
     * @return a list of tab-completions for the specified arguments
     */
    @Override
    public @Nonnull List<String> tabComplete(CommandSender sender, String alias, String[] args) {
        return complete(sender, new Arguments(preserveQuotes(args)));
    }
    
    /**
     * Delegates tab completion to a subcommand with a name equal to the first argument if present,
     * 
     * else returns a list of subcommands which begin with the last argument 
     * if this {@code Command} has subcommands and the number of arguments is 1,
     * 
     * else delegates execution to a {@code Completion} with an associated index equal to the index of the last argument if present, or returns a list of
     * {@code Player} names if not present.
     * 
     * Otherwise, return an empty list if the number of arguments is 0.
     * 
     * @param sender the source object which is executing this Command
     * @param arguments all arguments passed to this Command
     * @return a list of tab-completions for the specified Arguments
     */
    public @Nonnull List<String> complete(CommandSender sender, Arguments arguments) {
        if (arguments.length() == 0) {
            return EMPTY_LIST;
        }

        String argument = arguments.get()[0];
        
        Command subcommand = subcommands.get(argument);
        if (subcommand != null) {
            arguments.trim();
            return subcommand.complete(sender, arguments);

        } else if (!subcommands.isEmpty() && arguments.length() == 1) {
            return subcommands.values().stream()
                    .filter(command -> sender.hasPermission(command.getPermission()) && command.getName().startsWith(argument))
                    .map(Command::getName)
                    .collect(toList());

        } else {
            return completions.getOrDefault(arguments.length() - 1, PLAYER_NAMES).complete(sender, arguments.getLast().text());
        }
    }
       
    
    /**
     * Returns the {@code Plugin} which this {@code Command} belongs to.
     * 
     * @return the Plugin
     */
    @Override
    public Plugin getPlugin() {
        return plugin;
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public MessageTranslation getTranslation() {
        return translation;
    }
    
    /**
     * Returns the {@code CommandExecutor} for this {@code Command}.
     * 
     * @return the CommandExecutor
     */
    public CommandExecutor getExecutor() {
        return executor;
    }
    
    /**
     * Sets the {@code CommandExecutor} for this {@code Command}.
     * 
     * @param executor the CommandExecutor
     */
    public void setExecutor(CommandExecutor executor) {
        this.executor = executor;
    }

    /**
     * Returns the names of the subcommands associated with the subcommands.
     * 
     * @return a Map which contains the names and associated subcommands
     */
    public Map<String, Command> getSubcommands() {
        return subcommands;
    }
    
    /**
     * Returns the indexes of arguments associated with the {@code Completion}s.
     * 
     * @return a Map which contains the indexes of arguments and associated Completions
     */
    public Map<Integer, Completion> getCompletions() {
        return completions;
    }
    
    
    /**
     * Represents a {@code Command} builder for the specified {@code Plugin}.
     * 
     * @param plugin the Plugin
     * @return the builder
     */
    public static Builder builder(Plugin plugin) {
        return new Builder(new Command("", plugin, MessageTranslation.NONE, "", "", new ArrayList<>(), CommandExecutor.NONE, new HashMap<>(), new HashMap<>()));
    }
    
    
    /**
     * Represents a builder for {@code Command}s.
     */
    public static class Builder {
        
        private Command command;
        
        
        private Builder(Command command) {
            this.command = command;
        }
        
        
        /**
         * Sets the name.
         * 
         * @param name the name
         * @return this
         */
        public Builder name(String name) {
            command.setName(name);
            return this;
        }
        
        /**
         * Sets the description.
         * 
         * @param description the description
         * @return this
         */
        public Builder description(String description) {
            command.setDescription(description);
            return this;
        }
        
        /**
         * Sets the usage.
         * 
         * @param usage the usage
         * @return this
         */
        public Builder usage(String usage) {
            command.setUsage(usage);
            return this;
        }
        
        /**
         * Sets the aliases.
         * 
         * @param aliases the aliases
         * @return this
         */
        public Builder aliases(List<String> aliases) {
            command.setAliases(aliases);
            return this;
        }
        
        /**
         * Sets the {@code MessageTranslation}.
         * 
         * @param translation the MessageTranslation
         * @return this
         */
        public Builder translation(MessageTranslation translation) {
            command.translation = translation;
            return this;
        }
        
        /**
         * Sets the {@code CommandExecutor}.
         * 
         * @param executor the CommandExecutor
         * @return this
         */
        public Builder executor(CommandExecutor executor) {
            command.executor = executor;
            return this;
        }
        
        /**
         * Sets the subcommands.
         * 
         * @param subcommands the subcommands
         * @return this
         */
        public Builder subcommands(Map<String, Command> subcommands) {
            command.subcommands = subcommands;
            return this;
        }
        
        /**
         * Sets the {@code Completion}s.
         * 
         * @param completions the completions
         * @return this
         */
        public Builder completions(Map<Integer, Completion> completions) {
            command.completions = completions;
            return this;
        }
        
        /**
         * Builds the {@code Command}.
         * 
         * @return the Command
         */
        public Command build() {
            return command;
        }
        
    }
    
}
