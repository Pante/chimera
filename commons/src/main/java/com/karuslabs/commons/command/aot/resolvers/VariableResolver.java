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
package com.karuslabs.commons.command.aot.resolvers;

import com.karuslabs.commons.command.aot.*;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.suggestion.SuggestionProvider;

import java.util.Set;
import java.util.function.Predicate;
import javax.lang.model.element.*;
import javax.lang.model.type.TypeMirror;
import javax.lang.model.util.Types;

import org.bukkit.command.CommandSender;

import static javax.lang.model.element.Modifier.*;


public class VariableResolver extends Resolver<VariableElement> {
    
    static final Set<Modifier> MODIFIERS = Set.of(PUBLIC, FINAL);

    Types types;
    TypeMirror command;
    TypeMirror argumentType;
    TypeMirror requirement;
    TypeMirror suggestions;
    
    
    public VariableResolver(Environment environment) {
        super(environment);
        this.types = environment.types;
        
        var elements = environment.elements;
        var sender = elements.getTypeElement(CommandSender.class.getName()).asType();
        
        command = types.getDeclaredType(elements.getTypeElement(Command.class.getName()), sender);
        argumentType = types.erasure(elements.getTypeElement(ArgumentType.class.getName()).asType());
        requirement = types.getDeclaredType(elements.getTypeElement(Predicate.class.getName()), sender);
        suggestions = types.getDeclaredType(elements.getTypeElement(SuggestionProvider.class.getName()), sender);
    }

    
    @Override
    public void resolve(Token token, VariableElement variable, Token binding) {
        var modifiers = variable.getModifiers();
        if (!modifiers.containsAll(MODIFIERS) || modifiers.contains(STATIC)) {
            environment.error(variable, "Field should be public, final and non-static");
            return;
        }
        
        var type = variable.asType();
        if (types.isSubtype(type, command)) {
            token.bind(environment, Binding.EXECUTION, binding);
            
        } else if (types.isSubtype(type, argumentType)) {
            token.bind(environment, Binding.TYPE, binding);
            
        } else if (types.isSubtype(type, requirement)) {
            token.bind(environment, Binding.REQUIREMENT, binding);
            
        } else if (types.isSubtype(type, suggestions)) {
            token.bind(environment, Binding.SUGGESTIONS, binding);
            
        } else {
            environment.error(variable, variable.asType() + " should be a ArgumentType<?>, Command<CommandSender>, Predicate<CommandSender> or SuggestionProvider<CommandSender>");
        }
    }

}
