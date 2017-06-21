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

import java.util.Set;

import org.bukkit.*;
import org.bukkit.entity.Player;

import static com.google.common.collect.Sets.immutableEnumSet;

import static org.bukkit.Particle.*;


public class ColouredParticles extends Particles {
    
    private static final Set<Particle> ALLOWED = immutableEnumSet(SPELL, SPELL_INSTANT, SPELL_MOB, SPELL_MOB_AMBIENT, SPELL_WITCH);
    
    protected static Particle validate(Particle type) {
        if (ALLOWED.contains(type)) {
            return type;
            
        } else {
            throw new IllegalArgumentException("Invalid Particle: " + type);
        }
    }
    
    
    private Color colour;
    private double r, g, b;
    
    
    public ColouredParticles(Particle type, int amount, Color colour) {
        super(type, amount);
        validate(type);
        this.colour = colour;
        r = colour.getRed() / 255.0;
        g = colour.getGreen() / 255.0;
        b = colour.getBlue() / 255.0;
    }
    

    @Override
    public void render(Player player, Location location) {
        player.spawnParticle(type, location, amount, r, g, b, 1);
    }

    @Override
    public void render(Location location) {
        location.getWorld().spawnParticle(type, location, amount, r, g, b, 1);
    }
    
    
    public Color getColour() {
        return colour;
    }

    
    public static ColouredParticlesBuilder newColouredParticles() {
        return new ColouredParticlesBuilder(new ColouredParticles(Particle.SPELL, 0, Color.WHITE));
    }
    
    
    public static class ColouredParticlesBuilder extends Builder<ColouredParticlesBuilder, ColouredParticles> {

        protected ColouredParticlesBuilder(ColouredParticles particles) {
            super(particles);
        }
        
        
        @Override
        public ColouredParticlesBuilder type(Particle type) {
            particles.type = validate(type);
            return this;
        }
        
        public ColouredParticlesBuilder colour(Color colour) {
            particles.colour = colour;
            particles.r = colour.getRed() / 255.0;
            particles.g = colour.getGreen() / 255.0;
            particles.b = colour.getBlue() / 255.0;
            return this;
        }

        
        @Override
        protected ColouredParticlesBuilder getThis() {
            return this;
        }
        
    }
    
}
