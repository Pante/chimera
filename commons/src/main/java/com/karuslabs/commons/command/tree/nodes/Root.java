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

import com.karuslabs.annotations.Ignored;
import com.karuslabs.commons.command.Commands;
import com.karuslabs.commons.command.dispatcher.*;

import com.mojang.brigadier.tree.*;

import java.util.*;

import org.bukkit.command.*;


public class Root extends RootCommandNode<CommandSender> implements Mutable<CommandSender> {
    
    private String prefix;
    private DispatcherMap map;
    
    
    public Root(String prefix, DispatcherMap map) {
        this.prefix = prefix;
        this.map = map;
    }
    
    
    @Override
    public void addChild(CommandNode<CommandSender> command) {
        if (getChild(command.getName()) != null) {
            throw new IllegalArgumentException("Invalid command: '" + command.getName() + "', root already contains a child with the same name");
            
        }  else if (!(command instanceof LiteralCommandNode<?>)) {
            throw new IllegalArgumentException("Invalid command: '" + command.getName() + "', commands registered to root must be a literal");
        }
        
        var literal = (LiteralCommandNode<CommandSender>) command;
        
        var wrapper = map.register(literal);
        if (wrapper == null) {
            return;
        }
        
        super.addChild(Literal.alias(literal, prefix + ":" + literal.getName()));
        if (wrapper.getName().equals(wrapper.getLabel())) {
            super.addChild(literal);
        }
        
        if (literal instanceof Aliasable<?>) {
            for (var alias : new ArrayList<>((((Aliasable<CommandSender>) literal).aliases()))) {
                if (wrapper.getAliases().contains(alias.getName())) {
                    super.addChild(Literal.alias(literal, prefix + ":" + alias.getName()));
                    super.addChild(alias);
                }
            }
        }
    }

    @Override
    public CommandNode<CommandSender> removeChild(String child) {
        return Commands.remove(this, child);
    }

    
    @Override
    public void setCommand(@Ignored com.mojang.brigadier.Command<CommandSender> command) {
        // Does nothing
    }

    @Override
    public void setRedirect(@Ignored CommandNode<CommandSender> destination) {
        // Does nothing
    }
    
    
    public DispatcherMap map() {
        return map;
    }
    
}
