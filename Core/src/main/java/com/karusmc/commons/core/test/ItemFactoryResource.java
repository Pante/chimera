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
package com.karusmc.commons.core.test;

import java.util.logging.Logger;

import org.bukkit.*;
import org.bukkit.inventory.ItemFactory;

import org.bukkit.craftbukkit.v1_11_R1.inventory.*;

import org.junit.rules.ExternalResource;

import static org.mockito.Mockito.*;


public class ItemFactoryResource extends ExternalResource {
    
    public static final Server SERVER;
    public static final ItemFactory FACTORY;
    
    
    static {
        SERVER = mock(Server.class);
        FACTORY = CraftItemFactory.instance();

        when(SERVER.getItemFactory()).thenReturn(FACTORY);
        when(SERVER.getLogger()).thenReturn(LOGGER);

        Bukkit.setServer(SERVER);
    }

}
