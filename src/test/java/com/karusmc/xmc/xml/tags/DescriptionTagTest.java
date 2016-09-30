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

import java.util.*;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

import static org.junit.Assert.assertEquals;
/**
 *
 * @author PanteLegacy @ karusmc.com
 */
@RunWith(Parameterized.class)
public class DescriptionTagTest extends TagBase<DescriptionTag> {
    
    
    @Parameters
    public static Collection<Object[]> data() {
        return Arrays.asList(new Object[][]{
            {"xml/tags/description/description.xml", "test description", "test usage"},
            {"xml/tags/description/noDescription.xml", "No description for command", "No usage for command"}
        });
    }
    
    
    private String expectedDescription;
    private String expectedUsage;

    
    public DescriptionTagTest(String fileName, String expectedDescription, String expectedUsage) {
        super(new DescriptionTag(), fileName);
        
        this.expectedDescription = expectedDescription;
        this.expectedUsage = expectedUsage;
    }
    
    
    @Test
    public void getters_ReturnValues() {
        assertEquals(expectedDescription, tag.getDescription());
        assertEquals(expectedUsage, tag.getUsage());
    }
    
    
}
