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
package com.karuslabs.commons.command.internal;

import com.karuslabs.commons.command.*;
    
import com.mojang.brigadier.*;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.*;
import com.mojang.brigadier.suggestion.*;

import java.util.concurrent.CompletableFuture;
import java.util.function.Predicate;

import net.minecraft.server.v1_13_R2.CommandListenerWrapper;

import org.bukkit.command.CommandSender;

import org.checkerframework.checker.nullness.qual.Nullable;


public class Mapping implements Command<CommandListenerWrapper>, Predicate<CommandListenerWrapper>, SuggestionProvider<CommandListenerWrapper> {
    
    private @Nullable Execution execution;
    private @Nullable Predicate<CommandSender> requirement;
    private @Nullable Suggestor suggestor;
    
    
    @Override
    public int run(CommandContext<CommandListenerWrapper> context) throws CommandSyntaxException {
        return execution.execute(from(context), context);
}
    
    @Override
    public boolean test(CommandListenerWrapper listener) {
        return requirement.test(listener.getBukkitSender());
    }
    
    @Override
    public CompletableFuture<Suggestions> getSuggestions(CommandContext<CommandListenerWrapper> context, SuggestionsBuilder builder) throws CommandSyntaxException {
        return suggestor.suggest(from(context), context, builder);
    }
    
    protected CommandSender from(CommandContext<CommandListenerWrapper> context) {
        return context.getSource().getBukkitSender();
    }
    
    
    public Mapping execution(Execution execution) {
        this.execution = execution;
        return this;
    }
    
    public Mapping requirement(Predicate<CommandSender> requirement) {
        this.requirement = requirement;
        return this;
    }
    
    public Mapping suggests(Suggestor suggestor) {
        this.suggestor = suggestor;
        return this;
    }
    
}
