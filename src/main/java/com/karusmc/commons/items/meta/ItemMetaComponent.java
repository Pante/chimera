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

import com.karusmc.commons.core.xml.*;

import java.util.List;
import java.util.stream.Collectors;

import org.bukkit.ChatColor;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.meta.ItemMeta;

import org.jdom2.*;


public class ItemMetaComponent<GenericMeta extends ItemMeta> implements SetterComponent<GenericMeta> {

    @Override
    public void parse(Element root, GenericMeta meta) {
        try {
            meta.setDisplayName(ChatColor.translateAlternateColorCodes('&', root.getAttribute("display-name").getValue()));
            
            parseEnchantments(root.getChild("enchantments"), meta);
            parseDescription(root.getChild("description"), meta);
            parseItemFlags(root.getChild("flags"), meta);
            
        } catch (DataConversionException | IllegalArgumentException e) {
            throw new ParserException("Failed to parse element: " + root.getName(), e);
        }
    }
    
    
    protected void parseEnchantments(Element root, GenericMeta meta) throws DataConversionException {
        for (Element element : root.getChildren("enchantment")) {
            
            String enchantmentName = element.getAttribute("type").getValue();
            Enchantment enchantment = Enchantment.getByName(enchantmentName);
            int level = element.getAttribute("level").getIntValue();
            
            if (enchantment != null) {
                meta.addEnchant(enchantment, level, true);
            } else {
                throw new IllegalArgumentException("No such enchantment: " + enchantmentName);
            }
        }
    }
    
    protected void parseDescription(Element root, GenericMeta meta) {
        List<String> lore = root.getChildren("line").stream().map(element -> element.getText()).collect(Collectors.toList());
        meta.setLore(lore);
    }
    
    protected void parseItemFlags(Element root, GenericMeta meta) {
        root.getChildren("flag").forEach((element) -> {
            meta.addItemFlags(ItemFlag.valueOf(element.getText()));
        });
    }
    
}
