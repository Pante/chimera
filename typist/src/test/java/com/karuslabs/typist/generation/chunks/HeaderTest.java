/*
 * The MIT License
 *
 * Copyright 2021 Karus Labs.
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
package com.karuslabs.typist.generation.chunks;

import com.karuslabs.utilitary.Source;
import java.time.*;
import java.util.stream.Stream;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.*;

import static com.karuslabs.typist.generation.chunks.Constants.VERSION;
import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.params.provider.Arguments.of;
import static org.mockito.Mockito.*;

class HeaderTest {
    
    static final LocalDateTime TIME = LocalDateTime.now();
    static final String HEADER = """
        // CHECKSTYLE:OFF
                                 
        import com.karuslabs.commons.command.Execution;
        import com.karuslabs.commons.command.tree.nodes.*;
        
        import com.mojang.brigadier.Command;
        import com.mojang.brigadier.suggestion.SuggestionProvider;
        import com.mojang.brigadier.tree.CommandNode;
        
        import java.util.*;
        import java.util.function.Predicate;
        
        import org.bukkit.command.CommandSender;
                                 
        /**
         * This file was generated at %s using Chimera %s
         */
        """.formatted(TIME, VERSION);
    
    Header header = new Header();
    Source source = new Source();
    
    
    @ParameterizedTest
    @MethodSource("emit_parameters")
    void emit(String pack, String expected) {
        try (var date = mockStatic(LocalDateTime.class)) {
            date.when(LocalDateTime::now).thenReturn(TIME);
            
            header.emit(source, pack);
            assertEquals(expected, source.toString());
        }
    }
    
    static Stream<Arguments> emit_parameters() {
        return Stream.of(
            of("", HEADER),
            of("com.karuslabs.something", "package com.karuslabs.something;" + System.lineSeparator() + System.lineSeparator() + HEADER)
        );
    }

} 
