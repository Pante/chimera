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
package com.karuslabs.commons.command;

import com.karuslabs.commons.command.arguments.*;
import com.karuslabs.commons.command.suggestions.ClientsideProvider;
import com.karuslabs.commons.command.tree.nodes.*;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.BoolArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.suggestion.*;

import java.util.function.Predicate;

import net.minecraft.server.v1_13_R2.CommandListenerWrapper;

import org.bukkit.command.CommandSender;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
class DispatcherMapperTest {
    
    static final SuggestionProvider<CommandListenerWrapper> PROVIDER = (a, b) -> null;
    
    
    CommandDispatcher<CommandSender> dispatcher = new CommandDispatcher<>();
    DispatcherMapper mapper = spy(new DispatcherMapper(dispatcher));
    
    CommandSender sender = mock(CommandSender.class);
    CommandListenerWrapper listener = when(mock(CommandListenerWrapper.class).getBukkitSender()).thenReturn(sender).getMock();
    
    
    @Test
    void type() {
        var bool = BoolArgumentType.bool();
        
        assertEquals(bool, mapper.type(Argument.of("test", bool).build()));
        assertEquals(PlayerArgument.WORD, mapper.type(Argument.of("test", new PlayerArgument(null)).build()));
    }
    
    
    @Test
    void requirement() {
        assertSame(DispatcherMapper.TRUE, mapper.requirement(Literal.of("a").requires(null).build()));
        
        Predicate<CommandSender> predicate = mock(Predicate.class);
        mapper.requirement(Literal.of("a").requires(predicate).build()).test(listener);
        
        verify(predicate).test(sender);
    }
    
    
    @Test
    void suggestions_primitive_empty() {
        assertNull(mapper.suggestions(Argument.of("a", PlayerArgument.WORD).build()));
    }
    
    
    @Test
    void suggestions_typeargument() {
        var argument = new PlayerArgument(null);
        doReturn(PROVIDER).when(mapper).reparse(argument);
        
        assertSame(PROVIDER, mapper.suggestions(Argument.of("a", argument).build()));
        verify(mapper).reparse(argument);
    }
    
    
    @Test
    void suggestions_clientside() {
        assertSame(DispatcherMapper.CLIENT_SIDE.get(ClientsideProvider.ENTITIES), mapper.suggestions(Argument.of("a", PlayerArgument.WORD).suggests(ClientsideProvider.ENTITIES).build()));
    }
    
    
    @Test
    void suggestions_reparse() {
        SuggestionProvider<CommandSender> suggestor = (a, b) -> null;
        doReturn(PROVIDER).when(mapper).reparse(suggestor);
        
        assertSame(PROVIDER, mapper.suggestions(Argument.of("name", PlayerArgument.WORD).suggests(suggestor).build()));
    }
    
    
    @Test
    void reparse() throws CommandSyntaxException {
        SuggestionProvider provider = mock(SuggestionProvider.class);
        SuggestionsBuilder builder = mock(SuggestionsBuilder.class);
        CommandContext<CommandListenerWrapper> context = when(mock(CommandContext.class).getSource()).thenReturn(listener).getMock();
        
        mapper.reparse(provider).getSuggestions(context, builder);
        
        verify(provider).getSuggestions(any(CommandContext.class), eq(builder));
    }

} 
