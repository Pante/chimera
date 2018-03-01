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

import java.lang.reflect.Field;
import java.util.*;
import javax.annotation.Nullable;

import org.bukkit.*;
import org.bukkit.command.CommandSender;
import org.bukkit.command.SimpleCommandMap;
import org.bukkit.plugin.Plugin;

import static java.util.stream.Collectors.toMap;


/**
 * Represents a proxy for the {@code SimpleCommandMap} of the {@code Server}.
 */
public class ProxiedCommandMap {
    
    private SimpleCommandMap map;
    
    
    /**
     * Constructs a {@code ProxiedCommandMap} for the specified {@code Server}.
     * 
     * @param server the Server
     * @throws IllegalArgumentException if the specified Server does not contain a SimpleCommandMap
     */
    public ProxiedCommandMap(Server server) {
        try {
            Field field = server.getClass().getDeclaredField("commandMap");
            field.setAccessible(true);
            map = (SimpleCommandMap) field.get(server);

        } catch (ReflectiveOperationException | ClassCastException e) {
            throw new IllegalArgumentException("If you are reading this message, you're screwed.", e);
        }
    }
    
    
    /**
     * Registers all the commands belonging to a certain plugin.
     * <p>
     * Caller can use:-
     * <ul>
     * <li>command.getName() to determine the label registered for this
     *     command
     * <li>command.getAliases() to determine the aliases which where
     *     registered
     * </ul>
     *
     * @param fallbackPrefix a prefix which is prepended to each command with
     *     a ':' one or more times to make the command unique
     * @param commands a list of commands to register
     */
    public void registerAll(String fallbackPrefix, List<Command> commands) {
        commands.forEach(command -> register(fallbackPrefix, command));
    }
    
    /**
     * Registers a command. Returns true on success; false if name is already
     * taken and fallback had to be used.
     * <p>
     * Caller can use:-
     * <ul>
     * <li>command.getName() to determine the label registered for this
     *     command
     * <li>command.getAliases() to determine the aliases which where
     *     registered
     * </ul>
     *
     * @param label the label of the command, without the '/'-prefix.
     * @param fallbackPrefix a prefix which is prepended to the command with a
     *     ':' one or more times to make the command unique
     * @param command the command to register
     * @return true if command was registered with the passed in label, false
     *     otherwise, which indicates the fallbackPrefix was used one or more
     *     times
     */
    public boolean register(String label, String fallbackPrefix, Command command) {
        return map.register(label, fallbackPrefix, command);
    }
    
    /**
     * Registers a command. Returns true on success; false if name is already
     * taken and fallback had to be used.
     * <p>
     * Caller can use:-
     * <ul>
     * <li>command.getName() to determine the label registered for this
     *     command
     * <li>command.getAliases() to determine the aliases which where
     *     registered
     * </ul>
     *
     * @param fallbackPrefix a prefix which is prepended to the command with a
     *     ':' one or more times to make the command unique
     * @param command the command to register, from which label is determined
     *     from the command name
     * @return true if command was registered with the passed in label, false
     *     otherwise, which indicates the fallbackPrefix was used one or more
     *     times
     */
    public boolean register(String fallbackPrefix, Command command) {
        return map.register(fallbackPrefix, command);
    }
    
    
    /**
     * Looks for the requested command and executes it if found.
     *
     * @param sender The command's sender
     * @param cmdLine command + arguments. Example: "/test abc 123"
     * @return returns false if no target is found, true otherwise.
     */
    public boolean dispatch(CommandSender sender, String cmdLine) {
        return map.dispatch(sender, cmdLine);
    }

    
    /**
     * Clears all registered commands.
     */
    public void clearCommands() {
        map.clearCommands();
    }

    
    /**
     * Gets the command registered to the specified name, or {@code null} if no such {@code Command} exists.
     *
     * @param name Name of the command to retrieve
     * @return Command with the specified name or null if a command with that
     *     label doesn't exist
     */
    public @Nullable Command getCommand(String name) {
        org.bukkit.command.Command command = map.getCommand(name);
        return command instanceof Command ? (Command) command : null;
    }
    
    
    /**
     * Gets the names and associated {@code Command}s of the specified {@code Plugin}.
     * 
     * @param plugin the Plugin
     * @return a Map which contains the names and associated Commands of the specified Plugin
     */
    public Map<String, Command> getCommands(Plugin plugin) {
        return map.getCommands().stream()
            .filter(command -> command instanceof Command && plugin.equals(((Command) command).getPlugin()))
            .collect(toMap(command -> command.getName(), command -> (Command) command));
    }
    
    
    /**
     * Looks for the requested command and executes an appropriate
     * tab-completer if found. This method will also tab-complete partial
     * commands.
     *
     * @param sender The command's sender.
     * @param cmdLine The entire command string to tab-complete, excluding
     *     initial slash.
     * @return a list of possible tab-completions. This list may be immutable.
     *     Will be null if no matching command of which sender has permission.
     */
    public List<String> tabComplete(CommandSender sender, String cmdLine)  {
        return map.tabComplete(sender, cmdLine);
    }
    
    /**
     * Looks for the requested command and executes an appropriate
     * tab-completer if found. This method will also tab-complete partial
     * commands.
     *
     * @param sender The command's sender.
     * @param cmdLine The entire command string to tab-complete, excluding
     *     initial slash.
     * @param location The position looked at by the sender, or null if none
     * @return a list of possible tab-completions. This list may be immutable.
     *     Will be null if no matching command of which sender has permission.
     */
    public List<String> tabComplete(CommandSender sender, String cmdLine, Location location) {
        return map.tabComplete(sender, cmdLine, location);
    }
    
    
    /**
     * Returns the proxied {@code SimpleCommandMap}.
     * 
     * @return the SimpleCommandMap
     */
    public SimpleCommandMap getProxiedMap() {
        return map;
    }
    
}
