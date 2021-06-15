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
package com.karuslabs.typist.parsers;

import com.karuslabs.typist.annotations.Bind;
import com.karuslabs.typist.*;
import com.karuslabs.typist.Binding.*;
import com.karuslabs.typist.lexers.Lexer;
import com.karuslabs.utilitary.*;
import com.karuslabs.utilitary.type.Find;

import java.lang.annotation.Annotation;
import java.util.*;
import javax.lang.model.element.*;
import javax.lang.model.util.SimpleElementVisitor9;

import org.checkerframework.checker.nullness.qual.Nullable;

/**
 * A {@code Parser} that binds a field or method annotated with {@code @Bind} to 
 * the commands declared in the annotation. The annotated field or method may be
 * bound to multiple commands and commands that matches a given pattern. 
 * 
 * A pattern is defined as a sequence. of arguments and literals. A binding pattern
 * may overlap. For example, the pattern, {@code <a> <a>}, will be matched twice
 * in {@code <a> <a> <a>}; once for {@code (<a> <a>) <a>} and another for {@code <a> (<a> <a>)}.
 */
public class BindParser extends LexParser {
    
    private final Captor captor;
    private final Binder namespaceBinder = new NamespaceBinder();
    private final Binder patternBinder = new PatternBinder();
    
    public BindParser(Captor captor, Logger logger, Lexer lexer) {
        super(logger, lexer);
        this.captor = captor;
    }

    /**
     * Binds a field or method annotated with {@code @Bind} to the commands declared 
     * in the annotation.
     * 
     * @param environment the environment
     * @param element the annotated element
     */
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
        
        var binding = element.accept(captor, logger);
        if (binding == null) {
            return;
        }
        
