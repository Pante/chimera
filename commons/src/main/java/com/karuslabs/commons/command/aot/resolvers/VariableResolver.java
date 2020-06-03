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

import java.util.function.Predicate;
import javax.lang.model.element.*;
import javax.lang.model.type.TypeMirror;

import static com.karuslabs.commons.command.aot.Binding.*;
import static com.karuslabs.annotations.processor.Messages.quote;
import static javax.lang.model.element.Modifier.*;


/**
 * A {@code Resolver} that checks the signature of a variable and if valid, binds 
 * the variable to a token.
 */
public class VariableResolver extends Resolver<VariableElement> {

    TypeMirror command;
    TypeMirror argumentType;
    TypeMirror requirement;
    TypeMirror suggestions;
    
    
    /**
     * Creates a {@code VariableResolver} with the environment.
     * 
     * @param environment the environment
     */
    public VariableResolver(Environment environment) {
        super(environment);
        command = specialize(Command.class, sender);
        argumentType = types.erasure(elements.getTypeElement(ArgumentType.class.getName()).asType());
        requirement = specialize(Predicate.class, sender);
        suggestions = specialize(SuggestionProvider.class, sender);
    }

    
    /**
     * Checks the signature of the given variable and if valid, binds it to {@code token}.
     * <br><br>
     * <b>Implementation details:</b><br><br>
     * A variable is considered to be valid if it is public, final and type matches
     * {@code Command<CommandSender}, {@code Execution<CommandSender>}, {@code Predicate<CommandSender>} 
     * or {@code SuggestionProvider<CommandSender>}.
     * 
     * @param variable the variable
     * @param token the token
     */
    @Override
    public void resolve(VariableElement variable, Token token) {
        var modifiers = variable.getModifiers();
        if (!modifiers.contains(PUBLIC) || !modifiers.contains(FINAL)) {
            environment.error(variable, "Field should be public and final");
            return;
        }
        
        var type = variable.asType();
        if (types.isSubtype(type, command)) {
            token.bind(environment, COMMAND, variable);
            
        } else if (types.isSubtype(type, argumentType)) {
            token.bind(environment, TYPE, variable);
            
        } else if (types.isSubtype(type, requirement)) {
            token.bind(environment, REQUIREMENT, variable);
            
        } else if (types.isSubtype(type, suggestions)) {
            token.bind(environment, SUGGESTIONS, variable);
            
        } else {
            environment.error(variable, quote(variable) + " should be an ArgumentType<?>, Command<CommandSender>, Predicate<CommandSender> or SuggestionProvider<CommandSender>");
        }
    }

}
