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

import com.karuslabs.annotations.Static;

import java.util.*;
import javax.lang.model.element.*;

public @Static class Mirrors {
    
    public static final class Command {
    
        public final Identifier identifier;
        public final TypeElement site;
        public final Set<String> aliases;
        public final Map<Member.Type, Member<?>> members;
        public final Map<Identifier, Command> children;

        public Command(Identifier identifier, TypeElement site, Set<String> aliases) {
            this.identifier = identifier;
            this.site = site;
            this.aliases = aliases;
            members = new EnumMap<>(Member.Type.class);
            children = new HashMap<>();
        }

    }
    
    
    public static final class Method extends Member<ExecutableElement> {
    
        public final Map<String, Pointer> parameters;

        public Method(Identifier identifier, ExecutableElement site, Type type) {
            super(identifier, site, type);
            parameters = new HashMap<>();
        }

    }
    
    public static final class Field extends Member<VariableElement> {

        public Field(Identifier identifier, VariableElement site, Type type) {
            super(identifier, site, type);
        }

    }
    
    public static abstract class Member<T extends Element> {

        public final Identifier identifier;
        public final T site;
        public final Type type;

        public Member(Identifier identifier, T site, Type type) {
            this.identifier = identifier;
            this.site = site;
            this.type = type;
        }

        public enum Type {

            COMMAND("A", "Command<CommandSender>", "command"),
            TYPE("An", "ArgumentType<?>", "type"),
            REQUIREMENT("A", "Predicate<CommandSender>", "requirement"),
            SUGGESTIONS("A", "SuggestionProvider<CommandSender>", "suggestions");

            public final String article;
            public final String type;
            public final String value;

            private Type(String article, String type, String value) {
                this.article = article;
                this.type = type;
                this.value = value;
            }

            @Override
            public String toString() {
                return value;
            }

        }

    }
    
    
    public static final class Pointer {

        public final Identifier identifier;
        public final VariableElement site;
        public final Command value;
        public final String explicit;

        public Pointer(Identifier identifier, VariableElement site, String explicit, Command value) {
            this.identifier = identifier;
            this.site = site;
            this.explicit = explicit;
            this.value = value;
        }

    }
    
}
