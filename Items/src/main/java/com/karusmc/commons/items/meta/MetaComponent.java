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

import com.karusmc.commons.core.xml.SetterComponent;

import org.bukkit.ChatColor;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.meta.ItemMeta;

import org.jdom2.*;


public class MetaComponent<GenericMeta extends ItemMeta> implements SetterComponent<GenericMeta> {

    @Override
    public void parse(Element element, GenericMeta meta) {
        try {
            String displayName = ChatColor.translateAlternateColorCodes('&', element.getAttribute("display-name").getValue());
            
            parseEnchantments(element.getChild("enchantments"), meta);
            parseDescription(element.getChild("description"), meta);
            
        } catch (DataConversionException ex) {
            //TODO
        }
    }
    
    
    protected void parseEnchantments(Element root, GenericMeta meta) throws DataConversionException {
        for (Element element : root.getChildren("enchantment")) {
            
            Enchantment enchantment = Enchantment.getByName(element.getAttribute("type").getValue());
            int level = element.getAttribute("level").getIntValue();
            
            meta.addEnchant(enchantment, level, true);
        }
    }
    
    protected void parseDescription(Element root, GenericMeta meta) {
        for (Element element : root.getChildren("br")) {
            
            String line = ChatColor.translateAlternateColorCodes('&', element.getText());
            meta.getLore().add(line);
        }
    }
    
}
