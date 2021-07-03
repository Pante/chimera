/*
 * The MIT License
 *
 * Copyright 2021 Karus Labs.
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
package com.karuslabs.typist.generation.chunks;

import com.karuslabs.elementary.junit.*;
import com.karuslabs.elementary.junit.annotations.Inline;
import com.karuslabs.typist.Environment;
import com.karuslabs.typist.lexers.*;
import com.karuslabs.typist.lexers.Lexer.Memoizer;
import com.karuslabs.typist.parsers.CommandParser;
import com.karuslabs.utilitary.*;

import java.util.*;

import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(ToolsExtension.class)
@Inline(name = "com.karuslabs.typist.generation.chunks.A", source = """
package com.karuslabs.typist.generation.chunks;

import com.karuslabs.elementary.junit.annotations.Case;
import com.karuslabs.typist.annotations.*;
import com.mojang.brigadier.arguments.ArgumentType;

@Case("a")
@Command("a")
class A {}

@Case("b")
@Command("b")
class B {}
""")
class TypeTest {
    
    static final String A = """
    public class Commands {
        private static final Predicate<CommandSender> REQUIREMENT = s -> true;

        public static Map<String, CommandNode<CommandSender>> of(com.karuslabs.typist.generation.chunks.B source) {
            var commands = new HashMap<String, CommandNode<CommandSender>>();
            var command0 = new Literal<>(
                "b",
                "",
                null,
                REQUIREMENT
            );
            commands.put(command0.getName(), command0);

            return commands;
        }
        public static Map<String, CommandNode<CommandSender>> of(com.karuslabs.typist.generation.chunks.A source) {
            var commands = new HashMap<String, CommandNode<CommandSender>>();
            var command1 = new Literal<>(
                "a",
                "",
                null,
                REQUIREMENT
            );
            commands.put(command1.getName(), command1);

            return commands;
        }
    }
    """.replace("\n", System.lineSeparator());
    
    static final String B = """
    public class Commands {
        private static final Predicate<CommandSender> REQUIREMENT = s -> true;
    
        public static Map<String, CommandNode<CommandSender>> of(com.karuslabs.typist.generation.chunks.A source) {
            var commands = new HashMap<String, CommandNode<CommandSender>>();
            var command0 = new Literal<>(
                "a",
                "",
                null,
                REQUIREMENT
            );
            commands.put(command0.getName(), command0);
    
            return commands;
        }
        public static Map<String, CommandNode<CommandSender>> of(com.karuslabs.typist.generation.chunks.B source) {
            var commands = new HashMap<String, CommandNode<CommandSender>>();
            var command1 = new Literal<>(
                "b",
                "",
                null,
                REQUIREMENT
            );
            commands.put(command1.getName(), command1);
    
            return commands;
        }
    }
    """.replace("\n", System.lineSeparator());
    
    Logger logger = mock(Logger.class);
    CommandParser commands = new CommandParser(logger, new CommandLexer(new ArgumentLexer(), LiteralLexer.aliasable(new Memoizer())));
    int[] counter = new int[] {0};
    Type type = new Type(new MethodBody(new CommandInstantiation(Map.of(), counter)));
    Environment environment = spy(new Environment());
    Source source = new Source();
    Cases cases = Tools.cases();
    
    @Test
    void emit() {
        commands.parse(environment, Set.of(cases.one("a"), cases.one("b")));
        
        type.emit(source, environment);
        
        assertTrue(source.toString().equals(A) || source.toString().equals(B));
    }

} 
