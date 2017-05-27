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
 * Represents a geometrical helix composed of <pre>Particles</pre>.
 */
public class Helix {
    
    private double radius;
    private Particle particles;
    private int amount;
    private double height;
    private double period;
    
    
    /**
     * Creates a new <pre>Helix</pre> with the <pre>ConfigurationSection</pre> specified.
     * Reads the <pre>ConfigurationSection</pre> and retrieves the values associated with the keys
     * or their default values if the key is non-existent. Valid keys and their default values are
     * as follows: <pre>radius: 2, particles: ENCHANTMENT_TABLE, amount: 50, height: 12, period: 4</pre>. 
     * Note that the value for <pre>particles</pre> must be in all capital letters.
     * 
     * @param config the ConfigurationSection containing the values
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
     * Creates a new <pre>Helix</pre> with the radius, particles, amount, height and period
     * specified.
     * 
     * @param radius the radius
     * @param particles the particles the helix is composed of
     * @param amount the amount of particles per location
     * @param height the height
     * @param period the period
     */
    public Helix(double radius, Particle particles, int amount, double height, double period) {
        this.radius = radius;
        this.particles = particles;
        this.amount = amount;
        this.height = height;
        this.period = period;
    }
    
    
    /**
     * Renders the helix at the <pre>Player</pre>'s location.
     * Only visible to the <pre>Player</pre> specified.
     * 
     * @param player the player
     */
    public void render(Player player) {
        render(player, player.getLocation());
    }
    
    /**
     * Renders the helix at the <pre>Location</pre> specified.
     * Only visible to the <pre>Player</pre> specified.
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
     * Renders the helix at the <pre>Lcation</pre> specified.
     * Visible to all <pre>Player</pre>s within range.
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
     * @return the radius
     */
    public double getRadius() {
        return radius;
    }

    /**
     * @return the particles the helix is composed of
     */
    public Particle getParticles() {
        return particles;
    }

    /**
     * @return the amount of particles per location
     */
    public int getAmount() {
        return amount;
    }
    
    /**
     * @return the height
     */
    public double getHeight() {
        return height;
    }

    /**
     * @return the period
     */
    public double getPeriod() {
        return period;
    }
    
}
