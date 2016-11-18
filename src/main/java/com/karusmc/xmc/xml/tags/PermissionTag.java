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

import javax.xml.namespace.QName;
import javax.xml.stream.events.StartElement;

/**
 *
 * @author PanteLegacy @ karusmc.com
 */
public class PermissionTag implements Tag<XMCommand> {
    
    private QName permission;
    private QName message;
    private QName console;
    
    
    public PermissionTag() {
        permission = new QName("permission");
        message = new QName("message");
        console = new QName("allow-console");
    }
    
    
    @Override
    public void parse(StartElement element, XMCommand command) {
        command.setPermission(element.getAttributeByName(permission).getValue());
        command.setPermissionMessage(element.getAttributeByName(message).getValue());
        command.setConsoleAllowed(Boolean.parseBoolean(element.getAttributeByName(console).getValue()));
    }
    
}
