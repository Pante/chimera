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

import com.karuslabs.commons.command.arguments.Arguments;
import com.karuslabs.commons.command.parser.Parser;
import com.karuslabs.commons.locale.providers.Provider;

import java.util.ArrayList;

import org.bukkit.command.SimpleCommandMap;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.Plugin;

import org.junit.jupiter.api.Test;

import static java.util.Collections.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;


class CommandsTest {
    
    private Commands commands;
    private Plugin plugin;
    
    
    CommandsTest() {
        plugin = when(mock(Plugin.class).getServer()).thenReturn(new StubServer(mock(SimpleCommandMap.class))).getMock();
        when(plugin.getName()).thenReturn("name");
        commands = spy(new Commands(plugin, Provider.NONE));
        commands.map = spy(commands.map);
    }
    
    
    @Test
    void load() {
        doNothing().when(commands).load(any(Parser.class), anyString());
        
        commands.load("path.yml");
        
        verify(commands).load(any(Parser.class), eq("path.yml"));
    }
    
    
    @Test
    void load_Parser() {
        Parser parser = when(mock(Parser.class).parse(any())).thenReturn(new ArrayList<>()).getMock();
        
        commands.load(parser, "command/commands.yml");
        
        verify(parser).parse(any(YamlConfiguration.class));
        verify(commands.getProxiedCommandMap()).registerAll("name", EMPTY_LIST);
    }
    
    
    @Registration({"command"})
    private static class Annotation implements CommandExecutor {

        @Override
        public boolean execute(Context context, Arguments arguments) {
            return true;
        }
        
    }
    
    
    @Test
    void register_Registration() {
        Annotation annotation = new Annotation();
        
        doReturn(null).when(commands).register(annotation, "command");
        
        commands.registerAnnotated(annotation);
        
        verify(commands).register(annotation, "command");
    }
    
    
    
    @Registration({"a"})
    @Registration({"b"})
    private static class Annotations implements CommandExecutor {

        @Override
        public boolean execute(Context context, Arguments arguments) {
            return true;
        }
        
    }
    
    
    @Test
    void register_Registrations() {
        Annotations annotations = new Annotations();
        
        doReturn(null).when(commands).register(any(), any(String.class));
        
        commands.registerAnnotated(annotations);
        
        verify(commands).register(annotations, "a");
        verify(commands).register(annotations, "b");
    }
    
    
    @Test
    void register_ThrowsException() {
        assertEquals("CommandExecutor has no registrations", 
                    assertThrows(IllegalArgumentException.class, () -> commands.registerAnnotated(CommandExecutor.NONE)).getMessage()
        );
    }
    
    
    @Test
    void register_Names() {
        Command child = mock(Command.class);
        Command parent = when(mock(Command.class).getSubcommands()).thenReturn(singletonMap("child", child)).getMock();
        
        doReturn(parent).when(commands.map).getCommand("parent");
        
        commands.register(CommandExecutor.ALIASES, "parent", "child");
        
        verify(child).setExecutor(CommandExecutor.ALIASES);
    }
            
}
