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
import com.karuslabs.commons.command.types.Type;

import com.mojang.brigadier.*;
import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.suggestion.SuggestionProvider;
import com.mojang.brigadier.tree.*;

import java.util.*;
import java.util.function.Predicate;

import net.minecraft.commands.*;
import net.minecraft.commands.synchronization.*;

import org.bukkit.command.CommandSender;

import org.checkerframework.checker.nullness.qual.Nullable;

/**
 * A mapper that maps the commands in a {@link Dispatcher} to the internal dispatcher 
 * of the server.
 * 
 * As of 1.19, only commands from console are forwarded to a {@code DispatcherCommand}.
 * Commands issued by players are routed directly to the native {@code CommandDispatcher}.
 * This behaviour is weird as fuck. We previously only mapped completion related information.
 */
class SpigotMapper extends Mapper<CommandSender, CommandSourceStack> {

    static final Map<ClientSuggestionProvider, SuggestionProvider<CommandSourceStack>> CLIENT_SIDE = new EnumMap<>(ClientSuggestionProvider.class);
    static {
        CLIENT_SIDE.put(ClientSuggestionProvider.RECIPES, SuggestionProviders.ALL_RECIPES);
        CLIENT_SIDE.put(ClientSuggestionProvider.SOUNDS, SuggestionProviders.AVAILABLE_SOUNDS);
        CLIENT_SIDE.put(ClientSuggestionProvider.ENTITIES, SuggestionProviders.SUMMONABLE_ENTITIES);
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
        return type instanceof Type<?> mappable ? mappable.mapped() : type;
    }
    
    @Override
    protected @Nullable Command<CommandSourceStack> execution(CommandNode<CommandSender> command) {
        return command.getCommand() == null ? null : reparse(command);
    }
    
    Command<CommandSourceStack> reparse(CommandNode<CommandSender> command) {
        return (context) -> {
            var sender = context.getSource().getBukkitSender();
            
            var input = context.getInput();
            
            var reparsed = dispatcher.parse(input, sender).getContext().build(input);
            return command.getCommand().run(reparsed);
        };
    }
    
    /**
     * Wraps the requirement of the given command in a predicate that transforms 
     * a {@code CommandListenerWrapper} into a {@code CommandSender}.
     * 
     * @param command the command
     * @return the wrapped requirement
     */
    @Override
    protected Predicate<CommandSourceStack> requirement(CommandNode<CommandSender> command) {
        var requirement = command.getRequirement();
        return requirement == null ? (Predicate<CommandSourceStack>) TRUE : stack -> requirement.test(stack.getBukkitSender());
    }

    /**
     * Wraps a source of suggestions provided by the given command in a {@code SuggestionProvider}
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
    protected @Nullable SuggestionProvider<CommandSourceStack> suggestions(ArgumentCommandNode<CommandSender, ?> command) {
        var type = command.getType();
        var suggestor = command.getCustomSuggestions();
        
        if (suggestor == null) {
            return type instanceof Type<?> mappable ? reparse(mappable) : null;
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
    SuggestionProvider<CommandSourceStack> reparse(Type<?> type) {
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
    SuggestionProvider<CommandSourceStack> reparse(SuggestionProvider<CommandSender> suggestor) {
        return (context, suggestions) -> {
            var sender = context.getSource().getBukkitSender();
            
            var input = context.getInput();
            input = input.length() <= 1 ? "" : input.substring(1);
            
            var reparsed = dispatcher.parse(input, sender).getContext().build(context.getInput());
            return suggestor.getSuggestions(reparsed, suggestions);
        };
    }
    
}
