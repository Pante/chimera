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
import java.util.stream.Stream;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.*;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.params.provider.Arguments.of;
import static org.mockito.Mockito.*;


class ArgumentsTest {
    
    Arguments arguments = spy(new Arguments("0", "1"));
    
    
    @ParameterizedTest
    @CsvSource({"1, true", "-1, false", "2, false"})
    void contains(int index, boolean expected) {
        assertEquals(expected, arguments.contains(index));
    }
    
    
    @Test
    void match() {
        assertEquals(2, arguments.match().length());
        
        arguments.trim();
        assertEquals(1, arguments.match().length());
    }
    
    
    @ParameterizedTest
    @CsvSource({"0, 0", "-1, ''"})
    void get(int index, String expected) {
        assertEquals(expected, arguments.get(index).text());
    }
    
    
    @Test
    void getLast() {
        arguments.getLast();
        
        verify(arguments).get(1);
    }
    
    
    @Test
    void getImmutable() {
        assertFalse(arguments.getCopy(0) == arguments.getCopy(0));
    }
    
    @Test
    void getLastImmutable() {
        assertFalse(arguments.getLastCopy() == arguments.getLastCopy());
    }
    
    
    @ParameterizedTest
    @CsvSource({"true, 1, 0", "false, 0, 1"})
    void getOr(boolean contains, int functionTimes, int supplierTimes) {
        doReturn(contains).when(arguments).contains(anyInt());
        
        Function<String, Argument> function = mock(Function.class);
        Supplier<Argument> supplier = mock(Supplier.class);
        
        arguments.getOr(0, function, supplier);
        
        verify(function, times(functionTimes)).apply(anyString());
        verify(supplier, times(supplierTimes)).get();
    }
    
    
    @ParameterizedTest
    @MethodSource("trim_parameters")
    void trim(String[] args, String[] expected) {
        Arguments arguments = new Arguments(args);
        arguments.trim();
        
        assertThat(arguments.get(), equalTo(expected));
    }
    
    static Stream<org.junit.jupiter.params.provider.Arguments> trim_parameters() {
        return Stream.of(of(new String[] {"1"}, new String[] {}), of(new String[] {"1", "2"}, new String[] {"2"}));
    }
    
}
