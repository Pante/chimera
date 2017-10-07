/*
 * The MIT License
 *
 * Copyright 2017 Karus Labs.
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
package com.karuslabs.commons.animation.particles;

import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;


public class ItemParticles extends StandardParticles {
    
    private ItemStack item;

    
    public ItemParticles(Particle type, int amount, double offsetX, double offsetY, double offsetZ, double speed, ItemStack item) {
        super(type, amount, offsetX, offsetY, offsetZ, speed);
        this.item = item;
    }
    
    
    @Override
    public void render(Player player, Location location) {
        player.spawnParticle(particle, location, amount, offsetX, offsetY, offsetZ, speed, item);
    }

    @Override
    public void render(Location location) {
        location.getWorld().spawnParticle(particle, location, amount, offsetX, offsetY, offsetZ, speed, item);
    }
    
    
    public ItemStack getItem() {
        return item;
    }
    
    
    public static class ItemBuilder extends AbstractBuilder<ItemBuilder, ItemParticles> {

        public ItemBuilder(ItemParticles particles) {
            super(particles);
        }
        
        public ItemBuilder item(ItemStack item) {
            particles.item = item;
            return this;
        }

        @Override
        protected ItemBuilder getThis() {
            return this;
        }
        
    }
    
}
