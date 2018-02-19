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
package com.karuslabs.commons.command;

import java.util.Locale;
import java.util.function.Consumer;
import java.util.stream.Stream;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.*;

import static com.karuslabs.commons.locale.MessageTranslation.NONE;
import static java.util.Locale.getDefault;
import static org.bukkit.ChatColor.RED;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.params.provider.Arguments.of;
import static org.mockito.Mockito.*;


class CommandSourceTest {
    
    static final Player PLAYER = when(mock(Player.class).getLocale()).thenReturn("en_GB").getMock();
    static final Player INVALID = when(mock(Player.class).getLocale()).thenReturn("fuck_standards").getMock();
    static final CommandSender SENDER = mock(CommandSender.class);
    
    
    CommandSender sender = mock(CommandSender.class);
    CommandSource source = new CommandSource(sender, NONE);
    
    
    @ParameterizedTest
    @CsvSource({"true, 1, 0", "false, 0, 1"})
    void ifPlayerOrElse(boolean isPlayer, int playerTimes, int senderTimes) {
        source = new CommandSource(isPlayer ? PLAYER : SENDER, NONE);
        
        Consumer<Player> player = mock(Consumer.class);
        source.ifPlayer(player);
        verify(player, times(playerTimes)).accept(PLAYER);
        
        Consumer<CommandSource> sender = mock(Consumer.class);
        source.orElse(sender);
        verify(sender, times(senderTimes)).accept(source);
    }
    
    
    
    
    @Test
    void sendColouredTranslation() {
        source.sendColouredTranslation("&cKey");
        
        verify(sender).sendMessage(RED + "Key");
    }
    
    
    @Test
    void sendFormattedTranslation() {
        source.sendFormattedTranslation("&cKey", message -> message + " formatted");
        
        verify(sender).sendMessage("&cKey formatted");
    }
    
    
    @Test
    void sendRawTranslation() {
        source.sendRawTranslation("&cKey");
        
        verify(sender).sendMessage("&cKey");
    }
    
    
    @ParameterizedTest
    @MethodSource("player_parameters")
    void asPlayer(CommandSender sender, boolean expected) {
        assertEquals(expected, new CommandSource(sender, NONE).asPlayer() instanceof Player);
    }
    
    @ParameterizedTest
    @MethodSource("player_parameters")
    void isPlayer(CommandSender sender, boolean expected) {
        assertEquals(expected, new CommandSource(sender, NONE).isPlayer());
    }
    
    static Stream<Arguments> player_parameters() {
        return Stream.of(of(SENDER, false), of(PLAYER, true));
    }

    
    
    @ParameterizedTest
    @MethodSource("getLocale_parameters")
    void getLocale(CommandSender sender, Locale expected) {
        assertEquals(expected, new CommandSource(sender, NONE).getLocale());
    }
    
    static Stream<Arguments> getLocale_parameters() {
        return Stream.of(of(PLAYER, new Locale("en", "GB")), of(INVALID, getDefault()), of(SENDER, getDefault()));
    }
    
}
