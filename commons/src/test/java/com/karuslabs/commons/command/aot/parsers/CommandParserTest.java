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
import com.karuslabs.commons.command.aot.annotations.Command;
import com.karuslabs.commons.command.aot.lexers.*;

import java.lang.annotation.Annotation;
import java.util.*;
import javax.lang.model.element.Element;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
class CommandParserTest {
    
    Environment environment = mock(Environment.class);
    CommandParser parser = new CommandParser(environment, new CommandLexer(new ArgumentLexer(), new LiteralLexer()));
    Token root = Token.root();
    Token literal = Token.literal(mock(Element.class), "a", "a", Set.of());
    Token child = Token.literal(mock(Element.class), "b", "b", Set.of());
    Token argument = Token.argument(mock(Element.class), "<b>", "b");
    
    
    @Test
    void parse() {
        Element element = when(mock(Element.class).getAnnotation(Command.class)).thenReturn(new StubCommand("a")).getMock();
        var root = Token.root();
        when(environment.root(element)).thenReturn(root);
        
        parser.parse(element);
        
        assertEquals(1, root.children.size());
        assertEquals(Type.LITERAL, root.children.get("a").type);
        
        verify(environment, times(0)).error(any(), any());
    }
    
    
    @Test
    void parse_empty() {
        Element element = when(mock(Element.class).getAnnotation(Command.class)).thenReturn(new StubCommand()).getMock();
        
        parser.parse(element);
        
        verify(environment).error(element, "@Command annotation should not be empty");
    }
    
    
    @Test
    void parse_tokens() {
        assertNotNull(root.add(environment, argument));
        
        parser.parse(root, List.of(literal, child));
        
        assertEquals(2, root.children.size());
        assertEquals(1, literal.children.size());
    }
    
    
    @Test
    void parse_tokens_invalid() {
        assertNotNull(root.add(environment, argument));
        
        parser.parse(root, List.of(child));
        
        assertEquals(1, root.children.size());
    }

}

class StubCommand implements Command {
    
    String[] values;
    
    StubCommand(String... values) {
        this.values = values;
    }
    
    
    @Override
    public String[] value() {
        return values;
    }

    @Override
    public Class<? extends Annotation> annotationType() {
        return Command.class;
    }
    
}
