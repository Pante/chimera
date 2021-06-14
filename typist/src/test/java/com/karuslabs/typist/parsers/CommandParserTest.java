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
package com.karuslabs.typist.parsers;

import com.karuslabs.elementary.junit.*;
import com.karuslabs.elementary.junit.annotations.*;
import com.karuslabs.typist.*;
import com.karuslabs.typist.lexers.*;
import com.karuslabs.typist.lexers.Lexer.Memoizer;
import com.karuslabs.utilitary.Logger;

import java.util.List;
import javax.lang.model.element.TypeElement;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import static com.karuslabs.typist.Identity.Type.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(ToolsExtension.class)
@Inline(name = "com.karuslabs.typist.parsers.Cases", source = """
import com.karuslabs.elementary.junit.annotations.Case;
import com.karuslabs.typist.annotations.Command;                                                         

@Case("simple")
@Command("a|a1 <b> c")
class Simple {}

@Case("merge")
@Command({"a|a1 <b> c", "a|a2 <b> c", "a|a3 <other>"})
class Merge {}
                                                              
@Case("blank")
@Command({})
class Blank {}
""")
class CommandParserTest {

    Lexer lexer = new CommandLexer(new ArgumentLexer(), LiteralLexer.aliasable(new Memoizer()));
    Logger logger = mock(Logger.class);
    CommandParser parser = new CommandParser(logger, lexer);
    Environment environment = new Environment();
    Cases cases = Tools.cases();
    
    @Test
    void parse_simple() {
        var element = (TypeElement) cases.one("simple");
        
        var a = new Command(null, new Identity(LITERAL, "a"), element);
        var b = new Command(a, new Identity(ARGUMENT, "b"), element);
        var c = new Command(b, new Identity(LITERAL, "c"), element);
        
        a.children().put(b.identity(), b);
        a.aliases().add("a1");
        b.children().put(c.identity(), c);
        
        parser.parse(environment, element);
        
        var namespace = environment.namespace(element);
        assertEquals(1, namespace.size());
        assertEquals(a, namespace.get(new Identity(LITERAL, "a")));
    }
    
    @Test
    void parse_merge() {
        var element = (TypeElement) cases.one("merge");
        
        var a = new Command(null, new Identity(LITERAL, "a"), element);
        var b = new Command(a, new Identity(ARGUMENT, "b"), element);
        var other = new Command(a, new Identity(ARGUMENT, "other"), element);
        var c = new Command(b, new Identity(LITERAL, "c"), element);
       
        a.children().put(b.identity(), b);
        a.children().put(other.identity(), other);
        a.aliases().addAll(List.of("a1", "a2", "a3"));
        b.children().put(c.identity(), c);
        
        parser.parse(environment, element);
        
        var namespace = environment.namespace(element);
        assertEquals(1, namespace.size());
        assertEquals(a, namespace.get(new Identity(LITERAL, "a")));
    }
    
    @Test
    void parse_blank() {
        var element = cases.one("blank");
        
        parser.parse(environment, element);
        
        verify(logger).error(element, "@Command annotation should not be empty");
        assertEquals(0, environment.namespace(element).size());
    }
    
    @Test
    void annotation() {
        assertEquals(com.karuslabs.typist.annotations.Command.class, parser.annotation());
    }
    
}
