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
package com.karuslabs.commons.effect.particles;

import com.karuslabs.commons.annotation.Immutable;

import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;


/**
 * A concrete subclass of {@code articles} which additionally consists of a {@code ItemStack}, offset and speed.
 * <p>
 * This class may contain {@code Particle} type(s) of {@link Particle#ITEM_CRACK} only.
 */
@Immutable
public class ItemParticles extends AbstractParticles {
    
    private ItemStack item;

    
    /**
     * Constructs an {@code ItemParticles} with the specified {@code Particle} type, {@code ItemStack}, amount, offset and speed.
     * 
     * @param type the Particle type
     * @param item the ItemStack
     * @param amount the amount
     * @param offsetX the maximum random offset on the X axis
     * @param offsetY the maximum random offset on the Y axis
     * @param offsetZ the maximum random offset on the Z axis
     * @param speed the speed
     */
    public ItemParticles(Particle type, ItemStack item, int amount, double offsetX, double offsetY, double offsetZ, double speed) {
        super(type, amount, offsetX, offsetY, offsetZ, speed);
        this.item = item;
    }
    
    
    /**
     * {@inheritDoc}
     */
    @Override
    protected void render(World world, double x, double y, double z) {
        world.spawnParticle(particle, x, y, z, amount, offsetX, offsetY, offsetZ, speed, item);
    }

    
    /**
     * {@inheritDoc}
     */
    @Override
    protected void render(Player player, double x, double y, double z) {
        player.spawnParticle(particle, x, y, z, amount, offsetX, offsetY, offsetZ, speed, item);
    }
    
    
    /**
     * Returns the {@code ItemStack}.
     * 
     * @return the ItemStack
     */
    public ItemStack getItem() {
        return item;
    }
    
    
    /**
     * Creates a {@code ItemParticles} builder.
     * 
     * @return the Builder
     */
    public static ItemBuilder builder() {
        return new ItemBuilder(new ItemParticles(null, null, 0, 0, 0, 0, 0));
    } 
    
    
    /**
     * Represents a builder for {@code ItemParticles}.
     */
    public static class ItemBuilder extends AbstractBuilder<ItemBuilder, ItemParticles> {

        private ItemBuilder(ItemParticles particles) {
            super(particles);
        }
        
        /**
         * Sets the {@code ItemStack}.
         * 
         * @param item the ItemStack
         * @return this
         */
        public ItemBuilder item(ItemStack item) {
            particles.item = item;
            return this;
        }

        /**
         * {@inheritDoc}
         */
        @Override
        protected ItemBuilder getThis() {
            return this;
        }
        
    }
    
}
