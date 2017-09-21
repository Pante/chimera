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
package com.karuslabs.commons.command.completion;

import com.karuslabs.commons.command.StubServer;

import java.util.ArrayList;

import org.bukkit.World;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import org.junit.jupiter.api.Test;

import static java.util.Arrays.asList;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;


public class CompletionTest {
    
    private StubServer server = new StubServer(new ArrayList<>(), new ArrayList<>());
    private CommandSender sender = when(mock(CommandSender.class).getServer()).thenReturn(server).getMock();

    
    @Test
    public void player_names() {
        Player bob = when(mock(Player.class).getName()).thenReturn("bob").getMock();
        server.getOnlinePlayers().add(bob);
        
        Player bobby = when(mock(Player.class).getName()).thenReturn("bobby").getMock();
        server.getOnlinePlayers().add(bobby);
                
        assertEquals(asList("bobby"), Completion.PLAYER_NAMES.complete(sender, "bobb"));
    }
    
    
    @Test
    public void world_names() {
        World bob = when(mock(World.class).getName()).thenReturn("bob").getMock();
        server.getWorlds().add(bob);
        
        World bobby = when(mock(World.class).getName()).thenReturn("bobby").getMock();
        server.getWorlds().add(bobby);
        
        assertEquals(asList("bobby"), Completion.WORLD_NAMES.complete(sender, "bobb"));
    }
    
}
