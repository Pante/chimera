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

import com.karuslabs.puff.type.TypeMirrors;

import java.util.*;
import javax.lang.model.element.*;
import javax.lang.model.type.*;

import org.checkerframework.checker.nullness.qual.Nullable;

public abstract class Binding<T extends Element> {

    public final Identity identity;
    public final T site;
    public final Match match;
    
    public Binding(Identity identity, T site, Match match) {
        this.identity = identity;
        this.site = site;
        this.match = match;
    }
    
    public enum Match {
        
        ARGUMENT_TYPE("An", "ArgumentType<?>", "type"),
        COMMAND("A", "Command<CommandSender>", "command"),
        EXECUTABLE("An", "Executable<CommandSender>", "command"),
        REQUIREMENT("A", "Predicate<CommandSender>", "requirement"),
        SUGGESTION_PROVIDER("A", "SuggestionProvider<CommandSender>", "suggestions");
        
        public final String article;
        public final String type;
        public final String noun;
        
        private Match(String article, String type, String noun) {
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
        
        public static @Nullable Match pattern(TypeMirrors util, KnownTypes types, TypeMirror type) {
            if (util.isSubtype(type, types.argument)) {
                return Match.ARGUMENT_TYPE;

            } else if (util.isSubtype(type, types.command)) {
                return Match.COMMAND;

            } else if (util.isSubtype(type, types.requirement)) {
                return Match.REQUIREMENT;

            } else if (util.isSubtype(type, types.suggestions)) {
                return Match.SUGGESTION_PROVIDER;

            } else {
                return null;
            }
        }
        
        public Field(Identity identity, VariableElement site, Match pattern) {
            super(identity, site, pattern);
        }
        
    }
    
    public static class Method extends Binding<ExecutableElement> {
        
        public static @Nullable Match pattern(TypeMirrors utils, KnownTypes types, TypeMirror returned) {
            if (returned.getKind() == TypeKind.INT) {
                return Match.COMMAND;

            } else if (returned.getKind() == TypeKind.VOID) {
                return Match.EXECUTABLE;

            } else if (returned.getKind() == TypeKind.BOOLEAN) {
                return Match.REQUIREMENT;

            } else if (utils.isSubtype(returned, types.completable)) {
                return Match.SUGGESTION_PROVIDER;

            } else {
                return null;
            }
        }
        
        
        public final Map<Integer, Reference> parameters = new HashMap<>();

        public Method(Identity identity, ExecutableElement site, Match pattern) {
            super(identity, site, pattern);
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
