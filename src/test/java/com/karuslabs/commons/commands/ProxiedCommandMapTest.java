/*
 * Copyright (C) 2017 Karus Labs
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.karuslabs.commons.commands;

import com.karuslabs.commons.test.StubServer;

import java.util.*;

import junitparams.*;

import org.bukkit.Server;
import org.bukkit.event.Cancellable;
import org.bukkit.plugin.*;

import org.junit.*;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;

import org.mockito.stubbing.Answer;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;


@RunWith(JUnitParamsRunner.class)
public class ProxiedCommandMapTest {
    
    @Rule
    public ExpectedException exception = ExpectedException.none();
    
    private ProxiedCommandMap proxy;
    private StubServer server;
    private Command command;
    
    
    public ProxiedCommandMapTest() {        
        server = new StubServer();
        proxy = spy(new ProxiedCommandMap(server));
        command = mock(Command.class);
    }
    
    
    @Test
    public void CommandMapProxy_ThrowsException() {
        exception.expect(IllegalArgumentException.class);
        exception.expectMessage("Server contains an incompatible CommandMap implementation");
        
        new ProxiedCommandMap(mock(Server.class));
    }

}
