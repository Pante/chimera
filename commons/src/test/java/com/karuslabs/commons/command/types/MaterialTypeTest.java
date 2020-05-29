/*
 * The MIT License
 *
 * Copyright 2019 Karus Labs.
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
package com.karuslabs.commons.command.types;

import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;

import java.util.List;
import java.util.logging.Logger;

import org.bukkit.*;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;


class MaterialTypeTest {
       
    static {
        Logger logger = mock(Logger.class);
        Server server = when(mock(Server.class).getLogger()).thenReturn(logger).getMock();
        
        try {
            var field = Bukkit.class.getDeclaredField("server");
            field.setAccessible(true);
            field.set(null, server);
            
        } catch (NoSuchFieldException | SecurityException | IllegalArgumentException | IllegalAccessException e) {
            // ignored
        }
    }
    
    MaterialType type = new MaterialType();
    SuggestionsBuilder builder = mock(SuggestionsBuilder.class);
    
    
    @Test
    void parse() throws CommandSyntaxException {
        assertEquals(Material.CACTUS, type.parse(new StringReader("CACTUS")));
    }
    
    
    @Test
    void parse_throws_exception() throws CommandSyntaxException {
        assertEquals(
            "Unknown material: invalid",
            assertThrows(CommandSyntaxException.class, () -> type.parse(new StringReader("invalid"))).getRawMessage().toString()
        );
    }
    
    
    @Test
    void listSuggestions() {
        type.listSuggestions(null, when(builder.getRemaining()).thenReturn("cactu").getMock());
        
        verify(builder).suggest("cactus");
    }
    
    
    @Test
    void getExamples() {
        assertEquals(List.of("flint_and_steel", "tnt"), type.getExamples());
    }

} 
