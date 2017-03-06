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

import org.junit.rules.ExternalResource;

import static org.mockito.Mockito.*;


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
    
    
    public PotionEffectType getById(int id) {
        return PotionEffectType.getById(id);
    }
    
    public PotionEffectType getByName(String name) {
        return PotionEffectType.getByName(name);
    }
    
}
