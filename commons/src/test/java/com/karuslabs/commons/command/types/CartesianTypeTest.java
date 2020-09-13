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
package com.karuslabs.commons.command.types;

import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;

import java.util.concurrent.ExecutionException;

import net.minecraft.server.v1_16_R2.*;

import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;


class CartesianTypeTest {
    
    Location location = mock(Location.class);
    Block block = when(mock(Block.class).getLocation()).thenReturn(location).getMock();
    Player player = when(mock(Player.class).getTargetBlockExact(5)).thenReturn(block).getMock();
    SuggestionsBuilder builder = new SuggestionsBuilder("a b", 0);
    CommandContext<CommandSender> context = mock(CommandContext.class);
    
    
    CartesianType<String> type = spy(new CartesianType<>() {
        @Override
        public String parse(StringReader reader) throws CommandSyntaxException {
            throw new UnsupportedOperationException("Not supported yet.");
        }

        @Override
        public ArgumentType<?> mapped() {
            throw new UnsupportedOperationException("Not supported yet.");
        }
    });
    
    
    @Test
    void listSuggestions() throws InterruptedException, ExecutionException {
        assertTrue(type.listSuggestions(player, context, builder).get().isEmpty());
        
        verify(type).suggest(builder, new String[] {"a", "b"}, location);
        verify(type).suggest(builder, new String[] {"a", "b"});
    }
    
    
    @Test
    void listSuggestions_location() {
        var builder = mock(SuggestionsBuilder.class);
        var location = mock(Location.class);
        
        type.suggest(builder, new String[0], location);
        
        verifyNoInteractions(builder);
        verifyNoInteractions(location);
    }

}


class CartesianDimensionalTypeTest {
    
    Location location = new Location(null, 1, 2, 3);
    SuggestionsBuilder builder = mock(SuggestionsBuilder.class);
    
    
    
    @Nested class Cartesian2DTest {

        Cartesian2DType<String> type = new Cartesian2DType<>() {
            @Override
            public String parse(StringReader reader) throws CommandSyntaxException {
                throw new UnsupportedOperationException("Not supported yet.");
            }
        };

        @Test
        void suggest_all() {
            when(builder.getRemaining()).thenReturn("");
            type.suggest(builder, new String[0], location);

            verify(builder).suggest("1.0");
            verify(builder).suggest("1.0 3.0");
        }


        @Test
        void suggest_z() {
            when(builder.getRemaining()).thenReturn("4");
            type.suggest(builder, new String[] {"4"}, location);

            verify(builder).suggest("4 3.0");
        }


        @Test
        void mapped() {
            assertEquals(ArgumentVec2.class, type.mapped().getClass());
        }
    
    }
    
    
    @Nested class Cartesian3DTest {
    
        Cartesian3DType<String> type = new Cartesian3DType<>() {
            @Override
            public String parse(StringReader reader) throws CommandSyntaxException {
                throw new UnsupportedOperationException("Not supported yet.");
            }
        };


        @Test
        void suggest_all() {
            when(builder.getRemaining()).thenReturn("");
            type.suggest(builder, new String[0], location);

            verify(builder).suggest("1.0");
            verify(builder).suggest("1.0 2.0");
            verify(builder).suggest("1.0 2.0 3.0");
        }


        @Test
        void suggest_yz() {
            when(builder.getRemaining()).thenReturn("4");
            type.suggest(builder, new String[] {"4"}, location);

            verify(builder).suggest("4 2.0 3.0");
        }


        @Test
        void suggest_z() {
            when(builder.getRemaining()).thenReturn("4");
            type.suggest(builder, new String[] {"4", "5"}, location);

            verify(builder).suggest("4 5 3.0");
        }


        @Test
        void mapped() {
            assertEquals(ArgumentVec3.class, type.mapped().getClass());
        }
    
    }
    
}






