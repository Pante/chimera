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
package com.karuslabs.commons.command;

import java.util.Locale;
import java.util.stream.Stream;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.*;

import static java.util.Locale.getDefault;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.params.provider.Arguments.of;
import static org.mockito.Mockito.*;


public class ContextTest {
    
    private static final Player PLAYER = when(mock(Player.class).getLocale()).thenReturn("en_GB").getMock();
    private static final CommandSender SENDER = mock(CommandSender.class);
    private static final Command COMMAND = mock(Command.class);
  
    
    @Test
    public void update() {
        Command parent = mock(Command.class);
        Command command = mock(Command.class);
        
        Context context = new Context(null, null, parent, command);
        context.update("label", COMMAND);
        
        assertEquals("label", context.getLabel());
        assertEquals(command, context.getParentCommand());
        assertEquals(COMMAND, context.getCommand());
    }
    
    
    @ParameterizedTest
    @MethodSource("getPlayer_parameters")
    public void getPlayer(CommandSender sender, Player expected) {
        assertEquals(expected, new Context(sender, null, null, COMMAND).getPlayer());
    }
    
    static Stream<Arguments> getPlayer_parameters() {
        return Stream.of(of(SENDER, null), of(PLAYER, PLAYER));
    }
    
    
    @ParameterizedTest
    @MethodSource("getLocale_parameters")
    public void getLocale(CommandSender sender, Locale expected) {
        Context context = new Context(sender, null, null, COMMAND);
        assertEquals(expected, context.getLocale());
    }
    
    static Stream<Arguments> getLocale_parameters() {
        return Stream.of(of(PLAYER, new Locale("en", "GB")), of(SENDER, getDefault()));
    }
    
}
