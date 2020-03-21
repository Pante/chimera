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
package com.karuslabs.commons.command.tree.nodes;

import com.karuslabs.commons.command.*;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.tree.*;

import java.util.*;

import org.bukkit.command.*;
import org.bukkit.plugin.Plugin;

import org.checkerframework.checker.nullness.qual.Nullable;


public class Root extends RootCommandNode<CommandSender> {
    
    private String prefix;
    private Plugin plugin;
    private CommandMap map;
    @Nullable CommandDispatcher<CommandSender> dispatcher;
    
    
    public Root(Plugin plugin, CommandMap map) {
        this(plugin.getName().toLowerCase(), plugin, map);
    }
    
    public Root(String prefix, Plugin plugin, CommandMap map) {
        this.prefix = prefix;
        this.plugin = plugin;
        this.map = map;
    }
    
    
    @Override
    public void addChild(CommandNode<CommandSender> command) {
        if (!(command instanceof LiteralCommandNode<?>)) {
            throw new IllegalArgumentException("Invalid command registered: " + command.getName() + ", commands registered to root must be a literal");
        }
        
        var literal = (LiteralCommandNode<CommandSender>) command;
        
        var aliases = List.<LiteralCommandNode<CommandSender>>of();
        if (command instanceof Aliasable<?>) {
            aliases = new ArrayList<>(((Aliasable<CommandSender>) command).aliases());
        }
        
        register(literal, aliases);
    }
    
    protected void register(LiteralCommandNode<CommandSender> command, List<LiteralCommandNode<CommandSender>> aliases) {
        var wrapper = wrap(command, aliases);
        
        super.addChild(Literal.alias(command, prefix + ":" + command.getName()));
        if (map.register(prefix, wrapper)) {
            super.addChild(command);
        }
        
        for (var alias : aliases) {
            super.addChild(Literal.alias(command, prefix + ":" + alias.getName()));
            if (wrapper.getAliases().contains(alias.getName())) {
                super.addChild(alias);
            }
        }
    }
    
    protected Command wrap(LiteralCommandNode<CommandSender> command, List<LiteralCommandNode<CommandSender>> aliases) {
        var names = new ArrayList<String>();
        for (var alias : aliases) {
            names.add(alias.getName());
        }
        
        return new DispatcherCommand(command.getName(), plugin, dispatcher, command.getUsageText(), names);
    }
    
    
    public @Nullable CommandDispatcher<CommandSender> dispatcher() {
        return dispatcher;
    }
    
    public void dispatcher(CommandDispatcher<CommandSender> dispatcher) {
        if (this.dispatcher == null) {
            this.dispatcher = dispatcher;
            
        } else {
            throw new IllegalStateException("CommandDispatcher is already initialized");
        }
    }
    
}
