/*
 * The MIT License
 *
 * Copyright 2018 Karus Labs.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package com.karuslabs.commons.item.builders;

import org.bukkit.*;
import org.bukkit.inventory.meta.*;
import org.bukkit.map.MapView;

import org.checkerframework.checker.nullness.qual.Nullable;


/**
 * A map builder.
 */
public class MapBuilder extends Builder<MapMeta, MapBuilder> {
    
    /**
     * Creates a {@code MapBuilder} for the given material.
     * 
     * @param material the material
     * @return a {@code MapBuilder}
     */
    public static MapBuilder of(Material material) {
        return new MapBuilder(material);
    }
    
    MapBuilder(Material material) {
        super(material);
    }
    
    MapBuilder(Builder<ItemMeta, ?> source) {
        super(source);
    }
    
    
    /**
     * Sets the colour.
     * 
     * @param colour the colour
     * @return {@code this}
     */
    public MapBuilder colour(@Nullable Color colour) {
        meta.setColor(colour);
        return this;
    }
    
    /**
     * Sets the location name.
     * 
     * @param name the location name
     * @return this
     */
    public MapBuilder location(@Nullable String name) {
        meta.setLocationName(name);
        return this;
    }
    
    /**
     * Sets the map view.
     * 
     * @param map the map view
     * @return {@code this}
     */
    public MapBuilder view(MapView map) {
        meta.setMapView(map);
        return this;
    }
    
    /**
     * Sets the map scaling.
     * 
     * @param scaling the scaling
     * @return {@code this}
     */
    public MapBuilder scaling(boolean scaling) {
        meta.setScaling(scaling);
        return this;
    }

    
    @Override
    MapBuilder self() {
        return this;
    }
    
}
