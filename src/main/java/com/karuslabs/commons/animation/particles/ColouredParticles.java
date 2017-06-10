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


public class ColouredParticles extends Particles {
    
    private double r, g, b;
    private double extra;
    
    
    public ColouredParticles(ConfigurationSection config) {
        this(Particle.valueOf(config.getString("type")), config.getInt("amount"), config.getColor(config.getString("colour")));
    }
    
    public ColouredParticles(Particle type, int amount, Color color) {
        super(type, amount);
        r = color.getRed() / 255.0;
        g = color.getGreen() / 255.0;
        b = color.getBlue() / 255.0;
        extra = 1;
    }

    
    @Override
    public void render(Player player, Location location) {
        player.spawnParticle(type, location, amount, r, g, b, extra);
    }

    @Override
    public void render(Location location) {
        location.getWorld().spawnParticle(type, location, amount, r, g, b, extra);
    }
    
}
