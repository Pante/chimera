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
package com.karuslabs.commons.animation;

import org.bukkit.*;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;


/**
 * Represents a effectively immutable geometrical helix composed of <code>Particle</code>s. 
 */
public class Helix {
    
    private double radius;
    private Particle particles;
    private int amount;
    private double height;
    private double period;
    
    
    /**
     * Constructs a <code>Helix</code> with the specified <code>ConfigurationSection</code>.
     * Obtains the values mapped to the keys from the specified <code>ConfigurationSection</code> if present; else the default values.
     * <p>
     * Valid keys and their default values are as follows: <pre>radius: 2, particles: ENCHANTMENT_TABLE, amount: 50, height: 12, period: 4</pre>. 
     * Note that the value for <pre>particles</pre> must be in all capital letters.
     * 
     * @param config the ConfigurationSection
     */
    public Helix(ConfigurationSection config) {
        this(
            config.getDouble("radius", 2),
            Particle.valueOf(config.getString("particles", "ENCHANTMENT_TABLE")),
            config.getInt("amount", 50),
            config.getDouble("height", 12),
            config.getDouble("period", 4)
        );
    }
    
    /**
     * Constructs a <code>Helix</code> with the specified radius, particles, amount, height and period.
     * 
     * @param radius the radius
     * @param particles the particles
     * @param amount the amount
     * @param height the height
     * @param period the period of the helix
     */
    public Helix(double radius, Particle particles, int amount, double height, double period) {
        this.radius = radius;
        this.particles = particles;
        this.amount = amount;
        this.height = height;
        this.period = period;
    }
    
    
    /**
     * Renders this helix to the specified <code>Player</code> only at the <code>Player</code> location.
     * 
     * @param player the player
     */
    public void render(Player player) {
        render(player, player.getLocation());
    }
    
    /**
     * Renders this helix to the specified <code>Player</code> at the specified location.
     * 
     * @param player the player
     * @param location the location
     */
    public void render(Player player, Location location) {
        double baseX = location.getX();
        double baseY = location.getY();
        double baseZ = location.getZ();
        
        for (double y = 0; y <= height; y += 0.05) {
            double x = radius * Math.cos(period * y);
            double z = radius * Math.sin(period * y);
            player.spawnParticle(particles, baseX + x, baseY + y, baseZ + z, amount);
        }
    }
    
    
    /**
     * Renders this helix at the specified location, visible to all players within range.
     * 
     * @param location the location
     */
    public void render(Location location) {
        World world = location.getWorld();
        double baseX = location.getX();
        double baseY = location.getY();
        double baseZ = location.getZ();
        
        for (double y = 0; y <= height; y += 0.05) {
            double x = radius * Math.cos(period * y);
            double z = radius * Math.sin(period * y);
            world.spawnParticle(particles, baseX + x, baseY + y, baseZ + z, amount);
        }
    }

    
    /**
     * @return the radius of this helix
     */
    public double getRadius() {
        return radius;
    }
    
    /**
     * @return the particles which this helix is composed of
     */
    public Particle getParticles() {
        return particles;
    }
    
    /**
     * @return the amount of particles to render per location
     */
    public int getAmount() {
        return amount;
    }
    
    /**
     * @return the height of this helix
     */
    public double getHeight() {
        return height;
    }

    /**
     * @return the mathematical period of the curve of this helix
     */
    public double getPeriod() {
        return period;
    }
    
}
