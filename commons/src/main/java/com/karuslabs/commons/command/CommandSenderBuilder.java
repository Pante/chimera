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

import com.karuslabs.commons.command.tree.Trees.Builder;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.suggestion.SuggestionProvider;
import com.mojang.brigadier.tree.*;

import java.util.function.Predicate;

import net.minecraft.server.v1_13_R2.CommandListenerWrapper;

import org.bukkit.command.CommandSender;

import org.checkerframework.checker.nullness.qual.Nullable;


class CommandSenderBuilder extends Builder<CommandSender, CommandListenerWrapper> {
    
    private CommandDispatcher<CommandSender> dispatcher;
    
    
    CommandSenderBuilder(CommandDispatcher<CommandSender> dispatcher) {
        this.dispatcher = dispatcher;
    }
    

    @Override
    protected Predicate<CommandListenerWrapper> requirement(CommandNode<CommandSender> command) {
        var requirement = command.getRequirement();
        return requirement == null ? (Predicate<CommandListenerWrapper>) TRUE : listener -> requirement.test(listener.getBukkitSender());
    }

    @Override
    protected @Nullable SuggestionProvider<CommandListenerWrapper> suggestions(ArgumentCommandNode<CommandSender, ?> command) {
        var suggestor = command.getCustomSuggestions();
        if (suggestor != null) {
            return (context, suggestions) -> {
            var reparsed = dispatcher.parse(context.getInput(), context.getSource().getBukkitSender()).getContext().build(context.getInput());
            return suggestor.getSuggestions(reparsed, suggestions);
            };
            
        } else {
            return null;
        }
    }

}
