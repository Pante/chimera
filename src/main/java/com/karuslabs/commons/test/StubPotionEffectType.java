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

import org.bukkit.Color;
import org.bukkit.potion.PotionEffectType;


public class StubPotionEffectType extends PotionEffectType {
    
    public static final StubPotionEffectType INSTANCE = new StubPotionEffectType(0, "HE WHO SHALL NOT BE NAMED");
    
    
    static {
        try {
            for (Field field : PotionEffectType.class.getFields()) {
                PotionEffectType effect = (PotionEffectType) field.get(null);
                PotionEffectType.registerPotionEffectType(new StubPotionEffectType(effect.getId(), field.getName()));
            }

        } catch (ReflectiveOperationException e) {
            throw new IllegalStateException(e);
}
    }
    
    
    private String name;
    
    
    public StubPotionEffectType(int id, String name) {
        super(id);
        this.name = name;
    }
    

    @Override
    public double getDurationModifier() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    @Implemented
    public String getName() {
        return name;
    }

    @Override
    public boolean isInstant() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public Color getColor() {
        throw new UnsupportedOperationException("Not supported yet.");
    }
    
}
