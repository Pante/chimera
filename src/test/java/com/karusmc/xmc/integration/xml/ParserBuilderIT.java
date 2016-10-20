/*
 * Copyright (C) 2016 PanteLegacy @ karusmc.com
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.karusmc.xmc.integration.xml;

import com.karusmc.xmc.commands.DispatchCommand;
import com.karusmc.xmc.xml.*;

import java.io.File;
import java.util.Arrays;

import org.junit.*;

import static org.junit.Assert.*;
/**
 *
 * @author PanteLegacy @ karusmc.com
 */
public class ParserBuilderIT {
    
    private Parser parser;
    
    private DispatchCommand dispatcher;
    private StubCommand subcommand;
    
    
    @Before
    public void setup() {
        dispatcher = new DispatchCommand(null, "command");
        dispatcher.getCommands().put("subcommand", subcommand = new StubCommand(null, "subcommand"));
    }
    
    
    @Test
    public void parser_ParsesCommands() {
        parser = ParserBuilder.buildCommandsParser("dtd/commands.dtd");
        parser.register(dispatcher);
        parser.parse(new File(getClass().getClassLoader().getResource("integration/xml/commands.xml").getPath()));
        
        assertTrue(dispatcher.getAliases().containsAll(Arrays.asList("cmd", "comm")));
        assertEquals("command description", dispatcher.getDescription());
        assertEquals("example usage", dispatcher.getUsage());
        assertEquals("example.permission", dispatcher.getPermission());
        assertEquals("You do not have permission to use this command", dispatcher.getPermissionMessage());
        assertTrue(dispatcher.isConsoleAllowed());
        
        assertTrue(subcommand.getAliases().containsAll(Arrays.asList("subcmd", "subcomm")));
        assertEquals("command description", subcommand.getDescription());
        assertEquals("example usage", subcommand.getUsage());
        assertEquals("example.permission", subcommand.getPermission());
        assertEquals("You do not have permission to use this command", subcommand.getPermissionMessage());
        
        assertEquals(10, subcommand.getCooldown());
        assertTrue(subcommand.hasBlacklist());
        assertTrue(subcommand.getWorlds().containsAll(Arrays.asList("world1", "world2")));
    }
    
    
    @Test
    public void parser_ParsersConfiguration() {
        parser = ParserBuilder.buildConfigurationParser("dtd/configuration.dtd");
        parser.register(dispatcher);
        
        assertEquals(100, subcommand.getCooldown());
        assertTrue(subcommand.hasBlacklist());
        assertTrue(subcommand.getWorlds().containsAll(Arrays.asList("world1", "world2")));
    }
    
    
}
