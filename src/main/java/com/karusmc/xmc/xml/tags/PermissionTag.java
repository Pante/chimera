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

import javax.xml.namespace.QName;
import javax.xml.stream.events.Attribute;

/**
 *
 * @author PanteLegacy @ karusmc.com
 */
public class PermissionTag extends Tag {
    
    public PermissionTag() {
        super("permission");
    }
    
    
    public String getPermission() {
        Attribute permission = element.getAttributeByName(new QName("permission"));
        if (permission == null) {
            return "";
            
        } else {
            return permission.getValue();
        }
    }
    
    
    public String getMessage() {
        Attribute message = element.getAttributeByName(new QName("message"));
        if (message == null) {
            return "";
            
        } else {
            return message.getValue();
        }
    }
    
    
    public boolean isDefaultAllowed() {
        Attribute defaultAllowed = element.getAttributeByName(new QName("default"));
        if (defaultAllowed == null) {
            return false;
        }
        
        return Boolean.parseBoolean(defaultAllowed.getValue());
    }
    
    
    public boolean isConsoleAllowed() {
        Attribute consoleAllowed = element.getAttributeByName(new QName("console"));
        if (consoleAllowed == null) {
            return false;
        }
        
        return Boolean.parseBoolean(consoleAllowed.getValue());
    }
    
}
