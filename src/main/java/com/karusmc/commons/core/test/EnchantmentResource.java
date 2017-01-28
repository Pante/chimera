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
package com.karusmc.commons.core.test;

import java.lang.reflect.Field;
import java.util.Map;

import org.bukkit.enchantments.*;

import org.junit.rules.ExternalResource;


public class EnchantmentResource extends ExternalResource {
    
    public static final EnchantmentResource RESOURCE = new EnchantmentResource();
    
    
    private EnchantmentResource() {
        try {
            Field byIdField = Enchantment.class.getDeclaredField("byId");
            Field byNameField = Enchantment.class.getDeclaredField("byName");

            byIdField.setAccessible(true);
            byNameField.setAccessible(true);

            Map<Integer, Enchantment> byId = (Map<Integer, Enchantment>) byIdField.get(null);
            Map<String, Enchantment> byName = (Map<String, Enchantment>) byNameField.get(null);

            Field[] fields = Enchantment.class.getFields();
            for (Field field : fields) {
                Object object = field.get(null);
                
                if (object.getClass().equals(EnchantmentWrapper.class)) {
                    Enchantment enchantment = (Enchantment) object;

                    byId.put(enchantment.getId(), enchantment);
                    byName.put(field.getName(), enchantment);
                }
            }

        } catch (ReflectiveOperationException e) {
            throw new IllegalStateException(e);
        }
    }
    
}
