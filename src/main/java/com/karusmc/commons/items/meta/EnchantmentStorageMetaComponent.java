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
package com.karusmc.commons.items.meta;

import com.karusmc.commons.core.xml.ParserException;

import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.meta.EnchantmentStorageMeta;

import org.jdom2.*;


/**
 * Parses the enchantment-storage-meta nodes in a XML document.
 */
public class EnchantmentStorageMetaComponent extends ItemMetaComponent<EnchantmentStorageMeta> {
    
    
    /**
     * Parses a enchantment-storage-meta node.
     * 
     * @param root The starting element of the node
     * @param meta The EnchantmentStorageMeta to modify
     */
    @Override
    public void parse(Element root, EnchantmentStorageMeta meta) {
        super.parse(root, meta);

        Element stored = root.getChild("stored");
        if (stored != null) {
            try {
                for (Element element : stored.getChildren("enchantment")) {
                    String enchantmentName = element.getAttribute("type").getValue();
                    Enchantment enchantment = Enchantment.getByName(enchantmentName);
                    int level = element.getAttribute("level").getIntValue();

                    if (enchantment != null) {
                        meta.addStoredEnchant(enchantment, level, true);
                        
                    } else {
                        throw new IllegalArgumentException("No such enchantment: " + enchantmentName);
                    }
                }
            } catch (DataConversionException e) {
                throw new ParserException("Failed to parse element: " + root.getName());
            }
        }
    }
    
}
