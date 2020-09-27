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
package com.karuslabs.commons.command.aot.resolvers;

import com.karuslabs.commons.command.aot.Identifier;
import com.karuslabs.commons.command.aot.*;

import java.util.Set;
import java.util.stream.Stream;
import javax.annotation.processing.*;
import javax.lang.model.element.*;
import javax.lang.model.type.TypeMirror;
import javax.lang.model.util.*;

import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.*;

import org.mockito.junit.jupiter.*;

import static org.junit.jupiter.params.provider.Arguments.of;
import static org.mockito.Mockito.*;

import static javax.lang.model.element.Modifier.*;
import static org.mockito.quality.Strictness.LENIENT;


@MockitoSettings(strictness = LENIENT)
class VariableResolverTest {
    
    static final TypeMirror COMMAND = mock(TypeMirror.class);
    static final TypeMirror ARGUMENT_TYPE = mock(TypeMirror.class);
    static final TypeMirror REQUIREMENT = mock(TypeMirror.class);
    static final TypeMirror SUGGESTIONS = mock(TypeMirror.class);
    
    Environment environment;
    VariableResolver resolver;
    TypeMirror type = mock(TypeMirror.class);
    VariableElement variable = when(mock(VariableElement.class).asType()).thenReturn(type).getMock();
    
    
    @BeforeEach
    void before() {
        TypeMirror mirror = mock(TypeMirror.class);
        TypeElement type = when(mock(TypeElement.class).asType()).thenReturn(mirror).getMock();
        Elements elements = when(mock(Elements.class).getTypeElement(any())).thenReturn(type).getMock();
        
        environment = spy(new Environment(mock(Messager.class), null, elements, mock(Types.class)));
        
        resolver = new VariableResolver(environment);
        resolver.command = COMMAND;
        resolver.argumentType = ARGUMENT_TYPE;
        resolver.requirement = REQUIREMENT;
        resolver.suggestions = SUGGESTIONS;
    }
    
    
    @ParameterizedTest
    @MethodSource("resolve_parameters")
    void resolve(TypeMirror expected, Binding binding) {
        when(variable.getModifiers()).thenReturn(Set.of(PUBLIC, FINAL)).getMock();
        when(environment.types.isSubtype(type, expected)).thenReturn(true);
        
        var token = mock(Identifier.class);
        
        resolver.resolve(variable, token);
        
        verify(token).bind(environment, binding, variable);
    }
    
    static Stream<Arguments> resolve_parameters() {
        return Stream.of(
            of(COMMAND, Binding.COMMAND),
            of(ARGUMENT_TYPE, Binding.TYPE),
            of(REQUIREMENT, Binding.REQUIREMENT),
            of(SUGGESTIONS, Binding.SUGGESTIONS)
        );
    }
    
    
    @ParameterizedTest
    @MethodSource("resolve_errors_parameters")
    void resolve_errors(VariableElement variable, String error) {
        when(environment.types.isSubtype(any(), any())).thenReturn(false);
        
        resolver.resolve(variable, mock(Identifier.class));
        
        verify(environment).error(variable, error);
    }
    
    static Stream<Arguments> resolve_errors_parameters() {
        var variable = when(mock(VariableElement.class).getModifiers()).thenReturn(Set.of(PUBLIC, FINAL)).getMock();
        return Stream.of(
            of(when(mock(VariableElement.class).getModifiers()).thenReturn(Set.of(PUBLIC)).getMock(), "Field should be public and final"),
            of(when(mock(VariableElement.class).getModifiers()).thenReturn(Set.of(FINAL)).getMock(), "Field should be public and final"), 
            of(variable, "\"" + variable.toString() + "\" should be an ArgumentType<?>, Command<CommandSender>, Predicate<CommandSender> or SuggestionProvider<CommandSender>")
        );
    }

}
