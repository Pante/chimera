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

import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
class CartesianTypeTest {
    
    Location location = mock(Location.class);
    Block block = when(mock(Block.class).getLocation()).thenReturn(location).getMock();
    Player player = when(mock(Player.class).getTargetBlockExact(5)).thenReturn(block).getMock();
    SuggestionsBuilder builder = new SuggestionsBuilder("a b", 0);
    CommandContext<CommandSender> context = when(mock(CommandContext.class).getSource()).thenReturn(player).getMock();
    
    
    CartesianType<String> type = spy(new CartesianType<>() {
        @Override
        public ArgumentType<?> mapped() {
            throw new UnsupportedOperationException("Not supported yet.");
        }

        @Override
        public String parse(StringReader reader) throws CommandSyntaxException {
            throw new UnsupportedOperationException("Not supported yet.");
        }
    });
    
    
    @Test
    void listSuggestions() {
        type.listSuggestions(context, builder);
        
        verify(type).suggest(builder, context, location, new String[] {"a", "b"});
        verify(type).suggest(builder, context, new String[] {"a", "b"});
    }

} 
