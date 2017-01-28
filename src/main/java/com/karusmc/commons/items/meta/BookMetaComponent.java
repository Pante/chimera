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

import net.md_5.bungee.api.ChatColor;
import org.bukkit.inventory.meta.BookMeta;

import org.jdom2.Element;


public class BookMetaComponent extends ItemMetaComponent<BookMeta> {
    
    @Override
    public void parse(Element root, BookMeta meta) {
        super.parse(root, meta);
        
        meta.setAuthor(ChatColor.translateAlternateColorCodes('&', root.getAttribute("author").getValue()));
        meta.setTitle(ChatColor.translateAlternateColorCodes('&', root.getAttribute("title").getValue()));
        
        Element pages = root.getChild("pages");
        if (pages != null) {
            pages.getChildren("page").forEach((element) -> {
                meta.addPage(ChatColor.translateAlternateColorCodes('&', element.getTextNormalize()));
            });
        }
    }
    
}
