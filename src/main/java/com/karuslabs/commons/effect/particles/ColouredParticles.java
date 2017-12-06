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

import static org.bukkit.Color.WHITE;


/**
 * A concrete subclass of {@code Particles} which additionally consist of a colour.
 * <p>
 * This class may contain {@code Particle} type(s) of {@link Particle#SPELL}, {@link Particle#SPELL_INSTANT}, 
 * {@link Particle#SPELL_MOB}, {@link Particle#SPELL_MOB_AMBIENT} and {@link Particle#REDSTONE} only.
 */
@Immutable
public class ColouredParticles extends Particles {
    
    private Color colour;
    private double r, g, b;
    
    
    /**
     * Constructs a {@code ColouredParticles} with the specified {@code Particle} type, colour and amount.
     * 
     * @param type the Particle type
     * @param colour the colour
     * @param amount the amount
     */
    public ColouredParticles(Particle type, Color colour, int amount) {
        super(type, amount);
        setColour(colour);
    }
        
    private void setColour(Color colour) {
        this.colour = colour;
        r = colour.getRed() / 255.0;
        g = colour.getGreen() / 255.0;
        b = colour.getBlue() / 255.0;
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    protected void render(World world, double x, double y, double z) {
        world.spawnParticle(particle, x, y, z, amount, r, g, b, 1);
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    protected void render(Player player, double x, double y, double z) {
        player.spawnParticle(particle, x, y, z, amount, r, g, b, 1);
    }

    
    /**
     * Returns the colour.
     * 
     * @return the colour
     */
    public Color getColour() {
        return colour;
    }
    
    
    /**
     * Creates a {@code ColouredParticles} builder.
     * 
     * @return the Builder
     */
    public static ColouredBuilder builder() {
        return new ColouredBuilder(new ColouredParticles(null, WHITE, 0));
    }

    
    
    /**
     * Represents a builder for {@code ColouredParticles}.
     */
    public static class ColouredBuilder extends Builder<ColouredBuilder, ColouredParticles> {

        private ColouredBuilder(ColouredParticles particles) {
            super(particles);
        }
        
        
        /**
         * Sets the colour.
         * 
         * @param colour the colour
         * @return this
         */
        public ColouredBuilder colour(Color colour) {
            particles.setColour(colour);
            return this;
        }
        
        /**
         * {@inheritDoc}
         */
        @Override
        protected ColouredBuilder getThis() {
            return this;
        }
        
    }
    
}
