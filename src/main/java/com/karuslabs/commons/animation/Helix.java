/*
 * Copyright (C) 2017 Karus Labs
 * All rights reserved.
 */
package com.karuslabs.commons.animation;

import org.bukkit.*;
import org.bukkit.configuration.ConfigurationSection;


public class Helix {
    
    private double radius;
    private Particle particles;
    private int thickness;
    private double height;
    private double curve;
    
    
    public Helix(ConfigurationSection config) {
        radius = config.getDouble("radius", 2);
        particles = Particle.valueOf(config.getString("particles", "ENCHANTMENT_TABLE"));
        thickness = config.getInt("thickness", 50);
        height = config.getDouble("height", 12);
        curve = config.getDouble("curve", 4);
    }
    
    
    public void render(Location location) {
        World world = location.getWorld();
        double baseX = location.getX();
        double baseY = location.getY();
        double baseZ = location.getZ();
        
        for (double y = 0; y <= height; y += 0.05) {
            double x = radius * Math.cos(curve * y);
            double z = radius * Math.sin(curve * y);
            world.spawnParticle(particles, baseX + x, baseY + y, baseZ + z, thickness);
        }
    }

    
    public double getRadius() {
        return radius;
    }

    public Particle getParticles() {
        return particles;
    }

    public int getThickness() {
        return thickness;
    }

    public double getHeight() {
        return height;
    }

    public double getCurve() {
        return curve;
    }
    
}
