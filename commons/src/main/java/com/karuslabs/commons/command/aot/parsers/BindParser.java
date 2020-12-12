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

import com.karuslabs.commons.command.aot.*;
import com.karuslabs.commons.command.aot.Binding.*;
import com.karuslabs.commons.command.aot.annotations.Bind;
import com.karuslabs.commons.command.aot.lexers.Lexer;
import com.karuslabs.puff.*;
import com.karuslabs.puff.type.*;

import java.util.*;
import javax.lang.model.element.*;
import javax.lang.model.util.SimpleElementVisitor9;

import org.checkerframework.checker.nullness.qual.Nullable;

public class BindParser extends NamespaceParser {

    private final Binder namespaced;
    private final Binder pattern;
    
    public BindParser(Binder namespaced, Binder pattern, Logger logger, Lexer lexer) {
        super(logger, lexer);
        this.namespaced = namespaced;
        this.pattern = pattern;
    }

    @Override
    public void parse(Element element, Environment environment) {
        var namespace = environment.namespace(element);
        if (namespace.isEmpty()) {
            logger.error(element.accept(Find.TYPE, null), "Class should be annotated with @Command");
            return;
        }
        
        var namespaces = element.getAnnotation(Bind.class).value();
        var patterns = element.getAnnotation(Bind.class).pattern();
        
        if (namespaces.length == 0 && patterns.length == 0) {
            logger.error(element, "@Bind should not be empty");
            return;
        }
        
        namespaced.bind(lexer, element, namespace, namespaces);
        pattern.bind(lexer, element, namespace, patterns);
    }
    
    
    public static class NamespaceBinder extends Binder {

        public NamespaceBinder(TypeMirrors utils, KnownTypes types, Logger logger) {
            super(utils, types, logger);
        }

        @Override
        protected void bind(List<Token> tokens, Binding<?> binding, Element element, Map<Identity, Command> namespace) {
            Command command = null;
            for (var token : tokens) {
                command = namespace.get(token.identity);
                if (command == null) {
                    logger.error(element, Texts.join(tokens, (t, builder) -> builder.append(t.identity), " "), " does not exist");
                    return;
                }
                
                namespace = command.children;
            }
            
            command.bindings.put(element, binding);
        }
        
    }
    
    public static class PatternBinder extends Binder {

        public PatternBinder(TypeMirrors utils, KnownTypes types, Logger logger) {
            super(utils, types, logger);
        }

        @Override
        protected void bind(List<Token> tokens, Binding<?> binding, Element element, Map<Identity, Command> namespace) {
            if (!match(tokens, 0, binding, namespace)) {
                logger.warn(element, "Pattern: \"" + Texts.join(tokens, (t, builder) -> builder.append(t.identity), " ") + "\"", "does not exist");
            }
        }
        
        boolean match(List<Token> tokens, int index, Binding<?> binding, Map<Identity, Command> namespace) {
            var match = false;
            for (var command : namespace.values()) {
                match |= match(tokens, index, binding, command.children);
                if (!tokens.get(index).identity.equals(command.identity)) {
                    continue;
                }

                if (index == tokens.size() - 1) {
                    command.bindings.put(binding.site, binding);
                    match = true;

                } else {
                    match |= match(tokens, index + 1, binding, namespace);
                }
            }

            return match;
        }
 
        
    }
    
    public static abstract class Binder extends SimpleElementVisitor9<Binding<?>, Token> {
        
        private final TypeMirrors utils;
        private final KnownTypes types;
        protected final Logger logger;
        
        public Binder(TypeMirrors utils, KnownTypes types, Logger logger) {
            this.utils = utils;
            this.types = types;
            this.logger = logger;
        }
        
        public void bind(Lexer lexer, Element element, Map<Identity, Command> namespace, String[] commands) {
            for (var command : commands) {
                var tokens = lexer.lex(logger, element, command);
                if (tokens.isEmpty()) {
                    continue;
                }

                var binding = element.accept(this, tokens.get(tokens.size() - 1));
                if (binding == null) {
                    continue;
                }
                
                bind(tokens, binding, element, namespace);
            }
        }
        
        protected abstract void bind(List<Token> tokens, Binding<?> binding, Element element, Map<Identity, Command> namespace);
        
        
        @Override
        public @Nullable Method visitExecutable(ExecutableElement element, Token token) {
            var pattern = Method.pattern(utils, types, element.getReturnType());
            if (pattern != null) {
                return new Method(token.identity, element, pattern);
                
            } else {
                logger.error(element, element.getSimpleName(), "has an invalid return type", "should return an int, void, boolean or CompletableFuture<Suggestions>");
                return null;
            }
        }
        
        @Override
        public @Nullable Field visitVariable(VariableElement element, Token token) {
            var pattern = Field.pattern(utils, types, element.asType());
            if (pattern != null) {
                return new Field(token.identity, element, pattern);
                
            } else {
                logger.error(element, element.getSimpleName(), "has an invalid type", "should be an ArgumentType<?>, Command<CommandSender>, Predicate<CommandSender>, SuggestionProvider<CommandSender>");
                return null;
            }
        }
        
        @Override
        public @Nullable Binding<?> defaultAction(Element element, Token token) {
            logger.error(element, token.lexeme, "is used on an invalid target", "should annotate either a field or method");
            return null;
        }
        
    }

}
