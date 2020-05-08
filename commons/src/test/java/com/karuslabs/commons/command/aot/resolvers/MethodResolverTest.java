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

import com.karuslabs.commons.command.aot.*;

import java.util.*;
import java.util.stream.Stream;
import javax.annotation.processing.Messager;
import javax.lang.model.element.*;
import javax.lang.model.type.*;
import javax.lang.model.util.*;

import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.*;

import org.mockito.junit.jupiter.*;

import static java.util.stream.Collectors.toList;
import static javax.lang.model.element.Modifier.PUBLIC;
import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.params.provider.Arguments.of;
import static org.mockito.Mockito.*;
import static org.mockito.quality.Strictness.LENIENT;


@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = LENIENT)
class MethodResolverTest {
    
    static final TypeMirror SENDER = mock(TypeMirror.class);
    static final TypeMirror COMPLETABLE = mock(TypeMirror.class);
    static final TypeMirror CONTEXT = mock(TypeMirror.class);
    static final TypeMirror DEFAULTABLE = mock(TypeMirror.class);
    static final TypeMirror BUILDER = mock(TypeMirror.class);
    static final TypeMirror EXCEPTION = mock(TypeMirror.class);
    
    Environment environment;
    MethodResolver resolver;
    TypeMirror type = mock(TypeMirror.class);
    ExecutableElement method = when(mock(ExecutableElement.class).asType()).thenReturn(type).getMock();
    
    
    @BeforeEach
    void before() {
        TypeMirror mirror = mock(TypeMirror.class);
        TypeElement type = when(mock(TypeElement.class).asType()).thenReturn(mirror).getMock();
        Elements elements = when(mock(Elements.class).getTypeElement(any())).thenReturn(type).getMock();
        Types types = mock(Types.class);

        
        environment = spy(new Environment(mock(Messager.class), null, elements, types));
        
        resolver = spy(new MethodResolver(environment));
        resolver.sender = SENDER;
        resolver.completable = COMPLETABLE;
        resolver.context = CONTEXT;
        resolver.defaultable = DEFAULTABLE;
        resolver.builder = BUILDER;
        resolver.exception = EXCEPTION;
        
        when(types.isSubtype(SENDER, SENDER)).thenReturn(true);
        when(types.isSubtype(COMPLETABLE, COMPLETABLE)).thenReturn(true);
        when(types.isSubtype(CONTEXT, CONTEXT)).thenReturn(true);
        when(types.isSubtype(DEFAULTABLE, DEFAULTABLE)).thenReturn(true);
        when(types.isSubtype(BUILDER, BUILDER)).thenReturn(true);
        when(types.isSubtype(EXCEPTION, EXCEPTION)).thenReturn(true);
    }
    
    
    @ParameterizedTest
    @MethodSource("resolve_parameters")
    void resolve(TypeMirror type, List<? extends TypeMirror> parameters, Binding binding) {
        when(method.getModifiers()).thenReturn(Set.of(PUBLIC));
        when(method.getReturnType()).thenReturn(type);
        doReturn(map(parameters)).when(method).getParameters();
        when(method.getThrownTypes()).thenReturn(List.of());
        
        var token = mock(Token.class);
        var other = mock(Token.class);
        
        resolver.resolve(method, token, other);
        
        verify(token).bind(environment, binding, other);
    }
    
    static Stream<Arguments> resolve_parameters() {
        return Stream.of(
            of(when(mock(TypeMirror.class).getKind()).thenReturn(TypeKind.VOID).getMock(), List.of(DEFAULTABLE), Binding.COMMAND),
            of(when(mock(TypeMirror.class).getKind()).thenReturn(TypeKind.BOOLEAN).getMock(), List.of(SENDER), Binding.REQUIREMENT),
            of(COMPLETABLE, List.of(CONTEXT, BUILDER), Binding.SUGGESTIONS)
        );
    }
    
    
    @ParameterizedTest
    @MethodSource("resolve_errors_parameters")
    void resolve_errors(Set<Modifier> modifiers, String error) {
        doReturn(false).when(resolver).command(any(), any());
        doReturn(false).when(resolver).predicate(any(), any());
        doReturn(false).when(resolver).suggestions(any(), any());
        
        when(method.getModifiers()).thenReturn(modifiers);
        
        resolver.resolve(method, mock(Token.class), mock(Token.class));
        
        verify(environment).error(method, error);
    }
    
