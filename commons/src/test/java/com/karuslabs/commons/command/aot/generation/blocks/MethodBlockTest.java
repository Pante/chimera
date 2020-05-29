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
package com.karuslabs.commons.command.aot.generation.blocks;

import com.karuslabs.commons.command.aot.*;

import java.util.Set;
import javax.lang.model.element.*;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;


class MethodBlockTest {
    
    MethodBlock block = new MethodBlock();
    
    
    @Test
    void command_throws_exception() {
        assertEquals(
            "Invalid token type: \"root\", found when generating method",
            assertThrows(IllegalArgumentException.class, () -> block.command(Token.root())).getMessage()
        );
    }
    
    
    @Test
    void parameter_throws_exception() {
        Element type = when(mock(Element.class).getKind()).thenReturn(ElementKind.CLASS).getMock();
        when(type.getModifiers()).thenReturn(Set.of());
        
        var token = Token.root();
        token.bindings.put(Binding.TYPE, type);
        
        assertEquals(
            "Invalid bind target: \"CLASS\"",
            assertThrows(IllegalArgumentException.class, () -> block.parameter(token, Binding.TYPE, "")).getMessage()
        );
    }

} 
