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
package com.karuslabs.commons.command.aot.analyzer;

import com.karuslabs.commons.command.aot.*;
import com.karuslabs.commons.command.aot.Token.Type;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.suggestion.SuggestionProvider;

import java.util.function.Predicate;
import javax.lang.model.element.*;
import javax.lang.model.type.TypeMirror;
import javax.lang.model.util.*;

import org.bukkit.command.CommandSender;

import org.checkerframework.checker.nullness.qual.Nullable;

import static com.karuslabs.commons.command.aot.Messages.reason;


public class VariableVisitor extends SimpleElementVisitor9<Void, IR> {
    
    private Environment environment;
    Types types;
    TypeMirror command;
    TypeMirror suggestions;
    TypeMirror type;
    
    @Nullable VariableElement variable;
    @Nullable IR ir;
    
    
    public VariableVisitor(Environment environment) {
        this.environment = environment;
        this.types = environment.types;
        
        var elements = environment.elements;
        var sender = elements.getTypeElement(CommandSender.class.getName()).asType();
        
        command = types.getDeclaredType(elements.getTypeElement(Command.class.getName()), sender);
        suggestions = types.getDeclaredType(elements.getTypeElement(SuggestionProvider.class.getName()), sender);
        type = types.erasure(elements.getTypeElement(ArgumentType.class.getName()).asType());
    }
    
    
    @Override
    public Void visitVariable(VariableElement variable, IR ir) {
        var modifiers = variable.getModifiers();
        if (!modifiers.contains(Modifier.PUBLIC) || modifiers.contains(Modifier.STATIC)) {
            environment.error("Invalid field: '" + variable.getSimpleName() + "', field must be public and non-static", variable);
            return null;
        }
        
        this.variable = variable;
        this.ir = ir;
        
        if (types.isSubtype(variable.asType(), type)) {
            if (ir.declaration.type != Type.ARGUMENT) {
                environment.error("Invalid binding type: '" + variable.getSimpleName() + "', ArgumenType<T> can only be bound to arguments ", variable);
                
            } else if (!ir.type(variable)) {
                environment.error(reason("Invalid binding", ir.bindings.get(variable), "Binding for ArgumenType<T> already exists"), variable);
            }
            
        } else if (!match(command, ir::execution, "Command<CommandSender>") && !match(suggestions, ir::suggestions, "SuggestionProvider<Commandsender>")) {
            environment.error(reason("Invalid binding target", ir.bindings.get(variable), "ArgumenType<T> can only be bound to arguments "), variable);
        }
        
        return null;
    }
    
    boolean match(TypeMirror expected, Predicate<VariableElement> predicate, String type) {
        if (!types.isSubtype(variable.asType(), expected)) {
            return false;
        }
        
        if (!predicate.test(variable)) {
            environment.error(reason("Invalid binding", ir.bindings.get(variable), "binding for " + type + "  already exists"), variable);
        }
        
        return true;
    }
    
}
