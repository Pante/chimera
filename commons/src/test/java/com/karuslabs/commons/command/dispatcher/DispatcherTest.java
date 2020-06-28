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
package com.karuslabs.commons.command.dispatcher;

import com.karuslabs.commons.command.tree.nodes.*;

import com.mojang.brigadier.tree.CommandNode;

import java.util.*;

import net.minecraft.server.v1_16_R1.*;

import org.bukkit.command.CommandSender;
import org.bukkit.plugin.*;

import org.bukkit.craftbukkit.v1_16_R1.CraftServer;
import org.bukkit.craftbukkit.v1_16_R1.command.CraftCommandMap;
import org.bukkit.craftbukkit.v1_16_R1.entity.CraftPlayer;
import org.bukkit.craftbukkit.v1_16_R1.scheduler.CraftScheduler;

import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;


class DispatcherTest {
    
    Dispatcher dispatcher;
    Plugin plugin = when(mock(Plugin.class).getName()).thenReturn("test").getMock();
    CraftServer craftserver = mock(CraftServer.class);
    DedicatedServer server = mock(DedicatedServer.class);
    CraftCommandMap map = when(mock(CraftCommandMap.class).register(any(String.class), any())).thenReturn(true).getMock();
    CraftScheduler scheduler = mock(CraftScheduler.class);
    PluginManager manager = mock(PluginManager.class);
    com.mojang.brigadier.CommandDispatcher<CommandListenerWrapper> internal = new com.mojang.brigadier.CommandDispatcher();
    CommandDispatcher wrapper = when(mock(CommandDispatcher.class).a()).thenReturn(internal).getMock();
    
    
    @BeforeEach
    void before() {
        when(plugin.getServer()).thenReturn(craftserver);
        when(craftserver.getServer()).thenReturn(server);
        when(craftserver.getCommandMap()).thenReturn(map);
        when(craftserver.getScheduler()).thenReturn(scheduler);
        when(craftserver.getPluginManager()).thenReturn(manager);
        
        server.server = craftserver;
        when(server.getCommandDispatcher()).thenReturn(wrapper);
        
        dispatcher = spy(Dispatcher.of(plugin));
    }

    
    @Test
    void of() {
        reset(manager);
        
        var dispatcher = Dispatcher.of(plugin);
        
        assertSame(dispatcher, ((SpigotMap) dispatcher.getRoot().map()).dispatcher);
    }
    
    
    @Test
    void register_commands() {
        CommandNode<CommandSender> a = Literal.of("a").build();
        var commands = Map.of("a", a);
        
        dispatcher.register(commands);
        
        assertSame(a, dispatcher.getRoot().getChild("a"));
    }
    
    
    @Test
    void register_builder() {
        var a = dispatcher.register(Literal.of("a"));
        
        assertSame(a, dispatcher.getRoot().getChild("a"));
    }
    
    
    @Test
    void update() {
        var player = mock(CraftPlayer.class);
        when(craftserver.getOnlinePlayers()).thenReturn(List.of(player));
        
        dispatcher.getRoot().addChild(Literal.of("a").build());
        
        dispatcher.update();
        
        
        assertNotNull(dispatcher.dispatcher.getRoot().getChild("a"));
        verify(player).updateCommands();
    }
    
    
    @Test
    void update_server_reload() {
        dispatcher.getRoot().addChild(Literal.of("a").build());
        dispatcher.dispatcher = null;
        
        dispatcher.update(null);
        
        assertNotNull(dispatcher.dispatcher.getRoot().getChild("a"));
        assertSame(internal, dispatcher.dispatcher);
    }
    
} 
