/*
 * The MIT License
 *
 * Copyright 2019 Karus Labs.
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

import com.karuslabs.commons.command.arguments.TypeArgument;
import com.karuslabs.commons.command.suggestions.ClientsideProvider;
import com.karuslabs.commons.command.tree.Mapper;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.suggestion.SuggestionProvider;
import com.mojang.brigadier.tree.*;

import java.util.*;
import java.util.function.Predicate;

import net.minecraft.server.v1_13_R2.*;

import org.bukkit.command.CommandSender;

import org.checkerframework.checker.nullness.qual.Nullable;


class DispatcherMapper extends Mapper<CommandSender, CommandListenerWrapper> {
    
    private static final Map<ClientsideProvider, SuggestionProvider<CommandListenerWrapper>> CLIENT_SIDE;
    
    static {
        CLIENT_SIDE = new EnumMap<>(ClientsideProvider.class);
        CLIENT_SIDE.put(ClientsideProvider.RECIPES, CompletionProviders.b);
        CLIENT_SIDE.put(ClientsideProvider.SOUNDS, CompletionProviders.c);
        CLIENT_SIDE.put(ClientsideProvider.ENTITIES, CompletionProviders.d);
    }
    
    
    private CommandDispatcher<CommandSender> dispatcher;
    
    
    DispatcherMapper(CommandDispatcher<CommandSender> dispatcher) {
        this.dispatcher = dispatcher;
    }
    
    
    @Override
    protected ArgumentType<?> type(ArgumentCommandNode<CommandSender, ?> command) {
        var type = command.getType();
        return type instanceof TypeArgument<?, ?> ? ((TypeArgument<?, ?>) type).primitive() : type;
    }
    
    
    @Override
    protected Predicate<CommandListenerWrapper> requirement(CommandNode<CommandSender> command) {
        var requirement = command.getRequirement();
        return requirement == null ? (Predicate<CommandListenerWrapper>) TRUE : listener -> requirement.test(listener.getBukkitSender());
    }

    
    @Override
    protected @Nullable SuggestionProvider<CommandListenerWrapper> suggestions(ArgumentCommandNode<CommandSender, ?> command) {
        var type = command.getType();
        var suggestor = command.getCustomSuggestions();
        
        if (!(type instanceof TypeArgument<?, ?>) && suggestor == null) {
            return null;
            
        } else 
            if (suggestor == null) {
            return reparse((TypeArgument<?, ?>) type);
        }
        
        var client = CLIENT_SIDE.get(suggestor);
        if (client != null) {
            return client;
 
        } else {
            return reparse(suggestor);
        } 
    }
    
    protected SuggestionProvider<CommandListenerWrapper> reparse(SuggestionProvider<CommandSender> suggestor) {
        return (context, suggestions) -> {
            var sender = context.getSource().getBukkitSender();
            var reparsed = dispatcher.parse(context.getInput(), sender).getContext().build(context.getInput());
            return suggestor.getSuggestions(reparsed, suggestions);
        };
    }
    
}
