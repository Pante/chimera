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


/**
 * Provides classes with a singleton test resource which stub implementation of Bukkit and Server.
 * Meant for testing purposes and should never be used in production code.
 */
public class BukkitResource extends ExternalResource {
    
    public static final BukkitResource RESOURCE = new BukkitResource();
    
    
    private Server server;
    private Logger logger;
    private ItemFactory factory;
    
    
    private BukkitResource() {
        server = mock(Server.class);
        logger = mock(Logger.class);
        factory = CraftItemFactory.instance();
        
        when(server.getLogger()).thenReturn(logger);
        when(server.getItemFactory()).thenReturn(factory);
        
        Bukkit.setServer(server);
    }

    
    /**
     * @return The stub server implementation
     */
    public Server getServer() {
        return server;
    }

    /**
     * @return The Logger
     */
    public Logger getLogger() {
        return logger;
    }
    
    /**
     * @return The ItemFactory
     */
    public ItemFactory getFactory() {
        return factory;
    }
    
}
