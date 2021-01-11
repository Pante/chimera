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
import com.karuslabs.commons.command.dispatcher.Dispatcher;
import com.karuslabs.commons.command.tree.nodes.Literal;
import com.karuslabs.Satisfactory.*;
import com.karuslabs.Satisfactory.type.Find;

import java.lang.annotation.Annotation;
import java.util.*;
import javax.lang.model.element.*;
import javax.lang.model.util.SimpleElementVisitor9;

import org.checkerframework.checker.nullness.qual.Nullable;

public class BindParser extends LexParser {
    
    private final Visitor visitor;
    private final Binder namespaced = new NamespaceBinder();
    private final Binder pattern = new PatternBinder();
    
    public BindParser(Visitor visitor, Logger logger, Lexer lexer) {
        super(logger, lexer);
        this.visitor = visitor;
    }

    @Override
    public void parse(Environment environment, Element element) {
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
        
        var binding = element.accept(visitor, logger);
        if (binding == null) {
            return;
        }
        
        namespaced.bind(environment, binding, namespaces);
        pattern.bind(environment, binding, patterns);
    }
    
    @Override
    public Class<? extends Annotation> annotation() {
        return Bind.class;
    }
    
    
    abstract class Binder {
        void bind(Environment environment, Binding<?> binding, String[] commands) {
            var namespace = environment.namespace(binding.site);
            for (var command : commands) {
                var tokens = lexer.lex(logger, binding.site, command);
                if (tokens.isEmpty()) {
                    continue;
                }
                
                bind(environment, namespace, tokens, binding);
            }
        }
        
        abstract void bind(Environment environment, Map<Identity, Command> commands, List<Token> tokens, Binding<?> binding);
    }
    
    class NamespaceBinder extends Binder {
        @Override
        void bind(Environment environment, Map<Identity, Command> commands, List<Token> tokens, Binding<?> binding) {
            Command command = null;
            for (var token : tokens) {
                command = commands.get(token.identity);
                if (command == null) {
                    logger.error(binding.site, Texts.join(tokens, (t, builder) -> builder.append(t.identity), " "), " does not exist");
                    return;
                }
                
                commands = command.children;
            }
            
            command.bind(binding);
            environment.method(binding.site).add(command);
        } 
    }
    
    class PatternBinder extends Binder {
        
        private final Set<Command> matches = new HashSet<>();
        
        @Override
        void bind(Environment environment, Map<Identity, Command> commands, List<Token> tokens, Binding<?> binding) {
            match(environment, commands, tokens, 0, binding);
            if (matches.isEmpty()) {
                logger.error(binding.site, "Pattern: " + Texts.quote(Texts.join(tokens, (t, builder) -> builder.append(t.identity), " ")), "does not exist");
                
            } else {
                matches.clear();
            }
        }
        
        void match(Environment environment, Map<Identity, Command> commands, List<Token> tokens, int index, Binding<?> binding) {
            for (var command : commands.values()) {
                if (!matches.contains(command) && command.identity.equals(tokens.get(index).identity)) {
                    if (index < tokens.size() - 1) {
                        match(environment, command.children, tokens, index + 1, binding);
                        
                    } else {
                        command.bind(binding);
                        environment.method(binding.site).add(command);
                        
                        var match = command;
                        for (int i = 0; i < tokens.size(); i++) {
                            matches.add(match);
                            match = match.parent;
                        }
                    }
                }
                
                // Cannot be re-ordered since we rely on matching a sequence first
                match(environment, command.children, tokens, 0, binding);
            }
        }
    }
    
    
    public static class Visitor extends SimpleElementVisitor9<Binding<?>, Logger> {
        
        private final Types types;
        
        public Visitor(Types types) {
            this.types = types;
        }
        
        @Override
        public @Nullable Method visitExecutable(ExecutableElement element, Logger logger) {
            var method = Method.capture(types, element);
            if (method == null) {
                logger.error(element, "Method: " + element.getSimpleName() + " returns an invalid type, should be an int, void, boolean or CompletableFuture<Suggestions>");
            }
            return method;
        }
        
        @Override
        public @Nullable Field visitVariable(VariableElement element, Logger logger) {
            var field = Field.capture(types, element);
            if (field == null) {
                logger.error(element, "Method: " + element.getSimpleName() + " returns an invalid type, should be an ArgumentType<?>, Command<CommandSender>, Predicate<CommandSender>, SuggestionProvider<CommandSender>");
            }
            return field;
        }
        
        @Override
        public @Nullable Binding<?> defaultAction(Element element, Logger logger) {
            logger.error(element, "@Bind is used on an invalid target, should annotate a field or method");
            return null;
        }
        
    }

}
