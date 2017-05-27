/*
 * Copyright (C) 2017 Karus Labs
 * All rights reserved.
 */
package com.karuslabs.commons.animation;

import org.bukkit.*;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;


public class Helix {
    
    private double radius;
    private Particle particles;
    private int amount;
    private double height;
    private double period;
    
    
    public Helix(ConfigurationSection config) {
        this(
            config.getDouble("radius", 2),
            Particle.valueOf(config.getString("particles", "ENCHANTMENT_TABLE")),
            config.getInt("amount", 50),
            config.getDouble("height", 12),
            config.getDouble("period", 4)
        );
    }
    
    public Helix(double radius, Particle particles, int amount, double height, double period) {
        this.radius = radius;
        this.particles = particles;
        this.amount = amount;
        this.height = height;
        this.period = period;
    }
    
    
    public void render(Player player) {
        render(player, player.getLocation());
    }
    
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

    
    public double getRadius() {
        return radius;
    }

    public Particle getParticles() {
        return particles;
    }

    public int getAmount() {
        return amount;
    }

    public double getHeight() {
        return height;
    }

    public double getPeriod() {
        return period;
    }
    
}
