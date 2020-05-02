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

import com.karuslabs.commons.command.OptionalContext;
import com.karuslabs.commons.command.aot.*;

import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import java.util.List;

import javax.lang.model.element.*;
import javax.lang.model.type.*;

import org.bukkit.command.CommandSender;

import static javax.lang.model.element.Modifier.*;


public class MethodResolver extends Resolver<ExecutableElement> {

    TypeMirror sender;
    TypeMirror context;
    TypeMirror defaultable;
    TypeMirror exception;
    
    
    public MethodResolver(Environment environment) {
        super(environment);
        var elements = environment.elements;
        var types = environment.types;
        
        sender = elements.getTypeElement(CommandSender.class.getName()).asType();
        context = types.getDeclaredType(elements.getTypeElement(CommandContext.class.getName()), sender);
        defaultable = types.getDeclaredType(elements.getTypeElement(OptionalContext.class.getName()), sender);
        exception = elements.getTypeElement(CommandSyntaxException.class.getName()).asType();
    }

    
    @Override
    public void resolve(Token token, ExecutableElement method, Token binding) {
        var modifiers = method.getModifiers();
        if (!modifiers.contains(PUBLIC) || modifiers.contains(STATIC)) {
            environment.error(method, "Method must be public and non-static");
            return;
        }
        
        if (command(method.getReturnType(), method.getParameters()) && exceptions(method.getThrownTypes())) {
            token.bind(environment, Binding.EXECUTION, binding);

        } else if (predicate(method.getReturnType(), method.getParameters())) {
            token.bind(environment, Binding.REQUIREMENT, binding);
            
        } else {
            environment.error(method, "Signature must match either Command<CommandSender> or Executable<CommandSender>");
        }
    }

    
    boolean command(TypeMirror type, List<? extends VariableElement> parameters) {
        if (parameters.size() != 1) {
            return false;
        }
        
        var types = environment.types;
        
        var returnable = type.getKind();
        var parameter = parameters.get(0).asType();
        return (returnable == TypeKind.INT && types.isSameType(context, parameter))
            || (returnable == TypeKind.VOID && types.isSameType(defaultable, parameter));
    }
    
    boolean exceptions(List<? extends TypeMirror> thrown) {
        return thrown.isEmpty() || (thrown.size() == 1 && environment.types.isSubtype(thrown.get(0), exception));
    }
    
    
    boolean predicate(TypeMirror type, List<? extends VariableElement> parameters) {
        if (parameters.size() != 1) {
            return false;
        }
        
        var types = environment.types;
        return type.getKind() == TypeKind.BOOLEAN && types.isSameType(sender, parameters.get(0).asType());
    }
    
}
