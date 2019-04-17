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
package com.karuslabs.commons.command.annotations.assembler;

import com.karuslabs.commons.command.*;
import com.karuslabs.commons.command.annotations.*;
import com.karuslabs.commons.util.collections.TokenMap;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.suggestion.SuggestionProvider;
import com.mojang.brigadier.tree.*;
import java.lang.invoke.*;
import java.lang.invoke.MethodHandles.Lookup;
import java.lang.reflect.Method;
import java.util.*;


public class Assembler<T> {
    
    static final MethodType COMMAND_SIGNATURE = MethodType.methodType(int.class, CommandContext.class);
    static final Class<?>[] COMMAND_PARAMETERS = new Class<?>[] {CommandContext.class};
    
    static final MethodType EXECUTABLE_SIGNATURE = MethodType.methodType(void.class, DefaultableContext.class);
    static final Class<?>[] EXECUTABLE_PARAMETERS = new Class<?>[] {DefaultableContext.class};
    
    
    private CommandAssembler assembler;
    private Lookup lookup;
    private TokenMap<String, Object> bindings;
    
    
    public Assembler(CommandAssembler assembler) {
        this.assembler = assembler;
        lookup = MethodHandles.lookup();
        bindings = TokenMap.of();
    }
    
    
    public Map<String, CommandNode<T>> assemble(Object object) {
        try {
            var root = new RootCommandNode<T>();
            
            bind(object);
            assembler.assemble(object, root, object.getClass().getAnnotationsByType(Literal.class), null);
            assembler.assemble(object, bindings, root, object.getClass().getAnnotationsByType(Argument.class), null);
            generate(object, root);
            
            return Commands.children(root);
            
        } catch (IllegalArgumentException | IllegalAccessException e) {
            throw new IllegalStateException(e);
            
        } finally {
            bindings.map().clear();
        }
    }
    
    protected void bind(Object object) throws IllegalArgumentException, IllegalAccessException {
        for (var field : object.getClass().getDeclaredFields()) {
            field.setAccessible(true);
            
            var annotation = field.getAnnotation(Bind.class);
            if (annotation == null) {
                continue;
            }
            
            var type = field.getType();
            Object replaced;
            
            if (type.isAssignableFrom(ArgumentType.class)) {
                replaced = bindings.put(annotation.value(), ArgumentType.class, (ArgumentType) field.get(object));
                
            } else if (type.isAssignableFrom(SuggestionProvider.class)) {
                replaced = bindings.put(annotation.value(), SuggestionProvider.class, (SuggestionProvider) field.get(object));
                
            } else {
                throw new IllegalArgumentException("@Bind(" + annotation.value() + ") annotation cannot be applied to " + field.getName() + 
                                                   ", field must be either an ArgumentType or SuggestionProvider");
            }
            
            if (replaced != null) {
                throw new IllegalArgumentException("Binding of the same type already exist for: " + annotation.value());
            }
        }
    }
    
    protected void generate(Object object, RootCommandNode<T> root) {
        try {
            for (var method : object.getClass().getDeclaredMethods()) {
                method.setAccessible(true);

                var literals = method.getAnnotationsByType(Literal.class);
                var arguments = method.getAnnotationsByType(Argument.class);
                if (literals.length == 0 && arguments.length == 0) {
                    continue;
                }

                Command<T> command;
                if (method.getReturnType() == int.class && Arrays.equals(method.getParameterTypes(), COMMAND_PARAMETERS)) {
                    command = emit(object, method, COMMAND_SIGNATURE, Command.class, "run");

                } else if (method.getReturnType() == void.class && Arrays.equals(method.getParameterTypes(), EXECUTABLE_PARAMETERS)) {
                    command = emit(object, method, EXECUTABLE_SIGNATURE, Executable.class, "execute");

                } else {
                    throw new IllegalArgumentException("Method: " + method.getName() + " must have same return and parameter types as Command or Executable");
                }
                
                assembler.assemble(object, root, literals, command);
                assembler.assemble(object, bindings, root, arguments, command);
            }

        } catch (Throwable e) {
            throw new RuntimeException("Failed to generate lambda from " + object.getClass(), e);
        }
    }
    
    protected Command<T> emit(Object object, Method method, MethodType signature, Class<?> target, String targetMethod) throws Throwable {
        var handle = lookup.unreflect(method);
        var conversion = MethodType.methodType(target, object.getClass());
        var lambda = LambdaMetafactory.metafactory(lookup, targetMethod, conversion, signature, handle, signature).getTarget();

        return (Command<T>) lambda.invoke(object);
    }
    
}
