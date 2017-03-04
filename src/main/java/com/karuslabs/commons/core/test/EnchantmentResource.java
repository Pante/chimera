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
package com.karuslabs.commons.core.test;

import java.lang.reflect.Field;

import org.bukkit.enchantments.*;

import org.junit.rules.ExternalResource;

import static org.mockito.Mockito.*;


/**
 * Represents a stub, singleton implementation of <code>Enchantment</code>.
 */
public class EnchantmentResource extends ExternalResource {
    
    public static final EnchantmentResource RESOURCE = new EnchantmentResource();
    
    
    private EnchantmentResource() {
        try {
            for (Field field : Enchantment.class.getFields()) {
                if (field.getType().equals(Enchantment.class)) {
                    Enchantment enchantment = spy((Enchantment) field.get(null));
                    doReturn(field.getName()).when(enchantment).getName();
                    
                    Enchantment.registerEnchantment(enchantment);
                }
            }
            
        } catch (ReflectiveOperationException e) {
            throw new IllegalStateException(e);
        }
    }
    
    
    /**
     * Convenience method for obtaining an <code>Enchantment</code> based on its ID.
     * 
     * @param id the enchantment ID
     * @return the <code>Enchantment</code> with the specified ID
     */
    public Enchantment getById(int id) {
        return Enchantment.getById(id);
    }
    
    /**
     * Convenience method for obtaining an <code>Enchantment</code> based on its name.
     * 
     * @param name the enchantment name
     * @return the <code>Enchantment</code> with the specified name
     */
    public Enchantment getByName(String name) {
        return Enchantment.getByName(name);
    }
    
}
