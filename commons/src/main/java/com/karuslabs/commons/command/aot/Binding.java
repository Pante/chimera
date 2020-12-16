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
package com.karuslabs.commons.command.aot;

import java.util.*;
import javax.lang.model.element.*;
import javax.lang.model.type.*;

import org.checkerframework.checker.nullness.qual.Nullable;

public abstract class Binding<T extends Element> {

    public final T site;
    public final Pattern pattern;
    
    public Binding(T site, Pattern pattern) {
        this.site = site;
        this.pattern = pattern;
    }
    
    public enum Pattern {
        
        ARGUMENT_TYPE("An", "ArgumentType<?>", "type"),
        COMMAND("A", "Command<CommandSender>", "command"),
        EXECUTION("An", "Executable<CommandSender>", "command"),
        REQUIREMENT("A", "Predicate<CommandSender>", "requirement"),
        SUGGESTION_PROVIDER("A", "SuggestionProvider<CommandSender>", "suggestions");
        
        public final String article;
        public final String type;
        public final String noun;
        
        private Pattern(String article, String type, String noun) {
            this.article = article;
            this.type = type;
            this.noun = noun;
        }
        
        @Override
        public String toString() {
            return type;
        }
        
    }
    
    public static class Field extends Binding<VariableElement> {
        
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

            } else if (types.isSubtype(type, types.suggestions)) {
                return new Field(site, Pattern.SUGGESTION_PROVIDER);

            } else {
                return null;
            }
        }
        
        Field(VariableElement site, Pattern pattern) {
            super(site, pattern);
        }
        
    }
    
    public static class Method extends Binding<ExecutableElement> {
        
        public static @Nullable Method capture(Types types, ExecutableElement site) {
            var type = site.getReturnType();
            if (type.getKind() == TypeKind.VOID) {
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
        
        private final Map<Command, Map<Integer, Reference>> parameters = new HashMap<>();

        Method(ExecutableElement site, Pattern pattern) {
            super(site, pattern);
        }
        
        public void parameter(Command command, Reference reference) {
            parameters(command).put(reference.index, reference);
        }
        
        public Map<Integer, Reference> parameters(Command command) {
            var references = parameters.get(command);
            if (references == null) {
                parameters.put(command, references = new HashMap<>());
            }
            
            return references;
        }

    }
    
    public static class Reference {
        
        public final int index;
        public final VariableElement site;
        public final Command value;
        
        public Reference(int index, VariableElement site, Command value) {
            this.index = index;
            this.site = site;
            this.value = value;
        }
        
    }
    
}
