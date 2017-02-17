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

import org.bukkit.inventory.ItemStack;


public class ValueStack {
    
    private String name;
    private ItemStack item;
    private float buy;
    private float sell;
    
    
    public ValueStack(String name, ItemStack item, float buy, float sell) {
        this.name = name;
        this.item = item;
        this.buy = buy;
        this.sell = sell;
    }

    
    public String getName() {
        return name;
    }
    
    public ItemStack getItem() {
        return item;
    }

    public float getBuy() {
        return buy;
    }

    public float getSell() {
        return sell;
    }
    
}
