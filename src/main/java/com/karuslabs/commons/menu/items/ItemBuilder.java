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
package com.karuslabs.commons.menu.items;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.*;


public class ItemBuilder extends Builder<ItemBuilder, ItemMeta> {
    
    public ItemBuilder() {
        super(new ItemStack(Material.AIR));
    }

    
    public ItemBuilder material(Material material) {
        item.setType(material);
        //TODO: Fix meta
        return this;
    }

        
    public BannerBuilder asBanner() {
        return new BannerBuilder(item, (BannerMeta) meta);
    }
    
    public PotionBuilder asPotion() {
        return new PotionBuilder(item, (PotionMeta) meta);
    }

    
    @Override
    protected ItemBuilder getThis() {
        return this;
    }

}
