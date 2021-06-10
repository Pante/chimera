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
package com.karuslabs.typist;

import java.util.*;
import javax.lang.model.element.*;
import javax.lang.model.type.*;

import org.checkerframework.checker.nullness.qual.Nullable;

/**
 * A Java language construct which represents a part of a {@code Command}. A
 * {@code Binding} may be simultaneously bound to several {@code Command}s.
 * 
 * @param <T> the type of the Java language construct that this {@code Binding}
 *            represents
 */
public interface Binding<T extends Element> {
    
    /**
     * The site of the Java language construct that this binding represents.
     * 
     * @return the site
     */
    public T site();
    
    /**
     * The part of a {@code Command} that this {@code Binding} represents.
     * 
     * @return the pattern
     */
    public Pattern pattern();
    
    /**
     * The parts of a {@code Command} to which can be bound.
     */
    public static enum Pattern {
        
        /**
         * An {@code ArgumentType<?>}.
         */
        ARGUMENT_TYPE(Group.ARGUMENT_TYPE, "An", "ArgumentType<?>", "type"),
        /**
         * A {@code Command<CommandSender>}.
         */
        COMMAND(Group.COMMAND, "A", "Command<CommandSender>", "command"),
        /**
         * An {@code Execution<CommandSender}.
         */
        EXECUTION(Group.COMMAND, "An", "Execution<CommandSender>", "command"),
        /**
         * A {@code Predicate<CommandSender}.
         */
        REQUIREMENT(Group.REQUIREMENT, "A", "Predicate<CommandSender>", "requirement"),
        /**
         * A {@code SuggestionProvider<CommandSender>}.
         */
        SUGGESTION_PROVIDER(Group.SUGGESTION_PROVIDER, "A", "SuggestionProvider<CommandSender>", "suggestions");
        
        /**
         * The group.
         */
        public final Group group; 
        /**
         * The article used to describe this pattern.
         */
        public final String article;
        /**
         * The actual specialized type that this pattern represents.
         */
        public final String type;
        /**
         * The noun used to describe this pattern.
         */
        public final String noun;
        
        private Pattern(Group group, String article, String type, String noun) {
            this.group = group;
            this.article = article;
            this.type = type;
            this.noun = noun;
        }
        
        @Override
        public String toString() {
            return type;
        }
        
        /**
         * A group which a {@code Pattern} is part of.
         */
        public static enum Group {
            ARGUMENT_TYPE, COMMAND, REQUIREMENT, SUGGESTION_PROVIDER;
        }
        
    }
    
    /**
     * A Java field that represents a part of a {@code Command}.
     */
    public static record Field(VariableElement site, Pattern pattern) implements Binding<VariableElement> {
        
        /**
         * Creates a {@code Field} which represented part is inferred from the given
         * {@code VariableElement}.
         * 
         * @param types the types
         * @param site the site
         * @return a {@code Field} which represented part is inferred, or {@code null}
         *         if the represented part cannot be inferred
         */
        public static @Nullable Field capture(Types types, VariableElement site) {
            var type = site.asType();
            if (types.isSubtype(type, types.argument)) {
                return new Field(site, Pattern.ARGUMENT_TYPE);

            } else if (types.isSubtype(type, types.execution)) {
                // This needs to be before Pattern.COMMAND, since Execution extends Command
                return new Field(site, Pattern.EXECUTION);
                
            } else if (types.isSubtype(type, types.command)) {
                return new Field(site, Pattern.COMMAND);

            } else if (types.isSubtype(type, types.requirement)) {
                return new Field(site, Pattern.REQUIREMENT);

            } else if (types.isSubtype(type, types.suggestionProvider)) {
                return new Field(site, Pattern.SUGGESTION_PROVIDER);

            } else {
                return null;
            }
        }
        
    }
    
    /**
     * A Java method that represents a part of a {@code Command}. A method's parameters
     * may contain a reference to another {@code Command}.
     */
    public static record Method(ExecutableElement site, Pattern pattern, Map<Command, Map<Integer, Reference>> parameters) implements Binding<ExecutableElement> {
        
        /**
         * Creates a {@code Method} which represented part is inferred from the given
         * {@code ExecutableElement}.
         * 
         * @param types the types
         * @param site the site
         * @return a {@code Method} which represented part is inferred, or {@code null}
         *         if the represented part cannot be inferred
         */
        public static @Nullable Method capture(Types types, ExecutableElement site) {
            var type = site.getReturnType();
            if (type.getKind() == TypeKind.INT) {
                return new Method(site, Pattern.COMMAND);

            } else if (type.getKind() == TypeKind.VOID) {
                return new Method(site, Pattern.EXECUTION);

            } else if (type.getKind() == TypeKind.BOOLEAN) {
                return new Method(site, Pattern.REQUIREMENT);

            } else if (types.isSubtype(type, types.completable)) {
                return new Method(site, Pattern.SUGGESTION_PROVIDER);

            } else {
                return null;
            }
        }
        
        Method(ExecutableElement site, Pattern pattern) {
            this(site, pattern, new HashMap<>());
        }
        
        /**
         * Adds a reference to the given command.
         * 
         * @param command a command
         * @param reference a reference to the command
         */
        public void parameter(Command command, Reference reference) {
            parameters(command).put(reference.index, reference);
        }
        
        /**
         * Returns the parameters that are references to the given command.
         * 
         * @param command the command
         * @return the parameters that reference the given command
         */
        public Map<Integer, Reference> parameters(Command command) {
            var references = parameters.get(command);
            if (references == null) {
                parameters.put(command, references = new HashMap<>());
            }
            
            return references;
        }
        
    }
    
    /**
     * Represents a method parameter that is a reference to a command.
     */
    public static record Reference(int index, VariableElement site, Command value) {}
    
}
