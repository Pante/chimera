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
package com.karuslabs.commons.items.meta;

import com.karuslabs.commons.xml.ParserException;

import org.bukkit.Color;
import org.bukkit.inventory.meta.PotionMeta;
import org.bukkit.potion.*;

import org.jdom2.*;


public class PotionComponent extends ItemComponent<PotionMeta> {
    
    @Override
    public void parse(Element root, PotionMeta meta) {
        super.parse(root, meta);
        
        meta.setColor(fromString(root.getAttribute("color").getValue()));
        
        Element effects = root.getChild("effects");
        if (effects != null) {
            try {
                for (Element effect : effects.getChildren("effect")) {

                    PotionEffectType type = PotionEffectType.getByName(effect.getAttribute("type").getValue());
                    int duration = effect.getAttribute("duration").getIntValue();
                    int level = effect.getAttribute("level").getIntValue();
                    boolean ambient = effect.getAttribute("ambient").getBooleanValue();
                    boolean particles = effect.getAttribute("particles").getBooleanValue();

                    meta.addCustomEffect(new PotionEffect(type, duration, level, ambient, particles), true);
                }
            } catch (DataConversionException e) {
                throw new ParserException("Failed to parse element: " + effects.getName(), e);
            }
        }
    }
    
    public Color fromString(String color) {
        switch (color) {
            case "AQUA":
                return Color.AQUA;
                
            case "BLACK":
                return Color.BLACK;
                
            case "BLUE":
                return Color.BLUE;
                
            case "FUCHSIA":
                return Color.FUCHSIA;
                
            case "GRAY":
                return Color.FUCHSIA;
                
            case "GREEN":
                return Color.GREEN;
                
            case "LIME":
                return Color.LIME;
                
            case "MAROON":
                return Color.MAROON;
            
            case "NAVY":
                return Color.NAVY;
                
            case "OLIVE":
                return Color.OLIVE;
                
            case "ORANGE":
                return Color.ORANGE;
                
            case "PURPLE":
                return Color.PURPLE;
                
            case "RED":
                return Color.RED;
                
            case "SILVER":
                return Color.SILVER;
                
            case "TEAL":
                return Color.TEAL;
                
            case "WHITE":
                return Color.WHITE;
                
            case "YELLOW":
                return Color.YELLOW;
                
            default:
                throw new IllegalArgumentException("No such color: " + color);
        }
    }
    
}
