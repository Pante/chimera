/*
 * The MIT License
 *
 * Copyright 2020 Karus Labs.
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
package com.karuslabs.commons.command.dispatcher;

import com.karuslabs.commons.command.tree.nodes.*;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.tree.LiteralCommandNode;

import java.util.*;

import org.bukkit.Server;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.Plugin;

import org.bukkit.craftbukkit.v1_18_R2.command.CraftCommandMap;

import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;


class SpigotMapTest {
    
    static final String PREFIX = "prefix";
    
    Plugin plugin = mock(Plugin.class);
    CraftCommandMap craftmap = spy(new CraftCommandMap(mock(Server.class)));
    CommandDispatcher<CommandSender> dispatcher = mock(CommandDispatcher.class);
    SpigotMap map;
    
    Literal<CommandSender> literal = Literal.of("literal").alias("l").description("desc").build();
    LiteralCommandNode<CommandSender> node = when(mock(LiteralCommandNode.class).getUsageText()).thenReturn("usage").getMock();
    
    
    @BeforeEach
    void before() {
        map = new SpigotMap(PREFIX, plugin, craftmap);
        map.dispatcher = dispatcher;
    }
    
    
    @Test
    void register() {
        var command = map.register(literal);
        
        verify(craftmap).register(PREFIX, command);
        
        assertEquals("literal", command.getName());
        assertEquals("literal", command.getUsage());
        assertEquals(plugin, command.getPlugin());
        assertEquals(dispatcher, command.dispatcher);
        assertEquals(List.of("l"), command.getAliases());
    }
    
    
    @Test
    void register_already_exists() {
        craftmap.getKnownCommands().put("literal", mock(Command.class));
        
        assertNull(map.register(literal));
        verify(craftmap, times(0)).register(eq(PREFIX), any(Command.class));
    }
    
    
    @Test
    void unregister_unknown() {
        craftmap.getKnownCommands().put("something", mock(Command.class));
        
        assertNull(map.unregister("something"));
    }
    
    
    @Test
    void unregister_aliasable() {
        DispatcherCommand command = when(mock(DispatcherCommand.class).getAliases()).thenReturn(List.of("a", "b")).getMock();
        var other = mock(Command.class);
        var commands = craftmap.getKnownCommands();
        
        commands.clear();
        commands.put("literal", command);
        commands.put(PREFIX + ":literal", command);
        commands.put("a", command);
        commands.put(PREFIX + ":a", command);
        commands.put("b", other);
        commands.put(PREFIX + ":a", command);
        
        assertEquals(command, map.unregister("literal"));
        assertEquals(Map.of("b", other), commands);
        verify(command).unregister(craftmap);
    }
    

    @Test
    void wrap_literal() {
        var command = map.wrap(literal);
        
        assertEquals("literal", command.getName());
        assertEquals("desc", command.getDescription());
        assertEquals("literal", command.getUsage());
        assertEquals(plugin, command.getPlugin());
        assertEquals(dispatcher, command.dispatcher);
        assertEquals(List.of("l"), command.getAliases());
    }
    
    
    @Test
    void wrap_non_literal() {
        var command = map.wrap(node);
        
        assertEquals(null, command.getName());
        assertEquals("usage", command.getDescription());
        assertEquals("usage", command.getUsage());
        assertEquals(plugin, command.getPlugin());
        assertEquals(dispatcher, command.dispatcher);
        assertEquals(List.of(), command.getAliases());
    }

} 
