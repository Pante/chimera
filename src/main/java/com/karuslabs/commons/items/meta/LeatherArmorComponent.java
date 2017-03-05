/*
 * Copyright (C) 2017 Karus Labs
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
package com.karuslabs.commons.items.meta;

import com.karuslabs.commons.xml.ParserException;

import org.bukkit.Color;
import org.bukkit.inventory.meta.LeatherArmorMeta;

import org.jdom2.*;


/**
 * Represents a component for parsing <code>&lt;leather-armor-meta&gt;</code> nodes
 */
public class LeatherArmorComponent extends ItemComponent<LeatherArmorMeta> {
    
    /**
     * Parses a <code>&lt;leather-armor-meta&gt;</code> node.
     * 
     * @param element the node
     * @param meta the meta to set
     */
    @Override
    public void parse(Element element, LeatherArmorMeta meta) {
        super.parse(element, meta);
        
        try {
            meta.setColor(Color.fromRGB(element.getAttribute("r").getIntValue(), element.getAttribute("g").getIntValue(), element.getAttribute("b").getIntValue()));
            
        } catch (DataConversionException e) {
            throw new ParserException("Failed to parse element: " + element.getName(), e);
        }
    }
    
}
