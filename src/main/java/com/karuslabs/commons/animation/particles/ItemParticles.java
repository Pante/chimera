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
package com.karuslabs.commons.animation.particles;

import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;


public class ItemParticles extends AnimationParticles {
    
    private ItemStack item;

    
    public ItemParticles(int amount, ItemStack item) {
        this(amount, 0, 0, 0, 1, item);
    }
    
    public ItemParticles(int amount, double offsetX, double offsetY, double offsetZ, double speed, ItemStack item) {
        super(Particle.ITEM_CRACK, amount);
        this.item = item;
    }

    
    @Override
    public void render(Player player, Location location) {
        player.spawnParticle(type, location, amount, offsetX, offsetY, offsetZ, speed, item);
    }

    @Override
    public void render(Location location) {
        location.getWorld().spawnParticle(type, location, amount, offsetX, offsetY, offsetZ, speed, item);
    }
    
    
    public ItemStack getItemstack() {
        return item;
    }
    
    
    public static ItemParticlesBuilder newItemParticles() {
        return new ItemParticlesBuilder(new ItemParticles(1, new ItemStack(Material.AIR)));
    }
    
    
    public static class ItemParticlesBuilder extends ParticlesBuilder<ItemParticlesBuilder, ItemParticles> {
        
        protected ItemParticlesBuilder(ItemParticles particles) {
            super(particles);
        }
        
        
        @Override
        public ItemParticlesBuilder type(Particle type) {
            throw new UnsupportedOperationException("ItemParticles may only be composed of ITEM_CRACK particles");
        }
        
        public ItemParticlesBuilder item(ItemStack item) {
            particles.item = item;
            return this;
        }
        

        @Override
        protected ItemParticlesBuilder getThis() {
            return this;
        }
        
    }
    
}
