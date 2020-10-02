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
package com.karuslabs.commons.command.aot.parsers;

import com.karuslabs.annotations.processor.*;
import com.karuslabs.commons.command.aot.Identifier;
import com.karuslabs.commons.command.aot.Mirrors.*;
import com.karuslabs.commons.command.aot.Mirrors.Member.Type;

import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.suggestion.*;

import java.util.concurrent.CompletableFuture;
import java.util.function.Predicate;
import javax.lang.model.element.*;
import javax.lang.model.type.*;
import javax.lang.model.util.SimpleElementVisitor9;

import org.bukkit.command.CommandSender;

import org.checkerframework.checker.nullness.qual.Nullable;

public class Binder extends SimpleElementVisitor9<Member<?>, Identifier> {
    
    private final Typing typing;
    private final Logger logger;
    private final TypeMirror completable;
    private final TypeMirror argument;
    private final TypeMirror command;
    private final TypeMirror requirement;
    private final TypeMirror suggestions;
    
    
    
    public Binder(Typing typing, Logger logger) {
        this.typing = typing;
        this.logger = logger;
        completable = typing.specialize(CompletableFuture.class, Suggestions.class);
        argument = typing.erasure(ArgumentType.class);
        command = typing.specialize(Command.class, CommandSender.class);
        requirement = typing.specialize(Predicate.class, CommandSender.class);
        suggestions = typing.specialize(SuggestionProvider.class, CommandSender.class);
    }
    
    @Override
    public @Nullable Method visitExecutable(ExecutableElement element, Identifier identifier) {
        var type = infer(element.getReturnType());
        if (type == null) {
            logger.error(element.getSimpleName(), "has an invalid return type", "should return an integer, void, boolean or CompletableFuture<Suggestions>");
            return null;
        }
        
        return new Method(identifier, element, type);
    }
    
    @Nullable Type infer(TypeMirror returned) {
        if (returned.getKind() == TypeKind.INT || returned.getKind() == TypeKind.VOID) {
            return Member.Type.COMMAND;
            
        } else if (returned.getKind() == TypeKind.BOOLEAN) {
            return Member.Type.REQUIREMENT;
            
        } else if (typing.types.isSubtype(returned, completable)) {
            return Member.Type.SUGGESTION_PROVIDER;
            
        } else {
            return null;
        }
    }
    
    @Override
    public @Nullable Field visitVariable(VariableElement element, Identifier identifier) {
        var type = type(element.asType());
        if (type == null) {
            logger.error(element.getSimpleName(), "has an invalid type", "should be an ArgumentType<?>, Command<CommandSender>, Predicate<CommandSender>, SuggestionProvider<CommandSender>");
            return null;
        }
        
        return new Field(identifier, element, type);
    }
    
    @Nullable Type type(TypeMirror type) {
        if (typing.types.isSubtype(type, argument)) {
            return Member.Type.ARGUMENT_TYPE;
            
        } else if (typing.types.isSubtype(type, command)) {
            return Member.Type.COMMAND;
            
        } else if (typing.types.isSubtype(type, requirement)) {
            return Member.Type.REQUIREMENT;
            
        } else if (typing.types.isSubtype(type, suggestions)) {
            return Member.Type.SUGGESTION_PROVIDER;
            
        } else {
            return null;
        }
    }
    
    
    @Override
    public @Nullable Member<?> defaultAction(Element element, Identifier identifier) {
        logger.error(identifier.lexeme, "is used on an invalid target", "should annotate either a field or method");
        return null;
    }
    
}
