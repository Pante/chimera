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

import java.lang.reflect.Field;

import org.bukkit.potion.*;

import static org.mockito.Mockito.*;


/**
 * Represents a stub, singleton implementation of <code>PotionEffectType</code>.
 */
public class StubPotionEffectType {
    
    public static final StubPotionEffectType INSTANCE = new StubPotionEffectType();
    
    
    private StubPotionEffectType() {
        try {
            for (Field field : PotionEffectType.class.getFields()) {
                if (field.getType().equals(PotionEffectType.class)) {
                    PotionEffectType effect = spy((PotionEffectType) field.get(null));
                    doReturn(field.getName()).when(effect).getName();

                    PotionEffectType.registerPotionEffectType(effect);
                }
            }

        } catch (ReflectiveOperationException e) {
            throw new IllegalStateException(e);
        }
    }
    
    
    /**
     * Convenience method for obtaining a <code>PotionEffectType</code> based on its ID.
     * 
     * @param id the <code>PotionEffectType</code> ID
     * @return the <code>PotionEffectType</code> with the specified ID
     */
    public PotionEffectType getById(int id) {
        return PotionEffectType.getById(id);
    }
    
    /**
     * Convenience method for obtaining a <code>PotionEffectType</code> based on its name.
     * 
     * @param name the <code>PotionEffectType</code> name
     * @return the <code>PotionEffectType</code> with the specified name
     */
    public PotionEffectType getByName(String name) {
        return PotionEffectType.getByName(name);
    }
    
}
