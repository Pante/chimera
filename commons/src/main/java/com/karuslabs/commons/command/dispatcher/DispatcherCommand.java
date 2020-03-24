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
package com.karuslabs.commons.command.dispatcher;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.exceptions.CommandSyntaxException;

import java.util.List;

import org.bukkit.command.*;
import org.bukkit.plugin.Plugin;


public class DispatcherCommand extends Command implements PluginIdentifiableCommand {
    
    private Plugin plugin;
    private CommandDispatcher<CommandSender> dispatcher;
    
    
    public DispatcherCommand(String name, Plugin plugin, CommandDispatcher<CommandSender> dispatcher, String usage, List<String> aliases) {
        super(name, "", usage, aliases);
        this.plugin = plugin;
        this.dispatcher = dispatcher;
    }
    
    
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
    

    @Override
    public Plugin getPlugin() {
        return plugin;
    }
    
}
