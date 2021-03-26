/*
 * The MIT License
 *
 * Copyright 2020 Karus Labs.
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
package com.karuslabs.typist;

import com.karuslabs.commons.command.*;
import com.karuslabs.elementary.processor.type.TypeMirrors;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.suggestion.*;

import java.util.concurrent.CompletableFuture;
import java.util.function.Predicate;
import javax.lang.model.type.TypeMirror;
import javax.lang.model.util.*;

import org.bukkit.command.CommandSender;

public class Types extends TypeMirrors {

    public final TypeMirror object;
    public final TypeMirror sender; 
    public final TypeMirror context;
    public final TypeMirror optionalContext;
    public final TypeMirror argument;
    public final TypeMirror completable;
    public final TypeMirror command;
    public final TypeMirror execution;
    public final TypeMirror requirement;
    public final TypeMirror suggestionsBuilder;
    public final TypeMirror suggestionProvider;
    
    
    public Types(Elements elements, TypeMirrors types) {
        super(elements, types);
        object = super.type(Object.class);
        sender = super.type(CommandSender.class);
        context = super.specialize(CommandContext.class, CommandSender.class);
        optionalContext = super.specialize(OptionalContext.class, CommandSender.class);
        argument = super.erasure(ArgumentType.class);
        completable = super.specialize(CompletableFuture.class, Suggestions.class);
        command = super.specialize(Command.class, sender);
        execution = super.specialize(Execution.class, sender);
        requirement = super.specialize(Predicate.class, sender);
        suggestionsBuilder = super.type(SuggestionsBuilder.class);
        suggestionProvider = super.specialize(SuggestionProvider.class, sender);
    }
    
    Type(T.class, (CommandSender.class))

}
