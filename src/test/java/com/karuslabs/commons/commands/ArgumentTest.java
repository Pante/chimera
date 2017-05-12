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

import com.google.common.collect.Lists;

import com.karuslabs.mockkit.stub.StubServer;

import java.util.*;

import junitparams.*;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import org.junit.*;
import org.junit.runner.RunWith;

import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;


@RunWith(JUnitParamsRunner.class)
public class ArgumentTest {
    
    @ClassRule
    public static StubServer server = StubServer.INSTANCE;
    
    private Player player;
    
    
    public ArgumentTest() {
        Player bob = when(mock(Player.class).getName()).thenReturn("Bob").getMock();
        
        server.setOnlinePlayers(Lists.newArrayList(
            bob,
            when(mock(Player.class).getName()).thenReturn("bobby").getMock(),
            when(mock(Player.class).getName()).thenReturn("Joe").getMock()
        ));
        
        player = when(mock(Player.class).canSee(any(Player.class))).thenAnswer(a -> ((Player) a.getArgument(0)).getName().equals("Bob")).getMock();
        when(player.getServer()).thenReturn(server);
    }
    
    
    @Test
    @Parameters
    public void playerNames(CommandSender sender, String argument, List<String> expected) {
        List<String> returned = Argument.PLAYER_NAMES.complete(sender, null, argument);
        assertThat(returned, equalTo(expected));
    }
    
    protected Object[] parametersForPlayerNames() {
        CommandSender sender = mock(CommandSender.class);
        when(sender.getServer()).thenReturn(server);
        
        return new Object[] {
            new Object[] {sender, "B", Lists.newArrayList("Bob", "bobby")},
            new Object[] {sender, "J", Collections.singletonList("Joe")},
            new Object[] {player, "B", Collections.singletonList("Bob")}
        };
    }
    
    
    @Test
    public void none() {
        assertTrue(Argument.NONE.complete(null, null, null) == Collections.EMPTY_LIST);
    }
    
}
