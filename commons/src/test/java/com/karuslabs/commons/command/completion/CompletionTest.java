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
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static com.karuslabs.commons.command.completion.Completion.*;
import static java.util.Collections.*;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasItemInArray;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;


class CompletionTest {
    
    StubServer server = new StubServer(new ArrayList<>(), new ArrayList<>());
    CommandSender sender = when(mock(CommandSender.class).getServer()).thenReturn(server).getMock();

    
    @Test
    void none() {
        assertSame(EMPTY_LIST, NONE.complete(null, null));
    }
    
    
    @ParameterizedTest
    @CsvSource({"true, false", "false, true"})
    void player_names_player(boolean see, boolean empty) {
        Player source = when(mock(Player.class).canSee(any(Player.class))).thenReturn(see).getMock();
        when(source.getServer()).thenReturn(server);
        
        Player bob = when(mock(Player.class).getName()).thenReturn("bob").getMock();
        server.getOnlinePlayers().add(bob);
        
        Player bobby = when(mock(Player.class).getName()).thenReturn("bobby").getMock();
        server.getOnlinePlayers().add(bobby);
                
        assertEquals(empty, PLAYER_NAMES.complete(source, "bobb").isEmpty());
    }
    
    
    @Test
    void player_names_sender() {
        Player bob = when(mock(Player.class).getName()).thenReturn("bob").getMock();
        server.getOnlinePlayers().add(bob);
        
        Player bobby = when(mock(Player.class).getName()).thenReturn("bobby").getMock();
        server.getOnlinePlayers().add(bobby);
                
        assertEquals(singletonList("bobby"), PLAYER_NAMES.complete(sender, "bobb"));
    }
    
    
    @Test
    void world_names() {
        World bob = when(mock(World.class).getName()).thenReturn("bob").getMock();
        server.getWorlds().add(bob);
        
        World bobby = when(mock(World.class).getName()).thenReturn("bobby").getMock();
        server.getWorlds().add(bobby);
        
        assertEquals(singletonList("bobby"), WORLD_NAMES.complete(sender, "bobb"));
    }
    
    
    @Test
    void spam() {
        assertThat(SpamCompletion.MENU, hasItemInArray(SPAM.complete(sender, "").get(0)));
    }
    
}
