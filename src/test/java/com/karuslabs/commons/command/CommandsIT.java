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
package com.karuslabs.commons.command;

import com.karuslabs.commons.annotation.JDK9;
import com.karuslabs.commons.command.completion.Completion;
import com.karuslabs.commons.locale.providers.Provider;

import java.io.File;
import java.util.*;

import org.bukkit.plugin.Plugin;

import org.junit.jupiter.api.Test;

import static java.util.Arrays.asList;
import static java.util.Collections.singletonList;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;


public class CommandsIT {
    
    private Commands commands;
    private StubCommandMap map;
    private Plugin plugin;
    
    
    public CommandsIT() {
        map = new StubCommandMap();
        plugin = when(mock(Plugin.class).getDataFolder()).thenReturn(new File(getClass().getClassLoader().getResource("command/commands.yml").getPath()).getParentFile()).getMock();
        commands = new Commands(plugin, map, Provider.DETECTED);
    }
    
    
    @Test
    public void load() {
        commands.load("command/commands.yml");
        
        Command brush = map.commands.get(0);
        assertEquals(singletonList("b"), brush.getAliases());
        assertEquals("selects the brush tool", brush.getDescription());
        assertEquals("brush.permission", brush.getPermission());
        assertEquals("You do not have permission to use this command.", brush.getPermissionMessage());
        assertEquals("/brush [option]", brush.getUsage());
        assertEquals("Resource", brush.getTranslation().getBundleName());
        
        Command help = brush.getSubcommands().get("help");
        assertEquals(asList("a", "b", "c"), help.getAliases());
        assertEquals("Displays information about the command", help.getDescription());
        assertEquals("help.permission", help.getPermission());
        assertEquals("message", help.getPermissionMessage());
        assertEquals("usage", help.getUsage());
        assertEquals(2, help.getCompletions().size());
        
        Command fill = brush.getSubcommands().get("fill");
        assertEquals(singletonList("f"), fill.getAliases());
        
        Command test = map.commands.get(1);
        assertEquals(singletonList("c"), test.getAliases());
    }
    
}
