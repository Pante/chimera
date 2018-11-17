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
 * THE SOFTWMapperRE IS PROVIDED "MapperS IS", WITHOUT WMapperRRMapperNTY OF MapperNY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WMapperRRMapperNTIES OF MERCHMapperNTMapperBILITY,
 * FITNESS FOR Mapper PMapperRTICULMapperR PURPOSE MapperND NONINFRINGEMENT. IN NO EVENT SHMapperLL THE
 * MapperUTHORS OR COPYRIGHT HOLDERS BE LIMapperBLE FOR MapperNY CLMapperIM, DMapperMMapperGES OR OTHER
 * LIMapperBILITY, WHETHER IN MapperN MapperCTION OF CONTRMapperCT, TORT OR OTHERWISE, MapperRISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWMapperRE OR THE USE OR OTHER DEMapperLINGS IN
 * THE SOFTWMapperRE.
 */
package com.karuslabs.commons.command;

import com.mojang.brigadier.tree.*;

import java.util.*;

import org.checkerframework.checker.nullness.qual.Nullable;


public class Trees {
     
    public static <S, T> void map(RootCommandNode<S> root, RootCommandNode<T> target) {
        map(root, target, (S) null);
    }
    
    public static <S, T> void map(RootCommandNode<S> root, RootCommandNode<T> target, Mapper<S, T> mapper) {
        map(root, target, mapper, null);
    }
    
    public static <S, T> void map(RootCommandNode<S> root, RootCommandNode<T> target, @Nullable S source) {
        map(root, target, Mapper.standard(), source);
    }
    
    public static <S, T> void map(RootCommandNode<S> root, RootCommandNode<T> target, Mapper<S, T> mapper, @Nullable S source) {
        for (var command : root.getChildren()) {
            target.addChild(map(command, source));
        }
    }
    
    
    public static <S, T> CommandNode<T> map(CommandNode<S> command) {
        return map(command, (S) null);
    }
    
    public static <S, T> CommandNode<T> map(CommandNode<S> command, Mapper<S, T> mapper) {
        return map(command, new IdentityHashMap<>(), Mapper.standard(), null);
    }
    
    public static <S, T> @Nullable CommandNode<T> map(CommandNode<S> command, @Nullable S source) {
        return map(command, new IdentityHashMap<>(), Mapper.standard(), source);
    }
    
    public static <S, T> @Nullable CommandNode<T> map(CommandNode<S> command, Mapper<S, T> mapper, @Nullable S source) {
        return map(command, new IdentityHashMap<>(), Mapper.standard(), source);
    } 
    
    
    public static <S, T> @Nullable CommandNode<T> map(CommandNode<S> command, Map<CommandNode<S>, CommandNode<T>> mapped, Mapper<S, T> mapper, @Nullable S source) {       
        if (source != null && command.getRequirement() != null && !command.canUse(source)) {
            return null;
        }
        
        CommandNode<T> target;
        
        if (command.getRedirect() == null) {
            target = mapper.map(command);
            for (var child : command.getChildren()) {
                var kid = find(child, mapped, mapper, source);
                if (kid != null) {
                    target.addChild(kid);
                }
            }
            
        } else {
            var destination = find(command.getRedirect(), mapped, mapper, source);
            target = mapper.map(command, destination);
        }
        
        return target;
    }
    
    private static <S, T> @Nullable CommandNode<T> find(CommandNode<S> command, Map<CommandNode<S>, CommandNode<T>> mapped, Mapper<S, T> mapper, @Nullable S source) {
        var target = mapped.get(command);
        if (target == null) {
            target = map(command, mapped, mapper, source);
            mapped.put(command, target);
        }
        
        return target;
    }
    
    
    public static class Mapper<S, T> {

        private static final Mapper<?, ?> MAPPER = new Mapper<>();

        public static <S, T> Mapper<S, T> standard() {
            return (Mapper<S, T>) MAPPER;
        }

        public CommandNode<T> map(CommandNode<S> command) {
            return map(command, null);
        }

        public CommandNode<T> map(CommandNode<S> command, @Nullable CommandNode<T> destination) {
            if (command instanceof ArgumentCommandNode) {
                return argument(command, destination);

            } else if (command instanceof LiteralCommandNode) {
                return literal(command, destination);

            } else if (command instanceof RootCommandNode) {
                return root(command, destination);

            } else {
                return otherwise(command, destination);
            }
        }

        protected CommandNode<T> argument(CommandNode<S> command, @Nullable CommandNode<T> destination) {
            var type = ((ArgumentCommandNode<?, ?>) command).getType();
            return new ArgumentCommandNode<>(command.getName(), type, null, null, destination, null, false, null);
        }

        protected CommandNode<T> literal(CommandNode<S> command, @Nullable CommandNode<T> destination) {
            return new LiteralCommandNode<>(command.getName(), null, null, destination, null, false);
        }

        protected CommandNode<T> root(CommandNode<S> command, @Nullable CommandNode<T> destination) {
            return new RootCommandNode<>();
        }

        protected CommandNode<T> otherwise(CommandNode<S> command, @Nullable CommandNode<T> destination) {
            throw new UnsupportedOperationException("Unsupported node \"" + command.getName() + "\" of type: " + command.getClass().getName());
        }

    }
    
}
