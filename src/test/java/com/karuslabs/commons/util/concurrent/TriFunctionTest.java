/* 
 * The MIT License
 *
 * Copyright 2017 Karus Labs.
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
package com.karuslabs.commons.util.concurrent;

import com.karuslabs.commons.util.TriFunction;

import java.util.function.Function;

import org.junit.*;
import org.junit.rules.ExpectedException;

import static org.mockito.Mockito.*;


public class TriFunctionTest {
    
    private static final Object OBJECT = new Object();
    
    
    @Rule
    public ExpectedException exception = ExpectedException.none();
    
    private TriFunction<Object, Object, Object, Object> function;
    private Function<Object, Object> after;
    
    
    public TriFunctionTest() {
        function = spy(new StubFunction());
        after = mock(Function.class);
    }
    
    
    @Test
    public void andThen() {
        function.andThen(after).apply(null, null, null);
        verify(after).apply(OBJECT);
    }
    
    
    @Test
    public void andThen_ThrowsException() {
        exception.expect(NullPointerException.class);
        function.andThen(null);
    }
    
    
    private static class StubFunction implements TriFunction<Object, Object, Object, Object> {

        @Override
        public Object apply(Object a, Object b, Object c) {
            return OBJECT;
        }
        
    }
            
    
}
