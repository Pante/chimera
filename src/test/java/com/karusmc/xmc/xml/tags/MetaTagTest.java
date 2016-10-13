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
package com.karusmc.xmc.xml.tags;

import com.karusmc.xmc.core.XMCommand;
import com.karusmc.xmc.xml.XMLResource;

import java.util.*;

import junitparams.*;

import org.junit.*;
import org.junit.runner.RunWith;

import static org.mockito.Mockito.*;

/**
 *
 * @author PanteLegacy @ karusmc.com
 */
@RunWith(JUnitParamsRunner.class)
public class MetaTagTest {
    
    @ClassRule
    public static XMLResource resource = new XMLResource("xml/tags/meta.xml");
    
    private MetaTag tag;
    private XMCommand command;
    
    
    public MetaTagTest() {
        tag = new MetaTag();
        command = mock(XMCommand.class);
    }
    
    
    @Test
    @Parameters(method = "parse_parameters")
    public void parse(String id, List<String> expectedAliases) {
        resource.findCase(id);
        tag.parse(resource.find("meta"), command);
        
        verify(command, times(1)).setAliases(expectedAliases);
        verify(command, times(1)).setDescription("description " + id);
        verify(command, times(1)).setUsage("usage " + id);
    }
    
    public Object[] parse_parameters() {
        return new Object[] {
            new Object[] {"1", Arrays.asList("1", "2", "3")},
            new Object[] {"2", Arrays.asList("1", "2", "3")},
            new Object[] {"3", Arrays.<String>asList()}
        };
    }
    
}
