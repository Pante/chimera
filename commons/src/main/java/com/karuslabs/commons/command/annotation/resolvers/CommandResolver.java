/*
 * The MIT License
 *
 * Copyright 2018 Karus Labs.
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
package com.karuslabs.commons.command.annotation.resolvers;

import com.karuslabs.commons.command.*;
import com.karuslabs.commons.command.annotation.Namespace;
import com.karuslabs.commons.command.arguments.Arguments;

import java.lang.invoke.*;
import java.lang.invoke.MethodHandles.Lookup;
import java.lang.reflect.*;
import java.util.*;
import java.util.stream.Stream;

import org.bukkit.plugin.Plugin;



public class CommandResolver {
    
    static final Class<?>[] PARAMETERS = new Class<?>[] {CommandSource.class, Context.class, Arguments.class};
    
    
    Plugin plugin;
    Set<Resolver> resolvers;
    Lookup lookup;
    
    
    public CommandResolver(Plugin plugin, Set<Resolver> resolvers) {
        this.plugin = plugin;
        this.resolvers = resolvers;
        this.lookup = MethodHandles.lookup();
    }
    
    
    public void resolve(ProxiedCommandMap map, CommandExecutors executors) {
        for (Method method : executors.getClass().getMethods()) {
            if (method.getAnnotationsByType(Namespace.class).length == 0) {
                continue;
            }
            
            if (method.getReturnType() == boolean.class && Arrays.equals(method.getParameterTypes(), PARAMETERS)) {
                resolve(map, method, generate(executors, method));
                
            } else {
                throw new IllegalArgumentException("Invalid signature for method: " + method.getName() + " in " + executors.getClass().getSimpleName() + ", method must match " + CommandExecutor.class.getSimpleName());
            }
        }
    }
    
    protected CommandExecutor generate(CommandExecutors executors, Method reflective) {
        try {
            MethodHandle method = lookup.unreflect(reflective);
            MethodType type = MethodType.methodType(CommandExecutor.class, executors.getClass());
            MethodType signature = MethodType.methodType(reflective.getReturnType(), reflective.getParameterTypes());
            MethodHandle lambda = LambdaMetafactory.metafactory(lookup, "execute", type, signature, method, signature).getTarget();
            
            return (CommandExecutor) lambda.invoke(executors);
            
        } catch (Throwable e) {
            throw new IllegalArgumentException("Failed to generate CommandExecutor from method: " + reflective.getName() + " in " + executors.getClass().getSimpleName(), e);
        }
    }
    
    
    public void resolve(ProxiedCommandMap map, AnnotatedElement element, CommandExecutor executor) {
        if (element.getAnnotationsByType(Namespace.class).length > 0) {
            resolve(element, executor, find(map, element, executor));
            
        } else {
            throw new IllegalArgumentException("Unresolvable annotations for CommandExecutor: " + element + ", CommandExecutor must contain at least one namespace");
        }
    }
    
    public void resolve(ProxiedCommandMap map, AnnotatedElement element, CommandExecutor executor, String... namespace) {
        resolve(element, executor, find(map, executor, namespace));
    }

    public void resolve(AnnotatedElement element, CommandExecutor executor, Command... commands) {
        for (Resolver resolver : resolvers) {
            if (resolver.isResolvable(element)) {
                resolver.resolve(element, executor, commands);
            }
        }

        for (Command command : commands) {
            command.setExecutor(executor);
        }
    }
    
    
    protected Command[] find(ProxiedCommandMap map,  AnnotatedElement element, CommandExecutor executor) {
        Namespace[] namespaces = element.getAnnotationsByType(Namespace.class);
        return Stream.of(namespaces).map(namespace -> find(map , element, executor, namespace)).toArray(Command[]::new);
    } 
    
    protected Command find(ProxiedCommandMap map,  AnnotatedElement element, CommandExecutor executor, Namespace namespace) {
        String[] names = namespace.value();
        if (names.length > 0) {
            return find(map, executor, names);
            
        } else {
            throw new IllegalArgumentException("Invalid namespace for: " + element + ", namespace cannot be empty");
        }
    }
    
    protected Command find(ProxiedCommandMap map, CommandExecutor executor, String[] names) {
        Command command = map.getCommand(names[0]);
        if (command == null) {
            map.register(names[0], command = new Command(names[0], plugin));
        }
        
        for (int i = 1; i < names.length; i++) {
            command = append(command, names[i]);
        }
        
        return command;
    }
    
    protected Command append(Command parent, String name) {
        Command command = parent.getSubcommands().get(name);
        if (command == null) {
            parent.getSubcommands().put(name, command = new Command(name, plugin));
        }
        
        return command;
    } 
    
    
    public static CommandResolver simple(Plugin plugin, References references) {
        Set<Resolver> resolvers = Set.of(new InformationResolver(), new LiteralResolver(), new RegisteredResolver(references), new ResourceResolver());
        return new CommandResolver(plugin, resolvers);
    }
    
}
