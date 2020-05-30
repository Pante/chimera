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
package com.karuslabs.commons.command.synchronization;

import com.karuslabs.commons.command.tree.Mapper;

import com.mojang.brigadier.suggestion.SuggestionProvider;
import com.mojang.brigadier.tree.ArgumentCommandNode;

import net.minecraft.server.v1_15_R1.*;

import org.checkerframework.checker.nullness.qual.Nullable;


/**
 * A mapper that maps the commands in the server's internal dispatcher to the type 
 * required by an outgoing {@code PacketPlayOutCommands}.
 */
class SynchronizationMapper extends Mapper<CommandListenerWrapper, ICompletionProvider> {
    
    static final SynchronizationMapper MAPPER = new SynchronizationMapper();
    
    /**
     * Swaps the {@code SuggestionProvider} of the given command to 
     * {@code net.minecraft.commands.synchronization.SuggestionProviders#ASK_SERVER}
     * if present and not a {@coode net.minecraft.commands.synchronization.SuggestionProviders$Wrapper}.
     * 
     * Otherwise returns {@code null} if the {@code SuggestionProvider} of the given 
     * command is not present.
     * 
     * @param command the command
     * @return the swapped {@code SuggestionProvider}, or {@code null} if the
     *         the {@code SuggestionProvider} of the given command is not present.
     */
    @Override
    protected @Nullable SuggestionProvider<ICompletionProvider> suggestions(ArgumentCommandNode<CommandListenerWrapper, ?> command) {
        // Fucking nasty workaround in whcih Mojang abused using raw types. It only 
        // works because CommandListenerWrapper is the sole implementation of ICompleteionProvider.
        SuggestionProvider provider = command.getCustomSuggestions();
        return provider == null ? null: CompletionProviders.b(provider);
    }
    
}
