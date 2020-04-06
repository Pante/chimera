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
package com.karuslabs.commons.command.synchronization;

import java.util.stream.Stream;

import org.bukkit.Server;
import org.bukkit.event.server.ServiceUnregisterEvent;
import org.bukkit.plugin.*;
import org.bukkit.scheduler.BukkitScheduler;

import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.*;
import org.mockito.junit.jupiter.*;

import static org.junit.jupiter.params.provider.Arguments.of;
import static org.mockito.Mockito.*;
import static org.mockito.quality.Strictness.LENIENT;


@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = LENIENT)
class PulseListenerTest {
    
    PulseListener listener;
    Plugin plugin;
    Synchronizer synchronizer = mock(Synchronizer.class);
    Server server = mock(Server.class);
    BukkitScheduler scheduler = mock(BukkitScheduler.class);
    PluginManager manager = mock(PluginManager.class);
    ServicesManager services = mock(ServicesManager.class);
    ServiceUnregisterEvent event = mock(ServiceUnregisterEvent.class);
    
    
    @BeforeEach
    void before() {
        plugin = when(mock(Plugin.class).getServer()).thenReturn(server).getMock();
        when(server.getScheduler()).thenReturn(scheduler);
        when(server.getServicesManager()).thenReturn(services);
        when(server.getPluginManager()).thenReturn(manager);
        
        listener = spy(new PulseListener(synchronizer, plugin));
    }
    
    
    @ParameterizedTest
    @CsvSource({"true, 0", "false, 1"})
    void register(boolean provided, int times) {
        when(services.isProvidedFor(SynchronizationListener.class)).thenReturn(provided);
        
        listener.register();
        
        verify(services, times(times)).register(eq(SynchronizationListener.class), any(SynchronizationListener.class), eq(plugin), eq(ServicePriority.Normal));
    }
    
    @ParameterizedTest
    @MethodSource("listen_parameters")
    void listen(Class<?> service, int times) {
        RegisteredServiceProvider provider = when(mock(RegisteredServiceProvider.class).getService()).thenReturn(service).getMock();
        when(event.getProvider()).thenReturn(provider);
        
        doNothing().when(listener).register();
        
        listener.listen(event);
        
        verify(listener, times(times)).register();
    }
    
    static Stream<Arguments> listen_parameters() {
        return Stream.of(
            of(Object.class, 0),
            of(SynchronizationListener.class, 1)
        );
    }
    
} 
