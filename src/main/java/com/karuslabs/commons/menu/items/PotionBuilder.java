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

import org.bukkit.Color;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.PotionMeta;
import org.bukkit.potion.*;


public class PotionBuilder extends Builder<PotionBuilder, PotionMeta> {
    
    protected PotionBuilder(ItemStack item, PotionMeta meta) {
        super(item, meta);
    }
    
    
    public PotionBuilder effect(PotionEffect effect, boolean override) {
        meta.addCustomEffect(effect, override);
        return this;
    }
    
    public PotionBuilder color(Color color) {
        meta.setColor(color);
        return this;
    }
    
    public PotionBuilder data(PotionData data) {
        meta.setBasePotionData(data);
        return this;
    }
    

    @Override
    protected PotionBuilder getThis() {
        return this;
    }
    
}
