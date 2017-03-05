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
package com.karuslabs.commons.items;

import com.karuslabs.commons.xml.*;
import com.karuslabs.commons.items.meta.*;

import java.util.*;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import org.jdom2.*;


/**
 * Represents a component for parsing <code>&lt;item&gt;</code> nodes.
 */
public class ValueStackComponent implements Component<ValueStack> {

    private Map<String, ItemComponent> components;
    
    
    /**
     * Creates a new component.
     */
    public ValueStackComponent() {
        components = new HashMap<>(6);
        components.put("banner-meta", new BannerComponent());
        components.put("book-meta", new BookComponent());
        components.put("storage-meta", new StorageComponent());
        components.put("item-meta", new ItemComponent());
        components.put("leather-armor-meta", new LeatherArmorComponent());
        components.put("potion-meta", new PotionComponent());
    }
    
    /**
     * Creates a new component with the components for parsing <code>ItemMeta</code>s specified.
     * 
     * @param components the components for parsing variants of <code>&lt;ItemMeta&gt;</code>s
     */
    public ValueStackComponent(Map<String, ItemComponent> components) {
        this.components = components;
    }
    
    
    /**
     * Parses a <code>&lt;item&gt;</code> node.
     * 
     * @param element the node
     * @return the parsed <code>ValueStack</code>
     */
    @Override
    public ValueStack parse(Element element) {
        try {
            String name = element.getAttribute("name").getValue();
            
            Material material = Material.valueOf(element.getAttribute("type").getValue());
            int amount = element.getAttribute("amount").getIntValue();
            short data = (short) element.getAttribute("data").getIntValue();
            
            ItemStack item = new ItemStack(material, amount, data);
            
            
            Element value = element.getChild("value");
            float buy = value.getAttribute("buy").getFloatValue();
            float sell = value.getAttribute("sell").getFloatValue();

            
            Element meta = element.getChildren().get(1);
            ItemMeta itemMeta = item.getItemMeta();
            if (components.containsKey(meta.getName())) {
                components.get(meta.getName()).parse(meta, itemMeta);
                
            } else {
                throw new ParserException("No such meta type: " + meta.getName() + " is registered");
            }
            
            item.setItemMeta(itemMeta);
            
            return new ValueStack(name, item, buy, sell);
            
        } catch (DataConversionException e) {
            throw new ParserException("Failed to parse XML Document", e);
        }
    }
    
    
    /**
     * @return the components for parsing variants of <code>&lt;Itemmeta&gt;</code>
     */
    public Map<String, ItemComponent> getComponents() {
        return components;
    }
    
}
