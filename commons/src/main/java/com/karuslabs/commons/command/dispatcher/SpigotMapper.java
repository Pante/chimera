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
package com.karuslabs.commons.command.dispatcher;

import com.karuslabs.commons.command.ClientSuggestionProvider;
import com.karuslabs.commons.command.tree.Mapper;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.suggestion.SuggestionProvider;
import com.mojang.brigadier.tree.*;

import java.util.*;
import java.util.function.Predicate;

import net.minecraft.server.v1_16_R3.*;

import org.bukkit.command.CommandSender;

import org.checkerframework.checker.nullness.qual.Nullable;
import com.karuslabs.commons.command.types.Type;

/**
 * A mapper that maps the commands in a {@link Dispatcher} to the internal dispatcher 
 * of the server.
 */
class SpigotMapper extends Mapper<CommandSender, CommandListenerWrapper> {
    
    static final Map<ClientSuggestionProvider, SuggestionProvider<CommandListenerWrapper>> CLIENT_SIDE = new EnumMap<>(ClientSuggestionProvider.class);
    static {
        CLIENT_SIDE.put(ClientSuggestionProvider.RECIPES, CompletionProviders.b);
        CLIENT_SIDE.put(ClientSuggestionProvider.SOUNDS, CompletionProviders.c);
        CLIENT_SIDE.put(ClientSuggestionProvider.BIOMES, CompletionProviders.d);
        CLIENT_SIDE.put(ClientSuggestionProvider.ENTITIES, CompletionProviders.e);
    }
    
    private final CommandDispatcher<CommandSender> dispatcher;
    
    /**
     * Creates a {@code SpigotMapper} with the given {@code dispatcher}.
     * 
     * @param dispatcher the dispatcher
     */
    SpigotMapper(CommandDispatcher<CommandSender> dispatcher) {
        this.dispatcher = dispatcher;
    }
    
    /**
     * Returns either the type or mapped derivative of the given command if it has 
     * a custom type that implements {@link Type}.
     * 
     * @param command the command
     * @return the argument type, or mapped derivative if the command has a custom type
     *         that implements {@code Type}
     */
    @Override
    protected ArgumentType<?> type(ArgumentCommandNode<CommandSender, ?> command) {
        var type = command.getType();
        return type instanceof Type<?> ? ((Type<?>) type).mapped() : type;
    }
    
    /**
     * Wraps the requirement of the given command in a predicate that transforms 
     * a {@code CommandListenerWrapper} into a {@code CommandSender}.
     * 
     * @param command the command
     * @return the wrapped requirement
     */
    @Override
    protected Predicate<CommandListenerWrapper> requirement(CommandNode<CommandSender> command) {
        var requirement = command.getRequirement();
        return requirement == null ? (Predicate<CommandListenerWrapper>) TRUE : listener -> requirement.test(listener.getBukkitSender());
    }

    /**
     * Wraps a source of suggestions provided by the given command in a {@code SuggestionProvider
     * that transforms a {@code CommandListenerWrapper} into a {@code CommandSender}.
     * 
     * A client-side {@code SuggestionProvider} is returned if the {@code SuggestionProvider}
     * returned by {@code command} is a {@link ClientsideProvider};
     * 
     * Otherwise, if the given command has no custom {@code SuggestionProvider}
     * but has a custom type that implements {@link Type}, the custom type is treated 
     * as the source;
     * 
     * Otherwise if the command has neither a custom type nor custom {@code SuggestionProvider},
     * {@code null} is returned.
     * 
     * @param command the command which provides a source of suggestions
     * @return a {@code SuggestionProvider}, or {@code null} if no source was provided
     */
    @Override
    protected @Nullable SuggestionProvider<CommandListenerWrapper> suggestions(ArgumentCommandNode<CommandSender, ?> command) {
        var type = command.getType();
        var suggestor = command.getCustomSuggestions();
        
        if (!(type instanceof Type<?>) && suggestor == null) {
            return null;
            
        } else if (suggestor == null) {
            return reparse((Type<?>) type);
        }
        
        var client = CLIENT_SIDE.get(suggestor);
        if (client != null) {
            return client;
 
        } else {
            return reparse(suggestor);
        } 
    }
    
    /**
     * Wraps the given type in a {@code SuggestionProvider} that reparses an input 
     * using this {@code dispatcher}.
     * 
     * @param type the type
     * @return a {@code SuggestionProvider}
     */
    SuggestionProvider<CommandListenerWrapper> reparse(Type<?> type) {
        return (context, suggestions) -> {
            var sender = context.getSource().getBukkitSender();
            
            var input = context.getInput();
            input = input.length() <= 1 ? "" : input.substring(1);
            
            var reparsed = dispatcher.parse(input, sender).getContext().build(context.getInput());
            return type.listSuggestions(reparsed, suggestions);
        };
    }
    
    /**
     * Wraps the given suggestor in a {@code SuggestionProvider} that reparses an 
     * input using this {@code dispatcher}.
     * 
     * @param suggestor the {@code SuggestionProvider}
     * @return the {@code SuggestionProvider}
     */
    SuggestionProvider<CommandListenerWrapper> reparse(SuggestionProvider<CommandSender> suggestor) {
        return (context, suggestions) -> {
            var sender = context.getSource().getBukkitSender();
            
            var input = context.getInput();
            input = input.length() <= 1 ? "" : input.substring(1);
            
            var reparsed = dispatcher.parse(input, sender).getContext().build(context.getInput());
            return suggestor.getSuggestions(reparsed, suggestions);
        };
    }
    
}
