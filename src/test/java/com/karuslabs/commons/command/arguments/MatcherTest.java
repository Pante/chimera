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

import java.util.function.Predicate;
import java.util.stream.Stream;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.*;

import static java.util.Arrays.asList;
import static java.util.stream.Collectors.toList;
import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.params.provider.Arguments.of;
import static org.mockito.Mockito.*;


public class MatcherTest {
    
    private Matcher matcher = spy(new Matcher(new String[] {"0", "1", "2", "3"}));
    
    
    @Test
    public void matcher() {        
        assertEquals(0, matcher.getFirst());
        assertEquals(4, matcher.getLast());
    }
    
    
    @Test
    public void all() {
        matcher.between(2, 3);
        matcher.all();
        
        assertEquals(0, matcher.getFirst());
        assertEquals(4, matcher.getLast());
    }
    
    
    @Test
    public void starting() {
        matcher.starting(2);
        
        verify(matcher).between(2, 4);
    }
    
    
    @ParameterizedTest
    @CsvSource({"0, 4", "1, 3"})
    public void between(int first, int last) {
        matcher.between(first, last);
        
        assertEquals(first, matcher.getFirst());
        assertEquals(last, matcher.getLast());
    }
    
    
    @Test
    public void between_empty() {
        Matcher matcher = new Matcher(new String[] {}).between(0, 0);
        
        assertEquals(0, matcher.getFirst());
        assertEquals(0, matcher.getLast());
    }
    
    
    @ParameterizedTest
    @CsvSource({"-1, 3", "1, 5"})
    public void between_ThrowsException(int first, int last) {
        assertEquals(
            "Invalid bounds specified: " + first + ", " + last,
            assertThrows(IllegalArgumentException.class, () -> matcher.between(first, last)).getMessage()
        );
    }
    
    
    @Test
    public void stream() {
        assertEquals(asList("2", "3"), matcher.starting(2).stream().collect(toList()));
    }
    
    
    @ParameterizedTest
    @CsvSource({"true, true, true", "true, false, false", "false, true, false"})
    public void exact(boolean match1, boolean match2, boolean expected) {
        Predicate<String> predicate1 = when(mock(Predicate.class).test("1")).thenReturn(match1).getMock();
        Predicate<String> predicate2 = when(mock(Predicate.class).test("2")).thenReturn(match2).getMock();
        
        assertEquals(expected, matcher.exact(arg -> true, predicate1, predicate2, arg -> true));
    }
    
    
    @Test
    public void exact_ThrowsException() {
        assertEquals(
            "Invalid number of matches specified.",
            assertThrows(IllegalArgumentException.class, () -> matcher.exact(arg -> true, arg -> true)).getMessage()
        );
    }
    
    
    @ParameterizedTest
    @MethodSource("anySequence_parameters")
    public void anySequence(Predicate[] matches, boolean expected) {
        Matcher matcher = new Matcher(new String[] {"1", "2", "3", "4", "5"});
        
        assertEquals(expected, matcher.anySequence(matches));
    }
    
    static Stream<Arguments> anySequence_parameters() {
        return Stream.of(
            of(new Predicate[] {arg -> arg.equals("2"), arg -> arg.equals("3")}, true),
            of(new Predicate[] {arg -> arg.equals("3"), arg -> arg.equals("2")}, false)
        );
    }
    
    
    @Test
    public void anySequence_ThrowsException() {
       assertEquals(
            "Invalid number of matches specified.",
            assertThrows(IllegalArgumentException.class, () -> matcher.anySequence(arg -> true, arg -> true, arg -> true, arg -> true, arg -> true)).getMessage()
       );
    }
    
    
    @Test
    public void using() {
        Predicate<String[]> predicate = when(mock(Predicate.class).test(any())).thenReturn(true).getMock();
        
        assertTrue(matcher.using(predicate));
        verify(predicate).test(any());
    }
    
}
