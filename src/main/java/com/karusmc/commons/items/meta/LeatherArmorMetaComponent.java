/*
 * Copyright (C) 2017 PanteLegacy @ karusmc.com
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
package com.karusmc.commons.items.meta;

import com.karusmc.commons.core.xml.ParserException;

import org.bukkit.Color;
import org.bukkit.inventory.meta.LeatherArmorMeta;

import org.jdom2.*;


/**
 * Parses the Leather-Armor-Meta nodes in a XML document.
 */
public class LeatherArmorMetaComponent extends ItemMetaComponent<LeatherArmorMeta> {
    
    /**
     * Parses a leather-armor-meta node.
     * 
     * @param element The starting element of the node
     * @param meta The LeatherArmorMeta to modify
     */
    @Override
    public void parse(Element element, LeatherArmorMeta meta) {
        super.parse(element, meta);
        
        try {
            int r = element.getAttribute("r").getIntValue();
            int g = element.getAttribute("g").getIntValue();
            int b = element.getAttribute("b").getIntValue();

            meta.setColor(Color.fromRGB(r, g, b));
        } catch (DataConversionException e) {
            throw new ParserException("Failed to parse element: " + element.getName(), e);
        }
    }
    
}
