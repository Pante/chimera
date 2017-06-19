/*
 * Copyright (C) 2017 Karus Labs
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
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
