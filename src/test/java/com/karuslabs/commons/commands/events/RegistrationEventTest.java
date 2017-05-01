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
package com.karuslabs.commons.commands.events;

import com.karuslabs.commons.commands.Command;

import java.util.ArrayList;

import org.junit.*;
import org.junit.rules.ExpectedException;

import static org.mockito.Mockito.*;


public class RegistrationEventTest {
    
    @Rule
    public ExpectedException exception = ExpectedException.none();
    
    private RegistrationEvent event;
    
    
    public RegistrationEventTest() {
        event = new RegistrationEvent(new ArrayList<>(0));
    }
    
    
    @Test
    public void getCommands_ThrowsException() {
        exception.expect(UnsupportedOperationException.class);
        event.getCommands().add(mock(Command.class));
    }
    
}
