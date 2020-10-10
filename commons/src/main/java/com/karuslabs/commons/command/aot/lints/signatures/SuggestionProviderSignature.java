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
package com.karuslabs.commons.command.aot.lints.signatures;

import com.karuslabs.annotations.processor.*;
import com.karuslabs.commons.command.aot.Mirrors.Method;

import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;

import javax.lang.model.element.VariableElement;
import javax.lang.model.type.TypeMirror;

import org.bukkit.command.CommandSender;

public class SuggestionProviderSignature extends ContextualSignature {

    private final TypeMirror context;
    private final TypeMirror builder;
    boolean contextDeclared;
    boolean builderDeclared;
    
    public SuggestionProviderSignature(Typing typing) {
        super(typing);
        context = typing.specialize(CommandContext.class, CommandSender.class);
        builder = typing.type(SuggestionsBuilder.class);
        contextDeclared = false;
        builderDeclared = false;
    }

    @Override
    protected void lint(Logger logger, Method method, VariableElement parameter) {
        if (typing.types.isSubtype(context, parameter.asType())) {
            if (contextDeclared) {
                logger.error("Only one CommadContext<CommandSender> should be declared");
                
            } else {
                contextDeclared = true;
            }
            
        } else if (typing.types.isSubtype(builder, parameter.asType())) {
            if (builderDeclared) {
                logger.error("Only one SuggestionsBuilder should be declared");
                
            } else {
                builderDeclared = true;
            }
            
        } else {
            logger.error("Parameter should be a CommadContext<CommandSender>, SuggestionsBuilder or be annotated with @Infer");
        }
    }
    
    @Override
    protected void clear() {
        contextDeclared = false;
        builderDeclared = false;
    }

}
