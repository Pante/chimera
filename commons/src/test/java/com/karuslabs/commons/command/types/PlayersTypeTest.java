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
import java.util.concurrent.ExecutionException;
import java.util.stream.Stream;

import org.bukkit.*;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.*;

import org.mockito.junit.jupiter.*;
import org.mockito.quality.Strictness;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.params.provider.Arguments.of;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class PlayersTypeTest {
    
    static Player pante = when(mock(Player.class).getName()).thenReturn("Pante").getMock();
    static Player player = mock(Player.class);
    static CommandSender sender = mock(CommandSender.class);
    
    static {
        Server server = when(mock(MockServer.class).getOnlinePlayers()).thenReturn(List.of(pante)).getMock();
        when(server.getPlayerExact("Pante")).thenReturn(pante);
        
        try {
            var field = Bukkit.class.getDeclaredField("server");
            field.setAccessible(true);
            field.set(null, server);
            
        } catch (NoSuchFieldException | SecurityException | IllegalArgumentException | IllegalAccessException e) {
            // ignored
        }
    }
    
    PlayersType type = new PlayersType();
    
    
    @ParameterizedTest
    @MethodSource("parse_parameters")
    void parse(String line, List<Player> players) throws CommandSyntaxException {
        assertEquals(players, type.parse(new StringReader(line)));
    }
    
    
    static Stream<Arguments> parse_parameters() {
        return Stream.of(
            of("\"@r, Pante\"", List.of(pante, pante)),
            of("@a", List.of(pante)),
            of("Pante", List.of(pante))
        );
    }
    
    
    @Test
    void parse_throws_invalid_exception() {
        assertEquals(
            "'@a' cannot be used in a list of players.",
            assertThrows(CommandSyntaxException.class, () -> type.parse(new StringReader("\"@a, Pante\""))).getRawMessage().toString()
        );
    }
    
        
    @Test
    void parse_throws_unknown_exception() {
        assertEquals(
            "Unknown player or selector: invalid",
            assertThrows(CommandSyntaxException.class, () -> type.parse(new StringReader("invalid"))).getRawMessage().toString()
        );
    }
    
    
    @ParameterizedTest
    @MethodSource("listSuggestions_parameters")
    void listSuggestions(CommandSender source, boolean see, SuggestionsBuilder expected) throws InterruptedException, ExecutionException {
        CommandContext<CommandSender> context = when(mock(CommandContext.class).getSource()).thenReturn(source).getMock();
        when(player.canSee(any())).thenReturn(see);
        
        var future = type.listSuggestions(context, new SuggestionsBuilder(expected.getInput(), 0));
        
        assertEquals(expected.build(), future.get());
    }
    
    
    static Stream<Arguments> listSuggestions_parameters() {
        return Stream.of(
            of(player, true, new SuggestionsBuilder("P", 0).suggest("Pante")),
            of(sender, true, new SuggestionsBuilder("P", 0).suggest("Pante")),
            of(player, true, new SuggestionsBuilder("Invalid", 0)),
            of(player, false, new SuggestionsBuilder("P", 0)),
            
            of(player, true, new SuggestionsBuilder("@", 0).suggest("@a", PlayersType.ALL).suggest("@r", PlayersType.RANDOM)),
            of(player, true, new SuggestionsBuilder("\"", 0).suggest("\"Pante\"").suggest("\"@r\"", PlayersType.RANDOM)),
            of(player, true, new SuggestionsBuilder("\"Bob, P", 0).suggest("\"Bob, Pante\"")),
            of(player, true, new SuggestionsBuilder("\"Bob, P\"", 0).suggest("\"Bob, Pante\""))
        );
    }
    
    
    @Test
    void getExamples() {
        assertEquals(List.of("@a", "@r", "\"Pante, Kevaasaurus\""), type.getExamples());
    }

} 
