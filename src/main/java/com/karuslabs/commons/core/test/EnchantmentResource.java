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
package com.karuslabs.commons.core.test;

import java.lang.reflect.Field;

import org.bukkit.enchantments.*;

import org.junit.rules.ExternalResource;

import static org.mockito.Mockito.*;


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
    
    
    public Enchantment getById(int id) {
        return Enchantment.getById(id);
    }
    
    public Enchantment getByName(String name) {
        return Enchantment.getByName(name);
    }
    
}
