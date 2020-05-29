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

import com.karuslabs.commons.MockServer;

import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;

import java.util.*;
import java.util.stream.Stream;

import org.bukkit.*;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.*;

import org.mockito.junit.jupiter.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.params.provider.Arguments.of;
import static org.mockito.Mockito.*;
import static org.mockito.quality.Strictness.LENIENT;


@MockitoSettings(strictness = LENIENT)
class PlayerTypeTest {
    
    static {
        Player player = when(mock(Player.class).getName()).thenReturn("Pante").getMock();
        Server server = when(mock(MockServer.class).getOnlinePlayers()).thenReturn(List.of(player)).getMock();
        when(server.getPlayerExact("Pante")).thenReturn(player);
        
        try {
            var field = Bukkit.class.getDeclaredField("server");
            field.setAccessible(true);
            field.set(null, server);
            
        } catch (NoSuchFieldException | SecurityException | IllegalArgumentException | IllegalAccessException e) {
            // ignored
        }
    }
    
    
    static Player player = mock(Player.class);
    static CommandSender sender = mock(CommandSender.class);
    PlayerType type = new PlayerType();
    
    
    @Test
    void parse() throws CommandSyntaxException {
        assertNotNull(type.parse(new StringReader("Pante")));
    }
    
    
    @Test
    void parse_throws_exception() throws CommandSyntaxException {
        assertEquals(
            "Unknown player: invalid",
            assertThrows(CommandSyntaxException.class, () -> type.parse(new StringReader("invalid"))).getRawMessage().toString()
        );
    }
    
    
    @ParameterizedTest
    @MethodSource("listSuggestions_parameters")
    void listSuggestions(CommandSender source, String remaining, boolean see, int times) {
        CommandContext<CommandSender> context = when(mock(CommandContext.class).getSource()).thenReturn(source).getMock();
        SuggestionsBuilder builder = when(mock(SuggestionsBuilder.class).getRemaining()).thenReturn(remaining).getMock();
        
        when(player.canSee(any())).thenReturn(see);
        
        type.listSuggestions(context, builder);
        
        verify(builder, times(times)).suggest("Pante");
    }
    
    static Stream<Arguments> listSuggestions_parameters() {
        return Stream.of(
            of(player, "P", true, 1),
            of(player, "P", false, 0),
            of(player, "invalid", false, 0),
            of(sender, "P", false, 1),
            of(sender, "invalid", false, 0)
        );
    }
    
    
    @Test
    void getExamples() {
        assertEquals(List.of("Bob", "Pante"), type.getExamples());
    }
    

} 
