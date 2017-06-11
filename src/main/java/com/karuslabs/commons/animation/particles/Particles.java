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


public abstract class Particles {
    
    protected Particle type;
    protected int amount;
    
    
    public Particles(ConfigurationSection config) {
        this(Particle.valueOf(config.getString("type")), config.getInt("amount"));
    }
    
    public Particles(Particle type, int amount) {
        this.type = type;
        this.amount = amount;
    }
    
    
    public void render(Player player) {
        render(player, player.getLocation());
    }
    
    
    public abstract void render(Player player, Location location);
    
    public abstract void render(Location location);
    
}
