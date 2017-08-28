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

import junitparams.*;

import org.junit.*;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;

import static java.util.Arrays.asList;
import static java.util.stream.Collectors.toList;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;


@RunWith(JUnitParamsRunner.class)
public class MatcherTest {
    
    @Rule
    public ExpectedException exception = ExpectedException.none();
    
    
    private Matcher matcher;
    
    
    public MatcherTest() {
    }
    
    
    @Test
    public void matcher() {
        Matcher matcher = new Matcher(new String[] {""});
        
        assertEquals(0, matcher.getFirst());
        assertEquals(1, matcher.getLast());
    }
    
    
    @Test
    public void all() {
        matcher.all();
        
        assertEquals(0, matcher.getFirst());
        assertEquals(4, matcher.getLast());
    }
    
    
    @Test
    public void starting() {
        matcher.starting(2);
        
    }
    
    
    @Test
    @Parameters({"0, 4", "1, 3"})
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
    
    
    @Test
    @Parameters({"-1, 3", "1, 1", "1, 5"})
    public void between_ThrowsException(int first, int last) {
        exception.expect(IndexOutOfBoundsException.class);

        matcher.between(first, last);
    }
    
    
    @Test
    public void stream() {
        Matcher matcher = new Matcher(new String[] {"1", "2", "3"}).starting(1);
        
        assertThat(matcher.stream().collect(toList()), equalTo(asList("2", "3")));
    }
    
    
    @Test
    @Parameters({"true, true, true", "true, false, false", "false, true, false"})
    public void exact(boolean match1, boolean match2, boolean expected) {
        Matcher matcher = new Matcher(new String[] {"1", "2"});
        
        Predicate<String> predicate1 = when(mock(Predicate.class).test("1")).thenReturn(match1).getMock();
        Predicate<String> predicate2 = when(mock(Predicate.class).test("2")).thenReturn(match2).getMock();
        
        assertEquals(expected, matcher.exact(predicate1, predicate2));
    }
    
    
    @Test
    public void exact_ThrowsException() {
        exception.expect(IllegalArgumentException.class);
        exception.expectMessage("Invalid number of matches specified.");
        
        Matcher matcher = new Matcher(new String[] {"1"});
        
        matcher.exact(arg -> true, arg -> true);
    }
    
    
    @Test
    @Parameters
    public void anySequence(Predicate[] matches, boolean expected) {
        Matcher matcher = new Matcher(new String[] {"1", "2", "3", "4", "5"});
        
        assertEquals(expected, matcher.anySequence(matches));
    }
    
    protected Object[] parametersForAnySequence() {
        return new Object[] {
            new Object[] {new Predicate[] {arg -> arg.equals("2"), arg -> arg.equals("3")}, true},
            new Object[] {new Predicate[] {arg -> arg.equals("3"), arg -> arg.equals("2")}, false}
        };
    }
    
    
    @Test
    public void anySequence_ThrowsException() {
       exception.expect(IllegalArgumentException.class);
       exception.expectMessage("Invalid number of matches specified.");
       
       Matcher matcher = new Matcher(new String[] {"1"});
       
       matcher.anySequence(arg -> true, arg -> true);
    }
    
    
    @Test
    public void using() {
        Predicate<String[]> predicate = when(mock(Predicate.class).test(any())).thenReturn(true).getMock();
        
        assertTrue(matcher.using(predicate));
        verify(predicate).test(any());
    }
    
}
