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
package com.karuslabs.commons.command.parser;

import com.karuslabs.commons.command.Command;
import com.karuslabs.commons.locale.BundledTranslation;

import java.util.*;
import java.io.File;

import org.junit.Test;

import static com.karuslabs.commons.collection.Sets.asSet;
import static com.karuslabs.commons.configuration.Yaml.COMMANDS;
import static java.util.Arrays.asList;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.*;


public class ParserIT {
    
    private Parser parser;
    
    
    public ParserIT() {
        CompletionElement completion = new CompletionElement();
        TranslationElement translations = new TranslationElement(new File(""));
        
        parser = new Parser(new CommandElement(null, completion, translations), completion, translations);
    }
    
    
    @Test
    public void parse() {
        List<Command> commands = parser.parse(COMMANDS);
        Command command = commands.get(0);
        
        Map<String, Command> subcommands = command.getSubcommands();
        Command help = subcommands.get("help");
        Command subcommand = subcommands.get("b");
        
        assertEquals(1, commands.size());
        assertEquals("command", command.getName());
        assertEquals("description", command.getDescription());
        assertEquals("command.permission", command.getPermission());
        assertEquals("message", command.getPermissionMessage());
        assertEquals("usage", command.getUsage());
        assertEquals(2, command.getCompletions().size());
        assertEquals("Resource", ((BundledTranslation) command.getTranslation()).getBundleName());
        
        assertThat(subcommands.keySet(), equalTo(asSet("help", "a", "b", "c", "b", "b-alias")));
        
        assertThat(help.getAliases(), equalTo(asList("a", "b", "c")));
        assertEquals("help.permission", help.getPermission());
        
        assertThat(subcommand.getAliases(), equalTo(asList("b-alias")));
        assertEquals("Resource", ((BundledTranslation) subcommand.getTranslation()).getBundleName());
    }
    
}
