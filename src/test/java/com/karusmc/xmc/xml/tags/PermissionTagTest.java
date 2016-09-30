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

import java.util.Arrays;
import java.util.Collection;

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
public class PermissionTagTest extends TagBase<PermissionTag> {
    
    @Parameters
    public static Collection<Object[]> data() {
        return Arrays.asList(new Object[][] {
            {"xml/tags/permission/permission.xml", "test permission", "test message", true, true},
            {"xml/tags/permission/noPermission.xml", "", "You do not have permission to use this command", false, false}
        });
    }
    
    
    private String expectedPermission;
    private String expectedMessage;
    
    private boolean expectedDefault;
    private boolean expectedConsole;
    
    
    public PermissionTagTest(String fileName, String expectedPermission, String expectedMessage, boolean expectedDefault, boolean expectedConsole) {
        super(new PermissionTag(), fileName);
        
        this.expectedPermission = expectedPermission;
        this.expectedMessage = expectedMessage;
        
        this.expectedDefault = expectedDefault;
        this.expectedConsole = expectedConsole;
    }
    
    
    @Test
    public void getters_ReturnValues() {
        assertEquals(expectedPermission, tag.getPermission());
        assertEquals(expectedMessage, tag.getMessage());
        
        assertEquals(expectedDefault, tag.isDefaultAllowed());
        assertEquals(expectedConsole, tag.isConsoleAllowed());
    }
    
}
