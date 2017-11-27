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
package com.karuslabs.commons.command.parser;

import com.karuslabs.commons.annotation.JDK9;
import com.karuslabs.commons.command.completion.Completion;

import java.util.*;;

import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static com.karuslabs.commons.configuration.Yaml.COMMANDS;
import static java.util.Collections.singletonList;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.TestInstance.Lifecycle.PER_CLASS;


@TestInstance(PER_CLASS)
class CompletionElementTest {
    
    CompletionElement element = new CompletionElement();
    
    
    @Test
    void getDeclarations() {
        @JDK9("Replace with Map.of(...)")
        Map<String, Completion> completions = new HashMap<>();
        completions.put("NONE", Completion.NONE);
        completions.put("PLAYER_NAMES", Completion.PLAYER_NAMES);
        completions.put("WORLD_NAMES", Completion.WORLD_NAMES);
        
        assertEquals(completions, element.getDeclarations());
    }
    
    
    @Test
    void getDeclaredKey() {
        assertEquals("bundle", element.getDeclaredKey(COMMANDS, "commands.brush.translation"));
    }
    
    
    @ParameterizedTest
    @CsvSource({"true, declare.translations.bundle.embedded", "false, declare"})
    void check(boolean expected, String key) {
        assertEquals(expected, element.check(COMMANDS, key));
    }
    
    
    @Test
    void handle() {
        assertEquals(singletonList("path1"), element.handle(COMMANDS, "declare.translations.bundle.embedded").getCompletions());
    }
    
}