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
package com.karuslabs.commons.test;

import com.karuslabs.commons.annotations.Implemented;

import java.lang.reflect.Field;

import org.bukkit.enchantments.*;
import org.bukkit.inventory.ItemStack;


public class StubEnchantment extends Enchantment {
    
    public static final StubEnchantment INSTANCE = new StubEnchantment(0, "HE WHO SHALL NOT BE NAMED");
    
    
    static {
        try {
            for (Field field : Enchantment.class.getFields()) {
                Enchantment wrapper = (Enchantment) field.get(null);
                Enchantment.registerEnchantment(new StubEnchantment(wrapper.getId(), field.getName()));
            }
            
        } catch (ReflectiveOperationException e) {
            throw new IllegalStateException(e);
        }
    }

    
    private String name;
    
    
    public StubEnchantment(int id, String name) {
        super(id);
        this.name = name;
    }

    @Override
    @Implemented
    public String getName() {
        return name;
    }

    @Override
    public int getMaxLevel() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public int getStartLevel() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public EnchantmentTarget getItemTarget() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public boolean isTreasure() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public boolean isCursed() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public boolean conflictsWith(Enchantment other) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public boolean canEnchantItem(ItemStack item) {
        throw new UnsupportedOperationException("Not supported yet.");
    }
    
}
