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
package com.karuslabs.commons.command.bridges;

import com.karuslabs.commons.command.Execution;

import com.mojang.brigadier.*;
import com.mojang.brigadier.builder.ArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.*;

import java.util.function.Predicate;

import net.minecraft.server.v1_13_R2.CommandListenerWrapper;

import org.bukkit.command.CommandSender;

import org.checkerframework.checker.nullness.qual.Nullable;


public abstract class Bridge implements Command<CommandListenerWrapper>, Predicate<CommandListenerWrapper> {
    
    protected @Nullable Execution execution;
    protected @Nullable Predicate<CommandSender> requirement;
    
    
    @Override
    public int run(CommandContext<CommandListenerWrapper> context) throws CommandSyntaxException {
        return execution.execute(sender(context), context);
    }
    
    @Override
    public boolean test(CommandListenerWrapper listener) {
        return requirement.test(listener.getBukkitSender());
    }
    
    
    protected CommandSender sender(CommandContext<CommandListenerWrapper> context) {
        return context.getSource().getBukkitSender();
    }
    
    
    public static abstract class Builder<Self extends Builder<Self, B, T>, B extends Bridge, T extends ArgumentBuilder<CommandListenerWrapper, T>> {
        
        protected B bridge;
        protected T builder;
        
        
        protected Builder(B bridge, T builder) {
            this.bridge = bridge;
            this.builder = builder;
        }
        
        
        public Self then(Builder<?, ?, ?> builder) {
            this.builder.then(builder.unbridge());
            return self();
        }
        
        
        public Self executes(Execution execution) {
            bridge.execution = execution;
            builder.executes(bridge);
            return self();
        }
        
        public Self requires(Predicate<CommandSender> requirement) {
            bridge.requirement = requirement;
            builder.executes(bridge);
            return self();
        }
        
        protected abstract Self self();
        
        
        public T unbridge() {
            return builder;
        }
        
    }
    
}
