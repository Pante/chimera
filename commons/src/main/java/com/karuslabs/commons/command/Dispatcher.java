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

import com.karuslabs.commons.command.Trees.Mapper;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.tree.*;

import java.util.function.Predicate;

import net.minecraft.server.v1_13_R2.*;

import org.bukkit.command.CommandSender;
import org.bukkit.event.Listener;


public class Dispatcher extends CommandDispatcher<CommandSender> implements Listener {
    
    private static final RequirementMapper MAPPER = new RequirementMapper();
    
    private CommandDispatcher<CommandListenerWrapper> dispatcher;
    private MinecraftServer server;
    
    
    public Dispatcher synchronize() {
        Trees.map(getRoot(), dispatcher.getRoot(), MAPPER);
        return this;
    }
    
}

class RequirementMapper extends Mapper<CommandSender, CommandListenerWrapper> {
    
    static final Predicate<CommandListenerWrapper> TRUE = listener -> true;
    
    
    @Override
    protected Predicate<CommandListenerWrapper> requirement(CommandNode<CommandSender> command) {
        var requirement = command.getRequirement();
        return requirement == null ? TRUE : listener -> requirement.test(listener.getBukkitSender());
    }
    
}
