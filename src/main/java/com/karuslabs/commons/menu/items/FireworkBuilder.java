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

import org.bukkit.FireworkEffect;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.FireworkMeta;


public class FireworkBuilder extends Builder<FireworkBuilder, FireworkMeta> {

    protected FireworkBuilder(ItemStack item, FireworkMeta meta) {
        super(item, meta);
    }
    
    
    public FireworkBuilder effects(FireworkEffect... effects) {
        meta.addEffects(effects);
        return this;
    }
    
    public FireworkBuilder power(int power) {
        meta.setPower(power);
        return this;
    }

    
    @Override
    protected FireworkBuilder getThis() {
        return this;
    }
    
}
