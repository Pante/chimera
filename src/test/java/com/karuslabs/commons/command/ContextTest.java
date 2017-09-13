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

import com.karuslabs.commons.locale.Translation;
import com.karuslabs.commons.locale.Translations;

import java.util.Locale;

import junitparams.*;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;


@RunWith(JUnitParamsRunner.class)
public class ContextTest {
    
    private static final Player PLAYER = when(mock(Player.class).getLocale()).thenReturn("en_GB").getMock();
    private static final CommandSender SENDER = mock(CommandSender.class);
    private static final Command COMMAND = when(mock(Command.class).getTranslations()).thenReturn(Translations.NONE).getMock();
  
    
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
    
    
    @Test
    @Parameters
    public void getPlayer(CommandSender sender, Player expected) {
        assertEquals(expected, new Context(sender, null, null, null).getPlayer());
    }
    
    protected Object[] parametersForGetPlayer() {
        return new Object[] {
            new Object[] {SENDER, null},
            new Object[] {PLAYER, PLAYER}
        };
    }
    
    
    @Test
    @Parameters
    public void getLocale(CommandSender sender, Locale expected) {
        Context context = new Context(sender, null, null, null);
        assertEquals(expected, context.getLocale());
    }
    
    protected Object[] parametersForGetLocale() {
        return new Object[] {
            new Object[] {PLAYER, new Locale("en", "GB")},
            new Object[] {SENDER, Locale.getDefault()}
        };
    }
    
    
    @Test
    public void getTranslation() {
        assertEquals(Translation.NONE, new Context(SENDER, null, null, COMMAND).getTranslation());
    }
    
}
