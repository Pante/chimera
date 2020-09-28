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

import com.mojang.brigadier.suggestion.Suggestions;

import java.util.concurrent.CompletableFuture;
import javax.lang.model.element.*;
import javax.lang.model.type.*;
import javax.lang.model.util.SimpleElementVisitor9;

import org.checkerframework.checker.nullness.qual.Nullable;

class Binder extends SimpleElementVisitor9<Member<?>, Identifier> {
    
    private final Typing typing;
    private final Logger logger;
    private final TypeMirror completable;
    
    
    Binder(Typing typing, Logger logger) {
        this.typing = typing;
        this.logger = logger;
        this.completable = typing.specialize(CompletableFuture.class, Suggestions.class);
    }
    
    @Override
    public @Nullable Method visitExecutable(ExecutableElement element, Identifier identifier) {
        var type = infer(element.getReturnType());
        if (type == null) {
            // Log error
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
            return Member.Type.SUGGESTIONS;
            
        } else {
            return null;
        }
    }
    
    @Override
    public Field visitVariable(VariableElement element, Identifier identifier) {
        var type = element.asType();
    }
    
}
