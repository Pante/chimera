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

import com.karuslabs.commons.items.meta.BookMetaComponent;
import com.karuslabs.commons.items.meta.LeatherArmorMetaComponent;
import com.karuslabs.commons.items.meta.ItemMetaComponent;
import com.karuslabs.commons.items.meta.PotionMetaComponent;
import com.karuslabs.commons.items.meta.EnchantmentStorageMetaComponent;
import com.karuslabs.commons.items.meta.BannerMetaComponent;
import com.karuslabs.commons.core.xml.ParserException;
import com.karuslabs.commons.core.xml.Component;

import java.util.*;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import org.jdom2.*;


/**
 * Parses the item nodes in a XML document and transforms it into a {@link ValueStack}.
 */
public class ValueStackComponent implements Component<ValueStack> {

    private Map<String, ItemMetaComponent> components;
    
    
    /**
     * Constructs this with the default Item-meta components.
     */
    public ValueStackComponent() {
        components = new HashMap<>(6);
        components.put("banner-meta", new BannerMetaComponent());
        components.put("book-meta", new BookMetaComponent());
        components.put("storage-meta", new EnchantmentStorageMetaComponent());
        components.put("item-meta", new ItemMetaComponent());
        components.put("leather-armor-meta", new LeatherArmorMetaComponent());
        components.put("potion-meta", new PotionMetaComponent());
    }
    
    /**
     * Constructs this with the specified Item-meta components.
     * 
     * @param components The Item-meta components
     */
    public ValueStackComponent(Map<String, ItemMetaComponent> components) {
        this.components = components;
    }
    
    
    /**
     * Parses the item nodes. Delegates the parsing of item-meta nodes to a registered item-meta component.
     * 
     * @param element The starting element of the node
     * @return The parsed ValueStack
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
     * @return The registered Item-meta components
     */
    public Map<String, ItemMetaComponent> getComponents() {
        return components;
    }
    
}
