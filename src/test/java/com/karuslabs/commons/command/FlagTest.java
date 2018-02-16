/*
 * The MIT License
 *
 * Copyright 2018 Karus Labs.
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


import org.junit.jupiter.api.Test;

import static com.karuslabs.commons.command.Flag.*;
import static java.util.Arrays.asList;
import static org.mockito.Mockito.*;


class FlagTest {
    
    static final Flag NONE = spy(new Flag() {
        @Override
        public void execute(CommandSource source, Command parent, Context context) {
            source.get();
        }
    });
    static final Command command = new Command("name", null, "description", "usage", asList("A", "B"), null);
    
    CommandSource source = mock(CommandSource.class);
    
    
    @Test
    void no_parent() {
        NONE.execute(source, new Context(null, null, null), null);
        verify(source).sendColouredTranslation("Invalid command");
    }
    
    @Test
    void no_permission() {
        when(source.hasPermission((String) any())).thenReturn(false);
        
        NONE.execute(source, new Context(null, command, null), null);
        
        verify(source).sendMessage(command.getPermissionMessage());
    }
    
    
    @Test
    void execute() {
        when(source.hasPermission((String) any())).thenReturn(true);
        
        NONE.execute(source, new Context(null, command, null), null);
        
        verify(source).get();
    }
    
    
    @Test
    void aliases() {
        ALIASES.execute(source, command, null);
        verify(source).sendColouredTranslation("Aliases", command.getAliases().toString());
    }
    
    
    @Test
    void description() {
        DESCRIPTION.execute(source, command, null);
        verify(source).sendColouredTranslation("Description", command.getDescription(), command.getUsage());
    }
    
    
    @Test
    void help() {
        Command subcommand = when(mock(Command.class).getName()).thenReturn("subcommand").getMock();
        when(subcommand.getPermission()).thenReturn("");
        command.getSubcommands().put("subcommand", subcommand);
        
        when(source.hasPermission("")).thenReturn(true);
        
        HELP.execute(source, command, null);
        
        verify(source).sendColouredTranslation("Help header", command.getName());
        verify(source).sendColouredTranslation("Description", command.getDescription(), command.getUsage());
        verify(source).sendColouredTranslation("Subcommands", asList("subcommand").toString());
    }
    
}
