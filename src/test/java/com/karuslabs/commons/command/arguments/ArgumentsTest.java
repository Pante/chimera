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
package com.karuslabs.commons.command.arguments;

import java.util.function.*;

import junitparams.*;

import org.junit.*;
import org.junit.runner.RunWith;

import static org.junit.Assert.*;
import static org.hamcrest.Matchers.equalTo;
import static org.mockito.Mockito.*;


@RunWith(JUnitParamsRunner.class)
public class ArgumentsTest {
    
    private Arguments arguments;
    
    
    public ArgumentsTest() {
        arguments = spy(new Arguments(new String[] {"0", "1"}));
    }
    
    
    @Test
    @Parameters({"1, true", "-1, false", "2, false"})
    public void contains(int index, boolean expected) {
        assertEquals(expected, arguments.contains(index));
    }
    
    
    @Test
    public void match() {
        Arguments arguments = new Arguments(new String[] {"1"});
        
        assertEquals(1, arguments.match().length());
        
        arguments.trim();
        assertEquals(0, arguments.match().length());
    }
    
    
    @Test
    @Parameters({"0, 0", "-1, "})
    public void get(int index, String expected) {
        assertEquals(expected, arguments.get(index).text());
    }
    
    
    @Test
    public void getLast() {
        arguments.getLast();
        
        verify(arguments).get(1);
    }
    
    
    @Test
    public void getImmutable() {
        assertFalse(arguments.getImmutable(0) == arguments.getImmutable(0));
    }
    
    @Test
    public void getLastImmutable() {
        assertFalse(arguments.getLastImmutable() == arguments.getLastImmutable());
    }
    
    
    @Test
    @Parameters({"true, 1, 0", "false, 0, 1"})
    public void getOr(boolean contains, int functionTimes, int supplierTimes) {
        doReturn(contains).when(arguments).contains(anyInt());
        
        Function<String, Argument> function = mock(Function.class);
        Supplier<Argument> supplier = mock(Supplier.class);
        
        arguments.getOr(0, function, supplier);
        
        verify(function, times(functionTimes)).apply(anyString());
        verify(supplier, times(supplierTimes)).get();
    }
    
    
    @Test
    @Parameters
    public void trim(String[] args, String[] expected) {
        Arguments arguments = new Arguments(args);
        arguments.trim();
        
        assertThat(arguments.get(), equalTo(expected));
    }
    
    protected Object[] parametersForTrim() {
        return new Object[] {
            new Object[] {new String[] {"1"}, new String[] {}},
            new Object[] {new String[] {"1", "2"}, new String[] {"2"}}
        };
    }
    
}
