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
import com.karuslabs.commons.command.types.StringType;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.tree.LiteralCommandNode;

import java.util.List;
import java.util.stream.Stream;

import org.bukkit.Server;

import org.bukkit.craftbukkit.v1_15_R1.command.CraftCommandMap;

import org.bukkit.command.CommandSender;
import org.bukkit.plugin.Plugin;

import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.*;

import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.params.provider.Arguments.of;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
class NativeMapTest {
    
    static final String PREFIX = "prefix";
    
    Plugin plugin = mock(Plugin.class);
    CraftCommandMap craftmap = new CraftCommandMap(mock(Server.class));
    CommandDispatcher<CommandSender> dispatcher = mock(CommandDispatcher.class);
    NativeMap map;
    
    Literal<CommandSender> literal = Literal.of("literal").alias("l").build();
    LiteralCommandNode<CommandSender> node = mock(LiteralCommandNode.class);
    
    
    @BeforeEach
    void before() {
        map = new NativeMap(PREFIX, plugin, craftmap);
        map.dispatcher = dispatcher;
    }
    

    @Test
    void wrap_aliasable() {
        var command = map.wrap(literal);
        
        assertEquals("literal", command.getName());
        assertEquals("literal", command.getUsage());
        assertEquals(plugin, command.getPlugin());
        assertEquals(dispatcher, command.dispatcher);
        assertEquals(List.of("l"), command.getAliases());
    }
    
    
    @Test
    void wrap_non_aliasable() {
        var command = map.wrap(node);
        
        assertEquals(null, command.getName());
        assertEquals("/null", command.getUsage());
        assertEquals(plugin, command.getPlugin());
        assertEquals(dispatcher, command.dispatcher);
        assertEquals(List.of(), command.getAliases());
        
    }

} 
