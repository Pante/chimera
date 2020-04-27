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

import com.karuslabs.commons.command.OptionalContext;
import com.karuslabs.commons.command.aot.*;

import org.bukkit.command.CommandSender;

import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;

import java.util.List;
import javax.lang.model.element.*;
import javax.lang.model.type.*;
import javax.lang.model.util.SimpleElementVisitor9;

import static com.karuslabs.commons.command.aot.Messages.reason;


public class MethodVisitor extends SimpleElementVisitor9<Void, IR> {

    private Environment environment;
    TypeMirror context;
    TypeMirror defaultable;
    TypeMirror exception;
    
    
    public MethodVisitor(Environment environment) {
        this.environment = environment;
        
        var elements = environment.elements;
        var types = environment.types;
        var sender = elements.getTypeElement(CommandSender.class.getName()).asType();
        
        context = types.getDeclaredType(elements.getTypeElement(CommandContext.class.getName()), sender);
        defaultable = types.getDeclaredType(elements.getTypeElement(OptionalContext.class.getName()), sender);
        exception = elements.getTypeElement(CommandSyntaxException.class.getName()).asType();
    }
    
    
    @Override
    public Void visitExecutable(ExecutableElement method, IR ir) {
        var modifiers = method.getModifiers();
        if (!modifiers.contains(Modifier.PUBLIC) || modifiers.contains(Modifier.STATIC)) {
            environment.error("Invalid method: '" + method.getSimpleName() + "', method must be public and non-static", method);
            return null;
        }

        var parameters = method.getParameters();
        if (!signature(method.getReturnType(), parameters) || !exceptions(method.getThrownTypes())) {
            environment.error("Invalid method: " + method.getSimpleName() + ", method signature must match either Command<CommandSender> or Executable<CommandSender>", method);
            return null;
        }
        
        if (!ir.execution(method)) {
            environment.error(reason("Invalid binding", ir.bindings.get(method), "binding already exists"), method);
        }
        
        return null;
    }
    
    
    boolean signature(TypeMirror type, List<? extends VariableElement> parameters) {
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
    
}
