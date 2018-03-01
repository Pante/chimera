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
package com.karuslabs.spigot.plugin.processor.processors;

import com.karuslabs.spigot.plugin.processor.ProcessorException;

import java.util.*;
import java.util.stream.Stream;

import org.apache.maven.plugin.logging.Log;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.*;

import static com.karuslabs.spigot.plugin.processor.Yaml.TEST;
import static java.util.Arrays.asList;
import static java.util.Collections.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.params.provider.Arguments.of;
import static org.mockito.Mockito.*;


class PluginProcessorTest {
    
    Log log = mock(Log.class);
    PluginProcessor processor = spy(new PluginProcessor(log, asList(getClass().getResource("").getPath()), true));
    
    
    @ParameterizedTest
    @CsvSource({"help me, false, 0", ", false, 1", "wrong, true, 1", "help, true, 1"})
    void execute(String main, boolean override, int times) {
        PluginProcessor processor = spy(new PluginProcessor(log, asList(getClass().getResource("").getPath()), override));
        doReturn(singleton("help me")).when(processor).load();
        TEST.set("main", main);
        
        processor.execute(TEST, "main");
        
        verify(processor, times(times)).setMain(TEST, singleton("help me"));
    }
    
    
    @Test
    void execute_ThrowsException() {
        PluginProcessor processor = spy(new PluginProcessor(log, asList(getClass().getResource("").getPath()), false));
        doReturn(singleton("help me")).when(processor).load();
        TEST.set("main", "non-null");
        
        assertEquals(
            "Invalid main class specified: non-null, main must be a subclass of JavaPlugin",
            assertThrows(ProcessorException.class, () -> processor.execute(TEST, "main")).getMessage()
        );
    }
    
    
    @Test
    void load() {
        String[] classes = processor.load().toArray(new String[] {});
        
        assertEquals(1, classes.length);
        assertEquals("com.karuslabs.spigot.plugin.processor.processors.TestPlugin", classes[0]);
    }
    
    
    @Test
    void setMain() {
        processor.setMain(TEST, singleton("help me"));
        
        verify(log).info("Detected and setting main to: help me");
        assertEquals("help me", TEST.getString("main"));
    }
    
    
    @ParameterizedTest
    @MethodSource("setMain_ThrowsException_parameters")
    void setMain_ThrowsException(List<String> detected, String message) {
        assertEquals(
            message,
            assertThrows(ProcessorException.class, () -> processor.setMain(TEST, new HashSet<>(detected))).getMessage()
        );
    }
    
    static Stream<Arguments> setMain_ThrowsException_parameters() {
        return Stream.of(
            of(EMPTY_LIST, "No JavaPlugin subclass detected, main class must be specified manually"), 
            of(asList("a", "b"), "Multiple JavaPlugin subclasses detected, main class must be specified manually")
        );
    }

}
