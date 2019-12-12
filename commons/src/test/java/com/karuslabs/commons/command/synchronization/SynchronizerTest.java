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
package com.karuslabs.commons.command.synchronization;

import com.karuslabs.commons.command.tree.Tree;

import java.util.List;

import net.minecraft.server.v1_15_R1.*;

import org.bukkit.craftbukkit.v1_15_R1.CraftServer;
import org.bukkit.craftbukkit.v1_15_R1.entity.CraftPlayer;
import org.bukkit.craftbukkit.v1_15_R1.scheduler.CraftScheduler;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerCommandSendEvent;
import org.bukkit.plugin.*;

import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
class SynchronizerTest {
    
    Synchronizer synchronizer;
    Plugin plugin = mock(Plugin.class);
    CraftServer craftserver = mock(CraftServer.class);
    DedicatedServer server = mock(DedicatedServer.class);
    CraftScheduler scheduler = mock(CraftScheduler.class);
    PluginManager manager = mock(PluginManager.class);
    ServicesManager services = mock(ServicesManager.class);
    CommandDispatcher wrapper = mock(CommandDispatcher.class);
    com.mojang.brigadier.CommandDispatcher<CommandListenerWrapper> dispatcher = new com.mojang.brigadier.CommandDispatcher();
    
    
    @BeforeEach
    void before() {
        when(plugin.getServer()).thenReturn(craftserver);
        when(craftserver.getServer()).thenReturn(server);
        when(craftserver.getScheduler()).thenReturn(scheduler);
        when(craftserver.getPluginManager()).thenReturn(manager);
        when(craftserver.getServicesManager()).thenReturn(services);
        
        server.server = craftserver;
        server.commandDispatcher = when(wrapper.a()).thenReturn(dispatcher).getMock();
        
        synchronizer = spy(Synchronizer.of(plugin));
    }
    
    
    @Test
    void of() {
        reset(manager);
        
        var synchronizer = Synchronizer.of(plugin);
        
        verify(manager).registerEvents(synchronizer, plugin);
    }
    
    
    @Test
    void synchronize() {
        List<CraftPlayer> online = List.of(mock(CraftPlayer.class));
        when(craftserver.getOnlinePlayers()).thenReturn(online);
        doNothing().when(synchronizer).synchronize(any(Player.class));
        
        synchronizer.synchronize();
        
        verify(synchronizer).synchronize(any(Player.class));
    }
    
    
    @Test
    void synchronize_player() {
        Player player = mock(Player.class);
        doNothing().when(synchronizer).synchronize(any(), any());
        
        synchronizer.synchronize(player);
        
        verify(manager).callEvent(any(SynchronizationEvent.class));
        verify(synchronizer).synchronize(eq(player), any());
    }
    
    
    @Test
    void synchronize_player_commands() {
        synchronizer.tree = mock(Tree.class);
        
        EntityPlayer entity = mock(EntityPlayer.class);
        entity.playerConnection = mock(PlayerConnection.class);
        
        CraftPlayer player = when(mock(CraftPlayer.class).getHandle()).thenReturn(entity).getMock();
        
        synchronizer.synchronize(player, List.of());
        
        verify(synchronizer.tree).map(any(), any(), any(), any());
        verify(entity.playerConnection).sendPacket(any(PacketPlayOutCommands.class));
    }
    
    
    @Test
    void synchronize_event_ignore_event() {
        synchronizer.synchronize(new SynchronizationEvent(null, List.of()));
        
        assertNull(synchronizer.synchronization.get());
    }
    
    
    @Test
    void synchronize_event() {
        synchronizer.synchronize(mock(PlayerCommandSendEvent.class));
        var task = synchronizer.synchronization.get();
        
        assertTrue(task.running);
        verify(services).register(Synchronization.class, task, plugin, ServicePriority.Low);
    }
    
    
    @Test
    void load() {
        synchronizer.dispatcher = null;
        synchronizer.load(null);
        
        assertSame(synchronizer.dispatcher, dispatcher);
    }

} 
