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

import junitparams.*;

import org.bukkit.Server;
import org.bukkit.command.SimpleCommandMap;
import org.bukkit.plugin.Plugin;

import org.junit.*;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;

import static java.util.Arrays.asList;
import static java.util.Collections.*;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;


@RunWith(JUnitParamsRunner.class)
public class ProxiedCommandMapTest {
    
    @Rule
    public ExpectedException exception = ExpectedException.none();
    
    private static final Plugin PLUGIN = mock(Plugin.class);
    private static final Command COMMAND = when(mock(Command.class).getPlugin()).thenReturn(PLUGIN).getMock();
    private ProxiedCommandMap proxy;
    private SimpleCommandMap map;
    
    
    public ProxiedCommandMapTest() {
        map = mock(SimpleCommandMap.class);
        proxy = new ProxiedCommandMap(new StubServer(map));
    }
    
    
    @Test
    public void proxiedCommandMap_ThrowsException() {
        exception.expect(IllegalArgumentException.class);
        exception.expectMessage("If you are reading this message, you're screwed.");
        
        Server server = mock(Server.class);
        ProxiedCommandMap proxy = new ProxiedCommandMap(server);
    }
    
    
    @Test
    public void registerAll() {
        proxy.registerAll("",  singletonList(COMMAND));
        verify(map).register("", COMMAND);
    }
    
    
    @Test
    @Parameters
    public void getCommand(org.bukkit.command.Command command, Command expected) {
        when(map.getCommand("")).thenReturn(command);
        assertEquals(expected, proxy.getCommand(""));
    }
    
    protected Object[] parametersForGetCommand() {
        org.bukkit.command.Command command = mock(org.bukkit.command.Command.class);
        return new Object[] {
            new Object[] {COMMAND, COMMAND},
            new Object[] {command, null}
        };
    }
    
    
    @Test
    public void getCommands() {
        org.bukkit.command.Command bukkit = mock(Command.class);
        Command command = mock(Command.class);
        when(map.getCommands()).thenReturn(asList(COMMAND, bukkit, command));
        
        assertThat(proxy.getCommands(PLUGIN), equalTo(singletonMap(null, COMMAND)));
    }
    
}
