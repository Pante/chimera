/*
 * The MIT License
 *
 * Copyright 2018 Karus Labs.
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
package com.karuslabs.commons.command.annotation.resolvers;

import com.karuslabs.commons.command.*;
import com.karuslabs.commons.locale.*;
import com.karuslabs.commons.locale.annotation.*;
import com.karuslabs.commons.locale.resources.*;

import java.lang.reflect.*;
import java.util.stream.Stream;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.params.provider.Arguments.of;
import static org.mockito.Mockito.*;


class ResourceResolverTest {
    
    @Bundle("name")
    @EmbeddedResources({"a"})
    @ExternalResources({"b"})
    static class A implements CommandExecutor {

        @Override
        public boolean execute(CommandSource source, Context context, com.karuslabs.commons.command.arguments.Arguments arguments) {
            throw new UnsupportedOperationException("Not supported yet.");
        }
        
    }
    
    
    static class B {

        @Bundle("name")
        @EmbeddedResources({"a"})
        public boolean execute(CommandSource source, Context context, com.karuslabs.commons.command.arguments.Arguments arguments) {
            throw new UnsupportedOperationException("Not supported yet.");
        }
        
    }
    
    @ExternalResources({"b"})
    static class C implements CommandExecutor {

        @Override
        public boolean execute(CommandSource source, Context context, com.karuslabs.commons.command.arguments.Arguments arguments) {
            throw new UnsupportedOperationException("Not supported yet.");
        }
        
    }
    
    static class D implements CommandExecutor {

        @Override
        public boolean execute(CommandSource source, Context context, com.karuslabs.commons.command.arguments.Arguments arguments) {
            throw new UnsupportedOperationException("Not supported yet.");
        }
        
    }
    
    
    ResourceResolver resolver = spy(new ResourceResolver());
    
    
    @Test
    void resolve() {
        Command command = new Command("", null);
        MessageTranslation translation = mock(MessageTranslation.class);
        
        doReturn(translation).when(resolver).translation(CommandExecutor.class, CommandExecutor.NONE);
        
        resolver.resolve(CommandExecutor.class, CommandExecutor.NONE, command);
        
        assertEquals(translation, command.getTranslation());
    }
    
    
    @Test
    void translation() {
        MessageTranslation translation = resolver.translation(A.class, new A());
        ExternalControl control = (ExternalControl) translation.getControl();
        EmbeddedResource embedded = (EmbeddedResource) control.getResources()[0];
        ExternalResource external = (ExternalResource) control.getResources()[1];
        
        assertEquals("name", translation.getBundleName());
        assertEquals("a/", embedded.getPath());
        assertEquals("b", external.getPath());
    }
    
    
    @ParameterizedTest
    @MethodSource("isResolvable_parameters")
    void isResolvable(AnnotatedElement element,  boolean expected) {
        assertEquals(expected, resolver.isResolvable(element));
    }
    
    static Stream<Arguments> isResolvable_parameters() throws ReflectiveOperationException {
        Method method = B.class.getDeclaredMethod("execute", CommandSource.class, Context.class, com.karuslabs.commons.command.arguments.Arguments.class);
        return Stream.of(of(A.class, true), of(method, true), of(C.class, false), of(D.class, false));
    }
    
}
