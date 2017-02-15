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
package com.karusmc.commons.menu.xml;

import com.karusmc.commons.core.xml.*;
import com.karusmc.commons.items.meta.*;

import java.util.*;

import org.bukkit.Material;
import org.bukkit.inventory.*;
import org.bukkit.inventory.meta.ItemMeta;

import org.jdom2.*;


public class ItemStackComponent implements Component<ItemStack> {
    
    private Map<String, ItemMetaComponent> components;
    
    
    public ItemStackComponent() {
        components = new HashMap<>(6);
        components.put("banner-meta", new BannerMetaComponent());
        components.put("item-meta", new ItemMetaComponent());
        components.put("leather-armor-meta", new LeatherArmorMetaComponent());
        components.put("potion-meta", new PotionMetaComponent());
    }
    
    
    @Override
    public ItemStack parse(Element element) {
        try {
            Material material = Material.valueOf(element.getAttribute("type").getValue());
            int amount = element.getAttribute("amount").getIntValue();
            short data = (short) element.getAttribute("data").getIntValue();
            
            ItemStack item = new ItemStack(material, amount, data);
  
            Element meta = element.getChildren().get(0);
            ItemMeta itemMeta = item.getItemMeta();
            if (components.containsKey(meta.getName())) {
                components.get(meta.getName()).parse(meta, itemMeta);
                
            } else {
                throw new ParserException("No such meta type: " + meta.getName() + " is registered");
            }
            
            item.setItemMeta(itemMeta);
            
            return item;
            
        } catch (DataConversionException e) {
            throw new ParserException("Failed to parse XML Document", e);
        }
    }
    
    
    public Map<String, ItemMetaComponent> getComponents() {
        return components;
    }
    
}
