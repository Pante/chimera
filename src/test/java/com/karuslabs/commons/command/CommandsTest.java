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

import com.karuslabs.commons.command.parser.Parser;
import com.karuslabs.commons.locale.providers.Provider;

import java.util.ArrayList;

import org.bukkit.command.SimpleCommandMap;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.Plugin;

import org.junit.jupiter.api.Test;

import static java.util.Collections.EMPTY_LIST;
import static org.mockito.Mockito.*;


public class CommandsTest {
    
    private Commands commands;
    private Plugin plugin;
    
    
    public CommandsTest() {
        plugin = when(mock(Plugin.class).getServer()).thenReturn(new StubServer(mock(SimpleCommandMap.class))).getMock();
        when(plugin.getName()).thenReturn("name");
        commands = spy(new Commands(plugin, Provider.NONE));
        commands.map = spy(commands.map);
    }
    
    
    @Test
    public void load() {
        doNothing().when(commands).load(any(Parser.class), anyString());
        
        commands.load("path.yml");
        
        verify(commands).load(any(Parser.class), eq("path.yml"));
    }
    
    
    @Test
    public void load_Parser() {
        Parser parser = when(mock(Parser.class).parse(any())).thenReturn(new ArrayList<>()).getMock();
        
        commands.load(parser, "command/commands.yml");
        
        verify(parser).parse(any(YamlConfiguration.class));
        verify(commands.getProxiedCommandMap()).registerAll("name", EMPTY_LIST);
    }
            
}
