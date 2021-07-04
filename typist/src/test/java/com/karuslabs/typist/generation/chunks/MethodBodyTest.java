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
import com.karuslabs.typist.*;
import com.karuslabs.typist.lexers.*;
import com.karuslabs.typist.lexers.Lexer.Memoizer;
import com.karuslabs.typist.parsers.*;
import com.karuslabs.typist.parsers.BindParser.Captor;
import com.karuslabs.utilitary.*;

import java.util.Map;
import javax.lang.model.element.TypeElement;

import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(ToolsExtension.class)
@Inline(name = "com.karuslabs.typist.generation.chunks.Body", source = """
package com.karuslabs.typist.generation.chunks;

import com.karuslabs.elementary.junit.annotations.Case;
import com.karuslabs.typist.annotations.*;
import com.mojang.brigadier.arguments.ArgumentType;

@Case("class")
@Command({"a <b> c"})
class Body {
    
    @Case("type")
    public @Bind("a <b>") ArgumentType<?> type = null;
    
}
""")
class MethodBodyTest {
    
    Logger logger = mock(Logger.class);
    Lexer lexer = new CommandLexer(new ArgumentLexer(), LiteralLexer.aliasable(new Memoizer()));
    CommandParser commands = new CommandParser(logger, lexer);
    BindParser bindings = new BindParser(new Captor(new Types(Tools.elements(), Tools.typeMirrors())), logger, lexer);
    int[] counter = new int[] {0};
    MethodBody body = new MethodBody(new CommandInstantiation(Map.of(), counter));
    Environment environment = spy(new Environment());
    Source source = new Source();
    
    
    Cases cases = Tools.cases();
    
    @Test
    void emit() {
        var type = (TypeElement) cases.one("class");
        
        commands.parse(environment, type);
        bindings.parse(environment, cases.one("type"));
        
        body.emit(source,  type, environment.namespace(type).values());
        
        assertEquals("""
            public static Map<String, CommandNode<CommandSender>> of(com.karuslabs.typist.generation.chunks.Body source) {
                var commands = new HashMap<String, CommandNode<CommandSender>>();
                var command0 = new Literal<>(
                    "c",
                    "",
                    null,
                    REQUIREMENT
                );
            
                var command1 = new Argument<>(
                    "b",
                    source.type,
                    "",
                    null,
                    REQUIREMENT,
                    null
                );
                command1.addChild(command0);
            
                var command2 = new Literal<>(
                    "a",
                    "",
                    null,
                    REQUIREMENT
                );
                command2.addChild(command1);
                commands.put(command2.getName(), command2);
            
                return commands;
            }
            """.replace("\n", System.lineSeparator()), source.toString());
    }

} 
