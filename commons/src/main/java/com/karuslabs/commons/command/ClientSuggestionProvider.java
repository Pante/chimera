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

import com.karuslabs.annotations.Ignored;

import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.suggestion.*;

import java.util.concurrent.CompletableFuture;

import org.bukkit.command.CommandSender;

/**
 * A {@code SuggestionProvider} where suggestions are provided by the client.
 * <br><br>
 * <b>Implementation details: </b><br>
 * Since suggestions are provided by the client, a {@code ClientSuggestionProvider}
 * will always provide no suggestions.
 */
public enum ClientSuggestionProvider implements SuggestionProvider<CommandSender> {
    
    ENTITIES, RECIPES, SOUNDS;
    
    @Override
    public CompletableFuture<Suggestions> getSuggestions(@Ignored CommandContext<CommandSender> context, SuggestionsBuilder builder) {
        return builder.buildFuture();
    }
    
}