        namespaceBinder.bind(environment, binding, namespaces);
        patternBinder.bind(environment, binding, patterns);
    }
    
    @Override
    public Class<? extends Annotation> annotation() {
        return Bind.class;
    }
    
    /**
     * A {@code Binder} finds the commands to which a binding should be bound. The 
     * binding is then subsequently bound to the commands found and stored in a given
     * environment.
     */
    abstract class Binder {
        /**
         * Binds the given binding to the commands in the same namespace that match 
         * the given lines.
         * 
         * @param environment the environment
         * @param binding the binding
         * @param lines the lines
         */
        void bind(Environment environment, Binding<?> binding, String[] lines) {
            var namespace = environment.namespace(binding.site());
            for (var line : lines) {
                var tokens = lexer.lex(logger, binding.site(), line);
                if (tokens.isEmpty()) {
                    continue;
                }
                
                bind(environment, namespace, tokens, binding);
            }
        }
        
        /**
         * Finds the commands in the given namespace that match the given tokens.
         * The given binding is then bound to the commands.
         * 
         * @param environment the environment
         * @param commands the namespace
         * @param tokens the tokens
         * @param binding the binding
         */
        abstract void bind(Environment environment, Map<Identity, Command> namespace, List<Token> tokens, Binding<?> binding);
    }
    
    /**
     * A {@code Binder} that binds a given binding to commands that exactly match 
     * the given sequence of token.
     */
    class NamespaceBinder extends Binder {
        @Override
        void bind(Environment environment, Map<Identity, Command> commands, List<Token> tokens, Binding<?> binding) {
            Command command = null;
            for (var token : tokens) {
                command = commands.get(token.identity());
                if (command == null) {
                    logger.error(binding.site(), Texts.quote(Texts.join(tokens, (t, builder) -> builder.append(t.identity()), " ")), "does not exist");
                    return;
                }
                
                commands = command.children();
            }
            
            command.bind(binding);
            environment.method(binding.site()).add(command);
        } 
    }
    
    /**
     * A {@code Binder} that binds a given binding to commands that match the given
     * token pattern. Binding patterns may overlap. For example, the pattern, {@code <a> <a>}, 
     * will be matched twice in {@code <a> <a> <a>}; once for {@code (<a> <a>) <a>} 
     * and another for {@code <a> (<a> <a>)}.
     */
    class PatternBinder extends Binder {
        
        private final Set<Command> matches = new HashSet<>();
        
        @Override
        void bind(Environment environment, Map<Identity, Command> commands, List<Token> tokens, Binding<?> binding) {
            match(environment, commands, tokens, 0, binding);
            if (matches.isEmpty()) {
                logger.error(binding.site(), "Pattern: " + Texts.quote(Texts.join(tokens, (t, builder) -> builder.append(t.identity()), " ")), "does not exist");
                
            } else {
                matches.clear();
            }
        }
        
        /**
         * Recursively tests if a command is part of the given pattern. A binding
         * is then bound to the last command in a pattern.
         * 
         * @param environment the environment
         * @param commands the namespace
         * @param tokens the token pattern
         * @param index the index of the token in the given pattern to test against
         * @param binding the binding
         */
        void match(Environment environment, Map<Identity, Command> commands, List<Token> tokens, int index, Binding<?> binding) {
            for (var command : commands.values()) {
                if (!matches.contains(command) && command.identity().equals(tokens.get(index).identity())) {
                    if (index == tokens.size() - 1) {
                        command.bind(binding);
                        environment.method(binding.site()).add(command);
                        add(command, tokens.size());
                        
                    } else {
                        match(environment, command.children(), tokens, index + 1, binding);
                    }
                }
                
                // Cannot be re-ordered since we need to check if it's a sequence first
                match(environment, command.children(), tokens, 0, binding);
            }
        }
        
        /**
         * Adds all commands from the root to the given command as matches.
         * 
         * @param command the leaf command
         * @param length the pattern's length
         */
        void add(Command command, int length) {
            var current = command;
            for (int i = 0; i < length; i++) {
                matches.add(current);
                current = current.parent();
            }
        }
    }
    
    /**
     * A captor that transforms a field or method into a binding. May return {@code null}
     * if the binding's type cannot be inferred from the field or method's signature.
     */
    public static class Captor extends SimpleElementVisitor9<Binding<?>, Logger> {
        
        private final Types types;
        
        /**
         * Creates a {@code Captor} with the given {@code Types}.
         * 
         * @param types the types
         */
        public Captor(Types types) {
            this.types = types;
        }
        
        /**
         * Creates a {@code Method} binding from the given element. An error is
         * reported if the binding's type cannot be inferred.
         * 
         * @param element the element
         * @param logger the logger used to report errors
         * @return a {@code Method} binding, or {@code null} if the binding's type
         *         could not be inferred
         */
        @Override
        public @Nullable Method visitExecutable(ExecutableElement element, Logger logger) {
            var method = Method.capture(types, element);
            if (method == null) {
                logger.error(element, "Method: " + element.getSimpleName() + " returns an invalid type, should be an int, void, boolean or CompletableFuture<Suggestions>");
            }
            return method;
        }
        
        /**
         * Creates a {@code Field} binding from the given element. An error is
         * reported if the binding's type cannot be inferred.
         * 
         * @param element the element
         * @param logger the logger used to report errors
         * @return a {@code Field} binding, or {@code null} if the binding's type
         *         could not be inferred
         */
        @Override
        public @Nullable Field visitVariable(VariableElement element, Logger logger) {
            var field = Field.capture(types, element);
            if (field == null) {
                logger.error(element, "Field: " + element.getSimpleName() + " has an invalid type, should be an ArgumentType<?>, Command<CommandSender>, Predicate<CommandSender>, SuggestionProvider<CommandSender>");
            }
            return field;
        }
        
        /**
         * Reports an error as {@code Binding}s may only be created from fields
         * and methods.
         * 
         * @param element the element
         * @param logger the logger used to report errors
         * @return {@code null}
         */
        @Override
        public @Nullable Binding<?> defaultAction(Element element, Logger logger) {
            logger.error(element, "@Bind is used on an invalid target", "should annotate a field or method");
            return null;
        }
        
    }

}
