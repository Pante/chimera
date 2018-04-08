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

import java.util.List;
import java.util.stream.Stream;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.*;

import static java.util.Collections.singletonList;
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
    
    @Bundle("name")
    @EmbeddedResources({"a"})
    static class B implements CommandExecutor {

        @Override
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
        List<Command> commands = singletonList(new Command("", null));
        MessageTranslation translation = mock(MessageTranslation.class);
        
        doReturn(translation).when(resolver).translation(CommandExecutor.NONE);
        
        resolver.resolve(commands, CommandExecutor.NONE);
        
        assertEquals(translation, commands.get(0).getTranslation());
    }
    
    
    @Test
    void translation() {
        MessageTranslation translation = resolver.translation(new A());
        ExternalControl control = (ExternalControl) translation.getControl();
        EmbeddedResource embedded = (EmbeddedResource) control.getResources()[0];
        ExternalResource external = (ExternalResource) control.getResources()[1];
        
        assertEquals("name", translation.getBundleName());
        assertEquals("a/", embedded.getPath());
        assertEquals("b", external.getPath());
    }
    
    
    @ParameterizedTest
    @MethodSource("isResolvable_parameters")
    void isResolvable(CommandExecutor executor, boolean expected) {
        assertEquals(expected, resolver.isResolvable(executor));
    }
    
    static Stream<Arguments> isResolvable_parameters() {
        return Stream.of(of(new A(), true), of(new B(), true), of(new C(), false), of(new D(), false));
    }
    
}