    static Stream<Arguments> resolve_errors_parameters() {
        return Stream.of(
            of(Set.of(), "Method should be public"),
            of(Set.of(PUBLIC),"Signature should match Command<CommandSender>, Executable<CommandSender>, Predicate<CommandSender> or SuggestionProvider<CommandSender>")
        );
    }
    
    
    @ParameterizedTest
    @MethodSource("command_parameters")
    void command(TypeMirror type, List<? extends TypeMirror> parameters, boolean expected) {
        assertEquals(expected, resolver.command(type, map(parameters)));
    }
    
    static Stream<Arguments> command_parameters() {
        var integer = when(mock(TypeMirror.class).getKind()).thenReturn(TypeKind.INT).getMock();
        var nothing = when(mock(TypeMirror.class).getKind()).thenReturn(TypeKind.VOID).getMock();
        
        return Stream.of(
            of(integer, List.of(CONTEXT), true),
            of(nothing, List.of(DEFAULTABLE), true),
            of(integer, List.of(), false),
            of(integer, List.of(CONTEXT, CONTEXT), false),
            of(integer, List.of(EXCEPTION), false),
            of(nothing, List.of(EXCEPTION), false)
        );
    }
    
    
    
    @ParameterizedTest
    @MethodSource("predicate_parameters")
    void predicate(TypeMirror type, List<? extends TypeMirror> parameters, boolean expected) {
        assertEquals(expected, resolver.predicate(type, map(parameters)));
    }
    
    static Stream<Arguments> predicate_parameters() {
        var bool = when(mock(TypeMirror.class).getKind()).thenReturn(TypeKind.BOOLEAN).getMock();
        return Stream.of(
            of(bool, List.of(SENDER), true),
            of(EXCEPTION, List.of(SENDER), false),
            of(bool, List.of(SENDER, SENDER), false),
            of(bool, List.of(CONTEXT), false)
        );
    }
    
    
    @ParameterizedTest
    @MethodSource("suggestions_parameters")
    void suggestions(TypeMirror type, List<? extends TypeMirror> parameters, boolean expected) {
        assertEquals(expected, resolver.suggestions(type, map(parameters)));
    }
    
    static Stream<Arguments> suggestions_parameters() {
        return Stream.of(
            of(COMPLETABLE, List.of(CONTEXT, BUILDER), true),
            of(EXCEPTION, List.of(CONTEXT, BUILDER), false),
            of(COMPLETABLE, List.of(CONTEXT), false),
            of(COMPLETABLE, List.of(CONTEXT, BUILDER, EXCEPTION), false),
            of(COMPLETABLE, List.of(BUILDER, CONTEXT), false)
        );
    }
    
    
    @ParameterizedTest
    @MethodSource("exception_parameters")
    void exceptions(List<? extends TypeMirror> thrown, boolean expected) {
        assertEquals(expected, resolver.exceptions(thrown));
    }
    
    static Stream<Arguments> exception_parameters(){
        return Stream.of(
            of(List.of(), true),
            of(List.of(EXCEPTION), true),
            of(List.of(COMPLETABLE), false),
            of(List.of(COMPLETABLE, COMPLETABLE), false)
        );
    }
    
    static List<? extends VariableElement> map(List<? extends TypeMirror> types) {
        return types.stream().map(type -> (VariableElement) when(mock(VariableElement.class).asType()).thenReturn(type).getMock()).collect(toList());
    }

} 
