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
package com.karuslabs.commons.command.dispatcher;

import com.karuslabs.commons.command.types.*;
import com.karuslabs.commons.command.ClientSuggestionProvider;
import com.karuslabs.commons.command.tree.nodes.*;

import com.mojang.brigadier.*;
import com.mojang.brigadier.arguments.*;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.suggestion.*;

import java.util.function.Predicate;
import java.util.stream.Stream;

import net.minecraft.commands.CommandSourceStack;

import org.bukkit.command.CommandSender;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.params.provider.Arguments.of;
import static org.mockito.Mockito.*;


class SpigotMapperTest {
    
    static final SuggestionProvider<CommandSourceStack> PROVIDER = (a, b) -> null;
    static final Command<CommandSourceStack> COMMAND = (a) -> 0;
    
    CommandDispatcher<CommandSender> dispatcher = spy(new CommandDispatcher<>());
    SpigotMapper mapper = spy(new SpigotMapper(dispatcher));
    
    CommandSender sender = mock(CommandSender.class);
    CommandSourceStack stack = when(mock(CommandSourceStack.class).getBukkitSender()).thenReturn(sender).getMock();
    
    
    @Test
    void type() {
        var bool = BoolArgumentType.bool();
        assertEquals(bool, mapper.type(Argument.of("test", bool).build()));
        
        var argument = (StringArgumentType) mapper.type(Argument.of("test", new PlayerType()).build());
        assertEquals(StringArgumentType.word().getType(), argument.getType());
    }
    
    
    @Test
    void requirement() {
        assertTrue(mapper.requirement(Literal.of("a").requires(null).build()).test(null));
        
        Predicate<CommandSender> predicate = mock(Predicate.class);
        mapper.requirement(Literal.of("a").requires(predicate).build()).test(stack);
        
        verify(predicate).test(sender);
    }
    
    
    @Test
    void execution() {
        assertNull(mapper.execution(Literal.of("a").build()));
    }
    
    @Test
    void execution_reparse() {
        var literal = Literal.of("a").executes((sender, context) -> {}).build();
        doReturn(COMMAND).when(mapper).reparse(literal);
        
        assertSame(COMMAND, mapper.execution(literal));
    }
    
    @Test
    void suggestions_primitive_empty() {
        assertNull(mapper.suggestions(Argument.of("a", StringArgumentType.word()).build()));
    }
    
    
    @Test
    void suggestions_typeargument() {
        var argument = new PlayerType();
        doReturn(PROVIDER).when(mapper).reparse(argument);
        
        assertSame(PROVIDER, mapper.suggestions(Argument.of("a", argument).build()));
        verify(mapper).reparse(argument);
    }
    
    
    @Test
    void suggestions_clientside() {
        assertSame(SpigotMapper.CLIENT_SIDE.get(ClientSuggestionProvider.ENTITIES), mapper.suggestions(Argument.of("a", StringArgumentType.word()).suggests(ClientSuggestionProvider.ENTITIES).build()));
    }
    
    
    @Test
    void suggestions_reparse() {
        SuggestionProvider<CommandSender> suggestor = (a, b) -> null;
        doReturn(PROVIDER).when(mapper).reparse(suggestor);
        
        assertSame(PROVIDER, mapper.suggestions(Argument.of("name", StringArgumentType.word()).suggests(suggestor).build()));
    }
    
    
    @ParameterizedTest
    @MethodSource("command_parameters")
    void reparse_type(String command, String trimmed) throws CommandSyntaxException {
        Type<Object> provider = mock(Type.class);
        SuggestionsBuilder builder = mock(SuggestionsBuilder.class);
        
        CommandContext<CommandSourceStack> context = when(mock(CommandContext.class).getSource()).thenReturn(stack).getMock();
        when(context.getInput()).thenReturn(command);
        
        mapper.reparse(provider).getSuggestions(context, builder);
        
        verify(dispatcher).parse(trimmed, sender);
        verify(provider).listSuggestions(any(CommandContext.class), eq(builder));
    }
    
    
    @ParameterizedTest
    @MethodSource("command_parameters")
    void reparse_suggestion_provider(String command, String trimmed) throws CommandSyntaxException {
        SuggestionProvider provider = mock(SuggestionProvider.class);
        SuggestionsBuilder builder = mock(SuggestionsBuilder.class);
        
        CommandContext<CommandSourceStack> context = when(mock(CommandContext.class).getSource()).thenReturn(stack).getMock();
        when(context.getInput()).thenReturn(command);
        
        mapper.reparse(provider).getSuggestions(context, builder);
        
        verify(dispatcher).parse(trimmed, sender);
        verify(provider).getSuggestions(any(CommandContext.class), eq(builder));
    }
    
    static Stream<Arguments> command_parameters() {
        return Stream.of(
            of("/command", "command"),
            of("/", ""),
            of("", "")
        );
    }

} 
