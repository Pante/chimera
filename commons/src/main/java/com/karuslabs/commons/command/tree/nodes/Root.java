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


/**
 * A {code RootCommandNode} subclass that facilities the wrapping and registration
 * of a {@code CommandNode} to a {@code CommandMap}.
 */
public class Root extends RootCommandNode<CommandSender> implements Mutable<CommandSender> {
    
    private String prefix;
    private DispatcherMap map;
    
    
    /**
     * Creates a {@code Root} with the given parameters and the name of the given 
     * plugin as a fallback prefix.
     * 
     * @param prefix the fallback prefix
     * @param map the {@code DispatcherMap}
     */
    public Root(String prefix, DispatcherMap map) {
        this.prefix = prefix;
        this.map = map;
    }
    
    
    /**
     * Adds the {@code command} if the provided {@code DispatcherMap} does not contain 
     * a command with the same name. In addition, a fallback alias of the {@code command} 
     * is always created and added. If the {@code command} implements {@link Aliasable},
     * the aliases and the fallback of the aliases are also added in a similar fashion.
     * 
     * @param command the command to be added
     * @throws IllegalArgumentException if the {@code command} is not a {@code LiteralCommandNode}
     */
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
    
    
    /**
     * Returns the {@code DispatcherMap}.
     * 
     * @return the {@code DispatcherMap} 
     */
    public DispatcherMap getDispatcherMap() {
        return map;
    }
    
}
