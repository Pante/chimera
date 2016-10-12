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
import com.karusmc.xmc.xml.*;

import javax.xml.stream.XMLStreamException;

import junitparams.*;

import org.junit.*;
import org.junit.runner.RunWith;

import static org.mockito.Mockito.*;

/**
 *
 * @author PanteLegacy @ karusmc.com
 */
//@RunWith(JUnitParamsRunner.class)
public class PermissionTagTest {
    
    @ClassRule
    public static XMLResource resource = new XMLResource("xml/tags/permission.xml");
    
    
    private Case xmlCase;
    private XMCommand command;
    
    private PermissionTag tag;

    
    public PermissionTagTest() {
        xmlCase = new Case();
        command = mock(XMCommand.class);
        
        tag = new PermissionTag();
    }
    
    
//    @Test
//    @Parameters({"1, true", "2, false"})
//    public void parse(String id, boolean expected) throws XMLStreamException {
//        xmlCase.find(resource.getReader(), id);
//        tag.parse(resource.getReader().nextEvent().asStartElement(), command);
//        
//        verify(command, times(1)).setPermission("permission " + id);
//        verify(command, times(1)).setPermissionMessage("message " + id);
//        verify(command, times(1)).setConsoleAllowed(expected);
//    }
    
    @Test
    public void parse() throws XMLStreamException {
        String id = "1";
        boolean expected = true;
        
        tag.parse(xmlCase.find(resource.getReader(), id).nextEvent().asStartElement(), command);
        
        verify(command, times(1)).setPermission("permission " + id);
        verify(command, times(1)).setPermissionMessage("message " + id);
        verify(command, times(1)).setConsoleAllowed(expected);
    }
    
}
