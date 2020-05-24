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
import com.karuslabs.commons.command.aot.annotations.Bind;
import com.karuslabs.commons.command.aot.lexers.*;
import com.karuslabs.commons.command.aot.resolvers.Resolver;
import java.lang.annotation.Annotation;

import java.util.*;
import javax.annotation.processing.Messager;
import javax.lang.model.element.*;

import org.junit.jupiter.api.*;

import static org.mockito.Mockito.*;


class BindParserTest {
    
    Environment environment = spy(new Environment(mock(Messager.class), null, null, null));
    BindParser parser = spy(new BindParser(environment, new CommandLexer(new ArgumentLexer(), new LiteralLexer()), mock(Resolver.class), mock(Resolver.class)));
    
    TypeElement type = mock(TypeElement.class);
    ExecutableElement method = when(mock(ExecutableElement.class).accept(any(), any())).thenReturn(type).getMock();
    VariableElement variable = mock(VariableElement.class);
    
    Token token = Token.literal(mock(Element.class), "token", "token", Set.of());
    Token child = Token.literal(mock(Element.class), "child", "child", Set.of());
    Token grandchild = Token.literal(mock(Element.class), "grandchild", "grandchild", Set.of());
    
    
    Token bindingChild = Token.literal(mock(Element.class), "child", "child", Set.of());
    Token bindingGrandchild = Token.literal(mock(Element.class), "grandchild", "grandchild", Set.of());
    
    
    @BeforeEach
    void before() {
        environment.scopes.put(type, Token.root());
    }
    
    
    @Test
    void parse() {
        environment.scopes.get(type).add(environment, token).add(environment, child);
        
        when(method.getAnnotation(Bind.class)).thenReturn(new StubBind("token child"));
        
        parser.parse(method);
        
        verify(parser).match(any(), any(), any());
    }    
    
    
    @Test
    void parse_no_tokens() {
        when(method.getAnnotation(Bind.class)).thenReturn(new StubBind("<a"));
        
        parser.parse(method);
        
        verify(parser, times(0)).matchAny(any(), any(), any());
        verify(parser, times(0)).match(any(), any(), any());
    }
    
    
    @Test
    void parse_non_existing() {
        when(method.getAnnotation(Bind.class)).thenReturn(new StubBind("a"));
        
        parser.parse(method);
        
        verify(environment).error(method, "\"a\" does not exist");
    }
    
    
    @Test
    void parse_no_command_annotation() {
        environment.scopes.clear();
        
        parser.parse(method);
        
        verify(environment).error(type, "Class should be annotated with @Command");
    }
    
    
    @Test
    void parse_empty_annotation() {
        when(method.getAnnotation(Bind.class)).thenReturn(new StubBind());
        
        parser.parse(method);
        
        verify(environment).error(method, "@Bind annotation should not be empty");
    }
    
    
    @Test
    void matchAny() {
        doNothing().when(parser).resolve(any(), any(), any());
        
        var argument = Token.argument(mock(Element.class), "<child>", "child");
        var other = Token.literal(mock(Element.class), "child", "child", Set.of());
        
        token.add(environment, child);
        token.add(environment, grandchild).add(environment, argument).add(environment, other);
        
        parser.matchAny(method, token, bindingChild);
        
        verify(parser, times(2)).resolve(any(), any(), any());
        verify(parser).resolve(method, child, bindingChild.location);
        verify(parser).resolve(method, other, bindingChild.location);
    }
    
    
    @Test
    void match() {
        doNothing().when(parser).resolve(any(), any(), any());
        
        token.add(environment, child).add(environment, grandchild);
        
        parser.match(method, token, List.of(bindingChild, bindingGrandchild));
        
        verify(parser).resolve(method, grandchild, bindingGrandchild.location);
    }
    
    
    @Test
    void match_nothing() {
        doNothing().when(parser).resolve(any(), any(), any());
        
        token.add(environment, child);
        
        parser.match(method, token, List.of(bindingChild, bindingGrandchild));
        
        verify(environment).error(method, "\"child grandchild\" does not exist");
        verify(parser, times(0)).resolve(any(), any(), any());
    }
    
    
    @Test
    void resolve_method() {
        parser.resolve(method, token, type);
        
        verify(parser.method).resolve(method, token, type);
        verifyNoInteractions(environment);
    }
    
    
    @Test
    void resolve_variable() {
        parser.resolve(variable, token, type);
        
        verify(parser.variable).resolve(variable, token, type);
        verifyNoInteractions(environment);
    }
    
    
    @Test
    void resolve_error() {
        Element element = when(mock(Element.class).getKind()).thenReturn(ElementKind.CLASS).getMock();
        
        parser.resolve(element, token, type);
        
        verify(environment).error(element, "@Bind annotation should not be used on a " + ElementKind.CLASS);
    }

} 

class StubBind implements Bind {
    
    String[] values;
    
    StubBind(String... values) {
        this.values = values;
    }
    
    
    @Override
    public String[] value() {
        return values;
    }

    @Override
    public Class<? extends Annotation> annotationType() {
        return Bind.class;
    }
    
}
