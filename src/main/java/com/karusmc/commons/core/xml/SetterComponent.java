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
package com.karusmc.commons.core.xml;

import org.jdom2.Element;


/**
 * Parses a node in an XMl Document and modifies the argument object accordingly.
 * 
 * @param <ParseObject> The object to modify
 */
public interface SetterComponent<ParseObject> {
    
    /**
     * Parses the specified element and modifies the argument object.
     * 
     * @param element The start of the node
     * @param argument The argument object to modify
     */
    public void parse(Element element, ParseObject argument);

}
