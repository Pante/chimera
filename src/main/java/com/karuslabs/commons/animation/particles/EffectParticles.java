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
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;


public class EffectParticles extends Particles {
    
    protected double offsetX;
    protected double offsetY;
    protected double offsetZ;
    protected double speed;
    
    
    public EffectParticles(ConfigurationSection config) {
        this(
            Particle.valueOf(config.getString("type")), 
            config.getInt("amount"), 
            config.getDouble("offset.x"), 
            config.getDouble("offset.y"),
            config.getDouble("offset.z"),
            config.getDouble("speed")
        );
    }
    
    public EffectParticles(Particle type, int amount, double offsetX, double offsetY, double offsetZ, double speed) {
        super(type, amount);
        this.offsetX = offsetX;
        this.offsetY = offsetY;
        this.offsetZ = offsetZ;
        this.speed = speed;
    }
    
    
    @Override
    public void render(Player player, Location location) {
        player.spawnParticle(type, location, amount, offsetX, offsetY, offsetZ, speed);
    }

    @Override
    public void render(Location location) {
        location.getWorld().spawnParticle(type, location, amount, offsetX, offsetY, offsetZ, speed);
    }
    
}
