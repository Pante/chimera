/*
 * The MIT License
 *
 * Copyright 2018 Karus Labs.
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

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.exceptions.CommandSyntaxException;

import java.util.List;

import org.bukkit.command.*;
import org.bukkit.plugin.Plugin;


/**
 * A Spigot {@code Command} subclass that delegates execution to a underlying 
 * {@code CommandDispatcher}.
 */
public class DispatcherCommand extends Command implements PluginIdentifiableCommand {
    
    private Plugin plugin;
    private CommandDispatcher<CommandSender> dispatcher;
    
    
    /**
     * Constructs a {@code DispatcherCommand} with the given name plugin, dispatcher,
     * and an empty description.
     * 
     * @param name the name
     * @param plugin the owning plugin
     * @param dispatcher the underlying dispatcher to which execution is delegated
     * @param usage the example usage for this command
     */
    public DispatcherCommand(String name, Plugin plugin, CommandDispatcher<CommandSender> dispatcher, String usage) {
        this(name, plugin, dispatcher, "", usage);
    }
    
    /**
     * Constructs a {@code DispatcherCommand} with the given name, plugin, dispatcher,
     * description and usage.
     * 
     * @param name the name
     * @param plugin the owning plugin
     * @param dispatcher the underlying dispatcher to which execution is delegated
     * @param description the description of this command
     * @param usage the example usage for this command
     */
    public DispatcherCommand(String name, Plugin plugin, CommandDispatcher<CommandSender> dispatcher, String description, String usage) {
        super(name, description, usage, List.of());
        this.plugin = plugin;
        this.dispatcher = dispatcher;
    }
    
    
    /**
     * Delegates execution with the rejoined label and arguments to the underlying 
     * {@code CommandDisaptcher} if given sender has sufficient permission.
     * 
     * @param sender the sender
     * @param label the label
     * @param arguments the arguments
     * @return true
     */
    @Override
    public boolean execute(CommandSender sender, String label, String... arguments) {
        if (!testPermission(sender)) {
            return true;
        }
        
        var reader = new StringReader(join(label, arguments));
        if (reader.canRead() && reader.peek() == '/') {
            reader.skip();
        }
        
        try {
            dispatcher.execute(reader, sender);
            
        } catch (CommandSyntaxException e) {
            Exceptions.report(sender, e);
            
        } catch (Exception e) {
            Exceptions.report(sender, e);
        }
        
        return true;
    }
    
    private String join(String name, String[] arguments) {
        String command =  "/" + name;
        if (arguments.length > 0) {
            command += " " + String.join(" ", arguments);
        }
        
        return command;
    }
    
    
    /**
     * Returns the owning plugin.
     * 
     * @return the owning plugin
     */
    @Override
    public Plugin getPlugin() {
        return plugin;
    }
    
}
