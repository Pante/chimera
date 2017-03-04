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
package com.karuslabs.commons.core.xml;

import org.jdom2.Element;


/**
 * Represents a component for parsing a XML node and setting the values of an object.
 * @see Component
 * 
 * @param <ParseObject> the type of the object to set
 */
public interface SetterComponent<ParseObject> {
    
    /**
     * Parses the element and sets the object specified.
     * 
     * @param element the node
     * @param argument the object to set
     */
    public void parse(Element element, ParseObject argument);

}
