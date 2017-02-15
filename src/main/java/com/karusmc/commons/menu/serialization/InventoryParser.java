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
package com.karusmc.commons.menu.serialization;

import com.karusmc.commons.core.xml.*;

import org.bukkit.Server;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.*;

import org.jdom2.*;


public class InventoryParser extends Parser<Inventory> {

    private Server server;
    private Component<ItemStack> component;
    
    
    public InventoryParser(Server server, Component<ItemStack> component, String schemaPath) {
        super(schemaPath);
        this.server = server;
        this.component = component;
    }
    
    public InventoryParser(Server server, Component<ItemStack> component) {
        super(null);
        this.server = server;
        schemaPath = getClass().getClassLoader().getResource("menu/menu.xsd").getPath();
        this.component = component;
    }
    
    
    @Override
    public Inventory parse(Element root) {
        try {
            String title = root.getAttribute("title").getValue();
            InventoryType type = InventoryType.valueOf(root.getAttribute("type").getValue().toUpperCase());
            int size = root.getAttribute("size").getIntValue();
            
            Inventory inventory = createInventory(title, type, size);
            
            for (Element element : root.getChildren("slot")) {
                setSlot(element, inventory);
            }
            
            return inventory;
            
        } catch (DataConversionException | IllegalArgumentException e) {
            throw new ParserException("Failed to parse XML Document", e);
        }
    }
    
    protected Inventory createInventory(String title, InventoryType type, int size) {
        if (type == InventoryType.CHEST) {
            return server.createInventory(null, size, title);

        } else {
            return server.createInventory(null, type, title);
        }
    }
    
    protected void setSlot(Element element, Inventory inventory) throws DataConversionException {
        int slot = element.getAttribute("number").getIntValue();
        if (slot < inventory.getSize()) {
            inventory.setItem(slot, component.parse(element.getChild("item")));

        } else {
            throw new ParserException("Invalid slot: " + slot);
        }
    }
    
}
