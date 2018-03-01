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
package com.karuslabs.commons.command.parser;

import com.karuslabs.commons.command.*;
import com.karuslabs.commons.command.completion.*;
import com.karuslabs.commons.locale.*;
import com.karuslabs.commons.locale.providers.Provider;
import com.karuslabs.commons.locale.resources.*;

import java.util.*;

import org.junit.jupiter.api.Test;

import static com.karuslabs.commons.configuration.Yaml.COMMANDS;
import static com.karuslabs.commons.locale.MessageTranslation.NONE;
import static java.util.Arrays.asList;
import static org.junit.jupiter.api.Assertions.assertEquals;


class ParsersTest {

    @Test
    void parse() {
        Parser parser = Parsers.newParser(null, null, new References().translation("TEST-TRANSLATION", NONE), NullHandle.EXCEPTION, Provider.NONE);
        List<Command> commands = parser.parse(COMMANDS);
        assertEquals(2, commands.size());
        
        Command brush = commands.get(0);
        assertEquals("brush", brush.getName());
        assertEquals("selects the brush tool", brush.getDescription());
        assertEquals("brush.permission", brush.getPermission());
        assertEquals("No permission", brush.getPermissionMessage());
        assertEquals("/brush [option]", brush.getUsage());
        
        Command help  = brush.getSubcommands().get("help");
        assertEquals("help", help.getName());
        assertEquals(asList("a", "b", "c"), help.getAliases());
        assertEquals("Displays information about the command", help.getDescription());
        assertEquals("help.permission", help.getPermission());
        assertEquals("message", help.getPermissionMessage());
        assertEquals("usage", help.getUsage());
        
        Map<Integer, Completion> helps = help.getCompletions();
        assertEquals(2, helps.size());
        assertEquals(asList("a"), ((CachedCompletion) helps.get(0)).getCompletions());
        assertEquals(asList("b"), ((CachedCompletion) helps.get(2)).getCompletions());
        
        assertTranslation(help.getTranslation());
        
        Command fill = brush.getSubcommands().get("fill");
        assertEquals("fill", fill.getName());
        assertEquals(asList("f"), fill.getAliases());
        
        Map<Integer, Completion> fills = fill.getCompletions();
        assertEquals(2, fills.size());
        assertEquals(asList("void"), ((CachedCompletion) fills.get(0)).getCompletions());
        assertEquals(asList("a", "b", "c"), ((CachedCompletion) fills.get(2)).getCompletions());
        
        assertTranslation(brush.getTranslation());
        
        Command test = commands.get(1);
        assertEquals("test", test.getName());
        assertEquals(asList("c"), test.getAliases());
        assertEquals(NONE, test.getTranslation());
    }
    
    
    void assertTranslation(MessageTranslation translation) {
        assertEquals("Resource", translation.getBundleName());
        
        Resource[] resources = ((ExternalControl) translation.getControl()).getResources();
        assertEquals("path1/", ((EmbeddedResource) resources[0]).getPath());
        assertEquals("path2", ((ExternalResource) resources[1]).getPath());
    }
    
}
